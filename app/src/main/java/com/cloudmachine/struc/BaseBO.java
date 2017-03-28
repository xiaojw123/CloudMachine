package com.cloudmachine.struc;

import java.io.Serializable;

public class BaseBO<T> implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -1495290738122413991L;

	private boolean ok;
	
	private int code;
	
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
