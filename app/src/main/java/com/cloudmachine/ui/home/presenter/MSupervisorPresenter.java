package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.struc.RepairListInfo;
import com.cloudmachine.struc.UnfinishedBean;
import com.cloudmachine.ui.home.contract.MSupervisorContract;
import com.cloudmachine.ui.home.model.SiteBean;
import com.cloudmachine.utils.Constants;

import java.util.List;

/**
 * Created by xiaojw on 2017/6/7.
 */

public class MSupervisorPresenter extends MSupervisorContract.Preseneter {
    @Override
    public void updateStationView(double lng, double lat) {
        mRxManage.add(mModel.getSiteStation(lng, lat).subscribe(new RxSubscriber<SiteBean>(mContext, false) {
            @Override
            protected void _onNext(SiteBean siteBean) {
                mView.returnStationView(siteBean);

            }

            @Override
            protected void _onError(String message) {

            }
        }));

    }

    @Override
    public void getRepairItemView(long memberId) {
        mRxManage.add(mModel.getRepairList(memberId).subscribe(new RxSubscriber<RepairListInfo>(mContext, false) {
            @Override
            protected void _onNext(RepairListInfo repairListInfos) {
                if (repairListInfos != null) {
                    List<UnfinishedBean> unfinishedList = repairListInfos.getUnfinished();
                    if (unfinishedList.size() < 1) {
                        return;
                    }
                    UnfinishedBean bean = unfinishedList.get(0);
                    mView.returnRepairItemView(bean, getStatusStr(bean));
                }
            }


            @Override
            protected void _onError(String message) {

            }
        }));

    }


    public String getStatusStr(UnfinishedBean info) {
        String statusStr = "已报修";
        String isEvalue = info.getIs_EVALUATE();
        String nsStatus = info.getNstatus();
        int type = info.getNloanamount_TYPE();
        if ("0".equals(nsStatus)) {
            statusStr = "已下单";
        } else if ("1".equals(nsStatus)) {
            statusStr = "已派工";
        } else if ("2".equals(nsStatus)) {
            statusStr = "已技师确认";
        } else if ("3".equals(nsStatus)) {
            statusStr = "已预约";
        } else if ("4".equals(nsStatus)) {
            statusStr = "已出发";
        } else if ("5".equals(nsStatus)) {
            statusStr = "已到达";
        } else if ("6".equals(nsStatus)) {
            statusStr = "已客户确认";
        } else if ("7".equals(nsStatus)) {
            statusStr = "已结算";
            if ("N".equals(isEvalue)) {
                statusStr = "待评价";
            }
        } else if ("8".equals(nsStatus)) {
            switch (type) {
                case 0://正常状态(已付款,直接判断是否评价)
                    if ("N".equals(isEvalue)) {
                        //未评价
                        statusStr = "待评价";
                    } else if ("Y".equals(isEvalue)) {
                        statusStr = "已完工";
                    }
                    break;
                case -1://未知状态
                    try {
                        if (Double.parseDouble(info.getNloanamount()) > 0.0) {
                            statusStr = "待付款";
                        } else {
                            if ("N".equals(isEvalue)) {
                                statusStr = "待评价";
                            } else if ("Y".equals(isEvalue)) {
                                statusStr = "已完工";
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Constants.ToastAction("服务器传参数错误");
                    }
                    break;
                case 1:
                    if (Double.parseDouble(info.getNloanamount()) > 0.0) {
                        statusStr = "待支付";
                    } else {
                        if ("N".equals(isEvalue)) {
                            statusStr = "待评价";
                        } else if ("Y".equals(isEvalue)) {
                            statusStr = "已完工";
                        }
                    }
                    break;
            }
        } else if ("9".equals(nsStatus)) {
            statusStr = "已返程";
            if ("N".equals(isEvalue)) {
                statusStr = "待评价";
            }
        } else if ("10".equals(nsStatus)) {
            statusStr = "已丢单";
        }
        return statusStr;
    }


}
