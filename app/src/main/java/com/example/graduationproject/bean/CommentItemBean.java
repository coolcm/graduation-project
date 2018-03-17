package com.example.graduationproject.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by csn on 2018/3/17.
 */

public class CommentItemBean implements Serializable{
    private String content;
    private String commentatorName;
    private int commentatorCredit;
    private Date commentTime;
    private String userName;

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
