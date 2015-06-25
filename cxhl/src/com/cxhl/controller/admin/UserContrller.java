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

import com.cxhl.service.UserService;
import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.Md5Util;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

@Controller("fzbPlatformUserController")
@RequestMapping("/fzbpage/platform/user")
public class UserContrller  extends BaseController{

	@Resource(name = "fzbUserService")
	private UserService userService;
	
	
	/**
	 * 分页查询房东租客用户
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/UserList")
	public String list(Pageable pageable, ModelMap model) {
		userService.getRow().put("pageable", pageable);
		Page page = userService.queryPage();
		model.addAttribute("page", page);
		userService.getRow().clear();
		return "/fzbpage/platform/user/UserList";
	}
	/**
	 * 查询最近注册的5个房东租客用户
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/LatestUserList")
	public String queryLatestUserList(Pageable pageable, ModelMap model) {
		userService.getRow().put("pageable", pageable);
		DataSet data = userService.queryLatestTop5Users();
		model.addAttribute("data", data);
		userService.getRow().clear();
		return "/fzbpage/platform/webpart/LatestUserList";
	}
	
	/**
	 * 分页查询房东租客用户
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/SelectUser")
	public String selectUserList(String id,Pageable pageable, ModelMap model) {
		userService.getRow().put("pageable", pageable);
		Page page = userService.queryPage();
		model.addAttribute("page", page);
		if(StringUtils.isEmptyOrNull(id))
		{
			id ="";
		}
		model.addAttribute("id", id);
		userService.getRow().clear();
		return "/fzbpage/platform/user/SelectUser";
	}

	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		return "/fzbpage/platform/user/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row userRow =MapUtils.convertMaptoRowWithoutNullField(map);
		String telephone =userRow.getString("telephone","");
		if(StringUtils.isEmptyOrNull(telephone))
		{
			addFlashMessage(redirectAttributes, Message.error("手机号码不能为空"));
			return "redirect:add.do";
		}
		String password =userRow.getString("password","");
		if(StringUtils.isEmptyOrNull(password))
		{
			addFlashMessage(redirectAttributes, Message.error("密码不能为空"));
			return "redirect:add.do";
		}
		String name =userRow.getString("name","");
		if(! StringUtils.isEmptyOrNull(name))
		{
			name =AesUtil.encode(name);
			userRow.put("name", name);
		}
		String bank_card_no =userRow.getString("bank_card_no","");
		if(! StringUtils.isEmptyOrNull(bank_card_no))
		{
			bank_card_no =AesUtil.encode(bank_card_no);
			userRow.put("bank_card_no", bank_card_no);
		}
		String credit_card_no =userRow.getString("credit_card_no","");
		if(! StringUtils.isEmptyOrNull(credit_card_no))
		{
			credit_card_no =AesUtil.encode(credit_card_no);
			userRow.put("credit_card_no", credit_card_no);
		}
//		userRow.put("username", telephone);
		userRow.put("password", Md5Util.Md5(password));
		userRow.put("register_time", DateUtil.getCurrentDateTime());
		userService.insert(userRow);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:UserList.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(Long id, ModelMap model) throws Exception {
		Assert.notNull(id);
		Row userRow =userService.find(String.valueOf(id));
		String name =userRow.getString("name","");
		String bank_card_no =userRow.getString("bank_card_no","");
		String credit_card_no =userRow.getString("credit_card_no","");
		if(! StringUtils.isEmptyOrNull(name))
		{
			name =AesUtil.decode(name);
			userRow.put("name", name);
		}
		if(! StringUtils.isEmptyOrNull(bank_card_no))
		{
			bank_card_no =AesUtil.decode(bank_card_no);
			userRow.put("bank_card_no", bank_card_no);
		}
		if(! StringUtils.isEmptyOrNull(credit_card_no))
		{
			credit_card_no =AesUtil.decode(credit_card_no);
			userRow.put("credit_card_no", credit_card_no);
		}
		model.addAttribute("row", userRow);
		return "/fzbpage/platform/user/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
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
			email_existed =userService.isEmailExisted(id, email);
		}
		if(email_existed)
		{
			addFlashMessage(redirectAttributes, Message.warn("邮箱已被其他用户绑定"));
			return "redirect:edit.do?id="+id;
		}
		String name =userRow.getString("name","");
		if(! StringUtils.isEmptyOrNull(name))
		{
			name =AesUtil.encode(name);
			userRow.put("name", name);
		}
		String bank_card_no =userRow.getString("bank_card_no","");
		if(! StringUtils.isEmptyOrNull(bank_card_no))
		{
			bank_card_no =AesUtil.encode(bank_card_no);
			userRow.put("bank_card_no", bank_card_no);
		}
		String credit_card_no =userRow.getString("credit_card_no","");
		if(! StringUtils.isEmptyOrNull(credit_card_no))
		{
			credit_card_no =AesUtil.encode(credit_card_no);
			userRow.put("credit_card_no", credit_card_no);
		}
		userService.update(userRow);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:UserList.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		userService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
	
	@RequestMapping(value = "/resetPassword")
	public @ResponseBody
	Message resetPassword(String id) {
		userService.resetPassword(id);
		return SUCCESS_MESSAGE;
	}
	
	
	/**
	 * 检查手机号码是否已存在
	 */
	@RequestMapping(value = "/check_telephone", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkTelephone(String TELEPHONE) {
		if(StringUtils.isEmptyOrNull(TELEPHONE))
		{
			return false;
		}
		return (userService.isTelephoneExisted(TELEPHONE));
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
		return (userService.isEmailExisted(EMAIL));
	}
	
	/**
	 * 检查邮箱是否已存在
	 */
	@RequestMapping(value = "/check_extra_email")
	public @ResponseBody
	boolean checkExtraEmail(String ID,String EMAIL) {
		if(StringUtils.isEmptyOrNull(EMAIL))
		{
			return false;
		}
		if(StringUtils.isEmptyOrNull(ID))
		{
			return false;
		}
		return (userService.isExtraEmailExisted(ID,EMAIL));
	}
	
	/**
	 * 检查身份证号码是否已存在
	 */
	@RequestMapping(value = "/check_id_card_no", method = RequestMethod.GET)
	public @ResponseBody
	boolean check_id_card_no(String ID_CARD_NO) {
		if(StringUtils.isEmptyOrNull(ID_CARD_NO))
		{
			return false;
		}
		return (userService.isIdCardNoExisted(ID_CARD_NO));
	}
	
	/**
	 * 检查身份证号码是否已存在
	 */
	@RequestMapping(value = "/check_extra_id_card_no")
	public @ResponseBody
	boolean check_extra_id_card_no(String ID, String ID_CARD_NO) {
	if(StringUtils.isEmptyOrNull(ID_CARD_NO))
	{
		return false;
	}
	if(StringUtils.isEmptyOrNull(ID))
	{
		return false;
	}
	return (userService.isExtraIdCardNoExisted(ID,ID_CARD_NO));
	}
	
	/**
	 * 检查收款帐号是否已存在
	 * @throws Exception 
	 */
	@RequestMapping(value = "/check_extra_bank_card_no")
	public @ResponseBody
	boolean check_extra_bank_card_no(String ID, String BANK_CARD_NO) throws Exception {
		if(StringUtils.isEmptyOrNull(BANK_CARD_NO))
		{
			return false;
		}
		if(StringUtils.isEmptyOrNull(ID))
		{
			return false;
		}
		BANK_CARD_NO =AesUtil.encode(BANK_CARD_NO);
		return (userService.isExtraBankCardNoExisted(ID,BANK_CARD_NO));
	}
	/**
	 * 检查付款帐号是否已存在
	 * @throws Exception 
	 */
	@RequestMapping(value = "/check_extra_credit_card_no")
	public @ResponseBody
	boolean check_extra_credit_card_no(String ID, String CREDIT_CARD_NO) throws Exception {
		if(StringUtils.isEmptyOrNull(CREDIT_CARD_NO))
		{
			return false;
		}
		if(StringUtils.isEmptyOrNull(ID))
		{
			return false;
		}
		CREDIT_CARD_NO =AesUtil.encode(CREDIT_CARD_NO);
		return (userService.isExtraCreditCardNoExisted(ID,CREDIT_CARD_NO));
	}
}
