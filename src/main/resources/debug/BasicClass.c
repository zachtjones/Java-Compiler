
#include <jni.h>
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

    // BasicClass.setA(1L);
    setA(env, 1);

    // system.out.print("a is: ");
//    jclass system1 = (*env)->FindClass(env, "java/lang/System");
//    jfieldID out_id1 = (*env)->GetStaticFieldID(env, system1, "out", "Ljava/io/PrintStream;");
//    jobject out1 = (*env)->GetStaticObjectField(env, system1, out_id1);
//
//    jclass printstream1 = (*env)->GetObjectClass(env, out1);
//    jmethodID print_id = (*env)->GetMethodID(env, printstream1, "print", "(Ljava/lang/String;)V");
//    jstring a_is = (*env)->NewStringUTF(env, "a is: ");
//    (*env)->CallVoidMethod(env, out1, print_id, a_is);

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