package com.sat.satcontorl.fragment.displayserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.view.SurfaceView;

import com.sat.satcontorl.Setting;
import com.sat.satcontorl.base.AbstractPresenter;
import com.sat.satcontorl.fragment.displayserver.DisPlayServerMode.CallBack;
import com.sat.satcontorl.utils.LogUtils;

public class DisPlayServerPresenter extends
        AbstractPresenter<DisPlayServerView> {

    protected static final String TAG = "DisPlayServerPresenter";

    private String serverIp;
    private Socket touchSocket;
    private DataOutputStream dos;

    private DisPlayServerMode disPlayServerMode;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                case Setting.HandlerGlod.CONNECT_FAIL:
                    if (getView() != null) {
                        getView().fila();
                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };

    public DisPlayServerPresenter(String serverIp) {

        this.serverIp = serverIp;
        disPlayServerMode = new DisPlayServerMode(serverIp);
    }

    public void startDisPlayRomoteDesk(SurfaceView mSurfaceView) {
        disPlayServerMode.startDisPlayRomoteDesk(mSurfaceView, new CallBack() {

            @Override
            public void fila() {
                // TODO Auto-generated method stub
                if (getView() != null) {
                    getView().fila();
                }

            }

            @Override
            public void disPlayRemoteDesk() {
                // TODO Auto-generated method stub
                if (getView() != null) {
                    getView().disPlayRemoteDesk();
                }

            }

            @Override
            public void Loading() {
                // TODO Auto-generated method stub
                if (getView() != null) {
                    getView().Loading();
                }

            }
        });

        startTouchServer();
    }

    private void startTouchServer() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    LogUtils.e(TAG, "tlh--startTouchServer-:" + serverIp);
                    touchSocket = new Socket(serverIp,
                            Setting.PortGlob.TOUCHPORT);
                    dos = new DataOutputStream(touchSocket.getOutputStream());
                } catch (Exception e) {
                    LogUtils.e(TAG, "hdb--touchServer-ex:" + e.toString());
                    mHandler.sendEmptyMessage(Setting.HandlerGlod.CONNECT_FAIL);
                }
            }
        }).start();

    }

    public void sendTouchData(final int action, final int x, final int y) {
        LogUtils.i(TAG, "sendTouchData---action:" + action + "  x:" + x
                + "  y:" + y);

        newFixThreadPool(10).execute(new Runnable() {
            @Override
            public void run() {
                if (dos != null) {
                    if (x >= 0 && x <= 1024 && y >= 0 && y <= 600) {
                        JSONObject jObject = new JSONObject();
                        try {
                            jObject.put(Setting.MotionEventKey.JACTION, action);
                            jObject.put(Setting.MotionEventKey.JX, x);
                            jObject.put(Setting.MotionEventKey.JY, y);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        // Log.i(TAG, "hdb----jObject:" + jObject.toString());
                        byte[] jBytes = jObject.toString().getBytes();
                        byte[] intToByte = new byte[1];
                        intToByte[0] = (byte) jBytes.length;
                        byte[] data = new byte[jBytes.length + 1];
                        System.arraycopy(intToByte, 0, data, 0, 1);
                        System.arraycopy(jBytes, 0, data, 1, jBytes.length);
                        LogUtils.i(TAG, "hdb----data:" + new String(data));
                        try {
                            dos.write(data);
                            dos.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        });
    }

    private ExecutorService newFixThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }

    @Override
    public void detachView() {
        // TODO Auto-generated method stub
        super.detachView();
        if (disPlayServerMode != null) {
            disPlayServerMode.onDestroy();

        }

    }

}
