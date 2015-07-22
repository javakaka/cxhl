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
 * 类说明: 商家分类
 */

@Component("cxhlShopTypeService")
public class ShopTypeService extends Service{

	public ShopTypeService() {
		
	}

	
	/**
	 * @param id
	 * @return
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row find(String id)
	{
		Row row =null;
		String sSql ="select a.*,c.FILE_PATH from cxhl_shop_type a "
				+" left join file_attach_control b on a.id=b.DEAL_CODE and b.DEAL_TYPE='shop_type_icon' " 
				+" left join file_attach_upload c on b.CONTROL_ID=c.CONTROL_ID  "
				+" where a.id ='"+id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	public Row findByUserIdAndShopId(String user_id,String shop_id)
	{
		Row row =null;
		String sSql =" select * from cxhl_shop_type where user_id='"+user_id+"' and c_id='"+shop_id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int insert(Row row)
	{
		int num =0;
		int id =getTableSequence("cxhl_shop_type", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		num =insert("cxhl_shop_type", row);
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int update(Row row)
	{
		int num =0;
		String id =row.getString("id",null);
		row.put("modify_time", DateUtil.getCurrentDateTime());
		Assert.notNull(id);
		num =update("cxhl_shop_type", row, " id='"+id+"'");
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
		sql ="select a.*,c.FILE_PATH from cxhl_shop_type a "
		+" left join file_attach_control b on a.id=b.DEAL_CODE and b.DEAL_TYPE='shop_type_icon' "
		+" left join file_attach_upload c on b.CONTROL_ID=c.CONTROL_ID "
		+" order by a.level_index ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql ="select count(*) from cxhl_shop_type a "
				+" left join file_attach_control b on a.id=b.DEAL_CODE and b.DEAL_TYPE='shop_type_icon' "
				+" left join file_attach_upload c on b.CONTROL_ID=c.CONTROL_ID "
				+" order by a.level_index ";
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
	
	
	public DataSet list()
	{
		DataSet ds =new DataSet();
		String sSql ="select a.*,c.FILE_PATH from cxhl_shop_type a "
				+" left join file_attach_control b on a.id=b.DEAL_CODE and b.DEAL_TYPE='shop_type_icon' "
				+" left join file_attach_upload c on b.CONTROL_ID=c.CONTROL_ID "
				+" order by a.level_index ";
		ds =queryDataSet(sSql);
		Setting setting =SettingUtils.get();
		String site_url =setting.getSiteUrl();
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
						file_path =file_path.substring(iPos);
						file_path =site_url+"/"+file_path;
					}
				}
				else
				{
					file_path ="";
				}
				row.put("file_path", file_path);
				ds.set(i, row);
			}
		}
		return ds;
	}
	
	public DataSet querySummaryList()
	{
		DataSet ds =new DataSet();
		String sSql =" select id,name from cxhl_shop_type order by level_index ";
		ds =queryDataSet(sSql);
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
			sql = "delete from cxhl_shop_type where id in(" + id + ")";
			update(sql);
		}
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isNameExisted(String name)
	{
		boolean bool =true;
		String sql ="select count(*) from cxhl_shop_type where name ='"+name+"'";
		String count =queryField(sql);
		int sum =Integer.parseInt(count);
		if(sum >0)
			bool =false;
		else
			bool =true;
		return bool;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isExtraNameExisted(String id, String name)
	{
		boolean bool =true;
		String sql ="select count(*) from cxhl_shop_type where name ='"+name+"' and id !='"+id+"'";
		String count =queryField(sql);
		int sum =Integer.parseInt(count);
		if(sum >0)
			bool =false;
		else
			bool =true;
		return bool;
	}
	
}