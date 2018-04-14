package com.example.graduationproject.interfaces;

/**
 * Created by csn on 2018/3/20. 通过socket接收到条目时的回调函数
 */

public interface OnReceiveItemListener {
    void onReceiveItem(Object object);
    void onSuccess(); //成功获取对等节点时的回调
    void onFailure(); //获取对等节点失败的回调
}
