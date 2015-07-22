/**
 * 
 */
package com.cxhl.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ezcloud.framework.common.Setting;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.SettingUtils;
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

@Component("cxhlUserCollectionService")
public class UserCollectionService extends Service{

	public UserCollectionService() {
		
	}

	
	/**
	 * 点击进入商家详情
	 * @param id
	 * @return
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row find(String id)
	{
		Row row =null;
		String sSql =" select a.id,a.c_id as shop_id,a.create_time , "
				+ " b.c_name as shop_name,b.remark from cxhl_user_collection a " 
				+" left join cxhl_shop b on a.c_id=b.id "
				+" where a.id ='"+id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	public Row findByUserIdAndShopId(String user_id,String shop_id)
	{
		Row row =null;
		String sSql =" select * from cxhl_user_collection where user_id='"+user_id+"' and c_id='"+shop_id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int insert(Row row)
	{
		int num =0;
		int id =getTableSequence("cxhl_user_collection", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		num =insert("cxhl_user_collection", row);
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int update(Row row)
	{
		int num =0;
		String id =row.getString("id",null);
		row.put("modify_time", DateUtil.getCurrentDateTime());
		Assert.notNull(id);
		num =update("cxhl_user_collection", row, " id='"+id+"'");
		return num;
	}
	
	
	/**
	 * 分页查询
	 * @Title: queryPage
	 * @return Page
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Page queryPage() {
		Page page = null;
		Pageable pageable = (Pageable) row.get("pageable");
		sql = "select * from "
		+" ( "
		+" select a.* ,b.c_name as collection_name,c.telephone,c.`name` as username "
		+" from cxhl_user_collection a "
		+" left join cxhl_shop b on a.c_id=b.id "
		+" left join cxhl_users c on a.user_id=c.id "
		+" where a.c_type='0' "
		+" ) as tab  where 1=1 ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from "
				+" ( "
				+" select a.* ,b.c_name as collection_name,c.telephone,c.`name` as username "
				+" from cxhl_user_collection a "
				+" left join cxhl_shop b on a.c_id=b.id "
				+" left join cxhl_users c on a.user_id=c.id "
				+" where a.c_type='0' "
				+" ) as tab  where 1=1 ";
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
		String sSql ="select a.id,a.c_id as shop_id,a.create_time , "
				+" b.c_name as shop_name,b.remark,d.file_path from cxhl_user_collection a " 
				+" left join cxhl_shop b on a.c_id=b.id  "
				+" left join file_attach_control c on a.id=c.DEAL_CODE and c.DEAL_TYPE='shop_icon' " 
				+" left join file_attach_upload d on d.CONTROL_ID=c.CONTROL_ID  "
				+" where a.user_id ='"+user_id+"' "
				+" order by a.create_time desc "
				+" limit "+start+" ,"+page_size;
		ds =queryDataSet(sSql);
		Setting setting =SettingUtils.get();
		String url =setting.getSiteUrl();
		if(StringUtils.isEmptyOrNull(url))
		{
			url ="";
		}
		if(ds != null && ds.size() >0 )
		{
			for(int i=0;i<ds.size(); i++)
			{
				Row row =(Row)ds.get(i);
				String file_path =row.getString("file_path","");
				int iPos =-1;
				if(!StringUtils.isEmptyOrNull(file_path))
				{
					iPos =file_path.indexOf("resources");
					if(iPos != -1)
					{
						file_path =url+"/"+file_path.substring(iPos);
					}
				}
				else
				{
					file_path ="";
				}
				row.put("SHOP_PICTURE", file_path);
				ds.set(i, row);
			}
		}
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
			sql = "delete from cxhl_user_collection where id in(" + id + ")";
			update(sql);
		}
	}
	
}