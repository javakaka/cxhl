package com.mcn.service;


import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

@Component("companyService")
public class CompanyService  extends Service{

	/**
	 * 分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	public Page queryPage() {
		Page page = null;
		Pageable pageable = (Pageable) row.get("pageable");
		sql = "select * from sm_bureau where 1=1 ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from sm_bureau where 1=1 ";
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

	/**
	 * 保存
	 * 
	 * @Title: save
	 * @return void
	 */
	public void save() {
		Row row = new Row();
		String BUREAU_NAME=getRow().getString("BUREAU_NAME","");
		String UP_BUREAU_NO=getRow().getString("UP_BUREAU_NO","");
		String AREA_CODE=getRow().getString("AREA_CODE","");
		String LINKS=getRow().getString("LINKS","");
		String NOTES=getRow().getString("NOTES","");
		row.put("BUREAU_NAME", BUREAU_NAME);
		row.put("UP_BUREAU_NO", UP_BUREAU_NO);
		row.put("AREA_CODE", AREA_CODE);
		row.put("LINKS", LINKS);
		row.put("NOTES", NOTES);
		
		int BUREAU_NO = getTableSequence("sm_bureau", "bureau_no", 10000);
		row.put("BUREAU_NO", BUREAU_NO);
		insert("sm_bureau", row);
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
		sql = "select * from sm_bureau where bureau_no='" + id + "'";
		row = queryRow(sql);
		return row;
	}

	/**
	 * 更新
	 * 
	 * @return void
	 */
	public void update() {
		String BUREAU_NO=getRow().getString("BUREAU_NO","");
		String BUREAU_NAME=getRow().getString("BUREAU_NAME","");
		String UP_BUREAU_NO=getRow().getString("UP_BUREAU_NO","");
		String AREA_CODE=getRow().getString("AREA_CODE","");
		String LINKS=getRow().getString("LINKS","");
		String NOTES=getRow().getString("NOTES","");
		Row row = new Row();
		row.put("BUREAU_NAME", BUREAU_NAME);
		row.put("UP_BUREAU_NO", UP_BUREAU_NO);
		row.put("AREA_CODE", AREA_CODE);
		row.put("LINKS", LINKS);
		row.put("NOTES", NOTES);
		update("sm_bureau", row, "BUREAU_NO='" + BUREAU_NO + "'");
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
			sql = "delete from sm_bureau where bureau_no in(" + id + ")";
			update(sql);
		}
	}
	
	
	
	public DataSet queryAllBureau()
	{
		DataSet ds=new DataSet();
		sql ="select * from sm_bureau ";
		ds =queryDataSet(sql);
		return ds;
	}

	/******************************************* mobile api  *************************************************************/
	public DataSet queryAllSites(String org_id)
	{
		DataSet ds =new DataSet();
		sql="select site_no as id,SITE_NAME as name  from  sm_site  where BUREAU_NO='"+org_id+"'";
		ds =queryDataSet(sql);
		return ds;
	}
	
	public DataSet queryAllUsers(String org_id)
	{
		DataSet ds =new DataSet();
		sql ="SELECT id,name,depart_id,telephone,position from mcn_users where org_id='"+org_id+"'";
		ds =queryDataSet(sql);
		return ds;
	}
	/******************************************* mobile api  *************************************************************/
	
}
