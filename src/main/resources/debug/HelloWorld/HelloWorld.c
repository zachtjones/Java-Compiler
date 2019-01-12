
#include <jni.h>
#include <stdio.h>
#include "Main.h"

JNIEXPORT void JNICALL Java_Main_mainMethod(JNIEnv* env, jclass clazz, jobjectArray array) {

	// get static field out of java/lang/System
	jclass system = (*env)->FindClass(env, "java/lang/System");
	jfieldID out_id = (*env)->GetStaticFieldID(env, system, "out", "Ljava/io/PrintStream;");
	jobject out = (*env)->GetStaticObjectField(env, system, out_id);

	// call virtual out.println() -> void
	jclass printstream = (*env)->GetObjectClass(env, out);
	jmethodID println_id = (*env)->GetMethodID(env, printstream, "println", "(Ljava/lang/String;)V");
	jstring printout = (*env)->NewStringUTF(env, "Hello, world");
	(*env)->CallVoidMethod(env, out, println_id, printout);
}
