package com.example.graduationproject.utils;

import android.util.Log;

import com.example.graduationproject.bean.AgreeItemBean;
import com.example.graduationproject.bean.BlockBean;
import com.example.graduationproject.bean.BlockChainBean;
import com.example.graduationproject.bean.CommentItemBean;
import com.example.graduationproject.bean.DisagreeItemBean;
import com.example.graduationproject.bean.ListItemBean;
import com.example.graduationproject.interfaces.OnReceiveItemListener;

import org.litepal.crud.DataSupport;

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
            client = new Client("10.162.73.244", 1234);
        }
        return client;
    }

    public static void closeClient() {
        Log.e("closeClient", "关闭udp连接");
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
        int i;
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
            //udp传输最多一次只能传1024字节，传输较大的区块结构时需要分段传输
            try {
                for (i = 0; i * 1024 <= message.length - 1024; i++) {
                    datagramPacket = new DatagramPacket(message, i * 1024, 1024, socketAddress);
                    datagramSocket.send(datagramPacket);
                    Thread.sleep(1);
                }
                datagramPacket = new DatagramPacket(message, i * 1024, message.length % 1024, socketAddress);
                datagramSocket.send(datagramPacket);
                Thread.sleep(1);
                Log.e("向" + value + ":" + key + "发送信息", new String(message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //发送区块同步请求信息，请求编号为id之后的所有区块
    public void requestBlockChain(int id) {
        Set<String> set = map.keySet();
        for (String key: set) {
            String value = map.get(key);
            Log.e("requestBlockChain", "遍历到" + value + ":" + key);
            if (key.split(":")[0].equals(clientIP) && key.split(":")[1].equals(clientPort)) {
                continue; //如果是自己的地址，则向下一对地址发送请求
            }
            if (key.split(":")[0].equals(clientIP)) {
                socketAddress = new InetSocketAddress(value.split(":")[0], Integer.parseInt(value.split(":")[1]));
                System.out.println("use local address");
            } else {
                socketAddress = new InetSocketAddress(key.split(":")[0], Integer.parseInt(key.split(":")[1]));
                System.out.println("use client address");
            }
            byte[] message = ("requestBlockChain:" + id).getBytes();
            datagramPacket = new DatagramPacket(message, 0, message.length, socketAddress);
            try {
                datagramSocket.send(datagramPacket);
                Log.e("向" + value + ":" + key + "发送请求区块信息", new String(message));
                return; //只向一个综端发起区块同步请求
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 收到同步请求后发送本地区块信息
    private void sendBlockChain(byte[] blockChainBytes, String tempIP, int tempPort) {
        int i;
        socketAddress = new InetSocketAddress(tempIP, tempPort);
        //udp传输最多一次只能传1024字节，传输较大的区块结构时需要分段传输
        try {
            for (i = 0; i * 1024 <= blockChainBytes.length - 1024; i++) {
                datagramPacket = new DatagramPacket(blockChainBytes, i * 1024, 1024, socketAddress);
                datagramSocket.send(datagramPacket);
                Thread.sleep(1);
            }
            datagramPacket = new DatagramPacket(blockChainBytes, i * 1024, blockChainBytes.length % 1024, socketAddress);
            datagramSocket.send(datagramPacket);
            Thread.sleep(1);
            Log.e("向" + tempIP + ":" + tempPort + "发送信息", new String(blockChainBytes));
        } catch (Exception e) {
            e.printStackTrace();
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
                    //udp传输最多一次只能传1024字节，接收较大的区块结构时需要分段接收
                    StringBuilder str = new StringBuilder();
                    while (true) {
                        datagramSocket.receive(datagramPacket);
                        String temp = new String(datagramPacket.getData(), "ISO-8859-1"); //该编码方式可防止字符串和比特数组之间转换结果不一致
                        str.append(temp);
                        if (datagramPacket.getLength() < 1024) { //接收到结尾时跳出
                            break;
                        }
                    }
                    Object object = AppUtils.bytes2Object(str.toString().getBytes("ISO-8859-1"));
                    if (object instanceof HashMap) {
                        map.clear();
                        map.putAll((HashMap<String, String>) object);
                        for (String address: map.keySet()) {
                            System.out.println("外网地址" + address + "," + "内网地址" + map.get(address));
                        }
                    } else if (object instanceof ListItemBean || object instanceof CommentItemBean ||
                            object instanceof AgreeItemBean || object instanceof DisagreeItemBean || object instanceof BlockBean) {
                        onReceiveItemListener.onReceiveItem(object);
                    } else {
                        String message = new String(datagramPacket.getData(),0,datagramPacket.getLength());
                        Log.e("接收到", message);
                        if (message.equals("exit")) {
                            break;
                        } else if (message.startsWith("requestBlockChain")) { //处理区块同步请求
                            int id = Integer.parseInt(message.split(":")[1]);
                            onReceiveItemListener.onReceiveItem(id);
                            List<BlockChainBean> blockChainBeanList = DataSupport.where("id > ?", String.valueOf(id)).find(BlockChainBean.class);
                            InetSocketAddress inetSocketAddress = (InetSocketAddress) datagramPacket.getSocketAddress();;
                            for (BlockChainBean blockChain: blockChainBeanList) {
                                sendBlockChain(blockChain.getBlockBeanInfo(), inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort());
                            }
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
