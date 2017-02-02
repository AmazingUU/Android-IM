package com.example.administrator.im.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.im.R;
import com.example.administrator.im.model.bean.PickContactInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/1.
 */

//选择联系人的页面适配器
public class PickContactAdapter extends BaseAdapter {
    private Context mContext;
    private List<PickContactInfo> mPicks = new ArrayList<>();
    private List<String> mExistMembers=new ArrayList<>();//保存群中已经存在的成员集合

    public PickContactAdapter(Context mContext, List<PickContactInfo> picks,List<String> existMembers) {
        this.mContext = mContext;

        if (mPicks != null && mPicks.size() >= 0) {
            mPicks.clear();

            mPicks.addAll(picks);
        }

        //加载已经存在的成员集合
        mExistMembers.clear();
        mExistMembers.addAll(existMembers);
    }

    @Override
    public int getCount() {
        return mPicks == null ? 0 : mPicks.size();
    }

    @Override
    public Object getItem(int i) {
        return mPicks.get(i);
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

            view=View.inflate(mContext, R.layout.item_pick,null);

            holder.cb_pick= (CheckBox) view.findViewById(R.id.cb_pick);
            holder.tv_name= (TextView) view.findViewById(R.id.tv_pick_name);

            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }

        //获取当前item数据
        PickContactInfo pickContactInfo = mPicks.get(i);

        //显示数据
        holder.tv_name.setText(pickContactInfo.getUser().getName());
        holder.cb_pick.setChecked(pickContactInfo.isChecked());

        //判断
        if (mExistMembers.contains(pickContactInfo.getUser().getHxid())){
            holder.cb_pick.setChecked(true);
            pickContactInfo.setChecked(true);
        }

        //返回view
        return view;
    }

    //获取选择的联系人
    public List<String> getPickContacts() {
        List<String> picks=new ArrayList<>();

        for (PickContactInfo pick:mPicks){
            //判断是否选中
            if (pick.isChecked()){
                picks.add(pick.getUser().getName());
            }
        }

        return picks;
    }

    private class ViewHolder{
        private CheckBox cb_pick;
        private TextView tv_name;
    }
}
