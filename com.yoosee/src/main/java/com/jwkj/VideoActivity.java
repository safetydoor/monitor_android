package com.jwkj;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yoosee.R;
import com.jwkj.activity.MainActivity;
import com.jwkj.global.Constants;
import com.jwkj.global.MyApp;
import com.jwkj.utils.PhoneWatcher;
import com.jwkj.utils.T;
import com.p2p.core.BaseVideoActivity;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PView;

public class VideoActivity extends BaseVideoActivity implements OnClickListener {
	private final int DEFAULT_FRAME_RATE = 15;
	public static final int P2P_SURFACE_START_PLAYING_WIDTH = 320;
	public static final int P2P_SURFACE_START_PLAYING_HEIGHT = 240;
	Context mContext;
	boolean isRegFilter = false;
	boolean mIsCloseMike = false;
	boolean isYV12 = false;
	int type;
	ImageView switch_camera, hungup, close_mike;
	PhoneWatcher mPhoneWatcher;

	Camera mCamera;
	SurfaceHolder mHolder;
	SurfaceView local_surface_camera;
	ImageView mask_p2p_view, mask_camera;
	private boolean cameraIsShow = true;
	private boolean mPreviewRunning = false;
	private H264Encoder mEncoder;

	RelativeLayout control_bottom;
	boolean isControlShow = true;

	private int mWindowWidth, mWindowHeight;
	boolean isReject = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		P2PConnect.setPlaying(true);
		Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		setContentView(R.layout.p2p_video);
		type = this.getIntent().getIntExtra("type", -1);
		mContext = this;
		initComponent();
		regFilter();
		startWatcher();

		openCamera();
	}

	public void initComponent() {
		pView = (P2PView) findViewById(R.id.pView);
		this.initP2PView(P2PConnect.getCurrentDeviceType());

		switch_camera = (ImageView) findViewById(R.id.switch_camera);
		hungup = (ImageView) findViewById(R.id.hungup);
		close_mike = (ImageView) findViewById(R.id.close_mike);
		local_surface_camera = (SurfaceView) findViewById(R.id.local_surface_camera);
		mask_camera = (ImageView) findViewById(R.id.mask_camera);

		mask_p2p_view = (ImageView) findViewById(R.id.mask_p2p_view);
		control_bottom = (RelativeLayout) findViewById(R.id.control_bottom);

		mask_camera.setOnTouchListener(onTouch);
		local_surface_camera.setOnTouchListener(onTouch);

		switch_camera.setOnClickListener(this);
		hungup.setOnClickListener(this);
		close_mike.setOnClickListener(this);

		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();

		mWindowWidth = dm.widthPixels;
		mWindowHeight = dm.heightPixels;
		AbsoluteLayout.LayoutParams params1 = (AbsoluteLayout.LayoutParams) local_surface_camera
				.getLayoutParams();
		AbsoluteLayout.LayoutParams params2 = (AbsoluteLayout.LayoutParams) mask_camera
				.getLayoutParams();
		params1.x = mWindowWidth - params1.width;
		local_surface_camera.setLayoutParams(params1);

		params2.x = mWindowWidth - params2.width;
		mask_camera.setLayoutParams(params2);

	}

	@Override
	public void onHomePressed() {
		// TODO Auto-generated method stub
		super.onHomePressed();
		this.reject();
	}

	private void startWatcher() {
		mPhoneWatcher = new PhoneWatcher(mContext);
		mPhoneWatcher
				.setOnCommingCallListener(new PhoneWatcher.OnCommingCallListener() {

					@Override
					public void onCommingCall() {
						// TODO Auto-generated method stub
						reject();
					}

				});
		mPhoneWatcher.startWatcher();
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.P2P.P2P_REJECT);
		filter.addAction(Constants.P2P.P2P_CHANGE_IMAGE_TRANSFER);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		mContext.registerReceiver(mReceiver, filter);
		isRegFilter = true;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if (intent.getAction().equals(Constants.P2P.P2P_REJECT)) {
				reject();
			} else if (intent.getAction().equals(
					Constants.P2P.P2P_CHANGE_IMAGE_TRANSFER)) {
				int state = intent.getIntExtra("state", -1);
				if (state == 0) {
					mask_p2p_view.setVisibility(RelativeLayout.GONE);
				} else if (state == 1) {
					mask_p2p_view.setVisibility(RelativeLayout.VISIBLE);
				} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
					reject();
				}
			}

		}
	};

	OnTouchListener onTouch = new OnTouchListener() {
		boolean isActive = false;
		long downTime;
		int mWidth, mHeight;
		float downWidth;
		float downHeight;

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			// TODO Auto-generated method stub
			int y = (int) event.getY();
			AbsoluteLayout.LayoutParams params1 = (AbsoluteLayout.LayoutParams) local_surface_camera
					.getLayoutParams();
			AbsoluteLayout.LayoutParams params2 = (AbsoluteLayout.LayoutParams) mask_camera
					.getLayoutParams();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downTime = System.currentTimeMillis();
				mWidth = params1.width;
				mHeight = params1.height;
				downWidth = event.getRawX() - params1.x;
				downHeight = event.getRawY() - params1.y;
				isActive = true;
				break;
			case MotionEvent.ACTION_UP:
				isActive = false;
				if ((System.currentTimeMillis() - downTime) < 100) {
					if (cameraIsShow) {
						if (VideoActivity.this.closeLocalCamera()) {
							cameraIsShow = false;
							mask_camera.setVisibility(RelativeLayout.VISIBLE);
							local_surface_camera
									.setVisibility(RelativeLayout.GONE);
						}
					} else {
						if (VideoActivity.this.openLocalCamera()) {
							cameraIsShow = true;
							mask_camera.setVisibility(RelativeLayout.GONE);
							local_surface_camera
									.setVisibility(RelativeLayout.VISIBLE);
						}
					}
				}
				break;
			case MotionEvent.ACTION_MOVE:
				Log.e("my", "rawxy:" + event.getRawX() + ":" + event.getRawY());

				int changeX = (int) (event.getRawX() - downWidth);
				if (changeX < 0) {
					changeX = 0;
				} else if (changeX > (mWindowWidth - mWidth)) {
					changeX = mWindowWidth - mWidth;
				}

				int changeY = (int) (event.getRawY() - downHeight);
				if (changeY < 0) {
					changeY = 0;
				} else if (changeY > (mWindowHeight - mHeight)) {
					changeY = mWindowHeight - mHeight;
				}
				params1.x = changeX;
				params1.y = changeY;
				local_surface_camera.setLayoutParams(params1);

				params2.x = changeX;
				params2.y = changeY;
				mask_camera.setLayoutParams(params2);
				break;
			}
			return true;
		}

	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int resId = v.getId();
		if (resId == R.id.switch_camera) {
			swtichCamera();
		} else if (resId == R.id.hungup) {
			this.reject();
		} else if (resId == R.id.close_mike) {
			if (!mIsCloseMike) {
				close_mike.setImageResource(R.drawable.btn_no_sound_s);
				this.setMute(true);
				mIsCloseMike = true;
			} else {
				close_mike.setImageResource(R.drawable.btn_no_sound);
				this.setMute(false);
				mIsCloseMike = false;
			}
		} else if (resId == R.id.local_surface_camera) {
			if (cameraIsShow) {
				if (this.closeLocalCamera()) {
					Log.e("my", "close camera");
					cameraIsShow = false;
					mask_camera.setVisibility(RelativeLayout.VISIBLE);
					local_surface_camera.setVisibility(RelativeLayout.GONE);
				}
			}
		} else if (resId == R.id.mask_camera) {
			if (!cameraIsShow) {
				if (this.openLocalCamera()) {
					Log.e("my", "open camera");
					cameraIsShow = true;
					mask_camera.setVisibility(RelativeLayout.GONE);
					local_surface_camera.setVisibility(RelativeLayout.VISIBLE);
				}
			}
		}
	}

	public void changeControl() {
		if (isControlShow) {
			isControlShow = false;
			// Animation anim2 = AnimationUtils.loadAnimation(this,
			// R.anim.slide_out_top);
			Animation anim2 = AnimationUtils.loadAnimation(this,
					android.R.anim.fade_out);
			anim2.setDuration(200);
			control_bottom.startAnimation(anim2);
			control_bottom.setVisibility(RelativeLayout.GONE);

		} else {
			isControlShow = true;
			control_bottom.setVisibility(RelativeLayout.VISIBLE);
			// Animation anim2 = AnimationUtils.loadAnimation(this,
			// R.anim.slide_in_bottom);
			Animation anim2 = AnimationUtils.loadAnimation(this,
					android.R.anim.fade_in);
			anim2.setDuration(200);
			control_bottom.startAnimation(anim2);

		}
	}

	private void swtichCamera() {
		try {
			if (Camera.getNumberOfCameras() < 2) {
				return;
			}

			releaseCamera();
			mCamera = XCamera.switchCamera();
			if (null != mCamera) {
				mCamera.setPreviewDisplay(mHolder);
				Camera.Parameters parameters = mCamera.getParameters();

				List<Integer> LRates = parameters
						.getSupportedPreviewFrameRates();
				int iFrameRateTmp = 1;
				int iDiff = DEFAULT_FRAME_RATE;
				int iNewRate = 0;
				for (int i = 0; i < LRates.size(); i++) {
					iNewRate = LRates.get(i);
					if (iFrameRateTmp > DEFAULT_FRAME_RATE)
						iDiff = iFrameRateTmp - DEFAULT_FRAME_RATE;
					else
						iDiff = DEFAULT_FRAME_RATE - iFrameRateTmp;
					if (iDiff == 0)
						break;

					if (iNewRate <= DEFAULT_FRAME_RATE
							&& (DEFAULT_FRAME_RATE - iNewRate) < iDiff) {
						iFrameRateTmp = iNewRate;
					} else if (iNewRate > DEFAULT_FRAME_RATE
							&& (iNewRate - DEFAULT_FRAME_RATE) < iDiff) {
						iFrameRateTmp = iNewRate;
					}
				}
				if (iFrameRateTmp > DEFAULT_FRAME_RATE / 2
						|| (iFrameRateTmp < DEFAULT_FRAME_RATE * 3 / 2)) {
					mVideoFrameRate = iFrameRateTmp;
					parameters.setPreviewFrameRate(mVideoFrameRate);
				}

				parameters.setPreviewSize(P2P_SURFACE_START_PLAYING_WIDTH,
						P2P_SURFACE_START_PLAYING_HEIGHT);
				parameters.set("orientation", "landscape");
				setFormat(parameters);
				mCamera.setDisplayOrientation(0);
				mCamera.setParameters(parameters);
				mEncoder = new H264Encoder(mCamera.getParameters()
						.getPreviewSize().width, mCamera.getParameters()
						.getPreviewSize().height);
				mCamera.setPreviewCallback(mEncoder);
				mCamera.startPreview();
				mPreviewRunning = true;
			}
		} catch (Exception e) {
			releaseCamera();
		}

	}

	private void setFormat(Camera.Parameters p) {
		List<Integer> supportList = null;
		supportList = p.getSupportedPreviewFormats();
		if (supportList == null || supportList.size() == 0) {
			return;
		}
		int format = -1;
		for (int i = 0; i < supportList.size(); i++) {
			int test = supportList.get(i);
			Log.e("my", test + "");

		}

		for (int i = 0; i < supportList.size(); i++) {
			format = supportList.get(i);
			if (ImageFormat.NV21 == format) {
				p.setPreviewFormat(ImageFormat.NV21);
				isYV12 = false;
				break;
			} else if (ImageFormat.YV12 == format) {
				p.setPreviewFormat(ImageFormat.YV12);
				isYV12 = true;
				break;
			}

		}

	}

	@Override
	public void onBackPressed() {
		reject();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (isRegFilter) {
			mContext.unregisterReceiver(mReceiver);
			isRegFilter = false;
		}
		if (null != mPhoneWatcher) {
			mPhoneWatcher.stopWatcher();
		}
		P2PConnect.setPlaying(false);

		if (!activity_stack
				.containsKey(Constants.ActivityInfo.ACTIVITY_MAINACTIVITY)) {
			Intent i = new Intent(this, MainActivity.class);
			this.startActivity(i);
		}

		Intent refreshContans = new Intent();
		refreshContans.setAction(Constants.Action.REFRESH_CONTANTS);
		mContext.sendBroadcast(refreshContans);
	}

	public void openCamera() {
		mHolder = local_surface_camera.getHolder();
		mHolder.addCallback(new LocalCameraCallBack());
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		local_surface_camera.setZOrderOnTop(true);
	}

	public synchronized void releaseCamera() {
		if (mCamera != null) {
			Log.e("p2p", "releaseCamera");
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	class H264Encoder implements Camera.PreviewCallback {

		private boolean mIsWritingData = false;

		public H264Encoder(int width, int height) {
		};

		@Override
		public void onPreviewFrame(final byte[] data, Camera camera) {
			if (data != null && !mIsWritingData) {
				mIsWritingData = true;
				Parameters p = camera.getParameters();

				Log.e("debug",
						p.getPreviewSize().width + ":::::"
								+ p.getPreviewSize().height);
				if (cameraIsShow) {
					if (isYV12) {
						VideoActivity.this.fillCameraData(data, data.length,
								p.getPreviewSize().width,
								p.getPreviewSize().height, 1);
					} else {
						VideoActivity.this.fillCameraData(data, data.length,
								p.getPreviewSize().width,
								p.getPreviewSize().height, 0);
					}

				}
				mIsWritingData = false;
			}
		}
	}

	class LocalCameraCallBack implements SurfaceHolder.Callback,
			Camera.PictureCallback {

		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			if (mPreviewRunning) {
				mCamera.stopPreview();
			}
			try {
				if (null != mCamera) {
					Camera.Parameters parameters = mCamera.getParameters();

					List<Integer> LRates = parameters
							.getSupportedPreviewFrameRates();
					int iFrameRateTmp = 1;
					int iDiff = DEFAULT_FRAME_RATE;
					int iNewRate = 0;
					for (int i = 0; i < LRates.size(); i++) {
						iNewRate = LRates.get(i);
						if (iFrameRateTmp > DEFAULT_FRAME_RATE)
							iDiff = iFrameRateTmp - DEFAULT_FRAME_RATE;
						else
							iDiff = DEFAULT_FRAME_RATE - iFrameRateTmp;
						if (iDiff == 0)
							break;

						if (iNewRate <= DEFAULT_FRAME_RATE
								&& (DEFAULT_FRAME_RATE - iNewRate) < iDiff) {
							iFrameRateTmp = iNewRate;
						} else if (iNewRate > DEFAULT_FRAME_RATE
								&& (iNewRate - DEFAULT_FRAME_RATE) < iDiff) {
							iFrameRateTmp = iNewRate;
						}
					}
					if (iFrameRateTmp > DEFAULT_FRAME_RATE / 2
							|| (iFrameRateTmp < DEFAULT_FRAME_RATE * 3 / 2)) {
						mVideoFrameRate = iFrameRateTmp;
						parameters.setPreviewFrameRate(mVideoFrameRate);
					}

					List<Size> supportedPreviewSizes = parameters
							.getSupportedPreviewSizes();

					for (Size size : supportedPreviewSizes) {
						Log.e("debug", size.width + ":" + size.height);
					}
					parameters.setPreviewSize(P2P_SURFACE_START_PLAYING_WIDTH,
							P2P_SURFACE_START_PLAYING_HEIGHT);
					parameters.set("orientation", "landscape"); //

					setFormat(parameters);
					mCamera.setDisplayOrientation(0);
					mCamera.setParameters(parameters); // 将Camera.Parameters设定予Camera
					mEncoder = new H264Encoder(mCamera.getParameters()
							.getPreviewSize().width, mCamera.getParameters()
							.getPreviewSize().height);
					mCamera.setPreviewCallback(mEncoder);
					mCamera.startPreview(); // 打开预览画面
					mPreviewRunning = true;
				}

			} catch (Exception e) {
				T.showShort(mContext, R.string.camera_error);
				releaseCamera();
			}

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			try {
				mCamera = XCamera.open();
				mCamera.setPreviewDisplay(mHolder);
			} catch (Exception e) {
				if (null != mCamera) {
					releaseCamera();
				}
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			releaseCamera();
		}

	}

	public void reject() {
		if (!isReject) {
			isReject = true;
			P2PHandler.getInstance().reject();
			releaseCamera();
			finish();
		}
	}

	@Override
	public int getActivityInfo() {
		// TODO Auto-generated method stub
		return Constants.ActivityInfo.ACTIVITY_VIDEOACTIVITY;
	}

	@Override
	protected void onP2PViewSingleTap() {
		// TODO Auto-generated method stub
		changeControl();
	}

	@Override
	protected void onGoBack() {
		// TODO Auto-generated method stub
		MyApp.app.showNotification();
	}

	@Override
	protected void onGoFront() {
		// TODO Auto-generated method stub
		MyApp.app.hideNotification();
	}

	@Override
	protected void onExit() {
		// TODO Auto-generated method stub
		MyApp.app.hideNotification();
	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), R.string.Press_again,
						Toast.LENGTH_SHORT).show();
				T.showShort(mContext, R.string.Press_again);
				exitTime = System.currentTimeMillis();
			} else {
				reject();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
