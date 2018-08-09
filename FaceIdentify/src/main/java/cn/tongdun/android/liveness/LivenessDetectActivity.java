package cn.tongdun.android.liveness;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import android.os.Handler;

import com.oliveapp.face.livenessdetectorsdk.livenessdetector.datatype.LivenessDetectionFrames;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.tongdun.android.liveness.view_controller.LivenessDetectionMainActivity;


/**
 * 样例活体检测Activity
 */
public class LivenessDetectActivity extends LivenessDetectionMainActivity {
    public static final int RESULT_FACE_SUCCESS=0x006;
    public static final String URL_CONTRASTFACE = "url_contrastface";
    public static final String MEMBER_ID = "memberId";
    public static final String TAG = LivenessDetectActivity.class.getSimpleName();
    private String faceContrastUrl;
    private long memberId;
    String base64Data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 如果有设置全局包名的需要, 在这里进行设置
//        PackageNameManager.setPackageName();
        super.onCreate(savedInstanceState);
        faceContrastUrl = getIntent().getStringExtra(URL_CONTRASTFACE);
        memberId = getIntent().getLongExtra(MEMBER_ID, -1);
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
        Log.d("face","init failed____error:"+e);
        Toast.makeText(this, "无法初始化活体检测", Toast.LENGTH_LONG).show();
    }

    ////////////// LIVENESS DETECTION /////////////////

    @Override
    public void onLivenessSuccess(LivenessDetectionFrames livenessDetectionFrames) {
        super.onLivenessSuccess(livenessDetectionFrames);

        //LivenessDetectionFrames中有4个用于比对的数据包，具体使用哪个数据包进行对比请咨询对接人员
        //对数据包进行Base64编码的方法，用于发送HTTP请求，下面以带翻拍的数据包为样例

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                handleLivenessFinish(true);
//            }
//        }, 2000);
        base64Data = Base64.encodeToString(livenessDetectionFrames.verificationPackageWithFanpaiFull, Base64.NO_WRAP);
        new FaceAsyn().execute();


    }


  private   class FaceAsyn extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(faceContrastUrl);
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setConnectTimeout(3000);
                urlConn.setDoOutput(true);
                urlConn.setDoInput(true);
                urlConn.setUseCaches(false);
                urlConn.setRequestMethod("POST");
                urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                String parmas = "memberId=" + memberId + "&image=" + base64Data;
                Log.d("face",faceContrastUrl+", parmas:"+parmas);
                DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());
                out.writeBytes(parmas);
                out.flush();
                out.close();
                if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = urlConn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    if ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    is.close();
                    return sb.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            if (!TextUtils.isEmpty(s)) {
                JSONObject resJobj = null;
                try {
                    resJobj = new JSONObject(s);
                    boolean isSucess = resJobj.optBoolean("success");
                    String message = resJobj.optString("message");
                    if (isSucess) {
                        setResult(RESULT_FACE_SUCCESS);
                        String result = resJobj.optString("result");
                        Toast.makeText(LivenessDetectActivity.this, result, Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(LivenessDetectActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
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
            Toast.makeText(this, "恭喜，活体检测成功！！", Toast.LENGTH_LONG).show();
//            setResult(REST_FACE_IDENTIFY);
            finish();
        } else {
            Toast.makeText(this, "检测失败, 请返回重试", Toast.LENGTH_LONG).show();
        }

    }


}
