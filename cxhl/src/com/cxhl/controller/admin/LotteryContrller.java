package com.cxhl.controller.admin;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cxhl.service.ShopCouponService;
import com.cxhl.service.UserCouponService;
import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.service.system.SystemConfigService;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

@Controller("cxhlPlatformLotteryController")
@RequestMapping("/cxhlpage/platform/lottery")
public class LotteryContrller  extends BaseController{

	@Resource(name = "frameworkSystemConfigService")
	private SystemConfigService systemConfigService;
	
	@Resource(name = "cxhlShopCouponService")
	private ShopCouponService shopCouponService;
	
	@Resource(name = "cxhlUserCouponService")
	private UserCouponService userCouponService;
	
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
		String radio_times ="";
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
				else if(busi_code.equals("RADIO_TIMES"))
				{
					radio_times =row.getString("busi_code_set","");
				}
			}
		}
		model.addAttribute("lottery_switch", lottery_switch);
		model.addAttribute("probability", probability);
		model.addAttribute("day_num", day_num);
		model.addAttribute("week_num", week_num);
		model.addAttribute("month_num", month_num);
		model.addAttribute("busi_type", "CXHL_LOTTERY");
		model.addAttribute("radio_times", radio_times);
		return "/cxhlpage/platform/lottery/setting";
	}
	
	/**
	 * 赠送餐券
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/present")
	public String present(ModelMap model) {
		return "/cxhlpage/platform/lottery/present";
	}
	
	@RequestMapping(value = "/savePresent")
	public String savePresent(String USER_ID,String COUPON_ID,String NUM,ModelMap model,RedirectAttributes redirectAttributes) 
	{
		Assert.notNull(USER_ID, "");
		Assert.notNull(USER_ID, "");
		Assert.notNull(NUM, "");
		if(! NumberUtils.isNumber(NUM))
		{
			addFlashMessage(redirectAttributes, Message.error("数量应该是数字"));
			return "redirect:present.do";
		}
		Row row =new Row();
		row.put("user_id", USER_ID);
		row.put("coupon_id", COUPON_ID);
		row.put("state", "1");
		String price ="";
		Row srow =shopCouponService.find(COUPON_ID);
		if(srow == null)
		{
			addFlashMessage(redirectAttributes, Message.error("优惠券不存在"));
			return "redirect:present.do";
		}
		String shop_coupon_num =srow.getString("left_num","0");
		int ishop_coupon_num =Integer.parseInt(shop_coupon_num);
		if(ishop_coupon_num <= 0)
		{
			addFlashMessage(redirectAttributes, Message.error("优惠券剩余数量不足"));
			return "redirect:present.do";
		}
		int minus =ishop_coupon_num -Integer.parseInt(NUM);
		if(minus <= 0)
		{
			addFlashMessage(redirectAttributes, Message.error("优惠券剩余数量不足"));
			return "redirect:present.do";
		}
		price =srow.getString("raw_price","0");
		row.put("price", price);
		row.put("num", NUM);
		row.put("channel", "3");//1刮奖获得2购买获得3赠送
		String pay_code =userCouponService.getPayCode();
		row.put("pay_code", pay_code);
		userCouponService.insert(row);
		//减少商家的优惠券
		srow.put("left_num", minus);
		srow =MapUtils.convertMaptoRowWithoutNullField(srow);
		shopCouponService.update(srow);
		addFlashMessage(redirectAttributes, Message.success("赠送成功"));
		return "redirect:present.do";
	}
	
	
	@RequestMapping(value = "/SaveSetting")
	public String save(String lottery_switch, String probability,String day_num,
			String week_num,String month_num,String radio_times,ModelMap model,RedirectAttributes redirectAttributes) {
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
		if(!StringUtils.isEmptyOrNull(radio_times))
		{
			systemConfigService.setConfigData(busi_type,"RADIO_TIMES",radio_times,"电台抽奖时间段");
			
		}
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:setting.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		return SUCCESS_MESSAGE;
	}
	
	
}
