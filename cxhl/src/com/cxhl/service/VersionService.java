package com.cxhl.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2015-1-1 下午4:24:59  
 * 类说明:广告业务处理类 
 */
@Component("cxhlVersionService")
public class VersionService extends Service {

	public VersionService() 
	{
		
	}

	public int insert(Row row) 
	{
		int rowNum =0;
		int id =getTableSequence("cxhl_app_version", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		rowNum =insert("cxhl_app_version", row);
		return rowNum;
	}
	
	public Row findById(String id)
	{
		Row row =null;
		sql ="select * from cxhl_app_version where id='"+id+"'";
		row =queryRow(sql);
		return row;
	}
	
	public Row findLastestVersion(String app,String device)
	{
		Row row =null;
		sql ="select * from cxhl_app_version where app_id='"+app+"' and device='"+device+"'";
		row =queryRow(sql);
		return row;
	}
	
	public int update(Row row)
	{
		int rowNum =0;
		String id=row.getString("id");
		row.put("modify_time", DateUtil.getCurrentDateTime());
		update("cxhl_app_version", row, " id='"+id+"'");
		return rowNum;
	}
	
	public DataSet list()
	{
		DataSet ds =new DataSet();
		String now_time =DateUtil.getCurrentDateTime();
		sql ="select id,name,picture,width,height from cxhl_app_version where status=1 and (start_time<='"+now_time+"' and end_time>='"+now_time+"')";
		ds =queryDataSet(sql);
		return ds;
		
	} 
	
	/**
	 * 分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	@SuppressWarnings("unchecked")
	public Page queryPage(String app_id,String device, Pageable pageable) {
		Page page = null;
		sql = "select * from "
		+" ( "
		+" select a.FILE_NAME,a.FILE_TYPE,a.DEAL_CODE,a.DEAL_TYPE,b.FILE_CODE,b.FILE_SIZE,b.FILE_PATH ,c.* "
		+" from cxhl_app_version c  "
		+" left join file_attach_control a on a.DEAL_CODE=c.id "
		+" left join file_attach_upload b on a.CONTROL_ID=b.CONTROL_ID " 
		+" and a.DEAL_TYPE='fangzubao_app_file'   "
		+" and a.TYPE='app_file'  "
		+" and a.SUB_TYPE='app_file' " 
		+" ) as tab where 1=1  ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql ="select count(*) from "
				+" ( "
				+" select a.FILE_NAME,a.FILE_TYPE,a.DEAL_CODE,a.DEAL_TYPE,b.FILE_CODE,b.FILE_SIZE,b.FILE_PATH ,c.* "
				+" from cxhl_app_version c  "
				+" left join file_attach_control a on a.DEAL_CODE=c.id "
				+" left join file_attach_upload b on a.CONTROL_ID=b.CONTROL_ID " 
				+" and a.DEAL_TYPE='fangzubao_app_file'   "
				+" and a.TYPE='app_file'  "
				+" and a.SUB_TYPE='app_file' " 
				+" ) as tab where 1=1  ";
		countSql += restrictions;
		countSql += orders;
		if(! StringUtils.isEmptyOrNull(app_id))
		{
			sql +=" and app_id='"+app_id+"'";
			countSql +=" and app_id='"+app_id+"'";
		}
		if(! StringUtils.isEmptyOrNull(device))
		{
			sql +=" and device='"+device+"'";
			countSql +=" and device='"+device+"'";
		}
		long total = count(countSql);
		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}
		int startPos = (pageable.getPageNumber() - 1) * pageable.getPageSize();
		sql += " limit " + startPos + " , " + pageable.getPageSize();
		dataSet = queryDataSet(sql);
		String path ="";
		String pre_path ="";
		double size =0.00;
		for(int i=0;i<dataSet.size();i++)
		{
			Row tempRow =(Row)dataSet.get(i);
			path =tempRow.getString("file_path");
			int iPos =path.indexOf("resources");
			if(iPos !=-1)
			{
				pre_path =path.substring(0,iPos+"resources".length());
				path ="../"+path.substring(iPos);
			}
			String ssize =tempRow.getString("FILE_SIZE", "");
			if(!StringUtils.isEmptyOrNull(ssize))
			{
				ssize= NumberUtils.getTwoDecimal(ssize);
				size=Double.parseDouble(ssize);
				size=size/1024;
				size=size/1024;
				ssize= NumberUtils.getTwoDecimal(String.valueOf(size));
				tempRow.put("FILE_SIZE", ssize);
			}
			tempRow.put("path", path);
			tempRow.put("pre_path", pre_path);
			dataSet.set(i, tempRow);
		}
		page = new Page(dataSet, total, pageable);
		return page;
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
			sql = "delete from cxhl_app_version where id in(" + id + ")";
			update(sql);
		}
	}
	
	public void updateVersionStatus(String app_id,String device,String version_id)
	{
		//update db record
		String sql =" update cxhl_app_version set is_curcxhl_version='2' where app_id='"+app_id+"' and device='"+device+"'";
		update(sql);
		sql ="update cxhl_app_version set is_curcxhl_version='1' where id='"+version_id+"'";
		update(sql);
		
	}
	
}