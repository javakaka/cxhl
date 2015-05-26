package com.mcn.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.Row;

@Component("meetingRoomStatusService")
public class MeetingRoomStatus extends Service{

	/**
	 * 分页查询指定会议室状态
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	public Page queryPage() {
		Page page = null;
		Pageable pageable = (Pageable) row.get("pageable");
		sql = "select *  from mcn_meeting_room_status  where 1=1 ";
		String room_id =row.getString("room_id",null);
		if(room_id == null){
			return page;
		}
		sql +=" and room_id='"+room_id+"'";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from mcn_meeting_room_status where 1=1 ";
		countSql += " and room_id='"+room_id+"'";
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
		int id = getTableSequence("mcn_meeting_room_status", "id", 1);
		row.put("id", id);
		insert("mcn_meeting_room_status", row);
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
		sql = "select * from mcn_meeting_room_status where id='" + id + "'";
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
		update("mcn_meeting_room_status", row, "id='" + id + "'");
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
			sql = "delete from mcn_meeting_room_status where id in(" + id + ")";
			update(sql);
		}
	}
	
	//------------------------------mobile api------------------------------
	
	public void initRoomStatus(String org_id,String room_id,String date)
	{
		String year =date.substring(0, 4);
		String month =date.substring(5, 7);
		String day =date.substring(8,10);
		String type="1";
		String fieldNamePrefix ="t";
		String fieldName ="";
		String fieldValue ="1";
		Row statusRow =new Row();
		for( int i=1; i<=48; i++)
		{
			fieldName =fieldNamePrefix+String.valueOf(i);
			statusRow.put(fieldName, fieldValue);
			fieldName ="";
		}
		statusRow.put("org_id", org_id);
		statusRow.put("room_id", room_id);
		statusRow.put("year", year);
		statusRow.put("month", month);
		statusRow.put("day", day);
		statusRow.put("type", type);
		String id =String.valueOf(getTableSequence("mcn_meeting_room_status", "id", 1));
		statusRow.put("id", id);
		insert("mcn_meeting_room_status",statusRow);
	}
	
	public Row queryRoomStatus(String room_id,String date)
	{
		String year =date.substring(0, 4);
		String month =date.substring(5, 7);
		String day =date.substring(8,10);
		Row row =new Row();
		sql =" select * from mcn_meeting_room_status where room_id='"+room_id+"' and year='"+year+"' and month='"+month+"' and day='"+day+"' ";
		row =queryRow(sql);
		return row;
	}
	//------------------------------mobile api------------------------------
	
	public static void main(String args[])
	{
		System.out.println("2014-09-12".substring(0, 4));
		System.out.println("2014-09-12".substring(5, 7));
		System.out.println("2014-09-12".substring(8,10));
	}
}
