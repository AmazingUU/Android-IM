package com.example.administrator.im.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.im.R;
import com.example.administrator.im.model.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/2.
 */

public class GroupDetailAdapter extends BaseAdapter {
    private Context mContext;
    private boolean mIsCanModify;//是否允许添加和删除群成员
    private List<UserInfo> mUsers=new ArrayList<>();
    private boolean mIsDeleteModel;//删除模式，true：可以删除，false：不可删除
    private OnGroupDetailListener mOnGroupDetailListener;

    public GroupDetailAdapter(Context context,boolean isCanModify,OnGroupDetailListener onGroupDetailListener) {
        mContext=context;
        mIsCanModify=isCanModify;
        mOnGroupDetailListener=onGroupDetailListener;
    }

    //获取当前的删除模式
    public boolean ismIsDeleteModel() {
        return mIsDeleteModel;
    }

    //设置当前的删除模式
    public void setmIsDeleteModel(boolean mIsDeleteModel) {
        this.mIsDeleteModel = mIsDeleteModel;
    }

    //刷新方法
    public void refresh(List<UserInfo> users){

        if (users!=null&&users.size()>=0){
            mUsers.clear();

            //添加加号和减号
            initUsers();

            mUsers.addAll(0,users);
        }

        notifyDataSetChanged();
    }

    private void initUsers() {
        UserInfo add=new UserInfo("add");
        UserInfo delete=new UserInfo("delete");

        mUsers.add(delete);
        mUsers.add(0,add);
    }

    @Override
    public int getCount() {
        return mUsers==null?0:mUsers.size();
    }

    @Override
    public Object getItem(int i) {
        return mUsers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //获取或创建viewholder
        ViewHolder holder=null;
        if (view==null){
            holder=new ViewHolder();

            view=View.inflate(mContext, R.layout.item_groupdetail,null);

            holder.photo= (ImageView) view.findViewById(R.id.iv_group_detail_photo);
            holder.delete= (ImageView) view.findViewById(R.id.iv_group_detail_delete);
            holder.name= (TextView) view.findViewById(R.id.tv_group_detail_name);

            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }

        //获取当前item数据
        final UserInfo userInfo = mUsers.get(i);

        //显示数据
        if (mIsCanModify){//群主或开放群权限

            //布局处理
            if (i==getCount()-1){//减号处理
                //判断是否是删除模式
                if (mIsDeleteModel){
                    view.setVisibility(View.INVISIBLE);
                }else {
                    view.setVisibility(View.VISIBLE);

                    holder.photo.setImageResource(R.drawable.em_smiley_minus_btn_pressed);
                    holder.delete.setVisibility(View.GONE);
                    holder.name.setVisibility(View.INVISIBLE);
                }
            }else if (i==getCount()-2){//加号处理
                //判断是否是删除模式
                if (mIsDeleteModel){
                    view.setVisibility(View.INVISIBLE);
                }else {
                    view.setVisibility(View.VISIBLE);

                    holder.photo.setImageResource(R.drawable.em_smiley_add_btn_pressed);
                    holder.delete.setVisibility(View.GONE);
                    holder.name.setVisibility(View.INVISIBLE);
                }
            }else {//群成员
                view.setVisibility(View.VISIBLE);
                holder.name.setVisibility(View.VISIBLE);

                holder.name.setText(userInfo.getName());
                holder.photo.setImageResource(R.drawable.em_default_avatar);

                if (mIsDeleteModel){
                    holder.delete.setVisibility(View.VISIBLE);
                }else {
                    holder.delete.setVisibility(View.GONE);
                }
            }

            //点击事件处理
            if (i==getCount()-1){   //减号
                holder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!mIsDeleteModel){
                            mIsDeleteModel=true;
                            notifyDataSetChanged();
                        }
                    }
                });
            }else if (i==getCount()-2){ //加号
                holder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnGroupDetailListener.onAddMember();
                    }
                });
            }else {
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnGroupDetailListener.onDeleteMember(userInfo);
                    }
                });
            }

        }else {//普通的群成员
            if (i==getCount()-1||i==getCount()-2){
                view.setVisibility(View.GONE);
            }else {
                view.setVisibility(View.VISIBLE);

                //名称
                holder.name.setText(userInfo.getName());
                //头像
                holder.photo.setImageResource(R.drawable.em_default_avatar);
                //删除
                holder.delete.setVisibility(View.GONE);
            }
        }

        //返回view
        return view;
    }

    private class ViewHolder{
        private ImageView photo,delete;
        private TextView name;
    }

    public interface OnGroupDetailListener{
        //添加群成员方法
        void onAddMember();
        //删除群成员方法
        void onDeleteMember(UserInfo user);
    }
}
