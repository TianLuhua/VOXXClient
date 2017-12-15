package com.sat.satcontorl.fragment.SearchServer;

import java.util.ArrayList;

import android.content.Context;

import com.sat.satcontorl.base.AbstractPresenter;
import com.sat.satcontorl.bean.DeviceInfo;
import com.sat.satcontorl.fragment.SearchServer.SearchServerMode.CallBack;

public class SearchServerPresenter extends AbstractPresenter<SearchServerView> {

	private static final String TAG = "SearchServerPresenter";

	private SearchServerMode mSearchServerMode;

	public SearchServerPresenter() {
		mSearchServerMode = new SearchServerMode();
	}

	public void searchDevices(Context mContext) {
		if (mSearchServerMode != null) {

			mSearchServerMode.setCallBack(new CallBack() {

				@Override
				public void searchSuccess(ArrayList<DeviceInfo> deviceInfos) {
					// TODO Auto-generated method stub
					if (getView() != null) {
						getView().searchSuccess(deviceInfos);

					}

				}

				@Override
				public void searchLoading() {
					// TODO Auto-generated method stub

					if (getView() != null) {
						getView().searchLoading();
					}

				}

				@Override
				public void searchFila(String msg) {
					// TODO Auto-generated method stub

					if (getView() != null) {
						getView().searchFila(msg);

					}
				}

				@Override
				public void searchEnd() {
					// TODO Auto-generated method stub
					if (getView() != null) {
						getView().searchEnd();

					}

				}

				@Override
				public void searchOutTime() {
					// TODO Auto-generated method stub
					if (getView() != null) {
						getView().searchOutTime();

					}

				}
			});

		}
		mSearchServerMode.searchDevices(mContext);

	}

	@Override
	public void detachView() {
		// TODO Auto-generated method stub
		super.detachView();
		mSearchServerMode.onDestroy();

	}

}
