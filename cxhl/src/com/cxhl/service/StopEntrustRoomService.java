/**
 * 
 */
package com.cxhl.service;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2014-12-26 下午3:14:51  
 * 类说明: 终止租房委托
 */

@Component("fzbStopEntrustRoomService")
public class StopEntrustRoomService extends Service{

	public StopEntrustRoomService() {
		
	}

	public Row find(String id)
	{
		Row row =null;
		String sSql =" select * from rent_request_stop_entrust where  id='"+id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	public int insert(Row row)
	{
		int num =0;
		int id =getTableSequence("rent_request_stop_entrust", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		num =insert("rent_request_stop_entrust", row);
		return num;
	}
	
	public int update(Row row)
	{
		int num =0;
		String id =row.getString("id",null);
		Assert.notNull(id);
		num =update("rent_request_stop_entrust", row, " id='"+id+"'");
		return num;
	}
	
	
}