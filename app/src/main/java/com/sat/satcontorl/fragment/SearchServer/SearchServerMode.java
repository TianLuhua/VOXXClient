package com.sat.satcontorl.fragment.SearchServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.sat.satcontorl.Setting;
import com.sat.satcontorl.bean.DeviceInfo;
import com.sat.satcontorl.utils.IpUtils;
import com.sat.satcontorl.utils.LogUtils;

public class SearchServerMode {

	public static final String TAG = "SearchServerMode";

	private static final int SCAN_IP_OVER = 2;
	private static final int SCAN_SERVER_OUTTIME = 5000;;
	private MulticastSocket multicastSocket;
	private InetAddress broadcastAddress;
	private DatagramSocket udpBack;
	private FindDeviceRunnable fRunnable;
	private ArrayList<DeviceInfo> deviceInfos;
	private String serverIp;
	private CallBack callBack;

	private Handler mAsyncEventHandler = new Handler() {

		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {

			case SCAN_IP_OVER:
				if (callBack != null) {
					callBack.searchSuccess(deviceInfos);
					callBack.searchEnd();
				}

				break;

			default:
				break;
			}

		}
	};

	public SearchServerMode() {

	}

	public void searchDevices(Context mContext) {
		if (callBack != null) {
			callBack.searchLoading();
		}
		deviceInfos = new ArrayList<DeviceInfo>();
		fRunnable = new FindDeviceRunnable();
		mAsyncEventHandler.postDelayed(fRunnable, 2000);
		findDevice(mContext);
		startUdpBroadcast();
	}

	private void findDevice(Context mContext) {
		try {
			broadcastAddress = IpUtils.getBroadcastAddress(mContext);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			if (callBack != null) {
				callBack.searchFila(e.getMessage());
			}
		}
		IpUtils.openWifiBrocast(mContext); // for some phone can
											// not send
											// broadcast
	}

	private void startUdpBroadcast() {
		new Thread() {
			public void run() {
				try {
					if (multicastSocket == null) {
						multicastSocket = new MulticastSocket(
								Setting.PortGlob.MULTIPORT);
						multicastSocket.joinGroup(broadcastAddress);
					}
				} catch (IOException e) {
					e.printStackTrace();

				}
			}
		}.start();

	}

	private void sendBroadCast() throws IOException {
		String ipAddress = IpUtils.getHostIP();
		LogUtils.i(TAG, "hdb----send---ipAddress:" + ipAddress);
		mAsyncEventHandler.postDelayed(fRunnable, 3000);
		if (ipAddress != null) {
			byte[] data = ("phoneip:" + ipAddress).getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length,
					broadcastAddress, Setting.PortGlob.MULTIPORT);
			multicastSocket.send(packet);
			receiverBack();
		}

	}

	private void receiverBack() {
		try {
			if (udpBack == null) {
				udpBack = new DatagramSocket(Setting.PortGlob.BACKPORT);
			}
			byte[] data = new byte[50];
			DatagramPacket pack = new DatagramPacket(data, data.length);
			udpBack.receive(pack);
			udpBack.setSoTimeout(SCAN_SERVER_OUTTIME);
			String back = new String(pack.getData(), pack.getOffset(),
					pack.getLength());
			if (back != null && back.startsWith("serverip:")) {
				String[] split = back.split(":");
				serverIp = split[1];

				if (!hasDeviceInfo(deviceInfos, serverIp)) {
					LogUtils.i(TAG, "hdb-------in:");
					DeviceInfo mDeviceInfo = new DeviceInfo(serverIp, split[2]);
					deviceInfos.add(mDeviceInfo);

					byte[] over = "over".getBytes();
					DatagramPacket packet = new DatagramPacket(over,
							over.length, broadcastAddress,
							Setting.PortGlob.MULTIPORT);
					multicastSocket.send(packet);
					mAsyncEventHandler.sendEmptyMessageDelayed(SCAN_IP_OVER,
							2000);
				}
				LogUtils.i(TAG, "hdb-------serverIp:" + serverIp
						+ "   split[2]:" + split[2]);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean hasDeviceInfo(ArrayList<DeviceInfo> Infos, String ip) {
		for (int i = 0; i < Infos.size(); i++) {
			if (ip != null && ip.equals(Infos.get(i).getIpAddress())) {
				return true;
			}
		}
		return false;
	}

	private class FindDeviceRunnable implements Runnable {

		@Override
		public void run() {
			new Thread() {
				public void run() {
					try {
						sendBroadCast();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();

		}

	}

	public void setCallBack(CallBack callBack) {
		this.callBack = callBack;

	}

	public static interface CallBack {

		public void searchLoading();

		public void searchSuccess(ArrayList<DeviceInfo> deviceInfos);

		public void searchEnd();

		public void searchFila(String msg);

		public void searchOutTime();

	}

	public void onDestroy() {
		this.callBack = null;
		// if (multicastSocket != null) {
		// try {
		// multicastSocket.leaveGroup(broadcastAddress);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// multicastSocket.close();
		// }
		// if (udpBack != null) {
		// udpBack.close();
		// }
	}

}
