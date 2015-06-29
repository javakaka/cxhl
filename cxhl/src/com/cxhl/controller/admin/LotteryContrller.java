package com.cxhl.controller.admin;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.service.system.SystemConfigService;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

@Controller("cxhlPlatformLotteryController")
@RequestMapping("/cxhlpage/platform/lottery")
public class LotteryContrller  extends BaseController{

	@Resource(name = "frameworkSystemConfigService")
	private SystemConfigService systemConfigService;
	
	/**
	 *抽奖全局参数配置
	 * @return
	 */
	@RequestMapping(value = "/setting")
	public String list(ModelMap model) {
		DataSet ds =systemConfigService.getConfigData("CXHL_LOTTERY");
//		抽奖开关
		String lottery_switch ="";
//		中奖概率
		String probability ="";
//		每天中奖次数
		String day_num ="";
//		周中奖次数
		String week_num ="";
//		月中奖次数
		String month_num ="";
		String busi_code="";
		if( ds != null && ds.size()>0 )
		{
			for(int i=0; i< ds.size(); i++)
			{
				Row row =(Row)ds.get(i);
				busi_code =row.getString("busi_code","");
				if(busi_code.equals("SWITCH"))
				{
					lottery_switch =row.getString("busi_code_set","");
				}
				else if(busi_code.equals("PROBABILITY"))
				{
					probability =row.getString("busi_code_set","");
				}
				else if(busi_code.equals("DAY_NUM"))
				{
					day_num =row.getString("busi_code_set","");
				}
				else if(busi_code.equals("WEEK_NUM"))
				{
					week_num =row.getString("busi_code_set","");
				}
				else if(busi_code.equals("MONTH_NUM"))
				{
					month_num =row.getString("busi_code_set","");
				}
			}
		}
		model.addAttribute("lottery_switch", lottery_switch);
		model.addAttribute("probability", probability);
		model.addAttribute("day_num", day_num);
		model.addAttribute("week_num", week_num);
		model.addAttribute("month_num", month_num);
		model.addAttribute("busi_type", "CXHL_LOTTERY");
		return "/cxhlpage/platform/lottery/setting";
	}
	
	
	@RequestMapping(value = "/SaveSetting")
	public String save(String lottery_switch, String probability,String day_num,
			String week_num,String month_num,ModelMap model,RedirectAttributes redirectAttributes) {
		Assert.notNull(lottery_switch, "lottery_switch 不能为空");
		Assert.notNull(probability, "probability 不能为空");
		Assert.notNull(day_num, "day_num 不能为空");
		Assert.notNull(week_num, "week_num 不能为空");
		Assert.notNull(month_num, "month_num 不能为空");
		String busi_type="CXHL_LOTTERY";
		systemConfigService.setConfigData(busi_type,"SWITCH",lottery_switch,"抽奖开关");
		systemConfigService.setConfigData(busi_type,"PROBABILITY",probability,"中奖概率");
		systemConfigService.setConfigData(busi_type,"DAY_NUM",day_num,"每日中奖次数");
		systemConfigService.setConfigData(busi_type,"WEEK_NUM",week_num,"周中奖次数");
		systemConfigService.setConfigData(busi_type,"MONTH_NUM",month_num,"每月中奖次数");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:setting.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		return SUCCESS_MESSAGE;
	}
}
