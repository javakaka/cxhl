/**
 * 
 */
package com.cxhl.service;

import org.springframework.stereotype.Component;

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
 * @version 创建时间：2014-12-26 下午3:14:51  
 * 类说明: 中介的客户
 */

@Component("fzbAgentCustomerService")
public class AgentCustomerService extends Service{

	public AgentCustomerService() {
		
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DataSet queryPage(String mobile,int page,int page_size)
	{
		DataSet ds =null;
		int start =(page -1)*page_size;
		String sSql ="select a.id as id,a.address,a.status,a.monthly_rent, "
		+" b.name as city_name,c.name as zone_name, "
		+" d.name as landlord_name,d.telephone as landlord_phone, "
		+" f.name as rent_name ,f.telephone as rent_phone "
		+" from rent_rent e  "
		+" left join rent_room a on a.id=e.room_id "
		+" left join common_city b on a.city=b.id  "
		+" left join common_city_zone c on a.region=c.id "
		+" left join rent_users d on a.landlord_id=d.id  "
		+" left join rent_users f on f.id=e.rent_id "
		+" where a.invite_code='"+mobile+"' "
		+" limit "+start+","+page_size;
		ds =queryDataSet(sSql);
		if(ds != null && ds.size()>0)
		{
			for(int i=0; i<ds.size();i++)
			{
				Row temp =(Row)ds.get(i);
				String landlord_name =temp.getString("landlord_name","");
				String rent_name =temp.getString("rent_name","");
				if(! StringUtils.isEmptyOrNull(landlord_name))
				{
					try {
						landlord_name =AesUtil.decode(landlord_name);
						temp.put("landlord_name", landlord_name);
					} catch (Exception e) {
						landlord_name ="";
					}
				}
				if(! StringUtils.isEmptyOrNull(rent_name))
				{
					try {
						rent_name =AesUtil.decode(rent_name);
						temp.put("rent_name", rent_name);
					} catch (Exception e) {
						landlord_name ="";
					}
				}
				ds.set(i, temp);
			}
		}
		return ds;
	}
	
	/*********************************管理后台**********************************/
	/**
	 * 分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public Page queryPage(Pageable pageable) throws Exception {
		Page page = null;
		sql ="select * from "
		+" ( "
		+" select a.*,b.`name` as landlord_name,b.telephone as landlord_telephone , "
		+" c.`name` as agent_name, c.telephone as agent_telephone, "
		+" d.`name` as rent_name ,d.telephone as rent_phone," 
		+" e.`status` as room_status,e.address as room_address,e.code "
		+" from rent_agent_customer a  "
		+" left join rent_users b on a.landlord_id=b.id "
		+" left join rent_agent c on a.agent_id=c.id "
		+" left join rent_users d on a.rent_id=d.id "
		+" left join rent_room e on a.room_id =e.id "
		+" where e.`status` in ('2','-1','-2','-3') "
		+" ) as tab   where 1=1 "; 
		//对中介姓名搜索字段加密
		String search_pro =pageable.getSearchProperty();
		String search_value =pageable.getSearchValue();
		if(! StringUtils.isEmptyOrNull(search_pro))
		{
			if(search_pro.equalsIgnoreCase("AGENT_NAME"))
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
				+" ( "
				+" select a.*,b.`name` as landlord_name,b.telephone as landlord_telephone , "
				+" c.`name` as agent_name, c.telephone as agent_telephone, "
				+" d.`name` as rent_name ,d.telephone as rent_phone," 
				+" e.`status` as room_status,e.address as room_address,e.code "
				+" from rent_agent_customer a  "
				+" left join rent_users b on a.landlord_id=b.id "
				+" left join rent_agent c on a.agent_id=c.id "
				+" left join rent_users d on a.rent_id=d.id "
				+" left join rent_room e on a.room_id =e.id "
				+" where e.`status` in ('2','-1','-2','-3') "
				+" ) as tab   where 1=1 "; 
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
			for(int i=0; i<dataSet.size(); i++)
			{
				Row temp =(Row)dataSet.get(i);
				String agent_name =temp.getString("agent_name","");
				String landlord_name =temp.getString("landlord_name","");
				String rent_name =temp.getString("rent_name","");
				if(! StringUtils.isEmptyOrNull(agent_name))
				{
					try {
						agent_name =AesUtil.decode(agent_name);
						temp.put("agent_name",agent_name );
					} catch (Exception e) {
						agent_name ="";
					}
				}
				if(! StringUtils.isEmptyOrNull(landlord_name))
				{
					try {
						landlord_name =AesUtil.decode(landlord_name);
						temp.put("landlord_name", landlord_name);
					} catch (Exception e) {
						landlord_name ="";
					}
				}
				if(! StringUtils.isEmptyOrNull(rent_name))
				{
					try {
						rent_name =AesUtil.decode(rent_name);
						temp.put("rent_name", rent_name);
					} catch (Exception e) {
						rent_name ="";
					}
				}
				dataSet.set(i, temp);
			}
		}
		search_pro =pageable.getSearchProperty();
		search_value =pageable.getSearchValue();
		if(! StringUtils.isEmptyOrNull(search_pro))
		{
			if(search_pro.equalsIgnoreCase("AGENT_NAME"))
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
	
	public int insert(Row row)
	{
		int rowNum =0;
		int id=getTableSequence("rent_agent_customer", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		rowNum =insert("rent_agent_customer", row);
		return rowNum;
	}
	
	public Row find(String id)
	{
		Row row =null;
		String sql ="select a.*,b.`name` as username from rent_agent_customer a ,rent_agent b  where a.id='"+id+"' and a.user_id=b.id ";
		row =queryRow(sql);
		return row;
	}
	
	public int update(Row row)
	{
		int rowNum =0;
		String id=row.getString("id","");
		String modify_time =DateUtil.getCurrentDateTime();
		row.put("modify_time", modify_time);
		rowNum = update("rent_agent_customer", row, " id='"+id+"'");
		return rowNum;
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
			sql = "delete from rent_agent_customer where id in(" + id + ")";
			update(sql);
		}
	}
	
	/*********************************管理后台**********************************/
}