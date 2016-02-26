package hxvoice;

public class MyConfig {  
    public static String SERVER_HOST = "192.168.1.130";// 服务器的IP  
    public static final int SERVER_PORT = 5656;// 服务器的监听端口  
    public static final int CLIENT_PORT = 5757;//  
  
    public static final int AUDIO_STATUS_RECORDING = 0;//手机端的状态：录音 or 播放  
    public static final int AUDIO_STATUS_LISTENING = 1;  
  
    public static void setServerHost(String ip) {  
        System.out.println("修改后的服务器网址为  " + ip);  
        SERVER_HOST = ip;  
    }  
}
