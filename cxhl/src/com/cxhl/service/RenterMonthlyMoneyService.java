package com.cxhl.service;

import java.text.ParseException;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.DateUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2015-1-1 下午4:24:59  
 * 类说明:租金
 */
@Component("fzbRentMonthlyMoneyService")
public class RenterMonthlyMoneyService extends Service {

	public RenterMonthlyMoneyService() 
	{
		
	}

	public int insert(Row row) throws JException
	{
		int rowNum =0;
		String time =DateUtil.getCurrentDateTime();
//		String year =time.substring(0,4);
//		String month =time.substring(5,7);
//		String day =time.substring(8,10);
		int id =getTableSequence("rent_renter_monthly_money", "id", 1);
		row.put("id", id);
		row.put("create_time", time);
		rowNum =insert("rent_renter_monthly_money", row);
		return rowNum;
	}
	
	public Row find(String room_id, String start_date, String end_date) throws JException
	{
		Row row =null;
		sql ="select * from rent_renter_monthly_money where start_time='"+start_date+"' and end_time='"+end_date+"' and room_id='"+room_id+"';";
		row =queryRow(sql);
		return row;
	}
	
	public Row find(String id)  
	{
		Row row =null;
		sql ="select * from rent_renter_monthly_money where id='"+id+"' ";
		row =queryRow(sql);
		return row;
	}
	
	public int update(Row row)
	{
		int rowNum =0;
		String id=row.getString("id");
		row.put("modify_time", DateUtil.getCurrentDateTime());
		rowNum =update("rent_renter_monthly_money", row, " id='"+id+"'");
		return rowNum;
	}
	
	public int isPayDeposit(String user_id,String room_id,String money_type) throws JException
	{
		int rowNum =0;
		String sql ="select count(*) as num from rent_renter_monthly_money " +
				"where renter_id='"+user_id+"' and room_id='"+room_id+"' " +
				"and money_type='"+money_type+"'";
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
	public boolean havePayedMonthRentOrNot(String room_id,String start_date, String end_date, String appointed_date) throws JException, ParseException
	{
		boolean bool =false;
		Row timeRow =calculateRentPeriodByAppointedDate(start_date,end_date,appointed_date);
		String period_start_date =timeRow.getString("period_start_date");
		String period_end_date =timeRow.getString("period_end_date");
		int rowNum =0;
		start_date +=" 00:00:00";
		end_date +=" 00:00:00";
		String sql ="select count(*) as num from rent_renter_monthly_money " +
				"where room_id='"+room_id+"' " +
				"and money_type='1' and start_time='"+period_start_date+"' " +
				"and end_time='"+period_end_date+"' and pay_status in ('1','2','3','4','5') ";
		System.out.println("sql---->>"+sql);
		rowNum =Integer.parseInt(queryField(sql));
		if(rowNum >0){
			bool =true;
		}
		return bool;
	}
	
	/**
	 * 根据日期查询这个时间段的交房租状态
	 * @param user_id
	 * @param room_id
	 * @param date 日期
	 * @return
	 * @throws JException
	 * @throws ParseException 
	 */
	public String queryMonthRentStatus(String room_id,String start_date, String end_date, String appointed_date) throws JException, ParseException
	{
		String status =null;
		Row timeRow =calculateRentPeriodByAppointedDate(start_date,end_date,appointed_date);
		String period_start_date =timeRow.getString("period_start_date");
		String period_end_date =timeRow.getString("period_end_date");
		start_date +=" 00:00:00";
		end_date +=" 00:00:00";
		String sql ="select pay_status from rent_renter_monthly_money " +
				"where room_id='"+room_id+"' " +
				"and start_time='"+period_start_date+"' " +
				"and end_time='"+period_end_date+"' ";
		status =queryField(sql);
		return status;
	}
	
	/**
	 * 根据当前日期计算还有多少天到交租日
	 * @param user_id
	 * @param room_id
	 * @param date 日期
	 * @return
	 * @throws JException
	 * @throws ParseException 
	 */
	public long calculateDaysFromNowdayToRentday(String room_id,String start_date, 
			String end_date, String appointed_date,String pay_day) throws JException, ParseException
	{
		long days=0;
		Row timeRow =calculateRentPeriodByAppointedDate(start_date,end_date,appointed_date);
		String period_end_date =timeRow.getString("period_end_date");
		String period_start_date =timeRow.getString("period_start_date");
		String end_month =period_end_date.substring(0,7);
		String start_month =period_start_date.substring(0,7);
		String end_month_pay_day =end_month+"-"+pay_day+" 00:00:00";
		String start_month_pay_day =start_month+"-"+pay_day+" 00:00:00";
		String now_day =appointed_date+" 00:00:00";
		System.out.println("now_day--->>"+now_day);
		System.out.println("start_month_pay_day--->>"+start_month_pay_day);
		long temp_minus =DateUtil.getDayMinusOfTwoTime(now_day, start_month_pay_day);
		if(temp_minus >=0)
		{
			period_end_date =start_month_pay_day;
		}
		else
		{
			period_end_date =end_month_pay_day;
		}
		if(period_end_date !=null && period_end_date.length() <=10)
		{
			period_end_date +=" 00:00:00";
		}
		System.out.println("now_day---------------->>>"+now_day);
		System.out.println("period_end_date-------->>>"+period_end_date);
		days =DateUtil.getDayMinusOfTwoTime(now_day, period_end_date);
		return days;
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
//			System.out.println("row ======>>"+row);
			return row;
		}
		
		do
		{
			period_start_date =period_end_date;
			period_end_date =DateUtils.getStrOfNextMonth(period_start_date);
			start_com =DateUtil.compare(appointed_date+" 00:00:00", period_start_date+" 00:00:00");
			end_com =DateUtil.compare(appointed_date+" 00:00:00", period_end_date+" 00:00:00");
//			System.out.println("start_com------------->>"+start_com);
//			System.out.println("end_com--------------->>"+end_com);
			if(start_com >= 0 && end_com <=0)
			{
				boolFind =false;
			}
		}
		while(boolFind);
//		System.out.println("period_start_date------------->>"+period_start_date);
//		System.out.println("period_end_date--------------->>"+period_end_date);
		row.put("period_start_date", period_start_date);
		row.put("period_end_date", period_end_date);
//		System.out.println("row ======>>"+row);
		return row;
	}
	public void delete(String... ids) {
		String id = "";
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				if (id.length() > 0) {
					id += ",";
				}
				id += "'" + String.valueOf(ids[i]) + "'";
			}
			sql = "delete from rent_renter_monthly_money where id in(" + id + ")";
			update(sql);
		}
	}
	
	public void deleteByRoomId(String... ids) {
		String id = "";
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				if (id.length() > 0) {
					id += ",";
				}
				id += "'" + String.valueOf(ids[i]) + "'";
			}
			sql = "delete from rent_renter_monthly_money where room_id in(" + id + ")";
			update(sql);
		}
	}
	
	/**
	 * 计算某个房源的缴租情况，缴了多少个月，还剩多少个月没缴
	 * @param room_id
	 * @return
	 */
	public Row getPayStatusByRoomId(String room_id)
	{
		Row row =new Row();
		sql ="select * from ( "
		+" select count(*) as num from rent_renter_monthly_money where room_id ='"+room_id+"' and pay_status='0' "
		+" UNION "
		+" select count(*) as num from rent_renter_monthly_money where room_id ='"+room_id+"' and pay_status='1' "
		+" ) as tab ";
		DataSet ds =queryDataSet(sql);
		Row temp =(Row)ds.get(0);
		row.put("num1", temp.getString("num"));//未支付月租的月数
		temp =(Row)ds.get(1);
		row.put("num2", temp.getString("num"));//已支付月租的月数
		return row;
	}
	
	/**
	 * 分页查询
	 * @param room_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DataSet findPage(String room_id, String user_id, int page, int page_size)
	{
		DataSet ds =null;
		int start =(page-1)*page_size;
		sql ="select a.id,a.renter_id,a.room_id,a.start_time,a.end_time,a.money,"
				+"a.real_money,a.pay_status,a.from_account,a.to_account,"
				+"a.modify_time ,b.name "
				+"from rent_renter_monthly_money a "
				+"left join rent_users b on a.renter_id=b.id "
				+"where a.room_id='"+room_id+"' " ;
		if(! StringUtils.isEmptyOrNull(user_id))
		{
			sql +=" and a.renter_id='"+user_id+"' ";
		}
		sql += " limit "+start+", "+page_size;
		ds =queryDataSet(sql);
		String real_money ="";
		String name ="";
		String status ="";
		String status_name ="";
		for(int i=0;i<ds.size();i++)
		{
			Row tempRow =(Row)ds.get(i);
			real_money =tempRow.getString("real_money","");
			name =tempRow.getString("name","");
			status =tempRow.getString("pay_status","");
			if(StringUtils.isEmptyOrNull(real_money))
			{
				real_money ="0.00";
				tempRow.put("real_money",real_money );
			}
			if(!StringUtils.isEmptyOrNull(name))
			{
				try {
					name =AesUtil.decode(name);
				} catch (Exception e) {
					name ="";
				}
				tempRow.put("name",name );
			}
			if(status.equals("0"))
			{
				status_name ="未交租";
			}
			else if(status.equals("1"))
			{
				status_name ="已交租";
			}
			else if(status.equals("2"))
			{
				status_name ="已交租";
			}
			else if(status.equals("3"))
			{
				status_name ="已交租";
			}
			else if(status.equals("4"))
			{
				status_name ="已交租";
			}
			else if(status.equals("5"))
			{
				status_name ="已交租";
			}
			tempRow.put("status_name",status_name );
			ds.set(i, tempRow);
		}
		return ds;
	}
	
	
	public static void main(String[] args) {
		RenterMonthlyMoneyService rp =new RenterMonthlyMoneyService();
		try {
			rp.calculateRentPeriodByAppointedDate("2014-12-02","2015-12-02","2016-01-29");
//			String week=DateUtil.getWeekSequence("2015-01-09");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	/***************************管理后台*********************************/
	/**
	 * 分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public Page queryPage(String pay_status,Pageable pageable) throws Exception {
		Page page = null;
		sql = "select * from "
		+" (  "
		+" select a.*,b.code,b.address ,c.`name` as city_name ,cp.name as province_name, " 
		+" d.`name` as region_name,e.`name` as rent_name,e.telephone as rent_telephone, "
		+" f.`name` as landlord_name,f.telephone as landlord_telephone "
		+" from rent_renter_monthly_money a  "
		+" left join rent_room b on a.room_id=b.id " 
		+" left join common_city c on b.city=c.id  "
		+" left join common_province cp on b.province=cp.id  "
		+" left join common_city_zone d on b.region=d.id " 
		+" left join rent_users e on a.renter_id =e.id  "
		+" left join rent_users f on b.landlord_id =f.id  "
		+" where  b.status in ('2','-1','-2','-3') "
		+" order by a.room_id ,a.start_time    "
		+" ) as tab  where 1=1  ";
		if(! StringUtils.isEmptyOrNull(pay_status))
		{
			sql +=" and pay_status='"+pay_status+"' ";
		}
		//对租客姓名搜索字段做加密处理
		String search_pro =pageable.getSearchProperty();
		String search_value ="";
		if(! StringUtils.isEmptyOrNull(search_pro))
		{
			search_value =pageable.getSearchValue();
			if(search_pro.equalsIgnoreCase("RENT_NAME"))
			{
				if(! StringUtils.isEmptyOrNull(search_value))
				{
					search_value =AesUtil.encode(search_value);
					pageable.setSearchValue(search_value);
				}
			}
		}
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from "
				+" (  "
				+" select a.*,b.code,b.address ,c.`name` as city_name , " 
				+" d.`name` as region_name,e.`name` as rent_name,e.telephone as rent_telephone, "
				+" f.`name` as landlord_name,f.telephone as landlord_telephone "
				+" from rent_renter_monthly_money a  "
				+" left join rent_room b on a.room_id=b.id "
				+" left join common_city c on b.city=c.id  "
				+" left join common_city_zone d on b.region=d.id " 
				+" left join rent_users e on a.renter_id =e.id  "
				+" left join rent_users f on b.landlord_id =f.id  "
				+" where  b.status in ('2','-1','-2','-3') "
				+" order by a.room_id ,a.start_time    "
				 +" ) as tab  where 1=1  ";
		if(! StringUtils.isEmptyOrNull(pay_status))
		{
			countSql +=" and pay_status='"+pay_status+"' ";
		}
		countSql += restrictions;
		countSql += orders;
		long total = count(countSql);
		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}
		int startPos = (pageable.getPageNumber() - 1) * pageable.getPageSize();
		sql += " limit " + startPos + " , " + pageable.getPageSize();
		dataSet = queryDataSet(sql);
		if(dataSet != null && dataSet.size()>0)
		{
			for(int i=0;i<dataSet.size(); i++)
			{
				Row temp =(Row)dataSet.get(i);
				String rent_name =temp.getString("rent_name","");
				String landlord_name =temp.getString("landlord_name","");
				try {
					if(! StringUtils.isEmptyOrNull(rent_name))
					{
						rent_name =AesUtil.decode(rent_name);
					}
					if(! StringUtils.isEmptyOrNull(landlord_name))
					{
						landlord_name =AesUtil.decode(landlord_name);
					}
				} catch (Exception e) {
					rent_name ="";
					landlord_name ="";
				}
				temp.put("rent_name", rent_name);
				temp.put("landlord_name", landlord_name);
				dataSet.set(i, temp);
			}
		}
		//对租客姓名搜索字段做解密处理
		search_pro =pageable.getSearchProperty();
		search_value ="";
		if(! StringUtils.isEmptyOrNull(search_pro))
		{
			search_value =pageable.getSearchValue();
			if(search_pro.equalsIgnoreCase("RENT_NAME"))
			{
				if(! StringUtils.isEmptyOrNull(search_value))
				{
					search_value =AesUtil.decode(search_value);
					pageable.setSearchValue(search_value);
				}
			}
		}
		page = new Page(dataSet, total, pageable);
		return page;
	}
	
	public Row findById(String id)
	{
		Row row =null;
		String sql = "select * from "
				+" (  "
				+" select a.*,b.code,b.address ,cp.name as province_name, c.`name` as city_name, " 
				+" d.`name` as region_name,e.`name` as rent_name,e.telephone as rent_telephone, "
				+" f.`name` as landlord_name,f.telephone as landlord_telephone "
				+" from rent_renter_monthly_money a "
				+" left join rent_room b on a.room_id=b.id "
				+" left join common_province cp on b.province=cp.id "
				+" left join common_city c on b.city=c.id "
				+" left join common_city_zone d on b.region=d.id " 
				+" left join rent_users e on a.renter_id =e.id "
				+" left join rent_users f on b.landlord_id =f.id "
				+" order by a.room_id ,a.start_time "
				+" ) as tab  where id='"+id+"' ";
		row =queryRow(sql);
		return row;
	}
	
	public void updateRenterUserField(String room_id,String renter_id)
	{
		String sql ="update rent_renter_monthly_money set renter_id='"+renter_id+"' where room_id='"+room_id+"' ";
		update(sql);
	}
	
	/**
	 * 获取订单后4位
	 * @param cur_time
	 * @return
	 */
    public String getOrderLastFourNo(String cur_time)
    {
    	String no ="";
    	String sql ="select count(*) from rent_renter_monthly_money where create_time='"+cur_time+"'";
    	int num =Integer.parseInt(queryField(sql));
    	num++;
    	no =String.valueOf(num);
    	if(no.length()<4)
    	{
    		if(no.length() ==1)
    		{
    			no ="000"+no;
    		}
    		else if(no.length() ==2)
    		{
    			no ="00"+no;
    		}
    		else if(no.length() ==3)
    		{
    			no ="0"+no;
    		}
    	}
    	return no;
    }
    
    public void updateOrderPayConfirmStatus(String order_no,String status )
    {
    	String sql ="update rent_renter_monthly_money set order_pay_confirm='"+status+"' where order_no='"+order_no+"' ";
    	update(sql);
    }
    
    public void updateOrderNotPay(String order_no)
    {
    	String sql ="update rent_renter_monthly_money set pay_status='0' where order_no='"+order_no+"' ";
    	update(sql);
    }
    
    public void modifyOrderStateAfterBankInterfaceNotify(String order_no,String amount,String order_pay_confirm,String pay_status)
    {
    	String sql ="update rent_renter_monthly_money set pay_status='"+pay_status+"',order_pay_confirm='"+order_pay_confirm+"',real_money='"+amount+"' where order_no='"+order_no+"' ";
    	System.out.println("sql---->>"+sql);
    	update(sql);
    }
    
    public void updateOrderPaySuccess(String order_no,String status)
    {
    	String sql ="update rent_renter_monthly_money set pay_status='"+status+"' where order_no='"+order_no+"' ";
    	update(sql);
    }
	/***************************管理后台*********************************/
}