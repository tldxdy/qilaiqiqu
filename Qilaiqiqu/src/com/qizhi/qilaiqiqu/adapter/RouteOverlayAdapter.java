package com.qizhi.qilaiqiqu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.utils.WalkRouteOverlayUtil;

public class RouteOverlayAdapter extends BaseAdapter {

	private ViewHolder holder;
	private LayoutInflater inflater;

	private List<String> list;
	private Context context;

	private AMap aMap;
	List<Marker> markerList;

	public static WalkRouteOverlayUtil walkRouteOverlay;

	public RouteOverlayAdapter(Context context, List<String> list, AMap aMap,
			List<Marker> markerList) {
		this.list = list;
		this.context = context;
		this.aMap = aMap;
		this.markerList = markerList;
		inflater = LayoutInflater.from(context);
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
	public View getView(int position, View v, ViewGroup arg2) {
		if (v == null || v.getTag() == null) {
			holder = new ViewHolder();
			v = inflater.inflate(R.layout.item_list_route_overlay_body, null);
			holder.pointImg = (ImageView) v
					.findViewById(R.id.img_routeOverlayList_point);
			holder.deleteImg = (ImageView) v
					.findViewById(R.id.img_routeOverlayList_delete);
			holder.positionTxt = (TextView) v
					.findViewById(R.id.txt_routeOverlayList_position);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		if (position == list.size() - 1) {
			holder.deleteImg.setVisibility(View.VISIBLE);
			holder.pointImg.setImageResource(R.drawable.finish);
			holder.deleteImg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (markerList.size() > 1) {
						System.out.println("walkRouteOverlay.removeFromMap()");
						walkRouteOverlay.removeFromMap();
					}
					list.remove(list.size() - 1);
					markerList.get(markerList.size() - 1).remove();
					markerList.remove(markerList.size() - 1);
					

					notifyDataSetChanged();
					// lastPolyline.remove();
					// aMap.clear();
				}
			});
		} else if (position != list.size() - 1) {
			holder.deleteImg.setVisibility(View.GONE);
			holder.pointImg.setImageResource(R.drawable.start);
		}

		holder.positionTxt.setText(list.get(position));

		return v;
	}

	public class ViewHolder {

		private ImageView pointImg;

		private ImageView deleteImg;
		private TextView positionTxt;

	}
}
