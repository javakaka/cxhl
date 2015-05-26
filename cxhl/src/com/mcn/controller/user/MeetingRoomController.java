package com.mcn.controller.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.vo.Row;
import com.mcn.service.CompanyUser;
import com.mcn.service.MeetingRoom;

@Controller("companyMeetingRoomController")
@RequestMapping("/mcnpage/user/meetingroom")
public class MeetingRoomController extends BaseController{
	
	@Resource(name ="meetingRoomService")
	private MeetingRoom meetingRoomService;
	
	@Resource(name ="companyUserService")
	private CompanyUser companyUserService;
	
	
	/**
	 * 企业查询自己的会议室列表
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/RoomList")
	public String getMoudleList(Pageable pageable, ModelMap model) {
//		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/meetingroom/RoomList";
		}
		meetingRoomService.getRow().put("org_id", org_id);
		meetingRoomService.getRow().put("pageable", pageable);
		Page page = meetingRoomService.queryPage();
		model.addAttribute("page", page);
		return "/mcnpage/user/meetingroom/RoomList";
	}

	@RequestMapping(value = "/add")
	public String add(HttpSession session, ModelMap model) {
		Row staff =(Row)session.getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id !=null && org_id.replace(" ", "").length() >0)
		model.addAttribute("users",companyUserService.selectAll(org_id));
		return "/mcnpage/user/meetingroom/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(String name, String num, String audit_id, HttpSession session,RedirectAttributes redirectAttributes) {
		meetingRoomService.getRow().put("name", name);
		meetingRoomService.getRow().put("num", num);
		meetingRoomService.getRow().put("audit_id", audit_id);
		Row staff =(Row)session.getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id !=null && org_id.replace(" ", "").length() >0)
			meetingRoomService.getRow().put("org_id", org_id);
		
		meetingRoomService.save();
		return "redirect:RoomList.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(Long id, HttpSession session, ModelMap model) {
		meetingRoomService.getRow().put("id", id);
		model.addAttribute("room", meetingRoomService.find());
		Row staff =(Row)session.getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id !=null && org_id.replace(" ", "").length() >0)
		model.addAttribute("users",companyUserService.selectAll(org_id));
		return "/mcnpage/user/meetingroom/edit";
	}

	@RequestMapping(value = "/update")
	public String update(String id, String name, String num, String audit_id, ModelMap model) {
		meetingRoomService.getRow().clear();
		meetingRoomService.getRow().put("id", id);
		meetingRoomService.getRow().put("name", name);
		meetingRoomService.getRow().put("num", num);
		meetingRoomService.getRow().put("audit_id", audit_id);
		meetingRoomService.update();
		return "redirect:RoomList.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		meetingRoomService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
}