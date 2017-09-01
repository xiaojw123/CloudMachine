package com.cloudmachine.activities;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudmachine.R;

import java.util.ArrayList;
import java.util.List;

public class BigPicActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    public static final String BIG_PIC_URLS = "big_pic_url";
    public static final String POSITION="position";
    private static final String INDEX_FORMARTTER="%1$d/%2$d";
    ImageView bigPicBack;
    ViewPager bigPicVpager;
    TextView bigPicIndex;
    int len;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_pic);
        initView();


    }

    private void initView() {
        bigPicBack = (ImageView) findViewById(R.id.big_pic_backimg);
        bigPicVpager = (ViewPager) findViewById(R.id.big_pic_vpager);
        bigPicIndex = (TextView) findViewById(R.id.big_pic_index);
        bigPicBack.setOnClickListener(this);
        initViewPager();

    }

    private void initViewPager() {
        List<String> bigUrls = getIntent().getStringArrayListExtra(BIG_PIC_URLS);
        int currentIndex=getIntent().getIntExtra(POSITION,0);
        List<ImageView> imgList = new ArrayList<>();
        for (String url : bigUrls) {
            ImageView img = new ImageView(this);
            img.setOnClickListener(this);
            Glide.with(this).load(url).into(img);
            imgList.add(img);

        }
        len=imgList.size();
        bigPicVpager.setAdapter(new BigImgPageAdaper(imgList));
        bigPicVpager.addOnPageChangeListener(this);
        bigPicVpager.setCurrentItem(currentIndex);
        bigPicIndex.setText(String.format(INDEX_FORMARTTER,currentIndex+1, len));
    }


    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        bigPicIndex.setText(String.format(INDEX_FORMARTTER,position+1, len));

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private  class BigImgPageAdaper extends PagerAdapter {
        List<ImageView> mImgList;

        private BigImgPageAdaper(List<ImageView> imgList) {
            mImgList = imgList;
        }

        @Override
        public int getCount() {
            return mImgList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView img = mImgList.get(position);
            container.addView(img);
            return img;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }
    }


}
