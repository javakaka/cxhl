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

import com.cxhl.service.AgentWithdrawService;
import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**
 * 中介用户提现申请业务管理器
 */
@Controller("fzbPlatformAgentWithdrawController")
@RequestMapping("/fzbpage/platform/withdraw")
/**
 * 中介用户提现
 * @author Administrator
 */
public class AgentWithDrawContrller  extends BaseController{

	@Resource(name = "fzbWithdrawService")
	private AgentWithdrawService agentWithdrawService;

	
	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/list")
	public String list(String user_id, String status, Pageable pageable, ModelMap model) 
			throws Exception {
		Page page = agentWithdrawService.queryPage(user_id, status, pageable);
		model.addAttribute("page", page);
		model.addAttribute("user_id", user_id);
		model.addAttribute("status", status);
		return "/fzbpage/platform/withdraw/list";
	}

	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		return "/fzbpage/platform/withdraw/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) {
		Row rewardRow =MapUtils.convertMaptoRowWithoutNullField(map);
		rewardRow.remove("USER_NAME");
		String user_id =rewardRow.getString("user_id",null);
		String price =rewardRow.getString("price",null);
		String status =rewardRow.getString("status",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			addFlashMessage(redirectAttributes, Message.error("用户编号不能为空"));
			return "redirect:add.do";
		}
		if(StringUtils.isEmptyOrNull(price))
		{
			addFlashMessage(redirectAttributes, Message.error("奖励金额不能为空"));
			return "redirect:add.do";
		}
		if(StringUtils.isEmptyOrNull(status))
		{
			addFlashMessage(redirectAttributes, Message.error("状态不能为空"));
			return "redirect:add.do";
		}
		String curDate =DateUtil.getCurrentDate();
		String year =curDate.substring(0,4);
		String month =curDate.substring(5,7);
		String day =curDate.substring(8,10);
		rewardRow.put("year", year);
		rewardRow.put("month", month);
		rewardRow.put("day", day);
		rewardRow.put("date", curDate);
		agentWithdrawService.insert(rewardRow);
		addFlashMessage(redirectAttributes, Message.success("添加成功"));
		return "redirect:list.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(String id, ModelMap model) throws Exception {
		Assert.notNull(id);
		Row row =agentWithdrawService.findById(id);
		String name =row.getString("name","");
		if(! StringUtils.isEmptyOrNull(name))
		{
			name =AesUtil.decode(name);
			row.put("name", name);
		}
		model.addAttribute("row",row);
		return "/fzbpage/platform/withdraw/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) {
		Row row=MapUtils.convertMaptoRowWithoutNullField(map);
		row.remove("USER_NAME");
		String id =row.getString("id",null);
		String user_id =row.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			addFlashMessage(redirectAttributes, Message.error("编号不能为空"));
			return "redirect:list.do";
		}
		if(StringUtils.isEmptyOrNull(user_id))
		{
			addFlashMessage(redirectAttributes, Message.error("用户编号不能为空"));
			return "redirect:edit.do?id="+id;
		}
		agentWithdrawService.update(row);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.do";
	}
	
	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		Assert.notNull(ids);
		agentWithdrawService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}
