# parameters: (in this order for integer/pointer) %rdi​,​ %rsi​, ​%rdx​, ​%rcx​, ​%r8​, and ​%r9
# return: %rax

# ​%rax​, ​%rcx​, ​%rdx​, ​%rdi​, ​%rsi​, ​%rsp​, and ​%r8-r11​ may be trashed across function calls, push/pop when necessary
# %rbx​, %rbp​, and ​%r12-r15 should be saved across function calls, if you use, must restore

# this file represents the bridge from java code to native code
#  this is simply another wrapper layer of main(String[] args), with this layer convering java String [] to the x64
#  representation.

# You can't cache the JNIEnv*, since it changes based on threading.
# However, we only use it here to convert the String[] to the char*[]

	.text
	.global	_Java_Main_mainMethod
	.balign 16, 0x90

## javaEnv = arg0

_Java_Main_mainMethod: # (JNIEnv *, jclass, jobjectArray) -> int

    pushq   %rbx                        # javaEnv
    pushq   %r12                        # args.length
    pushq   %r13                        # counter
    pushq   %r14                        # args param


    movq    %rdi, %rbx                  # save java environment
    movq    %rdx, %r14                  # save args array

	movq	_javaEnv@GOTPCREL(%rip), %rax   # tempAddress = PC-relative javaEnv label
	movq	%rdi, (%rax)                # move arg1 to the address just computed


	# get the length, call: int GetArrayLength(JNIEnv *, array) : index 171 or JavaEnv*
	movq	(%rbx), %rax
	movq    1368(%rax), %rcx                 # rcx = JavaEnv -> 171 * pointer size

    # arg1 = javaEnv, which is already there
    # arg2 = array
    movq    %r14, %rsi

    callq   *%rcx                       # call the JavaEnv -> 171 * 8 (javaEnv*, array)

    # return value is the length, store it as upper bound
    movq    %rax, %r12
    movq    $0, %r13                   # counter = 0
start_while_loop:
    # condition
    cmpq   %r13, %r12                 # FLAGS = conditions(counter, length)
    je      end_while_loop

    # body: get the string @ index, convert it to null terminated char*


    # increment & goto top
    incq    %r13
    jmp     start_while_loop
end_while_loop:

	# callq	_main if we were actually calling the main function, for now let's just return the length

    popq    %r14
    popq    %r13
	popq    %r12
	popq    %rbx
	retq

    # common variable, _javaEnv, of size 8 bytes (pointer), aligned to 2 to the power of 3
	.comm	_javaEnv,8,3
