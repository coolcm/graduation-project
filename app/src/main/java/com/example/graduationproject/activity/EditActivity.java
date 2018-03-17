package com.example.graduationproject.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.bean.ListItemBean;
import com.example.graduationproject.bean.UserInfoBean;
import com.example.graduationproject.utils.MyCache;

public class EditActivity extends AppCompatActivity { //编辑想发送的内容的界面

    EditText editText;
    ListItemBean listItemBean = new ListItemBean();
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("新建");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        floatingActionButton = findViewById(R.id.floating_ok_button);
        editText = findViewById(R.id.edit_text);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(String.valueOf(editText.getText()))) {
                    Toast.makeText(EditActivity.this, "发送内容不能为空~", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserInfoBean userInfo = (UserInfoBean) MyCache.getCache(EditActivity.this, "user");
                if (userInfo == null) {
                    return;
                }
                listItemBean = new ListItemBean(String.valueOf(editText.getText()), userInfo.getUserName(), userInfo.getCredit());
                Intent intent = new Intent(EditActivity.this, ListActivity.class);
                intent.putExtra("listItemBean", listItemBean);
                setResult(1, intent); //返回发送的文字内容信息给上一个activity
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditActivity.this, ListActivity.class);
        setResult(0, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(EditActivity.this, ListActivity.class);
                setResult(0, intent);
                finish();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
