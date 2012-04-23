package org.xdruid.ui.examples;

import org.xdruid.ui.Dispatcher;
import org.xdruid.ui.SimpleScreen;

import android.app.Activity;

public class LogoScreen extends SimpleScreen{

	public LogoScreen(Activity parent, 
			Dispatcher dispatcher, String name) {
		super(parent, dispatcher, name);
	}

	public void screenReloading(Object domainObject) throws Exception {	}

	public Object getValue() {
		return null;
	}

	@Override
	protected String getInitialLayout() {
		return "xdruid.logo";
	}

}
