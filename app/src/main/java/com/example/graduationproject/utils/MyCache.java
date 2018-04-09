package com.example.graduationproject.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by csn on 2018/3/15.
 */

public class MyCache { //缓存相关数据信息

    public static void setCache(Context context, String child, Serializable object) { //设置缓存信息
        File file = new File(context.getCacheDir(), child + "/cache.txt");
        if (file.exists()) {
            return;
        }
        if (file.getParentFile().exists() || file.getParentFile().mkdirs()) {
            FileOutputStream fileOutputStream;
            ObjectOutputStream objectOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file.getPath());
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(object);
                objectOutputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (objectOutputStream != null) {
                    try {
                        objectOutputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void updateCache(Context context, String child, Serializable object) { //设置缓存信息
        File file = new File(context.getCacheDir(), child + "/cache.txt");
        if (file.exists()) {
            Log.i("updateCache: ", "更新用户缓存信息");
        }
        if (file.getParentFile().exists() || file.getParentFile().mkdirs()) {
            FileOutputStream fileOutputStream;
            ObjectOutputStream objectOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file.getPath());
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(object);
                objectOutputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (objectOutputStream != null) {
                    try {
                        objectOutputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static boolean isCacheExist(Context context, String child) { //判断缓存信息是否存在
        File file = new File(context.getCacheDir(), child + "/cache.txt");
        return file.exists();
    }

    public static Object getCache(Context context, String child) { //获取缓存信息
        File file = new File(context.getCacheDir(), child + "/cache.txt");
        if (file.exists()) {
            FileInputStream fileInputStream;
            ObjectInputStream objectInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                objectInputStream = new ObjectInputStream(fileInputStream);
                Object object = objectInputStream.readObject();
                System.out.println(object);
                return object;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (objectInputStream != null) {
                    try {
                        objectInputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
