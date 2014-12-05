package com.overview;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.test.R;
import com.obj.DetailContent;
import com.obj.MyBitmap;
//逻辑与OverviewActivity类似，不做重复注释，可以翻看OverviewActivity
//主题需要显示
//标题
//内容
//评论数量
//浏览次数
//发贴者的名字
//发贴的时间
//Like, Share与Report this post不在project的范围内
//
//对于每一条评论需显示
//评论者的名字
//评论的时间
//评论者的头像 (没有头像则不显示）
//内容
//Like, Reply和Report this reply不在project的范围内
//评论以创建时间排序，创建时间越晚则越靠前。
public class DetailActivity extends ActionBarActivity {
	private int index;
	private MyAdapter adapter;
	private ListView listView;
	private ArrayList<ResultObj> list = new ArrayList<ResultObj>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
        //接收从OverviewActivity传来的数据
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		//读取Bubble
		DetailContent obj = (DetailContent) bundle.getSerializable("data");
		index = obj.getPos();
		try {
			//进行图片下载
			for (int i = 0; i < com.obj.STATIC_VAR.list_Sort.get(index).getJb()
					.getJSONArray("comments").length(); i++) {
				(new DownloadTask()).execute(
						com.obj.STATIC_VAR.list_Sort.get(index).getJb()
								.getJSONArray("comments").getJSONObject(i)
								.getJSONObject("author")
								.getString("profile_image"),
						Integer.toString(i));
			}
			//将title_detail直接注入Activity
			TextView title_detailTextView = (TextView) findViewById(R.id.title_detail);
			TextView content_detailTextView = (TextView) findViewById(R.id.content_detail);
			TextView comment_countTextView = (TextView) findViewById(R.id.comment_number_detail);
			TextView commetviewsTextView = (TextView) findViewById(R.id.comment_views_detail);
			TextView last_nameTextView = (TextView) findViewById(R.id.last_name_detail);
			TextView time_detailTextView = (TextView) findViewById(R.id.post_time_detail);
			title_detailTextView.setText(""
					+ com.obj.STATIC_VAR.list_Sort.get(index).getJb()
							.getString("title"));
			content_detailTextView.setText(""
					+ com.obj.STATIC_VAR.list_Sort.get(index).getJb()
							.getString("content"));
			comment_countTextView.setText(""
					+ com.obj.STATIC_VAR.list_Sort.get(index).getJb().length()
					+ " Comments" + "·   ");
			commetviewsTextView.setText(""
					+ com.obj.STATIC_VAR.list_Sort.get(index).getJb()
							.getString("views") + "  Views");
			last_nameTextView.setText("Posted by "
					+ com.obj.STATIC_VAR.list_Sort.get(index).getJb()
							.getJSONObject("author").getString("first_name")
					+ " ·   ");
			time_detailTextView.setText("Created time "
					+ com.obj.STATIC_VAR.list_Sort.get(index).getJb()
							.getString("time_created"));
			listView = (ListView) findViewById(R.id.list_detail);
			adapter = new MyAdapter(getApplicationContext());
			listView.setAdapter(adapter);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private class DownloadTask extends AsyncTask<String, Integer, MyBitmap> {
		@Override
		protected MyBitmap doInBackground(String... bitmapUrl) {
			//
			// bitmapUrl[0] = "http://www.baidu.com/img/baidu_logo.gif";
			URL imageUrl;
			MyBitmap mp = new MyBitmap();
			mp.setIndex(Integer.parseInt(bitmapUrl[1]));
			Bitmap bitmap = null;
			if (bitmapUrl[0] != null) {
				try {
					imageUrl = new URL(bitmapUrl[0]);
					HttpURLConnection conn = (HttpURLConnection) imageUrl
							.openConnection();
					InputStream inputStream = conn.getInputStream();
					bitmap = BitmapFactory.decodeStream(inputStream);
					mp.setBitmap(bitmap);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return mp;
			} else {
				mp.setBitmap(com.obj.STATIC_VAR.bitmap_default);
				mp.setIndex(Integer.parseInt(bitmapUrl[1]));
				return mp;
			}

		}

		@Override
		protected void onPostExecute(MyBitmap result) {
			super.onPostExecute(result);
			list.get(result.getIndex()).bitmap = result.getBitmap();
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class ResultObj {
		public String replyer_first_name;
		public Bitmap bitmap;
		public String reply_content;
		public String rely_time;

	}
//通过listview来写入comments & comments detail，所以要用到Myadapter
	public class MyAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public MyAdapter(Context context) throws JSONException {
			inflater = LayoutInflater.from(context);
			for (int i = 0; i < com.obj.STATIC_VAR.list_Sort.get(index).getJb()
					.getJSONArray("comments").length(); i++) {
				ResultObj a = new ResultObj();
				a.replyer_first_name = com.obj.STATIC_VAR.list_Sort.get(index)
						.getJb().getJSONArray("comments").getJSONObject(i)
						.getJSONObject("author").getString("first_name");
				a.reply_content = com.obj.STATIC_VAR.list_Sort.get(index).getJb()
						.getJSONArray("comments").getJSONObject(i)
						.getString("content");
				a.bitmap = com.obj.STATIC_VAR.bitmap_default;
				a.rely_time = com.obj.STATIC_VAR.list_Sort.get(index).getJb()
						.getJSONArray("comments").getJSONObject(i)
						.getString("time_created");
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				//将item_detail与inflater绑定
				convertView = inflater.inflate(R.layout.item_detail, null);
			}
			ResultObj tmp = (ResultObj) getItem(position);
			//将前台ID与后台数据进行绑定
			TextView reply_nameTextView = (TextView) convertView
					.findViewById(R.id.reply_name);
			TextView reply_timeTextView = (TextView) convertView
					.findViewById(R.id.reply_created_time);
			ImageView reply_imgView = (ImageView) convertView
					.findViewById(R.id.reply_profile_image);
			Drawable drawable = new BitmapDrawable(getResources(), tmp.bitmap);
			TextView reply_content = (TextView) convertView
					.findViewById(R.id.reply_content);
			reply_nameTextView.setText(tmp.replyer_first_name + " · ");
			reply_timeTextView.setText(" created time " + tmp.rely_time);
			reply_imgView.setImageDrawable(drawable);
			reply_content.setText(tmp.reply_content);
			return convertView;
		}
	}
}
