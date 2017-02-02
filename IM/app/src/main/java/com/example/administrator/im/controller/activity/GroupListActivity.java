package com.example.administrator.im.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.im.R;
import com.example.administrator.im.controller.adapter.GroupListAdapter;
import com.example.administrator.im.model.Model;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class GroupListActivity extends Activity {

    private ListView lv_grouplist;
    private GroupListAdapter groupListAdapter;
    private LinearLayout ll_grouplist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        
        initView();
        
        initData();

        initListener();
    }

    private void initListener() {
        //listview的点击事件
        lv_grouplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TAG","i"+i);

                //这里要判断是否是头布局
                if (i==0){
                    return;
                }
                Intent intent = new Intent(GroupListActivity.this, ChatActivity.class);

                //传递会话类型，群id
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_GROUP);

                EMGroup emGroup = EMClient.getInstance().groupManager().getAllGroups().get(i - 1);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,emGroup.getGroupId());

                startActivity(intent);
            }
        });

        //跳转到新建群
        ll_grouplist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupListActivity.this, NewGroupActivity.class);

                startActivity(intent);
            }
        });
    }

    private void initData() {
        //初始化listview
        groupListAdapter = new GroupListAdapter(this);

        lv_grouplist.setAdapter(groupListAdapter);

        //从环信服务器获取所有群的信息
        getGroupsFromServer();
    }

    private void getGroupsFromServer() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //从环信服务器获取数据
                    List<EMGroup> mGroups = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();

                    //更新页面
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupListActivity.this, "加载群信息成功", Toast.LENGTH_SHORT).show();

                            //刷新页面
                            refresh();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupListActivity.this, "加载群信息失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    //刷新页面
    private void refresh() {
        groupListAdapter.refresh(EMClient.getInstance().groupManager().getAllGroups());
    }

    private void initView() {
        //获取listview对象
        lv_grouplist= (ListView) findViewById(R.id.lv_grouplist);

        //添加头布局
        View headerView = View.inflate(this, R.layout.header_grouplist, null);
        lv_grouplist.addHeaderView(headerView);

        ll_grouplist = (LinearLayout) headerView.findViewById(R.id.ll_grouplist);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refresh();
    }
}
