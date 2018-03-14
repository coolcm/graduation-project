package com.example.graduationproject.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.bean.ListItemBean;

import jp.wasabeef.richeditor.RichEditor;

public class EditActivity extends AppCompatActivity {

    RichEditor richEditor;
    ListItemBean listItemBean = new ListItemBean();
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        floatingActionButton = findViewById(R.id.floating_ok_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(listItemBean.getContent())) {
                    Toast.makeText(EditActivity.this, "发送内容不能为空~", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(EditActivity.this, ListActivity.class);
                intent.putExtra("listItemBean", listItemBean);
                setResult(1, intent);
                finish();
            }
        });
        richEditor = findViewById(R.id.rich_editor);
        richEditor.setEditorHeight(200);
        richEditor.setEditorFontSize(15);
        richEditor.setPadding(10, 10, 10, 10);
        richEditor.setPlaceholder("说说有什么新鲜事吧~");
        richEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                listItemBean.setContent(text);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditActivity.this, ListActivity.class);
        setResult(0, intent);
        finish();
    }
}
