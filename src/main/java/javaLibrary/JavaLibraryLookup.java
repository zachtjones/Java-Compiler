package javaLibrary;

import helper.CompileException;
import intermediate.InterFile;
import intermediate.InterFunction;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static main.FileReader.readResourcesFile;

/***
 * This class is used to look up information from the java standard library.
 */
public class JavaLibraryLookup {

    private static final String[] lines = readResourcesFile("javaLookup.txt").split("\n");
    private static final Map<String, InterFile> javaCache = new HashMap<>();

    private static final Pattern urlAndNamePattern = Pattern.compile("<a href=\"(.*?)\" title=\".*?\">.*?</a> (.*?)");
    private static final Pattern urlPattern = Pattern.compile("<a href=\"(.*?)\" title=\".*?\">.*?</a>");

    static {
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replace('/', '.');
        }
    }

    /** Returns the list of fully qualified names of the classes, enums, and interfaces in the package. */
    public static List<String> getClassesInPackage(String packageName) {
        return Arrays.stream(lines)
                .filter(line -> line.startsWith(packageName))
                .collect(Collectors.toList());
    }

    public static InterFile getLibraryFile(String fullyQualified) throws CompileException {

        // use the cache if already there
        if (javaCache.containsKey(fullyQualified)) {
            return javaCache.get(fullyQualified);
        }

        // TODO use the java dir to store the IL, speeding up consecutive iterations

        InterFile result = parseDocFile(fullyQualified, getNetworkFile(fullyQualified));
        javaCache.put(fullyQualified, result);
        return result;
    }

    private static InterFile parseDocFile(String fullyQualified, String contents) {

        // no sense generating the java code, then parsing & converting to IL, just build to IL as we go

        InterFile f = new InterFile(fullyQualified);

        System.out.println("Parsing javadoc: " + fullyQualified);

        // TODO - use the access modifiers, currently not part of IL
        final int startAccessModifiers = contents.indexOf("<pre>") + 5;
        final int endAccessModifiers = contents.indexOf("<span", startAccessModifiers);
        String[] classModifiers = contents.substring(startAccessModifiers, endAccessModifiers).split(" ");
        //System.out.println("Modifiers: " + Arrays.toString(classModifiers));

        // TODO parse the extends / implements documentation

        int fieldsConsFuncsStart = contents.indexOf("<h3>Field Detail</h3>");
        for (int currIndex = contents.indexOf("<h4>", fieldsConsFuncsStart);
             currIndex > -1;
             currIndex = contents.indexOf("<h4>", currIndex + 1)) {

            // process the field/constructor/method
            if (contents.indexOf("</div>", currIndex) == -1) {
                continue;
            }
            String total = contents.substring(currIndex, contents.indexOf("</div>", currIndex));
            if (!total.contains("<pre>")) {
                continue;
            }

            final String fieldOrMethod = total.substring(total.indexOf("<pre>") + 5, total.indexOf("</pre>"));
            // non-breaking space between modifiers and type name(args)
            final String[] split = fieldOrMethod.split("&nbsp;");
            final String[] modifiers = split[0].split(" ");

            if (split.length == 2) { // is a field
                Matcher matcher = urlAndNamePattern.matcher(split[1]);
                if (matcher.matches()) {
                    final String url = matcher.group(1);
                    final String type = url.replace(".html", "")
                            .replace("../", "");

                    final String name = matcher.group(2);
                    boolean isStatic = Arrays.asList(modifiers).contains("static");
                    f.addField(type, name, isStatic);
                }
            } else {

                final InterFunction function = new InterFunction();
                function.returnType = split[1];
                boolean isStatic = Arrays.asList(modifiers).contains("static");
                function.isInstance = !isStatic;

                final String rest;

                if (split[1].contains("(")) { // constructor, return type is the fully qualified name
                    rest = String.join(" ", Arrays.copyOfRange(split, 1, split.length));

                    // return type is the class, name <init>
                    function.returnType = fullyQualified;
                    function.name = "<init>";

                } else {
                    // get a sub-array, removing the first 2 entries to just show the arguments and throws list
                    rest = String.join(" ", Arrays.copyOfRange(split, 2, split.length));

                    // name is up to the first (
                    function.name = rest.substring(0, rest.indexOf('('));
                }

                addArgumentsToFunction(function, rest);

                addThrowsListToFunction(function, rest);

                f.addFunction(function);
            }
        }
        return f;
    }

    /**
     * Adds the function's throws list to the intermediate function.
     * If there is no throws list, this will not affect the function.
     * @param function The function to add the list to.
     * @param rest The string which represents the javadoc to convert to this list
     */
    private static void addThrowsListToFunction(InterFunction function, String rest) {
        final String throwsList = rest
                .substring(rest.indexOf(')') + 1)
                .trim()
                .replace("throws ", "");

        for (String throwingName : throwsList.split(",")) {
            Matcher matcher = urlPattern.matcher(throwingName);
            if (matcher.matches()) {
                final String url = matcher.group(1);
                final String type = url.replace(".html", "")
                        .replace("../", "");

                function.throwsList.add(type);
            }
        }
    }

    /**
     * Adds the function's arguments list to the intermediate function.
     * If there is no arguments, this will not affect the function.
     * @param function The function to add the list to.
     * @param rest The string which represents the javadoc to convert to this list
     */
    private static void addArgumentsToFunction(InterFunction function, String rest) {
        final String args = rest.substring(rest.indexOf('(') + 1, rest.indexOf(')'));

        for (String arg : args.split(",")) {

            Matcher matcher = urlAndNamePattern.matcher(arg.trim());
            if (matcher.matches()) {
                final String url = matcher.group(1);
                final String type = url.replace(".html", "")
                        .replace("../", "");

                final String name = matcher.group(2);
                function.paramTypes.add(type);
                function.paramNames.add(name);
            }
        }
    }

    /**
     * Downloads the javadoc html file for the supplied class name
     * @param javaClassName The fully qualified java class, such as java/lang/String
     * @return The javadoc file's contents.
     * @throws CompileException If there is a network error
     */
    private static String getNetworkFile(String javaClassName) throws CompileException {
        final String file = String.format("https://docs.oracle.com/javase/8/docs/api/%s.html", javaClassName);
        try {
            URL url = new URL(file);
            Scanner s = new Scanner(url.openStream());
            StringBuilder allContent = new StringBuilder();
            while (s.hasNextLine()) {
                allContent.append(s.nextLine());
                allContent.append("\n");
            }
            s.close();
            return allContent.toString();
        } catch (IOException e) {
            throw new CompileException("Could not download javadoc file: " + file, e, file, -1);
        }
    }
}
