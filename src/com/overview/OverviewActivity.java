package com.overview;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PublicKey;
import java.util.ArrayList;
import org.json.JSONException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.test.R;
import com.helpers.Helpers;
import com.obj.DetailContent;
import com.obj.MyBitmap;
//OverviewActivity为第一个界面，包括
//标签 (比如：My Journey to Glow)
//最后评论者的first name
//最后评论者的头像（没有头像的话，可以不显示）
//标题
//贴子内容的第一行
//评论的数量
//通过ListView来呈现主要内容

public class OverviewActivity extends ActionBarActivity {
	private ListView listView;
	private MyAdapter adapter;
	private ArrayList<ResultObj> list = new ArrayList<ResultObj>();
	private DownloadTask downloadTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_view);
		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onDestroy();
			}
		});

		// 调用init来得到初始数据
		// Helpers.init(getApplicationContext());
		try {
			for (int i = 0; i < com.obj.STATIC_VAR.ja.length(); i++) {
				// 对图片进行下载
				// 传入双参数，一个是图片URL，一个是对应的寻路信息
				downloadTask = new DownloadTask();
				downloadTask.execute(
						com.obj.STATIC_VAR.list_Sort.get(i).getJb().getJSONObject("author").getString("profile_image"),
						Integer.toString(i));
			}
			// 填充界面
			listView = (ListView) findViewById(R.id.listView);
			adapter = new MyAdapter(getApplicationContext());
			listView.setAdapter(adapter);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// downloadTask.cancel(true);
	}

	protected void onPause() {

	}

	// 因为图片内容较大，通过集成AsyncTask激活多线程下载，避免卡死主线程
	private class DownloadTask extends AsyncTask<String, Integer, MyBitmap> {
		@Override
		protected MyBitmap doInBackground(String... bitmapUrl) {
			// bitmapUrl[0]为图片URL
			// bitmapUrl[1]为寻路信息
			URL imageUrl;
			MyBitmap mp = new MyBitmap();
			mp.setIndex(Integer.parseInt(bitmapUrl[1]));
			Bitmap bitmap = null;
			if (bitmapUrl[0] != null) {
				try {
					// 对存在URL内容的图片进行读取和解码
					imageUrl = new URL(bitmapUrl[0]);
					HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
					InputStream inputStream = conn.getInputStream();
					bitmap = BitmapFactory.decodeStream(inputStream);
					mp.setBitmap(bitmap);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return mp;
			} else {
				// 若URL为空，则使用默认图片
				mp.setBitmap(com.obj.STATIC_VAR.bitmap_default);
				mp.setIndex(Integer.parseInt(bitmapUrl[1]));
				return mp;
			}

		}

		@Override
		protected void onPostExecute(MyBitmap result) {
			// 前台执行
			super.onPostExecute(result);
			// 填充图片
			list.get(result.getIndex()).bitmap = result.getBitmap();
			// 通知变化
			adapter.notifyDataSetChanged();
		}
	}

	// 对OverviewActivity进行数据填充的对象的原型
	public class ResultObj {
		public String tag_name;
		public Bitmap bitmap;
		public String last_name;
		public String title;
		public String content;
		public String Response_count;
		public int index;
	}

	// 为了使用ListView,编写MyAdapter，对数据进行绑定
	public class MyAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public MyAdapter(Context context) throws JSONException {
			// 初始化infalter,将OverviewActivity的context信息注入
			inflater = LayoutInflater.from(context);
			for (int i = 0; i < com.obj.STATIC_VAR.ja.length(); i++) {
				// 将要显示的数据传入Myadapter的媒介 ResultObj a = new ResultObj();
				ResultObj a = new ResultObj();
				a.tag_name = com.obj.STATIC_VAR.list_Sort.get(i).getJb().getString("tag").toString();
				a.last_name = com.obj.STATIC_VAR.list_Sort.get(i).getJb().getJSONArray("comments")
						.getJSONObject(com.obj.STATIC_VAR.lastest_reply_index[i]).getJSONObject("author")
						.getString("first_name");
				a.bitmap = com.obj.STATIC_VAR.bitmap_default;
				a.title = com.obj.STATIC_VAR.list_Sort.get(i).getJb().getString("title").toString();
				a.content = com.obj.STATIC_VAR.list_Sort.get(i).getJb().getString("content").toString();
				a.Response_count = Integer
						.toString(com.obj.STATIC_VAR.list_Sort.get(i).getJb().getJSONArray("comments").length());
				a.index = i;
				list.add(a);
			}
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		// 绑定，显示数据
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_list, null);
			}
			// 讲前台的ID与后台参数进行绑定
			ResultObj tmp = (ResultObj) getItem(position);
			TextView tagName = (TextView) convertView.findViewById(R.id.tagName);
			TextView lastName = (TextView) convertView.findViewById(R.id.lastName);

			ImageView imgView = (ImageView) convertView.findViewById(R.id.imgView);
			Drawable drawable = new BitmapDrawable(getResources(), tmp.bitmap);
			// 因为点击Title要进行跳转，设置OnlickListener并实例化
			TextView title = (TextView) convertView.findViewById(R.id.title);
			title.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// 通过DetailContent数据类型定义要传递的数据
					DetailContent data = new DetailContent();
					Intent intent = new Intent(OverviewActivity.this, DetailActivity.class);
					data.setPos(position);
					Bundle bundle = new Bundle();
					// 使用bundle来传递DetaiConten类型的data
					bundle.putSerializable("data", data);
					// 通过Intent传递数据
					intent.putExtras(bundle);
					OverviewActivity.this.startActivity(intent);
				}
			});

			TextView content = (TextView) convertView.findViewById(R.id.content);
			TextView Response_count = (TextView) convertView.findViewById(R.id.Response_count);
			// 将后台参数与数据实体进行绑定
			tagName.setText(tmp.tag_name + "  > ");
			lastName.setText(tmp.last_name);
			imgView.setImageDrawable(drawable);
			title.setText(tmp.title);
			content.setText(tmp.content);
			Response_count.setText(tmp.Response_count);
			// 返回convertView，完成数据填充
			return convertView;
		}
	}
}
