package com.cloudmachine.struc;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/13 下午1:10
 * 修改人：shixionglu
 * 修改时间：2017/3/13 下午1:10
 * 修改备注：
 */

public class McDeviceInfoBean  implements Serializable {

    public int code;
    public boolean ok;
    public String message;
    private List<McDeviceInfo> result;


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


    public List<McDeviceInfo> getResult() {
        return result;
    }

    public void setResult(List<McDeviceInfo> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "McDeviceInfoBean{" +
                "code=" + code +
                ", ok=" + ok +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
