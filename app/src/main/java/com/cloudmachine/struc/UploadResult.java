package com.cloudmachine.struc;

import java.io.Serializable;

public class UploadResult implements Serializable {

	/** code  成功返回0 */
	private int error;

	/** 图片上传的路径 */
	private String url;


	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "UploadResult [error=" + error + ", url=" + url + "]";
	}


}
