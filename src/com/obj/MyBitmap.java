package com.obj;

import android.graphics.Bitmap;

//MyBitmap是在处理下载头像时所用的对象
//bitmap为头像数据
//index为头像所对应的位置信息，因为DownloadinBackground的下载结果有先后，所以设置这个变量来寻路
public class MyBitmap {
	private Bitmap bitmap;
	private int index;

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
