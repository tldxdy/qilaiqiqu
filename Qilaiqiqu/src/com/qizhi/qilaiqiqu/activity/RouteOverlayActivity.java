package com.qizhi.qilaiqiqu.activity;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.ls.LSInput;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMapLongClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.maps.model.Polyline;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.FromAndTo;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.qizhi.qilaiqiqu.R;
import com.qizhi.qilaiqiqu.adapter.RouteOverlayAdapter;
import com.qizhi.qilaiqiqu.utils.AMapUtil;
import com.qizhi.qilaiqiqu.utils.SystemUtil;
import com.qizhi.qilaiqiqu.utils.WalkRouteOverlayUtil;
import com.umeng.analytics.MobclickAgent;

public class RouteOverlayActivity extends Activity implements OnClickListener,
		TextWatcher, LocationSource, AMapLocationListener,
		OnMarkerClickListener, InfoWindowAdapter, OnMapLongClickListener,
		OnGeocodeSearchListener, OnRouteSearchListener {

	private TextView searButton;
	private LinearLayout backLayout;
	private LinearLayout layoutLayout;
	private TextView positionTxt;
	private TextView keepLineTxt;

	private String addressName;

	private AMap aMap;
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	private GeocodeSearch geocoderSearch;
	private LatLonPoint latLonPoint;

	private List<LatLonPoint> latLonPointList = new ArrayList<LatLonPoint>();
	private List<String> addressList = new ArrayList<String>();
	StringBuffer addressBuffer = new StringBuffer();
	StringBuffer latLonPointBuffer = new StringBuffer();

	private Marker geoMarker;
	private Marker regeoMarker;

	// public List<Polyline> paths = new ArrayList<Polyline>();

	private LatLonPoint startPoint = null;
	private LatLonPoint endPoint = null;
	FromAndTo fromAndTo;// 起始点和终点的经纬度
	RouteSearch routeSearch;
	private WalkRouteResult walkRouteResult;// 步行模式查询结果
	// private LocationManagerProxy mAMapLocationManager;

	private List<String> list;
	private List<Marker> markerList = null;

	private ListView routeOverlayList;
	private RouteOverlayAdapter adapter;

	private TextView mLocationErrText;
	private TextView confirmTxt;
	private TextView cancleTxt;

	private LinearLayout infoLayout;

	private EditText searchText;// 输入搜索关键字
	private String keyWord = "";// 要输入的poi搜索关键字
	private ProgressDialog progDialog = null;// 搜索时进度条

	public static List<List<Polyline>> lineList = new ArrayList<List<Polyline>>();
	public static List<Integer> lineNum = new ArrayList<Integer>();

	private boolean isSelectOk = true;

	private boolean isCleanEdit = false;
	private String adress;
	public static WalkRouteOverlayUtil walkRouteOverlay = null;

	float distance = 0f; // 路线里程

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示程序的标题栏
		setContentView(R.layout.activity_route_overlay);
		initMap();
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		initView();
		initEvent();
	}

	// public boolean onTouchEvent(MotionEvent event) {
	// // 在这里判断一下如果是按下操作就获取坐标然后执行方法
	// if (event.getAction() == MotionEvent.ACTION_DOWN) {
	// displayXY(event.getX(), event.getY());
	// }
	// return super.onTouchEvent(event);
	// }
	//
	// private void displayXY(float x, float y) {
	// if (x > 80 && y > 300 && x < 200 && y < 600) {
	// LatLng target
	// } else {
	//
	// }
	// }

	private void initView() {
		layoutLayout = (LinearLayout) findViewById(R.id.layout_routeOverlayActivity_layout);
		positionTxt = (TextView) findViewById(R.id.txt_routeOverlayActivity_position);
		backLayout = (LinearLayout) findViewById(R.id.layout_routeOverlayActivity_back);
		searButton = (TextView) findViewById(R.id.txt_routeOverlayActivity_searchButton);
		searchText = (EditText) findViewById(R.id.edt_routeOverlayActivity_keyWord);
		keepLineTxt = (TextView) findViewById(R.id.txt_routeOverlayActivity_keepLine);
		confirmTxt = (TextView) findViewById(R.id.txt_routeOverlayActivity_confirm);
		cancleTxt = (TextView) findViewById(R.id.txt_routeOverlayActivity_cancel);
		infoLayout = (LinearLayout) findViewById(R.id.layout_routeOverlayActivity_info);
		routeOverlayList = (ListView) findViewById(R.id.routeOverlayActivity_list);
		list = new ArrayList<String>();
		markerList = new ArrayList<Marker>();
		searchText.addTextChangedListener(this);// 添加文本输入框监听事件
		adapter = new RouteOverlayAdapter(RouteOverlayActivity.this, list,
				aMap, markerList);

	}

	private void initEvent() {
		cancleTxt.setOnClickListener(this);
		backLayout.setOnClickListener(this);
		searButton.setOnClickListener(this);
		confirmTxt.setOnClickListener(this);
		searchText.setOnClickListener(this);
		keepLineTxt.setOnClickListener(this);
		routeOverlayList.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_routeOverlayActivity_back:
			finish();
			break;

		case R.id.txt_routeOverlayActivity_searchButton:

			isCleanEdit = true;
			searchButton();
			break;

		case R.id.txt_routeOverlayActivity_confirm:
			isSelectOk = true;
			addAddress();
			mRouteOverlay();
			infoLayout.setVisibility(View.GONE);
			break;

		case R.id.txt_routeOverlayActivity_cancel:
			isSelectOk = true;

			markerList.get(markerList.size() - 1).remove();
			markerList.remove(markerList.size() - 1);
			infoLayout.setVisibility(View.GONE);

			walkRouteOverlay.removeFromMap();

			break;

		case R.id.edt_routeOverlayActivity_keyWord:
			if (isCleanEdit) {
				searchText.setText("");
				isCleanEdit = false;
			}
			break;

		case R.id.txt_routeOverlayActivity_keepLine:

			for (int i = 0; i < latLonPointList.size(); i++) {
				latLonPointBuffer.append(latLonPointList.get(i).getLongitude());
				latLonPointBuffer.append(" ");
				latLonPointBuffer.append(latLonPointList.get(i).getLatitude());
				latLonPointBuffer.append(",");
			}
			String llp = latLonPointBuffer.substring(0,
					latLonPointBuffer.length() - 1).toString();

			for (int i = 0; i < addressList.size(); i++) {
				addressBuffer.append(addressList.get(i));
				addressBuffer.append(",");
			}
			String ars = addressBuffer.substring(0, addressBuffer.length() - 1)
					.toString();

			ReleaseActiveActivity.lanName = ars;
			ReleaseActiveActivity.lanInfo = llp;
			ReleaseActiveActivity.mileage = (distance / 1000) + "";

			System.out.println("lanName:" + ReleaseActiveActivity.lanName);
			System.out.println("lanInfo:" + ReleaseActiveActivity.lanInfo);
			System.out.println("mileage:" + ReleaseActiveActivity.mileage);

			break;

		default:
			break;
		}
	}

	private void mRouteOverlay() {
		if (markerList.size() > 1) {

			startPoint = AMapUtil.convertToLatLonPoint(markerList.get(
					markerList.size() - 2).getPosition());
			endPoint = AMapUtil.convertToLatLonPoint(markerList.get(
					markerList.size() - 1).getPosition());

			System.out.println("endPoint"+endPoint);
			
			fromAndTo = new FromAndTo(startPoint, endPoint);// 实例化FromAndTo，字面意思,哪到哪
			WalkRouteQuery walkRouteQuery = new WalkRouteQuery(fromAndTo,
					RouteSearch.WalkDefault);
			routeSearch.calculateWalkRouteAsyn(walkRouteQuery);
		}
	}

	private void addAddress() {
		list.add(adress);
		System.out.println(list);
		adapter.notifyDataSetChanged();
	}

	private void setMarker(LatLonPoint llp) {

		markerList.add(aMap.addMarker(new MarkerOptions().anchor(0.1f, 0.1f)
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));

		markerList.get(markerList.size() - 1).setPosition(
				AMapUtil.convertToLatLng(llp));

		// aMap.addMarker(
		// new MarkerOptions()
		// .anchor(0.1f, 0.1f)
		// .icon(BitmapDescriptorFactory
		// .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
		// .setPosition(AMapUtil.convertToLatLng(llp));

		adapter.notifyDataSetChanged();
	}

	/**
	 * 初始化
	 */
	private void initMap() {
		mapView = (MapView) findViewById(R.id.routeOverlayActivity_map);
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
		mLocationErrText = (TextView) findViewById(R.id.routeOverlayActivity_location_errInfo_text);
		mLocationErrText.setVisibility(View.GONE);
		aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
		aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
		aMap.setOnMapLongClickListener(this);
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		progDialog = new ProgressDialog(this);

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
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null && amapLocation.getErrorCode() == 0) {
				mLocationErrText.setVisibility(View.GONE);
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode() + ": "
						+ amapLocation.getErrorInfo();
				Log.e("AmapErr", errText);
				mLocationErrText.setVisibility(View.VISIBLE);
				mLocationErrText.setText(errText);
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
		mlocationClient = null;
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
		// .getInstance(RouteOverlayActivity.this);
		// 设置定位资源。如果不设置此定位资源则定位按钮不可点击。
		aMap.setLocationSource(this);
		// 设置默认定位按钮是否显示
		aMap.getUiSettings().setMyLocationButtonEnabled(false);
		// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		aMap.setMyLocationEnabled(true);
	}

	/**
	 * 点击搜索按钮
	 */
	public void searchButton() {
		keyWord = AMapUtil.checkEditText(searchText);
		if ("".equals(keyWord)) {
			new SystemUtil().makeToast(RouteOverlayActivity.this, "请输入地址");
			return;
		} else {
			showProgressDialog();
			geocoderSearch();
		}
	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * 显示进度框
	 */
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(false);
		progDialog.setMessage("正在搜索:\n" + keyWord);
		progDialog.show();
	}

	/**
	 * 开始进行地理编码搜索
	 */
	private void geocoderSearch() {
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		// name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
		GeocodeQuery query = new GeocodeQuery(keyWord, null);
		geocoderSearch.getFromLocationNameAsyn(query);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		marker.showInfoWindow();
		return false;
	}

	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	@Override
	public View getInfoWindow(final Marker marker) {
		View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri,
				null);
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(marker.getTitle());
		positionTxt.setText(marker.getTitle());
		TextView snippet = (TextView) view.findViewById(R.id.snippet);
		snippet.setText(marker.getSnippet());
		ImageButton button = (ImageButton) view
				.findViewById(R.id.start_amap_app);
		// 调起高德地图app
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startAMapNavi(marker);
			}
		});
		return view;
	}

	/**
	 * 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
	 */
	@SuppressWarnings("deprecation")
	public void startAMapNavi(Marker marker) {
		// 构造导航参数
		NaviPara naviPara = new NaviPara();
		// 设置终点位置
		naviPara.setTargetPoint(marker.getPosition());
		// 设置导航策略，这里是避免拥堵
		naviPara.setNaviStyle(NaviPara.DRIVING_AVOID_CONGESTION);

		// 调起高德地图导航
		try {
			AMapUtils.openAMapNavi(naviPara, getApplicationContext());
		} catch (com.amap.api.maps.AMapException e) {

			// 如果没安装会进入异常，调起下载页面
			AMapUtils.getLatestAMapApp(getApplicationContext());

		}

	}

	/**
	 * 判断高德地图app是否已经安装
	 */
	public boolean getAppIn() {
		PackageInfo packageInfo = null;
		try {
			packageInfo = this.getPackageManager().getPackageInfo(
					"com.autonavi.minimap", 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		// 本手机没有安装高德地图app
		if (packageInfo != null) {
			return true;
		}
		// 本手机成功安装有高德地图app
		else {
			return false;
		}
	}

	/**
	 * 获取当前app的应用名字
	 */
	public String getApplicationName() {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(
					getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager
				.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String newText = s.toString().trim();
		Inputtips inputTips = new Inputtips(RouteOverlayActivity.this,
				new InputtipsListener() {

					@Override
					public void onGetInputtips(List<Tip> tipList, int rCode) {
						if (rCode == 0) {// 正确返回
							List<String> listString = new ArrayList<String>();
							for (int i = 0; i < tipList.size(); i++) {
								listString.add(tipList.get(i).getName());
							}
							ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
									getApplicationContext(),
									R.layout.route_inputs, listString);
							// searchText.setAdapter(aAdapter);
							aAdapter.notifyDataSetChanged();
						}
					}
				});
		try {
			inputTips.requestInputtips(newText, "");// 第一个参数表示提示关键字，第二个参数默认代表全国，也可以为城市区号

		} catch (AMapException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMapLongClick(LatLng l) {
		if (isSelectOk) {
			isSelectOk = false;
			latLonPoint = new LatLonPoint(l.latitude, l.longitude);

			System.out.println("latLonPoint:" + latLonPoint);

			latLonPointList.add(latLonPoint);

			getAddress(latLonPoint);
		}
	}

	/**
	 * 响应逆地理编码
	 */
	public void getAddress(final LatLonPoint latLonPoint) {
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
				GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
	}

	/**
	 * 显示进度条对话框
	 */
	public void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在获取地址");
		progDialog.show();
	}

	/**
	 * 隐藏进度条对话框
	 */
	public void dismissDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	private float calculateLineDistance(LatLng startLatlng, LatLng endLatlng) {
		distance = distance
				+ AMapUtils.calculateLineDistance(startLatlng, endLatlng);
		return distance;

	}

	/**
	 * 逆地理编码回调
	 */
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				addressName = result.getRegeocodeAddress().getFormatAddress()
						+ "附近";

				adress = result.getRegeocodeAddress().getFormatAddress();

				// aMap.animateCamera(CameraUpdateFactory.zoomBy(10));

				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(latLonPoint), 15f));

				System.out.println("onRegeocodeSearched----latLonPoint:"
						+ latLonPoint);

				infoLayout.setVisibility(View.VISIBLE);
				positionTxt.setText(addressName);

				addressList.add(addressName);

				setMarker(latLonPoint);

				// regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
			} else {
				new SystemUtil().makeToast(RouteOverlayActivity.this,
						R.string.no_result + "no_result");
			}
		} else if (rCode == 27) {
			new SystemUtil().makeToast(RouteOverlayActivity.this,
					R.string.error_network + "error_network");
		} else if (rCode == 32) {
			new SystemUtil().makeToast(RouteOverlayActivity.this,
					R.string.error_key + "error_key");
		} else {
			new SystemUtil().makeToast(RouteOverlayActivity.this,
					getString(R.string.error_other) + rCode + "rCode");
		}
	}

	/**
	 * 地理编码回调
	 */
	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {

				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
						+ address.getFormatAddress();
				adress = address.getFormatAddress();
				new SystemUtil().makeToast(RouteOverlayActivity.this,
						addressName);
				infoLayout.setVisibility(View.VISIBLE);
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(address.getLatLonPoint()), 15f));

				setMarker(address.getLatLonPoint());

				// geoMarker.setPosition(AMapUtil.convertToLatLng(address
				// .getLatLonPoint()));
				positionTxt.setText(address.getFormatAddress());
				addressList.add(address.getFormatAddress().toString());

			} else {
				new SystemUtil().makeToast(RouteOverlayActivity.this,
						"对不起，没有搜索到相关地点!");
			}
		} else if (rCode == 27) {
			new SystemUtil().makeToast(RouteOverlayActivity.this,
					"搜索失败,请检查网络连接！");
		} else if (rCode == 32) {
			new SystemUtil().makeToast(RouteOverlayActivity.this, "key验证无效！");
		} else {
			new SystemUtil().makeToast(RouteOverlayActivity.this,
					"未知错误，请稍后重试!错误码为 " + rCode + "rCode");
		}
	}

	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			dismissDialog();
			finish();
		}
		return false;
	}

	@Override
	public void onBusRouteSearched(BusRouteResult arg0, int arg1) {

	}

	@Override
	public void onDriveRouteSearched(DriveRouteResult arg0, int arg1) {

	}

	/**
	 * 步行路线结果回调
	 */
	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				walkRouteResult = result;
				WalkPath walkPath = walkRouteResult.getPaths().get(0);

				walkRouteOverlay = new WalkRouteOverlayUtil(this, aMap,
						walkPath, walkRouteResult.getStartPos(),
						walkRouteResult.getTargetPos());
				walkRouteOverlay.addToMap();
				RouteOverlayAdapter.walkRouteOverlay = walkRouteOverlay;
				adapter.notifyDataSetChanged();

				calculateLineDistance(AMapUtil.convertToLatLng(walkRouteResult
						.getStartPos()), AMapUtil
						.convertToLatLng(walkRouteResult.getTargetPos()));

			} else {
				new SystemUtil().makeToast(RouteOverlayActivity.this,
						R.string.no_result + "no_result");
			}
		} else if (rCode == 27) {
			new SystemUtil().makeToast(RouteOverlayActivity.this,
					R.string.error_network + "error_network");
		} else if (rCode == 32) {
			new SystemUtil().makeToast(RouteOverlayActivity.this,
					R.string.error_key + "error_key");
		} else {
			new SystemUtil().makeToast(RouteOverlayActivity.this,
					getString(R.string.error_other) + rCode + "rCode");
		}

	}

}