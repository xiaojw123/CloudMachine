package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.cloudmachine.R;
import com.cloudmachine.adapter.BaseRecyclerAdapter;
import com.cloudmachine.adapter.MessageListAdapter;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.MessageBO;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.listener.OnItemClickListener;
import com.cloudmachine.ui.home.activity.IncomeSpendActivity;
import com.cloudmachine.ui.home.activity.MessageDetailActivity;
import com.cloudmachine.ui.home.contract.ViewMessageConract;
import com.cloudmachine.ui.home.model.ViewMessageModel;
import com.cloudmachine.ui.home.presenter.ViewMessagePresenter;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
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

public class ViewMessageActivity extends BaseAutoLayoutActivity<ViewMessagePresenter, ViewMessageModel> implements ViewMessageConract.View, OnItemClickListener, BaseRecyclerAdapter.OnItemClickListener, SwipeMenuRecyclerView.LoadMoreListener {
    @BindView(R.id.view_message_smrlv)
    SwipeMenuRecyclerView messageRlv;
    @BindView(R.id.view_message_empty)
    View emptyMsgView;
    MessageListAdapter mAdapter;
    int pageNo = 1;
    boolean isLast, isRefresh;
    List<MessageBO> mAllMessages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);
        ButterKnife.bind(this);
        initRecyclerView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.TIME_MESSAGE_LIST);
        pageNo = 1;
        mPresenter.getMessageList(pageNo);
    }

    private void initRecyclerView() {
        messageRlv.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration did = new DividerItemDecoration(this, LinearLayout.VERTICAL);
        did.setDrawable(getResources().getDrawable(R.drawable.divider_line_horztial));
        messageRlv.addItemDecoration(did);
        messageRlv.setSwipeMenuCreator(CommonUtils.getMenuCreator(this));
        messageRlv.setSwipeMenuItemClickListener(menuItemClickListener);
        messageRlv.useDefaultRefresh();
        messageRlv.useDefaultLoadMore();
        messageRlv.setLoadMoreListener(this);
        mAdapter = new MessageListAdapter(this);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setPresenter(mPresenter);
        messageRlv.setAdapter(mAdapter);
    }


    SwipeMenuItemClickListener menuItemClickListener = new SwipeMenuItemClickListener() {


        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            AppLog.print("onItemClick___");
            if (menuBridge.getDirection() == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                //删除item
                MessageBO msgBo = mAdapter.getItem(menuBridge.getAdapterPosition());
                if (msgBo != null) {
                    AppLog.print("onItemClick___deleMsg__");
                    menuBridge.closeMenu();
                    mPresenter.deleteMessage(msgBo);
                }

            }
        }
    };


    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }


    @Override
    public void updateMessageList(List<MessageBO> messageItems, boolean isFirstPage, boolean isLastPage) {
        resetScorllStatus();
        if (messageItems != null && messageItems.size() > 0) {
            isLast = isLastPage;
            if (isFirstPage) {
                mAllMessages.clear();
                messageRlv.setVisibility(View.VISIBLE);
                emptyMsgView.setVisibility(View.GONE);
            }
            mAllMessages.addAll(messageItems);
            mAdapter.updateItems(mAllMessages);
        } else {
            if (isFirstPage) {
                messageRlv.setVisibility(View.GONE);
                emptyMsgView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void resetScorllStatus() {
        if (isRefresh) {
            isRefresh = false;
            messageRlv.refreshComplete();
        }
        messageRlv.loadMoreFinish(false, true);
    }

    @Override
    public void updateMessageError() {
        resetScorllStatus();
    }

    @Override
    public void updateListAdapter() {
        mAdapter.notifyDataSetChanged();
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
                mPresenter.updateMsgStatus(msgBo);
            }
            int messageType = msgBo.getMessageType();
            String url = msgBo.getUrl();
            String title = null;
            if (messageType == 5) {
                url = msgBo.getContent();
                title = "问卷调查";
            }
//            messageType=8消息内容
            if (messageType == 8 || messageType == 3) {
                MobclickAgent.onEvent(this, MobEvent.TIME_MESSAGE_DETAIL);
                Bundle detailBundle = new Bundle();
                detailBundle.putString(MessageDetailActivity.MESSAGE_TITLE, msgBo.getTitle());
                detailBundle.putString(MessageDetailActivity.MESSAGE_CONTENT, msgBo.getContent());
                detailBundle.putString(MessageDetailActivity.MESSAGE_TIME, msgBo.getInviteDate());
                Constants.toActivity(this, MessageDetailActivity.class, detailBundle);
            } else if (messageType == 10 || messageType == 11) {
                Bundle data = new Bundle();
                int type = messageType == 11 ? IncomeSpendActivity.TYPE_INCOME : IncomeSpendActivity.TYPE_SPEND;
                if ((!UserHelper.isOwner(mContext, UserHelper.getMemberId(mContext)) && (type == IncomeSpendActivity.TYPE_INCOME))) {
                    data.putBoolean(IncomeSpendActivity.IS_MEMBER, true);
                }
                data.putInt(IncomeSpendActivity.TYPE, type);
                Constants.toActivity(this, IncomeSpendActivity.class, data);
            } else {
                if (!TextUtils.isEmpty(url)) {
                    MobclickAgent.onEvent(this, MobEvent.TIME_MESSAGE_DETAIL);
                    Bundle bundle = new Bundle();
                    bundle.putString(QuestionCommunityActivity.H5_TITLE, title);
                    bundle.putString(QuestionCommunityActivity.H5_URL, url);
                    Constants.toActivity(ViewMessageActivity.this, QuestionCommunityActivity.class, bundle);
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        pageNo = 1;
        mPresenter.getMessageList(pageNo);
    }

    @Override
    public void onLoadMore() {
        if (isLast) {
            messageRlv.loadMoreFinish(false, false);
        } else {
            pageNo++;
            mPresenter.getMessageList(pageNo);
        }
    }

}
