package com.sat.satcontorl;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.sat.satcontorl.fragment.SearchServer.SearchServerFragment;
import com.sat.satcontorl.fragment.displayserver.DisPlayServerFragment;
import com.sat.satcontorl.utils.LogUtils;

public class TestActivity extends Activity implements
        SearchServerFragment.IDisPlayServerByIP {

    private static final java.lang.String TAG = "TestActivity";
    private Fragment currentFragment;
    private SearchServerFragment searchServerFragment;
    private DisPlayServerFragment disPlayServerFragment;
    private long mExitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LogUtils.v(TAG, "onCreate----");
        setContentView(R.layout.activity_test);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        LogUtils.v(TAG, "onStart----");
        searchServerFragment = (SearchServerFragment) getFragmentManager()
                .findFragmentByTag("SearchServerFragment");
        if (searchServerFragment != null) {
            disPlayServerFragment.startDisPlayRomoteDesk();
        } else {
            searchServerFragment = SearchServerFragment.getInstans();
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.content, searchServerFragment,
                            "SearchServerFragment").commit();
        }
        setCurrentFragment(searchServerFragment);
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        LogUtils.v(TAG, "onRestart----");
        if (currentFragment instanceof DisPlayServerFragment) {

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, disPlayServerFragment,
                            "DisPlayServerFragment").commit();
        } else if (currentFragment instanceof SearchServerFragment) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, searchServerFragment,
                            "SearchServerFragment").commit();

        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        LogUtils.v(TAG, "onResume----");
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        LogUtils.v(TAG, "onPause----");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.v(TAG, "onStop----");
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        LogUtils.v(TAG, "onDestroy----");
    }

    @Override
    public void displayServerByIP(String serverIp) {
        // TODO Auto-generated method stub

        disPlayServerFragment = (DisPlayServerFragment) getFragmentManager()
                .findFragmentByTag("DisPlayServerFragment");
        if (disPlayServerFragment != null) {
            disPlayServerFragment.startDisPlayRomoteDesk();
        } else {

            disPlayServerFragment = DisPlayServerFragment.getInstans();
            Bundle bundle = new Bundle();
            bundle.putString(DisPlayServerFragment.SERVERIP, serverIp);
            disPlayServerFragment.setArguments(bundle);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, disPlayServerFragment,
                            "DisPlayServerFragment").addToBackStack(null).commit();
        }
        setCurrentFragment(disPlayServerFragment);
    }

    private void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                // 如果两次按键时间间隔大于2000毫秒，则不退出
                Toast.makeText(this, getResources().getString(R.string.press_again), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();// 更新mExitTime
            } else {
                System.exit(0);// 否则退出程序
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
