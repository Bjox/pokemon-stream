#include "vbajni_VBAJNI.h"
#include <iostream>

JNIEXPORT void JNICALL Java_vbajni_VBAJNI_sayHello(JNIEnv *, jobject)
{
	std::cout << "HELLO" << std::endl;
	return;
}