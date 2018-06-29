package com.cloudmachine.bean;

import java.io.Serializable;

public class BaseBO<T> implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -1495290738122413991L;

	private boolean ok;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	private int code;
	private boolean success;
	
	private String message;
	private T result;
	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "BaseBO{" +
				"ok=" + ok +
				", code=" + code +
				", message='" + message + '\'' +
				", result=" + result +
				'}';
	}
}
