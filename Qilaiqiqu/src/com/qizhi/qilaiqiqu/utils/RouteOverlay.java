package com.qizhi.qilaiqiqu.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.Polyline;

public class RouteOverlay {
	protected List<Marker> stationMarkers = new ArrayList<Marker>();
	protected List<Polyline> allPolyLines = new ArrayList<Polyline>();
	protected Marker startMarker;
	protected Marker endMarker;
	protected LatLng startPoint;
	protected LatLng endPoint;
	protected AMap mAMap;
	private Context mContext;
	private Bitmap startBit, endBit, busBit, walkBit, driveBit;
	private AssetManager am;

	public RouteOverlay(Context context) {
		mContext = context;
		am = mContext.getResources().getAssets();
	}

	/**
	 * 清除绘制
	 */
	public void removeFromMap() {
		if (startMarker != null) {
			startMarker.remove();

		}
		if (endMarker != null) {
			endMarker.remove();
		}
		for (Marker marker : stationMarkers) {
			marker.remove();
		}
		for (Polyline line : allPolyLines) {
			line.remove();
		}
		destroyBit();
	}

	private void destroyBit() {
		if (startBit != null) {
			startBit.recycle();
			startBit = null;
		}
		if (endBit != null) {
			endBit.recycle();
			endBit = null;
		}
		if (busBit != null) {
			busBit.recycle();
			busBit = null;
		}
		if (walkBit != null) {
			walkBit.recycle();
			walkBit = null;
		}
		if (driveBit != null) {
			driveBit.recycle();
			driveBit = null;
		}
	}

	protected BitmapDescriptor getStartBitmapDescriptor() {
		return getBitDes(startBit, "");
	}

	protected BitmapDescriptor getEndBitmapDescriptor() {
		return getBitDes(endBit, "");
	}

	protected BitmapDescriptor getBusBitmapDescriptor() {
		return getBitDes(busBit, "");
	}

	protected BitmapDescriptor getWalkBitmapDescriptor() {
		return getBitDes(walkBit, "");
	}

	protected BitmapDescriptor getDriveBitmapDescriptor() {
		return getBitDes(driveBit, "");
	}

	private BitmapDescriptor getBitDes(Bitmap bitmap, String fileName) {
		InputStream stream;
		try {
			stream = am.open(fileName);
			bitmap = BitmapFactory.decodeStream(stream);
			bitmap = AMapServicesUtil.zoomBitmap(bitmap, 1);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {

		}
		return BitmapDescriptorFactory.fromBitmap(bitmap);
	}

	protected void addStartAndEndMarker() {
//		startMarker = mAMap.addMarker((new MarkerOptions())
//				.position(startPoint));
//		// startMarker.showInfoWindow();
//
//		endMarker = mAMap.addMarker((new MarkerOptions()).position(endPoint)
//				);
//		// mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint,
//		// getShowRouteZoom()));
	}

	public void zoomToSpan() {
		if (startPoint != null && startPoint != null) {
			if (mAMap == null)
				return;
			LatLngBounds bounds = getLatLngBounds();
			mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
		}
	}

	protected LatLngBounds getLatLngBounds() {
		LatLngBounds.Builder b = LatLngBounds.builder();
		b.include(new LatLng(startPoint.latitude, startPoint.longitude));
		b.include(new LatLng(endPoint.latitude, endPoint.longitude));
		return b.build();
	}

	protected int getWalkColor() {
		return Color.parseColor("#cccccc");
	}

	protected int getBusColor() {
		return Color.parseColor("#ff3030");
	}

	protected int getDriveColor() {
		return Color.parseColor("#6dbfed");
	}

	// protected int getShowRouteZoom() {
	// return 15;
	// }
}
