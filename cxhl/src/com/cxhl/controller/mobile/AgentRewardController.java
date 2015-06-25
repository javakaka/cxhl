package com.cxhl.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.AgentRewardService;
import com.cxhl.service.AgentService;
import com.cxhl.service.AgentWithdrawService;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.DateUtil;

@Controller("mobileAgentRewardController")
@RequestMapping("/api/agent/reward")
/**
 * 中介奖励
 * @author Administrator
 *
 */
public class AgentRewardController extends BaseController {
	
	private static Logger logger = Logger.getLogger(AgentRewardController.class); 
	
	@Resource(name = "fzbAgentService")
	private AgentService agentService;
	
	@Resource(name = "fzbAgentRewardService")
	private AgentRewardService agentRewardService;

	@Resource(name = "fzbWithdrawService")
	private AgentWithdrawService agentWithdrawService;
	
	
	/**
	 * 月度奖励
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/month")
	public @ResponseBody
	String queryAgentMonthReward(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("月度奖励");
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-10012,"用户编号不能为空","用户编号不能为空");
		}
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet ds =agentRewardService.queryPage(user_id, Integer.parseInt(page), Integer.parseInt(page_size));
		ovo =new OVO(0,"查询成功","");
		ovo.set("list", ds);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 全部奖励
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/total")
	public @ResponseBody
	String queryAgentTotalReward(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("全部奖励");
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-10012,"用户编号不能为空","用户编号不能为空");
		}
		String total =agentRewardService.queryTotalReward(user_id);
		ovo =new OVO(0,"查询成功","");
		ovo.set("total", total);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 本月奖励
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/current_month")
	public @ResponseBody
	String queryAgentCurrentMonthReward(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("本月奖励");
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-10012,"用户编号不能为空","用户编号不能为空");
		}
		String cur_month=DateUtil.getCurrentDate().substring(0,7);
		String cur_month_reward =agentRewardService.queryMonthReward(user_id, cur_month);
		ovo =new OVO(0,"查询成功","");
		ovo.set("cur_month_reward", cur_month_reward);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

	/**
	 * 奖励汇总
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/summary")
	public @ResponseBody
	String queryAgentSummaryReward(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("奖励汇总");
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-10012,"用户编号不能为空","用户编号不能为空");
		}
		//本月奖励金额
		String cur_month=DateUtil.getCurrentDate().substring(0,7);
		String cur_month_reward =agentRewardService.queryMonthReward(user_id, cur_month);
		//全部奖励
		String total =agentRewardService.queryTotalReward(user_id);
		//已提现成功的奖励金额
		String withdrawSuccessedMoney =String.valueOf(agentWithdrawService.queryWithdrawSuccessTotalMoney(user_id));
		withdrawSuccessedMoney =NumberUtils.getTwoDecimal(withdrawSuccessedMoney);
		//提现申请中的奖励金额
		String withdrawProccessingMoney =String.valueOf(agentWithdrawService.queryWithdrawProcessingTotalMoney(user_id));
		withdrawProccessingMoney =NumberUtils.getTwoDecimal(withdrawProccessingMoney);
		ovo =new OVO(0,"查询成功","");
		ovo.set("cur_month_reward", cur_month_reward);
		ovo.set("total", total);
		ovo.set("cashed_money", withdrawSuccessedMoney);
		ovo.set("proccessing_money", withdrawProccessingMoney);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
}
