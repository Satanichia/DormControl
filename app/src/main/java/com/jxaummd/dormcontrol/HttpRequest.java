package com.jxaummd.dormcontrol;


import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpRequest {
    private Request request;
    private OkHttpClient client;

    HttpRequest() {
        this.client = new OkHttpClient();
    }

    public void setContent(String url, String type, String data) {
        RequestBody body = new FormBody.Builder().add(type, data).build();
        this.request = new Request.Builder().url(url).post(body).build();
    }

    public void pubRequest(Callback callback) {
        try {
            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
