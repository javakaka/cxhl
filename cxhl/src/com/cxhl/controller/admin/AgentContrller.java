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

import com.cxhl.service.AgentService;
import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.Md5Util;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

@Controller("fzbPlatformAgentController")
@RequestMapping("/fzbpage/platform/agent")
public class AgentContrller  extends BaseController{

	@Resource(name = "fzbAgentService")
	private AgentService agentService;
	
	/**
	 * 分页查询中介用户
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/AgentList")
	public String list(Pageable pageable, ModelMap model) {
		agentService.getRow().put("pageable", pageable);
		Page page = agentService.queryPage();
		model.addAttribute("page", page);
		agentService.getRow().clear();
		return "/fzbpage/platform/agent/AgentList";
	}
	
	@RequestMapping(value = "/LatestAgentList")
	public String queryTop5Agents(Pageable pageable, ModelMap model) {
		agentService.getRow().put("pageable", pageable);
		DataSet ds= agentService.queryTop5Agents();
		model.addAttribute("data", ds);
		agentService.getRow().clear();
		return "/fzbpage/platform/webpart/LatestAgentList";
	}
	
	/**
	 * 选择中介用户--分页查询中介用户
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/SelectAgent")
	public String selectPage(Pageable pageable, String id,ModelMap model) {
		agentService.getRow().put("pageable", pageable);
		Page page = agentService.queryPage();
		model.addAttribute("page", page);
		if(StringUtils.isEmptyOrNull(id))
		{
			id="";
		}
		model.addAttribute("id", id);
		agentService.getRow().clear();
		return "/fzbpage/platform/agent/SelectAgent";
	}

	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		return "/fzbpage/platform/agent/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row agentRow =MapUtils.convertMaptoRowWithoutNullField(map);
		String telephone =agentRow.getString("telephone",null);
		String password =agentRow.getString("password",null);
		password =Md5Util.Md5(password);
		String email =agentRow.getString("email","");
		String name =agentRow.getString("name","");
		String sex =agentRow.getString("sex","");
		String bank_card_no =agentRow.getString("bank_card_no","");
		if(!StringUtils.isEmptyOrNull(name))
		{
			name =AesUtil.encode(name);
		}
		if(!StringUtils.isTelphone(telephone))
		{
			addFlashMessage(redirectAttributes, Message.error("手机号码错误"));
			return "redirect:AgentList.do";
		}
		boolean bool =agentService.isPhoneExisted(telephone);
		if(bool)
		{
			addFlashMessage(redirectAttributes, Message.error("手机号已被注册"));
			return "redirect:AgentList.do";
		}
		if(!StringUtils.isEmptyOrNull(email))
		{
			if(!StringUtils.isEmail(email))
			{
				addFlashMessage(redirectAttributes, Message.error("邮箱格式错误"));
				return "redirect:AgentList.do";
			}
			bool =agentService.isEmailExisted(email);
			if(bool)
			{
				addFlashMessage(redirectAttributes, Message.error("邮箱已被绑定"));
				return "redirect:AgentList.do";
			}
		}
		if(!StringUtils.isEmptyOrNull(bank_card_no))
		{
			if(!NumberUtils.isPositiveNumber(bank_card_no))
			{
				addFlashMessage(redirectAttributes, Message.error("银行帐号应该是数字"));
				return "redirect:AgentList.do";
			}
			bank_card_no =AesUtil.encode(bank_card_no);
		}
		Row row =new Row();
		row.put("name", name);
		row.put("telephone", telephone);
		row.put("username", telephone);
		row.put("email", email);
		row.put("password", password);
		row.put("bank_card_no", bank_card_no);
		row.put("sex", sex);
		String time =DateUtil.getCurrentDateTime();
		row.put("register_time", time);
		agentService.insert(row);
		addFlashMessage(redirectAttributes, Message.success("添加成功"));
		return "redirect:AgentList.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(Long id, ModelMap model) {
		Assert.notNull(id);
		Row agentRow =agentService.find(String.valueOf(id));
		String name =agentRow.getString("name","");
		String bank_card_no =agentRow.getString("bank_card_no","");
		try {
			if(! StringUtils.isEmptyOrNull(name))
			{
				name =AesUtil.decode(name);
			}
			if(! StringUtils.isEmptyOrNull(bank_card_no))
			{
				bank_card_no =AesUtil.decode(bank_card_no);
			}
		} catch (Exception e) {
			name ="";
			bank_card_no ="";
		}
		agentRow.put("name", name);
		agentRow.put("bank_card_no", bank_card_no);
		model.addAttribute("row", agentRow);
		return "/fzbpage/platform/agent/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) {
		Row userRow=MapUtils.convertMaptoRowWithoutNullField(map);
		String email =userRow.getString("email",null);
		String id =userRow.getString("id",null);
		boolean email_existed =false;
		if(!StringUtils.isEmptyOrNull(email))
		{
			if(! StringUtils.isEmail(email))
			{
				addFlashMessage(redirectAttributes, Message.warn("邮箱格式错误"));
				return "redirect:edit.do?id="+id;
			}
			email_existed =agentService.isEmailExisted(id, email);
		}
		if(email_existed)
		{
			addFlashMessage(redirectAttributes, Message.warn("邮箱已被其他用户绑定"));
			return "redirect:edit.do?id="+id;
		}
		String name =userRow.getString("name","");
		String bank_card_no =userRow.getString("bank_card_no","");
		try {
			if(! StringUtils.isEmptyOrNull(name))
			{
				name =AesUtil.encode(name);
			}
			if(! StringUtils.isEmptyOrNull(bank_card_no))
			{
				bank_card_no =AesUtil.encode(bank_card_no);
			}
		} catch (Exception e) {
			name ="";
			bank_card_no ="";
		}
		userRow.put("name",name);
		userRow.put("bank_card_no", bank_card_no);
		agentService.update(userRow);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:AgentList.do";
	}

	/**
	 * 检查手机号码是否已存在
	 */
	@RequestMapping(value = "/check_telephone", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkUserTelephone(String TELEPHONE) {
		System.out.println("检查手机号码是否已存在："+TELEPHONE);
		if (StringUtils.isEmptyOrNull(TELEPHONE)) {
			return false;
		}
		if(agentService.isPhoneExisted(TELEPHONE))
		{
			System.out.println("手机号码已存在："+TELEPHONE);
			return false;
		}
		return true;
	}
	
	/**
	 * 检查邮箱是否已存在
	 */
	@RequestMapping(value = "/check_email", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkEmail(String EMAIL) {
		if(StringUtils.isEmptyOrNull(EMAIL))
		{
			return false;
		}
		if(agentService.isEmailExisted(EMAIL))
		{
			return false;
		}
		return true;
	}
	
	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		agentService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
	@RequestMapping(value = "/resetPassword")
	public @ResponseBody
	Message resetPassword(String id) {
		agentService.resetPassword(id);
		return SUCCESS_MESSAGE;
	}
}
