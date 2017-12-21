package com.sat.satcontorl.fragment.displayserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Handler;
import android.view.SurfaceView;

import com.sat.satcontorl.Setting;
import com.sat.satcontorl.utils.LogUtils;

public class DisPlayServerMode {

	protected static final String TAG = "DisPlayServerMode";

	private String serverIp;
	private MediaCodec mCodec;
	private Socket dataSocket;
	private DataOutputStream dos;
	private boolean isRun = true;
	private int failCount = 0;
	private CallBack callBack;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Setting.HandlerGlod.CONNET_SUCCESS:

				mHandler.sendEmptyMessageDelayed(
						Setting.HandlerGlod.START_SHOW_DATA, 5000);
				break;

			case Setting.HandlerGlod.CONNECT_FAIL:
				// isRun = false;
				if (callBack != null) {
					callBack.fila();
				}
				break;

			case Setting.HandlerGlod.CLEAR_FAILCOUNT:
				failCount = 0;
				break;

			case Setting.HandlerGlod.START_SHOW_DATA:
				if (callBack != null) {
					callBack.disPlayRemoteDesk();
				}
				break;

			default:
				break;
			}
		};
	};

	public DisPlayServerMode(String serverIp) {
		this.serverIp = serverIp;
	}

	public void startDisPlayRomoteDesk(SurfaceView mSurfaceView,
			CallBack callBack) {
		this.callBack = callBack;
		callBack.Loading();
		initDecoder(mSurfaceView);
		startServer();
	}

	private void initDecoder(SurfaceView mSurfaceView) {

		try {
			mCodec = MediaCodec
					.createDecoderByType(Setting.MediaCodecGlod.MIME_TYPE);

			final MediaFormat format = MediaFormat.createVideoFormat(
					Setting.MediaCodecGlod.MIME_TYPE,
					Setting.MediaCodecGlod.VIDEO_WIDTH,
					Setting.MediaCodecGlod.VIDEO_HEIGHT);
			format.setInteger(MediaFormat.KEY_BIT_RATE,
					Setting.MediaCodecGlod.FRAME_BIT_RATE);
			format.setInteger(MediaFormat.KEY_FRAME_RATE,
					Setting.MediaCodecGlod.FRAME_RATE);
			format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL,
					Setting.MediaCodecGlod.VGOP / Setting.MediaCodecGlod.VFPS);
			//8985
//			byte[] header_sps = { 0, 0, 0, 1, 103, 66, -64, 41, -115, 104, 4,
//					0, 77, 121, 96, 30, 17, 8, -44 };
//			byte[] header_pps = { 0, 0, 0, 1, 104, -50, 1, -88, 53, -56 };


			//8935
			byte[] header_sps = { 0, 0, 0, 1, 103, 66, 64, 31, -90, -128, 64,
					4, -33, -107 };
			byte[] header_pps = { 0, 0, 0, 1, 104, -50, 56, -128 };

			format.setByteBuffer("csd-0", ByteBuffer.wrap(header_sps));
			format.setByteBuffer("csd-1", ByteBuffer.wrap(header_pps));
			mCodec.configure(format, mSurfaceView.getHolder().getSurface(),
					null, 0);
			mCodec.start();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.e(TAG, "hdb--initDecoder-ex:" + e.toString());
		}
	}

	private void startServer() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					LogUtils.i(TAG, "hdb---data--连接start---serverIp:"
							+ serverIp);
					dataSocket = new Socket(serverIp, Setting.PortGlob.DATAPORT);// 10.0.0.24
					LogUtils.i(TAG, "hdb---data--连接成功");
					mHandler.sendEmptyMessage(Setting.HandlerGlod.CONNET_SUCCESS);
					DataInputStream dis = new DataInputStream(dataSocket
							.getInputStream());
					while (isRun) {
						// 远程服务器定义，每条数据的头为三个字节里面记录的是该条数据的长度
						byte[] head = new byte[3];
						dis.read(head);
						int len = bufferToInt(head);
						LogUtils.v(TAG, "hdb---read len " + len);

						if (len > 0 ) {
							byte[] buf = new byte[len];
							LogUtils.v(TAG, "hdb----read content " + buf.length);
							dis.readFully(buf);
							onFrame(buf, 0, buf.length);
							buf = null;
						} else {
							failCount++;
							if (failCount == 1) {
								mHandler.sendEmptyMessageDelayed(
										Setting.HandlerGlod.CLEAR_FAILCOUNT,
										300);
							}
							if (failCount > 5) {
								failCount = 0;
								mHandler.sendEmptyMessage(Setting.HandlerGlod.CONNECT_FAIL);
							}
						}
					}
					mHandler.sendEmptyMessage(Setting.HandlerGlod.CONNECT_FAIL);
				} catch (Exception ex) {
					LogUtils.e(TAG, "hdb--dataServer-ex:" + ex.toString());
					mHandler.sendEmptyMessage(Setting.HandlerGlod.CONNECT_FAIL);
				}
			}
		}).start();
	}

	public static int bufferToInt(byte[] src) {
		int value;
		value = (int) ((src[0] & 0xFF) | ((src[1] & 0xFF) << 8) | ((src[2] & 0xFF) << 16));
		return value;
	}

	private boolean onFrame(byte[] buf, int offset, int length) {

		mHandler.sendEmptyMessage(Setting.HandlerGlod.START_SHOW_DATA);
		// Get input buffer index
		ByteBuffer[] inputBuffers = mCodec.getInputBuffers();
		int inputBufferIndex = mCodec.dequeueInputBuffer(100);
		LogUtils.v(TAG, " inputBufferIndex  " + inputBufferIndex);

		if (inputBufferIndex >= 0) {
			ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
			inputBuffer.clear();
			inputBuffer.put(buf, offset, length);
			mCodec.queueInputBuffer(inputBufferIndex, 0, length,
					System.currentTimeMillis(), 0);
			// mCount++;
		} else {
			return false;
		}
		// Get output buffer index
		MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
		int outputBufferIndex = mCodec.dequeueOutputBuffer(bufferInfo, 100);

		while (outputBufferIndex >= 0) {
			mCodec.releaseOutputBuffer(outputBufferIndex, true);
			outputBufferIndex = mCodec.dequeueOutputBuffer(bufferInfo, 0);
		}
		return true;

	}

	public static interface CallBack {

		public void Loading();

		public void disPlayRemoteDesk();

		public void fila();

	}

	public void onDestroy() {
		if (mCodec != null) {
			mCodec.release();
			mCodec = null;

		}
		if (dataSocket != null) {

			try {
				dataSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}