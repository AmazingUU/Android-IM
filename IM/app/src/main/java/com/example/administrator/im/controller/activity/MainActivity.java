package com.example.administrator.im.controller.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.RadioGroup;

import com.example.administrator.im.R;
import com.example.administrator.im.controller.fragment.ChatFragment;
import com.example.administrator.im.controller.fragment.ContactListFragment;
import com.example.administrator.im.controller.fragment.SettingFragment;

public class MainActivity extends FragmentActivity {
    private RadioGroup rg_main;
    private ChatFragment chatFragment;
    private ContactListFragment contactListFragment;
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initView();

        initData();

        initListener();
    }

    private void initListener() {
        //RadioGroup的选择事件
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Fragment fragment =null;
                switch (i){
                    //会话列表页面
                    case R.id.rb_main_chat:
                        fragment=chatFragment;
                        break;
                    //联系人列表页面
                    case R.id.rb_main_contact:
                        fragment=contactListFragment;
                        break;
                    //设置页面
                    case R.id.rb_main_setting:
                        fragment=settingFragment;
                        break;
                }

                //实现fragment切换方法
                switchFragment(fragment);
            }
        });

        //默认实现会话页面
        rg_main.check(R.id.rb_main_chat);
    }

    private void switchFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_main,fragment).commit();
    }

    private void initData() {
        //创建三个fragment对象
        chatFragment = new ChatFragment();
        contactListFragment = new ContactListFragment();
        settingFragment = new SettingFragment();
    }

    private void initView() {
        rg_main= (RadioGroup) findViewById(R.id.rg_main);
    }
}
