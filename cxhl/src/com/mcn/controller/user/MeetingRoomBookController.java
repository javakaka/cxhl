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
import com.mcn.service.MeetingRoomBook;

@Controller("companyMeetingRoomBookController")
@RequestMapping("/mcnpage/user/meetingroom/book")
public class MeetingRoomBookController extends BaseController{
	
	@Resource(name ="meetingRoomBookService")
	private MeetingRoomBook meetingRoomBookService;
	
	@Resource(name ="companyUserService")
	private CompanyUser companyUserService;
	
	
	/**
	 * 企业查询自己的会议室预定记录列表
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/BookList")
	public String getMoudleList(Pageable pageable, ModelMap model) {
//		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/meetingroom/book/BookList";
		}
		meetingRoomBookService.getRow().put("org_id", org_id);
		meetingRoomBookService.getRow().put("pageable", pageable);
		Page page = meetingRoomBookService.queryPageForCompany();
		model.addAttribute("page", page);
		return "/mcnpage/user/meetingroom/book/BookList";
	}

	@RequestMapping(value = "/add")
	public String add(HttpSession session, ModelMap model) {
		Row staff =(Row)session.getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id !=null && org_id.replace(" ", "").length() >0)
		model.addAttribute("users",companyUserService.selectAll(org_id));
		return "/mcnpage/user/meetingroom/book/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(String name, String num, String audit_id, HttpSession session,RedirectAttributes redirectAttributes) {
		meetingRoomBookService.getRow().put("name", name);
		meetingRoomBookService.getRow().put("num", num);
		meetingRoomBookService.getRow().put("audit_id", audit_id);
		Row staff =(Row)session.getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id !=null && org_id.replace(" ", "").length() >0)
			meetingRoomBookService.getRow().put("org_id", org_id);
		
		meetingRoomBookService.save();
		return "redirect:RoomList.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(Long id, HttpSession session, ModelMap model) {
		meetingRoomBookService.getRow().put("id", id);
		model.addAttribute("room", meetingRoomBookService.find());
		Row staff =(Row)session.getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id !=null && org_id.replace(" ", "").length() >0)
		model.addAttribute("users",companyUserService.selectAll(org_id));
		return "/mcnpage/user/meetingroom/book/edit";
	}

	@RequestMapping(value = "/update")
	public String update(String id, String name, String num, String audit_id, ModelMap model) {
		meetingRoomBookService.getRow().clear();
		meetingRoomBookService.getRow().put("id", id);
		meetingRoomBookService.getRow().put("name", name);
		meetingRoomBookService.getRow().put("num", num);
		meetingRoomBookService.getRow().put("audit_id", audit_id);
		meetingRoomBookService.update();
		return "redirect:BookList.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		meetingRoomBookService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
}