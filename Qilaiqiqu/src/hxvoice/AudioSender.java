package hxvoice;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AudioSender implements Runnable {  
    String LOG = "AudioSender ";  
  
    private boolean isSendering = false;  
    private List<AudioData> dataList;  
  
    DatagramSocket socket;  
    DatagramPacket dataPacket;  
    private InetAddress ip;  
    private int port;  
  
    public AudioSender() {  
        dataList = Collections.synchronizedList(new LinkedList<AudioData>());  
        try {  
            try {  
                ip = InetAddress.getByName(MyConfig.SERVER_HOST);  
                this.port = MyConfig.SERVER_PORT;  
                socket = new DatagramSocket();  
            } catch (UnknownHostException e) {  
                e.printStackTrace();  
            }  
        } catch (SocketException e) {  
            e.printStackTrace();  
        }  
    }  
  
    // 添加数据  
    public void addData(byte[] data, int size) {  
        AudioData encodedData = new AudioData();  
        encodedData.setSize(size);  
        byte[] tempData = new byte[size];  
        System.arraycopy(data, 0, tempData, 0, size);  
        encodedData.setRealData(tempData);  
        dataList.add(encodedData);  
    }  
  
    // 发送数据  
    private void sendData(byte[] data, int size) {  
        try {  
            dataPacket = new DatagramPacket(data, size, ip, port);  
            dataPacket.setData(data);  
            socket.send(dataPacket);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
  
    // 开始发送  
    public void startSending() {  
        System.out.println(LOG + "发送线程启动");  
        new Thread(this).start();  
    }  
  
    // 停止发送  
    public void stopSending() {  
        this.isSendering = false;  
    }  
  
    // run  
    public void run() {  
        this.isSendering = true;  
        System.out.println(LOG + "开始发送数据");  
        while (isSendering) {  
            if (dataList.size() > 0) {  
                AudioData encodedData = dataList.remove(0);  
                sendData(encodedData.getRealData(), encodedData.getSize());  
            }  
        }  
        System.out.println(LOG + "发送结束");  
    }  
}
