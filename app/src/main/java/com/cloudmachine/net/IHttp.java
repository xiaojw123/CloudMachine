package com.cloudmachine.net;import java.util.List;import org.apache.http.NameValuePair;/** * 2013-10-17 * * @author coca */public interface IHttp {      /**       * 通用的接口调用，建议用HttpURLConnectionImpl（官方推荐，个人认为速度比较快），HttpClient(功能比较强大，但是在       * android上用会有少许bugs，后来官方在2.3以后修改了。但是对程序员的要求比较高0.0 )       * 下面是官方的推荐：       * Android includes two HTTP clients: HttpURLConnection and Apache HTTP       * Client. Both support HTTPS, streaming uploads and downloads, configurable       * timeouts, IPv6 and connection pooling. Apache HTTP client has fewer bugs       * in Android 2.2 (Froyo) and earlier releases. For Android 2.3       * (Gingerbread) and later, HttpURLConnection is the best choice. Its simple       * API and small size makes it great fit for Android. Transparent       * compression and response caching reduce network use, improve speed and       * save battery. See the Android Developers Blog for a comparison of the two       * HTTP clients.       */      public String post(String urlString, List<NameValuePair> params)              throws Exception;      public String get(String urlString, List<NameValuePair> params)              throws Exception;            public String uploadPost(String urlString, List<NameValuePair> params)    		  throws Exception;}