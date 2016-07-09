package com.jwkj;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import android.util.Log;
import com.yoosee.R;

public class XCamera {
	public final static int SUPPORT_SDK_VERSION = 9;
	public static int current_camera_id;

	public static int getDefaultCameraIndex() {
		int index = 0;
		if (Build.VERSION.SDK_INT >= SUPPORT_SDK_VERSION) {
			Log.e("2cu", "The current system version2: "
					+ Build.VERSION.SDK_INT);

			CameraInfo cameraInfo = new Camera.CameraInfo();
			int cameraCount = Camera.getNumberOfCameras(); // get cameras number
			for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
				Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
				if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) { // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
					index = camIdx;
				}
			}
		}

		return index;
	}

	public static Camera switchCamera() {
		Camera camera = null;
		if (Build.VERSION.SDK_INT >= SUPPORT_SDK_VERSION) {
			int cameraCount = Camera.getNumberOfCameras(); // get cameras number
			for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
				if (current_camera_id != camIdx) {
					current_camera_id = camIdx;
					try {
						camera = Camera.open(current_camera_id);
					} catch (Exception e) {
						camera = null;
					}
					break;
				}
			}
		}
		return camera;
	}

	public static Camera open() {
		Camera camera = null;
		if (Build.VERSION.SDK_INT <= SUPPORT_SDK_VERSION) {
			try {
				camera = Camera.open();
			} catch (Exception e2) {

			}
		} else {
			try {
				current_camera_id = getDefaultCameraIndex();
				camera = Camera.open(current_camera_id);
			} catch (Exception e) {

			}
		}
		return camera;
	}
}
