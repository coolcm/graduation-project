package com.example.graduationproject.bean;

import android.view.View;

/**
 * Created by csn on 2018/5/14. 设置所有可选头像
 */

public class UserPhotoBean {
    private int resourceId; //头像资源id
    private int visibility; //设置check图标是否可见

    public UserPhotoBean(int resourceId) {
        this.resourceId = resourceId;
        this.visibility = View.INVISIBLE;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }
}
