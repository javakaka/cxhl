/**
 * 
 */
package com.cxhl.service;

import java.util.Random;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2014-12-26 下午3:14:51  
 * 类说明: 中奖记录
 */

@Component("cxhlLotteryRecordService")
public class LotteryRecordService extends Service{

	public LotteryRecordService() {
		
	}
	
	/**
	 * @param id
	 * @return
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row find(String id)
	{
		Row row =null;
		String sSql ="select * from cxhl_lottery_record  where id='"+id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int insert(Row row)
	{
		int num =0;
		int id =getTableSequence("cxhl_lottery_record", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		num =insert("cxhl_lottery_record", row);
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int update(Row row)
	{
		int num =0;
		String id =row.getString("id",null);
		row.put("modify_time", DateUtil.getCurrentDateTime());
		Assert.notNull(id);
		num =update("cxhl_lottery_record", row, " id='"+id+"'");
		return num;
	}
	
	
	/**
	 * 分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Page queryPage() {
		Page page = null;
		Pageable pageable = (Pageable) row.get("pageable");
		sql ="select * from "
				+" ( "
				+" select a.*,b.`name`,c.c_name as shop_name from cxhl_lottery_record  a  "
				+" left join cxhl_gift b on a.gift_id=b.id "
				+" left join cxhl_shop c on b.shop_id=c.id "
				+" ) as tab   where 1=1 ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql ="select count(*) from "
				+" ( "
				+" select a.*,b.`name`,c.c_name as shop_name from cxhl_lottery_record  a  "
				+" left join cxhl_gift b on a.gift_id=b.id "
				+" left join cxhl_shop c on b.shop_id=c.id "
				+" ) as tab   where 1=1 ";
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
	 * 用户分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public DataSet queryUserList(String user_id,int page,int page_size) 
	{
		DataSet ds =new DataSet();
		int iSatrt =(page -1)*page_size;
		sql ="select id, user_id ,lottery_type,is_win,reward_type,reward_id,create_time from cxhl_lottery_record where user_id='"+user_id+"' "
				+ "order by create_time desc limit "+iSatrt+", "+page_size;
		ds = queryDataSet(sql);
		return ds;
	}
	
	/**
	 * 删除
	 * 
	 * @Title: delete
	 * @param @param ids
	 * @return void
	 */
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public void delete(String... ids) {
		String id = "";
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				if (id.length() > 0) {
					id += ",";
				}
				id += "'" + String.valueOf(ids[i]) + "'";
			}
			sql = "delete from cxhl_lottery_record where id in(" + id + ")";
			update(sql);
		}
	}
	
	//当日中奖次数
	public int getCurrentDayRewardNum(String year,String month,String day) {
		int total =0;
		String sql =" select count(*) from cxhl_lottery_record where year='"+year+"' and month='"+month+"' and day='"+day+"' and is_win='1' ";
		total =Integer.parseInt(queryField(sql));
		return total;
	}
	//当月中奖次数
	public int getCurrentMonthRewardNum(String year,String month) {
		int total =0;
		String sql =" select count(*) from cxhl_lottery_record where year='"+year+"' "
				+ "and month='"+month+"' and is_win='1' ";
		total =Integer.parseInt(queryField(sql));
		return total;
	}
	//当周中奖次数
	public int getCurrentWeekRewardNum(String year,String month,String week) {
		int total =0;
		String sql =" select count(*) from cxhl_lottery_record where year='"+year+"' "
				+ "and month='"+month+"' and week='"+week+"' and is_win='1' ";
		total =Integer.parseInt(queryField(sql));
		return total;
	}
	
	
	public static void main(String[] args) {
		String cur_date =DateUtil.getCurrentDateTime();
		String year =cur_date.substring(0, 4);
		String month =cur_date.substring(5, 7);
		String day =cur_date.substring(8, 10);
		System.out.println("1:"+year+"2:"+month+"3:"+day);
		Random random =null;
		random =new Random();
		int lottery_result =random.nextInt(2);
		System.out.println("lottery_result===========>>"+lottery_result);
	}
	
	
}