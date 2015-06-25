package com.cxhl.controller.mobile;

import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.AgentService;
import com.cxhl.service.SMSService;
import com.ezcloud.framework.common.Setting;
import com.ezcloud.framework.service.system.SystemMailService;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.Md5Util;
import com.ezcloud.framework.util.SettingUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.DateUtil;

@Controller("mobileAgentUserController")
@RequestMapping("/api/agent/action")
/**
 * 中介版用户
 * @author Administrator
 *
 */
public class AgentUserController extends BaseController {
	
	private static Logger logger = Logger.getLogger(AgentUserController.class); 
	@Resource(name = "fzbAgentService")
	private AgentService agentUserService;
	@Resource(name = "frameworkSystemMailService")
	private SystemMailService mailService;
	@Resource(name = "fzbSMSService")
	private SMSService smsService;
	
	
	/**
	 * 中介用户注册
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/register")
	public @ResponseBody
	String register(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		Row insertRow =new Row();
		/**手机号码 必填 **/
		String telephone =ivo.getString("telephone",null);
		if(StringUtils.isEmptyOrNull(telephone))
		{
			ovo =new OVO(-10010,"手机号码不能为空","手机号码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(! StringUtils.isTelphone(telephone))
		{
			ovo =new OVO(-10010,"手机号码不正确","手机号码不正确");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row staff =agentUserService.findByTelephone(telephone);
		if(staff != null)
		{
			ovo =new OVO(-10010,"手机号码已被注册","手机号码已被注册");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		insertRow.put("telephone", telephone);
		String username ="";
		insertRow.put("username", username);
		/**密码 必填 **/
		String password =ivo.getString("password",null);
		if(StringUtils.isEmptyOrNull(password))
		{
			ovo =new OVO(-10010,"密码不能为空","密码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		insertRow.put("password", password);
		
		/**短信验证码 必填**/
		String sms_code =ivo.getString("sms_code",null);
		if(StringUtils.isEmptyOrNull(sms_code))
		{
			ovo =new OVO(-10010,"短信验证码不能为空","短信验证码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		boolean codeExsited =smsService.findByCodeAndTelphone(sms_code, telephone,2);
		if(! codeExsited)
		{
			ovo =new OVO(-10010,"短信验证码错误或已过期","短信验证码错误或已过期");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		/**注册时间 取服务器时间 **/
		String register_time =DateUtil.getCurrentDateTime();
		insertRow.put("register_time", register_time);
		/**终端类型 必填 1 android 2 ios **/
		String device =ivo.getString("device",null);
		if(StringUtils.isEmptyOrNull(device))
		{
			ovo =new OVO(-10010,"设备类型不能为空","设备类型不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		insertRow.put("device", device);
		/**机器码 必填 **/
		String device_code =ivo.getString("device_code",null);
		if(StringUtils.isEmptyOrNull(device_code))
		{
			ovo =new OVO(-10010,"机器码不能为空","机器码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		insertRow.put("device_code", device_code);
		/**客户端版本 必填 **/
		String version =ivo.getString("version",null);
		if(StringUtils.isEmptyOrNull(version))
		{
			ovo =new OVO(-10010,"客户端版本号不能为空","客户端版本号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		insertRow.put("register_version", version);
		insertRow.put("create_time", register_time);
		int num =agentUserService.insert(insertRow);
		
		if(num > 0)
		{
			ovo =new OVO(0,"注册成功","");
			Row userRow =agentUserService.findByTelephone(telephone);
			if(userRow == null )
			{
				ovo =new OVO(-1001, "登录失败", "用户不存在");
				return AesUtil.encode(VOConvert.ovoToJson(ovo));
			}
			String id=userRow.getString("id");
			telephone=userRow.getString("telephone","");
			username=userRow.getString("username","");
			register_time=userRow.getString("register_time","");
			ovo.set("id", id);
			ovo.set("telephone", telephone);
			ovo.set("username", username);
			ovo.set("register_time", register_time);
			//写日志
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		else
		{
			ovo =new OVO(-10010,"注册失败","注册失败");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
	}
	
	/**
	 * 登录
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/login")
	public @ResponseBody
	String  login(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String username =ivo.getString("username",null);
		if(username == null || username.replace(" ","").length() ==0)
		{
			ovo =new OVO(-1001, "请求参数错误", "用户名不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String password =ivo.getString("password",null);
		if(password == null || password.replace(" ","").length() ==0)
		{
			ovo =new OVO(-1001, "请求参数错误", "密码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String version =ivo.getString("version",null);
		if(version == null || version.replace(" ","").length() ==0)
		{
			ovo =new OVO(-1001, "请求参数错误", "版本号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		logger.info("当前登录用户的客户端版本是:"+version);
		Row userRow =agentUserService.login(username);
		if(userRow == null )
		{
			ovo =new OVO(-1001, "登录失败", "用户不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String dbPassword =userRow.getString("password","");
		if( dbPassword.length()>0 && dbPassword.equals(password) )
		{
			String id=userRow.getString("id");
			String name=userRow.getString("name","");
			if(! StringUtils.isEmptyOrNull(name))
			{
				name =AesUtil.decode(name);
			}
			String telephone=userRow.getString("telephone","");
			String email=userRow.getString("email","");
			String bank_card_no=userRow.getString("bank_card_no","");
			if(! StringUtils.isEmptyOrNull(bank_card_no))
			{
				bank_card_no =AesUtil.decode(bank_card_no);
			}
			String bank_card_type=userRow.getString("bank_card_type","");
			String register_time=userRow.getString("register_time","");
			String id_card_no =userRow.getString("id_card_no","");
			ovo =new OVO(0, "登录成功", "");
			ovo.set("id", id);
			ovo.set("name", name);
			ovo.set("telephone", telephone);
			ovo.set("email", email);
			ovo.set("bank_card_no", bank_card_no);
			ovo.set("bank_card_type", bank_card_type);
			ovo.set("register_time", register_time);
			ovo.set("id_card_no", id_card_no);
			//写日志
			//如果版本号有变更，则更新登录版本号
			String userVersion =userRow.getString("current_version","");
			if(!version.equals(userVersion))
			{
				Row updateRow =new Row();
				updateRow.put("id", id);
				updateRow.put("current_version", version);
				agentUserService.update(updateRow);
			}
		}
		else
		{
			ovo =new OVO(-1002, "登录失败", "密码错误");
		}
		logger.info("登录成功");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 根据编号查询基本信息
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/profile")
	public @ResponseBody
	String  queryProfile(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String id =ivo.getString("id",null);
		if(id == null || id.replace(" ","").length() ==0)
		{
			ovo =new OVO(-1001, "用户编号不能为空", "");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row agentRow =agentUserService.find(id);
		if(agentRow == null)
		{
			ovo =new OVO(-1001, "用户不存在", "");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String name=agentRow.getString("name","");
		if(! StringUtils.isEmptyOrNull(name))
		{
			name =AesUtil.decode(name);
		}
		String telephone=agentRow.getString("telephone","");
		String email=agentRow.getString("email","");
		String bank_card_no=agentRow.getString("bank_card_no","");
		if(! StringUtils.isEmptyOrNull(bank_card_no))
		{
			bank_card_no =AesUtil.decode(bank_card_no);
		}
		String bank_card_type=agentRow.getString("bank_card_type","");
		String register_time=agentRow.getString("register_time","");
		String id_card_no =agentRow.getString("id_card_no","");
		ovo =new OVO(0, "查询成功", "");
		ovo.set("id", id);
		ovo.set("name", name);
		ovo.set("telephone", telephone);
		ovo.set("email", email);
		ovo.set("bank_card_no", bank_card_no);
		ovo.set("bank_card_type", bank_card_type);
		ovo.set("register_time", register_time);
		ovo.set("id_card_no", id_card_no);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 修改密码
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/changePassword")
	public @ResponseBody
	String changePassword(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String user_id =ivo.getString("user_id",null);
		String oldPwd =ivo.getString("oldPwd",null);
		String newPwd =ivo.getString("newPwd",null);
		
		if(user_id == null || user_id.replace(" ", "").length() ==0){
			ovo =new OVO(-10005,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(oldPwd == null || oldPwd.replace(" ", "").length() ==0){
			ovo =new OVO(-10006,"原密码不能为空","原密码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(newPwd == null || newPwd.replace(" ", "").length() ==0){
			ovo =new OVO(-10007,"新密码不能为空","新密码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(oldPwd.equals(newPwd))
		{
			ovo =new OVO(-10008,"新密码不能和旧密码相同","新密码不能和旧密码相同");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		int status =agentUserService.changePassword(user_id, oldPwd, newPwd);
		if(status == 1)
		{
			ovo =new OVO(-10002,"用户不存在","用户不存在");
		}
		else if(status == 2)
		{
			ovo =new OVO(-10002,"原密码错误","原密码错误");
		}
		else if(status == 3)
		{
			ovo =new OVO(-10002,"修改密码失败","修改密码失败");
		}
		else
		{
			ovo =new OVO(0,"操作成功","");
		}
		String json =AesUtil.encode(VOConvert.ovoToJson(ovo));
		return json;
	}
	
	
	
	/**
	 * 修改用户信息
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/updateProfile")
	public @ResponseBody
	String updateProfile(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		Row userRow =null;
		System.out.print("login controller ======>>"+ivo);
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-10010,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		userRow =agentUserService.find(id);
		if(userRow == null)
		{
			ovo =new OVO(-10010,"用户不存在","用户不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		userRow =new Row();
		userRow.put("id", id);
		String name =ivo.getString("name",null);
		if(!StringUtils.isEmptyOrNull(id))
		{
			name =AesUtil.encode(name);
			userRow.put("name", name);
		}
		//验证唯一性
		String email =ivo.getString("email",null);
		boolean email_existed=false;
		if(!StringUtils.isEmptyOrNull(email))
		{
			email_existed =agentUserService.isEmailExisted(id,email);
			if(email_existed)
			{
				ovo =new OVO(-10010,"此邮箱已被其他用户绑定，请使用其他邮箱","此邮箱已被其他用户绑定，请使用其他邮箱");
				return AesUtil.encode(VOConvert.ovoToJson(ovo));
			}
			userRow.put("email", email);
		}
		//验证唯一性
		String username =ivo.getString("username",null);
		boolean username_existed=false;
		if(!StringUtils.isEmptyOrNull(username))
		{
			username_existed =agentUserService.isUsernameExisted(id,username);
			if(username_existed)
			{
				ovo =new OVO(-10010,"此用户名已存在，请使用其他用户名","此用户名已存在，请使用其他用户名");
				return AesUtil.encode(VOConvert.ovoToJson(ovo));
			}
			userRow.put("username", username);
		}
		int rowNum =agentUserService.update(userRow);
		if(rowNum ==0)
		{
			ovo =new OVO(-10010, "操作失败", "操作失败");
		}
		else
		{
			ovo =new OVO(0, "操作成功", "");
		}
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	
	/**
	 * 通过邮箱找回密码
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/resetPassword")
	public @ResponseBody
	String resetPasswordByEmail(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String email =ivo.getString("email",null);
		if(StringUtils.isEmptyOrNull(email))
		{
			ovo =new OVO(-10010,"邮箱不能为空","邮箱不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//验证姓名、邮箱是否正确
		Row userRow =agentUserService.findByEmail(email);
		if(userRow == null)
		{
			ovo =new OVO(-10010,"用户不存在","非法用户");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String id =userRow.getString("id","");
		Setting setting = SettingUtils.get();
		String smtpFromMail =setting.getSmtpFromMail();
		String smtpHost =setting.getSmtpHost();
		Integer smtpPort =setting.getSmtpPort();
		String smtpUsername =setting.getSmtpUsername();
		String smtpPassword =setting.getSmtpPassword();
		String toMail =email;
		String subject ="密码找回";
		boolean async =false;
		Random random =new Random();
		int randomNumber =random.nextInt(899999);
		randomNumber =100000+randomNumber;
		String text ="您的新密码是："+String.valueOf(randomNumber);
		mailService.send(smtpFromMail, smtpHost, smtpPort, smtpUsername, smtpPassword, toMail, subject, null, async, text);
		//更新密码
		Row pwdRow =new Row();
		
		pwdRow.put("password", Md5Util.Md5(String.valueOf(randomNumber)));
		pwdRow.put("id", id);
		int rowNum =agentUserService.update(pwdRow);
		if(rowNum >0)
		{
			ovo =new OVO(0,"操作成功","");
		}
		else
		{
			ovo =new OVO(-10010,"操作失败","操作失败");
		}
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	
	/**
	 * 绑定银行卡
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/bind_card")
	public @ResponseBody
	String bindBankCard(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		Row userRow =null;
		System.out.print("agent user bind_card controller ======>>"+ivo);
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-10010,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		userRow =agentUserService.find(id);
		if(userRow == null)
		{
			ovo =new OVO(-10010,"用户不存在","用户不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String card_no =ivo.getString("card_no",null);
		String old_bank_card_no =userRow.getString("bank_card_no","");
		if(StringUtils.isEmptyOrNull(card_no))
		{
			ovo =new OVO(-10010,"银行卡号不能为空","银行卡号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		card_no =AesUtil.encode(card_no);
		if(!StringUtils.isEmptyOrNull(old_bank_card_no))
		{
			if(card_no.equals(old_bank_card_no))
			{
				ovo =new OVO(-10010,"此银行卡已绑定","此银行卡已绑定");
				return AesUtil.encode(VOConvert.ovoToJson(ovo));
			}
		}
		userRow =new Row();
		userRow.put("id", id);
		userRow.put("bank_card_no", card_no);
		String bank_card_type ="";
		userRow.put("bank_card_type", bank_card_type);
		String name =ivo.getString("user_name","");
		if(!StringUtils.isEmptyOrNull(name))
		{
			name =AesUtil.encode(name);
			userRow.put("name", name);
		}
		int rowNum =agentUserService.update(userRow);
		if(rowNum ==0)
		{
			ovo =new OVO(-10010, "操作失败", "操作失败");
		}
		else
		{
			ovo =new OVO(10010, "操作成功", "");
		}
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 中介修改银行账号
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/changeAccount")
	public @ResponseBody
	String changeAccount(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		//用户ID
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String password =ivo.getString("password",null);
		if(StringUtils.isEmptyOrNull(password))
		{
			ovo =new OVO(-1,"用户密码不能为空","用户密码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row userRow =agentUserService.find(id);
		String dbPassword =userRow.getString("password",null);
		if(!password.equals(dbPassword))
		{
			ovo =new OVO(-1,"用户密码错误","用户密码错误");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String bank_card_no =ivo.getString("card_no",null);
		String old_bank_card_no =userRow.getString("bank_card_no",null);
		if(StringUtils.isEmptyOrNull(bank_card_no))
		{
			ovo =new OVO(-1,"银行卡号不能为空","银行卡号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		bank_card_no =AesUtil.encode(bank_card_no);
		if(bank_card_no.equals(old_bank_card_no))
		{
			ovo =new OVO(-1,"此银行卡号已绑定，请使用其他银行卡号修改","此银行卡号已绑定，请使用其他银行卡号修改");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		userRow =new Row ();
		userRow.put("id", id);
		userRow.put("bank_card_no", bank_card_no);
		int rowNum =agentUserService.update(userRow);
		//发邮件、短信
		if(rowNum <=0)
		{
			ovo =new OVO(-1,"操作失败","操作失败");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		ovo =new OVO(0,"操作成功","");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

}
