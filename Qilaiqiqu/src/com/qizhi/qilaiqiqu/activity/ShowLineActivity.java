package com.qizhi.qilaiqiqu.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

import cn.jpush.android.api.JPushInterface;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.FromAndTo;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.utils.AMapUtil;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.WalkRouteOverlayUtil;
import com.umeng.analytics.MobclickAgent;

public class ShowLineActivity extends HuanxinLogOutActivity implements
		LocationSource, AMapLocationListener, OnRouteSearchListener {

	private AMap aMap;
	private MapView mapView;

	private List<Marker> markerList = null;
	private List<LatLonPoint> pointList = null;

	private Marker geoMarker;
	private Marker regeoMarker;
	private AMapLocationClient mlocationClient;
	private OnLocationChangedListener mListener;
	private AMapLocationClientOption mLocationOption;

	private LinearLayout Backlayout;

	FromAndTo fromAndTo;// 起始点和终点的经纬度
	RouteSearch routeSearch;
	private LatLonPoint startPoint = null;
	private LatLonPoint endPoint = null;

	private int isGoOn = 1;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (isGoOn == 1) {
				isGoOn = 0;
				stopTime();
				drawLine();
				System.out.println("stopTime()+drawLine()");
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_show_line);
		init();
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		initView();
		initEvent();
	}

	private void initView() {
		markerList = new ArrayList<Marker>();
		Backlayout = (LinearLayout) findViewById(R.id.layout_ShowLineActivity_back);
	}

	private void initEvent() {
		Backlayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 初始化
	 */
	private void init() {
		mapView = (MapView) findViewById(R.id.showLineActivty_map);
		if (aMap == null) {
			aMap = mapView.getMap();
			aMap.getUiSettings().setCompassEnabled(true);
			aMap.getUiSettings().setZoomControlsEnabled(false);
			regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
			geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
			routeSearch = new RouteSearch(this);
			routeSearch.setRouteSearchListener(this);
			setUpMap();
		}

	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null && amapLocation.getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
				startTime();
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode() + ": "
						+ amapLocation.getErrorInfo();
				Log.e("AmapErr", errText);
			}
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			// 设置定位监听
			mlocationClient.setLocationListener(this);
			// 设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			// 设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();
		}

	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
		MobclickAgent.onResume(this);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
		MobclickAgent.onPause(this);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		if (null != mlocationClient) {
			mlocationClient.onDestroy();
		}
	}

	/**
	 * 设置小篮点
	 */
	public void setLoctionStyle() {
		// 自定义系统定位蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		// 自定义定位蓝点图标
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.mark));
		// 自定义精度范围的圆形边框颜色
		myLocationStyle.strokeColor(getResources().getColor(R.color.mapblue));
		// 自定义精度范围的圆形边框宽度
		myLocationStyle.strokeWidth(5);
		// 将自定义的 myLocationStyle 对象添加到地图上
		aMap.setMyLocationStyle(myLocationStyle);
		// 构造 LocationManagerProxy 对象
		// mAMapLocationManager = LocationManagerProxy
		// .getInstance(MapActivity.this);
		// 设置定位资源。如果不设置此定位资源则定位按钮不可点击。
		aMap.setLocationSource(this);
		// 设置默认定位按钮是否显示
		aMap.getUiSettings().setMyLocationButtonEnabled(false);
		// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		aMap.setMyLocationEnabled(true);
	}

	/**
	 * @param 3秒启动handle
	 */
	private Timer timer;
	private TimerTask task;
	private String stringExtra;
	private String[] split;
	private String[] split2;
	private LatLonPoint latLonPoint;

	private void startTime() {
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				Message message = handler.obtainMessage();
				message.arg1 = isGoOn;
				handler.sendMessage(message);

				System.out.println("startTime()+run()");

			}
		};
		timer.schedule(task, 1000);
	}

	private void stopTime() {
		timer.cancel();
	}

	private void drawLine() {
		pointList = new ArrayList<LatLonPoint>();
		if (!"".equals(getIntent().getStringExtra("LanInfo"))) {
			stringExtra = getIntent().getStringExtra("LanInfo");
			if (stringExtra != null) {
				split = stringExtra.split(",");
				if (split.length > 1) {
					for (int i = 0; i < split.length; i++) {
						split2 = split[i].split(" ");
						latLonPoint = new LatLonPoint(
								Double.parseDouble(split2[0]),
								Double.parseDouble(split2[1]));
						pointList.add(latLonPoint);
						setmarker(pointList.get(i));
					}
					if (pointList.size() > 1) {
						for (int i = 0; i < pointList.size() - 1; i++) {
							mRouteOverlay(pointList.get(i),
									pointList.get(i + 1));
						}
					}

				}
			}
			aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					AMapUtil.convertToLatLng(pointList.get(0)), 14.5f));
			markerList.get(markerList.size() - 1)
					.setIcon(
							BitmapDescriptorFactory
									.fromResource(R.drawable.finish_map));
		}

	}

	private void mRouteOverlay(LatLonPoint start, LatLonPoint end) {

		startPoint = start;
		endPoint = end;

		fromAndTo = new FromAndTo(startPoint, endPoint);// 实例化FromAndTo，字面意思,哪到哪
		WalkRouteQuery walkRouteQuery = new WalkRouteQuery(fromAndTo,
				RouteSearch.WalkDefault);
		routeSearch.calculateWalkRouteAsyn(walkRouteQuery);
	}

	private void setmarker(LatLonPoint llp) {
		markerList.add(aMap.addMarker(new MarkerOptions().anchor(0.1f, 0.1f)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.strat_map))));

		markerList.get(markerList.size() - 1).setPosition(
				AMapUtil.convertToLatLng(llp));

	}

	/**
	 * 步行路线结果回调
	 */

	private WalkRouteResult walkRouteResult;// 步行模式查询结果
	public static WalkRouteOverlayUtil walkRouteOverlay = null;

	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int rCode) {

		System.out.println("rCode:" + rCode + ",result:" + result);

		if (rCode == 0) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				walkRouteResult = result;
				WalkPath walkPath = walkRouteResult.getPaths().get(0);
				walkRouteOverlay = new WalkRouteOverlayUtil(this, aMap,
						walkPath, walkRouteResult.getStartPos(),
						walkRouteResult.getTargetPos());
				walkRouteOverlay.addToMap();

			} else {
				new SystemUtil().makeToast(ShowLineActivity.this,
						R.string.no_result + "no_result");
			}
		} else if (rCode == 27) {
			new SystemUtil().makeToast(ShowLineActivity.this,
					R.string.error_network + "error_network");
		} else if (rCode == 32) {
			new SystemUtil().makeToast(ShowLineActivity.this,
					R.string.error_key + "error_key");
		} else {
			new SystemUtil().makeToast(ShowLineActivity.this,
					getString(R.string.error_other) + ",rCode:" + rCode + "");
		}

	}

	@Override
	public void onBusRouteSearched(BusRouteResult arg0, int arg1) {

	}

	@Override
	public void onDriveRouteSearched(DriveRouteResult arg0, int arg1) {

	}

}
