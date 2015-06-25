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

import com.cxhl.service.RentService;
import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.Md5Util;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.Row;

@Controller("fzbPlatformRentRoomController")
@RequestMapping("/fzbpage/platform/rent_room")
/**
 * 租客承租房源管理 
 * @author Administrator
 */
public class RentRoomContrller  extends BaseController{

	@Resource(name = "fzbRentService")
	private RentService rentService;

	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/RoomList")
	public String list(String present,Pageable pageable, ModelMap model) throws Exception {
		Page page = rentService.queryPage(present,pageable);
		model.addAttribute("page", page);
		model.addAttribute("present", present);
		return "/fzbpage/platform/rent_room/RoomList";
	}

	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		return "/fzbpage/platform/rent_room/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) {
		Row agentRow =MapUtils.convertMaptoRowWithoutNullField(map);
//		String telephone =agentRow.getString("telephone",null);
		String password =agentRow.getString("password",null);
		password =Md5Util.Md5(password);
//		String email =agentRow.getString("email","");
//		String name =agentRow.getString("name","");
//		String sex =agentRow.getString("sex","");
//		String bank_card_no =agentRow.getString("bank_card_no","");
		return "redirect:RoomList.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(String id, ModelMap model) throws Exception {
		Assert.notNull(id);
		Row row =rentService.findByIdForAdmin(id);
		String user_name =row.getString("user_name","");
		String landlord_name =row.getString("landlord_name","");
		if(! StringUtils.isEmptyOrNull(user_name))
		{
			user_name =AesUtil.decode(user_name);
			row.put("user_name", user_name);
		}
		if(! StringUtils.isEmptyOrNull(landlord_name))
		{
			landlord_name =AesUtil.decode(landlord_name);
			row.put("landlord_name", landlord_name);
		}
		model.addAttribute("row", row);
		return "/fzbpage/platform/rent_room/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) {
		Row rentRow=MapUtils.convertMaptoRowWithoutNullField(map);
		rentService.update(rentRow);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:RoomList.do";
	}
	
	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		rentService.delete(ids);
		return SUCCESS_MESSAGE;
	}
}