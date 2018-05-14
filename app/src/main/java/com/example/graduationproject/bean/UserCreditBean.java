package com.example.graduationproject.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by csn on 2018/4/10.
 */

public class UserCreditBean extends DataSupport implements Serializable {

    private String userName;
    private int userCredit;
    private int agreeNum; //得到的赞同数
    private int disagreeNum; //得到的反对数
    private int headPhotoId; //头像id

    public UserCreditBean(String userName, int userCredit, int headPhotoId) {
        this.userName = userName;
        this.userCredit = userCredit;
        this.agreeNum = 0;
        this.disagreeNum = 0;
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

    public int getAgreeNum() {
        return agreeNum;
    }

    public void setAgreeNum(int agreeNum) {
        this.agreeNum = agreeNum;
    }

    public int getDisagreeNum() {
        return disagreeNum;
    }

    public void setDisagreeNum(int disagreeNum) {
        this.disagreeNum = disagreeNum;
    }

    public int getHeadPhotoId() {
        return headPhotoId;
    }

    public void setHeadPhotoId(int headPhotoId) {
        this.headPhotoId = headPhotoId;
    }
}
