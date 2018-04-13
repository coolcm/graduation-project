package com.example.graduationproject.bean;

import com.example.graduationproject.utils.AppUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by csn on 2018/4/13.
 * 区块数据结构，不存储数据库，但是用于广播
 */

public class BlockBean implements Serializable {
    private String prevHash; //前一区块哈希
    private String hash; //本区块哈希
    private long timeStamp; //时间戳
    private int resourceLength = 0;
    private ArrayList<ListItemBean> resourceList = new ArrayList<>(); //发送的文字信息列表
    private int commentLength = 0;
    private ArrayList<CommentItemBean> commentList = new ArrayList<>(); //评论信息列表
    private int agreeLength = 0;
    private ArrayList<AgreeItemBean> agreeList = new ArrayList<>(); //点赞信息列表
    private int disagreeLength = 0;
    private ArrayList<DisagreeItemBean> disagreeList = new ArrayList<>(); //反对信息列表

    public BlockBean(String prevHash) {
        this.prevHash = prevHash;
    }

    //区块广播前先计算时间戳与哈希
    public void save() {
        timeStamp = new Date().getTime();
        hash = AppUtils.getSHA256Str(prevHash + timeStamp + new String(AppUtils.object2Bytes(resourceList)) + new String(AppUtils.object2Bytes(commentList)) +
                new String(AppUtils.object2Bytes(agreeList)) + new String(AppUtils.object2Bytes(disagreeList)));
    }

    public void addResourceItem(ListItemBean listItemBean) {
        resourceList.add(listItemBean);
        resourceLength ++;
    }

    public void addCommentItem(CommentItemBean commentItemBean) {
        commentList.add(commentItemBean);
        commentLength ++;
    }

    public void addAgreeItem(AgreeItemBean agreeItemBean) {
        agreeList.add(agreeItemBean);
        agreeLength ++;
    }

    public void addDisagreeItemBean(DisagreeItemBean disagreeItemBean) {
        disagreeList.add(disagreeItemBean);
        disagreeLength ++;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public String getHash() {
        return hash;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getResourceLength() {
        return resourceLength;
    }

    public ArrayList<ListItemBean> getResourceList() {
        return resourceList;
    }

    public int getCommentLength() {
        return commentLength;
    }

    public ArrayList<CommentItemBean> getCommentList() {
        return commentList;
    }

    public int getAgreeLength() {
        return agreeLength;
    }

    public ArrayList<AgreeItemBean> getAgreeList() {
        return agreeList;
    }

    public int getDisagreeLength() {
        return disagreeLength;
    }

    public ArrayList<DisagreeItemBean> getDisagreeList() {
        return disagreeList;
    }
}
