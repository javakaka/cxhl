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
 * 类说明: 资讯收藏
 */

@Component("cxhlInfoCollectionService")
public class UserInfoCollectionService extends Service{

	public UserInfoCollectionService() {
		
	}

	
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row find(String id)
	{
		Row row =null;
		String sSql ="select * from cxhl_info_collection where id='"+id+"' ";
		row =queryRow(sSql);
		return row;
	}
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row findByUserIdAndInfoId(String user_id,String info_id)
	{
		Row row =null;
		String sSql ="select * from cxhl_info_collection where user_id='"+user_id+"' and info_id='"+info_id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int insert(Row row)
	{
		int num =0;
		int id =getTableSequence("cxhl_info_collection", "id", 1);
		row.put("id", id);
		String cur_time=DateUtil.getCurrentDateTime();
		row.put("create_time", cur_time);
		num =insert("cxhl_info_collection", row);
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int update(Row row)
	{
		int num =0;
		String id =row.getString("id",null);
		Assert.notNull(id);
		num =update("cxhl_info_collection", row, " id='"+id+"'");
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
		sql ="select * from ( "
				+" select a.*,b.c_name as shop_name from cxhl_info_collection a "
				+" left join cxhl_shop b on a.shop_id=b.id "
				+" ) as tab  where 1=1 ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql ="select count(*) from ( "
				+" select a.*,b.c_name as shop_name from cxhl_info_collection a "
				+" left join cxhl_shop b on a.shop_id=b.id "
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
	
	@SuppressWarnings("unchecked")
	public DataSet list(String user_id,String page,String page_size)
	{
		int iStart =(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		
		DataSet ds =new DataSet();
		String sSql ="select a.*,b.title,b.subtitle,b.create_date,b.up_num " 
		+" from cxhl_info_collection a "
		+" left join cxhl_info b on a.info_id =b.id "
		+" left join file_attach_control c on c.DEAL_CODE=b.id and c.DEAL_TYPE='info_icon'  "
		+" left join file_attach_upload d on c.CONTROL_ID=d.CONTROL_ID  "
		+" where 1=1 ";
		if(! StringUtils.isEmptyOrNull(user_id))
		{
			sSql +=" and a.user_id ='"+user_id+"' ";
		}
		sSql +=" limit "+iStart+" , "+page_size;	
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
			sql = "delete from cxhl_info_collection where id in (" + id + ")";
			update(sql);
		}
	}
	
}