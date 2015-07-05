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

import com.cxhl.service.ShopService;
import com.cxhl.service.ShopTypeService;
import com.cxhl.service.UserService;
import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.Row;

@Controller("cxhlPlatformShopController")
@RequestMapping("/cxhlpage/platform/shop/profile")
public class ShopContrller  extends BaseController{

	@Resource(name = "cxhlUserService")
	private UserService userService;
	
	@Resource(name = "cxhlShopTypeService")
	private ShopTypeService shopTypeService;
	
	@Resource(name = "cxhlShopService")
	private ShopService shopService;
	
	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Pageable pageable, ModelMap model) {
		shopService.getRow().put("pageable", pageable);
		Page page = shopService.queryPage();
		model.addAttribute("page", page);
		userService.getRow().clear();
		return "/cxhlpage/platform/shop/profile/list";
	}
	

	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		return "/cxhlpage/platform/shop/profile/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row row =MapUtils.convertMaptoRowWithoutNullField(map);
		shopTypeService.insert(row);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(Long id, ModelMap model) throws Exception {
		Assert.notNull(id);
		Row row =shopTypeService.find(String.valueOf(id));
		model.addAttribute("row", row);
		return "/cxhlpage/platform/shop/profile/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row row=MapUtils.convertMaptoRowWithoutNullField(map);
		shopTypeService.update(row);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(String[] ids) {
		shopTypeService.delete(ids);
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
