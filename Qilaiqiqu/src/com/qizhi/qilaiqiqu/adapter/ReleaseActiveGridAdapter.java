package com.qizhi.qilaiqiqu.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import com.qizhi.qilaiqiqu.R;

public class ReleaseActiveGridAdapter extends BaseAdapter {

	private ImageView addImg;

	private List<?> list;
	private Context context;
	private LayoutInflater inflater;
	private ViewHolderHeader holderHeader;

	public ReleaseActiveGridAdapter(List<?> list, Context context) {
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);

		Toast.makeText(context, "list.size()" + list.size(), 0).show();
	}

	@Override
	public int getCount() {
		return list.size();
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
	public View getView(int position, View view, ViewGroup arg2) {

		Toast.makeText(context, "position" + position, 0).show();

		// if (position + 1 == list.size()) {
		// view = inflater.inflate(R.layout.item_grid_addpicture, null);
		// addImg = (ImageView) view
		// .findViewById(R.id.img_releaseActiveActivity_add);
		// addImg.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// context.startActivity(new Intent(context,
		// SelectImagesActivity.class));
		// }
		// });
		// }

		if (view == null) {
			holderHeader = new ViewHolderHeader();
			view = inflater.inflate(R.layout.item_grid_picture, null);
			holderHeader.pictureImg = (ImageView) view
					.findViewById(R.id.img_releaseActiveActivity_picture);
			holderHeader.deleteImg = (ImageView) view
					.findViewById(R.id.img_releaseActiveActivity_delete);
			view.setTag(holderHeader);
		} else {
			holderHeader = (ViewHolderHeader) view.getTag();
		}

		// Picasso.with(context).load(list.get(position).).into(holderHeader.pictureImg);

		holderHeader.deleteImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

		holderHeader.pictureImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

		holderHeader.deleteImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

		return view;

	}

	public class ViewHolderHeader {
		private ImageView deleteImg;
		private ImageView pictureImg;
	}

}
