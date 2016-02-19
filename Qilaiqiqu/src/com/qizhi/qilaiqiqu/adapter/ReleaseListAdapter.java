package com.qizhi.qilaiqiqu.adapter;

import java.io.File;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.activity.MapActivity;
import com.qizhi.qilaiqiqu.model.TravelsinformationModel;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.squareup.picasso.Picasso;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 
 * @author hujianbo
 * 
 */
public class ReleaseListAdapter extends BaseAdapter {

	private Context context;

	private LayoutInflater inflater;

	private List<TravelsinformationModel> list;

	private ViewHolder holder;

	private Button confirmBtn;
	private Button cancelBtn;
	private BitmapUtils bitmapUtils;

	private boolean falg;
	private Handler handler;
	private int updateListSum;
	//private int screenWidth;

	public ReleaseListAdapter(Context context,
			List<TravelsinformationModel> list,
			boolean falg, Handler handler,int updateListSum) {

		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		bitmapUtils = new BitmapUtils(context);
		this.falg = falg;
		this.handler = handler;
		this.updateListSum = updateListSum;
		//screenWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
		
	
	
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
//		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_list_releaseactivity,
					null);
			holder.photoImg = (ImageView) convertView
					.findViewById(R.id.img_releaseactivity_photo);
			/*holder.locationImg = (ImageView) convertView
					.findViewById(R.id.img_releaseactivity_location);*/
			holder.deleteImg = (ImageView) convertView
					.findViewById(R.id.img_releaseactivity_delete);

			holder.locationTxt = (TextView) convertView
					.findViewById(R.id.txt_releaseactivity_location);

			holder.deleteLayout = (LinearLayout) convertView
					.findViewById(R.id.layout_releaseactivity_delete);

			holder.contentEdit = (EditText) convertView
					.findViewById(R.id.edt_releaseactivity_content);
			holder.legendEdit = (EditText) convertView
					.findViewById(R.id.edt_releaseactivity_legend);
			holder.locationLayout = (LinearLayout) convertView
					.findViewById(R.id.layout_releaseactivity_location);

/*			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}*/

		
		holder.contentEdit.setText(list.get(position).getMemo());
		holder.legendEdit.setText(list.get(position).getImageMemo());
		holder.locationTxt.setText(list.get(position).getAddress());
		
		if (!falg) {
			//holder.photoImg.setImageBitmap(SystemUtil.compressImageFromFile(list.get(position).getArticleImage(), screenWidth));
			Picasso.with(context).load(new File(list.get(position).getArticleImage()))
			.resize(800, 400).centerInside()
			.placeholder(R.drawable.bitmap_homepage)
			.error(R.drawable.bitmap_homepage)
			.into(holder.photoImg);
			/*bitmapUtils.display(holder.photoImg, list.get(position)
					.getArticleImage());*/
		} else {
			if(position < updateListSum){
				SystemUtil.Imagexutils(list.get(position).getArticleImage(), holder.photoImg, context);

			}else{
				
				Picasso.with(context).load(new File(list.get(position).getArticleImage()))
				.resize(800, 400).centerInside()
				.placeholder(R.drawable.bitmap_homepage)
				.error(R.drawable.bitmap_homepage)
				.into(holder.photoImg);
				/*bitmapUtils.display(holder.photoImg, list.get(position)
						.getArticleImage());*/
			}
		}
		

		holder.locationLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, MapActivity.class);
				intent.putExtra("position", position);
				((Activity) context).startActivityForResult(intent, 2);
			}

		});

		holder.deleteImg.setImageResource(R.drawable.delete_picture);

		// 删除
		holder.deleteLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showPopupWindow(v, position, holder.deleteImg);

			}
		});
		// 内容监听
		holder.contentEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				list.get(position).setMemo(s.toString().trim());
			}
		});
		// 图片说明监听
		holder.legendEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				list.get(position).setImageMemo(s.toString().trim());
			}
		});

		// 位置的监听
		holder.locationTxt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				list.get(position).setAddress(s.toString().trim());
			}
		});
		return convertView;
	}

	private void showPopupWindow(View view, final int position,
			final ImageView img) {

		// 一个自定义的布局，作为显示的内容
		View mview = LayoutInflater.from(context).inflate(
				R.layout.popup_delete_releaseactivity, null);

		confirmBtn = (Button) mview.findViewById(R.id.btn_dialog_box_confirm);
		cancelBtn = (Button) mview.findViewById(R.id.btn_dialog_box_cancel);

		final PopupWindow popupWindow = new PopupWindow(mview,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

		popupWindow.setTouchable(true);

		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return true;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
					Message msg = new Message();
					msg.what = 2;
					msg.obj = position;
					handler.sendMessage(msg);
					popupWindow.dismiss();
			}
		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupWindow.dismiss();
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_layout));
		// 设置好参数之后再show
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

	}

	private class ViewHolder {
		private EditText contentEdit;
		private EditText legendEdit;

		private ImageView photoImg;
		//private ImageView locationImg;
		private ImageView deleteImg;

		private TextView locationTxt;
		private LinearLayout locationLayout;

		private LinearLayout deleteLayout;

	}

}
