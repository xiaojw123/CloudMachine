package com.cloudmachine.base.bean;

import java.io.Serializable;

/**
 * des:封装服务器返回数据
 * Created by xsf
 * on 2016.09.9:47
 */
public class BaseRespose<T> implements Serializable {
    private boolean success;
    private int code;
    private String message;
    private String devMsg;

    public String getDevMsg() {
        return devMsg;
    }

    public void setDevMsg(String devMsg) {
        this.devMsg = devMsg;
    }

    private T result;
    private PageBean page;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public boolean success() {
        return 800 == code;
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

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "BaseRespose{" +
                "code=" + code +
                ", success=" + success +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
