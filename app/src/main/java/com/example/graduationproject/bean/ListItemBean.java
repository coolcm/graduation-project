package com.example.graduationproject.bean;

import java.io.Serializable;

/**
 * Created by csn on 2018/3/14.
 */

public class ListItemBean implements Serializable {
    private String content;

    public ListItemBean() {

    }

    public ListItemBean(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
