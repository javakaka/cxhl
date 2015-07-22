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

import com.cxhl.service.ShopGiftService;
import com.cxhl.service.ShopService;
import com.cxhl.service.ShopTypeService;
import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.ResponseVO;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

@Controller("cxhlPlatformShopGiftController")
@RequestMapping("/cxhlpage/platform/shop/gift")
public class ShopGiftContrller  extends BaseController{
	
	@Resource(name = "cxhlShopTypeService")
	private ShopTypeService shopTypeService;
	
	@Resource(name = "cxhlShopService")
	private ShopService shopService;
	
	@Resource(name = "cxhlShopGiftService")
	private ShopGiftService shopGiftService;
	
	
	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Pageable pageable, ModelMap model) {
		shopGiftService.getRow().put("pageable", pageable);
		Page page = shopGiftService.queryPage();
		model.addAttribute("page", page);
		return "/cxhlpage/platform/shop/gift/list";
	}
	
	/**
	 * 分页选择
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/SelectGift")
	public String selectUserList(String id,Pageable pageable, ModelMap model) {
		shopGiftService.getRow().put("pageable", pageable);
		Page page = shopGiftService.queryPage();
		model.addAttribute("page", page);
		if(StringUtils.isEmptyOrNull(id))
		{
			id ="";
		}
		model.addAttribute("id", id);
		shopGiftService.getRow().clear();
		return "/cxhlpage/platform/shop/gift/SelectGift";
	}
	
	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		DataSet shop_list =shopService.queryAllShop();
		model.addAttribute("shop_list", shop_list);
		return "/cxhlpage/platform/shop/gift/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseVO save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row row =MapUtils.convertMaptoRowWithoutNullField(map);
		String id=row.getString("id","");
		String total_num =row.getString("total_num","0");
		if(StringUtils.isEmptyOrNull(id))
		{
			row.put("left_num", total_num);
			shopGiftService.insert(row);
		}
		else
		{
			shopGiftService.update(row);
		}
		ResponseVO ovo =new ResponseVO(0, "保存成功");
		id =row.getString("id");
		return ovo;
	}

	@RequestMapping(value = "/edit")
	public String edit(Long id, ModelMap model) throws Exception {
		Assert.notNull(id);
		DataSet shop_list =shopService.queryAllShop();
		model.addAttribute("shop_list", shop_list);
		Row row =shopGiftService.find(String.valueOf(id));
		model.addAttribute("row", row);
		return "/cxhlpage/platform/shop/gift/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row row=MapUtils.convertMaptoRowWithoutNullField(map);
		shopGiftService.update(row);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(String[] ids) {
		shopGiftService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
	/**
	 * 检查名称是否已存在
	 */
	@RequestMapping(value = "/check_name", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkName(String NAME) {
		if(StringUtils.isEmptyOrNull(NAME))
		{
			return false;
		}
		return (shopTypeService.isNameExisted(NAME));
	}
	
	/**
	 * 检查名称是否已存在
	 */
	@RequestMapping(value = "/check_extra_name", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkExtraName(String ID,String NAME) {
		if(StringUtils.isEmptyOrNull(NAME))
		{
			return false;
		}
		return (shopTypeService.isExtraNameExisted(ID,NAME));
	}
	
}
