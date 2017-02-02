package com.example.administrator.im.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.administrator.im.R;
import com.example.administrator.im.model.Model;
import com.example.administrator.im.model.bean.UserInfo;
import com.hyphenate.chat.EMClient;

//欢迎页面
public class SplashActivity extends Activity {
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //如果当前activity已经退出，就不处理消息
            if (isFinishing()){
                return;
            }

            //判断进入主页面还是登陆页面
            toMainOrLogin();
        }
    };

    private void toMainOrLogin() {
//       new Thread(){
//           @Override
//           public void run() {
//           }
//       }.start();

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //判断是否登陆过
                if (EMClient.getInstance().isLoggedInBefore()){//登陆过

                    //获取当前用户信息
                    UserInfo account = Model.getInstance().getUserAccountDao().getAccountByHxId(EMClient.getInstance().getCurrentUser());

                    if (account==null){
                        //跳转至登陆页面
                        Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }else {
                        //登录成功后的方法
                        Model.getInstance().loginSuccess(account);

                        //跳转至主页面
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }else {//没登录过

                    //跳转至登陆页面
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                }

                finish();//结束当前页面
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler.sendMessageDelayed(Message.obtain(),2000);//发送两秒的延时消息
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);//销毁消息
    }
}
