package com.cxhl.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.AgentCustomerService;
import com.cxhl.service.AgentService;
import com.cxhl.service.RentService;
import com.cxhl.service.RenterMonthlyMoneyService;
import com.cxhl.service.RoomService;
import com.cxhl.service.StopEntrustRoomService;
import com.cxhl.service.UserService;
import com.ezcloud.framework.service.system.SystemZoneService;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.DateUtils;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.DateUtil;

@Controller("mobileRoomController")
@RequestMapping("/api/core/room")
public class RoomController extends BaseController {
	
	private static Logger logger = Logger.getLogger(RoomController.class); 
	@Resource(name = "frameworkSystemZoneService")
	private SystemZoneService zoneService;
	
	@Resource(name = "fzbUserService")
	private UserService userService;
	
	@Resource(name = "fzbAgentService")
	private AgentService agentService;
	
	@Resource(name = "fzbRoomService")
	private RoomService roomService;
	
	@Resource(name = "fzbRentService")
	private RentService rentService;
	
	@Resource(name = "fzbRentMonthlyMoneyService")
	private RenterMonthlyMoneyService renterMonthlyMoneyService;
	
	@Resource(name = "fzbStopEntrustRoomService")
	private StopEntrustRoomService stopEntrustRoomService;

	@Resource(name = "fzbAgentCustomerService")
	private AgentCustomerService agentCustomerService;
	
	/**
	 * 新增房源
	 * status 0未出租，房东未确认
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/add")
	public @ResponseBody
	String addRoom(HttpServletRequest request) throws Exception
	{
		logger.info("新建房源");
		parseRequest(request);
		Row roomRow =new Row();
		int should_bind_card =0;
		//房东编号 必填
		String landlord_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(landlord_id))
		{
			ovo =new OVO(-10020,"房东编号不能为空","房东编号不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row staffRow =userService.find(landlord_id);
		if(staffRow == null)
		{
			ovo =new OVO(-10020,"房东用户不存在","房东用户不存在");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		roomRow.put("landlord_id", landlord_id);
		//检查用户是否已绑定银行卡
		String bank_card_no =staffRow.getString("bank_card_no",null);
		if(StringUtils.isEmptyOrNull(bank_card_no))
		{
			should_bind_card =1;
			ovo =new OVO(-10020,"请先绑定银行卡","请先绑定银行卡");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//省份编号 必填
		String province =ivo.getString("province_id",null);
		if(StringUtils.isEmptyOrNull(province))
		{
			ovo =new OVO(-10020,"省份编号不能为空","省份编号不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		roomRow.put("province", province);
		//城市编号 必填
		String city =ivo.getString("city_id",null);
		if(StringUtils.isEmptyOrNull(city))
		{
			ovo =new OVO(-10020,"城市编号不能为空","城市编号不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		roomRow.put("city", city);
		//城市辖区编号 必填
		String region =ivo.getString("zone_id",null);
		if(StringUtils.isEmptyOrNull(region))
		{
			ovo =new OVO(-10020,"区域东编号不能为空","区域东编号不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		roomRow.put("region", region);
		//详细地址 必填
		String address =ivo.getString("address",null);
		if(StringUtils.isEmptyOrNull(address))
		{
			ovo =new OVO(-10020,"详细地址不能为空","详细地址不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		roomRow.put("address", address);
		//面积 必填 /平方米
		String area =ivo.getString("area",null);
		if(StringUtils.isEmptyOrNull(area))
		{
			ovo =new OVO(-10020,"面积不能为空","面积不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//验证是正数字
		if(! NumberUtils.isPositiveNumber(area))
		{
			ovo =new OVO(-10020,"面积必须是正数","面积必须是正数");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		area =String.valueOf(NumberUtils.getTwoDecimal(area));
		roomRow.put("area", area);
		//开始日期 必填
		String start_date =ivo.getString("start_date",null);
		if(StringUtils.isEmptyOrNull(start_date))
		{
			ovo =new OVO(-10020,"面积不能为空","面积不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(! DateUtil.checkDateFormatYYYY_MM_DD(start_date))
		{
			ovo =new OVO(-10020,"开始日期格式错误","开始日期格式错误");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		roomRow.put("start_date", start_date);
		//结束日期 必填
		String end_date =ivo.getString("end_date",null);
		if(StringUtils.isEmptyOrNull(end_date))
		{
			ovo =new OVO(-10020,"结束日期不能为空","结束日期不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(! DateUtil.checkDateFormatYYYY_MM_DD(end_date))
		{
			ovo =new OVO(-10020,"结束日期格式错误","结束日期格式错误");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		roomRow.put("end_date", end_date);
		long dateCompare =DateUtil.compare(start_date+" 00:00:00", end_date+" 00:00:00");
		if(dateCompare>=0)
		{
			ovo =new OVO(-10020,"结束日期应该大于开始日期","结束日期应该大于开始日期");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//每月收款日 必填
		String pay_day =ivo.getString("pay_day",null);
		if(StringUtils.isEmptyOrNull(pay_day))
		{
			ovo =new OVO(-10020,"每月收款日不能为空","每月收款日不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(! NumberUtils.isPositiveNumber(pay_day))
		{
			ovo =new OVO(-10020,"每月收款日应该是数字","每月收款日应该是数字");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		int day =Integer.parseInt(pay_day);
		if(day < 0 || day >31)
		{
			ovo =new OVO(-10020,"每月收款日应该范围[1-31]","每月收款日应该范围[1-31]");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		roomRow.put("pay_day", pay_day);
		//租金 必填
		String monthly_rent =ivo.getString("monthly_rent",null);
		if(StringUtils.isEmptyOrNull(monthly_rent))
		{
			ovo =new OVO(-10020,"租金不能为空","租金不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(! NumberUtils.isPositiveNumber(pay_day))
		{
			ovo =new OVO(-10020,"租金应该是数字","租金应该是数字");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		monthly_rent =String.valueOf(NumberUtils.getTwoDecimal(monthly_rent));
		roomRow.put("monthly_rent", monthly_rent);
		//押金 必填
		String deposit =ivo.getString("deposit",null);
		if(StringUtils.isEmptyOrNull(deposit))
		{
			ovo =new OVO(-10020,"押金不能为空","押金不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(! NumberUtils.isPositiveNumber(deposit))
		{
			ovo =new OVO(-10020,"押金应该是数字","押金应该是数字");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		deposit =String.valueOf(NumberUtils.getTwoDecimal(deposit));
		roomRow.put("deposit", deposit);
		//邀请码 选填
		String invite_code =ivo.getString("invite_code",null);
		if(! StringUtils.isEmptyOrNull(invite_code))
		{
			Row agentRow =agentService.findByTelephone(invite_code);
			if(agentRow == null)
			{
				ovo =new OVO(-10020,"邀请码错误","邀请码错误");
				ovo.set("should_bind_card", should_bind_card);
				return AesUtil.encode(VOConvert.ovoToJson(ovo));
			}
			roomRow.put("invite_code", invite_code);
		}
		//房源状态 0 
		String status ="0";
		roomRow.put("status", status);
		//房源租金缴纳状态 0未交1已交
		String deposit_status ="0";
		roomRow.put("deposit_status", deposit_status);
		
		//判断是新增还是修改
		String id =ivo.getString("id","");
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo = roomService.addRoom(roomRow);
		}
		else
		{
			Row room =roomService.findById(id);
			if(room == null || room.get("id",null) == null)
			{
				ovo =new OVO(-10020,"房源id错误，不存在","房源id错误，不存在");
				return AesUtil.encode(VOConvert.ovoToJson(ovo));
			}
			else
			{
				roomRow.put("id", id);
				roomService.update(roomRow);
				ovo.set("id", id);
				ovo.set("code", room.getString("code"));
			}
		}
		ovo.set("should_bind_card", should_bind_card);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	
	/**
	 * 新增房源 --待租
	 * status 0 待租/新建/0，房东未确认
	 * 需要保存的字段
	 * 省、市、区、详细地址、面积、房东姓名、房东联系电话、身份证号码、月租、押金
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/add_create")
	public @ResponseBody
	String addCreateRoom(HttpServletRequest request) throws Exception
	{
		logger.info("新建待租房源");
		parseRequest(request);
		Row roomRow =new Row();
		int should_bind_card =0;
		//房东编号 必填
		String landlord_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(landlord_id))
		{
			ovo =new OVO(-10020,"房东编号不能为空","房东编号不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row staffRow =userService.find(landlord_id);
		if(staffRow == null)
		{
			ovo =new OVO(-10020,"房东用户不存在","房东用户不存在");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		roomRow.put("landlord_id", landlord_id);
		//检查用户是否已绑定银行卡
		String bank_card_no =staffRow.getString("bank_card_no",null);
		if(StringUtils.isEmptyOrNull(bank_card_no))
		{
			should_bind_card =1;
//			ovo =new OVO(-10020,"请先绑定银行卡","请先绑定银行卡");
//			ovo.set("should_bind_card", should_bind_card);
//			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//省份编号 必填
		String province =ivo.getString("province_id",null);
		if(StringUtils.isEmptyOrNull(province))
		{
			ovo =new OVO(-10020,"省份编号不能为空","省份编号不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		roomRow.put("province", province);
		//城市编号 必填
		String city =ivo.getString("city_id",null);
		if(StringUtils.isEmptyOrNull(city))
		{
			ovo =new OVO(-10020,"城市编号不能为空","城市编号不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		roomRow.put("city", city);
		//城市辖区编号 必填
		String region =ivo.getString("zone_id",null);
		if(StringUtils.isEmptyOrNull(region))
		{
			ovo =new OVO(-10020,"区域东编号不能为空","区域东编号不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		roomRow.put("region", region);
		//详细地址 必填
		String address =ivo.getString("address",null);
		if(StringUtils.isEmptyOrNull(address))
		{
			ovo =new OVO(-10020,"详细地址不能为空","详细地址不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		roomRow.put("address", address);
		//面积 必填 /平方米
		String area =ivo.getString("area",null);
		if(StringUtils.isEmptyOrNull(area))
		{
			ovo =new OVO(-10020,"面积不能为空","面积不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//验证是正数字
		if(! NumberUtils.isPositiveNumber(area))
		{
			ovo =new OVO(-10020,"面积必须是正数","面积必须是正数");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		area =String.valueOf(NumberUtils.getTwoDecimal(area));
		roomRow.put("area", area);
		//房东姓名
		String landlord_name =ivo.getString("user_name",null);
		if(StringUtils.isEmptyOrNull(landlord_name))
		{
			ovo =new OVO(-10020,"房东姓名不能为空","房东姓名不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//姓名不为空时，如果用户表中的姓名字段为空，则保存姓名到用户表
		String user_name =staffRow.getString("name",null);
		if(StringUtils.isEmptyOrNull(user_name))
		{
			user_name =AesUtil.encode(landlord_name);
			staffRow.put("name", user_name);
			staffRow =MapUtils.convertMaptoRowWithoutNullField(staffRow);
			userService.update(staffRow);
		}
		//房东电话/手机号
		String landlord_telephone =ivo.getString("telephone",null);
		if(StringUtils.isEmptyOrNull(landlord_telephone))
		{
			ovo =new OVO(-10020,"房东电话不能为空","房东电话不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//房东身份证号码
		String landlord_id_card_no =ivo.getString("id_card_no",null);
		if(StringUtils.isEmptyOrNull(landlord_id_card_no))
		{
			ovo =new OVO(-10020,"房东身份证号码不能为空","房东身份证号码不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//身份证号码不为空时，如果用户表中的身份证号码字段为空，则保存身份证号码到用户表
		String user_id_card_no =staffRow.getString("id_card_no",null);
		String user_bank_card_no =staffRow.getString("bank_card_no",null);
		if( StringUtils.isEmptyOrNull(user_bank_card_no))
		{
			should_bind_card =1;
		}
		if(StringUtils.isEmptyOrNull(user_id_card_no))
		{
			staffRow.put("id_card_no", landlord_id_card_no);
			staffRow =MapUtils.convertMaptoRowWithoutNullField(staffRow);
			userService.update(staffRow);
		}
		//租金 必填
		String monthly_rent =ivo.getString("monthly_rent",null);
		if(StringUtils.isEmptyOrNull(monthly_rent))
		{
			ovo =new OVO(-10020,"租金不能为空","租金不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(! NumberUtils.isPositiveNumber(monthly_rent))
		{
			ovo =new OVO(-10020,"租金应该是数字","租金应该是数字");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		monthly_rent =String.valueOf(NumberUtils.getTwoDecimal(monthly_rent));
		roomRow.put("monthly_rent", monthly_rent);
		//押金 必填
		String deposit =ivo.getString("deposit",null);
		if(StringUtils.isEmptyOrNull(deposit))
		{
			ovo =new OVO(-10020,"押金不能为空","押金不能为空");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(! NumberUtils.isPositiveNumber(deposit))
		{
			ovo =new OVO(-10020,"押金应该是数字","押金应该是数字");
			ovo.set("should_bind_card", should_bind_card);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		deposit =String.valueOf(NumberUtils.getTwoDecimal(deposit));
		roomRow.put("deposit", deposit);
		//房源状态 0 
		String status ="0";
		roomRow.put("status", status);
		//房源租金缴纳状态 0未交1已交
		String deposit_status ="1";
		roomRow.put("deposit_status", deposit_status);
		
		//判断是新增还是修改
		String id =ivo.getString("id","");
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo = roomService.addRoom(roomRow);
		}
		else
		{
			Row room =roomService.findById(id);
			if(room == null || room.get("id",null) == null)
			{
				ovo =new OVO(-10020,"房源id错误，不存在","房源id错误，不存在");
				return AesUtil.encode(VOConvert.ovoToJson(ovo));
			}
			else
			{
				roomRow.put("id", id);
				roomService.update(roomRow);
				ovo.set("id", id);
				ovo.set("code", room.getString("code"));
			}
		}
		ovo.set("should_bind_card", should_bind_card);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

	
	/**
	 * 新增发布房源
	 * status 0 签约中，房东未确认
	 * 包含字段：
	 * 月租（可修改）、押金（可修改）、起租时间、结束时间、收款日、水表读数、电表读数、燃气读数、物业管理费、邀请码、备注
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/add_publish")
	public @ResponseBody
	String addPublish(HttpServletRequest request) throws Exception
	{
		logger.info("新建发布房源");
		parseRequest(request);
		Row roomRow =new Row();
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-10020,"房源id不能为空","房源id不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		roomRow.put("id",id);
		//租金 必填
		String monthly_rent =ivo.getString("monthly_rent",null);
		if(StringUtils.isEmptyOrNull(monthly_rent))
		{
			ovo =new OVO(-10020,"租金不能为空","租金不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(! NumberUtils.isPositiveNumber(monthly_rent))
		{
			ovo =new OVO(-10020,"租金应该是数字","租金应该是数字");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		monthly_rent =String.valueOf(NumberUtils.getTwoDecimal(monthly_rent));
		roomRow.put("monthly_rent", monthly_rent);
		//押金 必填
		String deposit =ivo.getString("deposit",null);
		if(StringUtils.isEmptyOrNull(deposit))
		{
			ovo =new OVO(-10020,"押金不能为空","押金不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(! NumberUtils.isPositiveNumber(deposit))
		{
			ovo =new OVO(-10020,"押金应该是数字","押金应该是数字");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		deposit =String.valueOf(NumberUtils.getTwoDecimal(deposit));
		roomRow.put("deposit", deposit);
		//开始日期 必填
		String start_date =ivo.getString("start_date",null);
		if(StringUtils.isEmptyOrNull(start_date))
		{
			ovo =new OVO(-10020,"开始日期不能为空","开始日期不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(! DateUtil.checkDateFormatYYYY_MM_DD(start_date))
		{
			ovo =new OVO(-10020,"开始日期格式错误","开始日期格式错误");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		roomRow.put("start_date", start_date);
		//结束日期 必填
		String end_date =ivo.getString("end_date",null);
		if(StringUtils.isEmptyOrNull(end_date))
		{
			ovo =new OVO(-10020,"结束日期不能为空","结束日期不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(! DateUtil.checkDateFormatYYYY_MM_DD(end_date))
		{
			ovo =new OVO(-10020,"结束日期格式错误","结束日期格式错误");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		roomRow.put("end_date", end_date);
		long dateCompare =DateUtil.compare(start_date+" 00:00:00", end_date+" 00:00:00");
		if(dateCompare>=0)
		{
			ovo =new OVO(-10020,"结束日期应该大于开始日期","结束日期应该大于开始日期");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//每月收款日 必填
		String pay_day =ivo.getString("pay_day",null);
		if(StringUtils.isEmptyOrNull(pay_day))
		{
			ovo =new OVO(-10020,"每月收款日不能为空","每月收款日不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(! NumberUtils.isPositiveNumber(pay_day))
		{
			ovo =new OVO(-10020,"每月收款日应该是数字","每月收款日应该是数字");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		int day =Integer.parseInt(pay_day);
		if(day < 0 || day >31)
		{
			ovo =new OVO(-10020,"每月收款日应该范围[1-31]","每月收款日应该范围[1-31]");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		roomRow.put("pay_day", pay_day);
		
		//邀请码 选填
		String invite_code =ivo.getString("invite_code",null);
		if(! StringUtils.isEmptyOrNull(invite_code))
		{
//			invite_code =AesUtil.encode(invite_code);
			Row agentRow =agentService.findByTelephone(invite_code);
			if(agentRow == null)
			{
				ovo =new OVO(-10020,"邀请码错误","邀请码错误");
				return AesUtil.encode(VOConvert.ovoToJson(ovo));
			}
			roomRow.put("invite_code", invite_code);
		}
		//水
		String water_num =ivo.getString("water_num","");
		roomRow.put("water_num", water_num);
		//电
		String electricity_num =ivo.getString("electricity_num","");
		roomRow.put("electricity_num", electricity_num);
		//燃气
		String gas_num =ivo.getString("gas_num","");
		roomRow.put("gas_num", gas_num);
		//物业管理费
		String property =ivo.getString("property","");
		roomRow.put("property", property);
		//备注
		String remark =ivo.getString("remark","");
		roomRow.put("remark", remark);
		//房源状态 1
		String status ="0";
		roomRow.put("status", status);
		//房源租金缴纳状态 0未交1已交
		String deposit_status ="1";
		roomRow.put("deposit_status", deposit_status);
		roomService.update(roomRow);
		Row roomUpdateRow =roomService.findById(id);
		String code =roomUpdateRow.getString("code","");
		String user_id =roomUpdateRow.getString("landlord_id","");
		Row userRow =userService.find(user_id);
		String userBankCardNo =userRow.getString("bank_card_no","");
		int should_bind_card =0;
		if(StringUtils.isEmptyOrNull(userBankCardNo))
		{
			should_bind_card =1;
		}
		ovo =new OVO(0,"","");
		ovo.set("id", id);
		ovo.set("code", code);
		ovo.set("should_bind_card", should_bind_card);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 房东填写后确认页面查询数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/landlord_find")
	public @ResponseBody
	String landlordFindRoom(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-10020,"房源编号不能为空","房源编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row roomRow =roomService.findById(id);
		String html= "@@NAME,您拟出租@@CITY@@ZONE@@ADDRESS房源," +
				"面积@@AREA平米,起租日@@START_YEAR年@@START_MONTH月@@START_DAY日" +
				",终租日@@END_YEAR年@@END_MONTH月@@END_DAY日," +
				"房租收款日为该房源出租期内每月@@PAY_DAY日,月租@@MONTHLY_RENT元人民币," +
				"押金@@DEPOSIT元人民币。" +
//				"<br/><br/>水表读数:@@WATER_NUM"+
//				"<br/><br/>电表读数:@@ELECTRICITY_NUM"+
//				"<br/><br/>燃气读数:@@GAS_NUM"+
//				"<br/><br/>物业管理费:@@PROPERTY"+
				"<br/><br/>该出租房源ID:@@CODE";
		String user_id =roomRow.getString("landlord_id",null);
		Row staffRow =userService.find(user_id);
		String username =staffRow.getString("name","");
		if( !StringUtils.isEmptyOrNull(username))
		{
			username =AesUtil.decode(username);
		}
		html =html.replace("@@NAME", username);
		String city =roomRow.getString("city");
		Row cityRow =zoneService.findCityById(city);
		String cityName =cityRow.getString("name","");
		html =html.replace("@@CITY", cityName);
		String zone =roomRow.getString("region");
		Row zoneRow =zoneService.findZoneById(zone);
		String zoneName =zoneRow.getString("name","");
		html =html.replace("@@ZONE", zoneName);
		String address =roomRow.getString("address","");
		html =html.replace("@@ADDRESS", address);
		String area =roomRow.getString("area","0");
		html =html.replace("@@AREA", area);
		String start_date =roomRow.getString("start_date");
		String end_date =roomRow.getString("end_date");
		String start_year =start_date.substring(0, 4);
		String start_month =start_date.substring(5, 7);
		String start_day =start_date.substring(8, 10);
		String end_year =end_date.substring(0, 4);
		String end_month =end_date.substring(5, 7);
		String end_day =end_date.substring(8, 10);
		html =html.replace("@@START_YEAR", start_year);
		html =html.replace("@@START_MONTH", start_month);
		html =html.replace("@@START_DAY", start_day);
		html =html.replace("@@END_YEAR", end_year);
		html =html.replace("@@END_MONTH", end_month);
		html =html.replace("@@END_DAY", end_day);
		String pay_day =roomRow.getString("pay_day");
		html =html.replace("@@PAY_DAY", pay_day);
		String monthly_rent =roomRow.getString("monthly_rent");
		html =html.replace("@@MONTHLY_RENT", monthly_rent);
		String deposit =roomRow.getString("deposit");
		html =html.replace("@@DEPOSIT", deposit);
//		String electricity_num =roomRow.getString("electricity_num","");
//		html =html.replace("@@ELECTRICITY_NUM", electricity_num);
//		String water_num =roomRow.getString("water_num","");
//		html =html.replace("@@WATER_NUM", water_num);
//		String gas_num =roomRow.getString("gas_num","");
//		html =html.replace("@@GAS_NUM", gas_num);
//		String property =roomRow.getString("property","");
//		html =html.replace("@@PROPERTY", property);
		String code =roomRow.getString("code");
		html =html.replace("@@CODE", code);
		ovo =new OVO(0,"","");
		ovo.set("html", html);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 房东确认出租,确认后房源状态变为1，签约中
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/landlord_confirm")
	public @ResponseBody
	String landlordConfirmRoom(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-10022,"房源id不能为空","房源id不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row roomRow =new Row();
		roomRow.put("id", id);
		String status ="1";
		roomRow.put("status", status);
		int num =roomService.update(roomRow);

		//先删除原来的每月租房记录
		renterMonthlyMoneyService.deleteByRoomId(id);
		//创建每月房租记录
		roomRow =roomService.findById(id);
		String money =roomRow.getString("monthly_rent");
		String start_date =roomRow.getString("start_date","");
		String end_date =roomRow.getString("end_date","");
		DataSet monthDs =DateUtils.getEveryMonthPeriod(start_date, end_date);
		String period_start_date="";
		String period_end_date="";
		Row monthRentRow =null;
		for(int i=0; i< monthDs.size(); i++)
		{
			Row monthRow =(Row)monthDs.get(i);
			period_start_date =monthRow.getString("start_date");
			period_end_date =monthRow.getString("end_date");
			monthRentRow =new Row();
			monthRentRow.put("room_id", id);
			monthRentRow.put("month_num","1" );
			monthRentRow.put("start_time", period_start_date);
			monthRentRow.put("end_time", period_end_date);
			monthRentRow.put("money", money);
			monthRentRow.put("pay_status", "0");
			num =renterMonthlyMoneyService.insert(monthRentRow);
		}
		if(num >0)
		{
			ovo =new OVO(0,"操作成功","");
		}
		else
		{
			ovo =new OVO(-10022,"操作失败","操作失败");
		}
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 租客根据房源唯一码查询承租房源详情
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/agent_find")
	public @ResponseBody
	String agentFindRoom(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String code =ivo.getString("code",null);
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(code))
		{
			ovo =new OVO(-10020,"房源编号不能为空","房源编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-10020,"用户ID不能为空","用户ID不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String name =ivo.getString("name","");
		String telephone =ivo.getString("telephone","");
		String id_card_no =ivo.getString("id_card_no","");
		if(StringUtils.isEmptyOrNull(name))
		{
			ovo =new OVO(-10020,"租客姓名不能为空","租客姓名不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(StringUtils.isEmptyOrNull(telephone))
		{
			ovo =new OVO(-10020,"租客电话不能为空","租客电话不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(StringUtils.isEmptyOrNull(id_card_no))
		{
			ovo =new OVO(-10020,"租客身份证号码不能为空","租客身份证号码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row roomRow =roomService.findByCode(code);
		if(roomRow == null)
		{
			ovo =new OVO(-10020,"房源ID错误","房源ID错误");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String room_status =roomRow.getString("status","");
		if(!room_status.equals("1"))
		{
			ovo =new OVO(-10020,"未签约状态的房源不能出租","未签约状态的房源不能出租");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String html= "@@NAME,您拟承租@@CITY@@ZONE@@ADDRESS房源," +
				"面积@@AREA平米,起租日@@START_YEAR年@@START_MONTH月@@START_DAY日" +
				",终租日@@END_YEAR年@@END_MONTH月@@END_DAY日," +
				"房租收款日为该房源出租期内每月@@PAY_DAY日,月租@@MONTHLY_RENT元人民币," +
				"押金@@DEPOSIT元人民币。" +
				"<br/><br/>该出租房源ID:@@CODE";
		Row staffRow =userService.find(user_id);
		if(staffRow == null)
		{
			ovo =new OVO(-10020,"租客用户不存在","租客用户不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		// 0不需要1需要
		String should_bind_card ="0";
		String credit_card_no =staffRow.getString("credit_card_no","");
		if(StringUtils.isEmptyOrNull(credit_card_no))
		{
			should_bind_card="1";
		}
		//保存姓名到用户表
		Row updateUserRow =new Row();
		boolean userNeedUpdate =false;
		String username =staffRow.getString("name","");
		if( !StringUtils.isEmptyOrNull(username))
		{
			username =AesUtil.decode(username);
		}
		else
		{
			userNeedUpdate =true;
			updateUserRow.put("name", AesUtil.encode(name));
			username=name;
		}
		String u_id_card_no =staffRow.getString("id_card_no","");
		if( StringUtils.isEmptyOrNull(u_id_card_no))
		{
			userNeedUpdate =true;
			updateUserRow.put("id_card_no",u_id_card_no);
		}
		if(userNeedUpdate)
		{
			updateUserRow.put("id", staffRow.getString("id"));
			userService.update(updateUserRow);
		}
		html =html.replace("@@NAME", username);
		String city =roomRow.getString("city");
		Row cityRow =zoneService.findCityById(city);
		String cityName =cityRow.getString("name","");
		html =html.replace("@@CITY", cityName);
		String zone =roomRow.getString("region");
		Row zoneRow =zoneService.findZoneById(zone);
		String zoneName =zoneRow.getString("name","");
		html =html.replace("@@ZONE", zoneName);
		String address =roomRow.getString("address","");
		html =html.replace("@@ADDRESS", address);
		String area =roomRow.getString("area","0");
		html =html.replace("@@AREA", area);
		String start_date =roomRow.getString("start_date");
		String end_date =roomRow.getString("end_date");
		String start_year =start_date.substring(0, 4);
		String start_month =start_date.substring(5, 7);
		String start_day =start_date.substring(8, 10);
		String end_year =end_date.substring(0, 4);
		String end_month =end_date.substring(5, 7);
		String end_day =end_date.substring(8, 10);
		html =html.replace("@@START_YEAR", start_year);
		html =html.replace("@@START_MONTH", start_month);
		html =html.replace("@@START_DAY", start_day);
		html =html.replace("@@END_YEAR", end_year);
		html =html.replace("@@END_MONTH", end_month);
		html =html.replace("@@END_DAY", end_day);
		String pay_day =roomRow.getString("pay_day");
		html =html.replace("@@PAY_DAY", pay_day);
		String monthly_rent =roomRow.getString("monthly_rent");
		html =html.replace("@@MONTHLY_RENT", monthly_rent);
		String deposit =roomRow.getString("deposit");
		html =html.replace("@@DEPOSIT", deposit);
		code =roomRow.getString("code");
		html =html.replace("@@CODE", code);
		ovo =new OVO(0,"","");
		ovo.set("html", html);
		ovo.set("id", roomRow.getString("id"));
		ovo.set("should_bind_card", should_bind_card);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 租客确认承租房源
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/agent_confirm")
	public @ResponseBody
	String agentConfirmRoom(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		//房源ID
		String id =ivo.getString("id",null);
		Row roomRow =new Row();
//		roomRow.put("id", id);
//		String status ="2";
//		roomRow.put("status", status);
		int num =0;
		//是否赠送门锁
		String present =ivo.getString("present",null);
		if(StringUtils.isEmptyOrNull(present))
		{
			present ="0";
		}
		//租客用户编号
		String rent_id =ivo.getString("user_id",null);
		//送货地址
		String address =ivo.getString("address","");
		//收货人姓名
		String name =ivo.getString("name","");
		//收货人电话
		String telephone =ivo.getString("telephone","");
		//新建租房记录
		Row rentRow =new Row();
		rentRow.put("room_id", id);
		roomRow =roomService.findById(id);
		if(roomRow == null)
		{
			ovo =new OVO(-10022,"房源不存在","房源不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String code =roomRow.getString("code","");
		String roomStatus =roomRow.getString("status","");
		if(roomStatus.equals("2") || roomStatus.equals("-1"))
		{
			ovo =new OVO(-10022,"房源已出租","房源已出租");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		else if(roomStatus.equals("0") || roomStatus.equals("-2") 
				|| roomStatus.equals("-3") || roomStatus.equals("-4"))
		{
			ovo =new OVO(-10022,"请先放租","请先放租");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		
		if(present.equals("1"))
		{
			if(StringUtils.isEmptyOrNull(address))
			{
				ovo =new OVO(-10022,"选择赠送门锁后必须填写送货地址","选择赠送门锁后必须填写送货地址");
				return AesUtil.encode(VOConvert.ovoToJson(ovo));
			}
			if(StringUtils.isEmptyOrNull(name))
			{
				ovo =new OVO(-10022,"选择赠送门锁后必须填写收货人姓名","选择赠送门锁后必须填写收货人姓名");
				return AesUtil.encode(VOConvert.ovoToJson(ovo));
			}
			if(StringUtils.isEmptyOrNull(telephone))
			{
				ovo =new OVO(-10022,"选择赠送门锁后必须填写收货人电话","选择赠送门锁后必须填写收货人电话");
				return AesUtil.encode(VOConvert.ovoToJson(ovo));
			}
		}
		rentRow.put("code", code);
		rentRow.put("present", present);
		rentRow.put("rent_id", rent_id);
		rentRow.put("status", "2");
		rentRow.put("address", address);
		rentRow.put("receive_name", name);
		rentRow.put("receive_tel", telephone);
		num = rentService.insert(rentRow);
//		//如果有邀请码，则保存中介的客户
		String invite_code =roomRow.getString("invite_code",null);
		Row agentRow =null;
		Row customerRow =new Row();
		if(!StringUtils.isEmptyOrNull(invite_code))
		{
			agentRow =agentService.findByTelephone(invite_code);
			if(agentRow != null)
			{
				customerRow.put("agent_id", agentRow.getString("id"));
				customerRow.put("landlord_id", roomRow.getString("landlord_id"));
				customerRow.put("rent_id", rent_id);
				customerRow.put("room_id", id);
				agentCustomerService.insert(customerRow);
			}
		}
		//更新房源的状态为2（待租/新建/0、签约中/1、出租中/2、结束/-2、续租/-1、续租结束/-3、删除    /-4）
		roomRow =new Row();
		roomRow.put("id", id);
		roomRow.put("status", "2");
		num =roomService.update(roomRow);
		if(num >0)
		{
			ovo =new OVO(0,"操作成功","");
		}
		else
		{
			ovo =new OVO(-10022,"操作失败","操作失败");
		}
		//更新租客交租金记录表的renter_id字段，设值为租客的id
		renterMonthlyMoneyService.updateRenterUserField(id,rent_id);
		
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	
	/**
	 * 房东查询自己的出租房源列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value ="/list")
	public @ResponseBody
	String list(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		//房东用户ID
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-1,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet room_list =roomService.list(user_id, page, page_size);
		String start_date ="";
		String room_id ="";
		String end_date ="";
		String cur_date =DateUtil.getCurrentDate();
		String month_pay_status ="0";//0未收1已收
		String room_status ="";
		String pay_day ="";
		for(int i=0; i<room_list.size(); i++)
		{
			Row row =(Row)room_list.get(i);
			room_id =row.getString("id","");
			start_date =row.getString("start_date",null);
			end_date =row.getString("end_date",null);
			room_status =row.getString("status","");
			pay_day =row.getString("pay_day","");
			boolean hasPayed =false;
			String pay_type="0";
			String day_num ="0";//当前日期到交租日的天数,正数表示还未到交租日，负数表示已经超出了交租日
			//出租状态的房源
			if(room_status.equals("2") || room_status.equals("-1"))
			{
				hasPayed =renterMonthlyMoneyService.havePayedMonthRentOrNot(room_id, start_date, end_date, cur_date);
				if(hasPayed)
				{
					month_pay_status ="1";
					//如果当前日期所在的交租月份已经交纳了房租，查出交纳房租的方式1通过app交房租、2线下交租、3线下押金抵扣房租
					pay_type =renterMonthlyMoneyService.queryMonthRentStatus(room_id, start_date, end_date, cur_date);
				}
				else
				{
					month_pay_status ="0";
					pay_type="";
					//未交租，计算出离交租日还有多少天,2015-04-01 2015-04-15 15-1=14
					day_num =String.valueOf(renterMonthlyMoneyService.calculateDaysFromNowdayToRentday(room_id, start_date, end_date, cur_date,pay_day));
				}
				row.put("month_pay_status", month_pay_status);
				row.put("pay_type", pay_type);
				row.put("day_num", day_num);
			}
			else
			{
				row.put("month_pay_status", "");
				row.put("pay_type", "");
				row.put("day_num", "");
			}
			room_list.set(i, row);
		}
		ovo =new OVO(0,"操作成功","");
		ovo.set("room_list", room_list);
		//取收租账号信息
		Row userRow =null;
		String bank_card_no ="";
		String bank_card_type ="";
		if(page.equals("1"))
		{
			userRow =userService.find(user_id);
			bank_card_no =userRow.getString("bank_card_no","");
			bank_card_type =userRow.getString("bank_card_type","");
			if(! StringUtils.isEmptyOrNull(bank_card_no))
			{
				bank_card_no =AesUtil.decode(bank_card_no);
			}
			ovo.set("bank_card_no", bank_card_no);
			ovo.set("bank_card_type", bank_card_type);
		}
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	
	/**
	 * 房东查询自己的出租房源列表-出租中和已结束状态的房源
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value ="/using_list")
	public @ResponseBody
	String using_list(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		//房东用户ID
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-1,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet room_list =roomService.using_list(user_id, page, page_size);
		String start_date ="";
		String room_id ="";
		String end_date ="";
		String cur_date =DateUtil.getCurrentDate();
		String month_pay_status ="0";//0未收1已收
		String room_status ="";
		for(int i=0; i<room_list.size(); i++)
		{
			Row row =(Row)room_list.get(i);
			room_id =row.getString("id","");
			start_date =row.getString("start_date",null);
			end_date =row.getString("end_date",null);
			room_status =row.getString("status","");
			boolean hasPayed =false;
			if(room_status.equals("2"))
			{
				hasPayed =renterMonthlyMoneyService.havePayedMonthRentOrNot(room_id, start_date, end_date, cur_date);
				if(hasPayed)
				{
					month_pay_status ="1";
				}
				else
				{
					month_pay_status ="0";
				}
			}
			else
			{
				month_pay_status ="";
			}
			row.put("month_pay_status", month_pay_status);
			room_list.set(i, row);
		}
		ovo =new OVO(0,"操作成功","");
		ovo.set("room_list", room_list);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 房东查询出租房源详情
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/detail")
	public @ResponseBody
	String detail(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		//房源ID
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"房源编号不能为空","房源编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row roomRow =roomService.findDetailById(id);
		String room_id =roomRow.getString("id",null);
		String code =roomRow.getString("code",null);
		String address =roomRow.getString("address",null);
		String status =roomRow.getString("status",null);
		String status_name =roomRow.getString("status_name",null);
		String pay_day =roomRow.getString("pay_day",null);
		String start_date =roomRow.getString("start_date",null);
		String end_date =roomRow.getString("end_date",null);
		String city_name =roomRow.getString("city_name",null);
		String zone_name =roomRow.getString("zone_name",null);
		String bank_card_no =roomRow.getString("bank_card_no",null);
		String bank_card_type =roomRow.getString("bank_card_type",null);
		String rent_id =roomRow.getString("rent_id",null);
		String rent_name =roomRow.getString("rent_name","");
		String rent_telephone =roomRow.getString("rent_telephone","");
		String area =roomRow.getString("area",null);
		String money =roomRow.getString("monthly_rent",null);
		String deposit =roomRow.getString("deposit",null);
		String invite_code =roomRow.getString("invite_code","");
		String electricity_num =roomRow.getString("electricity_num","");
		String water_num =roomRow.getString("water_num","");
		String gas_num =roomRow.getString("gas_num","");
		String property =roomRow.getString("property","");
		String remark =roomRow.getString("remark","");
		if(! StringUtils.isEmptyOrNull(rent_name))
		{
			rent_name =AesUtil.decode(rent_name);
		}
		//是否可删除 0不可删除 1可删除
		String can_delete ="0";
		//是否可终止委托 0不可终止委托 1 可以终止委托
		String can_stop ="0";
		if(StringUtils.isEmptyOrNull(status))
		{
			can_delete ="1";
			can_stop ="0";
		}
		else 
		{
			if(status.equals("0") )
			{
				can_delete = "1";
				can_stop ="0";
				status_name ="待租";
			}
			else if(status.equals("1"))
			{
				can_delete = "0";
				can_stop ="1";
				status_name ="签约中";
			}
			else if(status.equals("2") || status.equals("-1"))
			{
				can_delete = "0";
				can_stop ="1";
				status ="2";
				status_name ="出租中";
			}
			else if(status.equals("-2") || status.equals("-3"))
			{
				status="-2";
				status_name ="已结束";
			}
			else
			{
				can_delete ="1";
				can_stop ="0";
				status_name ="已删除";
			}
		}
		
		if(! StringUtils.isEmptyOrNull(bank_card_no))
		{
			bank_card_no =AesUtil.decode(bank_card_no);
		}
		ovo =new OVO(0,"操作成功","");
		ovo.set("room_id", room_id);
		ovo.set("code", code);
		ovo.set("address",address );
		ovo.set("status", status);
		ovo.set("status_name", status_name);
		ovo.set("pay_day", pay_day);
		ovo.set("start_date", start_date);
		ovo.set("end_date", end_date);
		ovo.set("city_name", city_name);
		ovo.set("zone_name", zone_name);
		ovo.set("bank_card_no", bank_card_no);
		ovo.set("bank_card_type", bank_card_type);
		ovo.set("rent_id", rent_id);
		ovo.set("rent_name", rent_name);
		ovo.set("rent_telephone", rent_telephone);
		ovo.set("area", area);
		ovo.set("money", money);
		ovo.set("deposit", deposit);
		ovo.set("can_delete", can_delete);
		ovo.set("can_stop", can_stop);
		ovo.set("invite_code", invite_code);
		ovo.set("electricity_num", electricity_num);
		ovo.set("water_num", water_num);
		ovo.set("gas_num", gas_num);
		ovo.set("property", property);
		ovo.set("remark", remark);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 房东删除待出租房源
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/delete")
	public @ResponseBody
	String deleteRoom(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		//房源ID
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"房源编号不能为空","房源编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row roomRow =roomService.findById(id);
		String status =roomRow.getString("status",null);
		if(status.equals("0") || status.equals("1"))
		{
			//删除预生成每月缴费记录
			renterMonthlyMoneyService.deleteByRoomId(id);
			//将房源的状态设置为-4/删除状态
			roomService.delete(id);
			ovo =new OVO(0,"操作成功","");
		}
		else if(status.equals("2") || status.equals("-1"))
		{
			ovo =new OVO(-1,"非待租状态的房源不能删除","非待租状态的房源不能删除");
		}
		else if(status.equals("-2") || status.equals("-3"))
		{
			//将房源状态设置为删除状态-4
			roomService.deleteForHidden(id);
		}
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 房东申请终止委托
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/stop")
	public @ResponseBody
	String stopRentRoom(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		//房源ID
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"房源编号不能为空","房源编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
//		String code =ivo.getString("code",null);
		String stop_date =ivo.getString("date",null);
		//检验此房源的状态是否是正在出租状态
		Row roomRow =roomService.findDetailById(id);
		String status =roomRow.getString("status",null);
		if(!status.equals("2") && !status.equals("4"))
		{
			ovo =new OVO(-1,"非出租状态的房源不能终止委托","非出租状态的房源不能终止委托");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(! DateUtil.checkDateFormatYYYY_MM_DD(stop_date))
		{
			ovo =new OVO(-1,"时间格式不合法，yyyy-mm-dd","时间格式不合法，yyyy-mm-dd");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//检验终止时间是否在租房的开始日期与结束日期之内
		String start_date =roomRow.getString("start_date");
		String end_date =roomRow.getString("end_date");
		long start =DateUtil.compare(stop_date+" 00:00:00", start_date+" 00:00:00");
		long end =DateUtil.compare(stop_date+" 00:00:00", end_date+" 00:00:00");
		if( start <0 || end >0)
		{
			ovo =new OVO(-1,"申请终止委托时间不在租期之内","申请终止委托时间不在租期之内");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String city =roomRow.getString("city_name","");
		String zone =roomRow.getString("zone_name","");
		String address =roomRow.getString("address","");
		String deposit =roomRow.getString("deposit","");
		String rent_name =roomRow.getString("rent_name","");
		String rent_id =roomRow.getString("rent_id","");
		String room_code =roomRow.getString("code","");
		Row row =new Row();
		row.put("id", id);
		row.put("status", "-2");//待租/新建/0、签约中/1、出租中/2、结束/-2、续租/-1、续租结束/-3、删除    /-4
		row.put("request_end_date", stop_date);
		roomService.update(row);
		//新建一条待租房源
		Row bakRow =new Row();
		bakRow =roomService.findById(id);
		bakRow =MapUtils.convertMaptoRowWithoutNullField(bakRow);
		bakRow.put("id","");
		bakRow.put("code","");
		bakRow.put("start_date","");
		bakRow.put("end_date","");
		bakRow.put("pay_day","");
		bakRow.put("monthly_rent","0");
		bakRow.put("deposit","0");
		bakRow.put("request_end_date", "");
		bakRow.put("invite_code", "");
		bakRow.put("status", "0");
		bakRow.put("electricity_num", "0");
		bakRow.put("water_num", "0");
		bakRow.put("gas_num", "0");
		bakRow.put("property", "0");
		bakRow.put("remark", "");
		roomService.insert(bakRow);
		//新建终止委托申请记录备份
		Row entrustRow =new Row();
		entrustRow.put("user_id", rent_id);
		entrustRow.put("room_code", room_code);
		entrustRow.put("req_end_date", stop_date);
		entrustRow.put("status","0");
		stopEntrustRoomService.insert(entrustRow);
		
		Row rentRow =new Row();
		rentRow =rentService.findByCode(room_code);
		String rent_rent_id =rentRow.getString("id","");
		rentRow =new Row();
		rentRow.put("id", rent_rent_id);
		rentRow.put("status","3" );//终止
		rentService.update(rentRow);
		
		ovo =new OVO(0,"操作成功","");
		String html ="";
		html +="申请成功";
		html +="您申请终止@@CITY@@ZONE@@ADDRESS房源房租服务，系统已受理。";
//		html +="该房源房租押金@@DEPOSIT元人民币将于3个工作日内转至您的银行账户，请注意查收。";
		html +="房租宝将短信通知租客@@RENT_NAME。";
		html +="截止您终止委托服务时间，房租宝已代您收房租@@NUM个月，尚余@@LEFT_NUM个月房租未收。";
		html =html.replace("@@CITY", city);
		html =html.replace("@@ZONE", zone);
		html =html.replace("@@ADDRESS", address);
		html =html.replace("@@DEPOSIT", deposit);
		if(!StringUtils.isEmptyOrNull(rent_name))
		{
			rent_name =AesUtil.decode(rent_name);
		}
		html =html.replace("@@RENT_NAME", rent_name);
		Row monthRow =renterMonthlyMoneyService.getPayStatusByRoomId(id);
		String already_payed_month_num =monthRow.getString("num2","0");
		String not_payed_month_num =monthRow.getString("num1","0");
		html =html.replace("@@NUM", already_payed_month_num);
		html =html.replace("@@LEFT_NUM", not_payed_month_num);
		ovo.set("html", html);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	
	/**
	 * 房东重新放租
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/re_rent")
	public @ResponseBody
	String reRent(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		//房源ID
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"房源编号不能为空","房源编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row roomRow =roomService.findById(id);
		if(roomRow == null)
		{
			ovo =new OVO(-1,"房源不存在","房源不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String status =roomRow.getString("status","");
		String city =roomRow.getString("city");
		String region =roomRow.getString("region");
		String address =roomRow.getString("address");
		String area =roomRow.getString("area");
		String start_date =roomRow.getString("start_date");
		String end_date =roomRow.getString("end_date");
		String pay_day =roomRow.getString("pay_day");
		String money =roomRow.getString("monthly_rent");
		String deposit =roomRow.getString("deposit");
		//0未出租，房东未确认 1未租出，房东已确认出租 2已租出，双方已签署协议 3托管正常结束 4续租 5申请终止委托成功
		if(status.equals("0") || status.equals("1") || status.equals("3") || status.equals("5"))
		{
			ovo =new OVO(0,"","");
			ovo.set("city", city);
			ovo.set("region", region);
			ovo.set("address", address);
			ovo.set("area", area);
			ovo.set("start_date", start_date);
			ovo.set("end_date", end_date);
			ovo.set("pay_day", pay_day);
			ovo.set("money", money);
			ovo.set("deposit", deposit);
		}
		else
		{
			ovo =new OVO(-1,"此房源正处于出租状态，不能重新放租","此房源正处于出租状态，不能重新放租");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
}
