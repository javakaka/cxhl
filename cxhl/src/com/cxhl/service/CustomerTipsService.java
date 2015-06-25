package com.cxhl.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2015-1-1 下午4:24:59  
 * 类说明:在线客服用户反馈业务处理类 
 */
@Component("cxhlCustomerTipsService")
public class CustomerTipsService extends Service {

	public CustomerTipsService() 
	{
		
	}

	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int insert(Row row) 
	{
		int rowNum =0;
		int id =getTableSequence("cxhl_customer_tips", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		rowNum =insert("cxhl_customer_tips", row);
		return rowNum;
	}
	
	@Transactional(value="jdbcTransactionManager")
	public Row findById(String id)
	{
		Row row =null;
		sql ="select * from cxhl_customer_tips where id='"+id+"'";
		row =queryRow(sql);
		return row;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int update(Row row)
	{
		int rowNum =0;
		String id=row.getString("id");
		row.put("modify_time", DateUtil.getCurrentDateTime());
		update("cxhl_customer_tips", row, " id='"+id+"'");
		return rowNum;
	}
	
	@Transactional(value="jdbcTransactionManager")
	public DataSet list(int page,int page_size,String user_id)
	{
		DataSet ds =new DataSet();
		int iStart =(page-1)*page_size;
		sql =" select * from cxhl_customer_tips where user_id='"+user_id+"' "
			 +" LIMIT "+iStart+","+page_size+" ";
		ds =queryDataSet(sql);
		return ds;
	}
	
	/**
	 * 根据用户的编号查询此用户收到的所有私信
	 * @param user_id
	 * @return
	 */
	public int queryAllReceivedLetterNumByUserId(String user_id)
	{
		int num =0;
		String sql ="select count(*) from cxhl_customer_tips where to_id='"+user_id+"'";
		num =Integer.parseInt(queryField(sql));
		return num;
	}
	
	/**
	 * 根据用户的编号查询此用户发出去的所有私信
	 * @param user_id
	 * @return
	 */
	public int queryAllSendLetterNumByUserId(String user_id)
	{
		int num =0;
		String sql ="select count(*) from cxhl_customer_tips where from_id='"+user_id+"'";
		num =Integer.parseInt(queryField(sql));
		return num;
	}
	
	/**
	 * 根据用户的编号查询此用户收到的未读的私信数量
	 * @param user_id
	 * @return
	 */
	public int queryNotReadLetterNumByUserId(String user_id)
	{
		int num =0;
		String sql ="select count(*) from cxhl_customer_tips where to_id='"+user_id+"' and read_status='2' ";
		num =Integer.parseInt(queryField(sql));
		return num;
	}
	
	/**
	 * 根据用户的编号查询此用户收到的已读的私信数量
	 * @param user_id
	 * @return
	 */
	public int queryReadedLetterNumByUserId(String user_id)
	{
		int num =0;
		String sql ="select count(*) from cxhl_customer_tips where to_id='"+user_id+"'  and read_status='1' ";
		num =Integer.parseInt(queryField(sql));
		return num;
	}

	
	/*********************************管理后台**********************************/
	/**
	 * 分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	public Page queryPage(String status,Pageable pageable) {
		Page page = null;
		sql ="select * from cxhl_customer_tips where 1=1 "; 
		if(! StringUtils.isEmptyOrNull(status))
		{
			sql +=" and status='"+status+"' ";
		}
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql ="select count(*) from cxhl_customer_tips where 1=1 "; 
		if(! StringUtils.isEmptyOrNull(status))
		{
			countSql +=" and status='"+status+"' ";
		}
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
			sql = "delete from cxhl_customer_tips where id in(" + id + ")";
			update(sql);
		}
	}
	
	/*********************************管理后台**********************************/
}