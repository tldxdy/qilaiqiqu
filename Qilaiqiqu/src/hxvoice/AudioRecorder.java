package hxvoice;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioRecorder implements Runnable {  
	  
    String LOG = "Recorder ";  
  
    private boolean isRecording = false;  
    private AudioRecord audioRecord;  
  
    private static final int audioSource = MediaRecorder.AudioSource.MIC;  
    private static final int sampleRate = 8000;  
    private static final int channelConfig = AudioFormat.CHANNEL_IN_MONO;  
    private static final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;  
    private static final int BUFFER_FRAME_SIZE =960;  
    private int audioBufSize = 0;  
  
    //  
    private byte[] samples;// 缓冲区  
    private int bufferRead = 0;// 从recorder中读取的samples的大小  
  
    private int bufferSize = 0;// samples的大小  
  
    // 开始录制  
    public void startRecording() {  
        bufferSize = BUFFER_FRAME_SIZE;  
  
        audioBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig,  
                audioFormat);  
        if (audioBufSize == AudioRecord.ERROR_BAD_VALUE) {  
            Log.e(LOG, "audioBufSize error");  
            return;  
        }  
        samples = new byte[audioBufSize];  
        // 初始化recorder  
        if (null == audioRecord) {  
            audioRecord = new AudioRecord(audioSource, sampleRate,  
                    channelConfig, audioFormat, audioBufSize);  
        }  
        new Thread(this).start();  
    }  
  
    // 停止录制  
    public void stopRecording() {  
        this.isRecording = false;  
    }  
  
    public boolean isRecording() {  
        return isRecording;  
    }  
  
    // run  
    public void run() {  
        // 录制前，先启动解码器  
        AudioEncoder encoder = AudioEncoder.getInstance();  
        encoder.startEncoding();  
  
        System.out.println(LOG + "audioRecord startRecording()");  
        audioRecord.startRecording();  
  
        this.isRecording = true;  
        while (isRecording) {  
            bufferRead = audioRecord.read(samples, 0, bufferSize);  
            if (bufferRead > 0) {  
                // 将数据添加给解码器  
                encoder.addData(samples, bufferRead);  
            }  
  
            try {  
                Thread.sleep(20);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
        }  
        System.out.println(LOG + "录制结束");  
        audioRecord.stop();  
        encoder.stopEncoding();  
    }  
}