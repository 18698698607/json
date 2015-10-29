package com.example.httpdemo;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView mTextView;
	private Button mButton, mButton2, mButton3, mButton4;
	private ImageView mImageView;
	public static final String GET_URL = "http://op.juhe.cn/onebox/weather/query?cityname=%E6%B8%A9%E5%B7%9E&key=03b4b890ab2200e9db7000aa921e2437";
	public static final String GET_IMG = "http://t10.baidu.com/it/u=2263913181,532364989&fm=59";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.button1).setOnClickListener(mClickListener);
		findViewById(R.id.button2).setOnClickListener(mClickListener);
		findViewById(R.id.button3).setOnClickListener(mClickListener);
		findViewById(R.id.button4).setOnClickListener(mClickListener);
		mTextView = (TextView) findViewById(R.id.text);
		mImageView=(ImageView)findViewById(R.id.im);
	}

	// 监听
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.button1:
				// 异步操作
				new AsyncTask<Void, Void, String>() {

					@Override
					protected String doInBackground(Void... arg0) {
						// TODO Auto-generated method stub
						String txt = "";
						try {
							txt = doget();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						return txt;
					}

					protected void onPostExecute(String result) {
						super.onPostExecute(result);
						if (!TextUtils.isEmpty(result)) {
							mTextView.setText(result);
						}

					}
					// 启动
				}.execute();

				break;

			case R.id.button2:
				new AsyncTask<Void, Void, Bitmap>() {

					@Override
					protected Bitmap doInBackground(Void... arg0) {
						Bitmap bm = null;
						try {
							bm = getBitmap();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return bm;
					}

					protected void onPostExecute(Bitmap result) {
						super.onPostExecute(result);
						if (result != null) {
							mImageView.setImageBitmap(result);
						}
					}
				}.execute();

				break;
			case R.id.button3:
				break;
			case R.id.button4:
				break;
			}
		}

		// 获取数据
		private String doget() throws Exception {
			String txt = "";
			// 创建url
			URL url = new URL(GET_URL);
			// 通过HttpURLConnection创建连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置连接超时等时间
			conn.setConnectTimeout(500);
			// 对响应吗进行判断
			if (conn.getResponseCode() == 200) {
				// 获取数据用流
				InputStream is = conn.getInputStream();
				// Bitmap bmp = BitmapFactory.decodeStream(is);

				BufferedInputStream bis = new BufferedInputStream(is);
				byte[] data = new byte[1024];
				int len = 0;
				StringBuffer buff = new StringBuffer();
				while ((len = bis.read(data)) != -1) {
					String str = new String(data, 0, len);
					buff.append(str);
				}
				txt = buff.toString();
				
				// 关闭流
				bis.close();

			}
			// 关闭连接
			conn.disconnect();
			return txt;

		}

		private Bitmap getBitmap() throws Exception {
			Bitmap bm = null;
			URL url = new URL(GET_IMG);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(500);
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				bm = BitmapFactory.decodeStream(is);
				is.close();
			}
			conn.disconnect();
			return bm;
		}

	};
}
