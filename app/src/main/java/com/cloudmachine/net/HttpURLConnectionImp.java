package com.cloudmachine.net;

import android.os.Build;
import android.text.TextUtils;

import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.LarkUrls;
import com.cloudmachine.utils.VersionU;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


/**
 * 2013-10-17
 *
 * @author coca
 */
public class HttpURLConnectionImp implements IHttp {

    private static final int TIMEOUT = 60000;



    /**
     * @param params post params <key.params>
     * @return String
     * @throws IOException
     * @throw NullPointerException
     */

    @Override
    public String post(String urlString, Map<String, String> params)
            throws Exception {
        if (urlString == null || urlString.length() <= 0) {
            throw new NullPointerException("url is null or .........");
        }
        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url
                .openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setConnectTimeout(TIMEOUT);
        httpURLConnection.setReadTimeout(TIMEOUT);
        httpURLConnection.setInstanceFollowRedirects(true);
        httpURLConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        addHeaders(httpURLConnection, urlString);
        DataOutputStream out = new DataOutputStream(
                httpURLConnection.getOutputStream());
        String reqParams = getReqestParams(params);
        if (reqParams != null && reqParams.length() > 0) {
            out.writeBytes(reqParams);
        }
        out.flush();
        out.close();
        String resultString = "";
        InputStream in = httpURLConnection.getInputStream();
        if (null != in) {
            resultString = inputStreamToString(in);
            in.close();
        }
        httpURLConnection.disconnect();
        AppLog.printURl(resultString);
        return resultString;
    }


    @Override
    public String get(String urlString, Map<String, String> params)
            throws Exception {
        String reqParams = getReqestParams(params);
        if (reqParams != null && reqParams.length() > 0) {
            urlString += "?" + reqParams;
        }
        AppLog.printURl(urlString);
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setConnectTimeout(TIMEOUT);
        conn.setReadTimeout(TIMEOUT);
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("Content-Type", "application/json");
        addHeaders(conn, urlString);
        //		int resCode=conn.getResponseCode();
        conn.connect();
        InputStream in = conn.getInputStream();
        String resultString = "";
        if (null != in) {
            resultString = inputStreamToString(in);
            in.close();
        }
        AppLog.printURl(resultString);
        return resultString;
    }

    private void addHeaders(HttpURLConnection httpURLConnection, String url) {
        AppLog.printURl(url);
        httpURLConnection.setRequestProperty("osPlatform", "Android");
        httpURLConnection.setRequestProperty("osSystem", Build.VERSION.RELEASE);
        httpURLConnection.setRequestProperty("osVersion", VersionU.getVersionName());
        if (LarkUrls.LOGIN_URL.equals(url) || LarkUrls.GET_PAY_SIGN.equals(url)) {
            String timeStamp = CommonUtils.getTimeStamp();
            String signValue = null;
            if (LarkUrls.LOGIN_URL.equals(url)) {
                signValue = CommonUtils.getD5Str("passworduserName" + timeStamp);
            } else if (LarkUrls.GET_PAY_SIGN.equals(url)) {
                signValue = CommonUtils.getD5Str("orderNopayType" + timeStamp);
                if (!TextUtils.isEmpty(UserHelper.TOKEN)) {
                    httpURLConnection.setRequestProperty("token", UserHelper.TOKEN);
                }
            }
            httpURLConnection.setRequestProperty("timeStamp", timeStamp);
            httpURLConnection.setRequestProperty("sign", signValue);
        } else {
            if (!TextUtils.isEmpty(UserHelper.TOKEN)) {
                httpURLConnection.setRequestProperty("token", UserHelper.TOKEN);
            }
        }
    }

    private String getReqestParams(Map<String, String> params) throws UnsupportedEncodingException {
        if (params != null && params.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                String key = entry.getKey();
                String value = entry.getValue();
                if (value == null) {
                    value = "";
                }
                sb.append(key).append("=").append(URLEncoder.encode(value, "UTF-8"));
            }
            AppLog.printURl("Params:" + sb.toString());
            return sb.toString();
        }
        return null;
    }


    //    @Override
//    public String get(String urlString, List<NameValuePair> params)
//            throws Exception {
//        StringBuffer sb = new StringBuffer();
//        int size = params.size();
//        for (int a = 0; a < size; a++) {
//            if (params.get(a) != null) {
//                String key = params.get(a).getName();
//                if (a > 0) {
//                    sb.append("&");
//                }
//
//                if (params.get(a).getValue() == null) {
//                    sb.append(key + "="
//                            + URLEncoder.encode("", "UTF-8"));
//                } else {
//                    sb.append(key + "="
//                            + URLEncoder.encode(params.get(a).getValue(), "UTF-8"));
//                }
//
//                //                        }
//            }
//
//        }
//        String resultString = "";
//        urlString += "?" + sb.toString();
//        AppLog.printURl(urlString);
//        URL url = new URL(urlString);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setDoInput(true);
//        conn.setConnectTimeout(TIMEOUT);
//        conn.setReadTimeout(TIMEOUT);
//        conn.setRequestProperty("accept", "*/*");
//        conn.setRequestProperty("Content-Type", "application/json");
//        addHeaders(conn,urlString);
//        conn.connect();
//        InputStream in = conn.getInputStream();
//        if (null != in) {
//            resultString = IOUtil.inputStreamToString(in);
//            in.close();
//        }
//        AppLog.printURl(resultString);
//        return resultString;
//    }
    public static String inputStreamToString(InputStream is) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {

        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}
