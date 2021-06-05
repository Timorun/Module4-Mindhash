package com.mindhash.MindhashApp.model;

public class ResponseMsg {
	private boolean res;
	private String errMsg;
	
	public ResponseMsg() {
		
	}
	
	public boolean getRes() {
		return res;
	}
	
	public String getErrMsg() {
		return errMsg;
	}
	
	public void setRes(boolean res) {
		this.res = res;
	}
	
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
