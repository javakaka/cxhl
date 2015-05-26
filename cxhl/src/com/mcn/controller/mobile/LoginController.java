package com.mcn.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.mcn.service.CompanyUser;

@Controller("mobileLoginController")
@RequestMapping("/api/action")
public class LoginController extends BaseController {
	
	
	@Resource(name = "companyUserService")
	private CompanyUser companyUserService;
	
	/**
	 * 登陆
	 * @param request
	 * @return
	 * @throws JException
	 */
	@RequestMapping(value ="/login")
	public @ResponseBody
	String  login(HttpServletRequest request) throws JException
	{
		parseRequest(request);
		String token =ivo.getString("token",null);
		String username =ivo.getString("username",null);
		String password =ivo.getString("password",null);
		Row staff =companyUserService.login(token, username);
		if(staff == null)
		{
			ovo =new OVO();
			ovo.iCode =-10003;
			ovo.sMsg ="不存在此帐户";
			ovo.sExp ="不存在此帐户";
		}
		else
		{
			String pwd =staff.getString("password",null);
			if(pwd != null && ! pwd.equals(password))
			{
				ovo =new OVO(-10004, "密码错误", "密码错误");
			}
			else if(pwd !=null && pwd.equals(password)){
				ovo =new OVO(0, "登陆成功", "登陆成功");
				// user profile info
				ovo.set("user_id",staff.getString("id","") );
				ovo.set("username",username );
				ovo.set("realname",staff.getString("name", ""));
				ovo.set("telephone",staff.getString("telephone", ""));
				ovo.set("sex",staff.getString("sex", ""));
				ovo.set("depart_id",staff.getString("depart_id", ""));
				ovo.set("position",staff.getString("position", ""));
				ovo.set("manager_id",staff.getString("manager_id", ""));
				ovo.set("remark",staff.getString("remark", ""));
				
			}
		}
		return VOConvert.ovoToJson(ovo);
	}
	
	/**
	 * 修改密码
	 * @param request
	 * @return
	 * @throws JException
	 */
	@RequestMapping(value ="/changePassword")
	public @ResponseBody
	String changePassword(HttpServletRequest request) throws JException
	{
		parseRequest(request);
//		String token =ivo.getString("token",null);
		String user_id =ivo.getString("user_id",null);
		String oldPwd =ivo.getString("oldPwd",null);
		String newPwd =ivo.getString("newPwd",null);
		
		if(user_id == null || user_id.replace(" ", "").length() ==0){
			ovo =new OVO(-10005,"用户编号不能为空","用户编号不能为空");
		}
		else
		{
			int status =companyUserService.changePassword(user_id, oldPwd, newPwd);
			if(status ==1)
			{
				ovo =new OVO(-10006,"用户不存在","用户不存在");
			}
			else if(status ==2)
			{
				ovo =new OVO(-10007,"用户旧密码不正确","用户旧密码不正确");
			}
			else if(status ==3)
			{
				ovo =new OVO(-10008,"修改密码失败","修改密码失败");
			}
			else if(status ==0)
			{
				ovo =new OVO(0,"修改密码成功","");
			}
		}
		String json =VOConvert.ovoToJson(ovo);
		return json;
	}
	
	/**
	 * 更新信息
	 * @param request
	 * @return
	 * @throws JException
	 */
	@RequestMapping(value ="/updateProfile")
	public @ResponseBody
	OVO updateProfile(HttpServletRequest request) throws JException
	{
		parseRequest(request);
		System.out.print("login controller ======>>"+ivo);
		ovo.set("name", "admin");
		return ovo;
	}

}
