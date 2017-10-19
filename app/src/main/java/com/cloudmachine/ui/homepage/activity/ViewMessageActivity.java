package com.cloudmachine.ui.homepage.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.cloudmachine.R;
import com.cloudmachine.adapter.MessageListAdapter;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.listener.OnItemClickListener;
import com.cloudmachine.bean.MessageBO;
import com.cloudmachine.ui.home.contract.ViewMessageConract;
import com.cloudmachine.ui.home.model.ViewMessageModel;
import com.cloudmachine.ui.home.presenter.ViewMessagePresenter;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.chart.utils.AppLog;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/4/12 下午4:43
 * 修改人：shixionglu
 * 修改时间：2017/4/12 下午4:43
 * 修改备注：
 */

public class ViewMessageActivity extends BaseAutoLayoutActivity<ViewMessagePresenter, ViewMessageModel> implements ViewMessageConract.View, OnItemClickListener {
    public static final String FRAGMENT_TYPE_SYSTEM = "system_message";
    @BindView(R.id.view_message_smrlv)
    SwipeMenuRecyclerView messageRlv;
    @BindView(R.id.view_message_empty)
    View emptyMsgView;
    MessageListAdapter mAdapter;
    long memberId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);
        ButterKnife.bind(this);
        initRecyclerView();
        mContext = this;
        memberId = UserHelper.getMemberId(this);
        mPresenter.questionNeed(memberId);
        MobclickAgent.onEvent(this, MobEvent.TIME_MESSAGE_LIST);

    }

    private void initRecyclerView() {
        messageRlv.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration did=new DividerItemDecoration(this, LinearLayout.VERTICAL);
        did.setDrawable(getResources().getDrawable(R.drawable.divider_line_horztial));
        messageRlv.addItemDecoration(did);
        messageRlv.setSwipeMenuCreator(CommonUtils.getMenuCreator(this));
        messageRlv.setSwipeMenuItemClickListener(menuItemClickListener);
        mAdapter = new MessageListAdapter(this);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setPresenter(mPresenter);
        messageRlv.setAdapter(mAdapter);
    }



    OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {


        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            AppLog.print("onItemClick___");
            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                //删除item
                MessageBO msgBo = mAdapter.getItem(adapterPosition);
                if (msgBo != null) {
                    AppLog.print("onItemClick___deleMsg__");
                    closeable.smoothCloseLeftMenu();
                    mPresenter.deleteMessage(memberId, msgBo);
                }

            }
        }
    };


    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }


    @Override
    public void returnAcceptMessage() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void returnRejectMessage() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateMsgStatus() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void returnGetSystemMsg(List<MessageBO> msgList) {
        mAdapter.updateItems(msgList);
    }

    @Override
    public void retrunGetAllMsg(List<MessageBO> msgList) {
        AppLog.print("retrunGetAllMsg___");
        mAdapter.updateItems(msgList);
        List<MessageBO> messageBOs=mAdapter.getItems();
        if (messageBOs!=null&&messageBOs.size() >0) {
            messageRlv.setVisibility(View.VISIBLE);
            emptyMsgView.setVisibility(View.GONE);
        }else{
            messageRlv.setVisibility(View.GONE);
            emptyMsgView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void retrunQuestionNeed(MessageBO messageBO) {
        AppLog.print("retrunQuestionNeed___");
        mAdapter.updateItem(messageBO);
    }

    @Override
    public void returnDeleteMessage(MessageBO messageBO) {
        mAdapter.removeItem(messageBO);
    }


    @Override
    public void onItemClick(View view, int position) {
        Object obj = view.getTag();
        if (obj != null && obj instanceof MessageBO) {
//                dasdfasdf
            MessageBO msgBo = (MessageBO) obj;
            if (msgBo.getStatus() == 1) {
                mPresenter.updateMsgStatus(memberId, msgBo);
            }
            int messageType = msgBo.getMessageType();
            String url = msgBo.getUrl();
            String title = null;
            if (messageType == 5) {
                url = msgBo.getMessage();
                title = "问卷调查";
            }
            if (!TextUtils.isEmpty(url)) {
                MobclickAgent.onEvent(this,MobEvent.TIME_MESSAGE_DETAIL);
                Bundle bundle = new Bundle();
                bundle.putString(QuestionCommunityActivity.H5_TITLE, title);
                bundle.putString(QuestionCommunityActivity.H5_URL, url);
                Constants.toActivity(ViewMessageActivity.this, QuestionCommunityActivity.class, bundle);
            }
        }
    }
}
