package com.fbsf.fzb.test;


import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.vo.IVO;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.NetUtil;

/**
 * 中介版测试类
 * @author Administrator
 *
 */
public class AgentApiTest {

	//查询版本
	public static void getVersion()
	{
		String url ="http://yyltest.shike001.com:8080/fangzubao/api/core/version/lastest.do";
//		String url ="http://127.0.0.1:8080/fangzubao/api/core/version/lastest.do";
		IVO ivo =new IVO();
	   try {
		   //1房租宝房东租客版2中介版
			ivo.set("app", "2");
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
//		String url ="http://127.0.0.1:8080/fangzubao/api/agent/sms/send.do";
		String url ="http://yyltest.shike001.com:8080/fangzubao/api/agent/sms/send.do";
		IVO ivo =new IVO();
		   try {
			   //手机号
				ivo.set("telephone", "13826531136");
				//1 发送短信 0不发送短信
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
		
	//注册
	public static void register()
	{
//		String url ="http://127.0.0.1:8080/fangzubao/api/agent/action/register.do";
		String url ="http://yyltest.shike001.com:8080/fangzubao/api/agent/action/register.do";
		IVO ivo =new IVO();
		   try {
				ivo.set("telephone", "13826531136");
				ivo.set("password", "E10ADC3949BA59ABBE56E057F20F883E");
				ivo.set("sms_code", "420417");
				ivo.set("device", "1");
				ivo.set("device_code", "1235173571357562317");
				//客户端版本
				ivo.set("version", "1.0");
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
//		String url ="http://127.0.0.1:8080/fangzubao/api/agent/action/login.do";
		String url ="http://yyltest.shike001.com:8080/fangzubao/api/agent/action/login.do";
		IVO ivo =new IVO();
		   try {
				ivo.set("username", "15013790927");
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
	
	
	//根据id查询中介用户的基本信息
	public static void queryProfile()
	{
//		String url ="http://127.0.0.1:8080/fangzubao/api/agent/action/profile.do";
		String url ="http://yyltest.shike001.com:8080/fangzubao/api/agent/action/profile.do";
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
//		String url ="http://127.0.0.1:8080/fangzubao/api/agent/action/changePassword.do";
		String url ="http://yyltest.shike001.com:8080/fangzubao/api/agent/action/changePassword.do";
		IVO ivo =new IVO();
		   try {
				ivo.set("user_id", "2");
//				ivo.set("oldPwd", "E10ADC3949BA59ABBE56E057F20F883E");//123456
//				ivo.set("newPwd", "670B14728AD9902AECBA32E22FA4F6BD");//000000
				ivo.set("oldPwd", "670B14728AD9902AECBA32E22FA4F6BD");//123456
				ivo.set("newPwd", "E882B72BCCFC2AD578C27B0D9B472A14");//000000
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
	
	
	
	//通过邮箱重置密码
	public static void resetPassword()
	{
		String url ="http://127.0.0.1:8080/fangzubao/api/agent/action/resetPassword.do";
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
		String url ="http://127.0.0.1:8080/fangzubao/api/agent/action/updateProfile.do";
		IVO ivo =new IVO();
		   try {
				ivo.set("id", "1");
				ivo.set("name", "小童");
				ivo.set("email", "dabao1989125@163.com");
				ivo.set("username", "哈哈哈");
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
	String url ="http://127.0.0.1:8080/fangzubao/api/agent/ad/list.do";
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
	String url ="http://127.0.0.1:8080/fangzubao/api/agent/ad/find.do";
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
	 * 绑定银行卡
	 */
	public static void bindCard()
	{
	String url ="http://localhost:8080/fangzubao/api/agent/action/bind_card.do";
	IVO ivo =new IVO();
	try {
			ivo.set("id", "1");
			ivo.set("card_no", "6222021001116245708");
			ivo.set("user_name", "xiaotong");
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
	 * 中介修改银行账号
	 */
	public static void changeAccount()
	{
	String url ="http://127.0.0.1:8080/fangzubao/api/agent/action/changeAccount.do";
	IVO ivo =new IVO();
	try {
			ivo.set("id", "1");
			ivo.set("password", "2D23E461971CD2D03E6D6DEF61C4506C");
			ivo.set("card_no", "62220210011162457036");
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
	 * 分页查询客户列表
	 */
	public static void queryAgentCustomerList()
	{
//	String url ="http://127.0.0.1:8080/fangzubao/api/agent/customer/list.do";
	String url ="http://yyltest.shike001.com:8080/fangzubao/api/agent/customer/list.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "4");
			ivo.set("page", "0");
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
	 * 分页查询客户列表
	 */
	public static void queryRoomDetail()
	{
//	String url ="http://127.0.0.1:8080/fangzubao/api/agent/customer/room_detail.do";
	String url ="http://yyltest.shike001.com:8080/fangzubao/api/agent/customer/room_detail.do";
	IVO ivo =new IVO();
	try {
			ivo.set("room_id", "1");
			ivo.set("room_id", "8");
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
	 * 分页查询月度奖励
	 */
	public static void queryAgentMonthReward()
	{
	String url ="http://127.0.0.1:8080/fangzubao/api/agent/reward/month.do";
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
	 *根据用户编号查询全部奖励
	 */
	public static void queryAgentAllReward()
	{
	String url ="http://127.0.0.1:8080/fangzubao/api/agent/reward/total.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "1");
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
	 *根据用户编号查询当月奖励
	 */
	public static void queryAgentCurrentMonthReward()
	{
	String url ="http://127.0.0.1:8080/fangzubao/api/agent/reward/current_month.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "1");
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
	 *首页数据
	 */
	public static void queryAgentIndexPageData()
	{
	String url ="http://127.0.0.1:8080/fangzubao/api/agent/page/index.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "1");
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
	
	
	public static void testSendEncryptData()
	{
		String url ="http://yyltest.shike001.com:8080/fangzubao/api/agent/action/changePassword.do";
		String json ="Ngn1aWQ+u1siNR+rQkMMkJbWYIGCZ1TsoMykxSMNwq5o1DFiS1vO3s59YYanc+CASepqMi7TfTfpN2MsyvLyPPk9VokIKztnrCqk6Z57gy9HqH6DxmEDF+xD2kUbry/iiQC00McB7A+mWhX1Jx9FstAIrJlJCmN1MWpr5IZL15Nup7l1VKXvF8HokGg653mru4iji2if1ZnufwW2TfgbmuLPcgHYH+/EEL0I9aiUd+9uQCYjmzziL+kP7a/hTkIQ3fWOfk3zZcXDNthr49aV5ickB4xpkDVlLvXtBQoXabDhXDuazY+8Dzs13Y/mxdYVgh9edEFnKmFjfGdDOOWSAV2YIBiUp06VWU+bJiE+NOBCkC9sRFScg+wUuJdGuskObOvb7KOWv7gDVk54/vCxcvyyUWQEJRdAFmddv4R9g6EG/XNAS3Jzc4nHkiIlqzh0VjvfOaRffjzhvCdENVPSyIZNNPGKTb/vZOBRWe3OK3jPCFl8GqC+xVTZkQcdrnbzFegOug5mKw7tT/Nvf9gjpHeEvHEIwfN/06bYl81iqVbewv1dO/L9QEB6x3D70krZbt/nbm/yG3ISE4WaFHmAoDyopV17rh4+/salwb3ELJSYLL38P1Ma4MZNajV8qalk9/XrJ2osJsG8D7JyKIYEMQ==";
		System.out.println("\n ivo to json ====>>"+json);
		String res;
		try {
			res = NetUtil.getNetResponse(url, json,"UTF-8");
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
	 *申请提现
	 */
	public static void withdrawRequest()
	{
	String url ="http://127.0.0.1:8080/fangzubao/api/agent/withdraw/request.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "1");
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
	 *查询申请提现记录
	 */
	public static void findWithdrawPageList()
	{
//	String url ="http://127.0.0.1:8080/fangzubao/api/agent/withdraw/list.do";
	String url ="http://yyltest.shike001.com:8080/fangzubao/api/agent/withdraw/list.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "3");
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
	 *查询中介奖励汇总
	 */
	public static void queryAwardSummary()
	{
//	String url ="http://127.0.0.1:8080/fangzubao/api/agent/reward/summary.do";
	String url ="http://yyltest.shike001.com:8080/fangzubao/api/agent/reward/summary.do";
	IVO ivo =new IVO();
	try {
			ivo.set("user_id", "3");
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
//		根据id查询中介用户的基本信息
		queryProfile();
		//修改密码
//		changePassword();
		//通过邮箱重置密码
//		resetPassword();
		//修改个人信息
//		updateProfile();
//		查询广告列表
//		queryAllAds();
		// 查询广告详情
//		queryAdDetail();
//		绑定银行卡
//		bindCard();
//		中介修改银行账号
//		changeAccount();
//		 分页查询客户列表
//		queryAgentCustomerList();
//		中介查询房源详情
//		queryRoomDetail();
//		分页查询月度奖励
//		queryAgentMonthReward();
//		根据用户编号查询全部奖励
//		queryAgentAllReward();
//		根据用户编号查询当月奖励
//		queryAgentCurrentMonthReward();
//		首页数据
//		queryAgentIndexPageData();
//		testSendEncryptData();
//		withdrawRequest();
//		查询申请提现记录
//		findWithdrawPageList();
//		查询中介奖励汇总
//		queryAwardSummary();
		System.out.println("\n==========request  end=============");
	}
}
