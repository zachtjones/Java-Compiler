package javaLibrary;

import helper.CompileException;
import intermediate.InterFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static main.FileReader.readResourcesFile;

/***
 * This class is used to look up information from the java standard library.
 */
public class JavaLibraryLookup {

    private static final String[] lines = readResourcesFile("javaLookup.txt").split("\n");
    private static final Map<String, InterFile> javaCache = new HashMap<>();

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

        //print the access modifier(s), the extends and implements as fully qualified names
        int start = contents.indexOf("<pre>") + 5;
        int end = contents.indexOf("</pre>");
        String declaration = contents.substring(start, end);
        System.out.println(declaration);
        declaration = declaration.replace("<span class=\"typeNameLabel\">", "");
        declaration = declaration.replace("</span>", "");
        declaration = declaration.replace("\n", " ").replace("</a>", "").replace("../", "");
        declaration = declaration.replaceAll(" title=\".*?\">", " ");
        declaration = declaration.replace("<a href=\"", "").replace('/', '.');
        declaration = declaration.replace("&lt;", "<").replace("&gt;", ">");
        declaration = declaration.replaceAll("\\.\\w*\\.html\" ", ".");
        System.out.println(declaration + " {\n");

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
            String tempDec = total.substring(total.indexOf("<pre>") + 5, total.indexOf("</pre>"));
            tempDec = tempDec.replace("&nbsp;", " ").replace("../", "");
            tempDec = tempDec.replaceAll("title=\".*?\">\\w*</a>", "");
            tempDec = tempDec.replace("<a href=\"", "").replace(".html\"", "").replace('/', '.');
            tempDec = tempDec.replace("&lt;", "<").replace("&gt;", ">");
            tempDec = tempDec.replaceAll("\\s+", " ");//replace multiple white-spaces out
            if (tempDec.contains("Deprecated")) {
                continue; //skip methods, constructors, and fields that are deprecated.
            }
            // the declaration is now done, parse the text to show in the comments
            //  (useful for implementation details)
            if (!total.contains("<div")) {
                System.out.println("Error: " + fullyQualified + ": docs invalid for: " + tempDec);
                continue;
            }
            String tempDocs = total.substring(total.indexOf("<div"));
            tempDocs = tempDocs.replaceAll("<.*?>", "");
            // make tempDocs into the doc strings
            tempDocs = "\t/** " + tempDocs.replace("\n", "\n\t*") + " */";

            System.out.println(tempDocs);
            System.out.print('\t' + tempDec);
            if (tempDec.contains("(") && !tempDec.contains("abstract")) {
                // method or constructor, has {}
                System.out.println("{}");
            } else {
                System.out.println(';');
            }
            System.out.println();
        }

        System.out.println('}');
        return f;
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
