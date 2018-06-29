package cn.tongdun.android.liveness;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.os.Handler;

import com.oliveapp.face.livenessdetectorsdk.livenessdetector.datatype.LivenessDetectionFrames;

import cn.tongdun.android.liveness.view_controller.LivenessDetectionMainActivity;


/**
 * 样例活体检测Activity
 */
public class LivenessDetectActivity extends LivenessDetectionMainActivity {
    public static final int REST_FACE_IDENTIFY = 0x10;
    public static final String TAG = LivenessDetectActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 如果有设置全局包名的需要, 在这里进行设置
//        PackageNameManager.setPackageName();
        super.onCreate(savedInstanceState);
    }

    ////////////// INITIALIZATION //////////////
    @Override
    public void onInitializeSucc() {
        super.onInitializeSucc();
        super.startVerification();
    }

    @Override
    public void onInitializeFail(Throwable e) {
        super.onInitializeFail(e);
        Log.e(TAG, "无法初始化活体检测...", e);
        Toast.makeText(this, "无法初始化活体检测", Toast.LENGTH_LONG).show();
        handleLivenessFinish(false);
    }

    ////////////// LIVENESS DETECTION /////////////////

    @Override
    public void onLivenessSuccess(LivenessDetectionFrames livenessDetectionFrames) {
        super.onLivenessSuccess(livenessDetectionFrames);

        //LivenessDetectionFrames中有4个用于比对的数据包，具体使用哪个数据包进行对比请咨询对接人员
        //对数据包进行Base64编码的方法，用于发送HTTP请求，下面以带翻拍的数据包为样例
        //String base64Data = Base64.encodeToString(livenessDetectionFrames.verificationPackageWithFanpaiFull,Base64.NO_WRAP);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handleLivenessFinish(true);
            }
        }, 2000);
    }

    @Override
    public void onLivenessFail(int result, LivenessDetectionFrames livenessDetectionFrames) {
        super.onLivenessFail(result, livenessDetectionFrames);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handleLivenessFinish(false);
            }
        }, 2000);
    }

    private void handleLivenessFinish(boolean isSuccess) {
        if (isSuccess) {
            Toast.makeText(this,"恭喜，活体检测成功！！",Toast.LENGTH_LONG).show();
            setResult(REST_FACE_IDENTIFY);
            finish();
        }else{
            Toast.makeText(this,"检测失败, 请返回重试",Toast.LENGTH_LONG).show();
        }

    }
}
