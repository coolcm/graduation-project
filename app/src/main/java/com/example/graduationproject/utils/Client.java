package com.example.graduationproject.utils;

import android.util.Log;

import com.example.graduationproject.interfaces.OnReceiveItemListener;

import java.io.IOException;
import java.util.*;
import java.net.*;

/**
 * Created by csn on 2018/3/18. Udp打洞客户端代码
 */

public class Client {
    private String serverIP;
    private int serverPort;
    private Map<String,String> map = new HashMap<>();
    private DatagramPacket datagramPacket;
    private DatagramSocket datagramSocket;
    private SocketAddress socketAddress;
    private OnReceiveItemListener onReceiveItemListener;
    public Client (String serverIP, int serverPort, OnReceiveItemListener onReceiveItemListener) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.onReceiveItemListener = onReceiveItemListener;
    }

    public void getLocalAddress() {
        try {
            socketAddress = new InetSocketAddress(serverIP, serverPort);
            datagramSocket = new DatagramSocket();
            String str = "getIp";
            byte buff[] = str.getBytes();
            byte buffer[] = new byte[8 * 1024];
            datagramPacket = new DatagramPacket(buff, 0, buff.length, socketAddress);
            datagramSocket.send(datagramPacket);//发送信息到服务器
            datagramPacket.setData(buffer, 0, buffer.length);
            System.out.println(datagramPacket);
            datagramSocket.receive(datagramPacket);
            String message = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
            System.out.println("本机端口、IP" + message);
            new Thread(new UdpReceiveThread(datagramSocket)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPeerAddress() {
        try {
            byte[] buff = "aa".getBytes();
            socketAddress = new InetSocketAddress(serverIP, serverPort);
            datagramPacket = new DatagramPacket(buff, 0, buff.length, socketAddress);
            datagramSocket.send(datagramPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(byte[] message) {
        Set<String> set = map.keySet();
        Log.e("sendMessage: ", String.valueOf(set.size()));
        for (String key: set) {
            String value = map.get(key);
            socketAddress = new InetSocketAddress(value, Integer.parseInt(key));
            datagramPacket = new DatagramPacket(message, 0, message.length, socketAddress);
            try {
                datagramSocket.send(datagramPacket);
                System.out.println("向" + value + ":" + key + "发送信息" + message.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class UdpReceiveThread extends Thread {
        private DatagramSocket datagramSocket;
        private UdpReceiveThread(DatagramSocket datagramSocket) {
            this.datagramSocket = datagramSocket;
        }

        @Override
        public void run() {
            try{
                byte[] buffer = new byte[1024];
                DatagramPacket datagramPacket = new DatagramPacket(buffer,0, buffer.length);
                while (true)
                {
                    System.out.println("准备接收");
                    datagramSocket.receive(datagramPacket);
                    String message = new String(datagramPacket.getData(),0,datagramPacket.getLength());
                    if (message.startsWith("udp")) {
                        String port=message.split(",")[1];
                        String ip=message.split(",")[2];
                        map.put(port, ip);
                        System.out.println("目标端口、IP:"+port+","+ip);
                    } else {
                        System.out.println("接收到"+message);
                        onReceiveItemListener.onReceiveItem(AppUtils.bytes2Object(datagramPacket.getData()));
                        if (message.equals("exit")) {
                            break;
                        }
                    }
                    buffer = new byte[1024];
                    datagramPacket.setData(buffer,0, buffer.length);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
