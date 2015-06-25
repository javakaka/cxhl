package com.cxhl.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.AdService;
import com.cxhl.service.AgentRewardService;
import com.cxhl.service.AgentService;
import com.cxhl.service.AgentWithdrawService;
import com.ezcloud.framework.common.Setting;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.util.SettingUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.DateUtil;

@Controller("mobileAgentPageController")
@RequestMapping("/api/agent/page")
public class AgentPageController extends BaseController {
	
	private static Logger logger = Logger.getLogger(AgentPageController.class); 
	
	@Resource(name = "fzbAgentService")
	private AgentService agentService;
	
	@Resource(name = "fzbAgentRewardService")
	private AgentRewardService agentRewardService;

	@Resource(name = "fzbAdService")
	private AdService adService;
	
	@Resource(name = "fzbWithdrawService")
	private AgentWithdrawService agentWithdrawService;
	
	/**
	 * 首页
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value ="/index")
	public @ResponseBody
	String queryAgentCustomerList(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		
		//广告列表
		DataSet adDataSet =adService.list();
		Setting setting =SettingUtils.get();
		String siteUrl =setting.getSiteUrl();
		String siteDomain ="";
		int iPos =siteUrl.lastIndexOf("/");
		if(iPos !=-1)
		{
			siteDomain =siteUrl.substring(0,iPos);
		}
		if(! StringUtils.isEmptyOrNull(siteDomain))
		{
			for(int i=0; i<adDataSet.size(); i++)
			{
				Row adRow =(Row)adDataSet.get(i);
				String picture =adRow.getString("picture","");
				if(! StringUtils.isEmptyOrNull(picture))
				{
					picture	=siteDomain+picture;
					adRow.put("picture", picture);
					adDataSet.set(i, adRow);
				}
			}
		}
		OVO ovo =new OVO(0,"查询成功","");
		ovo.set("ad_list", adDataSet);
		
		logger.info("本月奖励");
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String cur_month=DateUtil.getCurrentDate().substring(0,7);
		String cur_month_reward =agentRewardService.queryMonthReward(user_id, cur_month);
		ovo.set("cur_month_reward", cur_month_reward);
		String total =agentRewardService.queryTotalReward(user_id);
		ovo.set("total", total);
		//已提现成功的奖励金额
		String withdrawSuccessedMoney =String.valueOf(agentWithdrawService.queryWithdrawSuccessTotalMoney(user_id));
		withdrawSuccessedMoney =NumberUtils.getTwoDecimal(withdrawSuccessedMoney);
		//提现申请中的奖励金额
		String withdrawProccessingMoney =String.valueOf(agentWithdrawService.queryWithdrawProcessingTotalMoney(user_id));
		withdrawProccessingMoney =NumberUtils.getTwoDecimal(withdrawProccessingMoney);
		ovo.set("cashed_money", withdrawSuccessedMoney);
		ovo.set("proccessing_money", withdrawProccessingMoney);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

}
