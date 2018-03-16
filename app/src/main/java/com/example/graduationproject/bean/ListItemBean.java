package com.example.graduationproject.bean;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by csn on 2018/3/14.
 */

public class ListItemBean implements Serializable {
    private String content;
    private String userName;
    private int userCredit;
    private int itemAgree;
    private int itemDisagree;
    private int itemComment;

    public ListItemBean() {

    }

    public ListItemBean(String content) {
        this.content = content;
        this.userName = "test" + new Random().nextInt(100);
        this.userCredit = new Random().nextInt(100);
        this.itemAgree = new Random().nextInt(100);
        this.itemDisagree = new Random().nextInt(100);
        this.itemComment = new Random().nextInt(100);
    }

    public ListItemBean(String content, String userName, int userCredit) {
        this.content = content;
        this.userName = userName;
        this.userCredit = userCredit;
        this.itemAgree = 0;
        this.itemDisagree = 0;
        this.itemComment = 0;
    }

    public int getUserCredit() {
        return userCredit;
    }

    public void setUserCredit(int userCredit) {
        this.userCredit = userCredit;
    }

    public int getItemAgree() {
        return itemAgree;
    }

    public void setItemAgree(int itemAgree) {
        this.itemAgree = itemAgree;
    }

    public int getItemDisagree() {
        return itemDisagree;
    }

    public void setItemDisagree(int itemDisagree) {
        this.itemDisagree = itemDisagree;
    }

    public int getItemComment() {
        return itemComment;
    }

    public void setItemComment(int itemComment) {
        this.itemComment = itemComment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
