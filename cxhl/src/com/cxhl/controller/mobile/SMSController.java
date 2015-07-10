package com.cxhl.controller.mobile;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.SMSService;
import com.cxhl.service.UserService;
import com.cxhl.util.SmsWebServiceUtil;
import com.ezcloud.framework.service.system.SystemConfigService;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.DateUtil;

@Controller("mobileSMSController")
@RequestMapping("/api/sms")
public class SMSController extends BaseController {
	
	private static Logger logger = Logger.getLogger(SMSController.class); 
	@Resource(name = "cxhlUserService")
	private UserService userService;
	
	@Resource(name = "cxhlSMSService")
	private SMSService smsService;
	
	@Resource(name = "frameworkSystemConfigService")
	private SystemConfigService systemConfigService;

	/**
	 * 发送短信
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/send")
	public @ResponseBody
	String sendSms(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("发送短信");
		String telephone =ivo.getString("telephone",null);
		String type =ivo.getString("type","1");//1 发送短信 0不发送短信
//		String version =ivo.getString("version","1");//1 房东租客版 2中介版
		String sms_switch =systemConfigService.querySingleConfig("APP_SMS_INTERFACE", "SWITCH");
		if(StringUtils.isEmptyOrNull(sms_switch))
		{
			sms_switch ="0";
		}
		if(sms_switch.equals("0"))
		{
			ovo =new OVO(-1,"系统未开放短信注册，请联系客服","");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(StringUtils.isEmptyOrNull(telephone))
		{
			ovo =new OVO(-1,"手机号不能为空","");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row userRow = userService.findByTelephone(telephone);
		if(userRow != null)
		{
			ovo =new OVO(-1,"此用户已注册!","");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//防止频繁请求发送短信
		int sms_num =smsService.findCodeNumByTelphone(telephone);
		if(sms_num >=5)
		{
			Row lastedRow =smsService.getLastedSms(telephone);
			String send_time =lastedRow.getString("send_time","");
			if(StringUtils.isEmptyOrNull(send_time))
			{
				ovo =new OVO(-1,"此帐号异常，请联系客服人员","");
				return AesUtil.encode(VOConvert.ovoToJson(ovo));
			}
			String nowTime =DateUtil.getCurrentDateTime();
			long time_tap =DateUtil.getMinuteMinusOfTwoTime(send_time, nowTime);
			if(time_tap <= 5)
			{
				ovo =new OVO(-1,"发送短信太频繁，请稍后再试","");
				return AesUtil.encode(VOConvert.ovoToJson(ovo));
			}
			else
			{
				smsService.setSmsTimeOut(telephone);
			}
		}
		int sms_code =NumberUtils.getSixRandomNumber();
//		String sms_content ="欢迎使用吃香喝辣,您的验证码是:"+sms_code+"【房不剩房】";
		String sms_content ="1074吃香喝辣带您免费吃遍南宁，感谢您的注册，您的验证码是："+sms_code+"【吃香喝辣】";
		//0发送失败 1发送成功 2已过期 -1本地测试短信，不发送
		int status =0;
		//调用第三方短信平台接口
		long remote_send_status =0;
		if(type.equals("1"))
		{
			String sms_sn =systemConfigService.querySingleConfig("APP_SMS_INTERFACE", "USERNAME");
			String sms_pwd =systemConfigService.querySingleConfig("APP_SMS_INTERFACE", "PASSWORD");
			if(StringUtils.isEmptyOrNull(sms_sn) || StringUtils.isEmptyOrNull(sms_pwd))
			{
				ovo =new OVO(-1,"短信发送异常，请稍后再试","");
				return AesUtil.encode(VOConvert.ovoToJson(ovo));			
			}
			logger.info("sms_sn------------------>>"+sms_sn);
			logger.info("sms_pwd------------------>>"+sms_pwd);
//			SmsWebSocketClient smsClient =new SmsWebSocketClient(sms_sn,sms_pwd);
			remote_send_status =SmsWebServiceUtil.sendSms(sms_sn, sms_pwd, telephone, sms_content, "");
//			remote_send_status =Long.parseLong(smsClient.mt(telephone, sms_content, "", "", ""));
			if(remote_send_status == 0)
			{
				status =1;
			}
			else
			{
				status =0;
			}
//			status =1;
		}
		else
		{
			status =-1;
			if(type.equals("0"))
			{
				status =1;
			}
		}
		Row smsRow =new Row();
		smsRow.put("to_account",telephone );
		smsRow.put("sms_code", sms_code);
		smsRow.put("sms_content", sms_content);
		smsRow.put("status", status);
		//1房东租客版2中介版
		smsRow.put("type", "1");
		int rowNum =smsService.insert(smsRow);
		if(rowNum <= 0)
		{
			ovo =new OVO(-1,"发送短信验证码失败","发送短信验证码失败");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		ovo =new OVO(0,"发送短信验证码成功","");
		if(type.equals("0"))
		{
			ovo.set("code", sms_code);
		}
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 找回密码时发送短信
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/send_reset_pwd")
	public @ResponseBody
	String sendSmsWhenResetPwd(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("发送短信");
		String telephone =ivo.getString("telephone",null);
		String type =ivo.getString("type","1");//1 发送短信 0不发送短信
		String sms_switch =systemConfigService.querySingleConfig("APP_SMS_INTERFACE", "SWITCH");
		if(StringUtils.isEmptyOrNull(sms_switch))
		{
			sms_switch ="0";
		}
		if(sms_switch.equals("0"))
		{
			ovo =new OVO(-1,"系统未开放短信注册，请联系客服","");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(StringUtils.isEmptyOrNull(telephone))
		{
			ovo =new OVO(-1,"手机号不能为空","");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row userRow = userService.findByTelephone(telephone);
		if(userRow == null)
		{
			ovo =new OVO(-1,"此手机号未注册，请先注册!","此手机号未注册，请先注册!");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//防止频繁请求发送短信
//		int sms_num =smsService.findCodeNumByTelphone(telephone);
//		if(sms_num >=5)
//		{
//			Row lastedRow =smsService.getLastedSms(telephone);
//			String send_time =lastedRow.getString("send_time","");
//			if(StringUtils.isEmptyOrNull(send_time))
//			{
//				ovo =new OVO(-1,"此帐号异常，请联系客服人员","");
//				return AesUtil.encode(VOConvert.ovoToJson(ovo));
//			}
//			String nowTime =DateUtil.getCurrentDateTime();
//			long time_tap =DateUtil.getMinuteMinusOfTwoTime(send_time, nowTime);
//			if(time_tap <= 5)
//			{
//				ovo =new OVO(-1,"发送短信太频繁，请稍后再试","");
//				return AesUtil.encode(VOConvert.ovoToJson(ovo));
//			}
//			else
//			{
//				smsService.setSmsTimeOut(telephone);
//			}
//		}
		int sms_code =NumberUtils.getSixRandomNumber();
		String sms_content ="1074吃香喝辣带您免费吃遍南宁，找回密码的验证码为："+sms_code+"，请在5分钟内完成验证。如非本机号码操作，请忽略此信息。【吃香喝辣】";
		//0发送失败 1发送成功 2已过期 -1本地测试短信，不发送
		int status =0;
		//调用第三方短信平台接口
		long remote_send_status =0;
		if(type.equals("1"))
		{
			String sms_sn =systemConfigService.querySingleConfig("APP_SMS_INTERFACE", "USERNAME");
			String sms_pwd =systemConfigService.querySingleConfig("APP_SMS_INTERFACE", "PASSWORD");
			if(StringUtils.isEmptyOrNull(sms_sn) || StringUtils.isEmptyOrNull(sms_pwd))
			{
				ovo =new OVO(-1,"短信发送异常，请稍后再试","");
				return AesUtil.encode(VOConvert.ovoToJson(ovo));			
			}
			logger.info("sms_sn------------------>>"+sms_sn);
			logger.info("sms_pwd------------------>>"+sms_pwd);
			remote_send_status =SmsWebServiceUtil.sendSms(sms_sn, sms_pwd, telephone, sms_content, "");
//			SmsWebSocketClient smsClient =new SmsWebSocketClient(sms_sn,sms_pwd);
//			remote_send_status =Long.parseLong(smsClient.mt(telephone, sms_content, "", "", ""));
			if(remote_send_status== 0)
			{
				status =1;
			}
			else
			{
				status =0;
			}
//			status =1;
		}
		else
		{
			status =-1;
			if(type.equals("0"))
			{
				status =1;
			}
		}
		Row smsRow =new Row();
		smsRow.put("to_account",telephone );
		smsRow.put("sms_code", sms_code);
		smsRow.put("sms_content", sms_content);
		smsRow.put("status", status);
		//1注册2找回密码
		smsRow.put("type", "2");
		int rowNum =smsService.insert(smsRow);
		if(rowNum <= 0)
		{
			ovo =new OVO(-1,"发送短信验证码失败","发送短信验证码失败");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		ovo =new OVO(0,"发送短信验证码成功","");
		if(type.equals("0"))
		{
			ovo.set("code", sms_code);
		}
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

	/**
	 * 更换手机号码时发送短信
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/send_change_telephone")
	public @ResponseBody
	String sendSmsWhenChangeTelephone(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("发送短信");
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-1,"用户编号不能为空","");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row uRow =userService.find(user_id);
		if(uRow == null)
		{
			ovo =new OVO(-1,"用户编号错误，用户不存在！","用户编号错误，用户不存在！");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String telephone =ivo.getString("telephone",null);
		String type =ivo.getString("type","1");//1 发送短信 0不发送短信
		String sms_switch =systemConfigService.querySingleConfig("APP_SMS_INTERFACE", "SWITCH");
		if(StringUtils.isEmptyOrNull(telephone))
		{
			ovo =new OVO(-1,"手机号不能为空","");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
//		手机号是否已经存在
		Row user_row =userService.findByTelephone(telephone);
		if(user_row != null)
		{
			ovo =new OVO(-1,"此手机号已经存在，请输入其他的手机号码!","此手机号已经存在，请输入其他的手机号码!");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(StringUtils.isEmptyOrNull(sms_switch))
		{
			sms_switch ="0";
		}
		if(sms_switch.equals("0"))
		{
			ovo =new OVO(-1,"系统未开放短信注册，请联系客服","");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		
		//防止频繁请求发送短信
//		int sms_num =smsService.findCodeNumByTelphone(telephone);
//		if(sms_num >=5)
//		{
//			Row lastedRow =smsService.getLastedSms(telephone);
//			String send_time =lastedRow.getString("send_time","");
//			if(StringUtils.isEmptyOrNull(send_time))
//			{
//				ovo =new OVO(-1,"此帐号异常，请联系客服人员","");
//				return AesUtil.encode(VOConvert.ovoToJson(ovo));
//			}
//			String nowTime =DateUtil.getCurrentDateTime();
//			long time_tap =DateUtil.getMinuteMinusOfTwoTime(send_time, nowTime);
//			if(time_tap <= 5)
//			{
//				ovo =new OVO(-1,"发送短信太频繁，请稍后再试","");
//				return AesUtil.encode(VOConvert.ovoToJson(ovo));
//			}
//			else
//			{
//				smsService.setSmsTimeOut(telephone);
//			}
//		}
		int sms_code =NumberUtils.getSixRandomNumber();
//		String sms_content ="欢迎使用吃香喝辣,您的验证码是:"+sms_code+"【房不剩房】";
		String sms_content ="1074吃香喝辣带您免费吃遍南宁，更换手机号码的验证码为："+sms_code+"，请在5分钟内完成验证。如非本机号码操作，请忽略此信息。【吃香喝辣】";
		//0发送失败 1发送成功 2已过期 -1本地测试短信，不发送
		int status =0;
		//调用第三方短信平台接口
		long remote_send_status =0;
		if(type.equals("1"))
		{
			String sms_sn =systemConfigService.querySingleConfig("APP_SMS_INTERFACE", "USERNAME");
			String sms_pwd =systemConfigService.querySingleConfig("APP_SMS_INTERFACE", "PASSWORD");
			if(StringUtils.isEmptyOrNull(sms_sn) || StringUtils.isEmptyOrNull(sms_pwd))
			{
				ovo =new OVO(-1,"短信发送异常，请稍后再试","");
				return AesUtil.encode(VOConvert.ovoToJson(ovo));			
			}
			logger.info("sms_sn------------------>>"+sms_sn);
			logger.info("sms_pwd------------------>>"+sms_pwd);
//			SmsWebSocketClient smsClient =new SmsWebSocketClient(sms_sn,sms_pwd);
//			remote_send_status =Long.parseLong(smsClient.mt(telephone, sms_content, "", "", ""));
			remote_send_status =SmsWebServiceUtil.sendSms(sms_sn, sms_pwd, telephone, sms_content, "");
			if(remote_send_status == 0)
			{
				status =1;
			}
			else
			{
				status =0;
			}
//			status =1;
		}
		else
		{
			status =-1;
			if(type.equals("0"))
			{
				status =1;
			}
		}
		Row smsRow =new Row();
		smsRow.put("to_account",telephone );
		smsRow.put("sms_code", sms_code);
		smsRow.put("sms_content", sms_content);
		smsRow.put("status", status);
		//1注册2找回密码3更换手机号码
		smsRow.put("type", "3");
		int rowNum =smsService.insert(smsRow);
		if(rowNum <= 0)
		{
			ovo =new OVO(-1,"发送短信验证码失败","发送短信验证码失败");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		ovo =new OVO(0,"发送短信验证码成功","");
		if(type.equals("0"))
		{
			ovo.set("code", sms_code);
		}
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
}
