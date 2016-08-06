package com.amenuo.monitor.utils;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by laps on 8/1/16.
 */
public class HttpRequest {

    public static final String BAIDU_API_KEY = "ade93ea486e5d4ced4bc8552978d17e6";
    private static final String DOMAIN = "http://www.tcyxk.com";
    private static final String LUMP_CATEGORY_LIST_PATH = "/category/list";
    private static final String AD_LIST_PATH = "/ad/list";
    private static final String LIVE_LIST_PATH = "/live/list";
    private static final String REGISTER_PATH = "/user/add";
    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static void requestLumpCategorys(Callback responseCallback) {
        String url = DOMAIN + LUMP_CATEGORY_LIST_PATH;
        enqueue(url, responseCallback);
    }

    public static void requestLives(Callback responseCallback) {
        String url = DOMAIN + LIVE_LIST_PATH;
        enqueue(url, responseCallback);
    }

    public static void requestAds(Callback responseCallback) {
        String url = DOMAIN + AD_LIST_PATH;
        enqueue(url, responseCallback);
    }

    public static void requestWeather(String cityName, Callback responseCallback){
        String url = "http://apis.baidu.com/apistore/weatherservice/cityname?cityname=" + cityName;
        Request request = new Request.Builder()
                .url(url)
                .header("apikey", BAIDU_API_KEY)
                .build();
        enqueue(request, responseCallback);
    }

    public static void requestRegister(String userName, String passWord, Callback responseCallback) {
        String url = DOMAIN + REGISTER_PATH;
        String json = String.format("{\"userName\":\"%s\",\"passWord\":\"%s\",\"phoneNumber\":\"%s\"}", userName, passWord, userName);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        enqueue(request, responseCallback);
    }

    public static void enqueue(String url, Callback responseCallback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        enqueue(request, responseCallback);
    }

    public static void enqueue(Request request, Callback responseCallback) {
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }

}
