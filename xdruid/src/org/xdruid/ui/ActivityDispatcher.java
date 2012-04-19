package org.xdruid.ui;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.xdruid.ui.messages.MessageBus;
import org.xdruid.ui.messages.SimpleMessageBus;

import android.app.Activity;
import android.os.Bundle;

public abstract class ActivityDispatcher extends Activity implements Dispatcher {
	
	private Map<String, Screen> screens = new HashMap<String, Screen>();
	private Screen currentScreen;
	private Screen defaultScreen;
	
	private LayoutManager layoutManager;
	private MessageBus messageBus;
	
	public Screen getScreen(String name) {
		return screens.get(name);
	}

	public void showScreen(String name, Object dataObject) throws Exception {
		Screen screen = getScreen(name);
		showScreen(screen, dataObject);
	}
	
	protected void showScreen(Screen screen, Object dataObject) throws Exception{
		if(screen != null &&
				!screen.isVisible()){
			prepareScreen(screen, dataObject);
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
			showScreen(defaultScreen, null);
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
	
	protected void prepareScreen(Screen screen, Object dataObject) throws Exception{
		if(dataObject == null)
			return;
		if(!screen.isInitialized()){
			screen.reloading(dataObject);
		}else if(!screen.isDestroyed()){
			screen.initializing(dataObject);
		}
	}
	
	
	protected void initializeDispatcher(Bundle savedInstanceState){
		layoutManager = getLayoutManagerInstance();
		messageBus = getMessageBusInstance();
	}
	
	protected LayoutManager getLayoutManagerInstance(){
		return new DefaultLayoutManager(getWindowManager());
	}
	protected MessageBus getMessageBusInstance(){
		return new SimpleMessageBus();
	}
	
	
	protected void addScreen(String name, Class<? extends Screen> screenClass) throws Exception{
		Constructor<? extends Screen> constructor = 
				screenClass.getConstructor(Activity.class, Dispatcher.class, String.class);
		Screen screen = constructor.newInstance(this,this, name);
		addScreen(name, screen);
	}
	
	protected void addScreen(String name, Screen screen) throws Exception{
		screens.put(name, screen);
	}
	
	public MessageBus bus(){
		return messageBus;
	}
}
