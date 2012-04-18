package org.xdruid.ui;

import android.app.Activity;

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

}
