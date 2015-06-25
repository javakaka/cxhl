package com.cxhl.controller.mobile;

import java.math.BigDecimal;

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
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;

@Controller("mobileAgentWithdrawController")
@RequestMapping("/api/agent/withdraw")
public class AgentWithdrawController extends BaseController {
	
	private static Logger logger = Logger.getLogger(AgentWithdrawController.class); 
	@Resource(name = "fzbAgentService")
	private AgentService agentService;
	
	@Resource(name = "fzbWithdrawService")
	private AgentWithdrawService agentWithdrawService;

	@Resource(name = "fzbAgentRewardService")
	private AgentRewardService agentRewardService;
	
	/**
	 * 根据用户编号查询申请提现记录列表
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/list")
	public @ResponseBody
	String queryAllAd(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("根据用户编号查询申请提现记录列表");
		OVO ovo =new OVO(0,"","");
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-10011,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row agentRow =agentService.find(user_id);
		if(agentRow == null)
		{
			ovo =new OVO(-10011,"用户不存在","用户不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet ds =agentWithdrawService.getPageListByUserId(user_id,page,page_size);
		ovo =new OVO(0,"操作成功","");
		ovo.set("list",ds);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 申请提现
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/request")
	public @ResponseBody
	String requestWithdraw(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("申请提现");
		String user_id=ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-10011,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row agentRow =agentService.find(user_id);
		if(agentRow == null)
		{
			ovo =new OVO(-10011,"用户不存在","用户不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String money =ivo.getString("money","");
		if(StringUtils.isEmptyOrNull(money))
		{
			ovo =new OVO(-10011,"提现金额不能为空","提现金额不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row row =new Row();
		row.put("user_id", user_id);
		row.put("money", money);
		row.put("status", "1");
		//计算提现前的奖金总金额,奖金总金额=全部奖励-已提现成功的奖励金额
		//全部奖励
		String total =agentRewardService.queryTotalReward(user_id);
		//已提现成功的奖励金额
		String withdrawSuccessedMoney =String.valueOf(agentWithdrawService.queryWithdrawSuccessTotalMoney(user_id));
		BigDecimal total_big = new BigDecimal(total);
		BigDecimal with_draw_success_big = new BigDecimal(withdrawSuccessedMoney);
		BigDecimal settle_money_big =total_big.subtract(with_draw_success_big);
		settle_money_big =settle_money_big.setScale(2, BigDecimal.ROUND_HALF_UP);
		row.put("total_money", settle_money_big.toString());
		row.put("settle_money", settle_money_big.toString());
		agentWithdrawService.insert(row);
		ovo =new OVO(0,"操作成功","");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

}
