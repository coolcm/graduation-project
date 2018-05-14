package com.example.graduationproject.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by csn on 2018/3/17.
 */

public class CommentItemBean extends DataSupport implements Serializable{ //每条评论相关信息
    private String content; //评论内容
    private String commentatorName; //评论者名称
    private int commentatorPhotoId; //评论者头像资源ud
    private int commentatorCredit; //评论者信用
    private Date commentTime; //评论时间
    private String userName; //被评论者名称
    private String resourceHash;  //被评论的资源hash

    public CommentItemBean(String content, String commentatorName, int commentatorPhotoId, int commentatorCredit, String userName, String resourceHash) {
        this.content = content;
        this.commentatorName = commentatorName;
        this.commentatorPhotoId = commentatorPhotoId;
        this.commentatorCredit = commentatorCredit;
        this.userName = userName;
        this.commentTime = new Date();
        this.resourceHash = resourceHash;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCommentatorPhotoId() {
        return commentatorPhotoId;
    }

    public void setCommentatorPhotoId(int commentatorPhotoId) {
        this.commentatorPhotoId = commentatorPhotoId;
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

    public String getResourceHash() {
        return resourceHash;
    }

    public void setResourceHash(String resourceHash) {
        this.resourceHash = resourceHash;
    }
}
