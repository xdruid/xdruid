package org.xdruid.ui;

import org.xdruid.ui.messages.MessageBus;


public interface Dispatcher {
	public Screen getScreen(String name);
	
	public void showScreen(String name) throws Exception;
	public void hideScreen(String name) throws Exception;
	public void closeScreen(String name) throws Exception;
	
	public Screen getCurrentScreen();
	
	public MessageBus notifications();
}
