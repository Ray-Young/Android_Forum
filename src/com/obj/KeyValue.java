package com.obj;

import java.util.ArrayList;

import org.json.JSONArray;

import android.graphics.Bitmap;
//keyvalue中存放一些全局变量
//res为读取JSON时的Buffer
//JA为初次将Buffer转换到内存中得变量
//bitmap_default为默认头像，在图片未读出的情况下使用
//list_Sort为排序list，是讲JSONOBJECT以title的时间顺序排序并存放的list
//temp存放的是在比较comments时间时的缓存数据
//latest_reply_index是存放comments最后评论者所在index位置的数组
public class KeyValue {
	public static String res;
	public static JSONArray ja;
	public static Bitmap bitmap_default;
	public static ArrayList<CompareObjTitle> list_Sort = new ArrayList<CompareObjTitle>();
	public static int[] temp = new int[10];
	public static int[] lastest_reply_index = new int[50];

}
