package com.qizhi.qilaiqiqu.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.lidroid.xutils.BitmapUtils;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.os.Environment;
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
	public static Bitmap compressImageFromFile(String srcPath) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        newOpts.inJustDecodeBounds = true;//只读边,不读内容  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
  
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        //现在主流手机比较多是800*480分辨率
        float hh = 800f;//  
        float ww = 480f;//  
        int be = 1;  
        if (w > h && w > ww) {  
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {  
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be;//设置采样率  
          
        newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设  
        newOpts.inPurgeable = true;// 同时设置才会有效  
        newOpts.inInputShareable = true;//当系统内存不够时候图片自动被回收  
          
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
		
        return compressImage(bitmap, 200);  
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
}
