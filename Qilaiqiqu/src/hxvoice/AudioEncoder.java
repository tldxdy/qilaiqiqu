package hxvoice;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.net.rtp.AudioCodec;
import android.util.Log;

public class AudioEncoder implements Runnable {
	String LOG = "AudioEncoder";

	private static AudioEncoder encoder;
	private boolean isEncoding = false;

	private List<AudioData> dataList = null;// 存放数据

	public static AudioEncoder getInstance() {
		if (encoder == null) {
			encoder = new AudioEncoder();
		}
		return encoder;
	}

	private AudioEncoder() {
		dataList = Collections.synchronizedList(new LinkedList<AudioData>());
	}

	public void addData(byte[] data, int size) {
		AudioData rawData = new AudioData();
		rawData.setSize(size);
		byte[] tempData = new byte[size];
		System.arraycopy(data, 0, tempData, 0, size);
		rawData.setRealData(tempData);
		dataList.add(rawData);
	}

	// 开始编码
	public void startEncoding() {
		System.out.println(LOG + "解码线程启动");
		if (isEncoding) {
			Log.e(LOG, "编码器已经启动，不能再次启动");
			return;
		}
		new Thread(this).start();
	}

	// 结束
	public void stopEncoding() {
		this.isEncoding = false;
	}

	public void run() {
		// 先启动发送端
		AudioSender sender = new AudioSender();
		sender.startSending();

		int encodeSize = 0;
		byte[] encodedData = new byte[256];

		// 初始化编码器
//		AudioCodec.audio_codec_init(30);

		isEncoding = true;
		while (isEncoding) {
			if (dataList.size() == 0) {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			if (isEncoding) {
				AudioData rawData = dataList.remove(0);
				encodedData = new byte[rawData.getSize()];
				//
//				encodeSize = AudioCodec.audio_encode(rawData.getRealData(), 0,
//						rawData.getSize(), encodedData, 0);
				System.out.println();
				if (encodeSize > 0) {
					sender.addData(encodedData, encodeSize);
					// 清空数据
					encodedData = new byte[encodedData.length];
				}
			}
		}
		System.out.println(LOG + "编码结束");
		sender.stopSending();
	}
}
