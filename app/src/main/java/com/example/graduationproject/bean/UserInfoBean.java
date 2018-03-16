package com.example.graduationproject.bean;

import java.io.Serializable;

/**
 * Created by csn on 2018/3/15.
 */

public class UserInfoBean implements Serializable{

    private String userName;
    private String userPassword;
    private String userId;
    private int credit;

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
