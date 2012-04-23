package org.xdruid.examples.tabata.screens;

import org.xdruid.examples.tabata.R;
import org.xdruid.ui.Dispatcher;
import org.xdruid.ui.SimpleScreen;

import android.app.Activity;

public class TabataScreen extends SimpleScreen{

	public TabataScreen(Activity parent, 
			Dispatcher dispatcher, String name) {
		super(parent, dispatcher, name);
	}

	@Override
	protected void bindLayouts() {
		bindLayout("tabata.tabata", R.layout.tabata);
	}
	
	
	public void screenReloading(Object domainObject) throws Exception {
	}

	public Object getValue() {
		return null;
	}

	@Override
	protected String getInitialLayout() {
		return "tabata.tabata";
	}

	
	
}
