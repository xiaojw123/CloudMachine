package com.cloudmachine.struc;

import java.util.ArrayList;

/**
 * Created by shixionglu on 2016/10/26.
 * 报修历史详情
 */

public class RepairListInfo {


    /**
     * flag : 1
     * price : 1.0
     * dopportunity : 2016-02-29 17:30:13
     * vmachinenum : 000000
     * vbrandname : 斗山
     * vmaterialname : DH225LC-9
     * vmacopname : 测试
     * vdiscription : 更换螺丝
     * is_EVALUATE : N
     * vprodname : 挖掘机
     * vmacoptel : 15357907070
     * nstatus : 8
     * orderNum : RO201602290084
     */
	
	

    private ArrayList<FinishBean> finish;
    

	/**
     * flag : 1
     * price : 1.0
     * dopportunity : 2016-02-29 17:30:13
     * vmachinenum : 000000
     * vbrandname : 斗山
     * vmaterialname : DH225LC-9
     * vmacopname : 测试
     * vdiscription : 更换螺丝
     * is_EVALUATE : N
     * vprodname : 挖掘机
     * vmacoptel : 15357907070
     * nstatus : 8
     * orderNum : RO201602290084
     */

    private ArrayList<UnfinishedBean> unfinished;

    public ArrayList<FinishBean> getFinish() {
        return finish;
    }

    public void setFinish(ArrayList<FinishBean> finish) {
        this.finish = finish;
    }

    public ArrayList<UnfinishedBean> getUnfinished() {
        return unfinished;
    }

    public void setUnfinished(ArrayList<UnfinishedBean> unfinished) {
        this.unfinished = unfinished;
    }

    @Override
	public String toString() {
		return "RepairListInfo [finish=" + finish + ", unfinished="
				+ unfinished + "]";
	}

   
}
