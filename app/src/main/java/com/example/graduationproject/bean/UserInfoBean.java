package com.example.graduationproject.bean;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by csn on 2018/3/15.
 */

public class UserInfoBean implements Serializable { //用户个人信息

    private String userName; //登录用户名
    private String userPassword; //登录用户密码
    private int credit; //用户信用指数
    private int headPhotoId; //头像id
    private List<String> agreeList = new ArrayList<>(); //赞同的资源hash列表
    private List<String> disagreeList = new ArrayList<>(); //反对的资源hash列表

    public void addAgreeList(String hash) {
        agreeList.add(hash);
    }

    public boolean containAgreeItem(String hash) {
        return agreeList.contains(hash);
    }

    public void addDisagreeList(String hash) {
        disagreeList.add(hash);
    }

    public boolean containDisAgreeItem(String hash) {
        return disagreeList.contains(hash);
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getHeadPhotoId() {
        return headPhotoId;
    }

    public void setHeadPhotoId(int headPhotoId) {
        this.headPhotoId = headPhotoId;
    }

}
