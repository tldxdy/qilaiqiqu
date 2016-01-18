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

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.lidroid.xutils.BitmapUtils;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

	/**
	 * @param 提示封装方法
	 * @param a
	 *            上下文
	 * @param msg
	 *            消息内容
	 */
	public void makeToast(Activity a, String msg) {
		Toast.makeText(a, msg, Toast.LENGTH_SHORT).show();
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
		bitmapUtils.display(sellersmallimg, "http://weride.oss-cn-hangzhou.aliyuncs.com/"+url);
	}
	
	/**
	 * 
	 * @param url	图片路径
	 * @param sellersmallimg	imageView控件
	 * @param context		上下文
	 */
	public static void Imagexutils(String url, ImageView sellersmallimg,Context context) {
		Picasso.with(context).load("http://weride.oss-cn-hangzhou.aliyuncs.com/"+url).into(sellersmallimg);
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
            be = (int) (newOpts.outWidth / ww);  
        }
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be;//设置采样率  
          
        newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设  
        newOpts.inPurgeable = true;// 同时设置才会有效  
        newOpts.inInputShareable = true;//当系统内存不够时候图片自动被回收  
          
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
		
        return compressImage(bitmap, 1024);  
    }
	/**
	 * 
	 * @param image  图片
	 * @param imageSize  压缩大小
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image , Integer imageSize) {  
		  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
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
		
		FileOutputStream fos = new FileOutputStream(outDir);
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);  
		fos.flush();
		fos.close();
		
		return s;
	}
	
	/**
	* 获取压缩后的图片
	* @param res
	* @param resId
	* @param reqWidth            所需图片压缩尺寸最小宽度
	* @param reqHeight           所需图片压缩尺寸最小高度
	* @return
	*/
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

		// 首先不加载图片,仅获取图片尺寸
		final BitmapFactory.Options options = new BitmapFactory.Options();
		// 当inJustDecodeBounds设为true时,不会加载图片仅获取图片尺寸信息
		options.inJustDecodeBounds = true;
		// 此时仅会将图片信息会保存至options对象内,decode方法不会返回bitmap对象
		BitmapFactory.decodeResource(res, resId, options);

		// 计算压缩比例,如inSampleSize=4时,图片会压缩成原图的1/4
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// 当inJustDecodeBounds设为false时,BitmapFactory.decode...就会返回图片对象了
		options.inJustDecodeBounds = false;
		// 利用计算的比例值获取压缩后的图片对象
		return BitmapFactory.decodeResource(res, resId, options);
	}
	/**
	* 计算压缩比例值
	* @param options       解析图片的配置信息
	* @param reqWidth            所需图片压缩尺寸最小宽度
	* @param reqHeight           所需图片压缩尺寸最小高度
	* @return
	*/
	public	static	int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// 保存图片原宽高值
			final int height = options.outHeight;
			final int width = options.outWidth;
			// 初始化压缩比例为1
			int inSampleSize = 1;

			// 当图片宽高值任何一个大于所需压缩图片宽高值时,进入循环计算系统
         	if (height > reqHeight || width > reqWidth) {

				final int halfHeight = height / 2;
				final int halfWidth = width / 2;

				// 压缩比例值每次循环两倍增加,
				// 直到原图宽高值的一半除以压缩值后都~大于所需宽高值为止
				while ((halfHeight / inSampleSize) >= reqHeight
							&& (halfWidth / inSampleSize) >= reqWidth) {
						inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
	

	public void httpClient(final String img_path, final SharedPreferences preferences, final Handler handler,final String type){
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpClient client=new DefaultHttpClient();
				client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000); 
				HttpPost post = new HttpPost("http://120.55.195.170:80/common/uploadImage.html");//创建 HTTP POST 请求  
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				//builder.setCharset(Charset.forName("uft-8"));//设置请求的编码格式
				builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
				FileBody fileBody = new FileBody(new File(img_path));//把文件转换成流对象FileBody
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
						if(jo.optBoolean("result")){
							JSONArray jsonArray = jo
									.getJSONArray("dataList");
							String s = jsonArray.getString(0);
							Message msg = new Message();
							msg.what = 1;
							msg.obj = s;
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
	
}
