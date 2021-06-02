package com.mindhash.MindhashApp.model;

public class CheckEmailRes {
	private boolean isTaken;
	private String errMsg;
	
	public CheckEmailRes() {
		
	}
	
	public boolean getIsTaken() {
		return isTaken;
	}
	
	public String getErrMsg() {
		return errMsg;
	}
	
	public void setIsTaken(boolean isTaken) {
		this.isTaken = isTaken;
	}
	
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
