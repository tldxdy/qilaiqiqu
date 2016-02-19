package com.qizhi.qilaiqiqu.progress;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.qizhi.qilaiqiqu.utils.XUtilsUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
public class FileUploadAsyncTask extends AsyncTask<File, Integer, String> {

	private String url =  XUtilsUtil.URL + "common/uploadImage.html";
	private Context context;
	private File file;
	//private ProgressDialog pd;
	private long totalSize;
	//private int sum;
	//private int number;
	private SharedPreferences preferences;
	private String type;
	private Handler handler;

	public FileUploadAsyncTask(Context context, SharedPreferences preferences, String type, Handler handler) {
		this.context = context;
		//this.sum = sum;
		//this.number = number;
		this.preferences = preferences;
		this.type = type;
		this.handler = handler;
	}

	@Override
	protected void onPreExecute() {
		/*pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("上传第"+number + "/" + sum + "张");
		pd.setCancelable(false);
		pd.show();*/
	}

	@Override
	protected String doInBackground(File... params) {
		// 保存需上传文件信息
		MultipartEntityBuilder entitys = MultipartEntityBuilder.create();
		entitys.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		entitys.setCharset(Charset.forName(HTTP.UTF_8));
		file = params[0];
		entitys.addPart("files", new FileBody(file));
		entitys.addTextBody("type", type);//设置请求参数
		entitys.addTextBody("uniqueKey", preferences.getString("uniqueKey", null));//设置请求参数
		HttpEntity httpEntity = entitys.build();
		totalSize = httpEntity.getContentLength();
		ProgressOutHttpEntity progressHttpEntity = new ProgressOutHttpEntity(
				httpEntity, new ProgressListener() {
					@Override
					public void transferred(long transferedBytes) {
						publishProgress((int) (100 * transferedBytes / totalSize));
					}
				});
		return uploadFile(url, progressHttpEntity);
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		/*System.out.println("-----------------------------");
		System.out.println((int) (progress[0]));
		System.out.println("-----------------------------");*/
			//pd.setProgress((int) (progress[0]));
	}

	@Override
	protected void onPostExecute(String result) {
		//pd.dismiss();
		file.delete();
		if(result != null){
			Message msg = new Message();
			msg.what = 1;
			msg.obj = result;
			handler.handleMessage(msg);
		}else{
			Toast.makeText(context, "图片上传失败", Toast.LENGTH_SHORT).show();
		}
		
	}

	/**
	 * 上传文件到服务器
	 * 
	 * @param url
	 *            服务器地址
	 * @param entity
	 *            文件
	 * @return
	 */
	public static String uploadFile(String url, ProgressOutHttpEntity entity) {
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		// 设置连接超时时间
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 1000*60*5);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(entity);
		try {
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String s;
				String strResult = EntityUtils.toString(httpResponse.getEntity());
				JSONObject jo = new JSONObject(strResult);
				if(jo.optBoolean("result")){
					JSONArray jsonArray = jo
							.getJSONArray("dataList");
					s = jsonArray.getString(0);
				}else{
					s = null;
				}
				return s;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpClient != null && httpClient.getConnectionManager() != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}
		return null;
	}
}
