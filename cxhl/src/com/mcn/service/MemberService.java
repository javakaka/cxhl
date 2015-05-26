package com.mcn.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.Row;

@Component("mcnMemberService")
public class MemberService extends Service{

	/**
	 * 分页查询企业的用户
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	public Page queryPageForCompany() {
		Page page = null;
		Pageable pageable = (Pageable) row.get("pageable");
		String org_id =row.getString("org_id",null);
		sql = "select a.*, b.site_name as depart_name  from mcn_users a left join sm_site b on a.depart_id=b.site_no where 1=1 ";
		if(org_id == null || org_id.replace(" ", "").length() == 0)
		{
			return page;
		}
		sql +=" and a.org_id='"+org_id+"' ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from mcn_users where 1=1 ";
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
		String DEPART_ID=getRow().getString("DEPART_ID","");
		String NAME=getRow().getString("NAME","");
		String PASSWORD=getRow().getString("PASSWORD","");
		String USERNAME=getRow().getString("USERNAME","");
		String TELEPHONE=getRow().getString("TELEPHONE","");
		String SEX=getRow().getString("SEX","");
		String POSITION=getRow().getString("POSITION","");
		String MANAGER_ID=getRow().getString("MANAGER_ID","");
		String REMARK=getRow().getString("REMARK","");
		String ORG_ID=getRow().getString("ORG_ID","");
		row.put("DEPART_ID", DEPART_ID);
		row.put("NAME", NAME);
		row.put("PASSWORD", PASSWORD);
		row.put("TELEPHONE", TELEPHONE);
		row.put("SEX", SEX);
		row.put("POSITION", POSITION);
		row.put("MANAGER_ID", MANAGER_ID);
		row.put("REMARK", REMARK);
		row.put("ORG_ID", ORG_ID);
		String sql ="select max(username) from mcn_users where org_id='"+ORG_ID+"'";
		String user_seq = queryField(sql);
		if(user_seq == null)
		{
			user_seq ="1001";
		}
		else{
			user_seq =String.valueOf(Integer.parseInt(user_seq)+1);
		}
		row.put("USERNAME", user_seq);
		int id = getTableSequence("mcn_users", "id", 1);
		row.put("ID", id);
		insert("mcn_users", row);
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
		sql = "select * from mcn_users where id='" + id + "'";
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
		String NAME=getRow().getString("NAME","");
		String PASSWORD=getRow().getString("PASSWORD","");
		String USERNAME=getRow().getString("USERNAME","");
		String TELEPHONE=getRow().getString("TELEPHONE","");
		String SEX=getRow().getString("SEX","");
		String POSITION=getRow().getString("POSITION","");
		String MANAGER_ID=getRow().getString("MANAGER_ID","");
		String REMARK=getRow().getString("REMARK","");
		String ORG_ID=getRow().getString("ORG_ID","");
		row.put("DEPART_ID", DEPART_ID);
		row.put("NAME", NAME);
		row.put("PASSWORD", PASSWORD);
		row.put("TELEPHONE", TELEPHONE);
		row.put("SEX", SEX);
		row.put("POSITION", POSITION);
		row.put("MANAGER_ID", MANAGER_ID);
		row.put("REMARK", REMARK);
		row.put("ORG_ID", ORG_ID);
		row.put("USERNAME", USERNAME);
		update("mcn_users", row, " id='" + ID + "'");
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
			sql = "delete from mcn_users where id in(" + id + ")";
			update(sql);
		}
	}
	
}