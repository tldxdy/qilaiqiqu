package com.qizhi.qilaiqiqu.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.lidroid.xutils.BitmapUtils;
import com.qizhi.qilaiqiqu.R;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 
 * @author leiqian
 * 
 */
public class SystemUtil {

	
	public final static String IMGPHTH = "http://img.weride.com.cn/";
	/**
	 * @param 提示封装方法
	 * @param context
	 *            上下文
	 * @param msg
	 *            消息内容
	 */
	public void makeToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 
	 * @return date 
	 * 				系统时间
	 */

	@SuppressLint("SimpleDateFormat")
	public static String getTime() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		return date;
	}
	/**
	 * 
	 * @param url	图片路径
	 * @param sellersmallimg	imageView控件
	 * @param context		上下文
	 */
	public static void loadImagexutils(String url, ImageView sellersmallimg,Context context) {
		BitmapUtils bitmapUtils = new BitmapUtils(context);
		bitmapUtils.display(sellersmallimg, IMGPHTH + url);
	}
	
	/**
	 * 
	 * @param url	图片路径
	 * @param sellersmallimg	imageView控件
	 * @param context		上下文
	 */
	public static void Imagexutils(String url, ImageView sellersmallimg,Context context) {

		@SuppressWarnings("deprecation")
		int screenWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽（像素，如：480px）  
		String[] s = url.split("@");
		if(s.length == 2){
			String ss = s[1].substring(1, s[1].length());
			String w = ss.split("h")[0].trim();
			String h = ss.split("h")[1].trim();
			int ww = Integer.parseInt(w);
			int hh = Integer.parseInt(h);
			//System.out.println(ss.split("h")[0] + ss.split("h")[1]);
			Picasso.with(context).load(IMGPHTH + s[0])
			.resize(screenWidth, hh * screenWidth / ww).centerInside()
			.placeholder(R.drawable.bitmap_homepage)
			.error(R.drawable.bitmap_homepage)
			.into(sellersmallimg);
		}else{
			String uri = UrlSize(url);
			Picasso.with(context).load(IMGPHTH + uri)
			.placeholder(R.drawable.bitmap_homepage)
			.error(R.drawable.bitmap_homepage)
			.into(sellersmallimg);
		}
		
	}
	
	
	
	/**
	 * @param srcPath图片路径
	 * @return 压缩后的bitmap
	 */
	public static Bitmap compressImageFromFile(String srcPath, float ww) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        newOpts.inJustDecodeBounds = true;//只读边,不读内容  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
  
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        //现在主流手机比较多是800*480分辨率
/*        float hh = 800f;//  
        float ww = 480f;//  
*/        int be = 1;  
        if (w > ww) {  
            be = (int) (newOpts.outWidth / ww );  
        }
        if (be <= 0)  
            be = 1; 
        
        newOpts.inSampleSize = be;//设置采样率  
          
        newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设  
        newOpts.inPurgeable = true;// 同时设置才会有效  
        newOpts.inInputShareable = true;//当系统内存不够时候图片自动被回收  
          
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
		
        return bitmap;  
    }
	public static String UrlSize(String url){
//		USER_201602261033021750_160_160_11.jpg
		String[] ss = url.split("_");
		if(ss.length == 5){
			int w = Integer.parseInt(ss[2]);
			int h = Integer.parseInt(ss[3]);
			//int size = Integer.parseInt(ss[4].split(".")[0]);
			if(w > 1024 || h > 1024){
				if(w > h){
					h = (int) (h * 1.0 * 1024 / w);
					w = 1024;
				}else{
					w = (int) (w * 1.0 * 1024 / h);
					h = 1024;
				}
				return url + "@" + w + "w_" + h + "h"; 
			}
		}
		return url;
		
	}

	
	@SuppressLint("NewApi")
	public String saveMyBitmap(Bitmap mBitmap) throws IOException {
		File outDir = null;
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			// 这个路径，在手机内存下创建一个pictures的文件夹，把图片存在其中。
			outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		}else{
			outDir = new File(state);
			//return null;
		}
		if (!outDir.exists()) {
			outDir.mkdirs();
		}
		outDir = new File(outDir, System.currentTimeMillis() + ".jpg");
		String s = outDir.toString();
		/*ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
		if(baos.toByteArray().length / 1024 > 400){
			FileOutputStream fos = new FileOutputStream(outDir);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);  
		}else{
			FileOutputStream fos = new FileOutputStream(outDir);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);  
		}*/
		
		FileOutputStream fos = new FileOutputStream(outDir);
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);  
		fos.flush();
		fos.close();
		
		if(mBitmap != null && !mBitmap.isRecycled()){  
		    // 回收并且置为null  
			mBitmap.recycle();  
			mBitmap = null;  
		}  
		System.gc();
		
		return s;
	}
	

	/**
	 * 
	 * @param image  图片
	 * @param imageSize  压缩大小
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image , Integer imageSize) {  
		  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 90, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100; 

        while ( baos.toByteArray().length / 1024 > imageSize) {  
            baos.reset();//重置baos即清空baos  
            options = options - 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
        }  

        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    }
	
	

	public void httpClient(final String img_path, final SharedPreferences preferences, final Handler handler,final String type){
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpClient client=new DefaultHttpClient();
				client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000); 
				HttpPost post = new HttpPost(XUtilsUtil.URL + "common/uploadImage.html");//创建 HTTP POST 请求  
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				//builder.setCharset(Charset.forName("uft-8"));//设置请求的编码格式
				builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
				System.out.println("=============================");
				System.out.println(img_path);
				System.out.println("=============================");
				File file = new File(img_path);
				FileBody fileBody = new FileBody(file);//把文件转换成流对象FileBody
				builder.addPart("files", fileBody);
				builder.addTextBody("type", type);//设置请求参数
				builder.addTextBody("uniqueKey", preferences.getString("uniqueKey", null));//设置请求参数
				HttpEntity entity = builder.build();// 生成 HTTP POST 实体  	
				post.setEntity(entity);//设置请求参数
				HttpResponse response;
				try {
					response = client.execute(post);
					// 发起请求 并返回请求的响应
					if (response.getStatusLine().getStatusCode()==200) {
						String strResult = EntityUtils.toString(response.getEntity());
						JSONObject jo = new JSONObject(strResult);
						System.out.println(jo.toString());
						if(jo.optBoolean("result")){
							JSONArray jsonArray = jo
									.getJSONArray("dataList");
							String s = jsonArray.getString(0);
							Message msg = new Message();
							msg.what = 1;
							msg.obj = s;
							file.delete();
							handler.handleMessage(msg);
						}else{
							Message msg = new Message();
							msg.what = 2;
							msg.obj = jo.opt("message");
							file.delete();
							handler.handleMessage(msg);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
