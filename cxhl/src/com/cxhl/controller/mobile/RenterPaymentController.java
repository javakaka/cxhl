package com.cxhl.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.RenterDepositService;
import com.cxhl.service.RenterMonthlyMoneyService;
import com.cxhl.service.RoomService;
import com.cxhl.service.UserService;
import com.ezcloud.framework.service.system.SystemConfigService;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.DateUtil;

@Controller("mobileRentPaymentController")
@RequestMapping("/api/core/renterpay")
/**
 * 
 * @author Administrator
 * 租客缴押金业务处理
 */
public class RenterPaymentController extends BaseController {
	
	private static Logger logger = Logger.getLogger(RenterPaymentController.class); 
	@Resource(name = "fzbRoomService")
	private RoomService roomService;
	
	@Resource(name = "fzbUserService")
	private UserService userService;
	
	@Resource(name = "frameworkSystemConfigService")
	private SystemConfigService systemConfigService;
	
	@Resource(name = "fzbRentDepositService")
	private RenterDepositService paymentService;
	
	@Resource(name = "fzbRentMonthlyMoneyService")
	private RenterMonthlyMoneyService monthlyMoneyService;
	
	/**
	 * 租客缴押金
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/pay_deposit")
	public @ResponseBody
	String payDeposit(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("租客缴押金");
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-1,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String room_id =ivo.getString("room_id",null);
		if(StringUtils.isEmptyOrNull(room_id))
		{
			ovo =new OVO(-1,"房源编号不能为空","房源编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String money =ivo.getString("money",null);
		if(StringUtils.isEmptyOrNull(money))
		{
			ovo =new OVO(-1,"押金不能为空","押金不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(! NumberUtils.isPositiveNumber(money))
		{
			ovo =new OVO(-1,"押金必须是数字","押金必须是数字");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//1租金2押金
		String money_type ="2";
		Row userRow =userService.find(user_id);
		Row roomRow =roomService.findById(room_id);
		if(userRow == null )
		{
			ovo =new OVO(-1,"用户不存在","用户不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(roomRow == null )
		{
			ovo =new OVO(-1,"房源不存在","房源不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String from_account =userRow.getString("credit_card_no","");
		if(StringUtils.isEmptyOrNull(from_account))
		{
			ovo =new OVO(-1,"付款账号为空","付款账号为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String from_account_type =userRow.getString("credit_card_type","");
		String to_account =systemConfigService.querySingleConfig("APP_COMPANY_BANK_ACCOUNT", "BANK_NO");
		if(StringUtils.isEmptyOrNull(to_account))
		{
			ovo =new OVO(-1,"系统未设置收款账号","系统未设置收款账号");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String to_account_type =systemConfigService.querySingleConfig("APP_COMPANY_BANK_ACCOUNT", "BANK_NO");

		int existed =paymentService.isPayDeposit(user_id, room_id);
		if(existed >=1)
		{
			ovo =new OVO(-1,"押金已交，不需要重复交押金","押金已交，不需要重复交押金");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row payRow =new Row();
		payRow.put("renter_id", user_id);
		payRow.put("room_id", room_id);
		payRow.put("money", money);
		payRow.put("money_type", money_type);
		payRow.put("from_account", from_account);
		payRow.put("from_account_type", from_account_type);
		payRow.put("to_account", to_account);
		payRow.put("to_account_type", to_account_type);
		
		int rowNum =paymentService.insert(payRow);
		if(rowNum <=0)
		{
			ovo =new OVO(-1,"保存转账信息出错","保存转账信息出错");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		roomRow.put("deposit_status", "1"); 
		roomService.update(roomRow);
		ovo =new OVO(0,"操作成功","");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

	/**
	 * 租客缴月租（一次只缴一个月）
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/pay_rent")
	public @ResponseBody
	String payMonthlyRent(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("租客缴月租（一次只缴一个月）,可以合并交租");
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-1,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String room_id =ivo.getString("room_id",null);
		if(StringUtils.isEmptyOrNull(room_id))
		{
			ovo =new OVO(-1,"房源编号不能为空","房源编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String money =ivo.getString("money",null);
		if(StringUtils.isEmptyOrNull(money))
		{
			ovo =new OVO(-1,"租金不能为空","租金不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(! NumberUtils.isPositiveNumber(money))
		{
			ovo =new OVO(-1,"租金必须是数字","租金必须是数字");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//判断是否是合并交房租
		String room_id_arr[] =room_id.split(",");
		if(room_id_arr !=null && room_id_arr.length >1)//跳转到合并交租
		{
			
		}
		//收租方式  1租客通过app已交租; 2先下交租;3押金抵扣;
		String pay_type =ivo.getString("pay_type",null);
		if(StringUtils.isEmptyOrNull(pay_type))
		{
			ovo =new OVO(-1,"收租方式参数不能为空，pay_type：1租客通过app已交租; 2先下交租;3押金抵扣","");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//房源交租对应的月份记录的编号
		String record_id =ivo.getString("record_id",null);
		if(StringUtils.isEmptyOrNull(record_id))
		{
			ovo =new OVO(-1,"月份记录id不能为空","月份记录id不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		boolean isPayTypeValid =false;
		if(pay_type.equals("1") || pay_type.equals("2") || pay_type.equals("3"))
		{
			isPayTypeValid =true;
		}
		if(!isPayTypeValid)
		{
			ovo =new OVO(-1,"收租方式参数错误，pay_type：1租客通过app已交租; 2先下交租;3押金抵扣","");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//1租金
		String money_type ="1";
		Row userRow =userService.find(user_id);
		Row roomRow =roomService.findById(room_id);
		if(userRow == null )
		{
			ovo =new OVO(-1,"用户不存在","用户不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(roomRow == null )
		{
			ovo =new OVO(-1,"房源不存在","房源不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String from_account =userRow.getString("credit_card_no","");
		if(StringUtils.isEmptyOrNull(from_account))
		{
			ovo =new OVO(-1,"付款账号为空","付款账号为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		from_account =AesUtil.decode(from_account);
		String from_account_type =userRow.getString("credit_card_type","");
		String to_account =systemConfigService.querySingleConfig("APP_COMPANY_BANK_ACCOUNT", "BANK_NO");
		if(StringUtils.isEmptyOrNull(to_account))
		{
			ovo =new OVO(-1,"系统未设置收款账号","系统未设置收款账号");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		/*
		String start_date =roomRow.getString("start_date","");
		String end_date =roomRow.getString("end_date","");
		String appointed_date =DateUtil.getCurrentDate();
		//找出本月对应的缴费记录
		Row periodRow =paymentService.calculateRentPeriodByAppointedDate(start_date, end_date, appointed_date);
		String p_start_date =periodRow.getString("period_start_date","");
		String p_end_date =periodRow.getString("period_end_date","");
		if(p_start_date.equals("-1") || p_end_date.equals("-1"))
		{
			ovo =new OVO(-1,"日期错误","日期错误");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row month_pay_row =monthlyMoneyService.find(room_id, p_start_date, p_end_date);
		*/
		Row month_pay_row =monthlyMoneyService.find(record_id);
		String now_time =DateUtil.getCurrentDateTime();
		String year =now_time.substring(0,4);
		String month =now_time.substring(5,7);
		String day =now_time.substring(8,10);
		month_pay_row.put("year", year);
		month_pay_row.put("month", month);
		month_pay_row.put("day", day);
//		month_pay_row.put("renter_id", user_id);
		month_pay_row.put("real_money", money);
		month_pay_row.put("pay_status", pay_type);//1租客通过app已交租; 2先下交租;3押金抵扣;
		String to_account_type =systemConfigService.querySingleConfig("APP_COMPANY_BANK_ACCOUNT", "BANK_TYPE");
		month_pay_row.put("money_type", money_type);
//		month_pay_row.put("from_account", from_account);
//		month_pay_row.put("from_account_type", from_account_type);
		month_pay_row.put("to_account", to_account);
		month_pay_row.put("to_account_type", to_account_type);
		int rowNum =monthlyMoneyService.update(month_pay_row);
		if(rowNum <=0)
		{
			ovo =new OVO(-1,"保存转账信息出错","保存转账信息出错");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		ovo =new OVO(0,"操作成功","");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 房东根据房源编号查询缴租流水记录
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/charge_history")
	public @ResponseBody
	String queryRoomChargeHistory(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("根据房源编号查询缴租流水记录");
		String room_id =ivo.getString("room_id",null);
		if(StringUtils.isEmptyOrNull(room_id))
		{
			ovo =new OVO(-1,"房源编号不能为空","房源编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-1,"用户编号不能为空","");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row userRow =userService.find(user_id);
		if(userRow == null)
		{
			ovo =new OVO(-1,"用户不存在，不能查询","用户不存在，不能查询");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet historyDs =monthlyMoneyService.findPage(room_id, null, Integer.parseInt(page), Integer.parseInt(page_size));
		ovo =new OVO(0,"查询成功","");
		ovo.set("list", historyDs);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 租客根据房源编号查询缴租流水记录
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/pay_history")
	public @ResponseBody
	String queryRoomPayHistory(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("根据房源编号查询缴租流水记录");
		String room_id =ivo.getString("room_id",null);
		if(StringUtils.isEmptyOrNull(room_id))
		{
			ovo =new OVO(-1,"房源编号不能为空","房源编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-1,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row userRow =userService.find(user_id);
		if(userRow == null)
		{
			ovo =new OVO(-1,"用户不存在，不能查询","用户不存在，不能查询");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet historyDs =monthlyMoneyService.findPage(room_id, user_id, Integer.parseInt(page), Integer.parseInt(page_size));
		ovo =new OVO(0,"查询成功","");
		ovo.set("list", historyDs);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
}
