# Java Compiler

<p>
    AWS Linux build details:
    <img src="https://codebuild.us-east-1.amazonaws.com/badges?uuid=eyJlbmNyeXB0ZWREYXRhIjoiZlQycVNFMGV4NEdDOGI4MWo1TDBFVXFERWdVZk80WCtGUnV1MHZtcmQvRVlXQ0g3eCtvaDlsK3FwRVRNQjF0Zm5GVHdSTDlNaU5hbWQ1ZmNqMGdyN3hBPSIsIml2UGFyYW1ldGVyU3BlYyI6IjVHWHdEY09EZjlicVlYUUYiLCJtYXRlcmlhbFNldFNlcmlhbCI6MX0%3D&branch=master" alt="aws build status"/>
    <img src="https://s3.amazonaws.com/java-compiler-badges/coverageReport.svg" alt="aws coverage number" />
</p>

This is a Java program used to compile Java files into native code.
Instead of using a complete java library implementation, this uses the Java Native Interface for calling
Java library methods.

The supported output language is x86-64 / AMD64.

These output languages covers both Intel's 64-bit and AMD's 64-bit
desktop computers, and this works on Linux, Mac, and Windows machines.

> Don't use this compiler for mission-critical systems. Oracle's `javac` program is much more likely to work properly.

## Prerequisites
 - running an x86-64 / AMD64 - based platform (virtual machines will work too)
 - java/javac 8+ in the path
 - gcc, also in the PATH (used to assemble and link results)
   - clang may be aliased to gcc on Mac OS is there is XCode installed; this is not a problem
 - apache maven installed and in the path (command-line mvn)

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

## Usage
1. Run `mvn clean package`
2. Invoke the jar on your main source file: `java -jar target/*.jar <your java file>`
   - This will compile your java files, starting from the references from the main java class.

## Contributing
 - See [Contributing.md](CONTRIBUTING.MD)
 
# Code organization

Main code is in the src/main/java folder.
 - conversions: Represents the type conversions present in the java language, and the steps required to convert between types.
 - helper: Classes that are used throughout many different steps are included here.
 - intermediate: Classes that represent statements

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
   - package x64.allocation and method allocate in all pseudo instructions
7. Write out the x64 assembly
8. Use the assembler to create the dynamic linked library


## Pseudo assembly
 - the only difference between this and x64 is that there are an unlimited amount of registers
 - these extra registers are preserved between function calls

## Tests
- there are tests involving several of the language features implemented so far.
- these compare the output of running java with the dynamic linked library created.
- use `mvn test jacoco:report` to get the code coverage report as well.

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
- arrays
- generics

## Current known bugs
- Nested generics (ex: <code>HashMap&lt;String, ArrayList&lt;String&gt;&gt;</code>) have to be written without the <code>&gt;&gt;</code>
  - write instead as <code>HashMap&lt;String, ArrayList&lt;String&gt; &gt;</code>
  - nested generics makes the grammar not context-free, so this might be hard to implement
