package com.qizhi.qilaiqiqu.ui;
/**
 * @author Administrator
 *			加密工具
 */
public class Encryption {
	
	public static String encryptionMethod(String checkStr,String checkCode){
		String umns = getNums(checkStr,checkCode);
        System.out.println("得到字符中数字:"+umns);
        //获取最后一个数字
        Integer lastNum=Integer.parseInt(umns.substring(umns.length()-1, umns.length()));
        System.out.println("最后一个数字:"+lastNum);
        //通过最后一个数字开始截取字符传,截取10位
        String subStr=checkStr.substring(lastNum,lastNum+10);
        System.out.println("截取取值字符:"+subStr);
        //获取数字对应的字母,得到最终解析结果
        String key=getKey(subStr, umns);
        //到时候把解析得到的结果提交给后台就OK
        System.out.println(key);
        
        return key;
	}
	
	
    //获取字符中的数字 默认取前6位 不足6位返回空
    public static String getNums(String str,String defaultStr){
        String strs="";
        if (str != null && !"".equals(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    strs += str.charAt(i);
                }
                if(strs.length()==6){
                    return strs;
                }
            }
        }
        return defaultStr;
    }
    
    //获取数字对应的字母
    public static String getKey(String subStr,String umns){
        String key="";
        for (int i = 0; i < umns.length(); i++) {
            int index=Integer.parseInt(String.valueOf(umns.charAt(i)));
            key+=subStr.charAt(index);
        }
        return key;
    }
}
