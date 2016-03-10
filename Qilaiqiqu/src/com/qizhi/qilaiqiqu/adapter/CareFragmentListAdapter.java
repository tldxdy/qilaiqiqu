package com.qizhi.qilaiqiqu.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.model.CareModel.CareDataList;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil;
import com.qizhi.qilaiqiqu.utils.XUtilsUtil.CallBackPost;
import com.squareup.picasso.Picasso;

public class CareFragmentListAdapter extends BaseAdapter {

	// private int careFlag = 1; // 关注判断:1为未关注,2为已关注

	private SharedPreferences sp;
	private ArrayList<CareDataList> list;
	private Context context;
	private ViewHolder holder;

	public CareFragmentListAdapter(Context context, ArrayList<CareDataList> list) {
		this.list = list;
		this.context = context;
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

	public void removeItem(int position) {
		list.remove(position);
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		holder = new ViewHolder();
		if (view == null || view.getTag() == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.item_list_carefragment, null);
			holder.photoImg = (ImageView) view
					.findViewById(R.id.img_careFragmentList_photo);
			holder.nickTxt = (TextView) view
					.findViewById(R.id.txt_careFragmentList_nick);
			holder.careTxt = (TextView) view
					.findViewById(R.id.txt_careFragmentList_care);
			holder.personalityTxt = (TextView) view
					.findViewById(R.id.txt_careFragmentList_personality);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		Picasso.with(context)
				.load("http://weride.oss-cn-hangzhou.aliyuncs.com/"
						+ list.get(position).getUserImage())
				.into(holder.photoImg);
		holder.nickTxt.setText(list.get(position).getUserName());
		holder.personalityTxt.setText(list.get(position).getUserMemo());
		holder.careTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				deleteCare(position);
			}
		});
		return view;
	}

	public class ViewHolder {
		private ImageView photoImg;
		private TextView nickTxt;
		private TextView careTxt;
		private TextView personalityTxt;
	}

	public void deleteCare(final int position) {
		sp = context.getSharedPreferences("userLogin", 0);
		String url = "mobile/attention/cancelAttention.html";
		RequestParams params = new RequestParams();

		String uniqueKey = sp.getString("uniqueKey", null);
		params.addBodyParameter("userId", list.get(position).getUserId() + "");
		params.addBodyParameter("quoteUserId", list.get(position)
				.getQuoteUserId() + "");
		params.addBodyParameter("uniqueKey", uniqueKey);
		new XUtilsUtil().httpPost(url, params, new CallBackPost() {
			
			@Override
			public void onMySuccess(ResponseInfo<String> responseInfo) {
				try {
					JSONObject object = new JSONObject(responseInfo.result);
					boolean result = object.getBoolean("result");
					if (result)
						removeItem(position);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onMyFailure(HttpException error, String msg) {
				
			}
		});
	}

}
