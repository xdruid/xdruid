package org.xdruid.exceptions;

public class TranslatableException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -988751126037218965L;

	
	
	public int code;
	
	
	public TranslatableException() {
		super();
	}

	public TranslatableException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public TranslatableException(String detailMessage) {
		super(detailMessage);
	}

	public TranslatableException(Throwable throwable) {
		super(throwable);
	}
}
