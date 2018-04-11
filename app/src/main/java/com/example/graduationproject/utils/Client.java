package com.example.graduationproject.utils;

import android.util.Log;

import com.example.graduationproject.bean.AgreeItemBean;
import com.example.graduationproject.bean.CommentItemBean;
import com.example.graduationproject.bean.DisagreeItemBean;
import com.example.graduationproject.bean.ListItemBean;
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
    private String clientIP;
    private String clientPort;
    private Map<String,String> map = new HashMap<>();
    private DatagramPacket datagramPacket;
    private DatagramSocket datagramSocket;
    private SocketAddress socketAddress;
    private OnReceiveItemListener onReceiveItemListener;

    public void setOnReceiveItemListener(OnReceiveItemListener onReceiveItemListener) {
        this.onReceiveItemListener = onReceiveItemListener;
    }

    private static Client client;

    public static Client getInstance() {
        if (client == null) {
            client = new Client("59.78.15.78", 1234);
        }
        return client;
    }

    public static void closeClient() {
        client = null;
    }

    public Client (String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        try {
            datagramSocket = new DatagramSocket(30000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLocalAddress() {
        if (clientIP != null && clientPort != null ) {
            return;
        }
        try {
            socketAddress = new InetSocketAddress(serverIP, serverPort);
            byte buff[] = ("login:" + getLocalHostLANAddress().getHostAddress() + ":" + datagramSocket.getLocalPort()).getBytes();
            byte buffer[] = new byte[8 * 1024];
            datagramPacket = new DatagramPacket(buff, 0, buff.length, socketAddress);
            datagramSocket.send(datagramPacket);//发送信息到服务器
            datagramPacket.setData(buffer, 0, buffer.length);
            datagramSocket.receive(datagramPacket);
            String message = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
            String str[] = message.split(",");
            clientIP = str[0];
            clientPort = str[1];
            System.out.println("本机地址" + message);
            new Thread(new UdpReceiveThread(datagramSocket)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPeerAddress() {
        try {
            byte[] buff = "findPeers".getBytes();
            socketAddress = new InetSocketAddress(serverIP, serverPort);
            datagramPacket = new DatagramPacket(buff, 0, buff.length, socketAddress);
            datagramSocket.send(datagramPacket);
            System.out.println("findPeers");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendLogoutMessage() {
        try {
            byte[] buff = "logout".getBytes();
            socketAddress = new InetSocketAddress(serverIP, serverPort);
            datagramPacket = new DatagramPacket(buff, 0, buff.length, socketAddress);
            Log.e("sendLogoutMessage: ", "我下线啦");
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
            System.out.println(key);
            System.out.println(clientIP);
            if (key.split(":")[0].equals(clientIP)) {
                socketAddress = new InetSocketAddress(value.split(":")[0], Integer.parseInt(value.split(":")[1]));
                System.out.println("use local address");
            } else {
                socketAddress = new InetSocketAddress(key.split(":")[0], Integer.parseInt(key.split(":")[1]));
                System.out.println("use client address");
            }
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
                    Object object = AppUtils.bytes2Object(datagramPacket.getData());
                    if (object instanceof HashMap) {
                        map.clear();
                        map.putAll((HashMap<String, String>) object);
                        for (String address: map.keySet()) {
                            System.out.println("外网地址" + address + "," + "内网地址" + map.get(address));
                        }
                    } else if (object instanceof ListItemBean || object instanceof CommentItemBean || object instanceof AgreeItemBean || object instanceof DisagreeItemBean) {
                        onReceiveItemListener.onReceiveItem(object);
                        System.out.println("接收到" + object);
                    } else {
                        String message = new String(datagramPacket.getData(),0,datagramPacket.getLength());
                        System.out.println("接收到"+ message);
                        if (message.equals("exit")) {
                            break;
                        }
                    }
                    buffer = new byte[1024];
                    datagramPacket.setData(buffer,0, buffer.length);
                }
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                datagramSocket.close();
            }
        }
    }

    private InetAddress getLocalHostLANAddress() throws Exception {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
        } catch (Exception e) {
            e.printStackTrace();
        }
        return InetAddress.getLocalHost();
    }
}
