package javaLibrary;

import helper.CompileException;
import helper.Types;
import intermediate.InterFile;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static main.FileReader.readResourcesFile;

/***
 * This class is used to look up information from the java standard library.
 */
public class JavaLibraryLookup {

    @NotNull private static final String[] lines = readResourcesFile("javaLookup.txt").split("\n");
    @NotNull private static final Map<String, InterFile> javaCache = new HashMap<>();

    static {
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replace('/', '.');
        }
    }

    /** Returns the list of fully qualified names of the classes, enums, and interfaces in the package. */
    @NotNull
    public static List<String> getClassesInPackage(@NotNull String packageName) {
        return Arrays.stream(lines)
                .filter(line -> line.startsWith(packageName))
                .collect(Collectors.toList());
    }

    @NotNull
    public static InterFile getLibraryFile(@NotNull String fullyQualified, @NotNull String fileName, int line)
            throws CompileException {

        // use the cache if already there
        if (javaCache.containsKey(fullyQualified)) {
            return javaCache.get(fullyQualified);
        }

        //  -- more reliable and doesn't require internet connection so it should be much faster
        try {
            // binary name has '.' as the separator instead of the .class file '/', as well as $ for inner,
            // but I don't think we should have java inner classes imported (but maybe like Map.Entry)
            Class<?> classFound = JavaLibraryLookup.class.getClassLoader()
                .loadClass(fullyQualified.replace('/','.'));

            String superClass = classFound.getSuperclass().getCanonicalName();

            Class<?>[] interfacesImplemented = classFound.getInterfaces();
            // interfaces
            ArrayList<String> interfaces = new ArrayList<>();
            for (Class<?> implemented : interfacesImplemented) {
                interfaces.add(implemented.getCanonicalName());
            }

            Field[] fields = classFound.getDeclaredFields();
            Method[] methods = classFound.getMethods();
            Constructor[] constructors = classFound.getConstructors();

            InterFile result = new InterFile(fullyQualified, superClass, interfaces);

            // fields -- since this is just used for type checking,
            // we don't need to know the field's initial value if it has one
            for (Field field : fields) {
                boolean isStatic = Modifier.isStatic(field.getModifiers());
                result.addField(Types.fromReflection(field.getType()), field.getName(), isStatic);
            }

            // constructors of the class
            for (Constructor constructor : constructors) {
                InterFunction function = new InterFunction(fullyQualified, "<init>", Types.VOID);

                ArrayList<Types> arguments = new ArrayList<>();
                for (Class<?> argType : constructor.getParameterTypes()) {
                    arguments.add(Types.fromReflection(argType));
                }
                function.paramTypes = arguments;

                // there isn't any information on the names of the arguments, it's just about the order
                //  I still need valid names though, so arg0..n works
                ArrayList<String> argNames = new ArrayList<>();
                for (int i = 0; i < arguments.size(); i++) {
                    argNames.add("arg" + i);
                }
                function.paramNames = argNames;
                result.addFunction(function);

                // TODO also add the throws list when those are added
            }

            // methods -- again don't need to know the contents, just the signatures
            // with methods, have to eliminate the ones that are inherited
            HashMap<String, Method> methodsMap = new HashMap<>();

            // the last one listed in the array is the one that we want for each signature
            for (Method method : methods) {
                // name(args,...) works for uniquely identifying each pair, don't want the ones of the parent
                //  class showing up unless there aren't any matches.
                String identifier = method.getName() + "(" +
                    Arrays.stream(method.getParameterTypes())
                        .map(Class::getName)
                        .collect(Collectors.joining(",")
                        ) + ")";

                methodsMap.put(identifier, method);
            }

            for (Method method : methodsMap.values()) {
                // fill in the info into the intermediate function
                Types returnType = Types.fromReflection(method.getReturnType());
                InterFunction function = new InterFunction(fullyQualified, method.getName(), returnType);

                ArrayList<Types> arguments = new ArrayList<>();
                for (Class<?> argType : method.getParameterTypes()) {
                    arguments.add(Types.fromReflection(argType));
                }
                function.paramTypes = arguments;

                // there isn't any information on the names of the arguments, it's just about the order
                //  I still need valid names though, so arg0..n works
                ArrayList<String> argNames = new ArrayList<>();
                for (int i = 0; i < arguments.size(); i++) {
                    argNames.add("arg" + i);
                }
                function.paramNames = argNames;
                result.addFunction(function);
                // TODO throws list when exceptions are added to this compiler
            }

            result.typeCheck();

            javaCache.put(fullyQualified, result);
            return result;

        } catch (ClassNotFoundException e) {
            throw new CompileException("Couldn't find class: " + fullyQualified + ", referenced ", fileName, line);
        }
    }
}
