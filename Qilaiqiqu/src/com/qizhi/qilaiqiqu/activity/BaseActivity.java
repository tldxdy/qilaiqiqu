package com.qizhi.qilaiqiqu.activity;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.app.NotificationManager;
import android.content.res.AssetManager;

import com.qizhi.qilaiqiqu.model.CityModel;
import com.qizhi.qilaiqiqu.model.DistrictModel;
import com.qizhi.qilaiqiqu.model.ProvinceModel;
import com.qizhi.qilaiqiqu.service.XmlParserHandler;
import com.umeng.analytics.MobclickAgent;

/**
 * @author leiqian 基类
 */
public class BaseActivity extends HuanxinLogOutActivity {

	/**
	 * ����ʡ
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - ʡ value - ��
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - �� values - ��
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - �� values - �ʱ�
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

	/**
	 * ��ǰʡ������
	 */
	protected String mCurrentProviceName;
	/**
	 * ��ǰ�е�����
	 */
	protected String mCurrentCityName;
	/**
	 * ��ǰ��������
	 */
	protected String mCurrentDistrictName = "";

	/**
	 * ��ǰ������������
	 */
	protected String mCurrentZipCode = "";

	/**
	 * ����ʡ������XML����
	 */
	private static final int notifiId = 11;
	protected NotificationManager notificationManager;

	protected void initProvinceDatas(AssetManager asset) {
		List<ProvinceModel> provinceList = null;
		try {
			InputStream input = asset.open("province_data.xml");
			// ����һ������xml�Ĺ�������
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// ����xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// ��ȡ��������������
			provinceList = handler.getDataList();
			// */ ��ʼ��Ĭ��ѡ�е�ʡ���С���
			if (provinceList != null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList != null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0)
							.getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			mProvinceDatas = new String[provinceList.size()];
			System.err.println(mProvinceDatas);
			for (int i = 0; i < provinceList.size(); i++) {
				mProvinceDatas[i] = provinceList.get(i).getName();
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j)
							.getDistrictList();
					String[] distrinctNameArray = new String[districtList
							.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList
							.size()];
					for (int k = 0; k < districtList.size(); k++) {
						DistrictModel districtModel = new DistrictModel(
								districtList.get(k).getName(), districtList
										.get(k).getZipcode());
						mZipcodeDatasMap.put(districtList.get(k).getName(),
								districtList.get(k).getZipcode());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
