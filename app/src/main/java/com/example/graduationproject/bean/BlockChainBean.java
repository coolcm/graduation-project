package com.example.graduationproject.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by csn on 2018/4/13.
 * 区块链数据结构，存储在数据库中
 */

public class BlockChainBean extends DataSupport implements Serializable {
    private int id; //数据库里的区块链id编号，区块同步时作为查询索引
    private long timeStamp; //时间戳
    private String prevHash; //前一区块哈希
    private String hash; //本区块哈希
    private byte[] blockBeanInfo; //存储区块结构序列化的值

    public BlockChainBean(long timeStamp, String prevHash, String hash, byte[] blockBeanInfo) {
        this.timeStamp = timeStamp;
        this.prevHash = prevHash;
        this.hash = hash;
        this.blockBeanInfo = blockBeanInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public String getHash() {
        return hash;
    }

    public byte[] getBlockBeanInfo() {
        return blockBeanInfo;
    }
}
