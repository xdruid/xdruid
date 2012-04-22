package org.xdruid.ui;

import android.app.Activity;

public abstract class SimpleScreen extends BaseScreen{

	public SimpleScreen(Activity parent, Dispatcher dispatcher, String name) {
		super(parent, dispatcher, name);
	}

	@Override
	protected void bindViews(Activity parent) throws Exception { }

	@Override
	protected void initializeViews(Activity parent, Dispatcher dispatcher,
			Object domainObject) throws Exception { }

	@Override
	protected void screenDestroyed() throws Exception { }

	@Override
	protected void screenShowing() throws Exception { }

	@Override
	protected void screenHiding() throws Exception { }

	@Override
	protected void bindEvents() throws Exception {	}
	
	@Override
	protected void bindLayouts() { }

}
