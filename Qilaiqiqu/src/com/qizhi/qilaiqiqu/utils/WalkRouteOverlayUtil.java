package com.qizhi.qilaiqiqu.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkStep;

public class WalkRouteOverlayUtil extends RouteOverlay {
	private WalkPath walkPath;

	public WalkRouteOverlayUtil(Context context, AMap amap, WalkPath path,
			LatLonPoint start, LatLonPoint end) {
		super(context);
		this.mAMap = amap;
		this.walkPath = path;
		startPoint = AMapServicesUtil.convertToLatLng(start);
		endPoint = AMapServicesUtil.convertToLatLng(end);
	}

	public void addToMap() {
		List<WalkStep> walkPaths = walkPath.getSteps();
		for (int i = 0; i < walkPaths.size(); i++) {
			WalkStep walkStep = walkPaths.get(i);
			LatLng latLng = AMapServicesUtil.convertToLatLng(walkStep
					.getPolyline().get(0));
			if (i < walkPaths.size() - 1) {
				if (i == 0) {
					Polyline startLine = mAMap
							.addPolyline(new PolylineOptions()
									.add(startPoint, latLng).color(getBusColor())
									.width(getBuslineWidth()));// 把起始点和第�?��步行的起点连接起�?
					startLine.setColor(getBusColor());
					allPolyLines.add(startLine);

				}
				LatLng latLngEnd = AMapServicesUtil.convertToLatLng(walkStep
						.getPolyline().get(walkStep.getPolyline().size() - 1));
				LatLng latLngStart = AMapServicesUtil.convertToLatLng(walkPaths
						.get(i + 1).getPolyline().get(0));
				if (!(latLngEnd.equals(latLngStart))) {
					Polyline breakLine = mAMap
							.addPolyline(new PolylineOptions()
									.add(latLngEnd, latLngStart).color(getBusColor())
									.width(getBuslineWidth()));// 把前�?��步行段的终点和后�?��步行段的起点连接起来
					allPolyLines.add(breakLine);
				}
			} else {
				LatLng latLng1 = AMapServicesUtil.convertToLatLng(walkStep
						.getPolyline().get(walkStep.getPolyline().size() - 1));
				Polyline endLine = mAMap.addPolyline(new PolylineOptions()
						.add(latLng1, endPoint).color(getBusColor())
						.width(getBuslineWidth()));// 把最终点和最后一个步行的终点连接起来
				endLine.setColor(getBusColor());
				allPolyLines.add(endLine);
			}

			// Marker walkMarker = mAMap.addMarker(new MarkerOptions()
			// .position(latLng)
			// .title("\u65B9\u5411:" + walkStep.getAction()
			// + "\n\u9053\u8DEF:" + walkStep.getRoad())
			// .snippet(walkStep.getInstruction()).anchor(0.5f, 0.5f));
			// stationMarkers.add(walkMarker);
			
			List<Polyline> paths = new ArrayList<Polyline>();
			
			Polyline walkLine = mAMap.addPolyline(new PolylineOptions()
					.addAll(AMapServicesUtil.convertArrList(walkStep
							.getPolyline())).color(getBusColor())
					.width(getBuslineWidth()));
			walkLine.setColor(getBusColor());
			allPolyLines.add(walkLine);
			paths.add(walkLine);
//			RouteOverlayActivity activity = new RouteOverlayActivity();
//			activity.paths = paths;
		}
		addStartAndEndMarker();
	}

	protected float getBuslineWidth() {
		return 30;
	}

}
