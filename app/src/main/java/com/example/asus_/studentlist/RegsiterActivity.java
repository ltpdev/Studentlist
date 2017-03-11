package com.example.asus_.studentlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegsiterActivity extends AppCompatActivity {
    private Button btnregsiter;
    private Button btncode;
    private EditText edtname;
    private EditText edtpwd;
    private EditText edtcode;
    private EditText edtnum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(RegsiterActivity.this, "7c6787e1cf575fc20765bed4d8f2cc96");
        setContentView(R.layout.activity_regsiter);
        initView();
    }

    private void initView() {
        edtname= (EditText) findViewById(R.id.edt_name);
        edtcode= (EditText) findViewById(R.id.edt_code);
        edtpwd= (EditText) findViewById(R.id.edt_password);
        edtnum= (EditText) findViewById(R.id.edt_num);
        btnregsiter= (Button) findViewById(R.id.btn_regsiter);
        btnregsiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone=edtnum.getText().toString().trim();
                String code=edtcode.getText().toString().trim();
                BmobSMS.verifySmsCode(phone, code, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null){
                            String name=edtname.getText().toString().trim();
                            String pwd=edtpwd.getText().toString().trim();
                            BmobUser user=new BmobUser();
                            user.setUsername(name);
                            user.setPassword(pwd);
                            user.setMobilePhoneNumber(phone);

                            user.signUp(new SaveListener<Object>() {
                                @Override
                                public void done(Object o, BmobException e) {
                                        finish();
                                }
                            });
                        }
                    }
                });
            }
        });
        btncode= (Button) findViewById(R.id.btn_code);
        btncode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String mobileNum=edtnum.getText().toString().trim();
                BmobSMS.requestSMSCode(mobileNum, "test", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e==null){
                            //查询成功
                        }
                    }
                });
            }
        });
    }
}
