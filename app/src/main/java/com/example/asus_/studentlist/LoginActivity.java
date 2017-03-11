package com.example.asus_.studentlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {
private Button btnlogin;
    private EditText edtname;
    private EditText edtpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        btnlogin= (Button) findViewById(R.id.btn_login);
        edtname= (EditText) findViewById(R.id.edt_name);
        edtpwd= (EditText) findViewById(R.id.edt_pwd);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser user=new BmobUser();
                user.setUsername(edtname.getText().toString());
                user.setPassword(edtpwd.getText().toString());
                user.login(new SaveListener<Object>() {
                    @Override
                    public void done(Object o, BmobException e) {
                       if (e==null){
                           finish();
                       }

                    }
                });
            }
        });
    }
}
