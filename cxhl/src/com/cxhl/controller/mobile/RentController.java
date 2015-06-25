package com.cxhl.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.RentService;
import com.cxhl.service.RenterDepositService;
import com.cxhl.service.RenterMonthlyMoneyService;
import com.cxhl.service.RoomService;
import com.cxhl.service.UserService;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.DateUtil;

@Controller("mobileRentController")
@RequestMapping("/api/core/rent")
public class RentController extends BaseController {
	
	private static Logger logger = Logger.getLogger(RentController.class); 
	@Resource(name = "fzbRentService")
	private RentService rentService;
	
	@Resource(name = "fzbUserService")
	private UserService userService;
	
	@Resource(name = "fzbRentMonthlyMoneyService")
	private RenterMonthlyMoneyService renterMonthlyMoneyService;
	
	@Resource(name = "fzbRoomService")
	private RoomService roomService;
	
	@Resource(name = "fzbRentDepositService")
	private RenterDepositService paymentService;
	/**
	 * 查询租客的租房列表
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value ="/room_list")
	public @ResponseBody
	String queryPageRoom(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("分页查询租客的租房列表");
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-1,"","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet rent_list =rentService.list(user_id, Integer.parseInt(page), Integer.parseInt(page_size));
		String start_date ="";
		String room_id ="";
		String end_date ="";
		String cur_date =DateUtil.getCurrentDate();
		String month_pay_status ="0";//0未收1已收
		String room_status ="";
		String pay_day ="";
		//当前月份交租记录的id
		String cur_month_record_id ="";
		for(int i=0; i<rent_list.size(); i++)
		{
			cur_month_record_id ="";
			Row row =(Row)rent_list.get(i);
			room_id =row.getString("room_id","");
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
					//找出当前月份交租记录的id
					start_date =row.getString("start_date","");
					end_date =row.getString("end_date","");
					String appointed_date =DateUtil.getCurrentDate();
					//找出本月对应的缴费记录
					Row periodRow =paymentService.calculateRentPeriodByAppointedDate(start_date, end_date, appointed_date);
					String p_start_date =periodRow.getString("period_start_date","");
					String p_end_date =periodRow.getString("period_end_date","");
					Row month_pay_row =renterMonthlyMoneyService.find(room_id, p_start_date, p_end_date);
					if(month_pay_row != null)
					{
						cur_month_record_id =month_pay_row.getString("id","");
					}
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
			row.put("cur_month_record_id", cur_month_record_id);
			rent_list.set(i, row);
		}
		ovo =new OVO(0,"操作成功","");
		ovo.set("rent_list", rent_list);
		//第一页时，查询缴租账号
		Row userRow =null;
		String credit_card_no ="";
		String credit_card_type="";
		if(page.equals("1"))
		{
			userRow = userService.find(user_id);
			credit_card_no =userRow.getString("credit_card_no","");
			if(! StringUtils.isEmptyOrNull(credit_card_no))
			{
				credit_card_no =AesUtil.decode(credit_card_no);
			}
			credit_card_type =userRow.getString("credit_card_type","");
			ovo.set("credit_card_no", credit_card_no);
			ovo.set("credit_card_type", credit_card_type);
		}
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 租客查询的房源详情
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/room_detail")
	public @ResponseBody
	String queryRoomDetail(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("租客查询的房源详情");
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"租房记录编号不能为空","租房记录编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row rentRow = rentService.findDetailById(id);
		String code =rentRow.getString("code","");
		String status =rentRow.getString("status","");
		String status_name ="";
		String address =rentRow.getString("address","");
		String start_date =rentRow.getString("start_date","");
		String end_date =rentRow.getString("end_date","");
		String city_name =rentRow.getString("city_name","");
		String zone_name =rentRow.getString("zone_name","");
		String area =rentRow.getString("area","");
		String money =rentRow.getString("monthly_rent",null);
		String deposit =rentRow.getString("deposit",null);
		String invite_code =rentRow.getString("invite_code","");
		String pay_day =rentRow.getString("pay_day","");
		//查询房东信息：姓名、手机号
		String room_id =rentRow.getString("room_id","");
		Row roomRow =roomService.findById(room_id);
		String landlord_id =roomRow.getString("landlord_id","");
		Row userRow =userService.find(landlord_id);
		String userName =userRow.getString("name","");
		if(! StringUtils.isEmptyOrNull(userName))
		{
			userName =AesUtil.decode(userName);
		}
		String userTelephone =userRow.getString("telephone","");
		//查询房源信息：水、电、燃气、备注
		String electricity_num =roomRow.getString("electricity_num","");
		String water_num =roomRow.getString("water_num","");
		String gas_num =roomRow.getString("gas_num","");
		String property =roomRow.getString("property","");
		String remark =roomRow.getString("remark","");
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
			status_name ="承租中";
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
		ovo.set("city_name", city_name);
		ovo.set("zone_name", zone_name);
		ovo.set("area", area);
		ovo.set("money", money);
		ovo.set("deposit", deposit);
		ovo.set("invite_code", invite_code);
		ovo.set("pay_day", pay_day);
		ovo.set("landlord_name", userName);
		ovo.set("landlord_telephone", userTelephone);
		ovo.set("electricity_num", electricity_num);
		ovo.set("water_num", water_num);
		ovo.set("gas_num", gas_num);
		ovo.set("property", property);
		ovo.set("remark", remark);
		ovo.set("landlord_id", landlord_id);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 租客删除承租房源
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/delete")
	public @ResponseBody
	String deleteRoom(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("租客查询的房源详情");
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"租房记录编号不能为空","租房记录编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row rentRow = rentService.findDetailById(id);
		String status =rentRow.getString("status","");
		if(status.equals("2") || status.equals("4"))
		{
			ovo =new OVO(-1,"承租中的房源不能删除","承租中的房源不能删除");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String rent_id =rentRow.getString("id");
		Row rRow =new Row();
		rRow.put("id", rent_id);
		rRow.put("status", "5");
		int rowNum =rentService.update(rRow);
		if(rowNum <=0)
		{
			ovo =new OVO(-1,"操作失败","操作失败");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		ovo =new OVO(0,"操作成功","");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

}
