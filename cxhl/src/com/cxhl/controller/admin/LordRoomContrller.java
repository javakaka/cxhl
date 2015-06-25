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

import com.cxhl.service.AgentService;
import com.cxhl.service.RoomService;
import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.system.SystemZoneService;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

@Controller("fzbPlatformLandlordRoomController")
@RequestMapping("/fzbpage/platform/landlord_room")
/**
 * 房东房源管理 
 * @author Administrator
 */
public class LordRoomContrller  extends BaseController{

	@Resource(name = "fzbAgentService")
	private AgentService agentService;
	
	@Resource(name = "fzbRoomService")
	private RoomService roomService;
	
	@Resource(name = "frameworkSystemZoneService")
	private SystemZoneService zoneService;
	
	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/RoomList")
	public String list(String room_status,Pageable pageable, ModelMap model) {
		Page page = roomService.queryPage(room_status,pageable);
		model.addAttribute("page", page);
		model.addAttribute("room_status", room_status);
		roomService.getRow().clear();
		return "/fzbpage/platform/landlord_room/RoomList";
	}

	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		//city 
		Row city_zone_row =zoneService.queryAllCityAndZone();
		DataSet provinceDs =zoneService.findAllOpenedProvince();
		DataSet cityDs =(DataSet)city_zone_row.get("city");
		DataSet zoneDs =(DataSet)city_zone_row.get("zone");
		model.addAttribute("province_list", provinceDs);
		model.addAttribute("city_list", cityDs);
		model.addAttribute("zone_list", zoneDs);
		//region
		return "/fzbpage/platform/landlord_room/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) {
		Row roomRow=MapUtils.convertMaptoRowWithoutNullField(map);
		String landlord_name =roomRow.getString("landlord_name","");
		if(! StringUtils.isEmptyOrNull(landlord_name))
		{
			roomRow.remove("LANDLORD_NAME");
		}
		roomService.insert(roomRow);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:RoomList.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(String id, ModelMap model) {
		Assert.notNull(id);
		model.addAttribute("row", roomService.findById(id));
		//city 
		Row city_zone_row =zoneService.queryAllCityAndZone();
		DataSet provinceDs =zoneService.findAllOpenedProvince();
		DataSet cityDs =(DataSet)city_zone_row.get("city");
		DataSet zoneDs =(DataSet)city_zone_row.get("zone");
		model.addAttribute("province_list", provinceDs);
		model.addAttribute("city_list", cityDs);
		model.addAttribute("zone_list", zoneDs);
		//region
		
		
		return "/fzbpage/platform/landlord_room/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) {
		Row roomRow=MapUtils.convertMaptoRowWithoutNullField(map);
		roomService.update(roomRow);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:RoomList.do";
	}
	
	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		agentService.delete(ids);
		return SUCCESS_MESSAGE;
	}
}
