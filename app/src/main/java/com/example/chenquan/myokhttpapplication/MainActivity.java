package com.example.chenquan.myokhttpapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private HttpGet httpGet;
    private MyHandler myHandler;
    private static final int GETRESPONSE = 0x000001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        init();
        httpGet.get("https://www.baidu.com/");
    }

    public class HttpGet{
        OkHttpClient okHttpClient = new OkHttpClient();
        void get(String url){
            Request request = new Request.Builder().url(url).build();
            try {
//                Response response = okHttpClient.newCall(request).execute();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.i("CQ","failure");
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        final String text =  response.body().toString();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.what = GETRESPONSE;
                                msg.obj = text;
                                myHandler.sendMessage(msg);
                            }
                        });
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void initView(){
        tv = (TextView)findViewById(R.id.tv);
    }

    private void init(){
        httpGet = new HttpGet();
        myHandler = new MyHandler();
    }
    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GETRESPONSE:
                    String text = msg.obj.toString();
                   tv.setText(""+text);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

}
