# Java Compiler
This is a java program used to compile Java files, outputting native x86-64 code instead
of .class files. This compiler is far from implementing all of java, and requires
an implementation of all the java.* library to fully work.

## Introduction
This program is written in Java, with the use of JavaCC as the parser.
The purpose of this project is not to replace the JVM, but instead demonstrate an
understanding of how a high level language can actually run on hardware.
This program is not complete, but does the following:
  1. Parses java code
  2. Converts the AST to a register-based intermediate language
     - this language was designed to be a high level abstraction of common assembly languages
  3. Type checks the resulting intermediate language
  4. Prints out the intermediate language

## Contributing
 - GitHub issues are being used for features & bugs that are planned to be implemented
 - The idea for progress is as follows:
   1. Get a Hello, World program to compile to x86 assembly, invoke the GCC as to assemble & link.
   2. Make lots of test classes for basic java syntax & how it works
   3. Do some optimizations (and create issues from the list that I want to do next)
   4. Implement a bunch more of the java library (writing test classes as I go)
 - Implementing many of these features may change the structure of this program drastically, I'm not sure

## Usage
1. ```make```
  - builds the source files
2. ```java JavaCompiler <main source file>```
  - invokes the compiler on the main source file
  - this compiles the file, and all files that it depends on

## Compiler passes
1. Parses the language, and builds the resulting tree from the context-free grammar.
  - classes: JavaParser.jj and generated java source files. (using JavaCC)
2. Resolve imports, and fully qualify names.
  - classes: tree/\*.java - method resolveImports()
3. Build the symbol table, and resolve the declarations of the symbols
  - classes: tree/\*.java - method resolveSymbols()
4. Compile to intermediate language (IL) code. (hopefully pass 4 only)

## Optimizations (done)
The key to most of these optimizations is to keep the IL in SSA form.
SSA - Single static assignment - in function, only assign to register once.
This makes some optimizations work in linear time that wouldn't work otherwise.
- None yet

## Optimizations (planned)
- optimize out multiple reads to final variables
  - try to put final static fields in instructions
- Jumps to labels with jump statements
- Dead code elimination (use control graphs)
- Common subexpression elimination
  - involves copy and constant propagation
  - if functions/expressions are used, need to know if they are pure (no side effects)
- Return statement Optimizations
  - put return value into the correct register from the start
  - eliminates a bunch of copy register
- Constant folding
- Register allocation (Assembly - minimize save / restore operations needed)
- Register allocation (IL - use smaller numbers)
- RISC -> CISC Optimizations
  - grouping several instructions into one
- remove useless statements
  - code that assigns to a value not used & expression is side-effect free
- replace instructions with faster ones
  - x \*= 2 -> x << 1
  - x = 0 with x ^= x
  - and many more

## Optimizations (potential)
- Tail recursion -> iterative
- maybe even tail sibling calls
  - (calls to functions which take and return the same types as the caller)
- Loop Optimizations
  - loop unrolling for small # iteration loops
  - move parts of a condition out
    - (ex if going through string and length doesn't change)
  - move constant parts of expressions out of the loop
- instruction scheduling to avoid pipeline stalls
- minimize cache misses (might need to be done on high level code & involve benchmarks)


## Tests
- Currently the only 'test' is the HelloWorld.java in tests/
- plans are to compare the javac and java ClassName vs java JavaCompiler & ./ClassName output to be compared
- this is really the only way to test the features, especially as optimizations become more prevalent


## Java Features not implemented (parsing phase)
You may get really strange error messages if you use any of these.
- For...in loops (ex: for (Integer e : list) { ... }  )
- Final local variables
- Diamond for generics  (ex: <code>ArrayList<String> s = new ArrayList<>()</code>)
- Nested classes
- Annotations (currently treated as comments)
- Lambdas
- Method references


## Java Features not implemented (compile phase)
These ones will give an error like: try statement not implemented yet.
- native methods
- synchronized methods
- try ... catch blocks
- switch statements
- break and continue
- labeled statements (used in break and continue)
- array initializer (using the {} syntax: a = new int[] { 2, 5 }; )

## Current known bugs
- Nested generics (ex: <code>HashMap&lt;String, ArrayList&lt;String&gt;&gt;</code>) have to be written without the <code>&gt;&gt;</code>
  - write instead as <code>HashMap&lt;String, ArrayList&lt;String&gt; &gt;</code>
  - nested generics makes the grammar not context-free, so this might be hard to implement
