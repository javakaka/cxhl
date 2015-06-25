package com.cxhl.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2015-1-1 下午4:24:59  
 * 类说明:房源业务处理类 
 */
@Component("fzbRoomService")
public class RoomService extends Service {

	public RoomService() 
	{
		
	}

	public OVO  addRoom(Row row) throws JException
	{
		OVO ovo =null;
		int id =getTableSequence("rent_room", "id", 1);
		row.put("id", id);
		int code =getTableSequence("rent_room", "code", 100000);
		row.put("code", code);
		row.put("create_time", DateUtil.getCurrentDateTime());
		int rowNum =insert("rent_room", row);
		if(rowNum >0)
		{
			ovo =new OVO(0,"操作成功","");
			ovo.set("id", id);
			ovo.set("code", code);
		}
		else
		{
			ovo =new OVO(-10020,"操作失败","操作失败");
		}
		return ovo;
	}
	
	public Row roomService(String id)
	{
		Row row =null;
		sql ="select * from rent_room where id='"+id+"'";
		row =queryRow(sql);
		return row;
	}
	
	public Row findByCode(String code)
	{
		Row row =null;
		sql ="select * from rent_room where code='"+code+"'";
		row =queryRow(sql);
		return row;
	}
	public Row findById(String id)
	{
		Row row =null;
		sql ="select * from rent_room where id='"+id+"'";
		row =queryRow(sql);
		return row;
	}
	
	public int findTotalNum()
	{
		int num =0;
		String sSql =" select count(*) from rent_room where status in ('0','1','2','-1','-2','-3') ";
		num =Integer.parseInt(queryField(sSql));
		return num;
	}
	
	public int insert(Row row)
	{
		int rowNum =0;
		int id=getTableSequence("rent_room", "id", 1);
		row.put("id", id);
		int code =getTableSequence("rent_room", "code", 100000);
		row.put("code", code);
		row.put("create_time", DateUtil.getCurrentDateTime());
		rowNum = insert("rent_room", row);
		return rowNum;
	}
	
	public int update(Row row)
	{
		int rowNum =0;
		String id=row.getString("id");
		row.put("modify_time", DateUtil.getCurrentDateTime());
		rowNum = update("rent_room", row, " id='"+id+"'");
		return rowNum;
	}
	
	/**
	 * 房东查询出租房源
	 * @param user_id
	 * @param page
	 * @param page_size
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DataSet list(String user_id,String page,String page_size)
	{
		DataSet ds =new DataSet();
		int start =(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		String sql ="select a.id,a.code,a.address,a.status," +
				"a.deposit_status,a.start_date,a.end_date,a.pay_day,b.name as city_name," +
				"c.name as zone_name " +
				"from rent_room a left join common_city b " +
				"on a.city=b.id left join common_city_zone c on a.region=c.id  " +
				"where a.landlord_id='"+user_id+"' and a.status in ('0','1','2','-1')" +
				"order by a.status desc "+
				"limit "+start+","+page_size;
		ds =queryDataSet(sql);
		//是否可删除 0不可删除 1可删除
		String can_delete ="0";
		//是否可终止委托 0不可终止委托 1 可以终止委托
		String can_stop ="0";
		String status ="";
		String deposit_status ="";
		String status_name ="";
		for(int i=0; i<ds.size(); i++)
		{
			Row row =(Row)ds.get(i);
			status =row.getString("status","");
			deposit_status =row.getString("deposit_status",null);
			if(StringUtils.isEmptyOrNull(status))
			{
				can_delete ="1";
				can_stop ="0";
			}
			else 
			{
				if( status.equals("0") )
				{
					can_delete = "1";
					can_stop ="0";
					status_name ="待租";
				}
				else if(status.equals("1"))
				{
					can_delete = "0";
					can_stop ="0";
					status_name ="签约中";
				}
				else if(status.equals("2") || status.equals("-1"))
				{
					status ="2";
					can_delete = "0";
					can_stop ="1";
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
			row.put("status", status);
			row.put("can_delete", can_delete);
			row.put("can_stop", can_stop);
			row.put("status_name", status_name);
			//查询是否已交押金
			//押金
			if(StringUtils.isEmptyOrNull(deposit_status))
			{
				deposit_status ="0";
			}
			row.put("deposit_status", deposit_status);
			ds.set(i, row);
		}
		return ds;
	}
	
	/**
	 * 房东查询自己的出租房源列表-出租中、签约中和已结束状态的房源
	 * @param user_id
	 * @param page
	 * @param page_size
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DataSet using_list(String user_id,String page,String page_size)
	{
		DataSet ds =new DataSet();
		int start =(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		String sql ="select a.id,a.code,a.address,a.status,a.deposit_status,a.start_date,a.end_date,b.name as city_name," +
				"c.name as zone_name " +
				"from rent_room a left join common_city b " +
				"on a.city=b.id left join common_city_zone c on a.region=c.id  " +
				"where a.landlord_id='"+user_id+"' and a.status in ('2','-1','-2','-3')  " +
				"order by a.status desc " +
				"limit "+start+","+page_size;
		ds =queryDataSet(sql);
		String status ="";
		String deposit_status ="";
		String status_name ="";
		for(int i=0; i<ds.size(); i++)
		{
			Row row =(Row)ds.get(i);
			status =row.getString("status","");
			deposit_status =row.getString("deposit_status",null);
			if( status.equals("1") )
			{
				status_name ="签约中";
			}
			else if(status.equals("2") || status.equals("-1"))
			{
				status="2";
				status_name ="出租中";
			}
			else if(status.equals("-2") || status.equals("-3"))
			{
				status="-2";
				status_name ="结束";
			}
			else
			{
				status_name ="已结束";
			}
			row.put("status_name", status_name);
			//查询是否已交押金
			//押金
			if(StringUtils.isEmptyOrNull(deposit_status))
			{
				deposit_status ="0";
			}
			row.put("status", status);
			row.put("deposit_status", deposit_status);
			ds.set(i, row);
		}
		return ds;
	}
	
	public Row findDetailById(String id)
	{
		Row row =null;
		sql ="select a.* , "
				+"b.name as city_name,c.name as zone_name,cp.name as province_name, "
				+"d.bank_card_no,d.bank_card_type,e.rent_id,"
				+"f.name as rent_name,f.telephone as rent_telephone "
				+"from rent_room a left join common_city b on a.city=b.id "
				+"left join common_province cp on a.province=cp.id "
				+"left join common_city_zone c on a.region=c.id "
				+"left join rent_users d on a.landlord_id=d.id "
				+"left join rent_rent e on a.id=e.room_id "
				+"left join rent_users f on f.id=e.rent_id "
				+"where a.id='"+id+"'";
		row =queryRow(sql);
		if(row == null )
		{
			return row;
		}
		String status =row.getString("status",null);
		String status_name ="";
		if(StringUtils.isEmptyOrNull(status))
		{
			status_name ="";
		}
		else 
		{
			if(status.equals("0") || status.equals("1"))
			{
				status_name ="待租";
			}
			else if(status.equals("2") || status.equals("-1"))
			{
				status_name ="出租中";
			}
			else
			{
				status_name ="结束";
			}
		}
		row.put("status_name", status_name);
		return row;
	}
	
	/**
	 * 重新放租的时候查询原房源信息
	 * @param id
	 * @return
	 */
	public Row findWhenReRent(String id)
	{
		Row row =null;
		sql ="select * from rent_room where id='"+id+"'";
		row =queryRow(sql);
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
			sql = "delete from rent_room where id in(" + id + ")";
			update(sql);
		}
	}
	
	public void deleteForHidden(String... ids) {
		String id = "";
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				if (id.length() > 0) {
					id += ",";
				}
				id += "'" + String.valueOf(ids[i]) + "'";
			}
			sql = "update rent_room set status='-4' where id in(" + id + ")";
			update(sql);
		}
	}
	
	/*******************************管理后台查询***************************************/
	/**
	 * 分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	@SuppressWarnings("unchecked")
	public Page queryPage(String room_status,Pageable pageable) {
		Page page = null;
		sql = "select * from ( "
				+" select a.id,a.code,a.address,a.status,a.deposit_status,a.start_date, " 
				+" a.end_date,a.area,a.deposit,a.monthly_rent,a.pay_day,a.invite_code, "
				+" a.water_num,a.electricity_num,a.gas_num,a.property, "
				+" cp.name as province_name,b.name as city_name,c.name as zone_name  ,d.`name` as landlord_name,d.telephone as telephone "
				+" from rent_room a  "
				+" left join common_city b on a.city=b.id " 
				+" left join common_province cp on a.province=cp.id " 
				+" left join common_city_zone c on a.region=c.id " 
				+" left join rent_users d on a.landlord_id=d.id "
				+" ) as tab  where 1=1 ";
		if(! StringUtils.isEmptyOrNull(room_status))
		{
			if(room_status.equals("2"))
			{
				sql +=" and status in ('2','-1')";
			}
			else if(room_status.equals("-2"))
			{
				sql +=" and status in ('-2','-3')";
			}
			else
			{
				sql +=" and status='"+room_status+"'";
			}
		}
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from ( "
				+" select a.id,a.code,a.address,a.status,a.deposit_status,a.start_date, " 
				+" a.end_date,a.area,a.deposit,a.monthly_rent,a.pay_day,a.invite_code, "
				+" cp.name as province_name,b.name as city_name,c.name as zone_name  ,d.`name` as landlord_name,d.telephone as telephone "
				+" from rent_room a  "
				+" left join common_city b  on a.city=b.id " 
				+" left join common_province cp  on a.province=cp.id "
				+" left join common_city_zone c on a.region=c.id " 
				+" left join rent_users d on a.landlord_id=d.id "
				+" ) as tab  where 1=1 ";
		if(! StringUtils.isEmptyOrNull(room_status))
		{
			if(room_status.equals("2"))
			{
				countSql +=" and status in ('2','-1')";
			}
			else if(room_status.equals("-2"))
			{
				countSql +=" and status in ('-2','-3')";
			}
			else
			{
				countSql +=" and status='"+room_status+"'";
			}
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
		//是否可删除 0不可删除 1可删除
		String can_delete ="0";
		//是否可终止委托 0不可终止委托 1 可以终止委托
		String can_stop ="0";
		String status ="";
		String deposit_status ="";
		String status_name ="";
		for(int i=0; i<dataSet.size(); i++)
		{
			Row row =(Row)dataSet.get(i);
			status =row.getString("status",null);
			deposit_status =row.getString("deposit_status",null);
			if(StringUtils.isEmptyOrNull(status))
			{
				can_delete ="0";
				can_stop ="0";
			}
			else 
			{
				if(status.equals("0") || status.equals("1"))
				{
					can_delete = "1";
					can_stop ="0";
					status_name ="待租";
				}
				else if(status.equals("2") || status.equals("4"))
				{
					can_delete = "0";
					can_stop ="1";
					status_name ="出租中";
				}
				else
				{
					can_delete ="0";
					can_stop ="0";
					status_name ="结束";
				}
			}
			row.put("can_delete", can_delete);
			row.put("can_stop", can_stop);
			row.put("status_name", status_name);
			//查询是否已交押金
			//押金
			if(StringUtils.isEmptyOrNull(deposit_status))
			{
				deposit_status ="0";
			}
			row.put("deposit_status", deposit_status);
			dataSet.set(i, row);
		}
		page = new Page(dataSet, total, pageable);
		return page;
	}
	
	/**
	 * 管理后台查询出租房源
	 * @param user_id
	 * @param page
	 * @param page_size
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DataSet list(String page,String page_size)
	{
		DataSet ds =new DataSet();
		int start =(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		String sql ="select a.id,a.code,a.address,a.status,a.deposit_status,a.start_date,a.end_date,b.name as city_name," +
				"c.name as zone_name " +
				"from rent_room a left join common_city b " +
				"on a.city=b.id left join common_city_zone c on a.region=c.id  " +
				"where  limit "+start+","+page_size;
		ds =queryDataSet(sql);
		//是否可删除 0不可删除 1可删除
		String can_delete ="0";
		//是否可终止委托 0不可终止委托 1 可以终止委托
		String can_stop ="0";
		String status ="";
		String deposit_status ="";
		String status_name ="";
		for(int i=0; i<ds.size(); i++)
		{
			Row row =(Row)ds.get(i);
			status =row.getString("status",null);
			deposit_status =row.getString("deposit_status",null);
			if(StringUtils.isEmptyOrNull(status))
			{
				can_delete ="0";
				can_stop ="0";
			}
			else 
			{
				if(status.equals("0") || status.equals("1"))
				{
					can_delete = "1";
					can_stop ="0";
					status_name ="待租";
				}
				else if(status.equals("2") || status.equals("4"))
				{
					can_delete = "0";
					can_stop ="1";
					status_name ="出租中";
				}
				else
				{
					can_delete ="0";
					can_stop ="0";
					status_name ="结束";
				}
			}
			row.put("can_delete", can_delete);
			row.put("can_stop", can_stop);
			row.put("status_name", status_name);
			//查询是否已交押金
			//押金
			if(StringUtils.isEmptyOrNull(deposit_status))
			{
				deposit_status ="0";
			}
			row.put("deposit_status", deposit_status);
			ds.set(i, row);
		}
		return ds;
	}
	
	
	public void resetRoomStatus()
	{
		String sql ="update rent_room set start_date='',end_date=''," +
				"pay_day='',deposit_status='0',invite_code='',status='0'," +
				"electricity_num='0',water_num='0',gas_num='0',property='0' where status='1' ";
		update(sql);
	}
//	public Row findByIdForAdmin(String id)
//	{
//		Row row =null;
//		sql ="select * from ( "
//				+" select a.id,a.code,a.address,a.status,a.deposit_status,a.start_date, " 
//				+" a.end_date,a.area,a.deposit,a.monthly_rent,a.pay_day,a.invite_code, "
//				+" b.name as city_name,c.name as zone_name  ,d.`name` as landlord_name,d.telephone as telephone "
//				+" from rent_room a  "
//				+" left join common_city b  on a.city=b.id " 
//				+" left join common_city_zone c on a.region=c.id " 
//				+" left join rent_users d on a.landlord_id=d.id "
//				+" ) as tab  where 1=1 ";
//		row =queryRow(sql);
//		return row;
//	}
	/**********************************************************************/
}