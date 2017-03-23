package com.cloudmachine.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RatingBar;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.net.task.GetTagInfoAsync;
import com.cloudmachine.net.task.SubmitCommentAsync;
import com.cloudmachine.struc.CommentsInfo;
import com.cloudmachine.struc.DisadvantageBean;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.widgets.tagview.TagBaseAdapter;
import com.cloudmachine.utils.widgets.tagview.TagCloudLayout;
import com.cloudmachine.utils.widgets.tagview.TagCloudLayout.TagItemClickListener;

import java.util.ArrayList;

/**
 * 
 * 评价页面
 * 
 * @author shixionglu
 * 
 */

public class EvaluationActivity extends BaseAutoLayoutActivity implements
		Callback {

	private Context mContext;
	private Handler mHandler;
	private ArrayList<DisadvantageBean> advantageBeans = new ArrayList<>();// 优点集合
	private ArrayList<DisadvantageBean> disadvantageBeans = new ArrayList<>();// 建议集合
	private TagBaseAdapter advantageAdapter;// 优点适配器
	private TagBaseAdapter disAdvantageAdapter;// 建议适配器
	private TagCloudLayout advantage;
	private TagCloudLayout disadvantage;
	private RadiusButtonView submit;
	private String css_work_no;
	private String cust_telString;
	private StringBuffer sbAdvantage = new StringBuffer();
	private StringBuffer sbAdvice = new StringBuffer();
	private TitleView titleView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);
		mContext = this;
		mHandler = new Handler(this);
		getIntentData();
		
		titleView = (TitleView) findViewById(R.id.title_layout);
		titleView.setTitle("评价");
		titleView.setLeftOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		new GetTagInfoAsync(mHandler, mContext).execute();

		advantage = (TagCloudLayout) findViewById(R.id.advantage);
		disadvantage = (TagCloudLayout) findViewById(R.id.disadvantage);
		RatingBar ratingBar = (RatingBar) findViewById(R.id.rb_rating_bar);// 星星
		final int rating = (int)ratingBar.getRating();// 满意度评分

		submit = (RadiusButtonView) findViewById(R.id.login_btn);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < advantageBeans.size(); i++) {
					if (advantageBeans.get(i).isChecked()
							&& i != advantageBeans.size() - 1) {
						sbAdvantage.append(advantageBeans.get(i).getCode())
								.append("_");
					}
					if (advantageBeans.get(i).isChecked()
							&& i == advantageBeans.size() - 1) {
						sbAdvantage.append(advantageBeans.get(i).getCode());
					}
				}

				for (int j = 0; j < disadvantageBeans.size(); j++) {
					if (disadvantageBeans.get(j).isChecked()
							&& j != disadvantageBeans.size() - 1) {
						sbAdvice.append(disadvantageBeans.get(j).getCode())
								.append("_");
					}
					if (disadvantageBeans.get(j).isChecked()
							&& j == disadvantageBeans.size() - 1) {
						sbAdvice.append(disadvantageBeans.get(j));
					}
				}

				if (TextUtils.isEmpty(css_work_no)) {
					Constants.ToastAction("工单id不能为空");
					return;
				}
				if (TextUtils.isEmpty(cust_telString)) {
					Constants.ToastAction("评价人电话不能为空");
					return;
				}
				if (TextUtils.isEmpty(sbAdvantage.toString())) {
					Constants.ToastAction("评价标签不能为空");
					return;
				}
				new SubmitCommentAsync(mContext, mHandler, css_work_no, String
						.valueOf(rating), cust_telString, sbAdvantage
						.toString(), sbAdvice.toString()).execute();
			}
		});

		advantageAdapter = new TagBaseAdapter(mContext, advantageBeans);
		disAdvantageAdapter = new TagBaseAdapter(mContext, disadvantageBeans);

		advantage.setAdapter(advantageAdapter);
		disadvantage.setAdapter(disAdvantageAdapter);
	}

	@Override
	public void initPresenter() {

	}

	@Override
	protected void onResume() {
		//MobclickAgent.onPageStart(UMengKey.time_repair_comment);
		super.onResume();
	}

	@Override
	protected void onPause() {
		//MobclickAgent.onPageEnd(UMengKey.time_repair_comment);
		super.onPause();
	}

	private void getIntentData() {
		Intent intent = this.getIntent();
		css_work_no = intent.getStringExtra("orderNum");// 工单id
		cust_telString = intent.getStringExtra("tel");// 评价人电话
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case Constants.HANDLER_GETTAGINFO_SUCCESS:
			Constants.MyLog("获取数据成功");
			if (null != msg.obj) {
				advantageBeans.clear();
				disadvantageBeans.clear();

				CommentsInfo commentsInfo = (CommentsInfo) msg.obj;
				ArrayList<DisadvantageBean> advaList = commentsInfo
						.getAdvantage();
				ArrayList<DisadvantageBean> disAdvaList = commentsInfo
						.getDisadvantage();

				advantageBeans.addAll(advaList);
				disadvantageBeans.addAll(disAdvaList);
				advantage.setItemClickListener(new TagItemClickListener() {

					@Override
					public void itemClick(int position) {
						advantageBeans.get(position).setChecked(
								!advantageBeans.get(position).isChecked());

						advantageAdapter.notifyDataSetChanged();
					}
				});
				disadvantage.setItemClickListener(new TagItemClickListener() {

					@Override
					public void itemClick(int position) {
						disadvantageBeans.get(position).setChecked(
								!disadvantageBeans.get(position).isChecked());
						disAdvantageAdapter.notifyDataSetChanged();
					}
				});

				advantageAdapter.notifyDataSetChanged();
				disAdvantageAdapter.notifyDataSetChanged();
			}

			break;

		case Constants.HANDLER_SUBMITCOMMENT_SUCCESS:
			Constants.ToastAction(String.valueOf(msg.obj));
			finish();
		case Constants.HANDLER_SUBMITCOMMENT_FAILD:
			Constants.ToastAction(String.valueOf(msg.obj));
		default:
			break;
		}
		return false;
	}

}
