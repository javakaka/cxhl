package com.cxhl.controller.admin;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cxhl.service.AgentService;
import com.cxhl.service.RoomService;
import com.cxhl.service.UserService;
import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Pageable;

@Controller("fzbPlatformReportController")
@RequestMapping("/fzbpage/platform/report")
public class ReportContrller  extends BaseController{

	@Resource(name = "fzbUserService")
	private UserService userService;
	
	@Resource(name = "fzbAgentService")
	private AgentService agentService;
	
	@Resource(name = "fzbRoomService")
	private RoomService roomService;
	
	/**
	 * 自定义桌面
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/SummaryReport")
	public String list(Pageable pageable, ModelMap model) {
		//agent
		int agentTotal =agentService.findTotalNum();
		//user
		int userTotal =userService.findTotalNum();
		//room
		int roomTotal =roomService.findTotalNum();
		model.addAttribute("agentTotal", agentTotal);
		model.addAttribute("userTotal", userTotal);
		model.addAttribute("roomTotal", roomTotal);
		return "/fzbpage/platform/webpart/SummaryReport";
	}

	


}
