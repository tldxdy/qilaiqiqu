package com.qizhi.qilaiqiqu.adapter;
import java.util.List;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.activity.MapActivity;
import com.qizhi.qilaiqiqu.activity.StartActivity;
import com.qizhi.qilaiqiqu.model.TravelsinformationModel;
import com.qizhi.qilaiqiqu.model.PublishTravelsModel;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
public class ReleaseListAdapter extends BaseAdapter{

	private Context context;

	private LayoutInflater inflater;

	private List<TravelsinformationModel> list;

	private ViewHolder holder;
	
	
	private Button confirmBtn;
	private Button cancelBtn;
	
	private PublishTravelsModel ptm;
	
	public ReleaseListAdapter(Context context, List<TravelsinformationModel> list, PublishTravelsModel ptm) {

		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.ptm = ptm;

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
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_list_releaseactivity,null);
			holder.photoImg = (ImageView) convertView.findViewById(R.id.img_releaseactivity_photo);
			holder.locationImg = (ImageView) convertView.findViewById(R.id.img_releaseactivity_location);
			holder.deleteImg = (ImageView) convertView.findViewById(R.id.img_releaseactivity_delete);
			
			holder.locationTxt = (TextView) convertView.findViewById(R.id.txt_releaseactivity_location);
			
			holder.deleteLayout = (LinearLayout) convertView.findViewById(R.id.layout_releaseactivity_delete);
			
			holder.contentEdit = (EditText) convertView.findViewById(R.id.edt_releaseactivity_content);
			holder.legendEdit = (EditText) convertView.findViewById(R.id.edt_releaseactivity_legend);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//Bitmap bm1 = BitmapFactory.decodeFile(list.get(position).getArticleImage());
		Bitmap bm = SystemUtil.compressImageFromFile(list.get(position).getArticleImage());
		//Bitmap bm = SystemUtil.compressImage(bm1, 200);
		//Bitmap bm = SystemUtil.ratio(bm1);
		holder.photoImg.setImageBitmap(bm);
		holder.locationImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				/*TravelsinformationModel tfm = list.get(position);
				tfm.setAddress("杭州");
				list.set(position, tfm);*/
				list.get(position).setAddress("杭州");
				notifyDataSetChanged();
				context.startActivity(new Intent(context,MapActivity.class));
			}

		});
		
		holder.deleteImg.setImageResource(R.drawable.delete_picture);
		
		//删除
		holder.deleteLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Matrix matrix=new Matrix();
				holder.deleteImg.setScaleType(ScaleType.MATRIX); //required
				matrix.postRotate((float) 90, holder.deleteImg.getWidth()/2, holder.deleteImg.getHeight()/2);
				holder.deleteImg.setImageMatrix(matrix);*/
				
				showPopupWindow(v,position,holder.deleteImg);
				
			}
		});
		//内容监听
		holder.contentEdit.addTextChangedListener(new TextWatcher(){
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
				StringBuffer sb = new StringBuffer();
				for(int i = 0; i < list.size(); i++){
					sb.append(list.get(i).getMemo());
					if(i != list.size()-1){
						sb.append("|");
					}
				}
				ptm.setMemo(sb.toString());
			}
		});
		//图片说明监听
		holder.legendEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				list.get(position).setImageMemo(s.toString().trim());
				StringBuffer sb = new StringBuffer();
				for(int i = 0; i < list.size(); i++){
					sb.append(list.get(i).getImageMemo());
					if(i != list.size()-1){
						sb.append("|");
					}
				}
				ptm.setImageMemo(sb.toString());
			}
		});
		
		//位置的监听
		holder.locationTxt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
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
	
	private void showPopupWindow(View view,final int position,final ImageView img) {

		
		
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
				list.remove(position);
				notifyDataSetChanged();
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
		//popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_layout));
		// 设置好参数之后再show
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

	}

	
	
	private class ViewHolder {
		private EditText contentEdit;
		private EditText legendEdit;
		
		private ImageView photoImg;
		private ImageView locationImg;
		private ImageView deleteImg;
		
		private TextView locationTxt;
		
		private LinearLayout deleteLayout;

	}

}
