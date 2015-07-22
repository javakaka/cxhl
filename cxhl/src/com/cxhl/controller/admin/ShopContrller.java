package com.cxhl.controller.admin;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cxhl.service.ShopAdminService;
import com.cxhl.service.ShopService;
import com.cxhl.service.ShopTypeService;
import com.cxhl.service.UserService;
import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.system.Staff;
import com.ezcloud.framework.service.system.StaffRole;
import com.ezcloud.framework.service.system.SystemZoneService;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.Md5Util;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.ResponseVO;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

@Controller("cxhlPlatformShopController")
@RequestMapping("/cxhlpage/platform/shop/profile")
public class ShopContrller  extends BaseController{

	@Resource(name = "cxhlUserService")
	private UserService userService;
	
	@Resource(name = "cxhlShopTypeService")
	private ShopTypeService shopTypeService;
	
	@Resource(name = "cxhlShopService")
	private ShopService shopService;
	
	@Resource(name = "frameworkSystemZoneService")
	private SystemZoneService zoneService;
	
	@Resource(name = "cxhlShopAdminService")
	private ShopAdminService shopAdminService;
	
	@Resource(name = "frameworkStaffService")
	private Staff staffService;
	
	@Resource(name = "frameworkStaffRoleService")
	private StaffRole staffRoleService;
	

	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Pageable pageable, ModelMap model) {
		shopService.getRow().put("pageable", pageable);
		Page page = shopService.queryPage();
		model.addAttribute("page", page);
		userService.getRow().clear();
		return "/cxhlpage/platform/shop/profile/list";
	}
	

	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		//商家分类
		DataSet shop_type_list =shopTypeService.querySummaryList();
		//省份
		model.addAttribute("province_list", zoneService.findAllProvince());
		model.addAttribute("shop_type_list", shop_type_list);
		return "/cxhlpage/platform/shop/profile/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseVO save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		ResponseVO ovo =null;
		Row row =MapUtils.convertMaptoRowWithoutNullField(map);
		String id =row.getString("id","");
		if(StringUtils.isEmptyOrNull(id))
		{
			shopService.insert(row);
			//添加商家管理用户
			String shop_id =row.getString("id");
			Row shopAdminRow =new Row();
			shopAdminRow.put("shop_id", shop_id);
			shopAdminRow.put("username", "admin");
			shopAdminRow.put("password", Md5Util.Md5("admin"));
			shopAdminService.insert(shopAdminRow);
			// add to sm_staff
			Row staffRow =new Row();
			staffRow.put("staff_name","admin" );
			staffRow.put("password",Md5Util.Md5("admin"));
			staffRow.put("real_name","admin" );
			staffRow.put("rtx_id",shopAdminRow.getString("id"));
			staffService.insert(staffRow);
			// add staff role
			String staff_no =staffRow.getString("staff_no","");
			Row staffRoleRow =new Row();
			staffRoleRow.put("staff_no", staff_no);
			staffRoleRow.put("role_id", "10002");
			staffRoleRow.put("assign_state", "1");
			staffRoleRow.put("use_state", "1");
			staffRoleService.setRow(staffRoleRow);
			staffRoleService.save();
			staffRoleService.getRow().clear();
		}
		else
		{
			shopService.update(row);
			//检查是否存在商家管理员
			Row shopRow =shopAdminService.findByShopId(id);
			if(shopRow == null)
			{
				Row shopAdminRow =new Row();
				shopAdminRow.put("shop_id", id);
				shopAdminRow.put("username", "admin");
				shopAdminRow.put("password", Md5Util.Md5("admin"));
				shopAdminService.insert(shopAdminRow);
				// add to sm_staff
				Row staffRow =new Row();
				staffRow.put("staff_name","admin" );
				staffRow.put("password",Md5Util.Md5("admin"));
				staffRow.put("real_name","admin" );
				staffRow.put("rtx_id",shopAdminRow.getString("id"));
				staffService.insert(staffRow);
				// add staff role
				String staff_no =staffRow.getString("staff_no","");
				Row staffRoleRow =new Row();
				staffRoleRow.put("staff_no", staff_no);
				staffRoleRow.put("role_id", "10002");
				staffRoleRow.put("assign_state", "1");
				staffRoleRow.put("use_state", "1");
				staffRoleService.setRow(staffRoleRow);
				staffRoleService.save();
				staffRoleService.getRow().clear();
			}
		}
		ovo =new ResponseVO(0,"保存成功");
		ovo.put("id", row.getString("id"));
		return ovo;
	}

	@RequestMapping(value = "/edit")
	public String edit(Long id, ModelMap model) throws Exception {
		Assert.notNull(id);
		Row row =shopService.findDetail(String.valueOf(id));
		model.addAttribute("row", row);
		//商家分类
		DataSet shop_type_list =shopTypeService.querySummaryList();
		//省份
		model.addAttribute("province_list", zoneService.findAllProvince());
		model.addAttribute("shop_type_list", shop_type_list);
		String province_id =row.getString("province_id","");
		String city_id =row.getString("city_id","");
		if(! StringUtils.isEmptyOrNull(province_id))
		{
			DataSet city_list =zoneService.findCitiesByProId(province_id);
			model.addAttribute("city_list", city_list);
		}
		if(! StringUtils.isEmptyOrNull(city_id))
		{
			DataSet zone_list =zoneService.findZoneByCityId(city_id);
			model.addAttribute("zone_list", zone_list);
		}
		return "/cxhlpage/platform/shop/profile/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row row=MapUtils.convertMaptoRowWithoutNullField(map);
		shopTypeService.update(row);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:list.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(String[] ids) {
		shopService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
	/**
	 * 检查名称是否已存在
	 */
	@RequestMapping(value = "/check_name", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkName(String NAME) {
		if(StringUtils.isEmptyOrNull(NAME))
		{
			return false;
		}
		return (shopTypeService.isNameExisted(NAME));
	}
	
	/**
	 * 检查名称是否已存在
	 */
	@RequestMapping(value = "/check_extra_name", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkExtraName(String ID,String NAME) {
		if(StringUtils.isEmptyOrNull(NAME))
		{
			return false;
		}
		return (shopTypeService.isExtraNameExisted(ID,NAME));
	}
	
}
