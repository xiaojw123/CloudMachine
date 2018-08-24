package com.cloudmachine.net;

import android.os.Build;

import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.utils.VersionU;

import org.apache.http.NameValuePair;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;


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
    public String post(String urlString, List<NameValuePair> params)
            throws Exception {
        if (urlString == null & urlString.length() <= 0) {
            throw new NullPointerException("url is null or .........");
        }
        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url
                .openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        //  setRequestMethod和 urlConnection.setDoOutput(true)只需要设置一个，暂时先全设置
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setConnectTimeout(TIMEOUT);
        httpURLConnection.setReadTimeout(TIMEOUT);
        httpURLConnection.setInstanceFollowRedirects(true);
        httpURLConnection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
//        LocationBean locBean = DataSupportManager.findFirst(LocationBean.class);
//        if (locBean != null) {
//            String lat = locBean.getLat();
//            String lng = locBean.getLng();
//            if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lng)) {
//                httpURLConnection.setRequestProperty("lat", lat);
//                httpURLConnection.setRequestProperty("lng", lng);
//            }
//            String address = locBean.getAddress();
//            String provice = locBean.getProvince();
//            String city = locBean.getCity();
//            String district = locBean.getDistrict();
//            if (!TextUtils.isEmpty(address)) {
//                httpURLConnection.setRequestProperty("address", address);
//            }
//            if (!TextUtils.isEmpty(provice)) {
//                httpURLConnection.setRequestProperty("province", provice);
//            }
//            if (!TextUtils.isEmpty(city)) {
//                httpURLConnection.setRequestProperty("city", city);
//            }
//            if (!TextUtils.isEmpty(district)) {
//                httpURLConnection.setRequestProperty("district", district);
//            }
//        }
        httpURLConnection.setRequestProperty("osPlatform", "Android");
        httpURLConnection.setRequestProperty("osSystem", Build.VERSION.RELEASE);
        httpURLConnection.setRequestProperty("osVersion", VersionU.getVersionName());
        int size = params.size();
//            if ( size > 0) {
        DataOutputStream out = new DataOutputStream(
                httpURLConnection.getOutputStream());
        StringBuffer sb = new StringBuffer();
        for (int a = 0; a < size; a++) {
            if (params.get(a) != null) {
                String key = params.get(a).getName();
                if (a > 0) {
                    sb.append("&");
                }
                if (params.get(a).getValue() == null) {
                    sb.append(key + "="
                            + URLEncoder.encode("", "UTF-8"));
                } else {
                    sb.append(key + "="
                            + URLEncoder.encode(params.get(a).getValue(), "UTF-8"));
                }

            }

        }
        out.writeBytes(sb.toString());
        out.flush();
        out.close();
        AppLog.printURl(urlString + "?" + sb.toString());
        String resultString = "";
        InputStream in = httpURLConnection.getInputStream();
        if (null != in) {
            resultString = IOUtil.inputStreamToString(in);
            in.close();
        }
        httpURLConnection.disconnect();
        AppLog.printURl(resultString);
        return resultString;
    }




    @Override
    public String get(String urlString, List<NameValuePair> params)
            throws Exception {
        StringBuffer sb = new StringBuffer();
        int size = params.size();
        for (int a = 0; a < size; a++) {
            if (params.get(a) != null) {
                String key = params.get(a).getName();
                if (a > 0) {
                    sb.append("&");
                }

                if (params.get(a).getValue() == null) {
                    sb.append(key + "="
                            + URLEncoder.encode("", "UTF-8"));
                } else {
                    sb.append(key + "="
                            + URLEncoder.encode(params.get(a).getValue(), "UTF-8"));
                }

                //                        }
            }

        }
        String resultString = "";
        urlString += "?" + sb.toString();
        AppLog.printURl(urlString);
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setConnectTimeout(TIMEOUT);
        conn.setReadTimeout(TIMEOUT);
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("osPlatform", "Android");
        conn.setRequestProperty("osSystem", Build.VERSION.RELEASE);
        conn.setRequestProperty("osVersion", VersionU.getVersionName());
        //		int resCode=conn.getResponseCode();
        conn.connect();
        InputStream in = conn.getInputStream();
        if (null != in) {
            resultString = IOUtil.inputStreamToString(in);
            in.close();
        }
        AppLog.printURl(resultString);
        return resultString;
    }


}
