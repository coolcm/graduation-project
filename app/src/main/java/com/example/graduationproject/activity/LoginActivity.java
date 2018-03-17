package com.example.graduationproject.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.bean.UserInfoBean;
import com.example.graduationproject.utils.MyCache;

public class LoginActivity extends AppCompatActivity { //登录界面

    Button loginButton;
    EditText userNameView;
    EditText userPasswordView;
    TextView newLoginView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("登录");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ok);
        loginButton = findViewById(R.id.login_button);
        userNameView = findViewById(R.id.user_name);
        userPasswordView = findViewById(R.id.user_password);
        newLoginView = findViewById(R.id.new_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.valueOf(userNameView.getText());
                String password = String.valueOf(userPasswordView.getText());
                UserInfoBean userInfo = (UserInfoBean) MyCache.getCache(LoginActivity.this, "user");
                if (userInfo != null && name.equals(userInfo.getUserName()) && password.equals(userInfo.getUserPassword())) { //通过缓存的信息验证用户名与密码
                    Intent intent = new Intent(LoginActivity.this, ListActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        newLoginView.setOnClickListener(new View.OnClickListener() { //启动注册界面
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        UserInfoBean userInfo = (UserInfoBean) MyCache.getCache(LoginActivity.this, "user");
        if (userInfo != null) {
            userNameView.setText(userInfo.getUserName());
            userPasswordView.setText(userInfo.getUserPassword()); //根据缓存信息自动填写用户名和密码
        }
        super.onResume();
    }
}
