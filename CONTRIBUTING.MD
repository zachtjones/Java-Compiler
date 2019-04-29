# Contributing

Thank you for taking the time to contribute!

## Bug Reports

If there is a problem with this compiler, please follow these
steps to ensure that the maintainers can understand your report and
reproduce it.

1. Check to make sure there isn't already a bug report
   - This compiler is not complete, and there is functionality of the Java language not implemented yet.
     - If this is the case, there will be either an open issue related, or a message in the [plan.md](plan.md).
2. Check to make sure the source code you're using this to compile is bug-free and compiles with javac without warnings / errors.
3. Make sure you're using the same version of java of java or a later one to run the compiled program as the one used to run this compiler on your program.
4. Include any error output from this compiler, as well as any error output of the java program itself.
5. Include screenshots where appropriate.
6. Include any extra information you feel helps clarify the issue.
7. If you have any questions, feel free to email zachtjones16 \[at\] gmail.com.
   Please remember this is just a side-project of mine, and I may take several days to respond.

## Development

GitHub issues are being used for features & bugs that are planned to be implemented

1. Assign yourself to the issue
2. Create a branch with a name resembling the issue
   - push changes to that branch as often as possible to show progress & help build off of what's going on
3. When done implementing the fix / feature:
   - run the tests locally, `mvn clean test jacoco:report`
4. If all the tests pass, create a Pull request
   - This will run automated tests against your code. 
   - If the tests pass locally, but don't on the server, test on a linux x86-64 virtual machine.
     - This is what the tests run on.
     - There are some platform differences, this will help figure those out
     
While the prerequisites above must be satisfied prior to having your pull request reviewed, 
the reviewer(s) may ask you to complete additional design work, tests, or other changes before 
your pull request can be ultimately accepted.


## Debugging
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
 
