package com.example.graduationproject.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by csn on 2018/3/15.
 */

public class MyCache {

    public static void setCache(Context context, String child, Serializable object) {
        File file = new File(context.getCacheDir(), child + "/cache.txt");
        if (file.exists()) {
            return;
        }
        if (file.getParentFile().exists() || file.getParentFile().mkdirs()) {
            FileOutputStream fileOutputStream = null;
            ObjectOutputStream objectOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file.getPath());
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(object);
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

    public static boolean isCacheExist(Context context, String child) {
        File file = new File(context.getCacheDir(), child + "/cache.txt");
        return file.exists();
    }

    public static Object getCache(Context context, String child) {
        File file = new File(context.getCacheDir(), child + "/cache.txt");
        if (file.exists()) {
            FileInputStream fileInputStream = null;
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
