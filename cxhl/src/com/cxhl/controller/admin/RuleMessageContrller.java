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

import com.cxhl.service.RuleMessageService;
import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.vo.Row;

@Controller("fzbPlatformRuleMessageController")
@RequestMapping("/fzbpage/platform/rule_message")
public class RuleMessageContrller  extends BaseController{
	
	@Resource(name = "fzbRuleMessageService")
	private RuleMessageService ruleMessageService;

	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Pageable pageable, ModelMap model) {
		Page page = ruleMessageService.queryPage(pageable);
		model.addAttribute("page", page);
		ruleMessageService.getRow().clear();
		return "/fzbpage/platform/rule_message/list";
	}
	
	
	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		return "/fzbpage/platform/rule_message/add";
	}	

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) {
		Row row =MapUtils.convertMaptoRowWithoutNullField(map);
		ruleMessageService.insert(row);
		addFlashMessage(redirectAttributes, Message.success("添加成功"));
		return "redirect:list.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(String id, ModelMap model) {
		Assert.notNull(id);
		model.addAttribute("row", ruleMessageService.findById(id));
		return "/fzbpage/platform/rule_message/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) {
		Row adRow=MapUtils.convertMaptoRowWithoutNullField(map);
		ruleMessageService.update(adRow);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		ruleMessageService.delete(ids);
		return SUCCESS_MESSAGE;
	}
}
