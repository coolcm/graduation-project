package com.example.graduationproject.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.graduationproject.R;
import com.example.graduationproject.bean.ListItemBean;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContentActivity extends AppCompatActivity implements View.OnClickListener{

    TextView textView;
    CircleImageView userImageView;
    TextView userNameView;
    TextView userCreditView;
    ListItemBean listItem;
    Button agreeButton;
    TextView agreeView;
    Button disagreeButton;
    TextView disagreeView;
    Button commentButton;
    TextView commentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("内容");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        textView = findViewById(R.id.content_text_view);
        userNameView = findViewById(R.id.content_user_name);
        userCreditView = findViewById(R.id.content_user_credit);
        userImageView = findViewById(R.id.content_user_image);
        agreeButton = findViewById(R.id.agree_button);
        disagreeButton = findViewById(R.id.disagree_button);
        commentButton = findViewById(R.id.comment_button);
        agreeView = findViewById(R.id.content_num_of_agree);
        disagreeView = findViewById(R.id.content_num_of_disagree);
        commentView = findViewById(R.id.content_num_of_comment);
        agreeButton.setOnClickListener(this);
        disagreeButton.setOnClickListener(this);
        commentButton.setOnClickListener(this);
        Intent intent = getIntent();
        listItem = (ListItemBean) intent.getSerializableExtra("content");
        if (listItem != null) {
            textView.setText(listItem.getContent());
            userNameView.setText(listItem.getUserName());
            userCreditView.setText(String.valueOf(listItem.getUserCredit()));
            agreeView.setText(String.valueOf(listItem.getItemAgree()));
            disagreeView.setText(String.valueOf(listItem.getItemDisagree()));
            commentView.setText(String.valueOf(listItem.getItemComment()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Button button;
        switch (view.getId()) {
            case R.id.agree_button:
                button = (Button) view;
                if (button.isActivated()) {
                    button.setActivated(false);
                    agreeView.setText(String.valueOf(Integer.valueOf(agreeView.getText().toString()) - 1));
                } else {
                    button.setActivated(true);
                    agreeView.setText(String.valueOf(Integer.valueOf(agreeView.getText().toString()) + 1));
                }
                break;
            case R.id.disagree_button:
                button = (Button) view;
                if (button.isActivated()) {
                    button.setActivated(false);
                    disagreeView.setText(String.valueOf(Integer.valueOf(disagreeView.getText().toString()) - 1));
                } else {
                    button.setActivated(true);
                    disagreeView.setText(String.valueOf(Integer.valueOf(disagreeView.getText().toString()) + 1));
                }
                break;
            case R.id.comment_button:
                button = (Button) view;
                if (button.isActivated()) {
                    button.setActivated(false);
                } else {
                    button.setActivated(true);
                }
                break;
        }
    }
}
