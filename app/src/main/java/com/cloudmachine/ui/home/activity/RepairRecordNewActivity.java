package com.cloudmachine.ui.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cloudmachine.R;
import com.cloudmachine.activities.EvaluationActivity;
import com.cloudmachine.activities.RepairPayDetailsActivity;
import com.cloudmachine.adapter.BaseRecyclerAdapter;
import com.cloudmachine.adapter.RepairListAdapter;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.base.bean.PageBean;
import com.cloudmachine.bean.RepairHistoryInfo;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.OrderStatus;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.login.acticity.LoginActivity;
import com.cloudmachine.ui.repair.activity.NewRepairActivity;
import com.cloudmachine.ui.repair.activity.RepairFinishDetailActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepairRecordNewActivity extends BaseAutoLayoutActivity implements XRecyclerView.LoadingListener, BaseRecyclerAdapter.OnItemClickListener {

    private RepairListAdapter repairListAdapter;
    @BindView(R.id.repair_list_xlv)
    XRecyclerView repairListRlv;
    @BindView(R.id.no_repair_records)
    LinearLayout noRepairRecord;
    @BindView(R.id.have_repair_records)
    RelativeLayout have_repair_records;
    @BindView(R.id.btn_repair_now)
    RadiusButtonView btnRepairNow;
    @BindView(R.id.rl_repair_now)
    LinearLayout rlRepairNow;
    @BindView(R.id.btn_bottom_repair)
    RadiusButtonView btnBottomRepair;
    String deviceIdStr;
    List<RepairHistoryInfo> mToalItems = new ArrayList<>();
    boolean isRefesh, isLoadMore, isLastPage;
    int pageNum = 1;
    boolean isLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_device_repair);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initPresenter() {
    }

    protected void initView() {
        repairListRlv.setLayoutManager(new LinearLayoutManager(mContext));
        repairListRlv.setPullRefreshEnabled(true);
        repairListRlv.setLoadingMoreEnabled(true);
        repairListRlv.setLoadingListener(this);
        btnRepairNow.setOnClickListener(repairClickLi);
        btnBottomRepair.setOnClickListener(repairClickLi);
        if (UserHelper.isLogin(this)) {
            isLogin = true;
            long deviceId = getIntent().getLongExtra(Constants.MC_DEVICEID, Constants.INVALID_DEVICE_ID);
            if (deviceId == Constants.INVALID_DEVICE_ID) {
                rlRepairNow.setVisibility(View.VISIBLE);
            } else {
                deviceIdStr = String.valueOf(deviceId);
            }
        } else {
            noRepairRecord.setVisibility(View.VISIBLE);
            have_repair_records.setVisibility(View.GONE);
            btnRepairNow.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener repairClickLi = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Constants.toActivity(RepairRecordNewActivity.this, NewRepairActivity.class, null);
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.REPAIR_HISTORY);
        if (UserHelper.isLogin(this)) {
            pageNum = 1;
            updateRepairList();
        }
    }

    public void updateRepairList() {
        mRxManager.add(Api.getDefault(HostType.HOST_LARK).getRepairList(deviceIdStr, pageNum)
                .compose(RxHelper.<List<RepairHistoryInfo>>handleBaseResult())
                .subscribe(new RxSubscriber<BaseRespose<List<RepairHistoryInfo>>>(mContext) {
                               @Override
                               protected void _onNext(BaseRespose<List<RepairHistoryInfo>> br) {
                                   resetRecyclerScroll();
                                   if (br.isSuccess()) {
                                       List<RepairHistoryInfo> items = br.getResult();
                                       PageBean page = br.getPage();
                                       if (page != null) {
                                           isLastPage = page.last;
                                           if (page.first) {
                                               pageNum = 1;
                                           }
                                       } else {
                                           pageNum = 1;
                                       }
                                       if (items != null && items.size() > 0) {
                                           if (pageNum == 1) {
                                               have_repair_records.setVisibility(View.VISIBLE);
                                               noRepairRecord.setVisibility(View.GONE);
                                               mToalItems.clear();
                                           }
                                           mToalItems.addAll(items);
                                           if (repairListAdapter == null) {
                                               repairListAdapter = new RepairListAdapter(mContext, mToalItems);
                                               repairListAdapter.setOnItemClickListener(RepairRecordNewActivity.this);
                                               repairListRlv.setAdapter(repairListAdapter);
                                           } else {
                                               repairListAdapter.updateItems(mToalItems);
                                           }
                                       } else {
                                           if (pageNum == 1) {
                                               have_repair_records.setVisibility(View.GONE);
                                               noRepairRecord.setVisibility(View.VISIBLE);
                                           }
                                       }

                                   } else {
                                       _onError(br.getMessage());
                                   }

                               }

                               @Override
                               protected void _onError(String message) {
                                   ToastUtils.showToast(mContext, message);
                                   resetRecyclerScroll();

                               }
                           }
                ));
    }

    private void resetRecyclerScroll() {
        if (isRefesh) {
            repairListRlv.refreshComplete();
        }
        if (isLoadMore) {
            repairListRlv.loadMoreComplete();
        }
    }

    @Override
    public void onRefresh() {
        isRefesh = true;
        pageNum = 1;
        updateRepairList();
    }

    @Override
    public void onLoadMore() {
        if (isLastPage) {
            repairListRlv.setNoMore(true);
        } else {
            pageNum++;
            updateRepairList();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        String orderStatus = (String) view.getTag(R.id.order_status);
        RepairHistoryInfo info = (RepairHistoryInfo) view.getTag();
        if (info == null) {
            return;
        }
        if (OrderStatus.CLOSED.equals(orderStatus)) {
            ToastUtils.showToast(this, "工单已关闭");
            return;
        }
        String orderNum = info.getOrderNo();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ORDER_NO, orderNum);
        switch (orderStatus) {
            case OrderStatus.PAY:
                Constants.toActivity(RepairRecordNewActivity.this, RepairPayDetailsActivity.class, bundle);
                break;
            case OrderStatus.EVALUATION:
                Constants.toActivity(RepairRecordNewActivity.this, EvaluationActivity.class, bundle);
                break;
            case OrderStatus.COMPLETED:
//                bundle.putString("tel", info.getVmacoptel());
                Constants.toActivity(RepairRecordNewActivity.this, RepairFinishDetailActivity.class, bundle);
                break;
            default:
                bundle.putString("nstatus", orderStatus);
                bundle.putString("orderStatus", info.getOrderStatusValue());
                Constants.toActivity(RepairRecordNewActivity.this, RepairDetailMapActivity.class, bundle);
                break;
        }


    }
}
// TODO: 2018/9/26 itemClick关键代码
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position,
//                            long id) {
//        String orderStatus = (String) view.getTag(R.id.order_status);
//        RepairHistoryInfo info = (RepairHistoryInfo) view.getTag(R.id.order_item);
//        if (info == null) {
//            return;
//        }
//        if (OrderStatus.CLOSED.equals(orderStatus)){
//            ToastUtils.showToast(this,"工单已关闭");
//            return;
//        }
//        String orderNum = info.getOrderNum();
//        String flag = info.getFlag();
//        Bundle bundle = new Bundle();
//        bundle.putBoolean("isAlliance", info.isAlliance());
//        switch (orderStatus) {
//            case OrderStatus.PAY:
//                bundle.putString("orderNum", orderNum);
//                bundle.putString("flag", flag);
//                Constants.toActivity(RepairRecordNewActivity.this, RepairPayDetailsActivity.class, bundle);
//                break;
//            case OrderStatus.EVALUATION:
//                bundle.putString("orderNum", orderNum);
//                bundle.putString("tel", info.getVmacoptel());
//                Constants.toActivity(RepairRecordNewActivity.this, EvaluationActivity.class, bundle);
//                break;
//            case OrderStatus.COMPLETED:
//                bundle.putString("orderNum", orderNum);
//                bundle.putString("flag", flag);
//                bundle.putString("tel", info.getVmacoptel());
//                Constants.toActivity(RepairRecordNewActivity.this, RepairFinishDetailActivity.class, bundle);
//                break;
//            default:
//                bundle.putString("orderNum", orderNum);
//                bundle.putString("nstatus", orderStatus);
//                bundle.putString("flag", flag);
//                bundle.putString("orderStatus",info.getNstatus());
//                Constants.toActivity(RepairRecordNewActivity.this, RepairDetailMapActivity.class, bundle);
//                break;
//        }
//    }


//    @Override
//    public void returnGetRepairList(RepairListInfo info) {


//        xlvComplete.stopRefresh();
//        xlvComplete.stopLoadMore();
//        finishBeans.clear();
//        unfinishedBeans.clear();
//        repairList.clear();
//        if (null != info) {
//            repairListInfo = info;
////                    TestCode();
//            ArrayList<FinishBean> finish = repairListInfo.getFinish();
//            ArrayList<UnfinishedBean> unfinished = repairListInfo
//                    .getUnfinished();
//        /*int finishLen = finish.size();
//        for(int f=0; f<finishLen; f++){
//            finishBeans.add(finish.get(finishLen-1-f));
//        }
//        int ufinishLen = unfinished.size();
//        for(int uf=0; uf<ufinishLen; uf++){
//            unfinishedBeans.add(unfinished.get(ufinishLen-1-uf));
//        }*/
//            if (null != finish) {
//                finishBeans.addAll(finish);
//            }
//            if (null != unfinished) {
//                unfinishedBeans.addAll(unfinished);
//            }
//
//            int count = finishBeans.size() + unfinishedBeans.size();
//            int finishCount = 0;
//            for (int i = 0; i < count; i++) {
//                RepairHistoryInfo historyInfo = new RepairHistoryInfo();
//                if (i < unfinishedBeans.size()) {
//                    historyInfo.setLogo_flag(unfinishedBeans.get(i).getLogo_flag());
//                    historyInfo.setFlag(unfinishedBeans.get(i).getFlag());
//                    historyInfo.setPrice(unfinishedBeans.get(i).getPrice());
//                    historyInfo.setDopportunity(unfinishedBeans.get(i)
//                            .getDopportunity());
//                    historyInfo.setVmachinenum(unfinishedBeans.get(i)
//                            .getVmachinenum());
//                    historyInfo.setVbrandname(unfinishedBeans.get(i)
//                            .getVbrandname());
//                    historyInfo.setVmaterialname(unfinishedBeans.get(i)
//                            .getVmaterialname());
//                    historyInfo.setVmacopname(unfinishedBeans.get(i)
//                            .getVmacopname());
//                    historyInfo.setVdiscription(unfinishedBeans.get(i)
//                            .getVdiscription());
//                    historyInfo.setIs_EVALUATE(unfinishedBeans.get(i)
//                            .getIs_EVALUATE());
//                    historyInfo.setVprodname(unfinishedBeans.get(i)
//                            .getVprodname());
//                    historyInfo.setVmacoptel(unfinishedBeans.get(i)
//                            .getVmacoptel());
//                    historyInfo.setNstatus(unfinishedBeans.get(i)
//                            .getNstatus());
//                    historyInfo.setOrderNum(unfinishedBeans.get(i)
//                            .getOrderNum());
//                    historyInfo.setNloanamount(unfinishedBeans.get(i).getNloanamount());
//                    historyInfo.setNloanamount_TYPE(unfinishedBeans.get(i).getNloanamount_TYPE());
//                    //Constants.MyLog(historyInfo.toString());
//                } else {
//                    historyInfo.setLogo_flag(finishBeans.get(finishCount).getLogo_flag());
//                    historyInfo.setFlag(finishBeans.get(finishCount)
//                            .getFlag());
//                    historyInfo.setPrice(finishBeans.get(finishCount)
//                            .getPrice());
//                    historyInfo.setDopportunity(finishBeans
//                            .get(finishCount).getDopportunity());
//                    historyInfo.setVmachinenum(finishBeans.get(finishCount)
//                            .getVmachinenum());
//                    historyInfo.setVbrandname(finishBeans.get(finishCount)
//                            .getVbrandname());
//                    historyInfo.setVmaterialname(finishBeans.get(
//                            finishCount).getVmaterialname());
//                    historyInfo.setVmacopname(finishBeans.get(finishCount)
//                            .getVmacopname());
//                    historyInfo.setVdiscription(finishBeans
//                            .get(finishCount).getVdiscription());
//                    historyInfo.setIs_EVALUATE(finishBeans.get(finishCount)
//                            .getIs_EVALUATE());
//                    historyInfo.setVprodname(finishBeans.get(finishCount)
//                            .getVprodname());
//                    historyInfo.setVmacoptel(finishBeans.get(finishCount)
//                            .getVmacoptel());
//                    historyInfo.setNstatus(finishBeans.get(finishCount)
//                            .getNstatus());
//                    historyInfo.setOrderNum(finishBeans.get(finishCount)
//                            .getOrderNum());
//                    historyInfo.setNloanamount_TYPE(finishBeans.get(finishCount).getNloanamount_TYPE());
//                    historyInfo.setNloanamount(finishBeans.get(finishCount).getNloanamount());
//                    finishCount++;
//
//                }
//                repairList.add(historyInfo);
//
//            }
//            repairListAdapter.setUnFinishedNum(unfinishedBeans.size());
//            repairListAdapter.notifyDataSetChanged();
//        }
//    }
//
//
//    @Override
//    public void retrunGetAllianceList(List<AllianceItem> itemList, boolean isLastPage, int pageNum) {
//        allianceXlv.stopLoadMore();
//        allianceXlv.stopRefresh();
//        if (pageNum <= 1) {
//            allianceList.clear();
//        }
//        if (itemList != null && itemList.size() > 0) {
//            this.isLastPage = isLastPage;
//            this.pageNum = pageNum;
//            if (!isLastPage&&pageNum==1) {
//                allianceXlv.setPullLoadEnable(true);
//            }
//            for (AllianceItem item : itemList) {
//                RepairHistoryInfo info = new RepairHistoryInfo();
//                info.setOrderNum(item.getOrderNo());
//                info.setAlliance(true);
//                info.setNstatus(String.valueOf(item.getOrderStatus()));
//                info.setVbrandname(item.getBrandName());
//                info.setVmacoptel(item.getReporterMobile());
//                info.setVmaterialname(item.getModelName());
//                info.setVdiscription(item.getDemandDescription());
//                info.setDopportunity(item.getGmtCreateStr());
//                List<String> urls = item.getAttachmentUrls();
//                if (urls != null && urls.size() > 0) {
//                    info.setLogo_flag("1");
//                } else {
//                    info.setLogo_flag("0");
//                }
//                if (item.getIsEvaluate() == 0) {
//                    info.setIs_EVALUATE("N");
//                } else {
//                    info.setIs_EVALUATE("Y");
//                }
//                info.setOrderStatusValue(item.getOrderStatusValue());
//                allianceList.add(info);
//            }
//            allianceListAdapter.notifyDataSetChanged();
//        }
//        if (repairList.size() > 0 && allianceList.size() > 0) {
//            navLlt.setVisibility(View.VISIBLE);
//            if (allTv.isSelected()) {
//                allianceXlv.setVisibility(View.VISIBLE);
//                xlvComplete.setVisibility(View.GONE);
//            } else {
//                allianceXlv.setVisibility(View.GONE);
//                xlvComplete.setVisibility(View.VISIBLE);
//            }
//            have_repair_records.setVisibility(View.VISIBLE);
//            noRepairRecord.setVisibility(View.GONE);
//        } else if (repairList.size() == 0 && allianceList.size() == 0) {
//            have_repair_records.setVisibility(View.GONE);
//            noRepairRecord.setVisibility(View.VISIBLE);
//        } else {
//            navLlt.setVisibility(View.GONE);
//            have_repair_records.setVisibility(View.VISIBLE);
//            noRepairRecord.setVisibility(View.GONE);
//            if (repairList.size() > 0 && allianceList.size() == 0) {
//                allianceXlv.setVisibility(View.GONE);
//                xlvComplete.setVisibility(View.VISIBLE);
//            } else if (allianceList.size() > 0 && repairList.size() == 0) {
//                allianceXlv.setVisibility(View.VISIBLE);
//                xlvComplete.setVisibility(View.GONE);
//            }
//
//        }
//    }

//    @Override
//    public void returnGetAllianceError() {
//        retrunGetAllianceList(null, false, 1);
//    }

//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.repair_all_tv:
//                if (allTv.isSelected()) {
//                    return;
//                }
//                allTv.setSelected(true);
//                areaTv.setSelected(false);
//                xlvComplete.setVisibility(View.GONE);
//                allianceXlv.setVisibility(View.VISIBLE);
//                break;
//            case R.id.repair_area_tv:
//                if (areaTv.isSelected()) {
//                    return;
//                }
//                allTv.setSelected(false);
//                areaTv.setSelected(true);
//                xlvComplete.setVisibility(View.VISIBLE);
//                allianceXlv.setVisibility(View.GONE);
//                break;
//
//        }
//
//
//    }

