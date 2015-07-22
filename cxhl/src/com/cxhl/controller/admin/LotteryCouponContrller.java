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

import com.cxhl.service.LotteryCouponService;
import com.cxhl.service.ShopCouponService;
import com.cxhl.service.ShopService;
import com.cxhl.service.ShopTypeService;
import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

@Controller("cxhlPlatformLotteryCouponController")
@RequestMapping("/cxhlpage/platform/lottery/coupon")
public class LotteryCouponContrller  extends BaseController{
	
	@Resource(name = "cxhlShopTypeService")
	private ShopTypeService shopTypeService;
	
	@Resource(name = "cxhlShopService")
	private ShopService shopService;
	
	@Resource(name = "cxhlShopCouponService")
	private ShopCouponService shopCouponService;
	
	@Resource(name = "cxhlLotteryCouponService")
	private LotteryCouponService lotteryCouponService;
	
	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Pageable pageable, ModelMap model) {
		lotteryCouponService.getRow().put("pageable", pageable);
		Page page = lotteryCouponService.queryPage();
		model.addAttribute("page", page);
		return "/cxhlpage/platform/lottery/coupon/list";
	}
	

	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		//商家列表
		DataSet shop_list =shopService.queryAllShop();
		model.addAttribute("shop_list", shop_list);
		return "/cxhlpage/platform/lottery/coupon/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String  save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row row =MapUtils.convertMaptoRowWithoutNullField(map);
		String lottery_num =row.getString("lottery_num","");
		String coupon_id =row.getString("coupon_id","");
		Row srow =shopCouponService.find(coupon_id);
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
		int minus =ishop_coupon_num -Integer.parseInt(lottery_num);
		if(minus <= 0)
		{
			addFlashMessage(redirectAttributes, Message.error("优惠券剩余数量不足"));
			return "redirect:present.do";
		}
		
		if(!StringUtils.isEmptyOrNull(lottery_num))
		{
			row.put("left_num", lottery_num);
		}
		lotteryCouponService.insert(row);
		//减少商家的优惠券
		srow.put("left_num", minus);
		srow =MapUtils.convertMaptoRowWithoutNullField(srow);
		shopCouponService.update(srow);
		addFlashMessage(redirectAttributes, Message.success("操作成功"));
		return "redirect:list.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(Long id, ModelMap model) throws Exception {
		Assert.notNull(id);
		Row row =lotteryCouponService.find(String.valueOf(id));
		model.addAttribute("row", row);
		return "/cxhlpage/platform/lottery/coupon/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row row=MapUtils.convertMaptoRowWithoutNullField(map);
		lotteryCouponService.update(row);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(String[] ids) {
		lotteryCouponService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
}
