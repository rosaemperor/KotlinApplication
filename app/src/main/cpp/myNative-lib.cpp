//
// Created by administrator on 2018/6/13.
//

#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_net_lanlingdai_kotlinapplication_MainActivity_stringMyJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from MyJNI";
    return env->NewStringUTF(hello.c_str());
}