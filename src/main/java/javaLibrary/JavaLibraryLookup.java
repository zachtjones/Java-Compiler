package javaLibrary;

import helper.CompileException;
import helper.Types;
import intermediate.InterFile;
import intermediate.InterFunction;
import tree.NameNode;

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

    public static InterFile getLibraryFile(String fullyQualified, String fileName, int line) throws CompileException {

        // use the cache if already there
        if (javaCache.containsKey(fullyQualified)) {
            return javaCache.get(fullyQualified);
        }

        // TODO converting to using reflection instead of the network to find the classes
        //  -- more reliable and doesn't require internet connection so it should be much faster
        try {
            // binary name has '.' as the separator instead of the .class file '/', as well as $ for inner,
            // but I don't think we should have java inner classes imported (but maybe like Map.Entry)
            Class<?> classFound = JavaLibraryLookup.class.getClassLoader()
                .loadClass(fullyQualified.replace('/','.'));

            Class<?> superClass = classFound.getSuperclass();
            Class<?>[] interfacesImplemented = classFound.getInterfaces();
            Field[] fields = classFound.getDeclaredFields();
            Method[] methods = classFound.getMethods();

            InterFile result = new InterFile(fullyQualified, superClass.getCanonicalName());



            // interfaces
            ArrayList<NameNode> interfaces = new ArrayList<>();
            for (Class<?> implemented : interfacesImplemented) {
                NameNode interfaceNode = new NameNode(fileName, line);
                interfaceNode.primaryName = implemented.getCanonicalName();
                interfaces.add(interfaceNode);
            }
            result.setImplements(interfaces);

            // fields -- since this is just used for type checking,
            // we don't need to know the field's initial value if it has one
            for (Field field : fields) {
                boolean isStatic = Modifier.isStatic(field.getModifiers());
                result.addField(Types.fromReflection(field.getType()), field.getName(), isStatic);
            }

            // methods -- again don't need to know the contents, just the signatures
            for (Method method : methods) {
                InterFunction function = new InterFunction(fullyQualified);
                function.name = method.getName();
                function.returnType = Types.fromReflection(method.getReturnType());

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
