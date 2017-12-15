package com.sat.satcontorl.fragment.SearchServer;

import java.util.ArrayList;

import com.sat.satcontorl.base.BaseView;
import com.sat.satcontorl.bean.DeviceInfo;

public interface SearchServerView extends BaseView {

	public void searchLoading();

	public void searchSuccess(ArrayList<DeviceInfo> deviceInfos);

	public void searchEnd();

	public void searchFila(String msg);

	public void searchOutTime();

}
