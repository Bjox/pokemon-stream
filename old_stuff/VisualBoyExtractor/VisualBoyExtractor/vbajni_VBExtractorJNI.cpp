#include "vbajni_VBExtractorJNI.h"
#include "VBAProcess.h"


VBAProcess vbaProcessInstance;


JNIEXPORT jboolean JNICALL Java_vbajni_VBExtractorJNI_readWRAM
(JNIEnv* env, jobject obj, jbyteArray jwramBuffer)
{
	jsize buffLen = env->GetArrayLength(jwramBuffer);
	if (buffLen < 0x40000) {
		return JNI_FALSE;
	}
	if (!vbaProcessInstance.isOpen()) {
		return JNI_FALSE;
	}

	jbyte* wramBuffer = env->GetByteArrayElements(jwramBuffer, NULL);
	vbaProcessInstance.readWRAM(wramBuffer, buffLen);
	env->ReleaseByteArrayElements(jwramBuffer, wramBuffer, NULL);

	return JNI_TRUE;
}


JNIEXPORT jboolean JNICALL Java_vbajni_VBExtractorJNI_openProcess
(JNIEnv *, jobject)
{
	return vbaProcessInstance.open();
}


JNIEXPORT jboolean JNICALL Java_vbajni_VBExtractorJNI_closeProcess
(JNIEnv *, jobject)
{
	return vbaProcessInstance.close();
}