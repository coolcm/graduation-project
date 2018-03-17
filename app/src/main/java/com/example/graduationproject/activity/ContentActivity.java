package com.example.graduationproject.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.graduationproject.R;
import com.example.graduationproject.adapter.CommentItemAdapter;
import com.example.graduationproject.bean.CommentItemBean;
import com.example.graduationproject.bean.ListItemBean;
import com.example.graduationproject.bean.UserInfoBean;
import com.example.graduationproject.utils.MyCache;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContentActivity extends AppCompatActivity implements View.OnClickListener{

    List<CommentItemBean> list = new ArrayList<>();
    CommentItemAdapter commentItemAdapter;
    LinearLayoutManager linearLayoutManager;
    ScrollView scrollView;
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
    RecyclerView commentRecyclerView;
    EditText commentEditText;
    Button sendCommentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("内容");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        scrollView = findViewById(R.id.scroll_view);
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
        commentRecyclerView = findViewById(R.id.comment_recycler_view);
        commentEditText = findViewById(R.id.comment_edit_text);
        sendCommentButton = findViewById(R.id.send_comment_button);
        agreeButton.setOnClickListener(this);
        disagreeButton.setOnClickListener(this);
        commentButton.setOnClickListener(this);
        sendCommentButton.setOnClickListener(this);
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
        linearLayoutManager = new LinearLayoutManager(this);
        commentItemAdapter = new CommentItemAdapter(list);
        commentRecyclerView.setLayoutManager(linearLayoutManager);
        commentRecyclerView.setAdapter(commentItemAdapter);
        sendCommentButton.setActivated(false);
        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence.toString())) {
                    sendCommentButton.setActivated(false);
                } else {
                    sendCommentButton.setActivated(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
        InputMethodManager inputMethodManager;
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
                commentEditText.requestFocus();
                inputMethodManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                break;
            case R.id.send_comment_button:
                button = (Button) view;
                if (!button.isActivated()) {
                    break;
                }
                String content = commentEditText.getText().toString();
                UserInfoBean userInfo = (UserInfoBean) MyCache.getCache(this, "user");
                if (userInfo != null) {
                    CommentItemBean commentItem = new CommentItemBean(content, userInfo.getUserName(), userInfo.getCredit(), listItem.getUserName());
                    list.add(commentItem);
                    commentItemAdapter.notifyItemInserted(list.size() - 1);
                    linearLayoutManager.scrollToPositionWithOffset(list.size() - 1, 0);
                    commentEditText.setText(null);
                    commentEditText.clearFocus();
                    inputMethodManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager != null) {
                        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
        }
    }
}
