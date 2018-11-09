package com.cloudmachine.camera;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.CustomDialog;

import java.io.File;
import java.io.FileOutputStream;


public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    public static final int REQUEST_CODE = 0x11;
    public static final String PIC_PATH = "pic_path";
    public static final String PIC_TYPE = "pic_type";
    private CameraManager cameraManager;
    private boolean hasSurface;
    private static final String DEFAULT_PATH = "/sdcard/";
    public static final String TYPE_PIC_FRONT = "pic_front";
    public static final String TYPE_PIC_BG = "pic_bg";
    public static final String TYPE_PIC_HAND = "pic_hand";
    public static final String TYPE_DEFAUT = "pic_default";
    private String filePath;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initIntent();
        initLayoutParams();
    }

    private void initIntent() {
        type = getIntent().getStringExtra(PIC_TYPE);
        if (TextUtils.isEmpty(type)) {
            type = TYPE_DEFAUT;
        }
        if (filePath == null) {
            filePath = DEFAULT_PATH;
        }
    }

    /**
     * 重置surface宽高比例为3:4，不重置的话图形会拉伸变形
     */
    private void initLayoutParams() {
        ImageView take = (ImageView) findViewById(R.id.take_img);
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraManager.takePicture(null, null, myjpegCallback);
            }
        });
        ImageView cameraImg = (ImageView) findViewById(R.id.camera_back_img);
        cameraImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.Builder builder = new CustomDialog.Builder(CameraActivity.this);
                builder.setMessage("确认放弃拍摄证件照?");
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
                        finish();
                    }
                });
                builder.create().show();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        cameraManager = new CameraManager();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();

        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    /**
     * 初始camera
     *
     * @param surfaceHolder SurfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            // 打开Camera硬件设备
            cameraManager.openDriver(surfaceHolder);
            // 创建一个handler来打开预览，并抛出一个运行时异常
            cameraManager.startPreview();
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 停止camera，是否资源操作
     */
    @Override
    protected void onPause() {
        cameraManager.stopPreview();
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    /**
     * 拍照回调
     */
    Camera.PictureCallback myjpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            // 根据拍照所得的数据创建位图
            final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
                    data.length);
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            final Bitmap bitmap1 = Bitmap.createBitmap(bitmap, (width - height) / 2, height / 6, height, height * 2 / 3);
            // 创建一个位于SD卡上的文件
            File path = new File(filePath);
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(path, type + "_" + System.currentTimeMillis());

            FileOutputStream outStream = null;
            try {
                // 打开指定文件对应的输出流
                outStream = new FileOutputStream(file);
                // 把位图输出到指定文件中
                bitmap1.compress(Bitmap.CompressFormat.JPEG,
                        100, outStream);
                outStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(PIC_PATH, file.getAbsolutePath());
            bundle.putString(PIC_TYPE, type);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);

            CameraActivity.this.finish();

        }
    };

}
