package com.cxhl.controller.mobile;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.AgentService;
import com.cxhl.service.SMSService;
import com.cxhl.util.SmsWebSocketClient;
import com.ezcloud.framework.service.system.SystemConfigService;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.DateUtil;

@Controller("mobileAgentSMSController")
@RequestMapping("/api/agent/sms")
public class AgentSMSController extends BaseController {
	
	private static Logger logger = Logger.getLogger(AgentSMSController.class); 
	@Resource(name = "fzbAgentService")
	private AgentService agentUserService;
	
	@Resource(name = "fzbSMSService")
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
	String queryPageRoom(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("发送短信");
		String telephone =ivo.getString("telephone",null);
		String type =ivo.getString("type","1");
		if(StringUtils.isEmptyOrNull(telephone))
		{
			ovo =new OVO(-1,"手机号不能为空","手机号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row userRow = agentUserService.findByTelephone(telephone);
		if(userRow != null)
		{
			ovo =new OVO(-1,"此用户已注册!","此用户已注册!");
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
		String sms_content ="房租宝注册验证码为："+sms_code+"，请在5分钟内完成验证。如非本机号码操作，请忽略此信息。【房不剩房】";
		//调用第三方短信平台接口
		long remote_send_status =0;
		//0发送失败 1发送成功 2已过期 
		int status =0;
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
			SmsWebSocketClient smsClient =new SmsWebSocketClient(sms_sn,sms_pwd);
			remote_send_status =Long.parseLong(smsClient.mt(telephone, sms_content, "", "", ""));
			if(remote_send_status > 0)
			{
				status =1;
			}
			else
			{
				status =0;
			}
		}
		else
		{
			status =-1;
		}
		if(remote_send_status == 0)
		{
			status =1;
			logger.info("发送短信验证码:"+sms_code);
		}
		Row smsRow =new Row();
		smsRow.put("to_account",telephone );
		smsRow.put("sms_code", sms_code);
		smsRow.put("sms_content", sms_content);
		smsRow.put("status", status);
		//1房东租客版2中介版
		smsRow.put("type", "2");
		int rowNum =smsService.insert(smsRow);
		if(rowNum <= 0)
		{
			ovo =new OVO(-1,"发送短信验证码失败","发送短信验证码失败");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		ovo =new OVO(0,"发送短信验证码成功","");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

}
