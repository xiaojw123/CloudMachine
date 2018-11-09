package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.iwf.photopicker.widget.TouchImageView;

public class PicPreviewActivity extends BaseAutoLayoutActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    public static final String PREVIEW_IDCARD = "p_idcard";
    public static final String PREVIEW_INCOME = "p_income";
    public static final String PRERIVEW_ENGER = "p_enger";
    public static final String PRERIVEW_MACHINE="p_machine";
    @BindView(R.id.preview_back_img)
    ImageView backImg;
    @BindView(R.id.preview_title_tv)
    TextView titleTv;
    @BindView(R.id.preview_vp)
    ViewPager viewPager;
    String pageType;
    int len;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_preview);
        ButterKnife.bind(this);
        backImg.setOnClickListener(this);
        pageType = getIntent().getStringExtra(Constants.PAGET_TYPE);
        List<TouchImageView> itemViewList = new ArrayList<>();
        switch (pageType) {
            case PREVIEW_IDCARD:
                titleTv.setText("预览");
                itemViewList.add(getItemImg(R.drawable.ic_hand_big));
                break;
            case PREVIEW_INCOME:
                titleTv.setText("查看示例");
                itemViewList.add(getItemImg(R.drawable.ic_income_sample));
                break;
            case PRERIVEW_ENGER:
                titleTv.setText("查看示例1/4");
                itemViewList.add(getItemImg(R.drawable.ic_enger_sample1));
                itemViewList.add(getItemImg(R.drawable.ic_enger_sample2));
                itemViewList.add(getItemImg(R.drawable.ic_enger_sample3));
                itemViewList.add(getItemImg(R.drawable.ic_enger_sample4));
                viewPager.addOnPageChangeListener(this);
                break;
            case PRERIVEW_MACHINE:
                titleTv.setText("查看示例1/3");
                itemViewList.add(getItemImg(R.drawable.ic_machine_sample1));
                itemViewList.add(getItemImg(R.drawable.ic_machine_sample2));
                itemViewList.add(getItemImg(R.drawable.ic_machine_sample3));
                viewPager.addOnPageChangeListener(this);
                break;
        }
        len =itemViewList.size();
        viewPager.setAdapter(new PreviewAdapter(itemViewList));
        viewPager.setCurrentItem(0);
    }

    public TouchImageView getItemImg(int resId) {
        TouchImageView img = new TouchImageView(this);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
        img.setImageResource(resId);
        return img;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        titleTv.setText("查看示例" + (position + 1) + "/"+len);
    }

    @Override
    public void onPageScrollStateChanged(int state) {


    }


    private class PreviewAdapter extends PagerAdapter {
        List<TouchImageView> mItemList;

        private PreviewAdapter(List<TouchImageView> itemList) {
            mItemList = itemList;
        }

        @Override
        public int getCount() {
            return mItemList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TouchImageView img = mItemList.get(position);
            container.addView(img);
            return img;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            TouchImageView img = mItemList.get(position);
            container.removeView(img);
        }
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
