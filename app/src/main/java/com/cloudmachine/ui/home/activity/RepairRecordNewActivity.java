package com.cloudmachine.ui.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.cloudmachine.R;
import com.cloudmachine.activities.EvaluationActivity;
import com.cloudmachine.activities.RepairBasicInfomationActivity;
import com.cloudmachine.activities.RepairDetailsActivity;
import com.cloudmachine.adapter.RepairListAdapter;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.net.task.AllRepairHistoryAsync;
import com.cloudmachine.struc.FinishBean;
import com.cloudmachine.struc.RepairHistoryInfo;
import com.cloudmachine.struc.RepairListInfo;
import com.cloudmachine.struc.UnfinishedBean;
import com.cloudmachine.ui.repair.activity.NewRepairActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.widgets.XListView;

import java.util.ArrayList;

public class RepairRecordNewActivity extends BaseAutoLayoutActivity implements Handler.Callback,XListView.IXListViewListener, AdapterView.OnItemClickListener {

    private Handler mHandler;
    private View noRepairRecord, have_repair_records;
    private RadiusButtonView btnRepairNow;
    private XListView xlvComplete;
    // private View completedHeaderView;
    private View             uncompleteView;
    // private XListView xlvUnComplete;

    private RepairListAdapter repairListAdapter;
    private RelativeLayout rlRepairNow;
    private RadiusButtonView  btnBottomRepair;
    private TitleView title_layout;
    private RepairListInfo repairListInfo;
    private ArrayList<FinishBean>        finishBeans     = new ArrayList<>(); // 已完成集合
    private ArrayList<UnfinishedBean>    unfinishedBeans = new ArrayList<>();// 未完成集合
    private ArrayList<RepairHistoryInfo> repairList      = new ArrayList<>();// 合并两个集合

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_device_repair);
        initView();
    }

    @Override
    public void initPresenter() {

    }

    protected void initView() {
        mHandler = new Handler(this);
        initTitleLayout();
        noRepairRecord = findViewById(R.id.no_repair_records); // 无维修记录布局
        have_repair_records = findViewById(R.id.have_repair_records); // 有维修记录布局
        btnRepairNow = (RadiusButtonView)findViewById(R.id.btn_repair_now); // 立刻报修
        btnRepairNow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Constants.toActivity(RepairRecordNewActivity.this, NewRepairActivity.class, null);
            }
        });
        xlvComplete = (XListView)findViewById(R.id.xlv_complete); // 已完成的listview
        repairListAdapter = new RepairListAdapter(RepairRecordNewActivity.this, repairList);
        xlvComplete.setPullRefreshEnable(true);
        xlvComplete.setPullLoadEnable(false);
        xlvComplete.setXListViewListener(this);
        xlvComplete.setAdapter(repairListAdapter);
        xlvComplete.setOnItemClickListener(this);
        rlRepairNow = (RelativeLayout)findViewById(
                R.id.rl_repair_now);

        btnBottomRepair = (RadiusButtonView)findViewById(
                R.id.btn_bottom_repair);
        btnBottomRepair.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Constants.toActivity(RepairRecordNewActivity.this, NewRepairActivity.class, null);
            }

        });

        showData();
    }


    private void initTitleLayout() {

        title_layout = (TitleView)findViewById(R.id.title_layout);
        title_layout.setTitle(getResources().getString(
                R.string.main3_title_text));
        title_layout.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        title_layout.setLeftLayoutVisibility(View.GONE);
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
                xlvComplete.stopRefresh();
                xlvComplete.stopLoadMore();
                finishBeans.clear();
                unfinishedBeans.clear();
                repairList.clear();
                if (null != msg.obj) {
                    noRepairRecord.setVisibility(View.GONE);
                    xlvComplete.setVisibility(View.VISIBLE);
                    repairListInfo = (RepairListInfo) msg.obj;
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

                break;
            case Constants.HANDLER_GET_REPAIRHISTORY_FAILD:
                showData();
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // Constants.MyLog("获取焦点");
        new AllRepairHistoryAsync(RepairRecordNewActivity.this, mHandler).execute();
        //MobclickAgent.onPageStart(this.getClass().getName());
        //MobclickAgent.onPageStart(UMengKey.time_repair_history);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        int temp = position - 1;
        if (null != repairList && repairList.size() > 0            //跳转单个报修详情页面
                ) {

            if (temp < unfinishedBeans.size()) {

                if (unfinishedBeans.get(temp).getNstatus().equals("8")) {
                    switch (unfinishedBeans.get(temp).getNloanamount_TYPE()) {
                        case 0://正常状态(已付款,直接判断是否评价)
                            if (unfinishedBeans.get(temp).getIs_EVALUATE().equals("N")) {
                                //未评价
                                //nStatusString = "待评价";
                            } else if (unfinishedBeans.get(temp).getIs_EVALUATE().equals("Y")) {
                                //nStatusString = "已完工";
                            }
                            break;
                        case -1://未知状态
                            try {
                                if (Double.parseDouble(unfinishedBeans.get(temp).getNloanamount()) > 0.0) {
                                    //nStatusString = "待付款";
                                } else {
                                    if (unfinishedBeans.get(temp).getIs_EVALUATE().equals("N")) {
                                        //nStatusString = "待评价";
                                    } else if (unfinishedBeans.get(temp).getIs_EVALUATE().equals("Y")) {
                                        //nStatusString = "已完工";
                                    }
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                Constants.ToastAction("服务器传参数错误");
                            }
                            break;
                        case 1:
                            if (Double.parseDouble(unfinishedBeans.get(temp).getNloanamount()) > 0.0) {
                                //nStatusString = "待支付";
                                String orderNum = unfinishedBeans.get(temp).getOrderNum();
                                String flag = unfinishedBeans.get(temp).getFlag();
                                Bundle b = new Bundle();
                                b.putString("orderNum", orderNum);
                                b.putString("flag", flag);
                                Constants.toActivity(RepairRecordNewActivity.this,RepairDetailsActivity.class,b,false);
                            } else {
                                if (unfinishedBeans.get(temp).getIs_EVALUATE().equals("N")) {
                                    //	nStatusString = "待评价";
                                } else if (unfinishedBeans.get(temp).getIs_EVALUATE().equals("Y")) {
                                    //nStatusString = "已完工";
                                }
                            }
                            break;
                    }
                } else {
                    String orderNum = unfinishedBeans.get(temp).getOrderNum();
                    Intent intent = new Intent(RepairRecordNewActivity.this, RepairBasicInfomationActivity.class);
                    intent.putExtra("orderNum", orderNum);
                    intent.putExtra("flag", unfinishedBeans.get(temp).getFlag());
                    startActivity(intent);
                }

            } else {
                int p = temp - unfinishedBeans.size();
                if (finishBeans.get(p).getIs_EVALUATE().equals("N")) {
                    Intent intent = new Intent(RepairRecordNewActivity.this, EvaluationActivity.class);
                    intent.putExtra("orderNum", finishBeans.get(p).getOrderNum());
                    intent.putExtra("tel", finishBeans.get(p).getVmacoptel());
                    startActivity(intent);
                }
            }

        }
    }


    @Override
    public void onRefresh() {
        new AllRepairHistoryAsync(RepairRecordNewActivity.this, mHandler).execute();

    }

    @Override
    public void onLoadMore() {

    }

}
