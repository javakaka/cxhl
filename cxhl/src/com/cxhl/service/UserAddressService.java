/**
 * 
 */
package com.cxhl.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
 * 类说明: 收货地址
 */

@Component("cxhlUserAddressService")
public class UserAddressService extends Service{

	public UserAddressService() {
		
	}

	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row find(String id)
	{
		Row row =null;
		String sSql =" select a.*,b.`name` as province_name,c.`name` as city_name,d.`name` as region_name "
				+" from cxhl_receive_address a "
				+" left join common_province b on  a.province_id=b.id "
				+" left join common_city c on a.city_id=c.id "
				+" left join common_city_zone d on a.region_id=d.id "
				+" where a.id='"+id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public int findTotalNum()
	{
		int num =0;
		String sSql =" select count(*) from cxhl_receive_address ";
		num =Integer.parseInt(queryField(sSql));
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int insert(Row row)
	{
		int num =0;
		int id =getTableSequence("cxhl_receive_address", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		num =insert("cxhl_receive_address", row);
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int update(Row row)
	{
		int num =0;
		String id =row.getString("id",null);
		row.put("modify_time", DateUtil.getCurrentDateTime());
		Assert.notNull(id);
		num =update("cxhl_receive_address", row, " id='"+id+"'");
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int updateUserDefaultAddressById(String id)
	{
		int num =0;
		String sql =" update cxhl_receive_address set is_default='0' "
				+ "where user_id in (select user_id from cxhl_receive_address where id='"+id+"') ";
		num =update(sql);
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int updateUserDefaultAddressByUserId(String user_id)
	{
		int num =0;
		String sql =" update cxhl_receive_address set is_default='0' "
				+ "where user_id ='"+user_id+"'";
		num =update(sql);
		return num;
	}
	
	/**
	 * 分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Page queryPage() {
		Page page = null;
		Pageable pageable = (Pageable) row.get("pageable");
		sql = "select * from ("
		+" select a.*,b.telephone,b.`name` as username, "
		+" cp.`name` as province_name,cc.`name` as city_name,ccz.`name` as region_name " 
		+" from cxhl_receive_address a "
		+" left join cxhl_users b on a.user_id=b.id "
		+" left join common_province cp on a.province_id=cp.id "
		+" left join common_city cc on a.city_id=cc.id "
		+" left join common_city_zone ccz on a.region_id=ccz.id "
		+" ) as tab where 1=1";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql ="select count(*) from ("
				+" select a.*,b.telephone,b.`name` as username, "
				+" cp.`name` as province_name,cc.`name` as city_name,ccz.`name` as region_name " 
				+" from cxhl_receive_address a "
				+" left join cxhl_users b on a.user_id=b.id "
				+" left join common_province cp on a.province_id=cp.id "
				+" left join common_city cc on a.city_id=cc.id "
				+" left join common_city_zone ccz on a.region_id=ccz.id "
				+" ) as tab where 1=1";
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
		page = new Page(dataSet, total, pageable);
		return page;
	}
	
	
	public DataSet list(String user_id,String page,String page_size)
	{
		DataSet ds =new DataSet();
		int start =(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		String sql ="select * " +
				"from cxhl_receive_address " +
				"where user_id='"+user_id+"' limit "+start+","+page_size;
		ds =queryDataSet(sql);
		return ds;
	}
	
	/**
	 * 删除
	 * 
	 * @Title: delete
	 * @param @param ids
	 * @return void
	 */
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public void delete(String... ids) {
		String id = "";
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				if (id.length() > 0) {
					id += ",";
				}
				id += "'" + String.valueOf(ids[i]) + "'";
			}
			sql = "delete from cxhl_receive_address where id in(" + id + ")";
			update(sql);
		}
	}
	
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isExtraEmailExisted(String id, String email)
	{
		boolean bool =true;
		String sql ="select count(*) from cxhl_receive_address where email ='"+email+"' and id !='"+id+"'";
		String count =queryField(sql);
		int sum =Integer.parseInt(count);
		if(sum >0)
			bool =false;
		else
			bool =true;
		return bool;
	}
	
}