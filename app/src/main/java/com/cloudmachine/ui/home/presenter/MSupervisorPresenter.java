package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.AllianceItem;
import com.cloudmachine.bean.RepairListInfo;
import com.cloudmachine.bean.UnfinishedBean;
import com.cloudmachine.helper.OrderStatus;
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

    //获取加盟站数据,若没有，拉取用户报修记录
    @Override
    public void getAllianceItemView(final long memberId) {
        mRxManage.add(mModel.getAllianceList(memberId).subscribe(new RxSubscriber<List<AllianceItem>>(mContext) {
            @Override
            protected void _onNext(List<AllianceItem> itemList) {
                if (itemList != null && itemList.size() > 0) {
                    AllianceItem item = itemList.get(0);
                    UnfinishedBean bean = new UnfinishedBean();
                    bean.setAlliance(true);
                    bean.setVbrandname(item.getBrandName());
                    bean.setVmaterialname(item.getModelName());
                    bean.setDopportunity(item.getGmtCreateStr());
                    bean.setVdiscription(item.getDemandDescription());
                    bean.setVmacoptel(item.getReporterMobile());
                    List<String> logoUrls = item.getAttachmentUrls();
                    if (logoUrls != null && logoUrls.size() > 0) {
                        bean.setLogo_flag("1");
                    } else {
                        bean.setLogo_flag("0");
                    }
                    if (item.getIsEvaluate() == 0) {
                        bean.setIs_EVALUATE("N");
                    } else {
                        bean.setIs_EVALUATE("Y");
                    }
                    String nStatusString = item.getOrderStatusValue();
                    bean.setNstatus(String.valueOf(item.getOrderStatus()));
                    switch (bean.getNstatus()) {
                        case "3"://待付款
                            nStatusString = OrderStatus.PAY;
                            break;
                        case "4":
                        case "5":
                        case "6":
                            if ("N".equals(bean.getIs_EVALUATE())) {
                                nStatusString = OrderStatus.EVALUATION;
                            } else {
                                nStatusString = OrderStatus.COMPLETED;
                            }
                            break;
                    }

                    bean.setOrderNum(item.getOrderNo());
//                    bean.setVmacoptel(item.getArtificerMobile());
                    mView.returnRepairItemView(bean, nStatusString);
                } else {
                    getRepairItemView(memberId);
                }
            }

            @Override
            protected void _onError(String message) {
                getRepairItemView(memberId);
            }
        }));


    }


    public String getStatusStr(UnfinishedBean info) {
        String statusStr = "等待接单";
        String isEvalue = info.getIs_EVALUATE();
        String nsStatus = info.getNstatus();
        int type = info.getNloanamount_TYPE();
        if ("0".equals(nsStatus)) {
            statusStr = "等待接单";
        } else if ("1".equals(nsStatus)) {
            statusStr = "进行中";
        } else if ("2".equals(nsStatus)) {
            statusStr = "进行中";
        } else if ("3".equals(nsStatus)) {
            statusStr = "进行中";
        } else if ("4".equals(nsStatus)) {
            statusStr = "进行中";
        } else if ("5".equals(nsStatus)) {
            statusStr = "进行中";
        } else if ("6".equals(nsStatus)) {
            statusStr = "进行中";

        } else if ("7".equals(nsStatus)) {
            statusStr = "进行中";
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
                            statusStr = "待支付";
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
            statusStr = "已完工";
            if ("N".equals(isEvalue)) {
                statusStr = "待评价";
            }
        } else if ("10".equals(nsStatus)) {
            statusStr = "已丢单";
        }
        return statusStr;
    }


}
