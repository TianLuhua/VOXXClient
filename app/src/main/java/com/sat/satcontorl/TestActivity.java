package com.sat.satcontorl;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.sat.satcontorl.fragment.SearchServer.SearchServerFragment;
import com.sat.satcontorl.fragment.displayserver.DisPlayServerFragment;

public class TestActivity extends Activity implements
		SearchServerFragment.IDisPlayServerByIP {

	private Fragment currentFragment;
	private SearchServerFragment searchServerFragment;
	private DisPlayServerFragment disPlayServerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
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
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
							"DisPlayServerFragment").commit();
		}
		setCurrentFragment(disPlayServerFragment);
	}

	private void setCurrentFragment(Fragment currentFragment) {
		this.currentFragment = currentFragment;
	}

	@Override
	public void onBackPressed() {
		System.exit(1);
	}
}
