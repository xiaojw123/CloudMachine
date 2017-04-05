package com.cloudmachine.recyclerbean;

import java.io.Serializable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/4/1 下午4:34
 * 修改人：shixionglu
 * 修改时间：2017/4/1 下午4:34
 * 修改备注：
 */

public class CheckNumBean implements Serializable {

    public long type;

    @Override
    public String toString() {
        return "CheckNumBean{" +
                "type=" + type +
                '}';
    }
}
