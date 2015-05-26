package com.fbsf.fzb.job;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ezcloud.framework.service.Service;
import com.fbsf.fzb.service.RoomService;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2015-4-28 上午10:49:35  
 * 类说明: 每天0点0分，将签约中的房源记录设置为待租状态，并清空起租日期、结束日期、每月收款日、水、电、燃气、物业管理费
 */
@Component("fzbResetRoomStatusJob")
@Lazy(false)
public class ResetRoomStatusJob extends Service{

	@Resource(name = "fzbRoomService")
	private RoomService roomService;
	
	@Scheduled(cron = "${job.fzb.room_reset.cron}")
	public void setSmsCodeTimeOut()
	{
		roomService.resetRoomStatus();
		System.out.println("-----------清空签约中的房源    操作成功！！----------------");
	}
	
}