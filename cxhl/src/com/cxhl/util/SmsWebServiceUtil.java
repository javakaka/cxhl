package com.cxhl.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class SmsWebServiceUtil {

	public static int sendSms(String user_name,String password,String Mobile,String Content,String send_time) throws MalformedURLException
	{
		URL url = null;
		String CorpID="p02615";//账户名
		String Pwd="123321";//密码
		String send_content="";
		try {
			send_content = URLEncoder.encode(Content.replaceAll("<br/>", " "), "GBK");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}//发送内容
		url = new URL("http://www.512688.com/WS/Send.aspx?CorpID="+CorpID+"&Pwd="+Pwd+"&Mobile="+Mobile+"&Content="+send_content+"&Cell=&SendTime="+send_time);
		BufferedReader in = null;
		int inputLine = 0;
		try {
			System.out.println("开始发送短信手机号码为 ："+Mobile);
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			inputLine = new Integer(in.readLine()).intValue();
		} catch (Exception e) {
			System.out.println("网络异常,发送短信失败！");
			inputLine=-9;
		}
		System.out.println("结束发送短信返回值：  "+inputLine);
		return inputLine;
	}
}
