package com.example.graduationproject.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.graduationproject.R;
import com.example.graduationproject.adapter.MyRecyclerAdapter;
import com.example.graduationproject.bean.AgreeItemBean;
import com.example.graduationproject.bean.CommentItemBean;
import com.example.graduationproject.bean.DisagreeItemBean;
import com.example.graduationproject.bean.ListItemBean;
import com.example.graduationproject.bean.UserCreditBean;
import com.example.graduationproject.bean.UserInfoBean;
import com.example.graduationproject.component.FixBugLinearLayoutManager;
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
    RecyclerView recyclerView;
    MyRecyclerAdapter myRecyclerAdapter;
    LinearLayoutManager linearLayoutManager;
    FloatingActionButton floatingActionButton;
    List<ListItemBean> list = new ArrayList<>();
    WifiDirect wifiDirect;
    Handler handler = new Handler();
    Runnable runnable;
    SQLiteDatabase db;
    UserInfoBean userInfo;
    OnReceiveItemListener onReceiveItemListener = new OnReceiveItemListener() {
        @Override
        public void onReceiveItem(Object object) {
            ListItemBean listItem = null;
            int i = -1; //发生变化的信息项在list中的位置
            ListItemBean listItemBean = null; //找到发生评论或点赞等变化的文字信息项
            if (object instanceof ListItemBean) {
                listItem = (ListItemBean) object;
            } else if (object instanceof CommentItemBean) {
                CommentItemBean commentItem = (CommentItemBean) object;
                commentItem.save();
                listItemBean = DataSupport.where("hash = ?", commentItem.getResourceHash()).findFirst(ListItemBean.class);
                if (listItemBean != null) {
                    i = list.indexOf(listItemBean);
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
                for (ListItemBean lb : list) {
                    if (lb.getUserName().equals(commentItem.getCommentatorName())) {
                        lb.setUserCredit(commentItem.getCommentatorCredit());
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("run: ", "更新评论者数据");
                        myRecyclerAdapter.notifyItemRangeChanged(0, list.size());
                    }
                });
            } else if (object instanceof AgreeItemBean) {
                AgreeItemBean agreeItem = (AgreeItemBean) object;
                if (userInfo.getUserName().equals(agreeItem.getUserName())) {
                    userInfo.setCredit(userInfo.getCredit() + 1);
                    MyCache.updateCache(ListActivity.this, "user", userInfo);
                }
                agreeItem.save();
                listItemBean = DataSupport.where("hash = ?", agreeItem.getResourceHash()).findFirst(ListItemBean.class);
                if (listItemBean != null) {
                    i = list.indexOf(listItemBean);
                    listItemBean.setItemAgree(listItemBean.getItemAgree() + 1);
                    listItemBean.save();
                }
            } else if (object instanceof DisagreeItemBean) {
                DisagreeItemBean disagreeItem = (DisagreeItemBean) object;
                if (userInfo.getUserName().equals(disagreeItem.getUserName())) {
                    userInfo.setCredit(userInfo.getCredit() - 1);
                    MyCache.updateCache(ListActivity.this, "user", userInfo);
                }
                disagreeItem.save();
                listItemBean = DataSupport.where("hash = ?", disagreeItem.getResourceHash()).findFirst(ListItemBean.class);
                if (listItemBean != null) {
                    i = list.indexOf(listItemBean);
                    listItemBean.setItemDisagree(listItemBean.getItemDisagree() + 1);
                    listItemBean.save();
                }
            }
            if (i != -1) {
                list.set(i, listItemBean); //找到界面上需要更新的文字资源项
                final int j = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("run: ", "更新数据");
                        myRecyclerAdapter.notifyItemChanged(j);
                    }
                });
            }
            if (listItem != null) { //处理接收到文字资源项的情况
                list.add(0, listItem);
                UserCreditBean userCredit = DataSupport.where("userName = ?", listItem.getUserName()).findFirst(UserCreditBean.class);
                if (userCredit == null) {
                    userCredit = new UserCreditBean(listItem.getUserName(), listItem.getUserCredit());
                } else {
                    userCredit.setUserCredit(listItem.getUserCredit());
                }
                userCredit.save();
                for (ListItemBean lb : list) {
                    lb.setUserCredit(DataSupport.where("userName = ?", lb.getUserName()).findFirst(UserCreditBean.class).getUserCredit());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("run: ", "插入数据");
                        myRecyclerAdapter.notifyItemRangeChanged(0, list.size());
                    }
                });
                listItem.save();
            }
            //wifiDirect.unRegisterReceiver();
            //wifiDirect = WifiDirect.newInstance(ListActivity.this, onReceiveItemListener);
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
        floatingActionButton = findViewById(R.id.floating_add_button);
        new Thread(new Runnable() {
            @Override
            public void run() {
                db = LitePal.getDatabase();
                /*list.add(new ListItemBean("好好读几年书，高考去，考个正经学校。再嫌不系统就考出国去，考个斯坦福伯克利什么的，再系统不过了。\n" +
                        "趁年轻，还有时间走正路，不要急着抄捷径。目前编程对你来说就当个爱好玩一玩挺好。"));
                list.add(new ListItemBean("最近一直在思考怎么样快速掌握一门编程语言基础，究竟哪些是入门编程语言的最少必要知识。\n" +
                        "\n" +
                        "在重新学习了python, javascript之后，我发现掌握基础编程概念后，使用如下套路可快速入门新的编程语言"));
                list.add(new ListItemBean("学英语\n" +
                        "考托福\n" +
                        "考 SAT\n" +
                        "申请美国计算机前 50 的学校 / 加拿大前 10 的学校\n" +
                        "好好听课\n" +
                        "没了"));
                list.add(new ListItemBean("到现在题主应该还没进入大学吧。没有基础？当然啦，谁一开始都是没有基础的。初中时候，家里没电脑，学校里上微机课我就写QB。当然内容也不是什么高大上的，比如写个剪刀石头布啦、模拟一下游戏里5-20的攻击力是怎么弄出来的、自己再捣鼓一个加幸运的算法进去。高中时期比较忙，泛泛地了解了一下C，只停留在能打印个菱形的水平。\n" +
                        "\n" +
                        "作者：[已重置]\n" +
                        "链接：https://www.zhihu.com/question/28611887/answer/141188181\n" +
                        "来源：知乎\n" +
                        "著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。"));
                list.add(new ListItemBean("不管做什么事情，兴趣是最重要的，没有兴趣是坚持不下去的。\n" +
                        "根据自己的兴趣，选择适合自己的学习路径，掌握一定的学习方法。\n" +
                        "最后，坚持+坚持+坚持。"));
                list.add(new ListItemBean("到现在题主应该还没进入大学吧。没有基础？当然啦，谁一开始都是没有基础的。初中时候，家里没电脑，学校里上微机课我就写QB。当然内容也不是什么高大上的，比如写个剪刀石头布啦、模拟一下游戏里5-20的攻击力是怎么弄出来的、自己再捣鼓一个加幸运的算法进去。高中时期比较忙，泛泛地了解了一下C，只停留在能打印个菱形的水平。\n" +
                        "\n" +
                        "著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。"));
                list.add(new ListItemBean("学英语\n" +
                        "考托福\n" +
                        "考 SAT\n" +
                        "申请美国计算机前 50 的学校 / 加拿大前 10 的学校\n" +
                        "好好听课\n" +
                        "没了"));
                list.add(new ListItemBean("最近一直在思考怎么样快速掌握一门编程语言基础，究竟哪些是入门编程语言的最少必要知识。\n" +
                        "\n" +
                        "在重新学习了python, javascript之后，我发现掌握基础编程概念后，使用如下套路可快速入门新的编程语言"));*/
                list.addAll(0, DataSupport.order("id desc").limit(10).find(ListItemBean.class));
            }
        }).start();
        myRecyclerAdapter = new MyRecyclerAdapter(list, this); //每项的内容适配器
        linearLayoutManager = new FixBugLinearLayoutManager(this); //处理华为5.1手机RecyclerView遇到Inconsistency detected崩溃的情况
        recyclerView.setLayoutManager(linearLayoutManager);;
        recyclerView.setAdapter(myRecyclerAdapter);
        recyclerView.setNestedScrollingEnabled(false); //禁止嵌套滑动，防止NestedScrollView和RecyclerView滑动冲突不流畅
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
                nestedScrollView.scrollTo(0, 0);
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
}
