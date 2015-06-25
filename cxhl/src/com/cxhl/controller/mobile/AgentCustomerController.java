package com.cxhl.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.AgentCustomerService;
import com.cxhl.service.AgentService;
import com.cxhl.service.RoomService;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;

@Controller("mobileAgentCustomerController")
@RequestMapping("/api/agent/customer")
public class AgentCustomerController extends BaseController {
	
	private static Logger logger = Logger.getLogger(AgentCustomerController.class); 
	
	@Resource(name = "fzbAgentCustomerService")
	private AgentCustomerService agentCustomerService;
	
	@Resource(name = "fzbAgentService")
	private AgentService agentService;

	@Resource(name = "fzbRoomService")
	private RoomService roomService;
	/**
	 * 客户列表
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/list")
	public @ResponseBody
	String queryAgentCustomerList(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("查询客户列表");
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-10012,"用户编号不能为空","用户编号不能为空");
		}
		Row agentRow =agentService.find(user_id);
		if(agentRow == null)
		{
			ovo =new OVO(-10012,"用户不存在","用户不存在");
		}
		String telephone =agentRow.getString("telephone","");
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet ds =agentCustomerService.queryPage(telephone, Integer.parseInt(page), Integer.parseInt(page_size));
		ovo =new OVO(0,"查询成功","");
		ovo.set("list", ds);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 中介查询房源详情
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/room_detail")
	public @ResponseBody
	String queryRoomDetail(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String id =ivo.getString("room_id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"房源编号不能为空","");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row row = roomService.findDetailById(id);
		String code =row.getString("code","");
		String status =row.getString("status","");
		String status_name ="";
		String address =row.getString("address","");
		String start_date =row.getString("start_date","");
		String end_date =row.getString("end_date","");
		String city_name =row.getString("city_name","");
		String zone_name =row.getString("zone_name","");
		String province_name =row.getString("province_name","");
		String area =row.getString("area","");
		String money =row.getString("monthly_rent",null);
		String deposit =row.getString("deposit",null);
		String invite_code =row.getString("invite_code","");
		String pay_day =row.getString("pay_day","");
		//查询房源信息：水、电、燃气、备注
		String electricity_num =row.getString("electricity_num","");
		String water_num =row.getString("water_num","");
		String gas_num =row.getString("gas_num","");
		String property =row.getString("property","");
		String remark =row.getString("remark","");
		if(!StringUtils.isEmptyOrNull(electricity_num))
		{
			electricity_num =NumberUtils.getTwoDecimal(electricity_num);
		}
		if(! StringUtils.isEmptyOrNull(water_num))
		{
			water_num =NumberUtils.getTwoDecimal(water_num);
		}
		if(! StringUtils.isEmptyOrNull(gas_num))
		{
			gas_num =NumberUtils.getTwoDecimal(gas_num);
		}
		if(! StringUtils.isEmptyOrNull(property))
		{
			property =NumberUtils.getTwoDecimal(property);
		}
		if(status.equals("0") || status.equals("1"))
		{
			status_name ="待租";
		}
		else if(status.equals("2") || status.equals("-1"))
		{
			status_name ="出租中";
		}
		else if(status.equals("-2") || status.equals("-3")|| status.equals("-4"))
		{
			status_name ="结束";
		}
		ovo =new OVO(0,"操作成功","");
		ovo.set("id", id);
		ovo.set("code", code);
		ovo.set("status", status);
		ovo.set("status_name", status_name);
		ovo.set("address", address);
		ovo.set("start_date", start_date);
		ovo.set("end_date", end_date);
		ovo.set("province_name", province_name);
		ovo.set("city_name", city_name);
		ovo.set("zone_name", zone_name);
		ovo.set("area", area);
		ovo.set("money", money);
		ovo.set("deposit", deposit);
		ovo.set("invite_code", invite_code);
		ovo.set("pay_day", pay_day);
		ovo.set("electricity_num", electricity_num);
		ovo.set("water_num", water_num);
		ovo.set("gas_num", gas_num);
		ovo.set("property", property);
		ovo.set("remark", remark);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

}
