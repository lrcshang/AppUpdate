LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := diff
LOCAL_SRC_FILES := blocksort.c	bzip2.c	bzip2recover.c	bzlib.c	bzlib.h	bzlib_private.h	compress.c	com_bobo_utils_DiffUtils.c	com_bobo_utils_DiffUtils.h	com_bobo_utils_PatchUtils.c	com_bobo_utils_PatchUtils.h	crctable.c	decompress.c dlltest.c	huffman.c	randtable.c spewG.c
LOCAL_ALLOW_UNDEFINED_SYMBOLS := true
include $(BUILD_SHARED_LIBRARY)