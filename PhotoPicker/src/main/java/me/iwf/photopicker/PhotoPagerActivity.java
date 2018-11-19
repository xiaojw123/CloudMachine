package me.iwf.photopicker;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.fragment.ImagePagerFragment;
import me.iwf.photopicker.widget.CustomDialog;

import static me.iwf.photopicker.PhotoPicker.KEY_DEL_PHOTOS;
import static me.iwf.photopicker.PhotoPicker.KEY_SELECTED_PHOTOS;
import static me.iwf.photopicker.PhotoPreview.EXTRA_CURRENT_ITEM;
import static me.iwf.photopicker.PhotoPreview.EXTRA_PHOTOS;
import static me.iwf.photopicker.PhotoPreview.EXTRA_SHOW_DELETE;

/**
 * Created by donglua on 15/6/24.
 */
public class PhotoPagerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImagePagerFragment pagerFragment;

    private boolean showDelete;
    private ImageView backImg;
    private TextView titleTv;
    private ImageView deleteImg;
    private ArrayList<String> delImgList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.__picker_activity_photo_pager);
        backImg = (ImageView) findViewById(R.id.back_img);
        titleTv = (TextView) findViewById(R.id.title_tv);
        deleteImg = (ImageView) findViewById(R.id.delte_img);
        int currentItem = getIntent().getIntExtra(EXTRA_CURRENT_ITEM, 0);
        List<String> paths = getIntent().getStringArrayListExtra(EXTRA_PHOTOS);
        showDelete = getIntent().getBooleanExtra(EXTRA_SHOW_DELETE, true);
        if (pagerFragment == null) {
            pagerFragment =
                    (ImagePagerFragment) getSupportFragmentManager().findFragmentById(R.id.photoPagerFragment);
        }
        pagerFragment.setPhotos(paths, currentItem);
        if (showDelete) {
            deleteImg.setVisibility(View.VISIBLE);
        } else {
            deleteImg.setVisibility(View.GONE);
        }
        backImg.setOnClickListener(this);
        deleteImg.setOnClickListener(this);
        updateActionBarTitle();
        pagerFragment.getViewPager().addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                updateActionBarTitle();
            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        if (showDelete) {
//            getMenuInflater().inflate(R.menu.__picker_menu_preview, menu);
//        }
//        return true;
//    }


    @Override
    public void onBackPressed() {
        if (delImgList.size() > 0) {
            Intent intent = new Intent();
            intent.putExtra(KEY_SELECTED_PHOTOS, pagerFragment.getPaths());
            intent.putExtra(KEY_DEL_PHOTOS, delImgList);
            setResult(RESULT_OK, intent);
        }
        finish();
        super.onBackPressed();
    }


    public void updateActionBarTitle() {
        titleTv.setText(getString(R.string.__picker_image_index, pagerFragment.getViewPager().getCurrentItem() + 1,
                pagerFragment.getPaths().size()));
    }


    @Override
    public void onClick(View v) {
        if (v == backImg) {
            onBackPressed();
        } else if (v == deleteImg) {
            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            builder.setMessage("确定要删除这张照片吗？");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    final int index = pagerFragment.getCurrentItem();
                    final String deletedPath = pagerFragment.getPaths().get(index);
                    delImgList.add(deletedPath);
                    pagerFragment.getPaths().remove(index);
                    pagerFragment.getViewPager().getAdapter().notifyDataSetChanged();
                    if (pagerFragment.getPaths().size() <= 0) {
                        onBackPressed();
                    }
                }
            });
            builder.create().show();
        }

    }

}
