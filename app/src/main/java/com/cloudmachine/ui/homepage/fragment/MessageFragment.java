package com.cloudmachine.ui.homepage.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudmachine.R;
import com.cloudmachine.activities.MessageContentActivity;
import com.cloudmachine.adapter.MessageAdapter;
import com.cloudmachine.api.Api;
import com.cloudmachine.api.HostType;
import com.cloudmachine.app.MyApplication;
import com.cloudmachine.base.baserx.RxBus;
import com.cloudmachine.base.baserx.RxConstants;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.net.task.AllMessagesListAsync;
import com.cloudmachine.net.task.QuestionMessageAsync;
import com.cloudmachine.struc.MessageBO;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.ui.homepage.activity.ViewMessageActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.UIHelper;
import com.cloudmachine.utils.widgets.XListView;
import com.cloudmachine.utils.widgets.XListView.IXListViewListener;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MessageFragment extends Fragment implements Callback, IXListViewListener {

    private XListView listView;
    private Context mContext;
    private Handler mHandler;
    private int pageNo = 1;
    private List<MessageBO> dataList = new ArrayList<MessageBO>();
    private MessageAdapter adapter;
    private View viewParent;
    private View empty_layout;
    private String mFragmentType;
    private Object systemMessage;

    public void setFragmentType(String fType) {
        mFragmentType = fType;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != viewParent) {
            ViewGroup parent = (ViewGroup) viewParent.getParent();
            if (null != parent) {
                parent.removeView(viewParent);
            }
        } else {
            viewParent = inflater.inflate(R.layout.activity_message, null);
            initRootView();// 控件初始化
        }
        return viewParent;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initRootView() {
        mContext = getActivity();
        mHandler = new Handler(this);
        initTitleLayout();
        listView = (XListView) viewParent.findViewById(R.id.noti_listview);
        listView.setPullRefreshEnable(true);
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(this);
        adapter = new MessageAdapter(mContext, dataList, mHandler);
        listView.setAdapter(adapter);

        empty_layout = viewParent.findViewById(R.id.empty_layout);

		/*acceptlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (dataList.size()>0) {
					for (int i = 0; i < dataList.size(); i++) {
						MessageBO item = dataList.get(i);
						new MessageUpdateStatusAsync(1,item.getId(),item.getInviterId(),mContext,mHandler).execute();
					}		
				}
			
			}
		});*/
        pageNo = 1;

        showData();
    }

    private void initTitleLayout() {

		/*title_layout = (TitleView)viewParent.findViewById(title_layout);
        title_layout.setTitle(getResources().getString(R.string.main_bar_text2));
		title_layout.setLeftLayoutVisibility(View.GONE);*/
    }

    private void showData() {
        if (null != dataList && dataList.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            empty_layout.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
        }
    }

//    private OnClickListener mErrorClickListener = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            new AllMessagesListAsync(pageNo, mContext, mHandler).execute();
//        }
//    };

    @Override
    public boolean handleMessage(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
            case Constants.HANDLER_GETALLMESSAGELIST_SUCCESS:
                listView.stopRefresh();
                listView.stopLoadMore();
                List<MessageBO> messageList = (List<MessageBO>) msg.obj;
                if (pageNo == 1) {
                    dataList.clear();
                }

                dataList.addAll(messageList);
                if (dataList.size() >= MyApplication.getInstance().getPageSize()) {
                    listView.setPullLoadEnable(true);
                }
                adapter.notifyDataSetChanged();
                showData();
                break;
            case Constants.HANDLER_GETALLMESSAGELIST_FAIL:
                if (pageNo == 1) {
                    dataList.clear();
                    adapter.notifyDataSetChanged();
                }
                listView.stopRefresh();
                listView.stopLoadMore();
                showData();
                break;
            case Constants.HANDLER_QUESTION_SUCCESS:
                MessageBO questionMessage = (MessageBO) msg.obj;
                if (null != dataList) {
                    questionMessage.setMessageType(5);
                    dataList.add(0, questionMessage);
                    adapter.notifyDataSetChanged();
                /*if(null != getActivity())
                    ((MainActivity)getActivity()).updateQuestion(true);*/
                } else {
                /*if(null != getActivity())
                    ((MainActivity)getActivity()).updateQuestion(false);*/
                }
                break;
            case Constants.HANDLER_QUESTION_FAIL:
            /*if(null != getActivity())
                ((MainActivity)getActivity()).updateQuestion(false);*/
                break;
            case Constants.HANDLER_MESSAGEACCEPTE_SUCCESS:
            /*((MainActivity)getActivity()).updateDevice();*/
                //添加设备成功，通知刷新设备页面
                RxBus.getInstance().post(RxConstants.REFRESH_DEVICE_FRAGMENT, "");
                Constants.isChangeDevice = true;
                Constants.isAddDevice = true;
                UIHelper.ToastMessage(mContext, "已经同意该邀请");
                dataList.get(msg.arg1).setStatus(3);
                adapter.notifyDataSetChanged();
                break;
            case Constants.HANDLER_MESSAGEREFUSE_SUCCESS:
                dataList.get(msg.arg1).setStatus(2);
                adapter.notifyDataSetChanged();
                UIHelper.ToastMessage(mContext, "已经拒绝该邀请");
                break;
            case Constants.HANDLER_MESSAGEUPSTATE_SUCCESS:
                dataList.get(msg.arg1).setStatus(4);
                adapter.notifyDataSetChanged();
                break;
            case Constants.HANDLER_MESSAGEACTION_FAIL:
                break;
            case Constants.HANDLER_GOTO_MESSAGECONTENT:
                if (null != dataList) {
                    MessageBO bo = dataList.get(msg.arg1);
                    if (null != bo) {
                        if (bo.getMessageType() == Constants.MESSAGETYPE[3]
                                || bo.getMessageType() == Constants.MESSAGETYPE[4]
                                || bo.getMessageType() == Constants.MESSAGETYPE[5]
                                ) {
                            Intent it = new Intent(getActivity(), MessageContentActivity.class);
                            it.putExtra("title", bo.getTitle());
                            it.putExtra("content", bo.getContent());
                            startActivity(it);
                        } else {
//                            Bundle bundle = new Bundle();
//                            bundle.putString(Constants.P_WebView_Url, bo.getUrl());
//                            bundle.putString(Constants.P_WebView_Title, bo.getTitle());
//                            bundle.putBoolean(Constants.P_WebView_With_Title,false);
//                            Constants.toActivity(getActivity(), WebviewActivity.class, bundle);
                            String url = bo.getUrl();
                            if (!TextUtils.isEmpty(url)) {
                                Bundle bundle = new Bundle();
                                bundle.putString("url", url);
                                Constants.toActivity(getActivity(), QuestionCommunityActivity.class, bundle);
                            }
                        }
                    }
                }


                break;
            default:
                break;

        }
        return false;
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        pageNo = 1;
        dataList.clear();
        loadData(true);
    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        pageNo++;
        loadData(false);
    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        loadData(true);
    }

    private void loadData(boolean isFirstPage) {
        if (ViewMessageActivity.FRAGMENT_TYPE_SYSTEM.equals(mFragmentType)) {
//            obtainSystemMessageTask();
            new AllMessagesListAsync(AllMessagesListAsync.TYPE_MESSAGE_SYSTEM, pageNo, mContext, mHandler).execute();
        } else {
            new AllMessagesListAsync(AllMessagesListAsync.TYPE_MESSAGE_ALL, pageNo, mContext, mHandler).execute();
            if (isFirstPage) {
                new QuestionMessageAsync(mContext, mHandler).execute();
            }
        }
    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }


    public void obtainSystemMessageTask() {
        if (MemeberKeeper.getOauth(getActivity()) != null) {
            Api.getDefault(HostType.CLOUDM_HOST).getSystemMessage(MemeberKeeper.getOauth(getActivity()).getId(), pageNo, 20).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<BaseRespose<List<MessageBO>>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Message msg = Message.obtain();
                    msg.what = Constants.HANDLER_GETALLMESSAGELIST_FAIL;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onNext(BaseRespose<List<MessageBO>> listBaseRespose) {
                    Message msg = Message.obtain();
                    msg.what = Constants.HANDLER_GETALLMESSAGELIST_SUCCESS;
                    msg.obj = listBaseRespose.getResult();
                    mHandler.sendMessage(msg);
                }
            });
        }

    }
}
