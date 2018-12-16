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
    pushq   %r14                        # args java/lang/String[]
    pushq   %r15                        # args char*[]

    movq    %rdi, %rbx                  # save java environment
    movq    %rdx, %r14                  # save args array

	# get the length, call: int GetArrayLength(JNIEnv *, array) : index 171 or JavaEnv*
	movq	(%rbx), %rax
	movq    1368(%rax), %rcx            # rcx = JavaEnv -> 171 * pointer size

    # arg1 = javaEnv, which is already there
    # arg2 = array
    movq    %r14, %rsi

    callq   *%rcx                       # call the JavaEnv -> 171 * 8 (javaEnv*, array)

    # return value is the length, store it as upper bound
    movq    %rax, %r12

    # r15 = malloc(length * 8)
    movq    %rax, %rdi
    salq    $3, %rdi                    # arg1 = length << 3 (same as length * 8)
    call    _malloc
    movq    %rax, %r15                  # r15 = new char*[length]

    movq    $0, %r13                    # counter = 0
start_while_loop:
    # condition
    cmpq   %r13, %r12                   # FLAGS = conditions(counter, length)
    je      end_while_loop

    # body: get the string @ index, then convert to char*

    # get the string @ index
    # index 173, jobject GetObjectArrayElement(JNIEnv *env, jobjectArray array, jsize index);

    movq    (%rbx), %rax
    movq    1384(%rax), %rcx

    movq    %rbx, %rdi                  # arg1 = javaEnv
    movq    %r14, %rsi                  # arg2 = args java array
    movq    %r13, %rdx                  # index = counter

    callq   *%rcx                       # call to JNI -> rax is the java/lang/String

    # store returned value in r9
    movq    %rax, %r9


    # convert it to null terminated char*
    # index 169, char* GetStringUTFChars(JNIEnv *env, jstring string, jboolean *isCopy);

    movq    (%rbx), %rax
    movq    1352(%rax), %rcx

    movq    %rbx, %rdi                  # arg1 = javaEnv
    movq    %r9, %rsi                   # arg2 = returned string from getArrayValue
    movq    $0, %rdx                    # arg3 = NULL for don't copy

    callq   *%rcx                       # call to JNI -> rax is the char*

    # store rax @ r15 + counter * 8
    movq    %rax, (%r15, %r13, 8)

    # increment & goto top
    incq    %r13
    jmp     start_while_loop
end_while_loop:

    # int main(int argc, char*[] argv)
    movq    %r12, %rdi                  # arg1 = args.length
    movq    %r15, %rsi
	callq	_main

	# return value is held there, return to Java

    popq    %r15
    popq    %r14
    popq    %r13
	popq    %r12
	popq    %rbx
	retq

