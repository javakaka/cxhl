package com.cxhl.controller.admin;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cxhl.service.RentService;
import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.Row;

@Controller("fzbPlatformPresentLockController")
@RequestMapping("/fzbpage/platform/present_lock")
/**
 * 需要赠送门锁的信息管理 
 * @author Administrator
 */
public class PresentLockContrller  extends BaseController{

	@Resource(name = "fzbRentService")
	private RentService rentService;

	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/PresentList")
	public String list(String present_status,Pageable pageable, ModelMap model) throws Exception {
		Page page = rentService.queryNeedPresentLockPage(present_status,pageable);
		model.addAttribute("page", page);
		model.addAttribute("present_status", present_status);
		return "/fzbpage/platform/present_lock/PresentList";
	}

	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		return "/fzbpage/platform/present_lock/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) {
		Row rentRow =MapUtils.convertMaptoRowWithoutNullField(map);
		rentService.update(rentRow);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:PresentList.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(String id, ModelMap model) throws Exception {
		Assert.notNull(id);
		Row row =rentService.findByIdForAdmin(id);
		String user_name =row.getString("user_name","");
		if(! StringUtils.isEmptyOrNull(user_name))
		{
			user_name =AesUtil.decode(user_name);
			row.put("user_name", user_name);
		}
		model.addAttribute("row", row);
		return "/fzbpage/platform/present_lock/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) {
		Row rentRow =MapUtils.convertMaptoRowWithoutNullField(map);
		rentService.update(rentRow);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:PresentList.do";
	}
}