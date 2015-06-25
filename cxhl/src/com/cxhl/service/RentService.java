package com.cxhl.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2015-1-1 下午4:24:59  
 * 类说明:租房业务处理类 
 */
@Component("fzbRentService")
public class RentService extends Service {

	public RentService() 
	{
		
	}

	public int insert(Row row) throws JException
	{
		int rowNum =0;
		int id =getTableSequence("rent_rent", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		rowNum =insert("rent_rent", row);
		return rowNum;
	}
	
	public Row findById(String id)
	{
		Row row =null;
		sql ="select * from rent_rent where id='"+id+"'";
		row =queryRow(sql);
		return row;
	}
	
	public Row findByCode(String code)
	{
		Row row =null;
		sql ="select * from rent_rent where code='"+code+"'";
		row =queryRow(sql);
		return row;
	}
	
	public int update(Row row)
	{
		int rowNum =0;
		String id=row.getString("id");
		row.put("modify_time", DateUtil.getCurrentDateTime());
		rowNum =update("rent_rent", row, " id='"+id+"'");
		return rowNum;
	}
	
	@SuppressWarnings("unchecked")
	public DataSet list(String user_id,int page,int page_size)
	{
		DataSet ds =new DataSet();
		int start =(page-1)*page_size;
		String sql ="select a.id,a.room_id,a.code, " 
		+"b.address,b.start_date,b.end_date,b.status,b.pay_day, "
		+"d.name as city_name,e.name as zone_name "
		+"from rent_rent a ,rent_room b ,common_city d,common_city_zone e " 
		+"where a.rent_id='"+user_id+"' and a.status in('1','2','3','4') and b.status in ('2','-1','-2','-3') "
		+"and a.room_id=b.id and b.city=d.id and b.region=e.id "
		+"order by a.create_time desc "
		+"limit "+start+", "+page_size;
		ds =queryDataSet(sql);
		String status ="";
		String status_name ="";
		String can_delete ="0";
		//是否可以删除
		for(int i=0; i<ds.size(); i++)
		{
			Row row =(Row)ds.get(i);
			status =row.getString("status","");
			if(status.equals("2") || status.equals("-1"))
			{
				status ="2";
				can_delete ="0";
				status_name ="承租中";
			}
			else if(status.equals("-2") || status.equals("-3"))
			{
				status ="-2";
				can_delete ="1";
				status_name ="已结束";
			}
			row.put("can_delete", can_delete);
			row.put("status_name", status_name);
			ds.set(i, row);
		}
		return ds;
	}
	
	public Row findDetailById(String id)
	{
		Row row =null;
		sql ="select a.id,a.room_id,a.code,b.status,b.invite_code,b.area,b.deposit,b.monthly_rent,b.address,b.start_date,b.end_date,b.pay_day, "
		+"d.name as city_name,e.name as zone_name "
		+"from rent_rent a ,rent_room b ,common_city d,common_city_zone e " 
		+"where a.id='"+id+"' and a.room_id=b.id and b.city=d.id and b.region=e.id ";
		row =queryRow(sql);
		return row;
	}
	
	/**************************管理后台***********************************/
	/**
	 * 分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public Page queryPage(String present,Pageable pageable) throws Exception {
		Page page = null;
		sql ="select * from "
		+" ( "
		+" select a.*,b.address as room_address,b.area,b.monthly_rent as money , " 
		+"b.start_date ,b.end_date,c.`name` as user_name ,c.telephone as user_telephone, " 
		+"f.`name` as landlord_name,f.telephone as landlord_telephone, "
		+" cp.name as province_name,d.`name` as city_name,e.`name` as region_name "
		+" from rent_rent a  "
		+" left join rent_room b on a.room_id=b.id "
		+" left join rent_users c on a.rent_id=c.id "
		+" left join common_province cp on b.province=cp.id "
		+" left join common_city d on b.city=d.id "
		+" left join common_city_zone e on b.region=e.id "
		+" left join rent_users f on b.landlord_id=f.id "
		+" where a.`status` in ('2','3','4') "
		+" ) as tab  where 1=1 ";
		if(! StringUtils.isEmptyOrNull(present))
		{
			sql +=" and present ='"+present+"' ";
		}
		String search_pro =pageable.getSearchProperty();
		if(! StringUtils.isEmptyOrNull(search_pro))
		{
			String search_value =pageable.getSearchValue();
			//租客姓名字段加密
			if(search_pro.equals("USER_NAME"))
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
		String countSql="select count(*) from "
				+" ( "
				+" select a.*,b.address as room_address,b.area,b.monthly_rent as money , " 
				+"b.start_date ,b.end_date,c.`name` as user_name ,c.telephone as user_telephone, " 
				+"f.`name` as landlord_name,f.telephone as landlord_telephone, "
				+"d.`name` as city_name,e.`name` as region_name "
				+" from rent_rent a  "
				+" left join rent_room b on a.room_id=b.id "
				+" left join rent_users c on a.rent_id=c.id "
				+" left join common_city d on b.city=d.id "
				+" left join common_city_zone e on b.region=e.id "
				+" left join rent_users f on b.landlord_id=f.id "
				+" where a.`status` in ('2','3','4') "
				+" ) as tab  where 1=1 ";
		if(! StringUtils.isEmptyOrNull(present))
		{
			countSql +=" and present ='"+present+"' ";
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
		System.out.println("sql------>>"+sql);
		dataSet = queryDataSet(sql);
		if(dataSet != null && dataSet.size() >0)
		{
			for(int i=0;i<dataSet.size();i++)
			{
				Row temp =(Row)dataSet.get(i);
				String user_name =temp.getString("user_name","");
				String landlord_name =temp.getString("landlord_name","");
				if(! StringUtils.isEmptyOrNull(user_name))
				{
					try {
						user_name =AesUtil.decode(user_name);
					} catch (Exception e) {
						user_name ="";
					}
					temp.put("user_name", user_name);
				}
				if(! StringUtils.isEmptyOrNull(landlord_name))
				{
					try {
						landlord_name =AesUtil.decode(landlord_name);
					} catch (Exception e) {
						landlord_name ="";
					}
					temp.put("landlord_name", landlord_name);
				}
				dataSet.set(i, temp);
			}
		}
		if(! StringUtils.isEmptyOrNull(search_pro))
		{
			String search_value =pageable.getSearchValue();
			//租客姓名字段加密
			if(search_pro.equals("USER_NAME"))
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
	
	/**
	 * 分页查询需要赠送门锁的记录
	 * 
	 * @Title: queryPage
	 * @return Page
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public Page queryNeedPresentLockPage(String present_status,Pageable pageable) throws Exception {
		Page page = null;
		sql ="select * from "
		+" (  "
		+" select a.*, "
		+" c.`name` as user_name,c.telephone as user_telephone "
		+" from rent_rent a   "
		+" left join rent_users c on a.rent_id=c.id " 
		+"  where a.`status` in ('2','3','4') and a.present='1' "
		+" ) as tab1 where 1=1 ";
		if(! StringUtils.isEmptyOrNull(present_status))
		{
			sql +=" and present_status='"+present_status+"' ";
		}
		//对租客姓名搜索字段进行加密
		String search_pro =pageable.getSearchProperty();
		String search_value =pageable.getSearchValue();
		if(! StringUtils.isEmptyOrNull(search_pro))
		{
			if(search_pro.equalsIgnoreCase("USER_NAME"))
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
		String countSql ="select count(*) from "
				+" (  "
				+" select a.*, "
				+" c.`name` as user_name,c.telephone as user_telephone "
				+" from rent_rent a   "
				+" left join rent_users c on a.rent_id=c.id " 
				+"  where a.`status` in ('2','3','4') and a.present='1' "
				+" ) as tab1 where 1=1 ";
		if(! StringUtils.isEmptyOrNull(present_status))
		{
			countSql +=" and present_status='"+present_status+"' ";
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
		if(dataSet != null && dataSet.size() >0)
		{
			for(int i=0;i<dataSet.size();i++)
			{
				Row temp =(Row)dataSet.get(i);
				String user_name =temp.getString("user_name","");
				if(! StringUtils.isEmptyOrNull(user_name))
				{
					try {
						user_name =AesUtil.decode(user_name);
					} catch (Exception e) {
						user_name ="";
					}
					temp.put("user_name", user_name);
				}
				dataSet.set(i, temp);
			}
		}
		//对租客姓名搜索字段进行解密
		search_pro =pageable.getSearchProperty();
		search_value =pageable.getSearchValue();
		if(! StringUtils.isEmptyOrNull(search_pro))
		{
			if(search_pro.equalsIgnoreCase("USER_NAME"))
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
	
	public Row findByIdForAdmin(String id)
	{
		Row row =null;
		String sql ="select * from "
				+" ( "
				+" select a.*,b.address as room_address,b.area,b.monthly_rent as money , " 
				+"b.start_date ,b.end_date,c.`name` as user_name ,c.telephone as user_telephone, " 
				+"f.`name` as landlord_name,f.telephone as landlord_telephone, "
				+" cp.name as province_name,d.`name` as city_name,e.`name` as region_name "
				+" from rent_rent a  "
				+" left join rent_room b on a.room_id=b.id "
				+" left join rent_users c on a.rent_id=c.id "
				+" left join common_province cp on b.province=cp.id "
				+" left join common_city d on b.city=d.id "
				+" left join common_city_zone e on b.region=e.id "
				+" left join rent_users f on b.landlord_id=f.id "
				+" where a.`status` in ('2','3','4') "
				+" ) as tab  where 1=1  and id='"+id+"'";
		row =queryRow(sql);
		return row;
	}
	
	/**
	 * 删除
	 * 
	 * @Title: delete
	 * @param @param ids
	 * @return void
	 */
	public void delete(Long... ids) {
		String id = "";
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				if (id.length() > 0) {
					id += ",";
				}
				id += "'" + String.valueOf(ids[i]) + "'";
			}
			sql = "update rent_rent set status='"+5+"' where id in(" + id + ")";
			update(sql);
		}
	}
	/**************************管理后台***********************************/
}