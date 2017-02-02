package com.example.administrator.im.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.im.R;
import com.hyphenate.chat.EMGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/1.
 */

//群组列表的适配器
public class GroupListAdapter extends BaseAdapter {
    private Context mContext;
    private List<EMGroup> mGroups=new ArrayList<>();

    public GroupListAdapter(Context context) {
        mContext=context;
    }

    //刷新方法
    public void refresh(List<EMGroup> groups){
        if (groups!=null&&groups.size()>=0){
            mGroups.clear();

            mGroups.addAll(groups);

            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mGroups==null?0:mGroups.size();
    }

    @Override
    public Object getItem(int i) {
        return mGroups.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //创建或获取viewHolder
        ViewHolder holder=null;
        if (view==null){
            holder=new ViewHolder();

            view=View.inflate(mContext, R.layout.item_grouplist,null);

            holder.name= (TextView) view.findViewById(R.id.tv_grouplist_name);

            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }

        //获取当前item数据
        EMGroup emGroup = mGroups.get(i);

        //显示数据
        holder.name.setText(emGroup.getGroupName());

        //返回数据
        return view;
    }

    private class ViewHolder{
        TextView name;
    }
}
