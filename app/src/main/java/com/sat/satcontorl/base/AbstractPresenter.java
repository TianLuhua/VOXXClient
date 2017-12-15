package com.sat.satcontorl.base;

public abstract class AbstractPresenter<V extends BaseView> {

	private V mView;

	/**
	 * Bind View
	 * 
	 * @param view
	 */
	public void attachView(V view) {
		this.mView = view;
	}

	/**
	 * Unbind View
	 */
	public void detachView() {
		this.mView = null;
	}

	public V getView() {
		return mView;
	}

}
