/**
 * @Title: LoginController.java
 * @Package com.ezcloud.framework.controller
 * @Description: TODO
 * @author ez-cloud work group
 * @date 2014-7-14 下午06:48:33
 * @version V1.0
 */
package com.ezcloud.framework.controller;

import java.net.URLDecoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cxhl.service.ShopAdminService;
import com.cxhl.service.ShopService;
import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.service.login.Login;
import com.ezcloud.framework.service.system.Staff;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

/**
 * 系统用户登陆控制器
 * 
 * @ClassName: LoginController
 * @Description: TODO
 * @author ez-cloud work group
 * @date 2014-7-14 下午06:48:33
 */
@Controller("frameworkLoginController")
@RequestMapping("/login")
public class LoginController extends BaseController {

	@Resource(name = "frameworkLoginService")
	private Login loginService;
	
	@Resource(name = "frameworkStaffService")
	private Staff staffService;
	@Resource(name = "cxhlShopService")
	private ShopService shopService;
	@Resource(name = "cxhlShopAdminService")
	private ShopAdminService shopAdminService;
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/login")
	public String login(String username, String password, String captcha, String isRememberUsername, String token ,RedirectAttributes redirectAttributes, HttpSession session, ModelMap model) {
		Assert.notNull(username, "username can not be null");
		Assert.notNull(password, "password can not be null");
		Assert.notNull(captcha, "captcha can not be null");
		String sessionCaptcha = (String) session.getAttribute("validateCode");
		boolean boolCode = captcha.toUpperCase().equals(sessionCaptcha);
		if (!boolCode) {
			model.addAttribute("error", message("framework.validatecode.error"));
			return "Login";
		}
		try {
			loginService.getRow().put("username", username);
			loginService.getRow().put("password", password);
			if(token == null || token.replace(" ", "").length() == 0){
				token ="";
			}
			token =URLDecoder.decode(token);
			loginService.getRow().put("token", token);
			loginService.login();
		} catch (JException e) {
			e.printStackTrace();
			model.addAttribute("error", e.getMsg());
			return "Login";
		}
		Row staff = loginService.getRow();
		session.setAttribute("staff", staff);
		// permissionService.getStaffPermission();
		// model.addAttribute("permission_list",
		// permissionService.getDataSet());
		// model.addAttribute("permission", "123");
		// return "/main/menu/main";
		return "redirect:/main/menu/main.do";
	}
	
	@RequestMapping(value = "/ShopLogin.do")
	public String thirdLogin(String username, String password, String captcha, String shop_id, String token ,RedirectAttributes redirectAttributes, HttpSession session, ModelMap model) {
		//取商家列表
		DataSet ds =shopService.queryAllShop();
		model.addAttribute("shop_list", ds);
		return "/login/ShopLogin";
	}
	
	
	@RequestMapping(value = "/thirdLogin.do")
	public String shopLogin(String username, String password, String captcha, String shop_id, String token ,RedirectAttributes redirectAttributes, HttpSession session, ModelMap model) {
		Assert.notNull(username, "username can not be null");
		Assert.notNull(password, "password can not be null");
		Assert.notNull(captcha, "captcha can not be null");
		Assert.notNull(shop_id, "shop_id can not be null");
		String sessionCaptcha = (String) session.getAttribute("validateCode");
		boolean boolCode = captcha.toUpperCase().equals(sessionCaptcha);
		if (!boolCode) {
			model.addAttribute("error", message("framework.validatecode.error"));
			return "Login";
		}
		Row shopRow =shopAdminService.findByShopId(shop_id);
		if(shopRow == null)
		{
			model.addAttribute("error", "商户账号不存在");
			return "ShopLogin.do";
		}
		System.out.println("shop_id============>>"+shop_id);
		String rtx_id =shopRow.getString("id","");
		System.out.println("rtx_id============>>"+rtx_id);
		try {
			loginService.getRow().put("username", username);
			loginService.getRow().put("password", password);
			loginService.getRow().put("rtx_id", rtx_id);
			if(token == null || token.replace(" ", "").length() == 0){
				token ="";
			}
			token =URLDecoder.decode(token);
			loginService.getRow().put("token", token);
			loginService.thirdLogin();
		} catch (JException e) {
			e.printStackTrace();
			model.addAttribute("error", e.getMsg());
			return "ShopLogin.do";
		}
		Row staff = loginService.getRow();
		staff.put("shop_id", shop_id);
		session.setAttribute("staff", staff);
		return "redirect:/main/menu/main.do";
	}
	
	@RequestMapping(value = "/changePassword")
	public @ResponseBody
	Message  changePassword(String pwd,String new_pwd) {
		Assert.notNull(pwd, "原密码不能为空");
		Assert.notNull(new_pwd, "新密码不能为空");
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		Row staff =(Row)session.getAttribute("staff");
		String staff_no =staff.getString("staff_no");
		Row staffRow =staffService.find(staff_no);
		String password =staffRow.getString("password","");
		if(! pwd.equals(password))
		{
			return Message.error("旧密码错误");
		}
		Row newStaffRow =new Row();
		newStaffRow.put("staff_no", staff_no);
		newStaffRow.put("password", new_pwd);
		staffService.update(newStaffRow);
		return SUCCESS_MESSAGE;
	}
}