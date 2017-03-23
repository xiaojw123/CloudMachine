package com.cloudmachine.base.bean;

import java.io.Serializable;

/**
 * des:封装服务器返回数据
 * Created by xsf
 * on 2016.09.9:47
 */
public class BaseRespose<T> implements Serializable {
    public int code;
    public boolean ok;
    public String message;

    public T result;

    public boolean success() {
        return 800 == code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
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
                ", ok=" + ok +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
