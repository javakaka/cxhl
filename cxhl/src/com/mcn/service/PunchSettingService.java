package com.mcn.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.Row;

@Component("mcnPunchSettingService")
public class PunchSettingService extends Service{

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
		sql = "select a.*, b.site_name as depart_name  from mcn_leave_setting a left join sm_site b on a.depart_id=b.site_no where 1=1 ";
		if(org_id == null || org_id.replace(" ", "").length() == 0)
		{
			return page;
		}
		sql +=" and a.org_id='"+org_id+"' ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from mcn_leave_setting where 1=1 ";
		countSql +=" and org_id='"+org_id+"'";
		countSql += restrictions;
//		countSql += orders;
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
		int id = getTableSequence("mcn_leave_setting", "id", 1);
		row.put("ID", id);
		insert("mcn_leave_setting", row);
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
		sql = "select * from mcn_leave_setting where id='" + id + "'";
		row = queryRow(sql);
		return row;
	}
	
	/**
	 * 根据org_id查找
	 * 
	 * @return Row
	 * @throws
	 */
	public Row findOrgSetting() {
		Row row = new Row();
		String org_id = getRow().getString("org_id");
		sql = "select * from mcn_leave_setting where org_id='" + org_id + "'";
		row = queryRow(sql);
		if(row == null)
		{
			int id=getTableSequence("mcn_leave_setting", "id",1);
			sql ="insert into  mcn_leave_setting  (id,org_id) values ('"+id+"','"+org_id+"')";
			insert(sql);
			sql = "select * from mcn_leave_setting where org_id='" + org_id + "'";
			row = queryRow(sql);
		}
		return row;
	}

	/**
	 * 更新
	 * 
	 * @return void
	 */
	public void update() {
		String id=getRow().getString("id",null);
		String year=getRow().getString("year","0");
		String sick=getRow().getString("sick","0");
		String exchange=getRow().getString("exchange","0");
		String personal=getRow().getString("personal","0");
		String outing=getRow().getString("outing","-1");
		String work=getRow().getString("work","0");
		row.put("year", year);
		row.put("sick", sick);
		row.put("exchange", exchange);
		row.put("personal", personal);
		row.put("outing", outing);
		row.put("work", work);
		update("mcn_leave_setting", row, " id='" + id + "'");
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
			sql = "delete from mcn_leave_setting where id in(" + id + ")";
			update(sql);
		}
	}
	
	/********************************************* mobile api ***************************************************************/
	public Row queryDepartTimes(String user_id)
	{
		Row row =new Row();
		sql ="select * from mcn_leave_setting  where DEPART_ID=(select depart_id from mcn_users where id='"+user_id+"') ";
		row =queryRow(sql);
		return row;
	}
	/********************************************* mobile api ***************************************************************/
}