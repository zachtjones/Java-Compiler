
#include <jni.h>
#include <stdlib.h>
#include "Main.h"

long long BasicClass_a;

struct _BasicClass {
    void* function_table;
    long long b;
};
// BasicClass is a pointer to the structure
typedef struct _BasicClass* BasicClass;

// instance methods
void setB(JNIEnv* env, BasicClass this, long long c) {
    this->b = c;
}

long long getB(JNIEnv* env, BasicClass this) {
    return this->b;
}

// class methods
void setA(JNIEnv* env, long long c) {
    BasicClass_a = c;
}

long long getA(JNIEnv* env) {
    return BasicClass_a;
}

// public static void main(String[] args)...
JNIEXPORT void JNICALL Java_Main_mainMethod(JNIEnv* env, jclass clazz, jobjectArray array) {

    // these two lines are part of the allocate class statement
    BasicClass temp = (BasicClass) malloc(16);
    // temp->vtable = BasicClass_v_table;

    // note this isn't a virtual function call
    // TODO make it a virtual function call -- when it gets implemented in the compiler itself
    setB(env, temp, 5);

    // BasicClass.setA(1L);
    setA(env, 1);

    // system.out.print("b is: ");
    jclass system3 = (*env)->FindClass(env, "java/lang/System");
    jfieldID out_id3 = (*env)->GetStaticFieldID(env, system3, "out", "Ljava/io/PrintStream;");
    jobject out3 = (*env)->GetStaticObjectField(env, system3, out_id3);

    jclass printstream3 = (*env)->GetObjectClass(env, out3);
    jmethodID print_id2 = (*env)->GetMethodID(env, printstream3, "print", "(Ljava/lang/String;)V");
    jstring b_is = (*env)->NewStringUTF(env, "b is: ");
    (*env)->CallVoidMethod(env, out3, print_id2, b_is);

    // system.out.println(getB(env, temp));
    jclass system4 = (*env)->FindClass(env, "java/lang/System");
    jfieldID out_id4 = (*env)->GetStaticFieldID(env, system4, "out", "Ljava/io/PrintStream;");
    jobject out4 = (*env)->GetStaticObjectField(env, system4, out_id4);

    jclass printstream4 = (*env)->GetObjectClass(env, out4);
    jmethodID println_id2 = (*env)->GetMethodID(env, printstream4, "println", "(J)V");

    // TODO make this a virtual function call as well
    long long result = getB(env, temp);

    (*env)->CallVoidMethod(env, out3, println_id2, result);

    // system.out.print("a is: ");
    jclass system1 = (*env)->FindClass(env, "java/lang/System");
    jfieldID out_id1 = (*env)->GetStaticFieldID(env, system1, "out", "Ljava/io/PrintStream;");
    jobject out1 = (*env)->GetStaticObjectField(env, system1, out_id1);

    jclass printstream1 = (*env)->GetObjectClass(env, out1);
    jmethodID print_id = (*env)->GetMethodID(env, printstream1, "print", "(Ljava/lang/String;)V");
    jstring a_is = (*env)->NewStringUTF(env, "a is: ");
    (*env)->CallVoidMethod(env, out1, print_id, a_is);

    // system.out.println(BasicClass.getA());
    jclass system2 = (*env)->FindClass(env, "java/lang/System");
    jfieldID out_id2 = (*env)->GetStaticFieldID(env, system2, "out", "Ljava/io/PrintStream;");
    jobject out2 = (*env)->GetStaticObjectField(env, system2, out_id2);

    jclass printstream2 =(*env)->GetObjectClass(env, out2);
    jmethodID println_id = (*env)->GetMethodID(env, printstream2, "println", "(J)V");
    (*env)->CallVoidMethod(env, out2, println_id, getA(env));
}

// example to compile on Windows:
// javac -h . Main.java
// gcc -shared --save-temps -O2 "-IC:\Program Files\Java\jdk-11.0.1\include" "-IC:\Program Files\Java\jdk-11.0.1\include\win32" BasicClass.c -o Main.dll