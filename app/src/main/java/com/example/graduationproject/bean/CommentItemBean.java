package com.example.graduationproject.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by csn on 2018/3/17.
 */

public class CommentItemBean implements Serializable{ //每条评论相关信息
    private String content; //评论内容
    private String commentatorName; //评论者名称
    private int commentatorCredit; //评论者信用
    private Date commentTime; //评论时间
    private String userName; //被评论者名称

    public CommentItemBean(String content, String commentatorName, int commentatorCredit, String userName) {
        this.content = content;
        this.commentatorName = commentatorName;
        this.commentatorCredit = commentatorCredit;
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentatorName() {
        return commentatorName;
    }

    public void setCommentatorName(String commentatorName) {
        this.commentatorName = commentatorName;
    }

    public int getCommentatorCredit() {
        return commentatorCredit;
    }

    public void setCommentatorCredit(int commentatorCredit) {
        this.commentatorCredit = commentatorCredit;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}