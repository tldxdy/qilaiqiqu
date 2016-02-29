package com.qizhi.qilaiqiqu.activity;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v4.app.NotificationCompat;

import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.Type;
import com.easemob.util.EasyUtils;
import com.qizhi.qilaiqiqu.model.CityModel;
import com.qizhi.qilaiqiqu.model.DistrictModel;
import com.qizhi.qilaiqiqu.model.ProvinceModel;
import com.qizhi.qilaiqiqu.service.XmlParserHandler;
import com.qizhi.qilaiqiqu.utils.CommonUtils;

/**
 * @author leiqian 地图基类
 */
public class BaseActivity extends Activity {

	private static final int notifiId = 11;
	protected NotificationManager notificationManager;
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
			// */
			mProvinceDatas = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				// ��������ʡ������
				mProvinceDatas[i] = provinceList.get(i).getName();
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					// ����ʡ����������е�����
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j)
							.getDistrictList();
					String[] distrinctNameArray = new String[districtList
							.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList
							.size()];
					for (int k = 0; k < districtList.size(); k++) {
						// ����������������/�ص�����
						DistrictModel districtModel = new DistrictModel(
								districtList.get(k).getName(), districtList
										.get(k).getZipcode());
						// ��/�ض��ڵ��ʱ࣬���浽mZipcodeDatasMap
						mZipcodeDatasMap.put(districtList.get(k).getName(),
								districtList.get(k).getZipcode());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					// ��-��/�ص����ݣ����浽mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				// ʡ-�е����ݣ����浽mCitisDatasMap
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}
	
	
	/**
     * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下 如果不需要，注释掉即可
     * 
     * @param message
     */
    protected void notifyNewMessage(EMMessage message) {
        // 如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
        // 以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
        if (!EasyUtils.isAppRunningForeground(this)) {
            return;
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(getApplicationInfo().icon)
                .setWhen(System.currentTimeMillis()).setAutoCancel(true);

        String ticker = CommonUtils.getMessageDigest(message, this);
        if (message.getType() == Type.TXT)
            ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
        // 设置状态栏提示
        mBuilder.setTicker(message.getFrom() + ": " + ticker);

        // 必须设置pendingintent，否则在2.3的机器上会有bug
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notifiId,
                intent, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pendingIntent);

        Notification notification = mBuilder.build();
        notificationManager.notify(notifiId, notification);
        notificationManager.cancel(notifiId);
    }

}
