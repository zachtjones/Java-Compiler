
// this file is a minimal example of a c function that isn't the main,
//  used to tell if the gcc can create and link with the JVM

#include <stdio.h>

// declaration from header, just trying to make it one file
void Java_Main_mainMethod();


// might want to add  __attribute__ ((visibility ("default"))),
//   which is the same as JNIEXPORT if the dynamic link fails
void Java_Main_mainMethod() {

	puts("hello from native code");
}
