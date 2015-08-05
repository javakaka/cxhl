package com.cxhl.job;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cxhl.util.SmsWebServiceUtil;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.service.system.SystemConfigService;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.util.StringUtils;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2015-4-28 上午10:49:35  
 * 类说明: 每天早上9点发送短信
 */
@Component("cxhlDailyAdminSmsReportJob")
@Lazy(false)
public class SmsAdminReportJob extends Service{

	private static Logger logger = Logger.getLogger(SmsAdminReportJob.class);
	@Resource(name = "frameworkSystemConfigService")
	private SystemConfigService systemConfigService;
	
	@Scheduled(cron = "${job.cxhl.sms.admin.daily.report.cron}")
	public void sendDailyAdminSmsReport()
	{
		logger.info("-----------每天早上9点发送短信！！----------------");
		int sms_code =NumberUtils.getSixRandomNumber();
		String sms_content ="1074吃香喝辣带您免费吃遍南宁，感谢您的注册，您的验证码是："+sms_code+" ";
		String sms_switch =systemConfigService.querySingleConfig("APP_SMS_INTERFACE", "SWITCH");
		String sms_url =systemConfigService.querySingleConfig("APP_SMS_INTERFACE", "URL");
		String sms_sn =systemConfigService.querySingleConfig("APP_SMS_INTERFACE", "USERNAME");
		String sms_pwd =systemConfigService.querySingleConfig("APP_SMS_INTERFACE", "PASSWORD");
		String sms_cgid =systemConfigService.querySingleConfig("APP_SMS_INTERFACE", "CGID");
		String sms_csid =systemConfigService.querySingleConfig("APP_SMS_INTERFACE", "CSID");
		String sms_admin_telephone =systemConfigService.querySingleConfig("APP_SMS_INTERFACE", "ADMINTELEPHONE");
		String telephone_arr[] =null;
		String telephone ="";
		int remote_send_status =-1;
		if(sms_switch.equals("1"))
		{
			if(! StringUtils.isEmptyOrNull(sms_admin_telephone))
			{
				telephone_arr =sms_admin_telephone.split(",");
				for(int i=0; i<telephone_arr.length; i++)
				{
					telephone =telephone_arr[i];
					if(! StringUtils.isEmptyOrNull(telephone) && StringUtils.isTelphone(telephone))
					{
						remote_send_status =SmsWebServiceUtil.sendC123Sms(sms_url, sms_sn, sms_pwd, 
								Integer.parseInt(sms_cgid), Integer.parseInt(sms_csid), 
								telephone, sms_content);
						if(remote_send_status > 0)
						{
							logger.info("-----------每天早上9点发送短信 ["+telephone+"]发送成功！！----------------");
						}
						else
						{
							logger.info("-----------每天早上9点发送短信 ["+telephone+"]发送失败！！----------------");
						}
					}
				}
			}
		}
		logger.info("-----------每天早上9点发送短信 发送完毕！！----------------");
	}
	
}
