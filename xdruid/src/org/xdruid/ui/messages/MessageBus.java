package org.xdruid.ui.messages;


public interface MessageBus {
	public void publish(String topic, Message message) throws Exception;
	public String subscribe(String topic, String callback, Object target, Object source, boolean async) throws Exception;
	public int unsubscribe(String topic, String id, boolean all);
}
