package com.example.graduationproject.bean;

import java.io.Serializable;

/**
 * Created by csn on 2018/3/15.
 */

public class UserInfoBean implements Serializable{ //用户个人信息

    private String userName; //登录用户名
    private String userPassword; //登录用户密码
    private String userId; //用户身份ID
    private int credit; //用户信用指数

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
