package org.xdruid.ui;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;

public abstract class ActivityDispatcher extends Activity implements Dispatcher {
	
	private Map<String, Screen> screens = new HashMap<String, Screen>();
	private Screen currentScreen;
	private Screen defaultScreen;
	
	private LayoutManager layoutManager;
	
	public Screen getScreen(String name) {
		return screens.get(name);
	}

	public void showScreen(String name) throws Exception {
		Screen screen = getScreen(name);
		showScreen(screen);
	}
	
	protected void showScreen(Screen screen) throws Exception{
		if(screen != null &&
				!screen.isVisible()){
			int layout = layoutManager.getLayoutId(screen.getCurrenLayoutName());
			if(layout != 0){
				currentScreen = screen;
				setContentView(layout);
				screen.screenVisible();
			}
		}
	}

	public void hideScreen(String name) throws Exception {
		Screen screen = getScreen(name);
		hideScreen(screen);
	}
	
	protected void hideScreen(Screen screen) throws Exception{
		if(screen != null &&
				screen.isVisible()){
			showScreen(defaultScreen);
			screen.screenHidden();
		}
	}

	public void closeScreen(String name) throws Exception {
		Screen screen = getScreen(name);
		closeScreen(screen);
	}
	
	protected void closeScreen(Screen screen) throws Exception{
		if(screen != null && 
				!screen.isDestroyed()){
			hideScreen(screen);
			screen.destroying();
		}
	}

	public Screen getCurrentScreen() {
		return currentScreen;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initializeDispatcher(savedInstanceState);
	}
	
	protected void initializeDispatcher(Bundle savedInstanceState){
		layoutManager = new DefaultLayoutManager(getWindowManager());
	}
	
	
	protected void addScreen(String name, Class<? extends Screen> screenClass) throws Exception{
		
	}
	
}
