package com.example.graduationproject.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by csn on 2018/4/10.
 */

public class UserCreditBean extends DataSupport implements Serializable {

    private String userName;
    private int userCredit;

    public UserCreditBean(String userName, int userCredit) {
        this.userName = userName;
        this.userCredit = userCredit;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserCredit() {
        return userCredit;
    }

    public void setUserCredit(int userCredit) {
        this.userCredit = userCredit;
    }
}
