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

> Don't use this compiler for mission-critical systems. 
> Oracle's `javac` program is much more likely to work properly.

This project is a side-project on mine that I designed to be a large, long-term,
difficult project to combine my skills in various areas of computer science.

I learned MIPS assembly in a class at school, and wanted to apply skills learned there
to learn another assembly language implemented in the desktop computing hardware.

This project combines both of those goals, while also doing something that isn't overdone
in the computing industry.

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

## Naming:

Node: classes with this 'Node' in the name are used to represent nodes in the parse tree.

Statement: classes with this suffix or in the intermediate package be an abstraction of low level operations, and are designed
 to be analogous to javac's bytecode. A crucial difference is these are for an abstract register machine, but bytecode is for a stack machine.

x64: Throughout the code, in naming, I use x64 to refer to the 64-bit Intel/AMD assembly constructs.

Pseudo: Classes with this name or in the x64.pseudo package are used to represent an abstraction of actual x64 instructions,
but with unlimited registers. This stage was designed to reduce the difficult of converting intermediate language to actual assembly.
This way, the intermediate classes don't have to worry about hardware register allocation.

Register: a location to store a temporary value; used in calculations.
  There are 3 different types of registers in this program:
  - intermediate.Register - used in the intermediate language representing parts of calculations
  - X64PseudoRegister - used to represent operands of calculations in assembly, but not allocated to specific hardware one
  - X64Register - used in hardware calculations; these are %rax, %rbx, %rcx, %rdx, %rsi, %rdi, %rbp, %rsp, %rax, and %r8 - %r15.

Instruction: these represent instructions that can be directly 
 executed by the host CPU. These are written in assembly form, 
 and used by gcc to create assemble and link.

Directive: these represent things the assembler should do to the
 resulting object file (where to place code, 
 what bytes to write into data segment, ...)

## Main project code

Main code is in the src/main/java folder.
 - conversions: Represents the type conversions present in the java language, and the steps required to convert between types.
 - helper: Classes that are used throughout many different steps are included here.
 - intermediate: Classes that represent statements in the intermediate language designed and implemented by this compiler.
 - javaLibrary: Uses reflection to type-check java library function calls.
 - main: The class that has the parser, as well as the entry point for this program.
 - tree: Classes that represent parts of the parse tree. 
 - x64: Classes used to represent 64-bit Intel/AMD assembly. These are broken up into:
   - allocation: Classes to decide how to turn pseudo instructions into real ones.
   - directives: Directions for the assembler on how to write object code.
   - instructions: Instructions with hardware registers for the CPU to run.
   - jni: Java Native Interface operations; used for interacting with the JVM for either JDK API calls or program management.
   - operands: Operands used in instructions / pseudo instructions; what those operate on (registers, memory, ...)
   - pseudo: Classes to represent an almost x64 assembly, but with unlimited registers.

## Testing code

The testing code is in src/test/java folder.

There are several programs designed to test the functionality of this compiler located in `src/main/resources/test-programs`.

C versions of some of the java code is located in the `src/main/resources/debug` folder. 
GCC's outputted assembly should be similar to the output of this compiler (will be significantly different when optimizations aren't on).

Tests are run by maven using `mvn test`.
A code coverage report is generated by issuing: `mvn test jacoco:report`

# Code steps overview
1. Reads the source java files, and parses them into a tree, using a context free grammar and the javaCC source.
   - [JavaCC](https://javacc.org/) is a parser generator tool, and is similar to Bison, Yacc, ANTLR, and many others. 
   - JavaCC generates LL(k) parser based on recursive descent. This is different than the shift-reduce tables
     generated by other parser generators.
2. Resolve the imports and fully qualify names
   - tree classes `resolveImports` method
   - example: String -\> java/lang/String
   - simplifies finding class definitions and usages later on
3. Compile to intermediate language
   - uses the tree classes `compile` method
   - the program is now in a structure with statements that happen sequentially and labels / jumps
   - uses a symbol table to keep track of variables and class names in scope
4. Type checks the intermediate language
   - uses the intermediate `typeCheck` method.
   - brings other needed files into compilation, which will run them up to this step
   - fills in the types of all registers and operations
   - inserts the necessary statements to perform sign-extension, zero-extension, casts, and other conversions.
   - this is performed after generating intermediate language as we need to know what the names mean.
5. Compile to pseudo-assembly
   - uses the intermediate `compile` method
   - a sequence of pseudo-instructions are generated from each intermediate language statement.
   - an unlimited amount of registers are used so intermediate language is independent of the number and restrictions
     in the actual hardware.
6. Allocate pseudo registers to hardware registers
   - uses the x64.allocation classes and the `allocate` method in x64.pseudo classes
   - combines usage count with scenarios like whether it's used across function calls to determine
     either which hardware register gets used, or in the case that there's not enough spills some to the stack.
   - determining which registers should be spilled to the stack is not an easy problem, and right now GCC will outperform this compiler
7. Writes the assembly code to files
   - GCC needs to operate on files on the hard drive,
      although I could use redirection to gcc standard input to eliminate the need to wait for file writes
8. Invokes GCC to assemble the code into object files and link them to create a DLL
   - the outputted file is platform specific:
     - .dll on Windows
     - .dylib on Mac OS
     - .so on Linux
   - these all function similarly, and are dynamically linked with the java when it runs the 
