package com.fbsf.fzb.job;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ezcloud.framework.service.Service;
import com.fbsf.fzb.service.SMSService;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2015-4-28 上午10:49:35  
 * 类说明: 每隔5分钟，将短信记录表的记录设置为过期状态
 */
@Component("fzbResetSmsStatusJob")
@Lazy(false)
public class SmsResetJob extends Service{

	@Resource(name = "fzbSMSService")
	private SMSService smsService;
	
	@Scheduled(cron = "${job.fzb.sms_reset.cron}")
	public void setSmsCodeTimeOut()
	{
		smsService.setCodeTimeOut();
		System.out.println("-----------设置短信状态为已过期    操作成功！！----------------");
	}
	
}
