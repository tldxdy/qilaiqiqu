package com.qizhi.qilaiqiqu.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkStep;

public class WalkRouteOverlayUtil extends RouteOverlay {
	private WalkPath walkPath;
	private List<Polyline> paths;

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
									.add(startPoint, latLng)
									.color(getWalkColor())
									.width(getBuslineWidth()));// 把起始点和第�?��步行的起点连接起�?
					startLine.setColor(getWalkColor());
					allPolyLines.add(startLine);
				}
				LatLng latLngEnd = AMapServicesUtil.convertToLatLng(walkStep
						.getPolyline().get(walkStep.getPolyline().size() - 1));
				LatLng latLngStart = AMapServicesUtil.convertToLatLng(walkPaths
						.get(i + 1).getPolyline().get(0));
				if (!(latLngEnd.equals(latLngStart))) {
					Polyline breakLine = mAMap
							.addPolyline(new PolylineOptions()
									.add(latLngEnd, latLngStart)
									.color(getWalkColor())
									.width(getBuslineWidth()));// 把前�?��步行段的终点和后�?��步行段的起点连接起来
					allPolyLines.add(breakLine);
				}
			} else {
				LatLng latLng1 = AMapServicesUtil.convertToLatLng(walkStep
						.getPolyline().get(walkStep.getPolyline().size() - 1));
				Polyline endLine = mAMap.addPolyline(new PolylineOptions()
						.add(latLng1, endPoint).color(getWalkColor())
						.width(getBuslineWidth()));// 把最终点和最后一个步行的终点连接起来
				endLine.setColor(getWalkColor());
				allPolyLines.add(endLine);
			}

			// Marker walkMarker = mAMap.addMarker(new MarkerOptions()
			// .position(latLng)
			// .title("\u65B9\u5411:" + walkStep.getAction()
			// + "\n\u9053\u8DEF:" + walkStep.getRoad())
			// .snippet(walkStep.getInstruction()).anchor(0.5f, 0.5f));
			// stationMarkers.add(walkMarker);

			paths = new ArrayList<Polyline>();

			Polyline walkLine = mAMap.addPolyline(new PolylineOptions()
					.addAll(AMapServicesUtil.convertArrList(walkStep
							.getPolyline())).color(getWalkColor())
					.width(getBuslineWidth()));

			walkLine.setColor(getWalkColor());
			paths.add(walkLine);
			allPolyLines.add(walkLine);
			
			ll.add(allPolyLines);
			System.out.println("paths:"+paths.size());
			System.out.println("allPolyLines:"+allPolyLines.size());
			System.out.println(ll.size());
			
			
			
			hashMap.put("line", allPolyLines);
			
			linesMaps.add(hashMap);
		}
		// addStartAndEndMarker();
	}

	public void removeLine() {
		// paths.remove(paths.size()-1);
		// allPolyLines.remove(allPolyLines.size()-1);
		removeFromMap();
	}

	protected float getBuslineWidth() {
		return 30;
	}

}
