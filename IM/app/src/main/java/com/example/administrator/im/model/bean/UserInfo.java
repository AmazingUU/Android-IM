package com.example.administrator.im.model.bean;

/**
 * Created by Administrator on 2017/1/26.
 */

//用户账号信息的bean类
public class UserInfo {
    private String name;//用户名称
    private String hxid;//环信服务器中用户id
    private String nick;//用户昵称
    private String photo;//头像

    public UserInfo(){

    }

    public UserInfo(String name) {
        this.hxid = name;
        this.name = name;
        this.nick = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHxid() {
        return hxid;
    }

    public void setHxid(String hxid) {
        this.hxid = hxid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", hxid='" + hxid + '\'' +
                ", nick='" + nick + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
