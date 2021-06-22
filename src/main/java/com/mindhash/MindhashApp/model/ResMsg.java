package com.mindhash.MindhashApp.model;

public class ResMsg {
	private boolean res;
	private String msg;
	
	public ResMsg() {
		
	}
	
	public boolean getRes() {
		return res;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setRes(boolean res) {
		this.res = res;
	}
	
	public void setMsg(String errMsg) {
		this.msg = errMsg;
	}
}
