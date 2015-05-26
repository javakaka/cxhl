package com.fbsf.fzb.test;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.Md5Util;
import com.ezcloud.framework.vo.IVO;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.MD5;
import com.ezcloud.utility.NetUtil;

/**
 * 房东租客版测试类
 * @author Administrator
 *
 */
public class ApiTest {

	//查询版本
	public static void getVersion()
	{
		String url ="http://localhost:8080/fangzubao/api/core/version/lastest.do";
//		String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/version/lastest.do";
		IVO ivo =new IVO();
		   try {
			   //1房租宝房东租客版2中介版
				ivo.set("app", "1");
				//1 ios 2 android 3 wp
				ivo.set("device", "1");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n 加密前 ivo to json ====>>"+json);
				//加密
				json =AesUtil.encode(json);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("\n response json ====>> \n");
				System.out.print(res);
				res = AesUtil.decode(res);
				System.out.println("\n decode response json ===========>>\n"+res);
		   } catch (JException e) {
				e.printStackTrace();
			}
		   catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	//发送短信验证码
	public static void sendSms()
	{
//		String url ="http://localhost:8080/fangzubao/api/core/sms/send.do";
		String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/sms/send.do";
		IVO ivo =new IVO();
		   try {
			   //手机号
				ivo.set("telephone", "13826531136");
				//1 发送短信 0不发送短信
				ivo.set("type", "0");
				ivo.set("type", "1");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n 加密前 ivo to json ====>>"+json);
				//加密
				json =AesUtil.encode(json);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("\n response json ====>> \n");
				System.out.print(res);
				res = AesUtil.decode(res);
				System.out.println("\n decode response json ===========>>\n"+res);
		   } catch (JException e) {
				e.printStackTrace();
			}
		   catch (Exception e) {
				e.printStackTrace();
			}
	}
		
	//登陆
	public static void login()
	{
//		String url ="http://localhost:8080/fangzubao/api/core/action/login.do";
		String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/action/login.do";
		IVO ivo =new IVO();
		   try {
				ivo.set("username", "13826531136");
				ivo.set("password", "670B14728AD9902AECBA32E22FA4F6BD");
				//客户端版本
				ivo.set("version", "1.1");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n 加密前 ivo to json ====>>"+json);
				//加密
				json =AesUtil.encode(json);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("\n response json ====>> \n");
				System.out.print(res);
				res = AesUtil.decode(res);
				System.out.println("\n decode response json ===========>>\n"+res);
		   } catch (JException e) {
				e.printStackTrace();
			}
		   catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	//登陆
	public static void login_with_incrypt_str()
	{
//		String url ="http://localhost:8080/fangzubao/api/core/action/login.do";
		String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/action/login.do";
		IVO ivo =new IVO();
		   try {
				String json ="Ngn1aWQ+u1siNR+rQkMMkJbWYIGCZ1TsoMykxSMNwq5o1DFiS1vO3s59YYanc+CASepqMi7TfTfpN2MsyvLyPPk9VokIKztnrCqk6Z57gy9HqH6DxmEDF+xD2kUbry/iVIUs9IdX73hXxPavHAZ7Z72ChNndvIRlHwtCwy2JvlXIHv3ahbFTxYfsvMZorzBnb3c20u1fTaJz6poV7RcIQMmvgz2tlj23kSoEXwvuYdy6n9d+Wup022OiuLFXQRpz8LieXjsO0rcWho5+P/A1N30S3zSlSTAwSG1sho6S69CgKnsFUtscIME2wgmdMnMnTuTw8cd0MkRiuA5y8mmWH39sx3qbPuESrcj2ouXqP2eqjzzs9GfNTUy3sOnkcnjyvukUxrymScxO3FjHCUjxSjmosN7PbvWAEShAo9Rh2f62q489U1bldH7qnSdAwKUQzDRXgBbiuDABybn0kgosAFTM2MNWo/KYISBKgch7NaeB3Rwoj0cfMcCR26V+dGZ1AFruTcziqSfqy1E/MyINtw5NBTTp8oto1xlAV0xNQ4Upcp6YapkopDk2NVZhCc7DAhaBMkQZRFRUXV6WDWED7vdI37pt0EiOA/nJJTKN6hN5/33VXcF7OaInwGlOVAaR";
				System.out.println("\n  json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("\n response json ====>> \n");
				System.out.print(res);
				res = AesUtil.decode(res);
				System.out.println("\n decode response json ===========>>\n"+res);
		   } catch (JException e) {
				e.printStackTrace();
			}
		   catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	//查询用户基本信息
	public static void queryProfile()
	{
//		String url ="http://localhost:8080/fangzubao/api/core/action/profile.do";
			String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/action/profile.do";
		IVO ivo =new IVO();
		   try {
				ivo.set("id", "1");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n 加密前 ivo to json ====>>"+json);
				//加密
				json =AesUtil.encode(json);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("\n response json ====>> \n");
				System.out.print(res);
				res = AesUtil.decode(res);
				System.out.println("\n decode response json ===========>>\n"+res);
		   } catch (JException e) {
				e.printStackTrace();
			}
		   catch (Exception e) {
				e.printStackTrace();
			}
	}
	//修改密码
	public static void changePassword()
	{
		String url ="http://localhost:8080/fangzubao/api/core/action/changePassword.do";
		IVO ivo =new IVO();
		   try {
				ivo.set("user_id", "1");
				ivo.set("oldPwd", "E10ADC3949BA59ABBE56E057F20F883E");//123456
				ivo.set("newPwd", "670B14728AD9902AECBA32E22FA4F6BD");//000000
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n 加密前 ivo to json ====>>"+json);
				//加密
				json =AesUtil.encode(json);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("\n response json ====>> \n");
				System.out.print(res);
				res = AesUtil.decode(res);
				System.out.println("\n decode response json ===========>>\n"+res);
		   } catch (JException e) {
				e.printStackTrace();
			}
		   catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	//注册
	public static void register()
	{
//		String url ="http://localhost:8080/fangzubao/api/core/action/register.do";
		String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/action/register.do";
		IVO ivo =new IVO();
	   try {
			ivo.set("telephone", "13826531136");
			ivo.set("password", "E10ADC3949BA59ABBE56E057F20F883E");
			ivo.set("sms_code", "577263");
			ivo.set("device", "1");
			ivo.set("device_code", "1235173571357562312");
			//客户端版本
			ivo.set("version", "1.0");
			String json =  VOConvert.ivoToJson(ivo);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//通过邮箱重置密码
	public static void resetPassword()
	{
		String url ="http://localhost:8080/fangzubao/api/core/action/resetPassword.do";
		IVO ivo =new IVO();
		   try {
				ivo.set("email", "510836102@qq.com");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				//加密
				json =AesUtil.encode(json);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("\n response json ====>> \n");
				System.out.print(res);
				res = AesUtil.decode(res);
				System.out.println("\n decode response json ===========>>\n"+res);
		   } catch (JException e) {
				e.printStackTrace();
			}
		   catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	//修改个人信息
	public static void updateProfile()
	{
		String url ="http://localhost:8080/fangzubao/api/core/action/updateProfile.do";
		IVO ivo =new IVO();
		   try {
				ivo.set("id", "1");
				ivo.set("name", "小童");
				ivo.set("email", "510836102@qq.com");
				ivo.set("username", "tong");
//				ivo.set("bank_card_no", "6222021001116245703");
//				ivo.set("credit_card_no", "6222021001116245708");
				ivo.set("address", "香景大厦");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				//加密
				json =AesUtil.encode(json);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("\n response json ====>> \n");
				System.out.print(res);
				res = AesUtil.decode(res);
				System.out.println("\n decode response json ===========>>\n"+res);
		   } catch (JException e) {
				e.printStackTrace();
			}
		   catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	
	public static void mailtest()
	{
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();  
		mailSender.setHost("smtp.qq.com");  
		mailSender.setUsername("1914662148@qq.com");  
		mailSender.setPassword("1989125tjb"); 
		SimpleMailMessage smm = new SimpleMailMessage();  
		// 设定邮件参数  
		smm.setFrom(mailSender.getUsername());  
		smm.setTo("dabao1989125@163.com");
		smm.setSubject("Hello world");
		smm.setText("Hello world via spring mail sender");  
		// 发送邮件  
		System.out.println("start send .......");
		mailSender.send(smm);
		System.out.println("end send .......");
	}
	
	/**
	 * 查询所有的城市和城市所辖区域
	 */
	public static void queryCityAndZone()
	{
	String url ="http://localhost:8080/fangzubao/api/core/zone/CityAndZone.do";
	IVO ivo =new IVO();
	try {
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 查询广告列表
	 */
	public static void queryAllAds()
	{
	String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/ad/list.do";
//	String url ="http://localhost:8080/fangzubao/api/core/ad/list.do";
	IVO ivo =new IVO();
	try {
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询广告详情
	 */
	public static void queryAdDetail()
	{
	String url ="http://localhost:8080/fangzubao/api/core/ad/find.do";
//	String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/ad/find.do";
	IVO ivo =new IVO();
	try {
			ivo.set("id", "3");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
			OVO ovo =VOConvert.jsonToOvo(res);
			System.out.println("ovo.oForm=====>>"+ovo.oForm);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 房东发布房源/未确认
	 */
	public static void addRoom()
	{
//	String url ="http://localhost:8080/fangzubao/api/core/room/add.do";
	String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/room/add.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "1");
			ivo.set("province_id", "19");
			ivo.set("city_id", "202");
			ivo.set("zone_id", "1956");
			ivo.set("address", "吉华大厦");
			ivo.set("area", "80");
			ivo.set("start_date", "2015-02-01");
			ivo.set("end_date", "2016-02-01");
			ivo.set("pay_day", "02");
			ivo.set("monthly_rent", "10000");
			ivo.set("deposit", "10000");
			ivo.set("invite_code", "");
			ivo.set("id", "1");
//			ivo.set("invite_code", "13826531136");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 房东发布待租房源
	 */
	public static void addCreateRoom()
	{
//	String url ="http://localhost:8080/fangzubao/api/core/room/add_create.do";
	String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/room/add_create.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "1");
			ivo.set("province_id", "19");
			ivo.set("city_id", "202");
			ivo.set("zone_id", "1956");
			ivo.set("address", "吉华大厦");
			ivo.set("area", "80");
			ivo.set("user_name", "tong");
			ivo.set("telephone", "13826531136");
			ivo.set("id_card_no", "430528198902101311");
			ivo.set("monthly_rent", "10000");
			ivo.set("deposit", "10000");
//			ivo.set("id", "14");
//			ivo.set("invite_code", "13826531136");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 房东立即放租
	 */
	public static void addPublishRoom()
	{
	String url ="http://localhost:8080/fangzubao/api/core/room/add_publish.do";
//	String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/room/add.do";
	IVO ivo =new IVO();
	try {
			ivo.set("id", "15");
			ivo.set("monthly_rent", "10000");
			ivo.set("deposit", "24000");
			ivo.set("start_date", "2015-04-03");
			ivo.set("end_date", "2016-04-03");
			ivo.set("pay_day", "15");
			ivo.set("invite_code", "13826531136");
			ivo.set("water_num", "1000");
			ivo.set("electricity_num", "800");
			ivo.set("gas_num", "800");
			ivo.set("property", "100");
			ivo.set("remark", "福田中心区优质房源");
//			ivo.set("invite_code", "13826531136");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 房东发布房源后查询房源详情
	 */
	public static void landlordFind()
	{
	String url ="http://localhost:8080/fangzubao/api/core/room/landlord_find.do";
	IVO ivo =new IVO();
	try {
			ivo.set("id", "15");
//			ivo.set("id", "10");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 房东确认出租
	 */
	public static void landlordConfirm()
	{
	String url ="http://localhost:8080/fangzubao/api/core/room/landlord_confirm.do";
	IVO ivo =new IVO();
	try {
			ivo.set("id", "15");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 绑定银行卡
	 */
	public static void bindCard()
	{
	String url ="http://localhost:8080/fangzubao/api/core/action/bind_card.do";
	IVO ivo =new IVO();
	try {
			ivo.set("id", "1");
			ivo.set("card_no", "6222021001116245906");
			ivo.set("user_name", "童先生");
//			ivo.set("password", Md5Util.Md5("000000"));
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 租客根据房源唯一码查询承租房源详情
	 */
	public static void agentFindRoom()
	{
	String url ="http://localhost:8080/fangzubao/api/core/room/agent_find.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "2");
			ivo.set("code", "100014");
			ivo.set("name", "test");
			ivo.set("telephone", "13826531135");
			ivo.set("id_card_no", "430528198902101310");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 租客确认承租房源
	 */
	public static void agentConfirm()
	{
	String url ="http://localhost:8080/fangzubao/api/core/room/agent_confirm.do";
//	String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/room/agent_confirm.do";
	IVO ivo =new IVO();
	try {
			ivo.set("id", "15");
			ivo.set("present", "1");
			ivo.set("user_id", "2");
			ivo.set("address", "收货地址");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 房东查询自己的出租房源列表
	 */
	public static void listLandlordRoom()
	{
//	String url ="http://localhost:8080/fangzubao/api/core/room/list.do";
	String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/room/list.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "19");
			ivo.set("user_id", "1");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 房东查询自己的出租房源列表-出租中和已结束状态的房源
	 */
	public static void listLandlordUsingRoom()
	{
	String url ="http://localhost:8080/fangzubao/api/core/room/using_list.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "1");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 房东查询出租房源详情
	 */
	public static void queryLandlordRoomDetail()
	{
	String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/room/detail.do";
//	String url ="http://localhost:8080/fangzubao/api/core/room/detail.do";
	IVO ivo =new IVO();
	try {
			ivo.set("id", "47");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 房东删除待出租房源
	 */
	public static void landlordDeleteRoom()
	{
	String url ="http://localhost:8080/fangzubao/api/core/room/delete.do";
	IVO ivo =new IVO();
	try {
			ivo.set("id", "1");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 房东申请终止委托
	 */
	public static void landlordStopRoom()
	{
	String url ="http://localhost:8080/fangzubao/api/core/room/stop.do";
//	String url ="http://113.105.76.195:8080/fangzubao/api/core/room/stop.do";
	IVO ivo =new IVO();
	try {
			ivo.set("id", "15");
			ivo.set("date", "2015-05-11");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 房东修改银行账号
	 */
	public static void changeAccount()
	{
	String url ="http://localhost:8080/fangzubao/api/core/action/changeAccount.do";
	IVO ivo =new IVO();
	try {
			ivo.set("id", "1");
			ivo.set("password", Md5Util.Md5("000000"));
			ivo.set("card_no", "6222021001116245703");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 重新放租，查询房源信息
	 */
	public static void re_rent()
	{
	String url ="http://localhost:8080/fangzubao/api/core/room/re_rent.do";
	IVO ivo =new IVO();
	try {
			ivo.set("id", "2");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 租客绑定支付银行卡
	 */
	public static void bindCreditCard()
	{
	String url ="http://localhost:8080/fangzubao/api/core/action/bind_credit_card.do";
	IVO ivo =new IVO();
	try {
			ivo.set("id", "1");
			ivo.set("card_no", "62200055513213123322266");
			ivo.set("user_name", "tong");
			ivo.set("password", Md5Util.Md5("000000"));
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 查询租客的租房列表
	 */
	public static void rentRoomList()
	{
//	String url ="http://localhost:8080/fangzubao/api/core/rent/room_list.do";
	String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/rent/room_list.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "7");
//			ivo.set("user_id", "16");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 租客查询的房源详情
	 */
	public static void rentRoomDetail()
	{
	String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/rent/room_detail.do";
//	String url ="http://localhost:8080/fangzubao/api/core/rent/room_detail.do";
	IVO ivo =new IVO();
	try {
			ivo.set("id", "10");
			ivo.set("id", "15");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   }
	catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 租客删除承租房源
	 */
	public static void deleteRentRoom()
	{
	String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/rent/delete.do";
//	String url ="http://localhost:8080/fangzubao/api/core/rent/delete.do";
	IVO ivo =new IVO();
	try {
			ivo.set("id", "1");
			ivo.set("id", "4");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 租客缴押金业务处理
	 */
	public static void payDeposit()
	{
	String url ="http://localhost:8080/fangzubao/api/core/renterpay/pay_deposit.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "2");
			ivo.set("room_id", "1");
			ivo.set("money", "1000");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 租客缴租金业务处理
	 */
	public static void payMonthRent()
	{
	String url ="http://localhost:8080/fangzubao/api/core/renterpay/pay_rent.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "2");
			ivo.set("room_id", "14");
			ivo.set("record_id", "3");
			ivo.set("money", "1000");
			ivo.set("pay_type", "1");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 房东根据房源编号查询收租流水记录
	 */
	public static void queryChargeHistory()
	{
//	String url ="http://localhost:8080/fangzubao/api/core/renterpay/charge_history.do";
	String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/renterpay/charge_history.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "1");//房东
			ivo.set("room_id", "11");
			ivo.set("user_id", "7");//房东
			ivo.set("room_id", "11");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 租客根据房源编号查询缴租流水记录
	 */
	public static void queryPayHistory()
	{
//	String url ="http://localhost:8080/fangzubao/api/core/renterpay/pay_history.do";
	String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/renterpay/pay_history.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "2");//租客
			ivo.set("room_id", "1");
			ivo.set("user_id", "7");//租客
			ivo.set("room_id", "24");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 查询关于我们
	 */
	public static void queryAboutUs()
	{
//	String url ="http://localhost:8080/fangzubao/api/core/about_us/find.do";
	String url ="http://113.105.76.195:8080/fangzubao/api/core/about_us/find.do";
	IVO ivo =new IVO();
	try {
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 测试事物
	 */
	public static void testTx()
	{
	String url ="http://localhost:8080/fangzubao/api/core/test/testTx.do";
//	String url ="http://113.105.76.195:8080/fangzubao/api/core/about_us/find.do";
	IVO ivo =new IVO();
	try {
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 检查地理位置文件版本
	 */
	public static void checkGeographyFileVersion()
	{
	String url ="http://localhost:8080/fangzubao/api/core/zone/checkVersion.do";
//	String url ="http://113.105.76.195:8080/fangzubao/api/core/zone/checkVersion.do";
	IVO ivo =new IVO();
	try {
			ivo.set("version", "20150331152746");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 发表点评 
	 */
	public static void addEvaluation()
	{
//	String url ="http://localhost:8080/fangzubao/api/core/evaluation/add.do";
	String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/evaluation/add.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "2");
			ivo.set("to_id", "1");
			ivo.set("type", "2");
			ivo.set("room_id", "14");
			ivo.set("content", "房东管理到位");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 分页查询点评 
	 */
	public static void queryPageEvaluation()
	{
//	String url ="http://localhost:8080/fangzubao/api/core/evaluation/list.do";
	String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/evaluation/list.do";
	IVO ivo =new IVO();
	try {
			ivo.set("id_card_no", "123456789012345678");
			ivo.set("id_card_no", "222555554444441111");
			ivo.set("name", "gggghhhhhhhhhoo");
//			ivo.set("name", "小明");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			ivo.set("type", "1");
//			ivo.set("type", "2");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 发站内信
	 */
	public static void addLetter()
	{
	String url ="http://localhost:8080/fangzubao/api/core/letter/add.do";
//	String url ="http://113.105.76.195:8080/fangzubao/api/core/letter/add.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "1");
			ivo.set("to_id", "2");
			ivo.set("up_id", "");
			ivo.set("title", "1212");
			ivo.set("content", "你好");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 分页查询站内信
	 */
	public static void queryPageLetter()
	{
	String url ="http://localhost:8080/fangzubao/api/core/letter/list.do";
//	String url ="http://113.105.76.195:8080/fangzubao/api/core/evaluation/list.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "2");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 发站在线反馈
	 */
	public static void addTips()
	{
	String url ="http://localhost:8080/fangzubao/api/core/customer_tips/add.do";
//	String url ="http://113.105.76.195:8080/fangzubao/api/core/customer_tips/add.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "1");
			ivo.set("title", "test");
			ivo.set("content", "软件用着不错");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 分页查询站反馈记录
	 */
	public static void queryPageTips()
	{
	String url ="http://localhost:8080/fangzubao/api/core/customer_tips/list.do";
//	String url ="http://113.105.76.195:8080/fangzubao/api/core/evaluation/list.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "1");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询关于我们
	 */
	public static void queryRuleMessageAboutUs()
	{
//	String url ="http://localhost:8080/fangzubao/api/core/rule_message/about_us.do";
	String url ="http://113.105.76.195:8080/fangzubao/api/core/rule_message/about_us.do";
	IVO ivo =new IVO();
	try {
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询免责申明
	 */
	public static void queryRuleMessageStatement()
	{
	String url ="http://localhost:8080/fangzubao/api/core/rule_message/statement.do";
//	String url ="http://113.105.76.195:8080/fangzubao/api/core/evaluation/list.do";
	IVO ivo =new IVO();
	try {
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询房东租客的二维码分享信息
	 */
	public static void queryRuleMessageCoreQrCode()
	{
	String url ="http://localhost:8080/fangzubao/api/core/rule_message/core_qrcode.do";
//	String url ="http://113.105.76.195:8080/fangzubao/api/core/evaluation/list.do";
	IVO ivo =new IVO();
	try {
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *中介版二维码
	 */
	public static void queryRuleMessageAgentQrCode()
	{
	String url ="http://localhost:8080/fangzubao/api/core/rule_message/agent_qrcode.do";
//	String url ="http://113.105.76.195:8080/fangzubao/api/core/evaluation/list.do";
	IVO ivo =new IVO();
	try {
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			//加密
			json =AesUtil.encode(json);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("\n response json ====>> \n");
			System.out.print(res);
			res = AesUtil.decode(res);
			System.out.println("\n decode response json ===========>>\n"+res);
	   } catch (JException e) {
			e.printStackTrace();
		}
	   catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])
	{
		System.out.println("\n==========request start=============");
//获取最新版本		
//		getVersion();
		//发送短信验证码
//		sendSms();
		//注册
//		register();
//		登陆
//		login();
//		登陆
//		login_with_incrypt_str();
		//修改密码
//		changePassword();
		//通过邮箱重置密码
//		resetPassword();
		//修改个人信息
//		updateProfile();
//		mailtest();
//		查询广告列表
//		queryAllAds();
		// 查询广告详情
		queryAdDetail();
//		查询所有的城市和城市所辖区域
//		queryCityAndZone();
//		房东发布房源/未确认
//		addRoom();
//		房东发布待租房源
//		addCreateRoom();
//		 房东立即放租
//		addPublishRoom();
//		 房东发布房源后查询房源详情
//		landlordFind();
//		 房东确认出租
//		landlordConfirm();
//		绑定银行卡
//		bindCard();
//		 租客根据房源唯一码查询承租房源详情
//		agentFindRoom();
//		租客确认承租房源
//		agentConfirm();
//		房东查询自己的出租房源列表
//		listLandlordRoom();
//		 房东查询出租房源详情
//		queryLandlordRoomDetail();
//		房东查询自己的出租房源列表-出租中和已结束状态的房源
//		listLandlordUsingRoom();
//		 房东删除待出租房源
//		landlordDeleteRoom();
//		 房东申请终止委托
//		landlordStopRoom();
//		重新放租，查询房源信息
//		re_rent();
		//租客绑定银行支付卡号
//		bindCreditCard();
//		房东修改银行账号
//		changeAccount();
//		查询租客的租房列表
//		rentRoomList();
//		 租客查询的房源详情
//		rentRoomDetail();
//		租客删除承租房源
//		deleteRentRoom();
//		租客缴押金业务处理
//		payDeposit();
//		租客缴租金业务处理
//		payMonthRent();
//		 房东根据房源编号查询收租流水记录
//		queryChargeHistory();
//		 租客根据房源编号查询缴租流水记录
//		queryPayHistory();
//		查询关于我们
//		queryAboutUs();
		//测试事物
//		testTx();
//		checkGeographyFileVersion();
//		 发表点评 
//		addEvaluation();
//		分页查询点评 
//		queryPageEvaluation();
//		发站内信
//		addLetter();
//		分页查询站内信
//		queryPageLetter();
//		发站在线反馈
//		addTips();
//		分页查询站反馈记录
//		queryPageTips();
//		查询用户基本信息
//		queryProfile();
//		 查询关于我们
//		 queryRuleMessageAboutUs();
//		 查询免责申明
//		 queryRuleMessageStatement();
//		 查询房东租客的二维码分享信息
//		 queryRuleMessageCoreQrCode();
//		 中介版二维码
//		 queryRuleMessageAgentQrCode();
		System.out.println("\n==========request  end=============");
	}
	
}
