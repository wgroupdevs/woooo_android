package com.woooapp.meeting.lib;


import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.mediasoup.droid.Logger;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.ThreadUtils;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoDecoderFactory;
import org.webrtc.VideoEncoderFactory;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;
import org.webrtc.audio.AudioDeviceModule;
import org.webrtc.audio.JavaAudioDeviceModule;

/**
 * @author Muneeb Ahmad
 * <p>
 * File PeerConnectionUtils.java
 * Class [PeerConnectionUtils]
 * on 08/09/2023 at 5:15 pm
 */
public class PeerConnectionUtils {

    private static final String TAG = "PeerConnectionUtils";

    private static String mPreferCameraFace = "front";
    private static EglBase mEglBase = EglBase.create();

    public static EglBase.Context getEglContext() {
        return mEglBase.getEglBaseContext();
    }

    public static void setPreferCameraFace(String preferCameraFace) {
        mPreferCameraFace = preferCameraFace;
    }

    private final ThreadUtils.ThreadChecker mThreadChecker;
    private PeerConnectionFactory mPeerConnectionFactory;

    private AudioSource mAudioSource;
    private VideoSource mVideoSource;
    private VideoSource mScreeVideoSource;
    private CameraVideoCapturer mCamCapture;
    private VideoCapturer mScreenCapturer;
    private SurfaceTextureHelper surfaceTextureHelper2;

    public PeerConnectionUtils() {
        mThreadChecker = new ThreadUtils.ThreadChecker();
        Log.d(TAG, "Initialized PeerConnectionUtils()");
    }

    // PeerConnection factory creation.
    private void createPeerConnectionFactory(Context context) {
        Logger.d(TAG, "createPeerConnectionFactory()");
        mThreadChecker.checkIsOnValidThread();
        PeerConnectionFactory.Builder builder = PeerConnectionFactory.builder();
        builder.setOptions(null);

        AudioDeviceModule adm = createJavaAudioDevice(context);
        VideoEncoderFactory encoderFactory =
                new DefaultVideoEncoderFactory(mEglBase.getEglBaseContext(), true, false);
        VideoDecoderFactory decoderFactory =
                new DefaultVideoDecoderFactory(mEglBase.getEglBaseContext());

        mPeerConnectionFactory =
                builder
                        .setAudioDeviceModule(adm)
                        .setVideoEncoderFactory(encoderFactory)
                        .setVideoDecoderFactory(decoderFactory)
                        .createPeerConnectionFactory();
    }

    private AudioDeviceModule createJavaAudioDevice(Context appContext) {
        Logger.d(TAG, "createJavaAudioDevice()");
        mThreadChecker.checkIsOnValidThread();
        // Enable/disable OpenSL ES playback.
        // Set audio record error callbacks.
        JavaAudioDeviceModule.AudioRecordErrorCallback audioRecordErrorCallback =
                new JavaAudioDeviceModule.AudioRecordErrorCallback() {
                    @Override
                    public void onWebRtcAudioRecordInitError(String errorMessage) {
                        Logger.e(TAG, "onWebRtcAudioRecordInitError: " + errorMessage);
                    }

                    @Override
                    public void onWebRtcAudioRecordStartError(
                            JavaAudioDeviceModule.AudioRecordStartErrorCode errorCode, String errorMessage) {
                        Logger.e(TAG, "onWebRtcAudioRecordStartError: " + errorCode + ". " + errorMessage);
                    }

                    @Override
                    public void onWebRtcAudioRecordError(String errorMessage) {
                        Logger.e(TAG, "onWebRtcAudioRecordError: " + errorMessage);
                    }
                };

        JavaAudioDeviceModule.AudioTrackErrorCallback audioTrackErrorCallback =
                new JavaAudioDeviceModule.AudioTrackErrorCallback() {
                    @Override
                    public void onWebRtcAudioTrackInitError(String errorMessage) {
                        Log.e(TAG, "onWebRtcAudioTrackInitError: " + errorMessage);
                    }

                    @Override
                    public void onWebRtcAudioTrackStartError(
                            JavaAudioDeviceModule.AudioTrackStartErrorCode errorCode, String errorMessage) {
                        Log.e(TAG, "onWebRtcAudioTrackStartError: " + errorCode + ". " + errorMessage);
                    }

                    @Override
                    public void onWebRtcAudioTrackError(String errorMessage) {
                        Log.e(TAG, "onWebRtcAudioTrackError: " + errorMessage);
                    }
                };

        return JavaAudioDeviceModule.builder(appContext)
                .setAudioRecordErrorCallback(audioRecordErrorCallback)
                .setAudioTrackErrorCallback(audioTrackErrorCallback)
                .createAudioDeviceModule();
    }

    /**
     *
     * @return
     */
    @Nullable
    public PeerConnectionFactory getPeerConnectionFactory() {
        return this.mPeerConnectionFactory;
    }

    // Audio source creation.
    private void createAudioSource(Context context) {
        Logger.d(TAG, "createAudioSource()");
        mThreadChecker.checkIsOnValidThread();
        if (mPeerConnectionFactory == null) {
            createPeerConnectionFactory(context);
        }

        mAudioSource = mPeerConnectionFactory.createAudioSource(new MediaConstraints());
    }

    private void createCamCapture(Context context) {
        Logger.d(TAG, "createCamCapture()");
        mThreadChecker.checkIsOnValidThread();
        boolean isCamera2Supported = Camera2Enumerator.isSupported(context);
        CameraEnumerator cameraEnumerator;

        if (isCamera2Supported) {
            cameraEnumerator = new Camera2Enumerator(context);
        } else {
            cameraEnumerator = new Camera1Enumerator();
        }
        final String[] deviceNames = cameraEnumerator.getDeviceNames();
        for (String deviceName : deviceNames) {
            boolean needFrontFacing = "front".endsWith(mPreferCameraFace);
            String selectedDeviceName = null;
            if (needFrontFacing) {
                if (cameraEnumerator.isFrontFacing(deviceName)) {
                    selectedDeviceName = deviceName;
                }
            } else {
                if (!cameraEnumerator.isFrontFacing(deviceName)) {
                    selectedDeviceName = deviceName;
                }
            }

            if (!TextUtils.isEmpty(selectedDeviceName)) {
                mCamCapture =
                        cameraEnumerator.createCapturer(
                                selectedDeviceName,
                                new CameraVideoCapturer.CameraEventsHandler() {
                                    @Override
                                    public void onCameraError(String s) {
                                        Logger.e(TAG, "onCameraError, " + s);
                                    }

                                    @Override
                                    public void onCameraDisconnected() {
                                        Logger.w(TAG, "onCameraDisconnected");
                                    }

                                    @Override
                                    public void onCameraFreezed(String s) {
                                        Logger.w(TAG, "onCameraFreezed, " + s);
                                    }

                                    @Override
                                    public void onCameraOpening(String s) {
                                        Logger.d(TAG, "onCameraOpening, " + s);
                                    }

                                    @Override
                                    public void onFirstFrameAvailable() {
                                        Logger.d(TAG, "onFirstFrameAvailable");
                                    }

                                    @Override
                                    public void onCameraClosed() {
                                        Logger.d(TAG, "onCameraClosed");
                                    }
                                });
                break;
            }
        }

        if (mCamCapture == null) {
            throw new IllegalStateException("Failed to create Camera Capture");
        }
    }

    public void switchCam(CameraVideoCapturer.CameraSwitchHandler switchHandler) {
        Logger.d(TAG, "switchCam()");
        mThreadChecker.checkIsOnValidThread();
        if (mCamCapture != null) {
            mCamCapture.switchCamera(switchHandler);
        }
    }

    // Video source creation.
    @MainThread
    private void createVideoSource(Context context) {
        Logger.d(TAG, "createVideoSource()");
        mThreadChecker.checkIsOnValidThread();
        if (mPeerConnectionFactory == null) {
            createPeerConnectionFactory(context);
        }
        if (mCamCapture == null) {
            createCamCapture(context);
        }

        mVideoSource = mPeerConnectionFactory.createVideoSource(false);
        SurfaceTextureHelper surfaceTextureHelper =
                SurfaceTextureHelper.create("CaptureThread", mEglBase.getEglBaseContext());

        mCamCapture.initialize(surfaceTextureHelper, context, mVideoSource.getCapturerObserver());
        mCamCapture.startCapture(640, 480, 30);
    }

    /**
     *
     * @param context
     * @param capturer
     */
    @MainThread
    private void createScreenVideoSource(@NonNull Context context, @NonNull DisplayMetrics metrics) {
        if (mScreenCapturer == null)
            return;

        Logger.d(TAG, "createScreenVideoSource()");
        mThreadChecker.checkIsOnValidThread();
        if (mPeerConnectionFactory == null) {
            createPeerConnectionFactory(context);
        }

        mScreeVideoSource = mPeerConnectionFactory.createVideoSource(true);

        if (surfaceTextureHelper2 == null) {
            surfaceTextureHelper2 =
                    SurfaceTextureHelper.create("CaptureThread2", mEglBase.getEglBaseContext());
        }

        mScreenCapturer.initialize(surfaceTextureHelper2, context, mScreeVideoSource.getCapturerObserver());
        mScreenCapturer.startCapture(1280, 720, 30);
    }

    public void disposeCamCapturer() {
        if (mCamCapture != null) {
            try {
                mCamCapture.stopCapture();
                mCamCapture.dispose();
                mCamCapture = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (mVideoSource != null) {
            mVideoSource.dispose();
            mVideoSource = null;
        }
    }

    public void disposeScreenVideoCapturer() {
        if (mScreenCapturer != null) {
            try {
                mScreenCapturer.stopCapture();
                mScreenCapturer.dispose();
                mScreenCapturer = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (mScreeVideoSource != null) {
            try {
                mScreeVideoSource.dispose();
                mScreeVideoSource = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Audio track creation.
    public AudioTrack createAudioTrack(Context context, String id) {
        Logger.d(TAG, "createAudioTrack()");
        mThreadChecker.checkIsOnValidThread();
        if (mAudioSource == null) {
            createAudioSource(context);
        }
        return mPeerConnectionFactory.createAudioTrack(id, mAudioSource);
    }

    // Video track creation.
    public VideoTrack createVideoTrack(Context context, String id) {
        Logger.d(TAG, "createVideoTrack()");
        mThreadChecker.checkIsOnValidThread();
        if (mVideoSource == null) {
            createVideoSource(context);
        }

        return mPeerConnectionFactory.createVideoTrack(id, mVideoSource);
    }

    /**
     *
     * @param context
     * @param videoCapturer
     * @param id
     * @param displayMetrics
     * @return
     */
    public VideoTrack createScreenVideoTrack(@NonNull Context context,
                                             @NonNull VideoCapturer videoCapturer,
                                             @NonNull String id,
                                             @NonNull DisplayMetrics displayMetrics) {
        Logger.d(TAG, "createVideoTrack()");
        mThreadChecker.checkIsOnValidThread();
        if (this.mScreenCapturer == null) {
            this.mScreenCapturer = videoCapturer;
        }
        if (mScreeVideoSource == null) {
            createScreenVideoSource(context, displayMetrics);
        }
        return mPeerConnectionFactory.createVideoTrack(id, mScreeVideoSource);
    }

    public void dispose() {
        Logger.w(TAG, "dispose()");
        mThreadChecker.checkIsOnValidThread();
        if (mCamCapture != null) {
            try {
                mCamCapture.dispose();
                mCamCapture = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (mScreenCapturer != null) {
            try {
                mScreenCapturer.dispose();
                mScreenCapturer = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (mVideoSource != null) {
            try {
                mVideoSource.dispose();
                mVideoSource = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (mScreeVideoSource != null) {
            try {
                mScreeVideoSource.dispose();
                mScreeVideoSource = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (mAudioSource != null) {
            try {
                mAudioSource.dispose();
                mAudioSource = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (mPeerConnectionFactory != null) {
            try {
                mPeerConnectionFactory.dispose();
                mPeerConnectionFactory = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

} /**
 * end class [PeerConnectionUtils]
 */
