package com.mcn.controller.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.system.SystemSite;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.vo.Row;
import com.mcn.service.PunchLogService;

@Controller("mcnPunchLogController")
@RequestMapping("/mcnpage/user/punch/log")
public class PunchLogController extends BaseController{
	
	@Resource(name ="mcnPunchLogService")
	private PunchLogService punchLogService;
	
	@Resource(name ="frameworkSystemSiteService")
	private SystemSite systemSiteService;
	
	
	/**
	 * 企业查询自己的打卡记录
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/PunchLogList")
	public String list(Pageable pageable, ModelMap model) {
//		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/punch/log/PunchLogList";
		}
		punchLogService.getRow().put("org_id", org_id);
		punchLogService.getRow().put("pageable", pageable);
		Page page = punchLogService.queryPageForCompany();
		model.addAttribute("page", page);
		return "/mcnpage/user/punch/log/PunchLogList";
	}

	@RequestMapping(value = "/add")
	public String add(HttpSession session, ModelMap model) {
		Row staff =(Row)session.getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id !=null && org_id.replace(" ", "").length() >0)
		model.addAttribute("sites",systemSiteService.queryOrgSite(org_id));
		return "/mcnpage/user/punch/log/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(String DEPART_ID, String AM_START,String AM_END, String PM_START,String PM_END,RedirectAttributes redirectAttributes) {
		Assert.notNull(DEPART_ID, "DEPART_ID can not be null");
		punchLogService.getRow().put("DEPART_ID", DEPART_ID);
		punchLogService.getRow().put("AM_START", AM_START);
		punchLogService.getRow().put("AM_END", AM_END);
		punchLogService.getRow().put("PM_START", PM_START);
		punchLogService.getRow().put("PM_END", PM_END);
		
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id ==null  ||org_id.replace(" ", "").length() == 0)
			return "redirect:PunchLogList.do";
		punchLogService.getRow().put("org_id", org_id);
		punchLogService.save();
		return "redirect:PunchLogList.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(Long id, ModelMap model) {
		punchLogService.getRow().put("id", id);
		model.addAttribute("row", punchLogService.find());
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id !=null && org_id.replace(" ", "").length() >0)
			model.addAttribute("sites",systemSiteService.queryOrgSite(org_id));
		return "/mcnpage/user/punch/log/edit";
	}

	@RequestMapping(value = "/update")
	public String update(String ID,String DEPART_ID,String AM_START,String AM_END, String PM_START,String PM_END, ModelMap model) {
		punchLogService.getRow().clear();
		Assert.notNull(ID, "ID can not be null");
		Assert.notNull(DEPART_ID, "DEPART_ID can not be null");
		punchLogService.getRow().put("ID", ID);
		punchLogService.getRow().put("DEPART_ID", DEPART_ID);
		punchLogService.getRow().put("AM_START", AM_START);
		punchLogService.getRow().put("AM_END", AM_END);
		punchLogService.getRow().put("PM_START", PM_START);
		punchLogService.getRow().put("PM_END", PM_END);
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id ==null  ||org_id.replace(" ", "").length() == 0)
			return "redirect:PunchLogList.do";
		punchLogService.update();
		return "redirect:PunchLogList.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		punchLogService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
}