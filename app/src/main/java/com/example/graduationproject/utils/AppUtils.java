package com.example.graduationproject.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by csn on 2018/3/19. App相关操作方法代码
 */

public class AppUtils {
    //可序列化对象转为比特流
    public static byte[] object2Bytes(Serializable object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        byte[] bytes = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            bytes = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    };

    //比特流转化为对象
    public static Object bytes2Object(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = null;
        Object object = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            object =  objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    // 计算sha256算法的hash值
    public static String getSHA256Str(String str){
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    //byte转为16进制表示
    private static String byte2Hex(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder();
        String temp;
        for (byte b: bytes){
            temp = Integer.toHexString(b & 0xFF);
            if (temp.length() == 1){
                //1得到一位的进行补0操作
                stringBuilder.append("0");
            }
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }
}
