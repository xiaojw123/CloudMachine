package com.cloudmachine.struc;

import java.io.Serializable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/2 下午3:56
 * 修改人：shixionglu
 * 修改时间：2017/3/2 下午3:56
 * 修改备注：
 */

public class YunBoxStoreVolumeInfo implements Serializable {

    private long store_volume;
    private boolean result;

    public long getStore_volume() {
        return store_volume;
    }

    public void setStore_volume(long store_volume) {
        this.store_volume = store_volume;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "YunBoxStoreVolumeInfo{" +
                "store_volume=" + store_volume +
                ", result=" + result +
                '}';
    }
}
