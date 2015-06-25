package com.cxhl.controller.admin;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cxhl.service.CustomerTipsService;
import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.Row;

/**
 * 用户反馈业务管理器
 */
@Controller("fzbPlatformCustomerTipsController")
@RequestMapping("/fzbpage/platform/customer_tips")

public class CustomerTipsContrller  extends BaseController{

	@Resource(name = "fzbCustomerTipsService")
	private CustomerTipsService customerTipsService;


	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/list")
	public String list(String status, Pageable pageable, ModelMap model) 
			throws Exception {
		Page page = customerTipsService.queryPage(status, pageable);
		model.addAttribute("page", page);
		model.addAttribute("status", status);
		return "/fzbpage/platform/customer_tips/list";
	}

	@RequestMapping(value = "/edit")
	public String edit(String id, ModelMap model) throws Exception {
		Assert.notNull(id);
		Row row =customerTipsService.findById(id);
		String name =row.getString("name","");
		if(! StringUtils.isEmptyOrNull(name))
		{
			name =AesUtil.decode(name);
			row.put("name", name);
		}
		model.addAttribute("row",row);
		return "/fzbpage/platform/customer_tips/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) {
		Row row=MapUtils.convertMaptoRowWithoutNullField(map);
		row.remove("USER_NAME");
		String id =row.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			addFlashMessage(redirectAttributes, Message.error("编号不能为空"));
			return "redirect:list.do";
		}
		row.put("status", "1");//已回复
		customerTipsService.update(row);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.do";
	}
	
	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		Assert.notNull(ids);
		customerTipsService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}
