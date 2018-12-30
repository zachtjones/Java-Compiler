
# this file represents the bridge from java code to native code
#  this is simply another wrapper layer of main(String[] args), with this layer calling the compiled main

    .global $NAME$

.text
.global	    _Java_Main_mainMethod
.balign     16, 0x90

_Java_Main_mainMethod: # (JNIEnv *, jclass, jobjectArray) -> int

    pushq   %rbx  # have to push an odd number to keep stack aligned

	callq	$NAME$

	popq    %rbx
	retq

