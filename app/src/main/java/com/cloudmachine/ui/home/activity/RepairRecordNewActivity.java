package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.activities.EvaluationActivity;
import com.cloudmachine.activities.RepairPayDetailsActivity;
import com.cloudmachine.adapter.RepairListAdapter;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.AllianceItem;
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
import java.util.List;

public class RepairRecordNewActivity extends BaseAutoLayoutActivity<RepairHistoryPresenter, RepairHistoryModel> implements RepairHistoryContract.View, XListView.IXListViewListener, AdapterView.OnItemClickListener, View.OnClickListener {

    private View noRepairRecord, have_repair_records;
    private RadiusButtonView btnRepairNow;
    private XListView xlvComplete, allianceXlv;
    // private View completedHeaderView;
    private View uncompleteView;
    // private XListView xlvUnComplete;

    private RepairListAdapter repairListAdapter, allianceListAdapter;
    private LinearLayout rlRepairNow;
    private RadiusButtonView btnBottomRepair;
    private RepairListInfo repairListInfo;
    private ArrayList<FinishBean> finishBeans = new ArrayList<>(); // 已完成集合
    private ArrayList<UnfinishedBean> unfinishedBeans = new ArrayList<>();// 未完成集合
    private ArrayList<RepairHistoryInfo> repairList = new ArrayList<>();// 合并两个集合
    private ArrayList<RepairHistoryInfo> allianceList = new ArrayList<>();
    private long deviceId;
    LinearLayout navLlt;
    TextView allTv, areaTv;
    int pageNum = 1;
    boolean isLastPage;

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
        navLlt = (LinearLayout) findViewById(R.id.repair_nav_container);
        allTv = (TextView) findViewById(R.id.repair_all_tv);
        areaTv = (TextView) findViewById(R.id.repair_area_tv);
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
        allianceXlv = (XListView) findViewById(R.id.repair_alliance_xlv);
        xlvComplete = (XListView) findViewById(R.id.xlv_complete); // 已完成的listview
        repairListAdapter = new RepairListAdapter(RepairRecordNewActivity.this, repairList);
        allianceListAdapter = new RepairListAdapter(this, allianceList);
        xlvComplete.setPullRefreshEnable(true);
        xlvComplete.setPullLoadEnable(false);
        xlvComplete.setXListViewListener(this);
        xlvComplete.setAdapter(repairListAdapter);
        xlvComplete.setOnItemClickListener(this);
        allianceXlv.setPullRefreshEnable(true);
        allianceXlv.setPullLoadEnable(false);
        allianceXlv.setXListViewListener(this);
        allianceXlv.setAdapter(allianceListAdapter);
        allianceXlv.setOnItemClickListener(this);
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
        allTv.setSelected(true);
        areaTv.setSelected(false);
        allTv.setOnClickListener(this);
        areaTv.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.REPAIR_HISTORY);
        mPresenter.getRepairList(deviceId, false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        String orderStatus = (String) view.getTag(R.id.order_status);
        RepairHistoryInfo info = (RepairHistoryInfo) view.getTag(R.id.order_item);
        if (info == null) {
            return;
        }
        String orderNum = info.getOrderNum();
        String flag = info.getFlag();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isAlliance", info.isAlliance());
        switch (orderStatus) {
            case OrderStatus.PAY:
                bundle.putString("orderNum", orderNum);
                bundle.putString("flag", flag);
                Constants.toActivity(RepairRecordNewActivity.this, RepairPayDetailsActivity.class, bundle);
                break;
            case OrderStatus.EVALUATION:
                bundle.putString("orderNum", orderNum);
                bundle.putString("tel", info.getVmacoptel());
                Constants.toActivity(RepairRecordNewActivity.this, EvaluationActivity.class, bundle);
                break;
            case OrderStatus.COMPLETED:
                bundle.putString("orderNum", orderNum);
                bundle.putString("flag", flag);
                bundle.putString("tel", info.getVmacoptel());
                Constants.toActivity(RepairRecordNewActivity.this, RepairFinishDetailActivity.class, bundle);
                break;
            default:
                bundle.putString("orderNum", orderNum);
                bundle.putString("nstatus", orderStatus);
                bundle.putString("flag", flag);
                bundle.putString("orderStatus",info.getNstatus());
                Constants.toActivity(RepairRecordNewActivity.this, RepairDetailMapActivity.class, bundle);
                break;
        }
    }

    @Override
    public void onRefresh() {
        if (xlvComplete.getVisibility() == View.VISIBLE) {
            mPresenter.getRepairList(deviceId, true);
        } else {
            mPresenter.getAllianceList();
        }
    }

    @Override
    public void onLoadMore() {
        if (allianceXlv.getVisibility() == View.VISIBLE) {
            if (!isLastPage) {
                ++pageNum;
                mPresenter.getAllianceList(pageNum);
            } else {
                allianceXlv.stopLoadMore();
            }
        }
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
            repairListAdapter.setUnFinishedNum(unfinishedBeans.size());
            repairListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void returnGetRepairError() {
        xlvComplete.stopRefresh();
        xlvComplete.stopLoadMore();
    }

    @Override
    public void retrunGetAllianceList(List<AllianceItem> itemList, boolean isLastPage, int pageNum) {
        allianceXlv.stopLoadMore();
        allianceXlv.stopRefresh();
        if (pageNum <= 1) {
            allianceList.clear();
        }
        if (itemList != null && itemList.size() > 0) {
            this.isLastPage = isLastPage;
            this.pageNum = pageNum;
            if (!isLastPage&&pageNum==1) {
                allianceXlv.setPullLoadEnable(true);
            }
            for (AllianceItem item : itemList) {
                RepairHistoryInfo info = new RepairHistoryInfo();
                info.setOrderNum(item.getOrderNo());
                info.setAlliance(true);
                info.setNstatus(String.valueOf(item.getOrderStatus()));
                info.setVbrandname(item.getBrandName());
                info.setVmacoptel(item.getReporterMobile());
                info.setVmaterialname(item.getModelName());
                info.setVdiscription(item.getDemandDescription());
                info.setDopportunity(item.getGmtCreateStr());
                List<String> urls = item.getAttachmentUrls();
                if (urls != null && urls.size() > 0) {
                    info.setLogo_flag("1");
                } else {
                    info.setLogo_flag("0");
                }
                if (item.getIsEvaluate() == 0) {
                    info.setIs_EVALUATE("N");
                } else {
                    info.setIs_EVALUATE("Y");
                }
                info.setOrderStatusValue(item.getOrderStatusValue());
                allianceList.add(info);
            }
            allianceListAdapter.notifyDataSetChanged();
        }
        if (repairList.size() > 0 && allianceList.size() > 0) {
            navLlt.setVisibility(View.VISIBLE);
            if (allTv.isSelected()) {
                allianceXlv.setVisibility(View.VISIBLE);
                xlvComplete.setVisibility(View.GONE);
            } else {
                allianceXlv.setVisibility(View.GONE);
                xlvComplete.setVisibility(View.VISIBLE);
            }
            have_repair_records.setVisibility(View.VISIBLE);
            noRepairRecord.setVisibility(View.GONE);
        } else if (repairList.size() == 0 && allianceList.size() == 0) {
            have_repair_records.setVisibility(View.GONE);
            noRepairRecord.setVisibility(View.VISIBLE);
        } else {
            navLlt.setVisibility(View.GONE);
            have_repair_records.setVisibility(View.VISIBLE);
            noRepairRecord.setVisibility(View.GONE);
            if (repairList.size() > 0 && allianceList.size() == 0) {
                allianceXlv.setVisibility(View.GONE);
                xlvComplete.setVisibility(View.VISIBLE);
            } else if (allianceList.size() > 0 && repairList.size() == 0) {
                allianceXlv.setVisibility(View.VISIBLE);
                xlvComplete.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void returnGetAllianceError() {
        retrunGetAllianceList(null, false, 1);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.repair_all_tv:
                if (allTv.isSelected()) {
                    return;
                }
                allTv.setSelected(true);
                areaTv.setSelected(false);
                xlvComplete.setVisibility(View.GONE);
                allianceXlv.setVisibility(View.VISIBLE);
                break;
            case R.id.repair_area_tv:
                if (areaTv.isSelected()) {
                    return;
                }
                allTv.setSelected(false);
                areaTv.setSelected(true);
                xlvComplete.setVisibility(View.VISIBLE);
                allianceXlv.setVisibility(View.GONE);
                break;

        }


    }
}
