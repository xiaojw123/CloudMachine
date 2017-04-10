package com.cloudmachine.struc;

import java.io.Serializable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/4/10 下午2:06
 * 修改人：shixionglu
 * 修改时间：2017/4/10 下午2:06
 * 修改备注：
 */

public class UserInfo implements Serializable{

    public ExcamMasterInfo userinfo;

    @Override
    public String toString() {
        return "UserInfo{" +
                "userinfo=" + userinfo +
                '}';
    }
}
