package com.example.graduationproject.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.bean.UserInfoBean;
import com.example.graduationproject.utils.MyCache;

public class RegisterActivity extends AppCompatActivity {

    EditText newUserView;
    EditText newPasswordView;
    EditText validatePasswordView;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("注册");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ok);
        newUserView = findViewById(R.id.new_user_name);
        newPasswordView = findViewById(R.id.new_user_password);
        validatePasswordView = findViewById(R.id.validate_user_password);
        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.valueOf(newUserView.getText());
                String password = String.valueOf(newPasswordView.getText());
                String validatePassword = String.valueOf(validatePasswordView.getText());
                if (name.length() > 0 && password.length() > 0 && validatePassword.length() > 0 && password.equals(validatePassword)) {
                    UserInfoBean userInfo = new UserInfoBean();
                    userInfo.setUserName(name);
                    userInfo.setUserPassword(password);
                    if (MyCache.isCacheExist(RegisterActivity.this, "user")) {
                        Toast.makeText(RegisterActivity.this, "用户注册失败,一个终端只能注册一个账号", Toast.LENGTH_SHORT).show();
                    } else {
                        MyCache.setCache(RegisterActivity.this, "user", userInfo);
                        Toast.makeText(RegisterActivity.this, "用户注册成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "用户注册失败，请输入正确的用户名或密码", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
