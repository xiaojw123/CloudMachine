package com.cloudmachine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudmachine.R;
import com.cloudmachine.adapter.holder.BaseHolder;
import com.cloudmachine.bean.MessageBO;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.ui.home.presenter.ViewMessagePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xiaojw on 2017/7/17.
 */

public class MessageListAdapter extends BaseRecyclerAdapter<MessageBO> {
    ViewMessagePresenter mPresenter;
    OnItemClickListener mOnItemClickListener;

    public MessageListAdapter(Context context) {
        super(context);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    public void updateItems(List<MessageBO> items) {
        if (mItems != null && mItems.size() > 0) {
            mItems.addAll(items);
        } else {
            mItems = items;
        }
        notifyDataSetChanged();
    }

    public void updateItem(MessageBO item) {
        if (mItems == null) {
            mItems = new ArrayList<>();
        }
        mItems.add(0, item);
        notifyDataSetChanged();
    }

    public void removeItem(MessageBO item) {
        mItems.remove(item);
        notifyDataSetChanged();
    }


    public void setPresenter(ViewMessagePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new MessageListHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_message, parent, false));
    }



    class MessageListHolder extends BaseHolder<MessageBO> implements View.OnClickListener {
        @BindView(R.id.item_message_img)
        CircleImageView itemMessageImg;
        @BindView(R.id.item_message_title)
        TextView itemTitleTv;
        @BindView(R.id.item_message_content)
        TextView itemContentTv;
        @BindView(R.id.item_message_date)
        TextView itemDateTv;
        @BindView(R.id.item_message_choose)
        LinearLayout itemChooseCotainer;
        @BindView(R.id.item_message_accept)
        TextView acceptTv;
        @BindView(R.id.item_message_refuse)
        TextView refuseTv;
        @BindView(R.id.item_message_unread_alert)
        View unReadAlertView;
        @BindView(R.id.item_message_phone)
        TextView phoneTv;


        private MessageListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        @Override
        public void initViewHolder(MessageBO item) {
            int status = item.getStatus();
            if (status == 1) {
                unReadAlertView.setVisibility(View.VISIBLE);
            } else {
                unReadAlertView.setVisibility(View.INVISIBLE);
            }
            itemDateTv.setText(item.getInviteTime());
            phoneTv.setVisibility(View.GONE);
            int messageType = item.getMessageType();
            switch (messageType) {
                case 3:
                case 4:
                case 5:
                case 8:
                case 9:
                case 10:
                case 11:
                    if (messageType==10||messageType==11){
                        Glide.with(mContext).load(item.getImgpath())
                                .error(R.drawable.ic_message_money)
                                .into(itemMessageImg);
                    }else{
                        Glide.with(mContext).load(item.getImgpath())
                                .error(R.drawable.ic_message_system)
                                .into(itemMessageImg);
                    }
                    //系统消息
//                    itemMessageImg.setImageResource(R.drawable.ic_message_system);
                    itemTitleTv.setText(item.getTitle());
                    itemContentTv.setText(item.getContent());
                    itemChooseCotainer.setVisibility(View.GONE);
                    break;
                case 6:
                    itemTitleTv.setText("问答社区");

                    String content = item.getInviterNickname()
                            + "回复了<font color=\"#333333\">\""
                            + item.getContent()
                            + "\"</font>点击查看";
                    itemContentTv.setText(Html.fromHtml(content));
                    itemMessageImg.setImageResource(R.drawable.ic_ask_question);
                    itemChooseCotainer.setVisibility(View.GONE);
                    break;
                case 7:
                    Glide.with(mContext).load(item.getImgpath())
                            .error(R.drawable.ic_default_head)
                            .into(itemMessageImg);
                    itemTitleTv.setText(item.getInviterNickname() + "");
                    phoneTv.setVisibility(View.VISIBLE);
                    phoneTv.setText(item.getInviterMobile());
                    String dMessage = "";
                    switch (status) {
                        case 1:
                        case 4:
                            dMessage = "申请成为<font color=\"#333333\">\"" + item.getDeviceName() + "\"</font>的操作者, 请接受";
                            itemChooseCotainer.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            dMessage = "申请成为<font color=\"#333333\">\"" + item.getDeviceName() + "\"</font>的成员，已拒绝";
                            itemChooseCotainer.setVisibility(View.GONE);
                            break;
                        case 3:
                            dMessage = "申请成为<font color=\"#333333\">\"" + item.getDeviceName() + "\"</font>的成员，已接受";
                            itemChooseCotainer.setVisibility(View.GONE);
                            break;
                    }
                    itemContentTv.setText(Html.fromHtml(dMessage));
                    break;

                default:
                    itemTitleTv.setText(item.getInviterNickname());
                    Glide.with(mContext).load(item.getImgpath())
                            .error(R.drawable.ic_default_head)
                            .into(itemMessageImg);
                    String msgContent = null;
                    String deviceName = item.getDeviceName();
                    switch (status) {
                        case 1:
                        case 4:
                            if (messageType == 1) {
                                msgContent = "邀请你共享<font color=\"#333333\">\"" + deviceName + "\"</font>该设备的数据";
                            } else {
                                msgContent = "技师移交<font color=\"#333333\">\"" + deviceName + "\"</font>给您，请接受";
                            }
                            itemChooseCotainer.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            if (messageType == 1) {
                                msgContent = "邀请你共享<font color=\"#333333\">\"" + deviceName + "\"</font>该设备的数据，已拒绝";
                            } else {
                                msgContent = "技师移交<font color=\"#333333\">\"" + deviceName + "\"</font>给您，已拒绝";
                            }
                            itemChooseCotainer.setVisibility(View.GONE);
                            break;
                        case 3:
                            if (messageType == 1) {
                                msgContent = "邀请你共享<font color=\"#333333\">\"" + deviceName + "\"</font>该设备的数据，已接受\"";
                            } else {
                                msgContent = "技师移交<font color=\"#333333\">\"" + deviceName + "\"</font>给您，已接受";
                            }
                            itemChooseCotainer.setVisibility(View.GONE);
                            break;
                    }
                    itemContentTv.setText(Html.fromHtml(msgContent));
                    break;
            }
            if (itemChooseCotainer.getVisibility() == View.VISIBLE) {
                acceptTv.setTag(item);
                refuseTv.setTag(item);
            } else {
                itemView.setTag(item);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(v, getAdapterPosition());
                    }
                });
            }
            acceptTv.setOnClickListener(this);
            refuseTv.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Object obj = v.getTag();
            MessageBO msgBO;
            if (obj != null && obj instanceof MessageBO) {
                msgBO = (MessageBO) obj;
            } else {
                return;
            }
            long memberId = UserHelper.getMemberId(mContext);
            switch (v.getId()) {
                case R.id.item_message_accept://接受移交
                    mPresenter.acceptMessage(memberId, msgBO);
                    break;
                case R.id.item_message_refuse://拒绝移交
                    mPresenter.rejectMessage(memberId, msgBO);
                    break;

            }


        }
    }

}
