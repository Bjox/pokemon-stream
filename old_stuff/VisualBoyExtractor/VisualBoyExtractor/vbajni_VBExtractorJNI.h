/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class vbajni_VBExtractorJNI */

#ifndef _Included_vbajni_VBExtractorJNI
#define _Included_vbajni_VBExtractorJNI
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     vbajni_VBExtractorJNI
 * Method:    readWRAM
 * Signature: ([B)V
 */
JNIEXPORT jboolean JNICALL Java_vbajni_VBExtractorJNI_readWRAM
  (JNIEnv *, jobject, jbyteArray);

/*
 * Class:     vbajni_VBExtractorJNI
 * Method:    openProcess
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_vbajni_VBExtractorJNI_openProcess
  (JNIEnv *, jobject);

/*
 * Class:     vbajni_VBExtractorJNI
 * Method:    closeProcess
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_vbajni_VBExtractorJNI_closeProcess
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif