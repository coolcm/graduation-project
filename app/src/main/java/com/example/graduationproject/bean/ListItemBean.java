package com.example.graduationproject.bean;

import com.example.graduationproject.utils.AppUtils;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

/**
 * Created by csn on 2018/3/14.
 */

public class ListItemBean extends DataSupport implements Serializable { //每项文字资源信息
    private String content; //文字内容
    private String userName; //发送者用户名
    private int userCredit; //发送者信用
    private int itemAgree; //赞同数
    private int itemDisagree; //反对数
    private int itemComment; //评论数
    private Date sendTime; //文字发送时间
    private String hash; //文字资源hash

    public ListItemBean() {

    }

    public ListItemBean(String content) { //测试使用，除主要文字内容外各项数据均随机生成
        this.content = content;
        this.userName = "test" + new Random().nextInt(100);
        this.userCredit = new Random().nextInt(100);
        this.itemAgree = new Random().nextInt(100);
        this.itemDisagree = new Random().nextInt(100);
        this.itemComment = new Random().nextInt(100);
        this.sendTime = new Date();
        this.hash = AppUtils.getSHA256Str(userName + content + sendTime.getTime());
    }

    public ListItemBean(String content, String userName, int userCredit) { //新建文字资源信息，赞同，反对，评论数一开始均为0
        this.content = content;
        this.userName = userName;
        this.userCredit = userCredit;
        this.itemAgree = 0;
        this.itemDisagree = 0;
        this.itemComment = 0;
        this.sendTime = new Date();
        this.hash = AppUtils.getSHA256Str(userName + content + sendTime.getTime());
    }

    //在list中进行indexOf的比较时会调用equals方法，重写它用于判断相同的文字资源项(通过比较资源hash)
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ListItemBean) {
            ListItemBean listItemBean = (ListItemBean) obj;
            return hash.equals(listItemBean.getHash());
        }
        return super.equals(obj);
    }

    public String getHash() {
        return hash;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
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
