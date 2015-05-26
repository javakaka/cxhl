package com.mcn.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

@Component("meetingRoomBookService")
public class MeetingRoomBook extends Service{
	
	/**
	 * 分页查询公司的预定记录
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	public Page queryPageForCompany() {
		Page page = null;
		Pageable pageable = (Pageable) row.get("pageable");
		sql = " select a.*, b.name as username ,c.name as roomname from mcn_meeting_room_book a " +
				" left join mcn_users b on a.book_user_id=b.id  " +
				" left join mcn_meeting_room c on a.room_id=c.id where 1=1 ";
		String org_id =row.getString("org_id",null);
		if(org_id != null && org_id.replace(" ", "").length() >0)
		{
			sql +=" and a.org_id='"+org_id+"'";
		}
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from ( "+sql+" ) as tab1";
//		String countSql = "select count(*) from mcn_meeting_room_book where 1=1 ";
//		if(org_id != null && org_id.replace(" ", "").length() >0)
//		{
//			countSql +=" and org_id='"+org_id+"'";
//		}
//		countSql += restrictions;
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
		
		int BUREAU_NO = getTableSequence("mcn_meeting_room_book", "bureau_no", 10000);
		row.put("BUREAU_NO", BUREAU_NO);
		insert("mcn_meeting_room_book", row);
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
		sql = "select a.*,b.name as username, c.name as roomname from mcn_meeting_room_book a " +
				" left join mcn_users b on a.book_user_id = b.id " +
				" left join mcn_meeting_room c on a.room_id=c.id where a.id='" + id + "'";
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
		update("mcn_meeting_room_book", row, "BUREAU_NO='" + BUREAU_NO + "'");
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
			sql = "delete from mcn_meeting_room_book where id in(" + id + ")";
			update(sql);
		}
	}

	//-----------------------------mobile api -----------------------------
	//预定
	public int bookRoom(Row row)
	{
		int state =0;
		String id =String.valueOf(getTableSequence("mcn_meeting_room_book", "id", 1));
		row.put("id", id);
		state = insert("mcn_meeting_room_book",row);
		return state;
	}
	
	public DataSet queryUserBooklist(String id)
	{
		DataSet ds =new DataSet();
		sql ="select * from mcn_meeting_room_book where book_user_id='"+id+"'";
		ds =queryDataSet(sql);
		return ds;
	}
	
	//-----------------------------mobile api -----------------------------
}
