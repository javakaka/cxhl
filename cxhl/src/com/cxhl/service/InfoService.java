/**
 * 
 */
package com.cxhl.service;

import java.util.Calendar;

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
 * 类说明: 资讯
 */

@Component("cxhlInfoService")
public class InfoService extends Service{

	public InfoService() {
		
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
		String sSql ="select * from cxhl_info where id='"+id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	public Row findDetail(String id)
	{
		Row row =null;
		String sSql ="select a.*,d.name as type_name,c.FILE_PATH " 
		+" from cxhl_info a "
		+" left join file_attach_control b on b.DEAL_CODE=a.id and b.DEAL_TYPE='info_icon'  "
		+" left join file_attach_upload c on b.CONTROL_ID=c.CONTROL_ID  "
		+" left join cxhl_info_type d on a.type_id=d.id "
		+" where a.id='"+id+"' ";
		row =queryRow(sSql);
		Setting setting =SettingUtils.get();
		String site_url =setting.getSiteUrl();
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
		return row;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isExisted(String id)
	{
		boolean bool =true;
		String sSql ="select count(*) from cxhl_info  "
				+" where id='"+id+"' ";
		int num =Integer.parseInt(queryField(sSql));
		if(num == 0)
		{
			bool =false;
		}
		return bool;
	}
	
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public String findRemark(String id)
	{
		String detail ="";
		String sSql ="select detail from cxhl_info  "
				+" where id='"+id+"' ";
		detail =queryField(sSql);
		return detail;
	}
	
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int insert(Row row)
	{
		int num =0;
		int id =getTableSequence("cxhl_info", "id", 1);
		String cur_time=DateUtil.getCurrentDateTime();
		String cur_date=cur_time.substring(0, 10);
		String year =cur_time.substring(0, 4);
		String month =cur_time.substring(5,7);
		String day =cur_time.substring(8, 10);
		row.put("id", id);
		row.put("cur_date", cur_date);
		row.put("year", year);
		row.put("month", month);
		row.put("day", day);
		row.put("create_time", cur_time);
		int week =Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
		String cur_week =cur_date.replace("-", "")+week;
		row.put("week", cur_week);
		num =insert("cxhl_info", row);
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int update(Row row)
	{
		int num =0;
		String id =row.getString("id",null);
		row.put("modify_time", DateUtil.getCurrentDateTime());
		Assert.notNull(id);
		num =update("cxhl_info", row, " id='"+id+"'");
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
				+" select a.*,b.c_name as shop_name from cxhl_info a "
				+" left join cxhl_shop b on a.shop_id=b.id "
				+" ) as tab  where 1=1 ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql ="select count(*) from ( "
				+" select a.*,b.c_name as shop_name from cxhl_info a "
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
	public DataSet list(String type_id,String key_words,String page,String page_size)
	{
		int iStart =(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		
		DataSet ds =new DataSet();
		String sSql ="select a.id,a.type_id,a.title,a.subtitle,a.create_date,"
		+" a.up_num,a.create_time,d.name as type_name,c.FILE_PATH " 
		+" from cxhl_info a "
		+" left join file_attach_control b on b.DEAL_CODE=a.id and b.DEAL_TYPE='info_icon'  "
		+" left join file_attach_upload c on b.CONTROL_ID=c.CONTROL_ID  "
		+" left join cxhl_info_type d on a.type_id=d.id "
		+" where 1=1 ";
		if(! StringUtils.isEmptyOrNull(type_id))
		{
			sSql +=" and a.type_id ='"+type_id+"' ";
		}
		sSql +=" order by a.create_time desc ";	
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
			sql = "delete from cxhl_info_praise where info_id in (" + id + ")";
			update(sql);
			sql = "delete from cxhl_info_collection  where info_id in (" + id + ")";
			update(sql);
			sql = "delete from cxhl_info where id in (" + id + ")";
			update(sql);
		}
	}
	
}