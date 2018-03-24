package com.example.graduationproject.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.example.graduationproject.bean.ListItemBean;
import com.example.graduationproject.broadcast.WifiDirectBroadcastReceiver;
import com.example.graduationproject.interfaces.OnReceiveItemListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WIFI_P2P_SERVICE;

/**
 * Created by csn on 2018/3/23.
 */

public class WifiDirect {
    private Activity activity;
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private WifiP2pManager.PeerListListener peerListListener;
    private WifiP2pManager.ConnectionInfoListener connectionInfoListener;
    private IntentFilter intentFilter;
    private List<WifiP2pDevice> list = new ArrayList<>();
    private BroadcastReceiver broadcastReceiver;
    private OnReceiveItemListener onReceiveItemListener;
    private ListItemBean listItem;
    private boolean connect = false; //判断是否主动连接

    public WifiDirect(Activity activity, OnReceiveItemListener onReceiveItemListener) {
        this.activity = activity;
        this.onReceiveItemListener = onReceiveItemListener;
    }

    public static WifiDirect newInstance(Activity activity, OnReceiveItemListener onReceiveItemListener) {
        WifiDirect wifiDirect = new WifiDirect(activity, onReceiveItemListener);
        wifiDirect.initiate();
        wifiDirect.discoverPeers();
        wifiDirect.registerReceiver();
        return wifiDirect;
    }

    public void setListItem (ListItemBean listItem) {
        this.listItem = listItem;
    }

    public void initiate() {
        manager = (WifiP2pManager) activity.getSystemService(WIFI_P2P_SERVICE);
        channel = manager.initialize(activity, activity.getMainLooper(), null);
        peerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                list.clear();
                list.addAll(wifiP2pDeviceList.getDeviceList());
                for (WifiP2pDevice device: list) {
                    Log.e("onConnectionInfo", "发现对等设备" + device.deviceAddress);
                }
            }
        };
        connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
            @Override
            public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
                final InetAddress inetAddress = wifiP2pInfo.groupOwnerAddress;
                if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
                    Log.e("onConnectionInfo", "服务端"+ String.valueOf(connect));
                    if (connect) { //主动连接
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ServerSocket serverSocket = new ServerSocket(9000);
                                    Socket socket = serverSocket.accept ();
                                    InputStream inputStream = socket.getInputStream();
                                    if (inputStream.read() == 114) {
                                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                        objectOutputStream.writeObject(listItem);
                                    }
                                    serverSocket.close ();
                                } catch (IOException e) {
                                    e.printStackTrace ();
                                } finally {
                                    removeGroup();
                                }
                            }
                        }).start();
                    } else { //被动连接
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ServerSocket serverSocket = new ServerSocket(8999);
                                    Socket socket = serverSocket.accept ();
                                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                                    ListItemBean listItem = (ListItemBean) input.readObject();
                                    onReceiveItemListener.onReceiveItem(listItem);
                                    serverSocket.close ();
                                    Log.e("run: ", listItem.getContent());
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                } finally {
                                    removeGroup();
                                }
                            }
                        }).start();
                    }
                } else if (wifiP2pInfo.groupFormed) {
                    Log.e("onConnectionInfo", "客户端"+String.valueOf(connect));
                    if (connect) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Socket socket = new Socket ();
                                try {
                                    socket.bind (null);
                                    socket.connect (new InetSocketAddress(inetAddress.getHostAddress(), 8999),5000);
                                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                    objectOutputStream.writeObject(listItem);
                                } catch (IOException e) {
                                    e.printStackTrace ();
                                } finally {
                                    if (socket.isConnected ()){
                                        try {
                                            socket.close ();
                                        } catch (IOException e) {
                                            e.printStackTrace ();
                                        }
                                    }
                                }
                            }
                        }).start();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Socket socket = new Socket();
                                Log.e("onConnectionInfo", socket.toString());
                                try {
                                    socket.bind (null);
                                    socket.connect(new InetSocketAddress(inetAddress.getHostAddress(), 9000),5000);
                                    OutputStream outputStream = socket.getOutputStream ();
                                    outputStream.write("r".getBytes());
                                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                                    ListItemBean listItem = (ListItemBean) input.readObject();
                                    onReceiveItemListener.onReceiveItem(listItem);
                                    Log.e("onConnectionInfo", listItem.getContent());
                                } catch (Exception e) {
                                    e.printStackTrace ();
                                } finally {
                                    if(socket.isConnected ()){
                                        try {
                                            socket.close ();
                                        } catch (IOException e) {
                                            e.printStackTrace ();
                                        }
                                    }
                                }
                            }
                        }).start();
                    }
                }
            }
        };
    }

    public void discoverPeers() {
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.e("onConnectionInfo", "开始发现对等节点");
            }

            @Override
            public void onFailure(int i) {
                Log.e("onConnectionInfo", "发现节点失败，原因是" + String.valueOf(i));
            }
        });
    }

    public void registerReceiver() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        broadcastReceiver = new WifiDirectBroadcastReceiver(manager, channel, activity, peerListListener, connectionInfoListener, this);
        activity.registerReceiver(broadcastReceiver, intentFilter);
    }

    public void unRegisterReceiver() {
        activity.unregisterReceiver(broadcastReceiver);
    }

    public void connect() {
        for (WifiP2pDevice wifiP2pDevice: list) {
            connect(wifiP2pDevice);
        }
        this.connect = true;
    }

    private void connect(WifiP2pDevice wifiP2pDevice) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = wifiP2pDevice.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.e("onConnectionInfo", "连接成功");
            }

            @Override
            public void onFailure(int i) {
                Log.e("onConnectionInfo", "连接失败，原因是" + String.valueOf(i));
            }
        });
    }

    private void removeGroup() {
        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.e("onConnectionInfo", "删除组成功");
            }

            @Override
            public void onFailure(int i) {
                Log.e("onConnectionInfo", "删除组失败，原因是" + String.valueOf(i));
            }
        });
    }
}
