package com.qizhi.qilaiqiqu.activity;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

import com.lidroid.xutils.BitmapUtils;
import com.qizhi.qilaiqiqu.R;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

public class NativeImagesActivity extends HuanxinLogOutActivity implements OnClickListener {

	// protected ImageLoader imageLoader = ImageLoader.getInstance();

	private GridView photoGrid;

	private LinearLayout backLayout;

	private TextView confirmTxt;
	private TextView optionTxt;

	private ArrayList<String> imageUrls;
	// private DisplayImageOptions options;
	private ImageAdapter imageAdapter;

	private ViewHolder holder;

	private boolean falg = false;
	BitmapUtils bitmapUtils;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				Object ob = msg.obj;
				if (!ob.toString().equals("0")) {
					optionTxt.setText("已选择" + ob.toString() + "张");
				} else {
					optionTxt.setText("发布骑游记");
				}
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_native_image);

		initData();
		initView();

	}

	private void initView() {
		/*
		 * options = new DisplayImageOptions.Builder()
		 * .showStubImage(R.drawable.bitmap_picture)
		 * .showImageForEmptyUri(R.drawable.bitmap_picture)
		 * .cacheInMemory().cacheOnDisc().build();
		 */
		photoGrid = (GridView) findViewById(R.id.grid_photo);

		backLayout = (LinearLayout) findViewById(R.id.layout_back);

		confirmTxt = (TextView) findViewById(R.id.txt_confirm);
		optionTxt = (TextView) findViewById(R.id.txt_nativeimageactivity_option);

		falg = getIntent().getBooleanExtra("falg", false);

		bitmapUtils = new BitmapUtils(NativeImagesActivity.this);

		backLayout.setOnClickListener(this);
		confirmTxt.setOnClickListener(this);
		imageAdapter = new ImageAdapter(this, imageUrls);
		photoGrid.setAdapter(imageAdapter);

	}

	/**
	 * 读取本地图片
	 */
	private void initData() {
		this.imageUrls = new ArrayList<String>();

		final String[] columns = { MediaStore.Images.Media.DATA,
				MediaStore.Images.Media._ID };
		final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
		Cursor imagecursor = this
				.getApplicationContext()
				.getContentResolver()
				.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
						null, null, orderBy + " DESC");
		for (int i = 0; i < imagecursor.getCount(); i++) {
			imagecursor.moveToPosition(i);
			int dataColumnIndex = imagecursor
					.getColumnIndex(MediaStore.Images.Media.DATA);
			if (imagecursor.getString(dataColumnIndex).contains(".jpg")
					|| imagecursor.getString(dataColumnIndex).contains(".JPG")
					|| imagecursor.getString(dataColumnIndex).contains(".png")
					|| imagecursor.getString(dataColumnIndex).contains(".PNG")
					&& fileIsExists(imagecursor.getString(dataColumnIndex))) {
				imageUrls.add(imagecursor.getString(dataColumnIndex));
			}
		}
	}
	public boolean fileIsExists(String img){
        try{
                File f=new File(img);
                if(!f.exists()){
                        return false;
                }
                
        }catch (Exception e) {
                return false;
        }
        return true;
}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		case R.id.txt_confirm:
			if (!falg) {
				System.out.println("---------------------------------------");
				ArrayList<String> selectedItems = imageAdapter
						.getCheckedItems();
				for (String string : selectedItems) {
					System.out.println(string);
				}
				Intent intentConfirm = new Intent(this, ReleaseActivity.class);
				intentConfirm.putStringArrayListExtra("photoList",
						selectedItems);
				startActivity(intentConfirm);
				finish();
			} else {
				ArrayList<String> selectedItems = imageAdapter
						.getCheckedItems();
				Intent intentConfirm = new Intent();
				intentConfirm.putStringArrayListExtra("photoList",
						selectedItems);
				setResult(1, intentConfirm);
				finish();

			}
			break;
		}
	}

	@Override
	protected void onStop() {
		// imageLoader.stop();
		super.onStop();
	}

	/**
	 * Description GridView Adapter
	 */
	public class ImageAdapter extends BaseAdapter {

		ArrayList<String> mList;
		LayoutInflater mInflater;
		Context mContext;
		SparseBooleanArray mSparseBooleanArray;

		public ImageAdapter(Context context, ArrayList<String> imageList) {
			mContext = context;
			mInflater = LayoutInflater.from(mContext);
			mSparseBooleanArray = new SparseBooleanArray();
			mList = new ArrayList<String>();
			this.mList = imageList;
		}

		public ArrayList<String> getCheckedItems() {
			ArrayList<String> mTempArry = new ArrayList<String>();

			for (int i = 0; i < mList.size(); i++) {
				if (mSparseBooleanArray.get(i)) {
					mTempArry.add(mList.get(i));
				}
			}

			return mTempArry;
		}

		@Override
		public int getCount() {
			return imageUrls.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(
						R.layout.item_grid_nativeimageactivity_photo, null);
				holder.photoChk = (CheckBox) convertView
						.findViewById(R.id.chk_photo);
				holder.photoImg = (ImageView) convertView
						.findViewById(R.id.img_photo);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			/*bitmapUtils.display(holder.photoImg,
					"file://" + imageUrls.get(position));*/
			
			 Picasso.with(mContext).load("file://" +
					 imageUrls.get(position)).resize(480, 480)
					 	.centerCrop().into(holder.photoImg);
			 

			holder.photoChk.setTag(position);
			holder.photoChk.setChecked(mSparseBooleanArray.get(position));
			holder.photoChk.setOnCheckedChangeListener(mCheckedChangeListener);

			return convertView;
		}

		OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mSparseBooleanArray.put((Integer) buttonView.getTag(),
						isChecked);
				Message msg = new Message();
				msg.what = 1;
				msg.obj = getCheckedItems().size();
				handler.sendMessage(msg);
			}
		};
	}

	private class ViewHolder {
		private ImageView photoImg;
		private CheckBox photoChk;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
