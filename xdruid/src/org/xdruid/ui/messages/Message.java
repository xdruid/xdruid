package org.xdruid.ui.messages;

public class Message {
	public String name;
	public Object body;
	public Object source;
	public Object target;
	
	public Message(String name, Object body, Object source, Object target) {
		super();
		this.name = name;
		this.body = body;
		this.source = source;
		this.target = target;
	}
	
}
