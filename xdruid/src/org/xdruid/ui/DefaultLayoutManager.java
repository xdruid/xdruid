package org.xdruid.ui;

import java.util.HashMap;
import java.util.Map;

import android.view.Display;
import android.view.WindowManager;

public class DefaultLayoutManager implements LayoutManager{

	
	private int width;
	private int height;
	
	public static final String DEFAULT_MODE = "default";
	
	private Map<String, Map<String, Integer>> layouts = new HashMap<String, Map<String,Integer>>();
	
	public DefaultLayoutManager(WindowManager windowManager) {
		setupDisplaySize(windowManager);
	}
	
	
	public int getLayoutId(String name) {
		return getLayoutId(name, width, height);
	}


	public int getLayoutId(String name, int width, int height) {
		Map<String, Integer> registered = getRegisteredLayouts(name);
		Integer retVal = registered.get(getMode(width, height));
		if(retVal == null){
			// try the default ...
			retVal = registered.get(DEFAULT_MODE);
		}
		return retVal;
	}


	public void register(String name, int layoutId, int width, int height) {
		Map<String, Integer> registered = getRegisteredLayouts(name);
		registered.put(getMode(width, height), layoutId);
	}


	public void registerDefaultLayout(String name, int layoutId) {
		getRegisteredLayouts(name).put(DEFAULT_MODE, layoutId);
	}
	
	protected void setupDisplaySize(WindowManager windowManager){
		Display display = windowManager.getDefaultDisplay();
		height = display.getHeight();
		width = display.getWidth();
	}

	protected String getMode(int width, int height){
		return width + "_" + height;
	}
	
	private Map<String, Integer> getRegisteredLayouts(String name){
		Map<String, Integer> registered = layouts.get(name);
		if(registered == null){
			registered = new HashMap<String, Integer>();
			layouts.put(name, registered);
		}
		return registered;
	}
	
}
