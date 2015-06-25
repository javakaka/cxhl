package com.cxhl.service;

import java.text.ParseException;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.DateUtils;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2015-1-1 下午4:24:59  
 * 类说明:押金
 */
@Component("fzbRentDepositService")
public class RenterDepositService extends Service {

	public RenterDepositService() 
	{
		
	}

	public int insert(Row row) throws JException
	{
		int rowNum =0;
		String time =DateUtil.getCurrentDateTime();
		String year =time.substring(0,4);
		String month =time.substring(5,7);
		String day =time.substring(8,10);
		row.put("year", year);
		row.put("month", month);
		row.put("day", day);
		int id =getTableSequence("rent_renter_deposit", "id", 1);
		row.put("id", id);
		row.put("create_time", time);
		rowNum =insert("rent_renter_deposit", row);
		return rowNum;
	}
	
	public int isPayDeposit(String room_id) throws JException
	{
		int rowNum =0;
		String sql ="select count(*) as num from rent_renter_deposit " +
				"where room_id='"+room_id+"' ";
		rowNum =Integer.parseInt(queryField(sql));
		return rowNum;
	}
	
	public int isPayDeposit(String user_id, String room_id) throws JException
	{
		int rowNum =0;
		String sql ="select count(*) as num from rent_renter_deposit " +
				"where room_id='"+room_id+"' and renter_id='"+user_id+"'";
		rowNum =Integer.parseInt(queryField(sql));
		return rowNum;
	}
	
	/**
	 * 根据日期查询用户是否已经缴了房租
	 * @param user_id
	 * @param room_id
	 * @param date 日期
	 * @return
	 * @throws JException
	 * @throws ParseException 
	 */
	public boolean havePayedMonthRentOrNot(String user_id,String room_id,String start_date, String end_date, String appointed_date) throws JException, ParseException
	{
		boolean bool =false;
//		Row timeRow =calculateRentPeriodByAppointedDate(start_date,end_date,appointed_date);
//		String period_start_date =timeRow.getString("period_start_date");
//		String period_end_date =timeRow.getString("period_end_date");
		int rowNum =0;
		start_date +=" 00:00:00";
		end_date +=" 00:00:00";
		String sql ="select count(*) as num from rent_renter_deposit " +
				"where renter_id='"+user_id+"' and room_id='"+room_id+"' " +
				"and money_type='1' and creat_time>='"+start_date+"' and create_time<'"+end_date+"'";
		rowNum =Integer.parseInt(queryField(sql));
		if(rowNum >0){
			bool =true;
		}
		return bool;
	}
	
	/**
	 * 根据房源的开始日期和结束日期，计算指定日期所属缴费月的时间段（开始日期、结束日期）
	 * @param start_date yyyy-MM-dd
	 * @param end_date yyyy-MM-dd
	 * @param appointed_date yyyy-MM-dd
	 * @return
	 * @throws ParseException 
	 */
	public Row calculateRentPeriodByAppointedDate(String start_date, String end_date, String appointed_date) throws ParseException
	{
		Row row =new Row();
		String period_start_date =start_date;
		String period_end_date =start_date;
		boolean boolFind =true;
		long start_com=0;
		long end_com=0;
		start_com =DateUtil.compare(appointed_date+" 00:00:00", start_date+" 00:00:00");
		end_com =DateUtil.compare(appointed_date+" 00:00:00", end_date+" 00:00:00");
		if(start_com < 0 || end_com>0)
		{
			row.put("period_start_date", "-1");
			row.put("period_end_date", "-1");
			System.out.println("row ======>>"+row);
			return row;
		}
		
		do
		{
			period_start_date =period_end_date;
			period_end_date =DateUtils.getStrOfNextMonth(period_start_date);
			start_com =DateUtil.compare(appointed_date+" 00:00:00", period_start_date+" 00:00:00");
			end_com =DateUtil.compare(appointed_date+" 00:00:00", period_end_date+" 00:00:00");
			System.out.println("start_com------------->>"+start_com);
			System.out.println("end_com--------------->>"+end_com);
			if(start_com >= 0 && end_com <=0)
			{
				boolFind =false;
			}
		}
		while(boolFind);
		System.out.println("period_start_date------------->>"+period_start_date);
		System.out.println("period_end_date--------------->>"+period_end_date);
		row.put("period_start_date", period_start_date);
		row.put("period_end_date", period_end_date);
		System.out.println("row ======>>"+row);
		return row;
	}
	
	
	public static void main(String[] args) {
		RenterDepositService rp =new RenterDepositService();
		try {
			rp.calculateRentPeriodByAppointedDate("2014-12-02","2015-12-02","2016-01-29");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}