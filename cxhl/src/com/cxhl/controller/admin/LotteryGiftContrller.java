package com.cxhl.controller.admin;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cxhl.service.LotteryGiftService;
import com.cxhl.service.ShopCouponService;
import com.cxhl.service.ShopService;
import com.cxhl.service.ShopTypeService;
import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.Row;

@Controller("cxhlPlatformLotteryGiftController")
@RequestMapping("/cxhlpage/platform/lottery/gift")
public class LotteryGiftContrller  extends BaseController{
	
	@Resource(name = "cxhlShopTypeService")
	private ShopTypeService shopTypeService;
	
	@Resource(name = "cxhlShopService")
	private ShopService shopService;
	
	@Resource(name = "cxhlShopCouponService")
	private ShopCouponService shopCouponService;
	
	@Resource(name = "cxhlLotteryGiftService")
	private LotteryGiftService lotteryGiftService;
	
	
	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Pageable pageable, ModelMap model) {
		lotteryGiftService.getRow().put("pageable", pageable);
		Page page = lotteryGiftService.queryPage();
		model.addAttribute("page", page);
		return "/cxhlpage/platform/lottery/gift/list";
	}
	

	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		return "/cxhlpage/platform/lottery/gift/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String  save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row row =MapUtils.convertMaptoRowWithoutNullField(map);
		String lottery_num =row.getString("lottery_num","");
		if(!StringUtils.isEmptyOrNull(lottery_num))
		{
			row.put("left_num", lottery_num);
		}
		lotteryGiftService.insert(row);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(Long id, ModelMap model) throws Exception {
		Assert.notNull(id);
		Row row =lotteryGiftService.find(String.valueOf(id));
		model.addAttribute("row", row);
		return "/cxhlpage/platform/lottery/gift/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row row=MapUtils.convertMaptoRowWithoutNullField(map);
		lotteryGiftService.update(row);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(String[] ids) {
		lotteryGiftService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
}
