package com.cxhl.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import com.ezcloud.framework.util.StringUtils;
import com.shcm.send.OpenApi;

public class SmsWebServiceUtil {

	private static String sOpenUrl = "http://smsapi.c123.cn/OpenPlatform/OpenApi";
	private static String sDataUrl = "http://smsapi.c123.cn/DataPlatform/DataApi";
	// 接口帐号
	private static  String account = "1001@501159790001";
	// 接口密钥
	private static  String authkey = "81382AE196192291E60D6922AF46A277";
	// 通道组编号
	private static  int cgid = 52;
	// 默认使用的签名编号(未指定签名编号时传此值到服务器)
	private static  int csid = 0;
	
	public static int sendSms(String user_name,String password,String Mobile,String Content,String send_time) throws MalformedURLException
	{
		URL url = null;
//		String CorpID="p02615";//账户名
//		String Pwd="123321";//密码
		String CorpID=user_name;//账户名
		String Pwd=password;//密码
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
	
	public static int sendC123Sms(String db_sOpenUrl,String db_account, String db_authkey,int db_cgid,int db_csid,String sTelephone,String sContent) 
	{
		if(StringUtils.isEmptyOrNull(db_sOpenUrl) && !db_sOpenUrl.equals(sOpenUrl))
		{
			sOpenUrl =db_sOpenUrl;
		}
		if(StringUtils.isEmptyOrNull(db_account) && !db_account.equals(account))
		{
			account =db_account;
		}
		if(StringUtils.isEmptyOrNull(db_authkey) && !db_authkey.equals(authkey))
		{
			authkey =db_authkey;
		}
		if(db_cgid !=cgid )
		{
			cgid =db_cgid;
		}
		if(db_csid !=csid )
		{
			csid =db_csid;
		}
		OpenApi.initialzeAccount(sOpenUrl, account, authkey, cgid, csid);
		String sSend ="";
		int nRet =100;
		try {
//			sSend = new String(sContent.getBytes(), "UTF-8");
			sSend = new String(sContent.getBytes(), System.getProperty("file.encoding"));
		} catch (UnsupportedEncodingException e) {
			nRet =-1;
			return nRet;
		}
		nRet = OpenApi.sendOnce(sTelephone, sSend, 0, 0, null);
//		if(nRet > 0)
//		{
//			System.out.println("发送成功!");
//		}
//		else
//		{
//			System.out.println("发送失败! => " + nRet);
//		}
		return nRet;
	}
	
	public static void main(String[] args) {
//		String sms_content ="1074吃香喝辣带您免费吃遍南宁，感谢您的注册，您的验证码是：118966【吃香喝辣】";
//		try {
////			int num =sendSms("p02615", "150727", "13826531136", sms_content, "");
////			int num =sendSms("p02615", "150727", "15877126966", sms_content, "");
////			System.out.println("---------->>"+num);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
		System.out.println("---------->>"+System.getProperty("file.encoding") );
	}
}
