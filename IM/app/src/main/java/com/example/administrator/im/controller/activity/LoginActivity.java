package com.example.administrator.im.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.im.R;
import com.example.administrator.im.model.Model;
import com.example.administrator.im.model.bean.UserInfo;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

//登陆页面
public class LoginActivity extends Activity {
    private EditText et_login_name, et_login_pwd;
    private Button btn_login_regist, btn_login_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化控件
        initView();

        //初始化监听
        initListener();
    }

    private void initListener() {
        //注册按钮的点击事件处理
        btn_login_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regist();
            }
        });

        //登陆按钮的点击事件处理
        btn_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    //登陆的业务逻辑处理
    private void login() {
        //1、获取输入的用户名和密码
        final String loginName = et_login_name.getText().toString();
        final String loginPwd = et_login_pwd.getText().toString();

        //2、校验输入的用户名和密码
        if (TextUtils.isEmpty(loginName) || TextUtils.isEmpty(loginPwd)) {
            Toast.makeText(LoginActivity.this, "输入的用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //3、登录逻辑处理
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //去环信服务器登录
                EMClient.getInstance().login(loginName, loginPwd, new EMCallBack() {
                    //登录成功后的处理
                    @Override
                    public void onSuccess() {
                        //对模型层数据的处理
                        Model.getInstance().loginSuccess(new UserInfo(loginName));

                        //保存用户账号信息到本地数据库
                        Model.getInstance().getUserAccountDao().addAccount(new UserInfo(loginName));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //提示登录成功
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                                //跳转至主页面
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);

                                finish();
                            }
                        });
                    }

                    //登录失败后的处理
                    @Override
                    public void onError(int i, final String s) {
                        //提示登录失败
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "登录失败"+s, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    //登录过程中的处理
                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
    }

    //注册的业务逻辑处理
    private void regist() {
        //1、获取输入的用户名和密码
        final String registName = et_login_name.getText().toString();
        final String registPwd = et_login_pwd.getText().toString();

        //2、校验输入的用户名和密码
        if (TextUtils.isEmpty(registName) || TextUtils.isEmpty(registPwd)) {
            Toast.makeText(LoginActivity.this, "输入的用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //3、去服务器注册账号
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //环信服务器注册账号
                    EMClient.getInstance().createAccount(registName, registPwd);

                    //更新页面显示
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (e.toString().contains("User already exist")) {
                                Toast.makeText(LoginActivity.this, "注册失败，用户已经存在", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "注册失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                            }
                            Log.d("tag", e.toString());
                        }
                    });
                }
            }
        });
    }

    private void initView() {
        et_login_name = (EditText) findViewById(R.id.et_login_name);
        et_login_pwd = (EditText) findViewById(R.id.et_login_pwd);
        btn_login_regist = (Button) findViewById(R.id.btn_login_regist);
        btn_login_login = (Button) findViewById(R.id.btn_login_login);
    }
}
