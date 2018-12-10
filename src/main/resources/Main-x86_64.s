# parameters: (in this order for integer/pointer) %rdi​,​ %rsi​, ​%rdx​, ​%rcx​, ​%r8​, and ​%r9
# return: %rax

# ​%rax​, ​%rcx​, ​%rdx​, ​%rdi​, ​%rsi​, ​%rsp​, and ​%r8-r11​ may be trashed across function calls, push/pop when necessary
# %rbx​, %rbp​, and ​%r12-r15 should be saved across function calls, if you use, must restore

# this file represents the bridge from java code to native code
#  this is simply another wrapper layer of main(String[] args), with this layer convering java String [] to the x64
#  representation.

	.text
	.global	_Java_Main_mainMethod
	.balign 16, 0x90

## javaEnv = arg0

_Java_Main_mainMethod: # (JNIEnv *, jclass, jobjectArray) -> int

    # rbx is the javaEnv *, r12 is the args.length, r13 is the counter, r14 is the array

    pushq   %rbx
    pushq   %r12
    pushq   %r13
    pushq   %r14

    movq    %rdi, %rbx                  # save java environment
    movq    %rdx, %r14                  # save args array

	movq	_javaEnv@GOTPCREL(%rip), %rax   # tempAddress = PC-relative javaEnv label
	movq	%rdi, (%rax)                # move arg1 to the address just computed

	# %rax is the java environment pointer to the function table
	movq	(%rdi), %rax

	# convert the Java String[] to int, String*

	# get the length, call: int GetArrayLength(JNIEnv *, array) : index 171 or JavaEnv*
	movq    1368(%rax), %rcx                 # rcx = JavaEnv -> 171 * pointer size

    # arg1 = javaEnv
    movq    %rbx, %rdi
    # arg2 = array
    movq    %r14, %rsi

    callq   *%rcx                       # call the JavaEnv -> 171 * 8 (javaEnv*, array)

    # eax is the length, want to return that from here too

	# callq	_main if we were actually calling the main function, for now let's just return the length

    popq    %r14
    popq    %r13
	popq    %r12
	popq    %rbx
	retq

    # common variable, _javaEnv, of size 8 bytes (pointer), aligned to 2 to the power of 3
	.comm	_javaEnv,8,3
