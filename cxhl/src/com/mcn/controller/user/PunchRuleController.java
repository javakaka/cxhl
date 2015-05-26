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
import com.mcn.service.PunchRuleService;
import com.mcn.service.PunchSettingService;

@Controller("mcnPunchRuleController")
@RequestMapping("/mcnpage/user/punch/rule")
public class PunchRuleController extends BaseController{
	
	@Resource(name ="mcnPunchRuleService")
	private PunchRuleService punchRuleService;
	
	@Resource(name ="frameworkSystemSiteService")
	private SystemSite systemSiteService;
	
	@Resource(name ="mcnPunchSettingService")
	private PunchSettingService punchSettingService;
	
	
	/**
	 * 企业查询自己的打卡规则
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/PunchRuleList")
	public String list(Pageable pageable, ModelMap model) {
//		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/punch/rule/PunchRuleList";
		}
		punchRuleService.getRow().put("org_id", org_id);
		punchRuleService.getRow().put("pageable", pageable);
		Page page = punchRuleService.queryPageForCompany();
		model.addAttribute("page", page);
		return "/mcnpage/user/punch/rule/PunchRuleList";
	}

	@RequestMapping(value = "/add")
	public String add(HttpSession session, ModelMap model) {
		Row staff =(Row)session.getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id !=null && org_id.replace(" ", "").length() >0)
		model.addAttribute("sites",systemSiteService.queryOrgSite(org_id));
		return "/mcnpage/user/punch/rule/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(String DEPART_ID, String AM_START,String AM_END, String PM_START,String PM_END,RedirectAttributes redirectAttributes) {
		Assert.notNull(DEPART_ID, "DEPART_ID can not be null");
		punchRuleService.getRow().put("DEPART_ID", DEPART_ID);
		punchRuleService.getRow().put("AM_START", AM_START);
		punchRuleService.getRow().put("AM_END", AM_END);
		punchRuleService.getRow().put("PM_START", PM_START);
		punchRuleService.getRow().put("PM_END", PM_END);
		
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id ==null  ||org_id.replace(" ", "").length() == 0)
			return "redirect:PunchRuleList.do";
		punchRuleService.getRow().put("org_id", org_id);
		punchRuleService.save();
		return "redirect:PunchRuleList.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(Long id, ModelMap model) {
		punchRuleService.getRow().put("id", id);
		model.addAttribute("row", punchRuleService.find());
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id !=null && org_id.replace(" ", "").length() >0)
			model.addAttribute("sites",systemSiteService.queryOrgSite(org_id));
		return "/mcnpage/user/punch/rule/edit";
	}

	@RequestMapping(value = "/update")
	public String update(String ID,String DEPART_ID,String AM_START,String AM_END, String PM_START,String PM_END, ModelMap model) {
		punchRuleService.getRow().clear();
		Assert.notNull(ID, "ID can not be null");
		Assert.notNull(DEPART_ID, "DEPART_ID can not be null");
		punchRuleService.getRow().put("ID", ID);
		punchRuleService.getRow().put("DEPART_ID", DEPART_ID);
		punchRuleService.getRow().put("AM_START", AM_START);
		punchRuleService.getRow().put("AM_END", AM_END);
		punchRuleService.getRow().put("PM_START", PM_START);
		punchRuleService.getRow().put("PM_END", PM_END);
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		if(org_id ==null  ||org_id.replace(" ", "").length() == 0)
			return "redirect:PunchRuleList.do";
		punchRuleService.update();
		return "redirect:PunchRuleList.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		punchRuleService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
	//假期设置
	@RequestMapping(value = "/setting")
	public String setting(ModelMap model) {
		Row staff =(Row)getSession().getAttribute("staff");
		String org_id=staff.getString("bureau_no",null);
		punchSettingService.getRow().put("org_id", org_id);
		model.addAttribute("row", punchSettingService.findOrgSetting());
		return "/mcnpage/user/punch/rule/setting";
	}
	
	//保存假期设置
	@RequestMapping(value = "/saveSetting")
	public String saveSetting(String id,String year,String sick,String exchange,String outing,String personal,String work,ModelMap model) {
		Assert.notNull(id);
		Assert.notNull(year);
		Assert.notNull(sick);
		Assert.notNull(exchange);
		Assert.notNull(outing);
		punchSettingService.getRow().put("id",id);
		punchSettingService.getRow().put("year",year);
		punchSettingService.getRow().put("sick",sick);
		punchSettingService.getRow().put("exchange",exchange);
		punchSettingService.getRow().put("personal",outing);
		punchSettingService.getRow().put("outing",personal);
		punchSettingService.getRow().put("work",work);
		punchSettingService.update();
		return "redirect:setting.do";
	}
	
}
