package com.mcn.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

@Component("meetingRoomService")
public class MeetingRoom  extends Service{

	/**
	 * 分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	public Page queryPage() {
		Page page = null;
		Pageable pageable = (Pageable) row.get("pageable");
		sql = "select a.*,b.name as audit_name from mcn_meeting_room a  left join mcn_users b on a.audit_id=b.id where 1=1 ";
		String org_id =row.getString("org_id",null);
		if(org_id == null){
			return page;
		}
		sql +=" and a.org_id='"+org_id+"'";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from mcn_meeting_room where 1=1 ";
		countSql += " and org_id='"+org_id+"'";
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
//		System.out.println("======sql=======>>>"+sql);
		page = new Page(dataSet, total, pageable);
//		System.out.print("\n======dataSet=======>>>"+dataSet);
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
		String name = getRow().getString("name", null);
		String num = getRow().getString("num", null);
		String audit_id = getRow().getString("audit_id", null);
		String org_id = getRow().getString("org_id", null);
		row.put("name", name);
		row.put("num", num);
		row.put("audit_id", audit_id);
		row.put("org_id", org_id);
		int id = getTableSequence("mcn_meeting_room", "id", 1);
		row.put("id", id);
		insert("mcn_meeting_room", row);
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
		sql = "select * from mcn_meeting_room where id='" + id + "'";
		row = queryRow(sql);
		return row;
	}

	/**
	 * 更新
	 * 
	 * @return void
	 */
	public void update() {
		String id = getRow().getString("id", null);
		String name = getRow().getString("name", null);
		String num = getRow().getString("num", null);
		String audit_id = getRow().getString("audit_id", null);
		Row row = new Row();
		row.put("id", id);
		row.put("name", name);
		row.put("num", num);
		row.put("audit_id", audit_id);
		update("mcn_meeting_room", row, "id='" + id + "'");
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
			sql = "delete from mcn_meeting_room where id in(" + id + ")";
			update(sql);
		}
	}
	
	//-----------------------------------------------------mobile api start---------------------------------- 
	public DataSet getMobileRoomPage(String token,String page,String pageSize)
	{
		DataSet ds =new DataSet();
		
		int recordStart =(Integer.parseInt(page) -1) *Integer.parseInt(pageSize);
		sql ="select id,name,num,audit_id,status from mcn_meeting_room where org_id ='"+token+"'  limit "+recordStart +" , "+pageSize;
		ds =queryDataSet(sql);
		return ds;
	}
	//-----------------------------------------------------mobile api end---------------------------------- 
}
