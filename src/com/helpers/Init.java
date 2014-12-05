package com.helpers;

import java.io.InputStream;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;

import com.example.test.R;
import com.obj.CompareObjTitle;

import android.app.Application;
import android.content.Context;
import android.graphics.BitmapFactory;

public class Init extends Application {
	public void onCreate() {
		super.onCreate();
		try {
			Context context=getApplicationContext();
			// 读取JSON文件
			InputStream in = context.getResources().openRawResource(
					R.raw.community);
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			com.obj.STATIC_VAR.res = EncodingUtils.getString(buffer, "BIG5");
			in.close();
			com.obj.STATIC_VAR.ja = new JSONArray(com.obj.STATIC_VAR.res);
			// 读取本地图片
			in = context.getResources().openRawResource(R.drawable.ic_launcher);
			com.obj.STATIC_VAR.bitmap_default = BitmapFactory.decodeStream(in);
			in.close();
			// 将原始的JSON数据放入List_sort(未排序），List_sort中的元素由一个index和一个JSONObject构成，index代表的内容是time_created
			for (int i = 0; i < com.obj.STATIC_VAR.ja.length(); ++i) {
				CompareObjTitle tmp = new CompareObjTitle();
				tmp.setIndex(Integer.parseInt(com.obj.STATIC_VAR.ja
						.getJSONObject(i).getString("time_created").toString()));
				tmp.setJb(com.obj.STATIC_VAR.ja.getJSONObject(i));
				com.obj.STATIC_VAR.list_Sort.add(tmp);
			}
			// 对List_sort进行排序，得到按时间顺序排列的JSONOBJECT
			Helpers.bubbleSort(com.obj.STATIC_VAR.list_Sort);
			// 获取每个title下comment得时间顺序
			for (int i = 0; i < com.obj.STATIC_VAR.ja.length(); i++) {
				com.obj.STATIC_VAR.temp[0] = 0;
				com.obj.STATIC_VAR.temp[1] = 0;

				for (int j = 0; j < com.obj.STATIC_VAR.list_Sort.get(i).getJb()
						.getJSONArray("comments").length(); j++) {
					// temp[0]是比较值，比较replytime
					if (Integer.parseInt(com.obj.STATIC_VAR.list_Sort.get(i)
							.getJb().getJSONArray("comments").getJSONObject(j)
							.getString("time_created").toString()) > com.obj.STATIC_VAR.temp[0]) {
						com.obj.STATIC_VAR.temp[0] = Integer
								.parseInt(com.obj.STATIC_VAR.list_Sort.get(i)
										.getJb().getJSONArray("comments")
										.getJSONObject(j)
										.getString("time_created").toString());
						com.obj.STATIC_VAR.temp[1] = j; // temp[1]存放最后评论者的时间
					}
					// 得到以List_sort为序列的每个latest_reply_index
					com.obj.STATIC_VAR.lastest_reply_index[i] = com.obj.STATIC_VAR.temp[1];
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
