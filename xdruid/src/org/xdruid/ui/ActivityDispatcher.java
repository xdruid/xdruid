package org.xdruid.ui;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.xdruid.R;
import org.xdruid.ui.examples.LogoScreen;
import org.xdruid.ui.messages.Message;
import org.xdruid.ui.messages.MessageBus;
import org.xdruid.ui.messages.SimpleMessageBus;

import android.app.Activity;
import android.os.Bundle;

public abstract class ActivityDispatcher extends Activity implements Dispatcher {
	
	public static final String XDRUID_UI_TOPIC = "xdruid.topic.ui";
	
	private Map<String, Screen> screens = new HashMap<String, Screen>();
	private History history;
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
			int layout = layoutManager.getLayoutId(screen.getCurrenLayoutName());
			if(layout != 0){
				currentScreen = screen;
				setContentView(layout);
				/*
				 * the View components will become available
				 * only after the screen has been created and
				 * put to front.
				 */
				prepareScreen(screen, dataObject);
				screen.screenVisible();
				history.add(screen);
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

	
	public void updateScreen(String name, Object dataObject) throws Exception {
		Screen screen = getScreen(name);
		updateScreen(screen, dataObject);
	}
	
	protected void updateScreen(Screen screen, Object dataObject) throws Exception{
		if(screen != null &&
				screen.isVisible()){
			screen.reloading(dataObject);
		}
	}
	
	public Screen getCurrentScreen() {
		return currentScreen;
	}
	
	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initializeDispatcher(savedInstanceState);
		try{
			addLayouts();
			addScreens();
			initializeScreens();
			continueOnCreate();
		}catch (Exception e) {
			// TODO: Exactry
			throw new RuntimeException(e);
		}
	}
	
	private void initializeScreens() throws Exception{
		if(defaultScreen == null){
			useDefaultScreen("xdruid.logo");
			showScreen(defaultScreen, new Object());
		}
	}
	
	protected void useDefaultScreen(String screenName){
		Screen screen = getScreen(screenName);
		if(screen != null){
			defaultScreen = screen;
		}
	}
	
	protected void prepareScreen(Screen screen, Object dataObject) throws Exception{
		if(dataObject == null)
			return;
		if(screen.isInitialized()){
			screen.reloading(dataObject);
		}else if(!screen.isDestroyed()){
			screen.initializing(dataObject);
		}
	}
	
	
	protected void initializeDispatcher(Bundle savedInstanceState){
		layoutManager = getLayoutManagerInstance();
		messageBus = getMessageBusInstance();
		history = getHistoryInstance();
		subscribe();
	}
	
	private void subscribe() {
		try{
			messageBus.subscribe("xdruid.ui.history.back", "onHistoryBack", this, null, false);
			messageBus.subscribe("xdruid.ui.history.forward", "onHistoryForward", this, null, false);
		}catch (Exception e) {
			// TODO: yeah..
		}
	}
	
	public void onHistoryBack(String topic, Message message) throws Exception{
		Screen screen = history.back();
		if(screen != null){
			showScreen(screen, screen.getValue());
		}
	}
	
	public void onHistoryForward(String topic, Message message){
		history.forward();
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
		screen.creating();
		addScreen(name, screen);
	}
	
	protected void addScreen(String name, Screen screen) throws Exception{
		screens.put(name, screen);
	}
	
	public MessageBus bus(){
		return messageBus;
	}
	
	protected void addScreens() throws Exception{
		addScreen("xdruid.logo", LogoScreen.class);
	}
	private void addLayouts() throws Exception{
		layoutManager.registerDefaultLayout("xdruid.logo", R.layout.xdruid);
		bindLayouts();
	}
	protected void bindLayouts(){};
	
	protected void bindLayout(String name, int layoutId, int width, int height){
		layoutManager.register(name, layoutId, width, height);
	}
	
	protected void bindLayout(String name, int layoutId){
		layoutManager.registerDefaultLayout(name, layoutId);
	}
	
	protected void continueOnCreate() throws Exception{}
	
	public LayoutManager getLayoutManager() {
		return layoutManager;
	}
	
	protected History getHistoryInstance(){
		return new History();
	}
	
	protected class History{
		private Queue<Screen> history = new LinkedList<Screen>();
		
		
		private int current = -1;
		private int capacity = 100;
		
		
		public History(int capacity) {
			this.capacity = capacity;
		}
		public History() {}
		
		@SuppressWarnings("unchecked")
		public Screen go(int count){
			if(history.size() > 0){
				current += count;
				if(current < 0){
					current = 0;
				}else if(current >= history.size()){
					current = history.size() - 1;
				}
				int step = count > 0 ? 1:-1;
				Screen screen = ((List<Screen>)history).get(current);
				while(screen.isDestroyed()){
					current+=step;
					if(current < 0 || current >= history.size())
						break;
					screen = ((List<Screen>)history).get(current);
				}
				return screen;
			}
			return null;
		}
		
		public Screen forward(){
			return go(1);
		}
		
		public Screen back(){
			return go(-1);
		}
		
		public void add(Screen screen){
			if(screen.isDestroyed())
				return;
			if(capacity > 0){
				if(history.size() >= capacity){
					history.poll();
				}
				history.add(screen);
			}else{
				history.add(screen);
			}
			current = history.size()-1;
		}
	}
	
}
