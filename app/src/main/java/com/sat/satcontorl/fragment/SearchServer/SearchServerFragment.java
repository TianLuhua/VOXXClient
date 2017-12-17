package com.sat.satcontorl.fragment.SearchServer;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.sat.satcontorl.R;
import com.sat.satcontorl.base.AbstractMVPFragment;
import com.sat.satcontorl.bean.DeviceInfo;
import com.sat.satcontorl.utils.LogUtils;

public class SearchServerFragment extends
        AbstractMVPFragment<SearchServerView, SearchServerPresenter> implements
        SearchServerView {

    private static final String TAG = "SearchServerFragment";

    private ImageView ivSearch;
    private ListView deviceListView;
    private LinearLayout deviceContent;
    private LinearLayout searchView;
    private DeviceAdapter searchAdapter;
    private ArrayList<DeviceInfo> deviceInfos;

    private SearchServerPresenter mPresenter;
    private static SearchServerFragment searchServerFragmentInstans;

    public static SearchServerFragment getInstans() {
        if (searchServerFragmentInstans == null) {
            searchServerFragmentInstans = new SearchServerFragment();
        }
        return searchServerFragmentInstans;
    }

    public SearchServerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = LayoutInflater.from(container.getContext()).inflate(
                R.layout.fragment_searchdevice, null);
        initItemView(rootView);
        return rootView;
    }

    private void initItemView(View rootView) {
        // TODO Auto-generated method stub
        ivSearch = (ImageView) rootView.findViewById(R.id.iv_search);
        searchView = (LinearLayout) rootView.findViewById(R.id.ll_find);
        deviceContent = (LinearLayout) rootView.findViewById(R.id.ll_info);
        deviceListView = (ListView) rootView.findViewById(R.id.lv_device);
        deviceListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String serverIp = deviceInfos.get(position).getIpAddress();
                Toast.makeText(SearchServerFragment.getInstans().getContext(),
                        "serverIp:" + serverIp, 1).show();
                if (callback != null) {
                    callback.displayServerByIP(serverIp);
                }

            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        callback = (IDisPlayServerByIP) activity;
    }

    private IDisPlayServerByIP callback;

    public interface IDisPlayServerByIP {
        public void displayServerByIP(String ip);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        startSearchService();
    }

    public void startSearchService() {
        if (mPresenter != null) {
            mPresenter.searchDevices(getContext().getApplicationContext());
        }
    }

    @Override
    public void searchLoading() {
        // TODO Auto-generated method stub
        LogUtils.e(TAG, "searchLoading");
        Toast.makeText(SearchServerFragment.getInstans().getContext(),
                "SearchServerFragmentSearchLoading", 1).show();
        startSearchAnimation();
    }

    @Override
    public void searchSuccess(ArrayList<DeviceInfo> deviceInfos) {
        // TODO Auto-generated method stub
        LogUtils.e(TAG, "searchSuccess---:" + deviceInfos.toString());
        Toast.makeText(SearchServerFragment.getInstans().getContext(),
                "searchSuccess", 1).show();
        if (searchAdapter == null) {
            searchAdapter = new DeviceAdapter(getActivity()
                    .getApplicationContext(), deviceInfos);
        }
        deviceListView.setAdapter(searchAdapter);

        if (searchAdapter.getDeviceInfos() != deviceInfos) {
            searchAdapter.setDeviceInfos(deviceInfos);
        }
        this.deviceInfos = deviceInfos;
    }

    @Override
    public void searchEnd() {
        showDevice();

    }

    @Override
    public void searchFila(String msg) {
        // TODO Auto-generated method stub
        LogUtils.e(TAG, "searchFila---msg:" + msg);
        Toast.makeText(SearchServerFragment.getInstans().getContext(),
                "searchFila:" + msg, 1).show();
    }

    @Override
    public void searchOutTime() {
        // TODO Auto-generated method stub
        LogUtils.e(TAG, "searchOutTime");
        Toast.makeText(SearchServerFragment.getInstans().getContext(),
                "searchOutTime", 1).show();

    }

    private void showDevice() {
        if (searchView.getVisibility() == View.VISIBLE) {
            searchView.setVisibility(View.GONE);
        }
        if (deviceContent.getVisibility() != View.VISIBLE) {
            deviceContent.setVisibility(View.VISIBLE);
        }
    }

    private void startSearchAnimation() {
        AnimationSet as = new AnimationSet(true);
        RotateAnimation ra = new RotateAnimation(0, 90,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 1f);
        TranslateAnimation ta = new TranslateAnimation(-50, 0, 0, -50);
        ta.setDuration(1000);
        ta.setRepeatCount(-1);
        ra.setDuration(1000);
        ra.setRepeatCount(-1);
        as.addAnimation(ta);
        as.addAnimation(ra);
        as.setRepeatMode(AnimationSet.REVERSE);
        if (ivSearch != null) {
            ivSearch.startAnimation(as);
        }
    }

    @Override
    protected SearchServerPresenter createPresenter() {
        // TODO Auto-generated method stub
        return new SearchServerPresenter();
    }

}
