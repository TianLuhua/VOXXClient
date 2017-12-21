package com.sat.satcontorl.fragment.displayserver;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sat.satcontorl.R;
import com.sat.satcontorl.base.AbstractMVPFragment;
import com.sat.satcontorl.utils.LogUtils;

public class DisPlayServerFragment extends
        AbstractMVPFragment<DisPlayServerView, DisPlayServerPresenter>
        implements DisPlayServerView, SurfaceHolder.Callback {

    private static final String TAG = "DisPlayServerFragment";

    public static final String SERVERIP = "serverIp";
    private SurfaceView displayServerView;
    private ProgressBar loadingBar;
    private String serverIp;
    private float densityX;
    private float densityY;
    private int changeX = 0;
    private int changeY = 0;

    private DisPlayServerPresenter mPresenter;
    private static DisPlayServerFragment disPlayServerFragment;

    public static DisPlayServerFragment getInstans() {
        if (disPlayServerFragment == null) {
            disPlayServerFragment = new DisPlayServerFragment();
        }
        return disPlayServerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Bundle bundle = getArguments();
        serverIp = bundle.getString(SERVERIP);
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int widthPixels = dm.widthPixels;
        int heightPixels = dm.heightPixels;
        LogUtils.i(TAG, "hdb---widthPixels:" + widthPixels + "  heightPixels:"
                + heightPixels);
        densityX = 1024f / (float) widthPixels;
        densityY = 600f / (float) heightPixels;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.fragment_displayserver, null);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        // TODO Auto-generated method stub
        displayServerView = (SurfaceView) rootView
                .findViewById(R.id.surfaceView1);
        displayServerView.getHolder().addCallback(this);
        displayServerView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                changeX = (int) (x * densityX);
                changeY = (int) (y * densityY);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mPresenter.sendTouchData(MotionEvent.ACTION_DOWN, changeX,
                                changeY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mPresenter.sendTouchData(MotionEvent.ACTION_MOVE, changeX,
                                changeY);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mPresenter.sendTouchData(MotionEvent.ACTION_UP, changeX,
                                changeY);
                        break;

                    default:
                        break;
                }

                return true;
            }
        });
        loadingBar = (ProgressBar) rootView.findViewById(R.id.pb_wait);

    }

    @Override
    public void Loading() {
        // TODO Auto-generated method stub
        if (loadingBar.getVisibility() != View.VISIBLE) {
            loadingBar.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void disPlayRemoteDesk() {
        if (loadingBar.getVisibility() == View.VISIBLE) {
            loadingBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void fila() {
        Toast.makeText(getContext(), R.string.connection_failed, Toast.LENGTH_LONG).show();

    }

    @Override
    protected DisPlayServerPresenter createPresenter() {
        LogUtils.i(TAG, "DisPlayServerFragment---createPresenter---serverIp:"
                + serverIp);
        return new DisPlayServerPresenter(serverIp);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startDisPlayRomoteDesk();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    public void startDisPlayRomoteDesk() {
        if (mPresenter != null) {
            mPresenter.startDisPlayRomoteDesk(displayServerView);
        }

    }
}
