package com.cxhl.test;

import java.io.UnsupportedEncodingException;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.IVO;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.Base64Util;
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
//		String url ="http://localhost:8080/cxhl/api/version/lastest.do";
		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/version/lastest.do";
		IVO ivo =new IVO();
		   try {
			   //1房租宝房东租客版2中介版
//				ivo.set("app", "1");
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
//		String url ="http://localhost:8080/cxhl/api/sms/send.do";
		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/sms/send.do";
		IVO ivo =new IVO();
		   try {
			   //手机号
				ivo.set("telephone", "13826531131");
//				ivo.set("telephone", "13826531137");
//				ivo.set("telephone", "13590856852");
				//1 发送短信 0不发送短信
//				ivo.set("type", "0");
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
		String url ="http://localhost:8080/cxhl/api/user/login.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/login.do";
		IVO ivo =new IVO();
		   try {
				ivo.set("username", "13826531136");
//				ivo.set("username", "13826531137");
				ivo.set("password", "E10ADC3949BA59ABBE56E057F20F883E");
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
	//logout
	public static void logout()
	{
		String url ="http://localhost:8080/cxhl/api/user/logout.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/logout.do";
		IVO ivo =new IVO();
		try {
			ivo.set("id", "1");
//			ivo.set("username", "13826531136");
//				ivo.set("username", "13826531137");
//			ivo.set("password", "E10ADC3949BA59ABBE56E057F20F883E");
			//客户端版本
//			ivo.set("version", "1.1");
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
//		String url ="http://localhost:8080/cxhl/api/action/login.do";
		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/action/login.do";
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
//		String url ="http://localhost:8080/cxhl/api/user/profile.do";
		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/profile.do";
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
//		String url ="http://localhost:8080/cxhl/api/user/changePassword.do";
		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/changePassword.do";
		IVO ivo =new IVO();
		   try {
				ivo.set("telephone", "13826531137");
				ivo.set("password", "670B14728AD9902AECBA32E22FA4F6BD");//000000
				ivo.set("sms_code", "121");//000000
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
		String url ="http://localhost:8080/cxhl/api/user/register.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/register.do";
		IVO ivo =new IVO();
	   try {
			ivo.set("telephone", "13826531136");
//			ivo.set("telephone", "13826531137");
			ivo.set("password", "E10ADC3949BA59ABBE56E057F20F883E");
			ivo.set("sms_code", "158835");
//			ivo.set("sms_code", "15883523");
			ivo.set("device", "1");
			ivo.set("device_code", "1212312735713575663");
//			ivo.set("invite_code", "123456");
			//客户端版本
			ivo.set("version", "1.0");
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
	
	//找回密码时发送短信验证码
	public static void sendSmsResetPwd()
	{
		String url ="http://localhost:8080/cxhl/api/sms/send_reset_pwd.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/sms/send_reset_pwd.do";
		IVO ivo =new IVO();
		   try {
			   //手机号
				ivo.set("telephone", "13826531136");
				//1 发送短信 0不发送短信
				ivo.set("type", "0");
//					ivo.set("type", "1");
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
	//通过短信重置密码
	public static void resetPassword()
	{
		String url ="http://localhost:8080/cxhl/api/user/resetPassword.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/resetPassword.do";
		IVO ivo =new IVO();
		   try {
				ivo.set("telephone", "13826531136");
				ivo.set("password", "E10ADC3949BA59ABBE56E057F20F883E");
				ivo.set("sms_code", "794113");
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
		String url ="http://localhost:8080/cxhl/api/user/updateProfile.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/updateProfile.do";
		IVO ivo =new IVO();
		   try {
				ivo.set("id", "1");
				ivo.set("name", "小童");
//				ivo.set("email", "510836102@qq.com");
				ivo.set("username", "tong.kaka");
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
	String url ="http://localhost:8080/cxhl/api/zone/CityAndZone.do";
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
	String url ="http://ilef.vxg196.10000net.cn/cxhl/api/ad/list.do";
//	String url ="http://localhost:8080/cxhl/api/ad/list.do";
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
	   } 
		catch (JException e) {
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
	String url ="http://localhost:8080/cxhl/api/ad/find.do";
//	String url ="http://ilef.vxg196.10000net.cn/cxhl/api/ad/find.do";
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
	 * 查询关于我们
	 */
	public static void queryAboutUs()
	{
//	String url ="http://localhost:8080/cxhl/api/about_us/find.do";
	String url ="http://ilef.vxg196.10000net.cn/cxhl/api/about_us/find.do";
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
	String url ="http://localhost:8080/cxhl/api/test/testTx.do";
//	String url ="http://ilef.vxg196.10000net.cn/cxhl/api/about_us/find.do";
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
//	String url ="http://localhost:8080/cxhl/api/zone/checkVersion.do";
	String url ="http://ilef.vxg196.10000net.cn/cxhl/api/zone/checkVersion.do";
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
	 * 查询关于我们
	 */
	public static void queryRuleMessageAboutUs()
	{
//	String url ="http://localhost:8080/cxhl/api/rule_message/about_us.do";
	String url ="http://ilef.vxg196.10000net.cn/cxhl/api/rule_message/about_us.do";
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
	String url ="http://localhost:8080/cxhl/api/rule_message/statement.do";
//	String url ="http://ilef.vxg196.10000net.cn/cxhl/api/evaluation/list.do";
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
	String url ="http://localhost:8080/cxhl/api/rule_message_qrcode.do";
//	String url ="http://ilef.vxg196.10000net.cn/cxhl/api/evaluation/list.do";
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
	String url ="http://localhost:8080/cxhl/api/rule_message/agent_qrcode.do";
//	String url ="http://ilef.vxg196.10000net.cn/cxhl/api/evaluation/list.do";
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
	 *收货地址列表
	 */
	public static void queryUserAddressPaeg()
	{
		String url ="http://localhost:8080/cxhl/api/user/address/list.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/address/list.do";
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
	 *添加收货地址
	 */
	public static void addUserAddress()
	{
		String url ="http://localhost:8080/cxhl/api/user/address/add.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/address/add.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("province_id", "19");
			ivo.set("city_id", "202");
			ivo.set("region_id", "1956");
			ivo.set("address", "福田车公庙");
			ivo.set("receive_name", "小童");
			ivo.set("receive_tel", "1382653136");
			ivo.set("zip_code", "518002");
			ivo.set("is_default","1");
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
	 *删除收货地址
	 */
	public static void deleteUserAddress()
	{
		String url ="http://localhost:8080/cxhl/api/user/address/delete.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/address/delete.do";
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
	
	//更换手机号码时发送短信验证码
	public static void sendSmsWhenChangeTelephone()
	{
		String url ="http://localhost:8080/cxhl/api/sms/send_change_telephone.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/sms/send_change_telephone.do";
		IVO ivo =new IVO();
		   try {
			   //手机号
				ivo.set("user_id", "1");
				ivo.set("telephone", "13826531130");
				//1 发送短信 0不发送短信
				ivo.set("type", "0");
//				ivo.set("type", "1");
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
	
	//更换手机号码
	public static void changeTelephone()
	{
		String url ="http://localhost:8080/cxhl/api/user/changeTelephone.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/changeTelephone.do";
		IVO ivo =new IVO();
		try {
			//手机号
			ivo.set("id", "1");
			ivo.set("telephone", "13826531130");
			ivo.set("sms_code", "295517");
//				ivo.set("type", "1");
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
	
	//设置支付密码
	public static void setPayPassword()
	{
		String url ="http://localhost:8080/cxhl/api/user/setPayPassword.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/setPayPassword.do";
		IVO ivo =new IVO();
		try {
			//手机号
			ivo.set("id", "1");
			ivo.set("pay_password", "96E79218965EB72C92A549DD5A330112");
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
	
	//添加意见反馈
	public static void addCustomerTip()
	{
		String url ="http://localhost:8080/cxhl/api/customer_tips/add.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/customer_tips/add.do";
		IVO ivo =new IVO();
		try {
			//手机号
			ivo.set("user_id", "1");
			ivo.set("title", "意见反馈");
			ivo.set("content", "app使用意见反馈");
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
	
	//分页查询用户收藏
	public static void queryPageUserCollection()
	{
//		String url ="http://localhost:8080/cxhl/api/user/collection/list.do";
		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/collection/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("user_id", "494");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
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
	
	//用户添加收藏
	public static void userAddCollection()
	{
		String url ="http://localhost:8080/cxhl/api/user/collection/add.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/collection/add.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("shop_id", "1");
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
	//用户批量删除收藏
	public static void userDeleteCollection()
	{
		String url ="http://localhost:8080/cxhl/api/user/collection/delete.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/collection/delete.do";
		IVO ivo =new IVO();
		try {
			ivo.set("id", "1,2,3");
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
	
	//查询商家分类
	public static void queryShopType()
	{
		String url ="http://localhost:8080/cxhl/api/shop/category/list.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/shop/category/list.do";
		IVO ivo =new IVO();
		try {
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
	
	//分页查询商家分类
	public static void queryShopPage()
	{
//		String url ="http://localhost:8080/cxhl/api/shop/profile/list.do";
		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/shop/profile/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("type", "1");
			ivo.set("key_word", "黄");
			ivo.set("page", "1");
			ivo.set("page_size", "3");
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
	//查询商家详情
	public static void queryShopDetail()
	{
//		String url ="http://localhost:8080/cxhl/api/shop/profile/detail.do";
		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/shop/profile/detail.do";
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
	
	//分页查询商家优惠券
	public static void queryShopCouponPage()
	{
//		String url ="http://localhost:8080/cxhl/api/shop/coupon/list.do";
		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/shop/coupon/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("shop_id", "1");
			ivo.set("shop_id", "19");
			ivo.set("page", "1");
			ivo.set("page_size", "1");
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
	
	//分页查询商家优惠券图文详情
	public static void queryShopCouponRemark()
	{
//		String url ="http://localhost:8080/cxhl/api/shop/coupon/remark.do";
		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/shop/coupon/remark.do";
		IVO ivo =new IVO();
		try {
			ivo.set("id", "3");
			ivo.set("id", "3000");
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
	//创建订单
	public static void createOrder()
	{
		String url ="http://localhost:8080/cxhl/api/order/profile/add.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/order/profile/add.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("money", "80");
			DataSet items =new DataSet();
			Row irow =new Row();
			irow.put("coupon_id", "1");
			irow.put("price", "80");
			irow.put("num", "1");
			items.add(irow);
			ivo.set("items", items);
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
	//用户分页查询自己的订单
	public static void queryUserOrder()
	{
		String url ="http://localhost:8080/cxhl/api/order/profile/list.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/order/profile/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("state", "");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
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
	//订单项
	public static void queryOrderItems()
	{
		String url ="http://localhost:8080/cxhl/api/order/item/list.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/order/item/list.do";
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
	//用户查询自己的优惠券
	public static void queryUserCoupon()
	{
//		String url ="http://localhost:8080/cxhl/api/user/coupon/list.do";
		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/coupon/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("user_id", "207");
			ivo.set("user_id", "493");
			ivo.set("user_id", "206");
//			ivo.set("state", "1");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
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
	
	//创建用户消费优惠券订单
	public static void createCouponOrder()
	{
		String url ="http://localhost:8080/cxhl/api/user/coupon/order/add.do";
//			String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/coupon/order/add.do";
		IVO ivo =new IVO();
		try {
			ivo.set("shop_id", "1");
			ivo.set("user_id", "1");
			ivo.set("money", "80");
			DataSet items =new DataSet();
			Row irow =new Row();
			irow.put("coupon_id", "1");
			irow.put("price", "80");
			irow.put("num", "1");
			items.add(irow);
			ivo.set("items", items);
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
	
	//用户消费优惠券记录汇总页面
	public static void userCouponSummary()
	{
//		String url ="http://localhost:8080/cxhl/api/user/coupon/order/summary.do";
			String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/coupon/order/summary.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
//			ivo.set("month", "2015-06");
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
	
	//查询指定月份的用户消费优惠券的详情
	public static void userCouponMonthList()
	{
		String url ="http://localhost:8080/cxhl/api/user/coupon/order/month_list.do";
//			String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/coupon/order/month_list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("month", "2015-06");
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
	
	//上传头像
	public static void uploadAvatar()
	{
		String url ="http://localhost:8080/cxhl/api/user/upload_avatar.do";
//			String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/upload_avatar.do";
		IVO ivo =new IVO();
		try {
			ivo.set("id", "1");
			String pictrue = Base64Util.GetImageStr("/Users/TongJianbo/Desktop/123.png");
			ivo.set("picture_base64_str", Base64Util.encode(pictrue.getBytes()));
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
	
	//用户分页查询自己的礼品
	public static void queryUserGiftPage()
	{
		String url ="http://localhost:8080/cxhl/api/user/gift/list.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/gift/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("user_id", "7");
//			ivo.set("state", "1");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
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
	
	//用户查询礼品详情
	public static void queryUserGiftDetail()
	{
		String url ="http://localhost:8080/cxhl/api/user/gift/detail.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/gift/detail.do";
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
	
	//用户兑换礼品
	public static void queryExchangeGift()
	{
		String url ="http://localhost:8080/cxhl/api/user/gift/exchange.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/gift/exchange.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("gift_id", "1");
			ivo.set("exchange_num", "1");
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
	//微信app支付
	public static void weixinAppPay()
	{
		String url ="http://localhost:8080/cxhl/api/pay/weixin/app/validate.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/pay/weixin/app/validate.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("order_id", "1");
			ivo.set("app_ip", "192.168.11.99");
			ivo.set("service_name", "cxhlWeiXinAppPayService");
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
//	用户分享
	public static void userShare()
	{
//		String url ="http://localhost:8080/cxhl/api/user/share/add.do";
		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/share/add.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
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
	
//	优惠券抽奖
	public static void lotteryCoupon()
	{
//		String url ="http://localhost:8080/cxhl/api/user/lottery/coupon.do";
		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/lottery/coupon.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("user_id", "493");
//			ivo.set("user_id", "2");
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
//	二维码抽奖
	public static void lotteryQRCode()
	{
		String url ="http://localhost:8080/cxhl/api/user/lottery/qrcode.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/lottery/qrcode.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
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
//	电台抽奖
	public static void lotteryRadio()
	{
		String url ="http://localhost:8080/cxhl/api/user/lottery/radio.do";
//		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/lottery/radio.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
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
	
	
	
	
	public static void monitorS2WX()
	{
		String url ="https://api.mch.weixin.qq.com/pay/unifiedorder";
		String postStr ="<xml><appid>wx44e3ee46a26f4e21</appid><mch_id>1251662201</mch_id><device_info></device_info><nonce_str>RGDJ6RQ62Y5MMHFYEHVFU5RUKTTL</nonce_str><sign>60D59E27413821B92EE1C4F2798774D3</sign><body>订单:2015062217343610001支付备注</body><detail></detail><attach></attach><out_trade_no>2015062217343610001</out_trade_no><fee_type>CNY</fee_type><total_fee>8000</total_fee><spbill_create_ip>192.168.11.99</spbill_create_ip><time_start></time_start><time_expire></time_expire><goods_tag></goods_tag><notify_url>http://localhost:8080/cxhl/notify/weixin/pay/app.do?order_no=3kLVUn/XTRB1bpSLU+EXAKhrD+UkGtrb1FIqwzqTASU=</notify_url><trade_type>APP</trade_type><product_id></product_id><openid></openid></xml>";
		String response ="";
		try {
			response =NetUtil.getNetResponse(url, postStr);
			response =new String(response.getBytes("utf-8"),"GBK");
		} catch (JException e) {
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print("response====>> \n"+response);
	}
	
	//查询用户的抽奖次数
	public static void lotteryNum()
	{
//		String url ="http://localhost:8080/cxhl/api/user/lottery/lottery_num.do";
		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/lottery/lottery_num.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
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
	//查询用户的抽奖记录
	public static void lotteryList()
	{
//		String url ="http://localhost:8080/cxhl/api/user/lottery/list.do";
		String url ="http://ilef.vxg196.10000net.cn/cxhl/api/user/lottery/list.do";
		IVO ivo =new IVO();
		try {
			ivo.set("user_id", "1");
			ivo.set("page", "1");
			ivo.set("page_size", "10");
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
	
	public static void main(String args[])
	{
		System.out.println("\n==========request start=============");
//获取最新版本		
//		getVersion();
//		发送短信验证码
//		sendSms();
//		注册
//		register();
//		登陆
//		login();
//		注销
//		logout();
//		登陆
//		login_with_incrypt_str();
//		修改密码
//		changePassword();
//		找回密码时发送短信验证码
//		sendSmsResetPwd();
//		重置密码
//		resetPassword();
//		根据用户编号查询用户信息
//		queryProfile();
//		修改个人信息
//		updateProfile();
//		mailtest();
//		查询广告列表
//		queryAllAds();
//		查询广告详情
//		queryAdDetail();
//		查询所有的城市和城市所辖区域
//		queryCityAndZone();
//		检查地理位置文件是否要更新
//		checkGeographyFileVersion();
//		收货地址列表
//		queryUserAddressPaeg();
//		添加收货地址
//		addUserAddress();
//		删除收货地址
//		deleteUserAddress();
//		更换手机号码时发送短信验证码
//		sendSmsWhenChangeTelephone();
//		更换手机号码
//		changeTelephone();
//		设置支付密码
//		setPayPassword();
//		添加意见反馈
//		addCustomerTip();
//		分页查询用户收藏
//		queryPageUserCollection();
//		用户添加收藏
//		userAddCollection();
//		用户批量删除收藏
//		userDeleteCollection();
//		查询商家分类
//		queryShopType();
//		分页查询商家分类
//		queryShopPage();
//		查询商家详情
//		queryShopDetail();
//		分页查询商家优惠券
//		queryShopCouponPage();
//		分页查询商家优惠券图文详情
//		queryShopCouponRemark();
//		创建订单
//		createOrder();
//		用户分页查询自己的订单
//		queryUserOrder();
//		订单项
//		queryOrderItems();
//		用户查询自己的优惠券
//		queryUserCoupon();
//		创建用户消费优惠券订单
//		createCouponOrder();
//		用户消费优惠券记录汇总页面
		userCouponSummary();
//		查询指定月份的用户消费优惠券的详情
//		userCouponMonthList();
//		上传头像
//		uploadAvatar();
//		用户分页查询自己的礼品
//		queryUserGiftPage();
//		用户查询礼品详情
//		queryUserGiftDetail();
//		用户兑换礼品
//		queryExchangeGift();
//		微信app支付
//		weixinAppPay();
//		模拟调用统一支付接口
//		monitorS2WX();
//		用户分享
//		userShare();
//		优惠券抽奖
//		lotteryCoupon();
//		二维码抽奖
//		lotteryQRCode();
//		电台抽奖
//		lotteryRadio();
//		查询用户的抽奖次数
//		lotteryNum();
//		查询用户的抽奖记录
//		lotteryList();
		System.out.println("\n==========request  end=============");
	}
	
}
