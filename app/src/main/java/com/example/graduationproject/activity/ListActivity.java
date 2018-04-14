package com.example.graduationproject.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.adapter.MyRecyclerAdapter;
import com.example.graduationproject.bean.AgreeItemBean;
import com.example.graduationproject.bean.BlockBean;
import com.example.graduationproject.bean.BlockChainBean;
import com.example.graduationproject.bean.CommentItemBean;
import com.example.graduationproject.bean.DisagreeItemBean;
import com.example.graduationproject.bean.ListItemBean;
import com.example.graduationproject.bean.UserCreditBean;
import com.example.graduationproject.bean.UserInfoBean;
import com.example.graduationproject.component.FixBugLinearLayoutManager;
import com.example.graduationproject.component.MySwipeRefreshLayout;
import com.example.graduationproject.interfaces.OnReceiveItemListener;
import com.example.graduationproject.utils.AppUtils;
import com.example.graduationproject.utils.Client;
import com.example.graduationproject.utils.MyCache;
import com.example.graduationproject.utils.WifiDirect;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity { //主界面，对每段资源的内容进行列表显示

    NestedScrollView nestedScrollView;
    MySwipeRefreshLayout mySwipeRefreshLayout;
    RecyclerView recyclerView;
    MyRecyclerAdapter myRecyclerAdapter;
    LinearLayoutManager linearLayoutManager;
    FloatingActionButton floatingActionButton;
    List<ListItemBean> list = new ArrayList<>();
    private int startId = 0;
    WifiDirect wifiDirect;
    Handler handler = new Handler();
    Runnable runnable;
    Runnable blockChainRunnable;
    SQLiteDatabase db;
    UserInfoBean userInfo;
    BlockBean blockBean = null; //收集接收到的各种文字，点赞，反对，评论信息，用于区块广播
    int currentId = 1; //记录当前主链最新区块id
    OnReceiveItemListener onReceiveItemListener = new OnReceiveItemListener() {
        @Override
        public void onReceiveItem(Object object) {
            if (blockBean == null) {
                BlockChainBean blockChain = DataSupport.findLast(BlockChainBean.class);
                blockBean = new BlockBean(blockChain.getId(), blockChain.getHash());
            }
            if (object instanceof ListItemBean) {
                ListItemBean listItem = (ListItemBean) object;
                blockBean.addResourceItem(listItem);
                if (listItem.getUserName().equals(userInfo.getUserName())) { //即时更新自己发送的文字资源信息
                    list.add(0, listItem);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("run: ", "插入数据");
                            myRecyclerAdapter.notifyItemRangeChanged(0, list.size());
                            nestedScrollView.scrollTo(0, 0);
                        }
                    });
                }
            } else if (object instanceof CommentItemBean) {
                blockBean.addCommentItem((CommentItemBean) object);
            } else if (object instanceof AgreeItemBean) {
                blockBean.addAgreeItem((AgreeItemBean) object);
            } else if (object instanceof DisagreeItemBean) {
                blockBean.addDisagreeItemBean((DisagreeItemBean) object);
            } else if (object instanceof BlockBean) {
                BlockBean blockBean = (BlockBean) object;
                onReceiveBlockChainBean(blockBean);
                if (blockBean.getId() == currentId) { //同步区块完成后刷新页面，普通更新不会触发该逻辑
                    list.clear();
                    list.addAll(0, DataSupport.order("id desc").limit(10).find(ListItemBean.class));
                    startId = 0;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myRecyclerAdapter.notifyItemRangeChanged(0, list.size());
                            mySwipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
            //wifiDirect.unRegisterReceiver();
            //wifiDirect = WifiDirect.newInstance(ListActivity.this, onReceiveItemListener);
        }

        @Override
        public void onSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ListActivity.this, "联网成功", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onFailure() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ListActivity.this, "联网失败，请检查网络设置", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
    Client client = Client.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("信乎");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nestedScrollView = findViewById(R.id.scroll_view);
        recyclerView = findViewById(R.id.recycler_view);
        mySwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        floatingActionButton = findViewById(R.id.floating_add_button);
        new Thread(new Runnable() {
            @Override
            public void run() {
                db = LitePal.getDatabase();
                list.addAll(0, DataSupport.order("id desc").limit(10).find(ListItemBean.class));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myRecyclerAdapter.notifyItemRangeChanged(0, list.size());
                    }
                });
            }
        }).start();
        myRecyclerAdapter = new MyRecyclerAdapter(list, this); //每项的内容适配器
        linearLayoutManager = new FixBugLinearLayoutManager(this); //处理华为5.1手机RecyclerView遇到Inconsistency detected崩溃的情况
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myRecyclerAdapter);
        recyclerView.setNestedScrollingEnabled(false); //禁止嵌套滑动，防止NestedScrollView和RecyclerView滑动冲突不流畅
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final int id = DataSupport.findLast(BlockChainBean.class).getId();
                if (id < currentId - 1) {
                    Toast.makeText(ListActivity.this, "正在更新区块信息", Toast.LENGTH_SHORT).show();
                    Log.e("请求ID为*之后的区块", String.valueOf(id));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            client.requestBlockChain(id);
                        }
                    }).start();
                } else {
                    list.clear();
                    list.addAll(0, DataSupport.order("id desc").limit(10).find(ListItemBean.class));
                    startId = 0;
                    myRecyclerAdapter.notifyItemRangeChanged(0, list.size());
                    mySwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        mySwipeRefreshLayout.setOnLoadMoreListener(new MySwipeRefreshLayout.OnLoadMoreListener() {
            @Override
            public void onLoad() {
                startId += 10;
                mySwipeRefreshLayout.setProgressViewEndTarget(false, 900);
                mySwipeRefreshLayout.setRefreshing(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (DataSupport.count(ListItemBean.class) > startId) {
                            list.addAll(startId, DataSupport.order("id desc").offset(startId).limit(10).find(ListItemBean.class));
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    myRecyclerAdapter.notifyItemRangeChanged(startId, list.size() - startId);
                                    mySwipeRefreshLayout.setRefreshing(false);
                                    mySwipeRefreshLayout.setLoadingMore(false);
                                    mySwipeRefreshLayout.setProgressViewEndTarget(false, (int) (64 * getResources().getDisplayMetrics().density));
                                }
                            }, 500);
                        } else {
                            mySwipeRefreshLayout.setRefreshing(false);
                            mySwipeRefreshLayout.setLoadingMore(false);
                            mySwipeRefreshLayout.setProgressViewEndTarget(false, (int) (64 * getResources().getDisplayMetrics().density));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ListActivity.this, "已经到底啦~", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
        mySwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.translucent);
        mySwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_light);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {  //启动新增文字资源信息界面
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, EditActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        //wifiDirect = WifiDirect.newInstance(this, onReceiveItemListener);
        userInfo = (UserInfoBean) MyCache.getCache(this, "user");
        client.setOnReceiveItemListener(onReceiveItemListener);
        runnable = new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        client.getLocalAddress();
                        client.getPeerAddress();
                    }
                }).start();
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(runnable, 2000);
        blockChainRunnable = new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (blockBean != null && userInfo.getUserName().equals("admin")) { //暂定只有管理员可广播区块
                            Log.e("run: ", "广播区块");
                            blockBean.save();
                            client.sendMessage(AppUtils.object2Bytes(blockBean));
                            blockBean = null;
                        }
                    }
                }).start();
                handler.postDelayed(this, 10000);
            }
        };
        handler.postDelayed(blockChainRunnable, 10000); //10秒更新一次区块信息
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.sendLogoutMessage();
                Client.closeClient(); //防止再次开启应用时该client尚未被释放
            }
        }).start();
        /*if (wifiDirect != null) {
            wifiDirect.unRegisterReceiver();
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 0: break;
            case 1:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ListItemBean listItem = (ListItemBean) data.getSerializableExtra("listItemBean");
                        Log.e("run: ", String.valueOf(listItem.getHash()));
                        client.sendMessage(AppUtils.object2Bytes(listItem));
                        //wifiDirect.setListItem(listItem);
                        //wifiDirect.connect();
                    }
                }).start();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.my_info: //启动个人信息界面
                Intent intent = new Intent(this, UserActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //解析区块信息
    private void onReceiveBlockChainBean(BlockBean blockBean) {
        Log.e("onReceiveBlock: ", blockBean.getPrevHash());
        if (DataSupport.where("prevHash = ?", blockBean.getPrevHash()).findFirst(BlockChainBean.class) != null) {
            Log.e("onReceiveBlockChain", "存在了");
            this.blockBean = null; //接收后将区块置为空，防止区块重新发送
            return;
        } else {
            int id = DataSupport.findLast(BlockChainBean.class).getId();
            if (id < blockBean.getId() - 1) {
                Log.e("onReceiveBlockChainBean","区块信息不匹配，请先更新区块");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ListActivity.this, "信息不匹配，请先下拉刷新", Toast.LENGTH_SHORT).show();
                    }
                });
                currentId = blockBean.getId(); //记录当前主链最新区块id
                this.blockBean = null; //接收后将区块置为空，防止区块重新发送
                return;
            }
        }
        BlockChainBean blockChainBean = new BlockChainBean(blockBean.getTimeStamp(), blockBean.getPrevHash(), blockBean.getHash(), AppUtils.object2Bytes(blockBean));
        blockChainBean.save();
        List<ListItemBean> resourceList = blockBean.getResourceList();
        List<CommentItemBean> commentList = blockBean.getCommentList();
        List<AgreeItemBean> agreeList = blockBean.getAgreeList();
        List<DisagreeItemBean> disagreeList = blockBean.getDisagreeList();
        for (ListItemBean listItem: resourceList) {
            listItem.save();
            UserCreditBean userCredit = DataSupport.where("userName = ?", listItem.getUserName()).findFirst(UserCreditBean.class);
            if (userCredit == null) {
                userCredit = new UserCreditBean(listItem.getUserName(), listItem.getUserCredit());
            } else {
                userCredit.setUserCredit(listItem.getUserCredit());
            }
            userCredit.save();
        }
        for (CommentItemBean commentItem: commentList) {
            commentItem.save();
            ListItemBean listItemBean = DataSupport.where("hash = ?", commentItem.getResourceHash()).findFirst(ListItemBean.class);
            if (listItemBean != null) {
                listItemBean.setItemComment(listItemBean.getItemComment() + 1);
                listItemBean.save();
            }
            UserCreditBean userCredit = DataSupport.where("userName = ?", commentItem.getCommentatorName()).findFirst(UserCreditBean.class);
            if (userCredit == null) {
                userCredit = new UserCreditBean(commentItem.getCommentatorName(), commentItem.getCommentatorCredit());
            } else {
                userCredit.setUserCredit(commentItem.getCommentatorCredit());
            }
            userCredit.save();
        }
        for (AgreeItemBean agreeItem: agreeList) {
            agreeItem.save();
            UserCreditBean userCredit = DataSupport.where("userName = ?", agreeItem.getUserName()).findFirst(UserCreditBean.class);
            if (userCredit == null) {
                userCredit = new UserCreditBean(agreeItem.getUserName(), agreeItem.getUserCredit() + 1);
            } else {
                userCredit.setUserCredit(userCredit.getUserCredit() + 1);
            }
            userCredit.save();
            ListItemBean listItemBean = DataSupport.where("hash = ?", agreeItem.getResourceHash()).findFirst(ListItemBean.class);
            if (listItemBean != null) {
                listItemBean.setItemAgree(listItemBean.getItemAgree() + 1);
                listItemBean.save();
            }
        }
        for (DisagreeItemBean disagreeItem: disagreeList) {
            disagreeItem.save();
            UserCreditBean userCredit = DataSupport.where("userName = ?", disagreeItem.getUserName()).findFirst(UserCreditBean.class);
            if (userCredit == null) {
                userCredit = new UserCreditBean(disagreeItem.getUserName(), disagreeItem.getUserCredit() - 1);
            } else {
                userCredit.setUserCredit(userCredit.getUserCredit() - 1);
            }
            userCredit.save();
            ListItemBean listItemBean = DataSupport.where("hash = ?", disagreeItem.getResourceHash()).findFirst(ListItemBean.class);
            if (listItemBean != null) {
                listItemBean.setItemDisagree(listItemBean.getItemDisagree() + 1);
                listItemBean.save();
            }
        }
        this.blockBean = null; //接收后将区块置为空，防止区块重新发送
    }
}
