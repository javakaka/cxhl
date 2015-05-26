package com.mcn.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

@Component("mcnLeaveService")
public class LeaveService extends Service{

	/**
	 * 分页查询打卡规则
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	public Page queryPageForCompany() {
		Page page = null;
		Pageable pageable = (Pageable) row.get("pageable");
		String org_id =row.getString("org_id",null);
		sql = "select a.*, b.name  from mcn_leave_log a left join mcn_users b on a.user_id=b.id where 1=1 ";
		if(org_id == null || org_id.replace(" ", "").length() == 0)
		{
			return page;
		}
		sql +=" and a.org_id='"+org_id+"' ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from mcn_leave_log a left join mcn_users b  on a.user_id=b.id  where 1=1 ";
		countSql +=" and a.org_id='"+org_id+"'";
		countSql += restrictions;
//		countSql += orders;
		long total = count(countSql);
		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) 
		{
			pageable.setPageNumber(totalPages);
		}
		int startPos = (pageable.getPageNumber() - 1) * pageable.getPageSize();
		sql += " limit " + startPos + " , " + pageable.getPageSize();
		dataSet = queryDataSet(sql);
		page = new Page(dataSet, total, pageable);
		return page;
	}

	/**
	 * 保存
	 * 
	 * @Title: save
	 * @return void
	 */
	public void save() {
		Row row = new Row();
		String ORG_ID=getRow().getString("org_id","");
		String DEPART_ID=getRow().getString("DEPART_ID","");
		String AM_START=getRow().getString("AM_START","");
		String AM_END=getRow().getString("AM_END","");
		String PM_START=getRow().getString("PM_START","");
		String PM_END=getRow().getString("PM_END","");
		row.put("ORG_ID", ORG_ID);
		row.put("DEPART_ID", DEPART_ID);
		row.put("AM_START", AM_START);
		row.put("AM_END", AM_END);
		row.put("PM_START", PM_START);
		row.put("PM_END", PM_END);
		int id = getTableSequence("mcn_leave_log", "id", 1);
		row.put("ID", id);
		insert("mcn_leave_log", row);
	}

	/**
	 * 根据id查找
	 * 
	 * @return Row
	 * @throws
	 */
	public Row find() {
		Row row = new Row();
		String id = getRow().getString("id");
		sql = "select * from mcn_leave_log where id='" + id + "'";
		row = queryRow(sql);
		return row;
	}

	/**
	 * 更新
	 * 
	 * @return void
	 */
	public void update() {
		String ID=getRow().getString("ID","");
		String DEPART_ID=getRow().getString("DEPART_ID","");
		String AM_START=getRow().getString("AM_START","");
		String AM_END=getRow().getString("AM_END","");
		String PM_START=getRow().getString("PM_START","");
		String PM_END=getRow().getString("PM_END","");
		row.put("DEPART_ID", DEPART_ID);
		row.put("AM_START", AM_START);
		row.put("AM_END", AM_END);
		row.put("PM_START", PM_START);
		row.put("PM_END", PM_END);
		row.put("ID", ID);
		update("mcn_leave_log", row, " id='" + ID + "'");
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
			sql = "delete from mcn_leave_log where id in(" + id + ")";
			update(sql);
		}
	}
	
	/************************************* mobile api ************************************************/
	//手机请假
	public void mobileAdd(Row row)
	{
		int id =getTableSequence("mcn_leave_log", "id", 1);
		row.put("id", id);
		String curTime =DateUtil.getCurrentDateTime();
		row.put("create_time",curTime );
		row.put("year", curTime.substring(0,4));
		row.put("month", curTime.substring(5, 7));
		row.put("status", 1);
		insert("mcn_leave_log", row);
	}
	
	//查询自己的请假列表
	public DataSet querySelfLeaveList(String user_id,String page,String page_size)
	{
		DataSet ds =new DataSet();
		int iStart=(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		sql ="select id,leave_type,start_date, end_date, reason,status, audit_objection from mcn_leave_log where user_id='"+user_id+"'  limit "+iStart+" , "+page_size;
		ds =queryDataSet(sql);
		return ds;
	}
	
	//查询属下的请假列表
	public DataSet queryFollowerLeaveList(String user_id,String page,String page_size)
	{
		DataSet ds =new DataSet();
		int iStart=(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		sql ="select a.id,name,leave_type,start_date, end_date, reason,status, audit_objection from mcn_leave_log a left join mcn_users b on a.user_id=b.id  where user_id "
		+" in ( select id from mcn_users where depart_id in (select depart_id from mcn_users where id='"+user_id+"') and id !='"+user_id+"')   limit "+iStart+" , "+page_size;
		ds =queryDataSet(sql);
		return ds;
	}
	
	public void auditLeave(Row row)
	{
		String id=row.getString("id");
		update("mcn_leave_log", row, " id='"+id+"'");
	}
	/************************************* mobile api ************************************************/
	
}