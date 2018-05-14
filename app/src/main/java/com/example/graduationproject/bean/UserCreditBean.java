package com.example.graduationproject.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by csn on 2018/4/10.
 */

public class UserCreditBean extends DataSupport implements Serializable {

    private String userName;
    private int userCredit;
    private int headPhotoId; //头像id

    public UserCreditBean(String userName, int userCredit, int headPhotoId) {
        this.userName = userName;
        this.userCredit = userCredit;
        this.headPhotoId = headPhotoId;
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

    public int getHeadPhotoId() {
        return headPhotoId;
    }

    public void setHeadPhotoId(int headPhotoId) {
        this.headPhotoId = headPhotoId;
    }
}
