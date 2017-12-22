package com.cloudmachine.ui.home.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cloudmachine.R;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.PickItemBean;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.widget.CommonTitleView;
import com.cloudmachine.widget.ImageViewTouch;
import com.cloudmachine.widget.ImageViewTouchBase;
import com.cloudmachine.widget.PreviewViewPager;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class WorkPickItemActivity extends BaseAutoLayoutActivity implements View.OnClickListener {

    public static final String KEY_PIC_ITEM_POSITOIN = "pic_item_positoin";
    public static final String KEY_PIC_ITEM_lIST = "pic_item_list";

    PickItemBean picItem;
    CommonTitleView picItemCtv;
    List<PickItemBean> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_pick_item);
        int position = getIntent().getIntExtra(KEY_PIC_ITEM_POSITOIN, 0);
        items = getIntent().getParcelableArrayListExtra(KEY_PIC_ITEM_lIST);
        picItemCtv = (CommonTitleView) findViewById(R.id.pic_item_ctv);
        picItemCtv.setRightClickListener(this);
        setTitleName(position);
        initViewPager(position);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.TIME_MACHINE_WORK_IMAGE_LARGE);
    }

    private void initViewPager(int position) {
        PreviewViewPager itemsVp = (PreviewViewPager) findViewById(R.id.pic_items_vp);
        List<View> viewItems = new ArrayList<>();
        for (PickItemBean item : items) {
            ImageViewTouch img = new ImageViewTouch(this);
            img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            img.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
            Glide.with(this).load(item.getImgUrl()).placeholder(R.drawable.icon_defalut_img).error(R.drawable.icon_defalut_img).into(img);
            viewItems.add(img);
        }
        itemsVp.setAdapter(new PicItemPageAdapter(viewItems));
        itemsVp.setCurrentItem(position);
        itemsVp.addOnPageChangeListener(itemPageChangeLi);
    }

    private ViewPager.OnPageChangeListener itemPageChangeLi = new ViewPager.OnPageChangeListener() {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setTitleName(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void setTitleName(int position) {
        picItem = items.get(position);
        if (picItem != null) {
            picItemCtv.setTitleName(picItem.getPutTime());
        }
    }

    private class PicItemPageAdapter extends PagerAdapter {
        List<View> mItemViews;

        public PicItemPageAdapter(List<View> itemViews) {
            mItemViews = itemViews;
        }


        @Override
        public int getCount() {
            return mItemViews != null ? mItemViews.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mItemViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mItemViews.get(position);
            container.addView(itemView);
            return itemView;
        }
    }


    @Override
    public void initPresenter() {

    }

    @Override
    public void onClick(View v) {

        PermissionsChecker checker = new PermissionsChecker(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checker.lacksPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            PermissionsActivity.startActivityForResult(this, HomeActivity.PEM_REQCODE_WRITESD,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            dowloadFile();
        }


    }

    private void dowloadFile() {
        if (picItem == null) {
            return;
        }
        final String imgName = picItem.getHash() + ".jpg";
        final File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/cloudmachine/H_COFIG");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String[] fileNameList = dir.list();
        if (fileNameList.length > 0) {
            for (String name : fileNameList) {
                if (imgName.equals(name)) {
                    ToastUtils.showToast(this, "照片已保存！！");
                    return;
                }
            }
        }
        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).downloadFile(picItem.getImgUrl()).compose(RxSchedulers.<ResponseBody>io_main()).subscribe(new RxSubscriber<ResponseBody>(this) {
            @Override
            protected void _onNext(ResponseBody responseBody) {
                FileOutputStream fos = null;
                try {
                    File file = new File(dir, imgName);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    fos = new FileOutputStream(file);
                    fos.write(responseBody.bytes());
                    fos.flush();
                    fos.close();
                    ToastUtils.showToast(WorkPickItemActivity.this, "照片保存成功！！");
                    sendFileUpdateRevice(file);
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showToast(WorkPickItemActivity.this, "照片保存失败！！");
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(WorkPickItemActivity.this, message);
            }
        }));
    }

    private void sendFileUpdateRevice(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        sendBroadcast(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HomeActivity.PEM_REQCODE_WRITESD) {
            if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                CommonUtils.showPermissionDialog(this, Constants.PermissionType.STORAGE);
            } else {
                dowloadFile();
            }
        }

    }
}
