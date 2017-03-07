package com.example.asus_.studentlist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private List<Student> datalist;
    private Button add;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "7c6787e1cf575fc20765bed4d8f2cc96");
        setContentView(R.layout.activity_main);
        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                queryDataFromBmob();
            }
        }).start();


    }

    private void queryDataFromBmob() {
        BmobQuery<Student>bmobQuery=new BmobQuery<Student>();
        bmobQuery.findObjects(new FindListener<Student>() {
            @Override
            public void done(final List<Student> list, BmobException e) {
                datalist.clear();
               if (e==null){
                   datalist.addAll(list);
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           Toast.makeText(MainActivity.this, "查询成功，共有"+list.size()+"条数据", Toast.LENGTH_SHORT).show();
                           baseAdapter.notifyDataSetChanged();
                       }
                   });

               }else {
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           Toast.makeText(MainActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                       }
                   });

               }
            }
        });
    }

    private void initView() {
        datalist = new ArrayList<Student>();
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(baseAdapter);
        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }


    private BaseAdapter baseAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return datalist.size();
        }

        @Override
        public Object getItem(int position) {
            return datalist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            }
            final Student student = datalist.get(position);
            TextView txt_name = (TextView) convertView.findViewById(R.id.name);
            CircleImageView circleImageView = (CircleImageView) convertView.findViewById(R.id.image);
            txt_name.setText(student.getName());
            Glide.with(MainActivity.this).load(student.getHeadImg().getUrl()).into(circleImageView);
            Button btn_del= (Button)convertView.findViewById(R.id.del);
            student.getHeadImg().getFileUrl();
            btn_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "dd", Toast.LENGTH_SHORT).show();
                    student.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            queryDataFromBmob();
                        }
                    });

                }
            });

            return convertView;
        }
    };


    @Override
    protected void onResume() {
       queryDataFromBmob();
        super.onResume();
    }
}
