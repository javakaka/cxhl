package com.cxhl.controller.admin;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cxhl.service.AgentCustomerService;
import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;

@Controller("fzbPlatformAgentCustomerController")
@RequestMapping("/fzbpage/platform/customer")
/**
 * 中介奖励
 * @author Administrator
 */
public class AgentCustomerContrller  extends BaseController{

	@Resource(name = "fzbAgentCustomerService")
	private AgentCustomerService agentCustomerService;

	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/CustomerList")
	public String list(Pageable pageable, ModelMap model) throws Exception {
		Page page = agentCustomerService.queryPage(pageable);
		model.addAttribute("page", page);
		return "/fzbpage/platform/customer/CustomerList";
	}

}
