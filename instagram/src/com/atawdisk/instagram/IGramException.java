package com.atawdisk.instagram;

public class IGramException extends Exception {

	private static final long serialVersionUID = 7730573224452085381L;

	public IGramException(){
		super();
	}
	
	public IGramException(String msg){
		super(msg);
	}
	
	public IGramException(String msg, Throwable cause){
		super(msg, cause);
	}
	
	public IGramException(Throwable cause){
		super(cause);
	}
}
