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

import com.cxhl.service.RenterMonthlyMoneyService;
import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.Row;

@Controller("fzbPlatformChargeController")
@RequestMapping("/fzbpage/platform/charge")
/**
 * 房东收租记录
 * @author Administrator
 */
public class ChargeContrller  extends BaseController{

	@Resource(name = "fzbRentMonthlyMoneyService")
	private RenterMonthlyMoneyService renterMonthlyMoneyService;
	
	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/ChargeList")
	public String list(String pay_status,Pageable pageable, ModelMap model) throws Exception {
		Page page = renterMonthlyMoneyService.queryPage(pay_status,pageable);
		model.addAttribute("page", page);
		model.addAttribute("pay_status", pay_status);
		return "/fzbpage/platform/charge/ChargeList";
	}

	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		return "/fzbpage/platform/charge/add";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) {
		return "redirect:ChargeList.do";
	}

	@RequestMapping(value = "/edit")
	public String edit(String id, ModelMap model) throws Exception {
		Assert.notNull(id);
		Assert.notNull(id, "编号不能为空!");
		Row row =renterMonthlyMoneyService.findById(id);
		String rent_name =row.getString("rent_name","");
		String landlord_name =row.getString("landlord_name","");
		if(! StringUtils.isEmptyOrNull(rent_name))
		{
			rent_name =AesUtil.decode(rent_name);
			row.put("rent_name", rent_name);
		}
		if(! StringUtils.isEmptyOrNull(landlord_name))
		{
			landlord_name =AesUtil.decode(landlord_name);
			row.put("landlord_name", landlord_name);
		}
		model.addAttribute("row", row);
		return "/fzbpage/platform/charge/edit";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws Exception {
		Row chargeRow=MapUtils.convertMaptoRowWithoutNullField(map);
		String rent_name =chargeRow.getString("rent_name","");
		String landlord_name =chargeRow.getString("landlord_name","");
		if(! StringUtils.isEmptyOrNull(rent_name))
		{
			rent_name =AesUtil.encode(rent_name);
			chargeRow.put("rent_name", rent_name);
		}
		if(! StringUtils.isEmptyOrNull(landlord_name))
		{
			landlord_name =AesUtil.encode(landlord_name);
			chargeRow.put("landlord_name", landlord_name);
		}
		renterMonthlyMoneyService.update(chargeRow);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:ChargeList.do";
	}
	
	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		return SUCCESS_MESSAGE;
	}
}
