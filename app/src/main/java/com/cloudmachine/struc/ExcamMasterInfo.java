package com.cloudmachine.struc;

import java.io.Serializable;

/**
 * 项目名称：CloudMachine
 * 类描述：由云机械用户id拿到挖机大师用户id和用户角色
 * 创建人：shixionglu
 * 创建时间：2017/4/10 下午1:52
 * 修改人：shixionglu
 * 修改时间：2017/4/10 下午1:52
 * 修改备注：
 */

public class ExcamMasterInfo implements Serializable{

    public Long id;
    public Long role_id;
    public Long status;

    @Override
    public String toString() {
        return "ExcamMasterInfo{" +
                "id=" + id +
                ", role_id=" + role_id +
                ", status=" + status +
                '}';
    }
}
