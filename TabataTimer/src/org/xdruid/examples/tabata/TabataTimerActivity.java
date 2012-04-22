package org.xdruid.examples.tabata;

import org.xdruid.examples.tabata.screens.MainScreen;
import org.xdruid.ui.ActivityDispatcher;

public class TabataTimerActivity extends ActivityDispatcher {
    
	@Override
	protected void addScreens() throws Exception {
		addScreen("tabata.screens.main", MainScreen.class);
		
		
		
		useDefaultScreen("tabata.screens.main");
	}
	
	@Override
	protected void continueOnCreate() throws Exception {
		showScreen("tabata.screens.main", new Object());
	}
}