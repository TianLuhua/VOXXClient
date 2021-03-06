package com.sat.satcontorl.fragment.SearchServer;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sat.satcontorl.R;
import com.sat.satcontorl.bean.DeviceInfo;
import com.sat.satcontorl.utils.LogUtils;

public class DeviceAdapter extends BaseAdapter {

	private static final String TAG = "DeviceAdapter";

	private Context mContext;
	private ArrayList<DeviceInfo> deviceInfos;

	public DeviceAdapter(Context mContext, ArrayList<DeviceInfo> deviceInfos) {
		// TODO Auto-generated constructor stub
		this.deviceInfos = deviceInfos;
		this.mContext = mContext;
	}

	public ArrayList<DeviceInfo> getDeviceInfos() {
		return deviceInfos;
	}

	public void setDeviceInfos(ArrayList<DeviceInfo> deviceInfos) {
		this.deviceInfos = deviceInfos;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return deviceInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder vHolder;
		if (convertView == null) {
			vHolder = new ViewHolder();
			convertView = View.inflate(mContext,
					R.layout.fragment_searchservice_listview_item, null);
			vHolder.deviceName = (TextView) convertView
					.findViewById(R.id.tv_list_ip);
			convertView.setTag(vHolder);
		}
		LogUtils.i(TAG, "hdb---name:" + deviceInfos.get(position).getName());
		vHolder = (ViewHolder) convertView.getTag();
		vHolder.deviceName.setText(deviceInfos.get(position).getName());
		return convertView;
	}

	class ViewHolder {
		TextView deviceName;
	}

}
