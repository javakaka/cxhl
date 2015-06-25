package com.cxhl.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2015-1-1 下午4:24:59  
 * 类说明:规则消息业务处理类 
 */
@Component("fzbRuleMessageService")
public class RuleMessageService extends Service {

	public RuleMessageService() 
	{
		
	}

	public int insert(Row row) 
	{
		int rowNum =0;
		int id =getTableSequence("rent_rule_message", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		rowNum =insert("rent_rule_message", row);
		return rowNum;
	}
	
	public Row findById(String id)
	{
		Row row =null;
		sql ="select * from rent_rule_message where id='"+id+"'";
		row =queryRow(sql);
		return row;
	}
	
	public int update(Row row)
	{
		int rowNum =0;
		String id=row.getString("id");
		row.put("modify_time", DateUtil.getCurrentDateTime());
		update("rent_rule_message", row, " id='"+id+"'");
		return rowNum;
	}
	
	/*********************************管理后台**********************************/
	/**
	 * 分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	public Page queryPage(Pageable pageable) {
		Page page = null;
		sql ="select * from rent_rule_message  where 1=1 "; 
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql ="select count(*) from rent_rule_message  where 1=1 "; 
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
			sql = "delete from rent_rule_message where id in(" + id + ")";
			update(sql);
		}
	}
	
	/*********************************管理后台**********************************/
}