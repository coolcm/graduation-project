package com.example.graduationproject.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by csn on 2018/4/9.
 */

public class DisagreeItemBean extends DataSupport implements Serializable {
    private String resourceHash;  //被反对的资源hash
    private String userName; //被反对的用户名称
    private String disagreeName; //反对名称

    public DisagreeItemBean(String resourceHash, String userName, String disagreeName) {
        this.resourceHash = resourceHash;
        this.userName = userName;
        this.disagreeName = disagreeName;
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

    public String getDisagreeName() {
        return disagreeName;
    }

    public void setDisagreeName(String disagreeName) {
        this.disagreeName = disagreeName;
    }
}
