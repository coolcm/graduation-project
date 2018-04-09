package com.example.graduationproject.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by csn on 2018/4/9.
 */

public class AgreeItemBean extends DataSupport implements Serializable {
    private String resourceHash;  //被点赞的资源hash
    private String userName; //被点赞的用户名称
    private String agreeName; //点赞者名称

    public AgreeItemBean(String resourceHash, String userName, String agreeName) {
        this.resourceHash = resourceHash;
        this.userName = userName;
        this.agreeName = agreeName;
    }

    public String getResourceHash() {
        return resourceHash;
    }

    public void setResourceHash(String resourceHash) {
        this.resourceHash = resourceHash;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAgreeName() {
        return agreeName;
    }

    public void setAgreeName(String agreeName) {
        this.agreeName = agreeName;
    }
}
