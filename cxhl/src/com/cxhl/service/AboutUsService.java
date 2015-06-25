package com.cxhl.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2015-1-1 下午4:24:59  
 * 类说明:关于我们
 */
@Component("fzbAboutUsService")
public class AboutUsService extends Service {

	public int insert(Row row) throws JException
	{
		int rowNum =0;
		int id =getTableSequence("rent_about_us", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		rowNum =insert("rent_about_us", row);
		return rowNum;
	}
	
	public Row findById(String id)
	{
		Row row =null;
		sql ="select * from rent_about_us where id='"+id+"'";
		row =queryRow(sql);
		return row;
	}
	
	public int update(Row row)
	{
		int rowNum =0;
		String id=row.getString("id");
		row.put("modify_time", DateUtil.getCurrentDateTime());
		update("rent_about_us", row, " id='"+id+"'");
		return rowNum;
	}
}