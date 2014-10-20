package com.luna.xform.model;

import java.io.Serializable;

public class ProcessVariable implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String title;
	private String code;
	private String value;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
