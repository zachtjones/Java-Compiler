package intermediate;

import conversions.Conversion;
import helper.CompileException;
import helper.Types;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import x64.X64File;

import java.util.*;
import java.util.stream.Collectors;

import static helper.Types.STRING;
import static helper.Types.fromFullyQualifiedClass;

public class InterFile {
	@NotNull private final String name;
	@NotNull private final String superClass;
	@NotNull private final ArrayList<String> implementsClasses;

	@NotNull private final InterStructure staticPart;
	@NotNull private final InterStructure instancePart;
	@NotNull private final ArrayList<InterFunction> functions;

	/**
	 * Creates an intermediate file, given the name
	 * @param name The name, such as java/util/Scanner
	 * @param superClass The name of the superclass, same form as name.
	 */
	public InterFile(@NotNull String name, @NotNull String superClass,
					 @NotNull ArrayList<String> implementsClasses) {

		this.name = name;
		this.superClass = superClass;
		this.implementsClasses = implementsClasses;
		this.staticPart = new InterStructure(false);
		this.instancePart = new InterStructure(true);
		this.functions = new ArrayList<>();
	}

	/**
	 * Adds a field to the intermediate file
	 * @param type The string that is the type of the field
	 * @param name The identifier of the field
	 * @param isStatic true if the field is static, 
	 * false if it is instance-based.
	 */
	public void addField(@NotNull Types type, @NotNull String name, boolean isStatic) {
		addField(type, name, isStatic, null);
	}

	/**
	 * Adds a field to the intermediate file
	 * @param type The string that is the type of the field
	 * @param name The identifier of the field
	 * @param isStatic true if the field is static, 
	 * @param value The default value of the field.
	 * false if it is instance-based.
	 */
	public void addField(@NotNull Types type, @NotNull String name, boolean isStatic, @Nullable String value) {
		if (isStatic) {
			this.staticPart.addMember(type, name, value);
		} else {
			this.instancePart.addMember(type, name, value);
		}
	}

	/** Gets the file's name (with package, using '/') as a String. */
	@NotNull
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("// file: ");
		result.append(name);
		result.append(".jil\n\n");

		// super class
		result.append("super ");
		result.append(this.superClass);
		result.append('\n');

		// interfaces
		if (!implementsClasses.isEmpty()) {
			result.append("implements ");
			result.append(
				String.join(", ", implementsClasses)
			);
			result.append('\n');
		}

		// instance structure
		result.append(instancePart.toString());

		// static structure
		result.append(staticPart.toString());

		// functions
		for (InterFunction f : functions) {
			result.append(f.toString());
		}

		return result.toString();
	}

	/**
	 * Adds an intermediate file function to the list.
	 * @param func The IL function.
	 */
	public void addFunction(@NotNull InterFunction func) {
		this.functions.add(func);
	}

	/**
	 * Generates the default constructor if needed.
	 */
	public void generateDefaultConstructor(@NotNull String fileName, int line) {

		boolean hasOne = functions.stream().anyMatch(InterFunction::isConstructor);

		if (!hasOne) {
			InterFunction d = new InterFunction(name, "<init>", Types.VOID);
			// add the name and return type
			d.isInstance = true;

			// TODO call to super (that's pretty complicated since the test classes extend object)
			// in order to call a superclass that is part of the java library, have to do some sort of
			//  re-arrangement of the field order

			// add the only statement, super();
			//CallActualStatement c;
			//Register[] args = new Register[0];

			// load this pointer (call super on this)
			//RegisterAllocator ra = new RegisterAllocator();
			//Register thisPointer = ra.getNext(fromFullyQualifiedClass(this.name));
			//d.statements.add(new GetParamStatement(thisPointer, "this", fileName, line));
			//Register voidReg = ra.getNext(Types.VOID);
			//  superclass of this object.
			// add in the call to it's init
			//CallActualStatement c = new CallActualStatement(thisPointer, superNode.primaryName, "<init>",
			// args, voidReg, fileName, line);
			//d.statements.add(c);
			functions.add(d);
		}
	}

	/** Returns if there is a main method. */
	public boolean hasMainMethod() {
		// name is main, it has 1 arg, type is String[].
		return functions.stream()
			.filter(f -> f.name.equals("main"))
			.filter(f -> f.paramTypes.size() == 1)
			.anyMatch(f -> f.paramTypes.get(0).equals(Types.arrayOf(STRING)));
	}

	/** Type checks all the functions
	 * @throws CompileException If there is an error with type checking.*/
	public void typeCheck() throws CompileException {

		for (InterFunction f : functions) {
			f.typeCheck(fromFullyQualifiedClass(name));
		}

	}

	/**
	 * Gets the type of the field for this structure
	 * @param fieldName The field's name
	 * @return The JIL representation of the type
	 * @throws CompileException if the field doesn't exist, or there is a problem checking it.
	 */
	@NotNull
	Types getInstFieldType(String fieldName, String fileName, int line) throws CompileException {
		return instancePart.getFieldType(fieldName, fileName, line);
	}

	/**
	 * Gets the type of the field for this class static field.
	 * @param fieldName The field's name
	 * @return The JIL representation of the type
	 * @throws CompileException if the field doesn't exist, or there is a problem checking it.
	 */
	@NotNull
	Types getStatFieldType(String fieldName, String fileName, int line) throws CompileException {
		return staticPart.getFieldType(fieldName, fileName, line);
	}

	/**
	 * Finds the method with the signature that matches.
	 * The destArgs will have their types filled in to the match, and the returned data class will have
	 * the necessary instructions to convert everything over.
	 * @param name The method's name.
	 * @param args The array of arguments.
	 * @param destArgs The destination arguments, these should be unique for each call
	 * @return The return object type, or null if there's no method with that signature
	 */
	@NotNull
	public MethodMatch getReturnType(@NotNull String name, @NotNull List<Register> args,
									 @NotNull List<Register> destArgs,
									 @NotNull String fileName, int line) throws CompileException {

		// TODO support varargs

		// overloading methods -- decided which one to take at compile time

		// functions number of args -> matching functions
		//   the mapping list will never be empty
		//   the key is the number of args that are implicitly converted.

		// TODO there is probably a better way to do this -- the param types should be mutable since they
		//  get changed, but don't want them to be changed for a non-match.

		HashMap<Integer, List<MethodMatch>> matches = new HashMap<>();

		// iterate through all, finding the count of arguments that are convertible
		functions.stream()
			.filter(f -> f.name.equals(name))
			.filter(f -> f.paramTypes.size() == args.size())
			.forEach(f -> {
				try {
					ArrayList<List<InterStatement>> conversionsToArgs = new ArrayList<>();
					int numDifferences = 0;
					for (int i = 0; i < args.size(); i++) {
						Register source = args.get(i);
						Types destType = f.paramTypes.get(i);
						Register destination = destArgs.get(i);
						destination.setType(destType);

						// not the exact same, increment it
						//   we will need a more specific way for java.io.PrintStream#println(S)
						//     should map to the int version, but long, float, and double match
						//     with the same number of differences
						if (!source.getType().equals(destType)) {
							numDifferences++;
						}

						// capture the conversion
						conversionsToArgs.add(Conversion.methodInvocation(source, destination, fileName, line));
					}
					// the args match -- add it into the list
					List<MethodMatch> values = matches.getOrDefault(numDifferences, new ArrayList<>());
					values.add(new MethodMatch(conversionsToArgs, f));
					matches.putIfAbsent(numDifferences, values);

					// if we have the exception thrown, it means it's not a match, so don't add it.
				} catch (CompileException ignored){}
			});


		final String goalSignature = name + "(" +
			args.stream()
				.map(r -> r.getType().getIntermediateRepresentation())
				.collect(Collectors.joining()) + ")";


		// no function matching this
		if (matches.isEmpty()) {
			throw new CompileException("no method found with signature, " + goalSignature
				+ ", referenced", fileName, line);
		}

		// could be multiple functions that have the same number of differences
		//  in this case it's an ambiguous method call
		TreeSet<Integer> integers = new TreeSet<>(matches.keySet());

		// potential list of candidates -- these ones would work, all are convertible
		List<MethodMatch> candidates = matches.get(integers.first());

		// find the first one, that one will be it if there's no ties
		MethodMatch first = candidates.get(0);

		if (candidates.size() != 1) {

			// evaluate specificity, through the ordering when sorted
			//  can't use a set as there might be ones of equal specificity
			// the most specific are first (smallest value in compareTo)
			Collections.sort(candidates);

			// we know candidates.size() >= 2, so compare the first 2
			//   if equal, then stop, there's an ambiguous method call
			first = candidates.get(0);
			MethodMatch second = candidates.get(1);

			if (first.compareTo(second) == 0) {
				final MethodMatch firstOne = first;
				// determine the equal ones
				List<MethodMatch> options = candidates.stream()
					.filter(i -> i.compareTo(firstOne) == 0)
					.collect(Collectors.toList());

				// construct message from the list of options
				String message = options.stream().map(
					f -> name + "(" + f.match.paramTypes.stream()
						.map(Types::getIntermediateRepresentation)
						.collect(Collectors.joining()) + ")"
				).collect(Collectors.joining(", "));
				throw new CompileException("Ambiguous method call " + goalSignature + " matches: " + message,
					fileName, line);
			}

			// we know it is the first one, which we already set.

		}

		for (int i = 0; i < destArgs.size(); i++) {
			destArgs.get(i).setType(first.match.paramTypes.get(i));
		}

		return first;
	}

	/**
	 * Creates an X64 file representing this intermediate file.
	 * @return The X64 assembly file.
	 * @throws CompileException If there is a problem converting the IL to the assembly.
	 */
	@NotNull
	public X64File compileX64() throws CompileException {

		// TODO use the inheritance to build to function tables

		X64File compiled = new X64File(this.name, staticPart);

		for (InterFunction function : functions) {
			function.compile(compiled);
		}

		return compiled;
	}

	/** Returns the offset, in bytes for the fieldName within the structure. */
	int getFieldOffset(String fieldName) {
		return instancePart.getFieldOffset(fieldName);
	}

	/** Returns the size of the instance field table in bytes */
	int getClassSize() {
		return instancePart.getTotalSize();
	}
}
