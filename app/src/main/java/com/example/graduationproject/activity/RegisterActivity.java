package com.example.graduationproject.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graduationproject.R;
import com.example.graduationproject.bean.BlockBean;
import com.example.graduationproject.bean.BlockChainBean;
import com.example.graduationproject.bean.UserCreditBean;
import com.example.graduationproject.bean.UserInfoBean;
import com.example.graduationproject.component.PhotoDialogFragment;
import com.example.graduationproject.utils.AppUtils;
import com.example.graduationproject.utils.MyCache;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity implements PhotoDialogFragment.CallBack{ //注册界面

    EditText newUserView;
    EditText newPasswordView;
    EditText validatePasswordView;
    Button registerButton;
    UserInfoBean userInfo = new UserInfoBean();

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
                InputMethodManager inputMethodManager = (InputMethodManager)RegisterActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) { //关闭输入法界面
                    inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                String name = String.valueOf(newUserView.getText());
                String password = String.valueOf(newPasswordView.getText());
                String validatePassword = String.valueOf(validatePasswordView.getText());
                if (name.length() > 0 && password.length() > 0 && validatePassword.length() > 0 && password.equals(validatePassword)) {
                    userInfo.setUserName(name);
                    userInfo.setUserPassword(password);
                    userInfo.setCredit(new Random().nextInt(9000) + 1000);
                    if (MyCache.isCacheExist(RegisterActivity.this, "user")) {
                        Toast.makeText(RegisterActivity.this, "用户注册失败,一个终端只能注册一个账号", Toast.LENGTH_SHORT).show();
                    } else {
                        PhotoDialogFragment photoDialogFragment = new PhotoDialogFragment();
                        photoDialogFragment.setCancelable(false);  //设置对话框不可取消
                        photoDialogFragment.show(getSupportFragmentManager(), "photoDialogFragment");
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "用户注册失败，请输入正确的用户名或密码", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onSelect(int resourceId) {
        userInfo.setHeadPhotoId(resourceId);
        MyCache.setCache(RegisterActivity.this, "user", userInfo);
        new UserCreditBean(userInfo.getUserName(), userInfo.getCredit(), userInfo.getHeadPhotoId()).save();
        Toast.makeText(RegisterActivity.this, "用户注册成功", Toast.LENGTH_SHORT).show();
        BlockBean blockBean = new BlockBean(1, AppUtils.getSHA256Str("创世区块")); //添加创世区块结构
        blockBean.save();
        BlockChainBean blockChainBean = new BlockChainBean(blockBean.getTimeStamp(), blockBean.getPrevHash(), blockBean.getHash(), AppUtils.object2Bytes(blockBean));
        blockChainBean.save();
        finish();
    }
}
