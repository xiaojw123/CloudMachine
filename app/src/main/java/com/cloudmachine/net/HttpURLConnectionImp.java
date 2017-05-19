package com.cloudmachine.net;

import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.VersionU;
import com.github.mikephil.charting.utils.AppLog;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * 2013-10-17
 *
 * @author coca
 */
public class HttpURLConnectionImp implements IHttp {

    private static final int TIMEOUT = 20000;

    /**
     * @param url
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
        int size = params.size();
//            if ( size > 0) {
        DataOutputStream out = new DataOutputStream(
                httpURLConnection.getOutputStream());
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("osPlatform=Android");
        stringBuffer.append("&osVersion="
                + URLEncoder.encode(VersionU.getVersionName(), "UTF-8"));
        for (int a = 0; a < size; a++) {
            if (params.get(a) != null) {
                String key = params.get(a).getName();
//                    	  if (a == 0) {
//                              stringBuffer.append(key + "="
//                                      + URLEncoder.encode(params.get(a).getValue(), "UTF-8"));
//                        } else {
                if (params.get(a).getValue() == null) {
                    stringBuffer.append("&" + key + "="
                            + URLEncoder.encode("", "UTF-8"));
                } else {
                    stringBuffer.append("&" + key + "="
                            + URLEncoder.encode(params.get(a).getValue(), "UTF-8"));
                }

//                        }
            }

        }

        out.writeBytes(stringBuffer.toString());
        out.flush();
        out.close();
        Constants.MyLog(urlString + "?" + stringBuffer.toString());
        AppLog.print(urlString + "?" + stringBuffer.toString());
//            }

        String resultString = "";
        InputStream in = httpURLConnection.getInputStream();
        if (null != in) {
            resultString = IOUtil.inputStreamToString(in);
            in.close();
        }
        httpURLConnection.disconnect();
        return resultString;
    }

    public String post2(String urlString, List<NameValuePair> params) throws Exception {
        if (urlString == null & urlString.length() <= 0) {
            throw new NullPointerException("url is null or .........");
        }

        // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        TrustManager[] tm = {new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        }};

        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        sslContext.init(null, tm, new java.security.SecureRandom());
        // 从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();


        URL url = new URL(urlString);
        HttpsURLConnection httpURLConnection = (HttpsURLConnection) url
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
        httpURLConnection.setSSLSocketFactory(ssf);
        httpURLConnection.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        int size = params.size();
        //            if ( size > 0) {
        DataOutputStream out = new DataOutputStream(
                httpURLConnection.getOutputStream());
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("osPlatform=Android");
        stringBuffer.append("&osVersion="
                + URLEncoder.encode(VersionU.getVersionName(), "UTF-8"));
        for (int a = 0; a < size; a++) {
            if (params.get(a) != null) {
                String key = params.get(a).getName();
                //                    	  if (a == 0) {
                //                              stringBuffer.append(key + "="
                //                                      + URLEncoder.encode(params.get(a).getValue(), "UTF-8"));
                //                        } else {
                if (params.get(a).getValue() == null) {
                    stringBuffer.append("&" + key + "="
                            + URLEncoder.encode("", "UTF-8"));
                } else {
                    stringBuffer.append("&" + key + "="
                            + URLEncoder.encode(params.get(a).getValue(), "UTF-8"));
                }

                //                        }
            }

        }

        out.writeBytes(stringBuffer.toString());
        out.flush();
        out.close();
        Constants.MyLog(urlString + "?" + stringBuffer.toString());
        //            }

        String resultString = "";
        InputStream in = httpURLConnection.getInputStream();
        if (null != in) {
            resultString = IOUtil.inputStreamToString(in);
            in.close();
        }
        httpURLConnection.disconnect();
        return resultString;
    }

    @Override
    public String get(String urlString, List<NameValuePair> params)
            throws Exception {
        String resultString = "";
        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url
                .openConnection();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setConnectTimeout(TIMEOUT);
        conn.setReadTimeout(TIMEOUT);
        conn.setRequestProperty("accept", "*/*");
        //		int resCode=conn.getResponseCode();
        conn.connect();
        InputStream in = conn.getInputStream();
        if (null != in) {
            resultString = IOUtil.inputStreamToString(in);
            in.close();
        }
        httpURLConnection.disconnect();
        return resultString;
    }

    public String uploadPost(String urlString, List<NameValuePair> params) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(urlString);
            MultipartEntity mulentity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            int size = params.size();
            for (int a = 0; a < size; a++) {
                if (params.get(a) != null) {
                    String key = params.get(a).getName();
                    if (params.get(a).getValue() == null) {
                    } else {
                        mulentity.addPart(key, new StringBody(params.get(a).getValue()));
                    }
                }
            }
            httpPost.setEntity(mulentity);
            HttpResponse response = httpclient.execute(httpPost);
            // 如果返回状态为200，获得返回的结果
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                InputStream inputStream = response.getEntity().getContent();
                return IOUtil.inputStreamToString(inputStream);
            }
        } catch (Exception e) {
            Constants.MyLog(e.getLocalizedMessage());
        }
        return null;
    }

}
