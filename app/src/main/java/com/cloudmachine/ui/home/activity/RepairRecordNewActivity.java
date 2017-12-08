package com.cloudmachine.ui.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.cloudmachine.R;
import com.cloudmachine.activities.EvaluationActivity;
import com.cloudmachine.activities.RepairPayDetailsActivity;
import com.cloudmachine.adapter.RepairListAdapter;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.FinishBean;
import com.cloudmachine.bean.RepairHistoryInfo;
import com.cloudmachine.bean.RepairListInfo;
import com.cloudmachine.bean.UnfinishedBean;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.OrderStatus;
import com.cloudmachine.ui.home.contract.RepairHistoryContract;
import com.cloudmachine.ui.home.model.RepairHistoryModel;
import com.cloudmachine.ui.home.presenter.RepairHistoryPresenter;
import com.cloudmachine.ui.repair.activity.NewRepairActivity;
import com.cloudmachine.ui.repair.activity.RepairFinishDetailActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.widgets.XListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

public class RepairRecordNewActivity extends BaseAutoLayoutActivity<RepairHistoryPresenter, RepairHistoryModel> implements RepairHistoryContract.View, Handler.Callback, XListView.IXListViewListener, AdapterView.OnItemClickListener {

    private Handler mHandler;
    private View noRepairRecord, have_repair_records;
    private RadiusButtonView btnRepairNow;
    private XListView xlvComplete;
    // private View completedHeaderView;
    private View uncompleteView;
    // private XListView xlvUnComplete;

    private RepairListAdapter repairListAdapter;
    private LinearLayout rlRepairNow;
    private RadiusButtonView btnBottomRepair;
    private RepairListInfo repairListInfo;
    private ArrayList<FinishBean> finishBeans = new ArrayList<>(); // 已完成集合
    private ArrayList<UnfinishedBean> unfinishedBeans = new ArrayList<>();// 未完成集合
    private ArrayList<RepairHistoryInfo> repairList = new ArrayList<>();// 合并两个集合
    private long deviceId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_device_repair);
        initView();
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    protected void initView() {
        mHandler = new Handler(this);
        noRepairRecord = findViewById(R.id.no_repair_records); // 无维修记录布局
        have_repair_records = findViewById(R.id.have_repair_records); // 有维修记录布局
        btnRepairNow = (RadiusButtonView) findViewById(R.id.btn_repair_now); // 立刻报修
        btnRepairNow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Constants.toActivity(RepairRecordNewActivity.this, NewRepairActivity.class, null);
            }
        });
        xlvComplete = (XListView) findViewById(R.id.xlv_complete); // 已完成的listview
        repairListAdapter = new RepairListAdapter(RepairRecordNewActivity.this, repairList);
        xlvComplete.setPullRefreshEnable(true);
        xlvComplete.setPullLoadEnable(false);
        xlvComplete.setXListViewListener(this);
        xlvComplete.setAdapter(repairListAdapter);
        xlvComplete.setOnItemClickListener(this);
        rlRepairNow = (LinearLayout) findViewById(
                R.id.rl_repair_now);

        btnBottomRepair = (RadiusButtonView) findViewById(
                R.id.btn_bottom_repair);
        btnBottomRepair.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Constants.toActivity(RepairRecordNewActivity.this, NewRepairActivity.class, null);
            }

        });
        deviceId = getIntent().getLongExtra(Constants.MC_DEVICEID, Constants.INVALID_DEVICE_ID);
        if (deviceId == Constants.INVALID_DEVICE_ID) {
            rlRepairNow.setVisibility(View.VISIBLE);
        }
    }


    private void showData() {

        if ((null != repairList && repairList.size() > 0)) {
            have_repair_records.setVisibility(View.VISIBLE);
            noRepairRecord.setVisibility(View.GONE);
        } else {
            have_repair_records.setVisibility(View.GONE);
            noRepairRecord.setVisibility(View.VISIBLE);
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.HANDLER_GET_REPAIRHISTORY_SUCCESS:
//                updateView(msg);
                break;
            case Constants.HANDLER_GET_REPAIRHISTORY_FAILD:
                showData();
                break;
            default:
                break;
        }

        return false;
    }

    private void updateView(RepairListInfo info) {

    }

//    private void TestCode() {
//        ArrayList<FinishBean> finishBeens = new ArrayList<>();
//        FinishBean bean1 = new FinishBean();
//        bean1.setVbrandname("何苗");
//        bean1.setVprodname("挖掘机");
//        bean1.setNloanamount("10");
//        bean1.setVprodname("12390131");
//        bean1.setLogo_flag("1");
//        bean1.setFlag("1");
//        bean1.setNstatus("12");
//        bean1.setVmacoptel("15268168675");
//        bean1.setIs_EVALUATE("Y");
//        bean1.setDopportunity("fafasfajlasuioruwd");
//        bean1.setPrice("100");
//        bean1.setNloanamount_TYPE(1);
//        bean1.setOrderNum("131341241234");
//        bean1.setVmachinenum("23333");
//        bean1.setVmacopname("dfasfas");
//        finishBeens.add(bean1);
//        FinishBean bean2 = new FinishBean();
//        bean2.setVbrandname("li mid");
//        bean2.setVprodname("挖掘机");
//        bean2.setNloanamount("1012");
//        bean2.setVprodname("12390112331");
//        bean2.setLogo_flag("1");
//        bean2.setFlag("1");
//        bean2.setNstatus("12");
//        bean2.setVmacoptel("15268168675");
//        bean2.setIs_EVALUATE("N");
//        bean2.setDopportunity("fafasfajlasuioruwd");
//        bean2.setPrice("100");
//        bean2.setNloanamount_TYPE(1);
//        bean2.setOrderNum("131341241234");
//        bean2.setVmachinenum("23333");
//        bean2.setVmacopname("dfasfas");
//        finishBeens.add(bean2);
//        repairListInfo.setFinish(finishBeens);
//    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.REPAIR_HISTORY);
        // Constants.MyLog("获取焦点");
//        new AllRepairHistoryAsync(RepairRecordNewActivity.this, mHandler).execute();
        //MobclickAgent.onPageStart(this.getClass().getName());
        //MobclickAgent.onPageStart(UMengKey.time_repair_history);
        mPresenter.getRepairList(deviceId);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        String orderStatus = (String) view.getTag(R.id.order_status);
        RepairHistoryInfo info = (RepairHistoryInfo) view.getTag(R.id.order_item);
        if (info==null){
            return;
        }
        String orderNum = info.getOrderNum();
        String flag = info.getFlag();
        switch (orderStatus) {
            case OrderStatus.PAY:
                Bundle b = new Bundle();
                b.putString("orderNum", orderNum);
                b.putString("flag", flag);
                Constants.toActivity(RepairRecordNewActivity.this, RepairPayDetailsActivity.class, b, false);
                break;
            case OrderStatus.EVALUATION:
                Bundle eBundle = new Bundle();
                eBundle.putString("orderNum", orderNum);
                eBundle.putString("tel", info.getVmacoptel());
                Constants.toActivity(RepairRecordNewActivity.this, EvaluationActivity.class, eBundle);
                break;
            case OrderStatus.COMPLETED:
                Intent intent = new Intent(RepairRecordNewActivity.this, RepairFinishDetailActivity.class);
                intent.putExtra("orderNum", orderNum);
                intent.putExtra("flag", flag);
                startActivity(intent);
                break;
            default:
                Bundle dBundle = new Bundle();
                dBundle.putString("orderNum", orderNum);
                dBundle.putString("nstatus", orderStatus);
                dBundle.putString("flag", flag);
                Constants.toActivity(RepairRecordNewActivity.this, RepairDetailMapActivity.class, dBundle);
                break;
        }

//
//        if (null != repairList && repairList.size() > 0            //跳转单个报修详情页面
//                ) {
//
//            if (temp < unfinishedBeans.size()) {
//
//                if (unfinishedBeans.get(temp).getNstatus().equals("8")) {
//                    switch (unfinishedBeans.get(temp).getNloanamount_TYPE()) {
//                        case 0://正常状态(已付款,直接判断是否评价)
//                            if (unfinishedBeans.get(temp).getIs_EVALUATE().equals("N")) {
//                                //未评价
//                                //nStatusString = "待评价";
//                            } else if (unfinishedBeans.get(temp).getIs_EVALUATE().equals("Y")) {
//                                //nStatusString = "已完工";
//                            }
//                            break;
//                        case -1://未知状态
//                            try {
//                                if (Double.parseDouble(unfinishedBeans.get(temp).getNloanamount()) > 0.0) {
//                                    //nStatusString = "待付款";
//                                } else {
//                                    if (unfinishedBeans.get(temp).getIs_EVALUATE().equals("N")) {
//                                        //nStatusString = "待评价";
//                                    } else if (unfinishedBeans.get(temp).getIs_EVALUATE().equals("Y")) {
//                                        //nStatusString = "已完工";
//                                    }
//                                }
//                            } catch (NumberFormatException e) {
//                                e.printStackTrace();
//                                Constants.ToastAction("服务器传参数错误");
//                            }
//                            break;
//                        case 1:
//                            if (Double.parseDouble(unfinishedBeans.get(temp).getNloanamount()) > 0.0) {
//                                //nStatusString = "待支付";
//                                String orderNum = unfinishedBeans.get(temp).getOrderNum();
//                                String flag = unfinishedBeans.get(temp).getFlag();
//                                Bundle b = new Bundle();
//                                b.putString("orderNum", orderNum);
//                                b.putString("flag", flag);
//                                Constants.toActivity(RepairRecordNewActivity.this, RepairPayDetailsActivity.class, b, false);
//                            } else {
//                                if (unfinishedBeans.get(temp).getIs_EVALUATE().equals("N")) {
//                                    //	nStatusString = "待评价";
//                                } else if (unfinishedBeans.get(temp).getIs_EVALUATE().equals("Y")) {
//                                    //nStatusString = "已完工";
//                                }
//                            }
//                            break;
//                    }
//                } else {
//
//                }
//
//            } else {
//                int p = temp - unfinishedBeans.size();
//                if (finishBeans.get(p).getIs_EVALUATE().equals("N")) {
//
//                } else {
//
//                }
//            }
//
//        }
    }


    @Override
    public void onRefresh() {
//        new AllRepairHistoryAsync(RepairRecordNewActivity.this, mHandler).execute();
        mPresenter.getRepairList(deviceId);

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void returnGetRepairList(RepairListInfo info) {
        xlvComplete.stopRefresh();
        xlvComplete.stopLoadMore();
        finishBeans.clear();
        unfinishedBeans.clear();
        repairList.clear();
        if (null != info) {
            repairListInfo = info;
            noRepairRecord.setVisibility(View.GONE);
            xlvComplete.setVisibility(View.VISIBLE);
//                    TestCode();
            ArrayList<FinishBean> finish = repairListInfo.getFinish();
            ArrayList<UnfinishedBean> unfinished = repairListInfo
                    .getUnfinished();
        /*int finishLen = finish.size();
        for(int f=0; f<finishLen; f++){
            finishBeans.add(finish.get(finishLen-1-f));
        }
        int ufinishLen = unfinished.size();
        for(int uf=0; uf<ufinishLen; uf++){
            unfinishedBeans.add(unfinished.get(ufinishLen-1-uf));
        }*/
            if (null != finish) {
                finishBeans.addAll(finish);
            }
            if (null != unfinished) {
                unfinishedBeans.addAll(unfinished);
            }

            int count = finishBeans.size() + unfinishedBeans.size();
            int finishCount = 0;
            for (int i = 0; i < count; i++) {
                RepairHistoryInfo historyInfo = new RepairHistoryInfo();
                if (i < unfinishedBeans.size()) {
                    historyInfo.setLogo_flag(unfinishedBeans.get(i).getLogo_flag());
                    historyInfo.setFlag(unfinishedBeans.get(i).getFlag());
                    historyInfo.setPrice(unfinishedBeans.get(i).getPrice());
                    historyInfo.setDopportunity(unfinishedBeans.get(i)
                            .getDopportunity());
                    historyInfo.setVmachinenum(unfinishedBeans.get(i)
                            .getVmachinenum());
                    historyInfo.setVbrandname(unfinishedBeans.get(i)
                            .getVbrandname());
                    historyInfo.setVmaterialname(unfinishedBeans.get(i)
                            .getVmaterialname());
                    historyInfo.setVmacopname(unfinishedBeans.get(i)
                            .getVmacopname());
                    historyInfo.setVdiscription(unfinishedBeans.get(i)
                            .getVdiscription());
                    historyInfo.setIs_EVALUATE(unfinishedBeans.get(i)
                            .getIs_EVALUATE());
                    historyInfo.setVprodname(unfinishedBeans.get(i)
                            .getVprodname());
                    historyInfo.setVmacoptel(unfinishedBeans.get(i)
                            .getVmacoptel());
                    historyInfo.setNstatus(unfinishedBeans.get(i)
                            .getNstatus());
                    historyInfo.setOrderNum(unfinishedBeans.get(i)
                            .getOrderNum());
                    historyInfo.setNloanamount(unfinishedBeans.get(i).getNloanamount());
                    historyInfo.setNloanamount_TYPE(unfinishedBeans.get(i).getNloanamount_TYPE());
                    //Constants.MyLog(historyInfo.toString());
                } else {
                    historyInfo.setLogo_flag(finishBeans.get(finishCount).getLogo_flag());
                    historyInfo.setFlag(finishBeans.get(finishCount)
                            .getFlag());
                    historyInfo.setPrice(finishBeans.get(finishCount)
                            .getPrice());
                    historyInfo.setDopportunity(finishBeans
                            .get(finishCount).getDopportunity());
                    historyInfo.setVmachinenum(finishBeans.get(finishCount)
                            .getVmachinenum());
                    historyInfo.setVbrandname(finishBeans.get(finishCount)
                            .getVbrandname());
                    historyInfo.setVmaterialname(finishBeans.get(
                            finishCount).getVmaterialname());
                    historyInfo.setVmacopname(finishBeans.get(finishCount)
                            .getVmacopname());
                    historyInfo.setVdiscription(finishBeans
                            .get(finishCount).getVdiscription());
                    historyInfo.setIs_EVALUATE(finishBeans.get(finishCount)
                            .getIs_EVALUATE());
                    historyInfo.setVprodname(finishBeans.get(finishCount)
                            .getVprodname());
                    historyInfo.setVmacoptel(finishBeans.get(finishCount)
                            .getVmacoptel());
                    historyInfo.setNstatus(finishBeans.get(finishCount)
                            .getNstatus());
                    historyInfo.setOrderNum(finishBeans.get(finishCount)
                            .getOrderNum());
                    historyInfo.setNloanamount_TYPE(finishBeans.get(finishCount).getNloanamount_TYPE());
                    historyInfo.setNloanamount(finishBeans.get(finishCount).getNloanamount());
                    finishCount++;

                }
                repairList.add(historyInfo);

            }
            showData();
            repairListAdapter.setUnFinishedNum(unfinishedBeans.size());
            repairListAdapter.notifyDataSetChanged();
        } else {
            xlvComplete.setVisibility(View.GONE);
            noRepairRecord.setVisibility(View.VISIBLE);
            btnBottomRepair.setVisibility(View.GONE);
        }
    }

    @Override
    public void returnGetRepairError() {
        showData();
    }
}
