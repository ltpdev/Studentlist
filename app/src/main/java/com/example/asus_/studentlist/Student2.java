package com.example.asus_.studentlist;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by asus- on 2017/3/4.
 */

public class Student2 extends BmobObject{
private String name;
    private BmobFile headImg;

    public BmobFile getHeadImg() {
        return headImg;
    }

    public void setHeadImg(BmobFile headImg) {
        this.headImg = headImg;
    }
    /* private String imgPath;*/

    public Student2(String name, BmobFile headImg) {
        this.name = name;
        this.headImg = headImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

 /*   public String getImgPath() {
        return imgPath;
    }*/

   /* public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }*/
}
