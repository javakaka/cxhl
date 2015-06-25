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
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

@Controller("fzbPlatformPayController")
@RequestMapping("/fzbpage/platform/pay")
public class PayContrller  extends BaseController{

	@Resource(name = "frameworkSystemConfigService")
	private SystemConfigService systemConfigService;
	
	/**
	 * 查询支付帐号配置信息
	 * @return
	 */
	@RequestMapping(value = "/PaySetting")
	public String list(ModelMap model) {
		DataSet ds =systemConfigService.getConfigData("APP_COMPANY_BANK_ACCOUNT");
		String bank_no ="";
		String bank_type ="";
		String busi_code ="";
		if( ds != null && ds.size()>0 )
		{
			for(int i=0; i< ds.size(); i++)
			{
				Row row =(Row)ds.get(i);
				busi_code =row.getString("busi_code","");
				if(busi_code.equalsIgnoreCase("bank_no"))
				{
					bank_no =row.getString("busi_code_set","");
				}
				else if(busi_code.equalsIgnoreCase("bank_type"))
				{
					bank_type =row.getString("busi_code_set","");
				}
			}
		}
		model.addAttribute("bank_no", bank_no);
		model.addAttribute("bank_type", bank_type);
		model.addAttribute("busi_type", "APP_COMPANY_BANK_ACCOUNT");
		return "/fzbpage/platform/pay_setting/PaySetting";
	}
	
	
	@RequestMapping(value = "/SavePaySetting")
	public String save(String bank_type, String bank_no,String password,ModelMap model,RedirectAttributes redirectAttributes) {
		Assert.notNull(bank_type, "bank_type 不能为空");
		Assert.notNull(bank_no, "bank_no 不能为空");
		String busi_type="APP_COMPANY_BANK_ACCOUNT";
		systemConfigService.setConfigData(busi_type,"BANK_TYPE",bank_type,"房租宝房东租客版公司收款账号所属银行");
		systemConfigService.setConfigData(busi_type,"BANK_NO",bank_no,"房租宝房东租客版公司收款账号");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:PaySetting.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		return SUCCESS_MESSAGE;
	}
}
