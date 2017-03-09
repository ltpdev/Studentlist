package com.example.asus_.studentlist;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import static android.R.attr.bitmap;
import static android.R.attr.commitIcon;

public class AddActivity extends AppCompatActivity {
    private ImageView img;
    private EditText name;
    private Button save;
    private String imgpath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bmob.initialize(AddActivity.this, "371da77d17aba0ef576941cfb7da1407");
        setContentView(R.layout.activity_add);
        initView();
    }

    private void initView() {
        img = (ImageView) findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AddActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    openAlbum();
                }


            }
        });
        name = (EditText) findViewById(R.id.edt_name);
        save = (Button) findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* final String namestr = name.getText().toString();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
                savebitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] appicon = baos.toByteArray();
                Log.d("dd",appicon.toString());*/

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final BmobFile bmobFile = new BmobFile(new File(imgpath));
                        bmobFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Student s = new Student();
                                    s.setName(name.getText().toString());
                                            s.setHeadImg(bmobFile);
                                            s.save(new SaveListener<String>() {
                                                @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        finish();
                                                        Toast.makeText(AddActivity.this, "yes", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            } else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(AddActivity.this, "no", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    });

                                }
                            }
                        });

                    }
                }).start();


            }
        });
    }

    private void openAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1002);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* if (requestCode == 1002) {
            Uri imgUri = data.getData();
            Intent intent = new Intent();
            intent.setAction("com.android.camera.action.CROP");
            intent.setDataAndType(imgUri, "image*//*");
            intent.putExtra("crop", true);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, 1001);

        }

        if (requestCode == 1001) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = bundle.getParcelable("data");
            savebitmap=bitmap;

            img.setImageBitmap(bitmap);

        }*/

        if (requestCode == 1002) {

                //判断系统版本号
                if (Build.VERSION.SDK_INT >= 19) {
                    //4.4版本及以上获取路径的方法
                    handleImageOnKitKat(data);
                } else {
                    //4.4版本以下获取路径的方法
                    handleImageBeforeKitKat(data);
                }



        }

    }
@TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }

        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);

        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        imgpath = imagePath;
        displayImage(imagePath);
    //已修改过 烦烦烦

    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        imgpath = imagePath;
        displayImage(imagePath);

    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            img.setImageBitmap(bitmap);
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /*private void saveDataToServer(String namestr, final byte[] bitmap) {
        String url = "http://192.168.191.4:4040/Student/add.php?name=" + namestr + "&img=" + bitmap;
        Request request = new Request.Builder().url(url).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();

                    }
                });

            }
        });
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }
               break ;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
