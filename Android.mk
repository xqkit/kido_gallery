LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := $(call all-java-files-under, app/src/main/java)
LOCAL_RESOURCE_DIR := $(addprefix $(LOCAL_PATH)/, app/src/main/res)
#LOCAL_DEX_PREOPT := false
LOCAL_PACKAGE_NAME := KidoGallery

LOCAL_STATIC_JAVA_LIBRARIES := \
        android-support-v4 \
        android-support-v7-appcompat
LOCAL_AAPT_FLAGS := \
    --auto-add-overlay \
    --extra-packages android.support.v7.appcompat

LOCAL_CERTIFICATE := platform
include $(BUILD_PACKAGE)

include $(call all-makefiles-under,$(LOCAL_PATH))