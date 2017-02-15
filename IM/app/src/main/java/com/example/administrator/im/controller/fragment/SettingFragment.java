package com.example.administrator.im.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.im.R;
import com.example.administrator.im.controller.activity.LoginActivity;
import com.example.administrator.im.model.Model;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by Administrator on 2017/1/27.
 */

//设置页面
public class SettingFragment extends Fragment {
    private Button btn_setting_logout;
    private TextView tv_setting_name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_setting, null);

        initView(view);

        return view;
    }

    private void initView(View view) {
        btn_setting_logout = (Button) view.findViewById(R.id.btn_setting_logout);
        tv_setting_name= (TextView) view.findViewById(R.id.tv_setting_name);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    private void initData() {
        //显示当前用户名称
        tv_setting_name.setText(EMClient.getInstance().getCurrentUser());
        
        btn_setting_logout.setText("退出登录");

        //退出登录的逻辑处理
        btn_setting_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        //登录环信服务器退出
                        EMClient.getInstance().logout(false, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                //关闭DBHelper
                                Model.getInstance().getDbManager().close();

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //更新UI显示
                                        Toast.makeText(getActivity(), "退出成功", Toast.LENGTH_SHORT).show();

                                        //回到登录页面
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);

                                        getActivity().finish();
                                    }
                                });
                            }

                            @Override
                            public void onError(int i, final String s) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "退出失败" + s, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });
                    }
                });
            }
        });
    }
}
