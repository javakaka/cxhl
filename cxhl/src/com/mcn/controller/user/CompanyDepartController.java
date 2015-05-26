package com.mcn.controller.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.system.SystemSite;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.vo.Row;

@Controller("mcnCompanyDepartController")
@RequestMapping("/mcnpage/user/depart")
public class CompanyDepartController extends BaseController{

	@Resource(name ="frameworkSystemSiteService")
	private SystemSite systemSiteService;
	
	
	/**
	 * 企业查询自己的用户
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/DepartList")
	public String getMoudleList(Pageable pageable, ModelMap model) {
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/depart/DepartList";
		}
		systemSiteService.getRow().put("org_id", org_id);
		systemSiteService.getRow().put("pageable", pageable);
		Page page = systemSiteService.queryPage();
		model.addAttribute("page", page);
		return "/mcnpage/user/depart/DepartList";
	}
	
	@RequestMapping(value = "/add")
	public String add(  ModelMap model )
	{
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/depart/DepartList";
		}
		model.addAttribute("departs", systemSiteService.queryOrgSite(org_id));
		return  "/mcnpage/user/depart/add";
	}
	
	@RequestMapping(value = "/save")
	public String save(String SITE_NAME,String UP_SITE_NO,  ModelMap model )
	{
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/depart/add";
		}
		Assert.notNull(SITE_NAME, "SITE_NAME can not be null");
		Row siteRow =new Row();
		siteRow.put("SITE_NAME", SITE_NAME);
		siteRow.put("BUREAU_NO", org_id);
		if(UP_SITE_NO!= null && UP_SITE_NO.replace(" ", "").length() >0)
		{
			siteRow.put("UP_SITE_NO", UP_SITE_NO);
		}
		
		systemSiteService.insertOrgSite(siteRow);
		return "redirect:DepartList.do";
	}
	
	@RequestMapping(value = "/edit")
	public String find(String id,  ModelMap model )
	{
		Assert.notNull(id, "id can not be null");
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
		}
		if(org_id == null){
			return "/mcnpage/user/depart/DepartList";
		}
		systemSiteService.getRow().put("id", id);
		model.addAttribute("departs", systemSiteService.queryOrgSite(org_id));
		model.addAttribute("row", systemSiteService.find());
		return  "/mcnpage/user/depart/edit";
	}
	
	@RequestMapping(value = "/update")
	public String update(String SITE_NO,String SITE_NAME,String UP_SITE_NO,  ModelMap model )
	{
		Assert.notNull(SITE_NO, "SITE_NO can not be null");
		Assert.notNull(SITE_NAME, "SITE_NAME can not be null");
		Row siteRow =new Row();
		siteRow.put("SITE_NO", SITE_NO);
		siteRow.put("SITE_NAME", SITE_NAME);
		if(UP_SITE_NO!= null && UP_SITE_NO.replace(" ", "").length() >0)
		{
			siteRow.put("UP_SITE_NO", UP_SITE_NO);
		}
		
		HttpSession session = getSession();
		Row staff =(Row)session.getAttribute("staff");
		String org_id =null;
		if(staff != null){
			org_id =staff.getString("bureau_no", null);
			siteRow.put("BUREAU_NO", org_id);
		}
		else{
			return "redirect:DepartList.do";
		}
		systemSiteService.saveOrgSite(siteRow);
		return "redirect:DepartList.do";
	}
	
	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		systemSiteService.deleteOrgSite(ids);
		return SUCCESS_MESSAGE;
	}
}
