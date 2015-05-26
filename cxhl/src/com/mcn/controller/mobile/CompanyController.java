package com.mcn.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.mcn.service.CompanyService;
import com.mcn.service.PunchLogService;
import com.mcn.service.PunchRuleService;

/**
 * 手机端查询企业相关信息接口
 * @author JianBoTong
 *
 */
@Controller("mobileCompanyController")
@RequestMapping("/api/company")
public class CompanyController extends BaseController{
	
	@Resource(name = "companyService")
	private   CompanyService companyService;
	
	//查询企业的部门以及部门人员
	@RequestMapping("/all")
	public @ResponseBody String queryUserPunchLog(HttpServletRequest request) throws JException
	{
		String json ="";
		parseRequest(request);
		String token =ivo.getString("token",null);
		DataSet departDs =companyService.queryAllSites(token);
		DataSet userDs =companyService.queryAllUsers(token);
		ovo =new OVO();
		ovo.set("depart_list", departDs);
		ovo.set("user_list", userDs);
		json =VOConvert.ovoToJson(ovo);
		return json;
	} 
	
	
}
