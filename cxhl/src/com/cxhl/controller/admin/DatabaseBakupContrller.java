package com.cxhl.controller.admin;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.service.system.SystemConfigService;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.MysqlBackUpUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

@Controller("fzbPlatformDatabaseBackUpController")
@RequestMapping("/fzbpage/platform/backup")
public class DatabaseBakupContrller  extends BaseController{

	@Resource(name = "frameworkSystemConfigService")
	private SystemConfigService systemConfigService;
	
	/**
	 * Mysql备份
	 * @return
	 */
	@RequestMapping(value = "/DatabaseBakSetting")
	public String list(ModelMap model) {
		DataSet ds =systemConfigService.getConfigData("DB_BACKUP");
		String busi_code ="";
		String base_path ="";
		String db_name ="";
		String db_type ="";
		String username ="";
		String password ="";
		if( ds != null && ds.size()>0 )
		{
			for(int i=0; i< ds.size(); i++)
			{
				Row row =(Row)ds.get(i);
				busi_code =row.getString("busi_code","");
				if(busi_code.equalsIgnoreCase("base_path"))
				{
					base_path =row.getString("BUSI_CODE_SET","");
				}
				else if(busi_code.equalsIgnoreCase("db_name"))
				{
					db_name =row.getString("BUSI_CODE_SET","");
				}
				else if(busi_code.equalsIgnoreCase("db_type"))
				{
					db_type =row.getString("BUSI_CODE_SET","");
				}
				else if(busi_code.equalsIgnoreCase("username"))
				{
					username =row.getString("BUSI_CODE_SET","");
				}
				else if(busi_code.equalsIgnoreCase("password"))
				{
					password =row.getString("BUSI_CODE_SET","");
				}
			}
		}
		model.addAttribute("base_path", base_path);
		model.addAttribute("db_name", db_name);
		model.addAttribute("db_type", db_type);
		model.addAttribute("username", username);
		model.addAttribute("password", password);
		model.addAttribute("busi_type", "DB_BACKUP");
		return "/fzbpage/platform/backup/DatabaseBakSetting";
	}
	
	
	@RequestMapping(value = "/SaveBakSetting")
	public String save(String db_type, String db_name,String base_path,
			String username,String password,ModelMap model,RedirectAttributes redirectAttributes) {
		Assert.notNull(db_type, "db_type 不能为空");
		Assert.notNull(db_name, "db_name 不能为空");
		Assert.notNull(base_path, "base_path 不能为空");
		Assert.notNull(username, "username 不能为空");
		Assert.notNull(password, "password 不能为空");
		String busi_type="DB_BACKUP";
		systemConfigService.setConfigData(busi_type,"DB_TYPE",db_type,"数据库类型");
		systemConfigService.setConfigData(busi_type,"DB_NAME",db_name,"数据库名称");
		systemConfigService.setConfigData(busi_type,"BASE_PATH",base_path,"数据库备份根路径");
		systemConfigService.setConfigData(busi_type,"USERNAME",username,"数据库备份的用户帐号");
		systemConfigService.setConfigData(busi_type,"PASSWORD",password,"数据库备份的用户密码");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:DatabaseBakSetting.do";
	}

	@RequestMapping(value = "/backup")
	public @ResponseBody
	Message backup() {
		boolean bool =MysqlBackUpUtils.executeBackUp();
		if(bool)
		{
			return SUCCESS_MESSAGE;
		}
		else
			return Message.error("备份数据库失败");
	}
	
	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		return SUCCESS_MESSAGE;
	}
}
