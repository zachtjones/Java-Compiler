# Java Compiler
This is a Java program used to compile Java files into native code.
Instead of using a complete java library implementation, this uses the Java Native Interface for calling
Java library methods.

The supported output languages are x86-64 and AMD64.

## Prerequisites
 - java/javac 8+ in the path
 - gcc, also in the PATH (used to assemble and link results)
   - clang may be aliased to gcc on Mac OS is there is XCode installed; this is not a problem
 - If using Intellij, there is a plugin for JavaCC; this will make editing the parser easier

### GCC / java instillation on Windows
 - For the other platforms, having the two on the path works fine for all versions I've tested
 - There might be other setups that work, but this one worked for me.
 - Note that the calling convention is different on Windows than all the other platforms
 - But for windows, make sure you use MSYS2 with MingGW-w64 installed on it:
   - https://stackoverflow.com/questions/30069830/how-to-install-mingw-w64-and-msys2/30071634#30071634
   - If the link above becomes inaccessible:
     - download msys2 from http://www.msys2.org/ (use the 64-bit version)
     - from msys2, run `pacman -S mingw-w64-x86_64-gcc`
     - from msys2, run `pacman -S mingw-w64-x86_64-gsl`
     - add `C:\path\to\msys64\mingw64\bin` to your PATH variable
     - verify gcc works by opening a new cmd.exe command prompt, and running `gcc -v`

## Contributing
 - GitHub issues are being used for features & bugs that are planned to be implemented
 - The idea for progress is as follows:
   1. Get a Hello, World program to compile to x86 assembly, invoke the GCC as to assemble & link.
   2. Make lots of test classes for basic java syntax & how it works
   3. Do some optimizations (and create issues from the list that I want to do next)
   4. Implement a bunch more of the java library (writing test classes as I go)
 - Implementing many of these features may change the structure of this program drastically, I'm not sure

## Usage
1. Install Java 8+ and Apache Maven
2. Run `mvn clean package`
3. Invoke the jar on your main source file: `java -jar target/*.jar <your java file>`
   - This will compile your java files, starting from the references from the main java class.

## Compiler passes
1. Parses the language, and builds the resulting tree from the context-free grammar.
   - package main and the javaCC generated java source files. (using JavaCC)
2. Resolve imports, and fully qualify names.
   - package tree - method resolveImports()
3. Build the symbol table, resolving the declarations of the symbols & convert to intermediate language (IL)
   - package tree - method compile()
4. Type-check the intermediate language
   - package intermediate - method typeCheck(), which may bring in other source files into compilation to this step
   - this also fully-qualifies the function calls & now all registers have a primitive/class type
5. Convert to pseudo-assembly (see below)
   - package intermediate - method compile()
6. Allocate the hardware registers
   - package x64.allocation
7. Write out the x64 assembly
8. Use the assembler to create the dynamic linked library


## Pseudo assembly
 - the only difference between this and x64 is that there are an unlimited amount of registers
 - these extra registers are preserved between function calls

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

# Debugging
- When a test program fails, it can be difficult to know why it breaks.
- If there is a crash from the compiler, then you can use your favorite IDE to debug.
- If the crash is when you do `java -Djava.library.path=. Main`, then I've followed one of these two ways:
- Option 1 (using the assembly):
   - Disassemble to the Main.dll generated `objdump -d Main.dll > Main.txt`
   - Use the assembly and the full crash log to figure out where the problem is
   - You can compare the Main.txt to find the and the dynamic linking info to find the line where it breaks if it happens in the assembly code generated
- Option 2 (using the comparable c code):
   - Copy the Main.java from the resources to this nested folder
   - Compile the java file: `javac -h . Main.java` from the resources/debug/&lt;program name&gt; folder
   - Compile the C file: `gcc "-Ipath/to/jdk/include" "-Ipath/to/jdk/include/os_folder" program.c -shared -o Main.dll -O2 --save-temps`
      - the paths include the directories necessary to find jni.h and jni_md.h
      - os_folder is darwin on Mac OS X, or win32 on Windows (even if 64 bit)
   - Run the program and make sure it works from the c code, if it does, then there's a problem with the assembly generation
   - You can examine the differences between the assembly and what this compiler does for flaws
 

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
