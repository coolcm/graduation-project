package com.example.graduationproject.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.graduationproject.R;

import java.util.Random;

public class UserActivity extends AppCompatActivity {  //用户个人信息界面

    private TextView agreeView;
    private TextView disagreeView;
    private TextView creditView;

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
        agreeView.setText(String.valueOf(new Random().nextInt(100)));
        disagreeView.setText(String.valueOf(new Random().nextInt(100)));
        creditView.setText(String.valueOf(new Random().nextInt(100)));
    }
}
