package com.example.graduationproject.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.*;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.net.wifi.p2p.nsd.WifiP2pServiceRequest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.graduationproject.R;
import com.example.graduationproject.adapter.WifiP2pItemAdapter;
import com.example.graduationproject.bean.ListItemBean;
import com.example.graduationproject.broadcast.WifiDirectBroadcastReceiver;
import com.example.graduationproject.interfaces.OnDeviceClickListener;
import com.example.graduationproject.utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class UserActivity extends AppCompatActivity {  //用户个人信息界面

    private TextView agreeView;
    private TextView disagreeView;
    private TextView creditView;
    private RecyclerView recyclerView;

    private WifiP2pItemAdapter wifiP2pItemAdapter;
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private WifiP2pManager.PeerListListener peerListListener;
    private WifiP2pManager.ConnectionInfoListener connectionInfoListener;
    private IntentFilter intentFilter;
    private List<WifiP2pDevice> list = new ArrayList<>();
    private BroadcastReceiver broadcastReceiver;
    private HashMap<String, String> buddies = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("用户信息");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        agreeView = findViewById(R.id.user_num_of_agree);
        disagreeView = findViewById(R.id.user_num_of_disagree);
        creditView = findViewById(R.id.user_num_of_credit);
        recyclerView = findViewById(R.id.wifi_p2p_recycler_view);
        agreeView.setText(String.valueOf(new Random().nextInt(100)));
        disagreeView.setText(String.valueOf(new Random().nextInt(100)));
        creditView.setText(String.valueOf(new Random().nextInt(100)));

        /*manager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserActivity.this));
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
        peerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                list.clear();
                list.addAll(wifiP2pDeviceList.getDeviceList());
                for (WifiP2pDevice device: list) {
                    Log.e("onConnectionInfo", "发现对等设备" + device.deviceAddress);
                }
                wifiP2pItemAdapter = new WifiP2pItemAdapter(list);
                wifiP2pItemAdapter.setOnDeviceClickListener(new OnDeviceClickListener() {
                    @Override
                    public void onDeviceClick(int position) {
                        Log.e("onConnectionInfo", "点击设备");
                        connect(list.get(position));
                    }
                });
                recyclerView.setAdapter(wifiP2pItemAdapter);
            }
        };
        connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
            @Override
            public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
                final InetAddress inetAddress = wifiP2pInfo.groupOwnerAddress;
                Log.e("onConnectionInfo", "发现服务端或客户端" + inetAddress.getHostAddress());
                if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
                    Log.e("onConnectionInfo", "服务端");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String str = null;
                            try {
                                ServerSocket serverSocket = new ServerSocket(8999);
                                //等待客户端来连接
                                Socket client = serverSocket.accept ();

                                InputStream is = client.getInputStream ();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                int i = 0;
                                while((i = is.read ())!=-1){
                                    baos.write (i);
                                }
                                ListItemBean listItem = (ListItemBean) AppUtils.bytes2Object(baos.toByteArray());
                                serverSocket.close ();
                                Log.e("run: ", listItem.getContent());
                            } catch (IOException e) {
                                e.printStackTrace ();
                            }
                        }
                    }).start();
                } else if (wifiP2pInfo.groupFormed) {
                    Log.e("onConnectionInfo", "客户端");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Socket socket = new Socket ();
                            OutputStream os = null;
                            try {
                                socket.bind (null);
                                socket.connect (new InetSocketAddress(inetAddress.getHostAddress(), 8999),5000);
                                os = socket.getOutputStream ();
                                os.write (AppUtils.object2Bytes(new ListItemBean("hello, world")));
                            } catch (IOException e) {
                                e.printStackTrace ();
                            }finally {
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
        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        broadcastReceiver = new WifiDirectBroadcastReceiver(manager, channel, peerListListener, connectionInfoListener);
        registerReceiver(broadcastReceiver, intentFilter);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*unregisterReceiver(broadcastReceiver);
        manager.cancelConnect(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.e("onConnectionInfo", "取消连接成功");
            }

            @Override
            public void onFailure(int i) {
                Log.e("onConnectionInfo", "取消连接失败，原因是" + String.valueOf(i));
            }
        });
        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.e("onConnectionInfo", "移除组成功");
            }

            @Override
            public void onFailure(int i) {
                Log.e("onConnectionInfo", "移除组失败，原因是" + String.valueOf(i));
            }
        });
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
        });*/
    }

    private void startRegistration() {
        //  Create a string map containing information about your service.
        Map<String, String> record = new HashMap<>();
        record.put("listenport", String.valueOf(9999));
        record.put("buddyname", "John Doe" + (int) (Math.random() * 1000));
        record.put("available", "visible");

        // Service information.  Pass it an instance name, service type
        // _protocol._transportlayer , and the map containing
        // information other devices will want once they connect to this one.
        WifiP2pDnsSdServiceInfo serviceInfo =
                WifiP2pDnsSdServiceInfo.newInstance("_test", "_presence._tcp", record);

        // Add the local service, sending the service info, network channel,
        // and listener that will be used to indicate success or failure of
        // the request.
        manager.addLocalService(channel, serviceInfo, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Command successful! Code isn't necessarily needed here,
                // Unless you want to update the UI or add logging statements.
            }

            @Override
            public void onFailure(int arg0) {
                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
            }
        });
    }

    private void discoverService() {
       DnsSdTxtRecordListener txtListener = new DnsSdTxtRecordListener() {
            @Override
        /* Callback includes:
         * fullDomain: full domain name: e.g "printer._ipp._tcp.local."
         * record: TXT record dta as a map of key/value pairs.
         * device: The device running the advertised service.
         */
            public void onDnsSdTxtRecordAvailable(
                    String fullDomain, Map record, WifiP2pDevice device) {
                Log.e("discoverService", "DnsSdTxtRecord available -" + record.toString());
                buddies.put(device.deviceAddress, (String) record.get("buddyname"));
            }
        };
        DnsSdServiceResponseListener servListener = new DnsSdServiceResponseListener() {
            @Override
            public void onDnsSdServiceAvailable(String instanceName, String registrationType,
                                                WifiP2pDevice resourceType) {

                // Update the device name with the human-friendly version from
                // the DnsTxtRecord, assuming one arrived.
                resourceType.deviceName = buddies
                        .containsKey(resourceType.deviceAddress) ? buddies
                        .get(resourceType.deviceAddress) : resourceType.deviceName;

                list.add(resourceType);
                wifiP2pItemAdapter.notifyDataSetChanged();
                Log.e("discoverService", "onBonjourServiceAvailable " + instanceName);
            }
        };
        manager.setDnsSdResponseListeners(channel, servListener, txtListener);
        WifiP2pServiceRequest serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        manager.addServiceRequest(channel, serviceRequest, new ActionListener() {
            @Override
            public void onSuccess() {
                // Success!
            }

            @Override
            public void onFailure(int code) {
                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
            }
        });
        manager.discoverServices(channel, new ActionListener() {
            @Override
            public void onSuccess() {
                // Success!
            }

            @Override
            public void onFailure(int code) {
                Log.e("discoverService", "P2P isn't supported on this device.");
            }
        });
    }
}
