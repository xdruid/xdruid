package org.xdruid.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;


public abstract class BaseScreen implements Screen{
	
	private State state = State.CREATED;
	private String layout;
	
	protected String name;
	
	protected Activity parent;
	protected Dispatcher dispatcher;
	
	
	public BaseScreen(Activity parent, Dispatcher dispatcher, String name) {
		this.parent = parent;
		this.dispatcher = dispatcher;
		this.name = name;
	}
	
	
	public final void initializing(Object domainObject) throws Exception {
		if(state == State.CREATED){
			bindViews(parent);
			initializeViews(parent, dispatcher, domainObject);
			useLayout(getInitialLayout());
			continueInitializing();
			this.state = State.INITIALIZED;
		}else{
			throw new IllegalStateException("The Screen not in CREATED state. Already initialized.");
		}
	}

	protected abstract void bindViews(Activity parent) throws Exception;
	protected abstract void initializeViews(Activity parent, Dispatcher dispatcher, Object domainObject) throws Exception;
	
	protected void continueInitializing() throws Exception { }
	
	
	public final void destroying() throws Exception {
		if(isInitialized()){
			screenDestroyed();
			this.state = State.DESTROYED;
		}else{
			throw new IllegalStateException("The Screen cannot be DESTROYED because it is in illegal state.");
		}
	}
	
	protected abstract void screenDestroyed() throws Exception;
	

	public final void screenVisible() throws Exception {
		if(isDestroyed()){
			throw new IllegalStateException("Cannot show already DESTROYED Screen.");
		}else if(state == State.CREATED){
			throw new IllegalStateException("Too early to show this screen.");
		}else if(!isVisible()){
			screenShowing();
		}
	}

	protected abstract void screenShowing()throws Exception;
	
	
	public final void screenHidden() throws Exception {
		if(isDestroyed()){
			throw new IllegalStateException("Cannot show already DESTROYED Screen.");
		}else if(state == State.CREATED){
			throw new IllegalStateException("Too early to hide this screen.");
		}else if(!isVisible()){
			screenHiding();
		}
	}
	
	protected abstract void screenHiding() throws Exception;
	protected abstract String getInitialLayout();
	protected void useLayout(String layout){
		this.layout = layout;
	}
	

	public String getCurrenLayoutName() {
		return layout;
	}

	public State getState() {
		return state;
	}

	public boolean isInitialized() {
		return state.ordinal() > State.CREATED.ordinal() &&
				state.ordinal() < State.DESTROYED.ordinal();
	}

	public boolean isVisible() {
		return state == State.VISIBLE;
	}

	public boolean isHidden() {
		return state == State.HIDDEN;
	}

	public boolean isDestroyed() {
		return state == State.DESTROYED;
	}

	public String getName() {
		return name;
	}
	
	
	protected void on(String eventName, View component, EventCallback callback){
		if("click".equals(eventName)){
			component.setOnClickListener(callback);
		}else if("touch".equals(eventName)){
			component.setOnTouchListener(callback);
		}else if("longClick".equals(eventName)){
			component.setOnLongClickListener(callback);
		}else if("key".equals(eventName)){
			component.setOnKeyListener(callback);
		}else if("focusChange".equals(eventName)){
			component.setOnFocusChangeListener(callback);
		}else if("createContextMenu".equals(eventName)){
			component.setOnCreateContextMenuListener(callback);
		}else{
			onUnknown(eventName, component, callback);
		}
	}
	
	protected void onUnknown(String eventName, View component, EventCallback callback){	}
	
	
	public void on(String eventName, View component, String callbackMethod){
		on(eventName, component, new EventCallback(this, callbackMethod));
	}
	
	
	protected class Callback{
		public Object target;
		public String method;
		
		
		
		public Callback(Object target, String method) {
			super();
			this.target = target;
			this.method = method;
		}

		private Method callbackMethod;
		
		
		public Object doInvoke(Object...args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			Object r = null;
			if(callbackMethod == null){
				build(args);
			}
			if(callbackMethod != null){
				r = (Object) callbackMethod.invoke(target, args);
			}
			return r;
		}
		
		private void build(Object...args){
			Class<?> [] types = new Class<?>[args.length];
			int i = 0;
			for(Object a:args){
				types[i]=a.getClass();
				i++;
			}
			try {
				callbackMethod = target.getClass().getMethod(method, types);
			} catch (SecurityException e) {
				callbackMethod = null;
			} catch (NoSuchMethodException e) {
				callbackMethod = null;
			}
		}
	}
	
	protected class EventCallback extends Callback implements
		View.OnClickListener, View.OnCreateContextMenuListener,
		View.OnFocusChangeListener, View.OnKeyListener,
		View.OnLongClickListener,View.OnTouchListener{

		public EventCallback(Object target, String method) {
			super(target, method);
		}

		public boolean onTouch(View v, MotionEvent event) {
			try {
				return (Boolean)doInvoke(v,event);
			} catch (Exception e){
				// FIXME
			}
			return false;
		}

		public boolean onLongClick(View v) {
			try {
				return (Boolean)doInvoke(v);
			} catch (Exception e){
				// FIXME
			}
			return false;
		}

		public boolean onKey(View v, int keyCode, KeyEvent event) {
			try {
				return (Boolean)doInvoke(v,keyCode,event);
			} catch (Exception e){
				// FIXME
			}
			return false;
		}

		public void onFocusChange(View v, boolean hasFocus) {
		}

		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			try {
				doInvoke(menu,v,menuInfo);
			} catch (Exception e){
				// FIXME
			}
		}

		public void onClick(View v) {
			try {
				doInvoke(v);
			} catch (Exception e){
				// FIXME
			}
		}
		
	}
	
}
