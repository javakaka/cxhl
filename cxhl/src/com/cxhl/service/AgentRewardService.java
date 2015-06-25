/**
 * 
 */
package com.cxhl.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2014-12-26 下午3:14:51  
 * 类说明: 中介奖励
 */

@Component("fzbAgentRewardService")
public class AgentRewardService extends Service{

	public AgentRewardService() {
		
	}

	/**
	 * 
	 * @return
	 */
	public DataSet queryPage(String user_id,int page,int page_size)
	{
		DataSet ds =null;
		int start =(page -1)*page_size;
		String sSql ="select id,left(date,7) as month,price from rent_agent_reward where user_id='"+user_id+"' order by create_time desc "
		+" limit "+start+","+page_size;
		ds =queryDataSet(sSql);
		return ds;
	}
	
	/**
	 * 根据用户编号，查询用户的奖金总额
	 * @return
	 */
	public String queryTotalReward(String user_id)
	{
		String total ="0.00";
		String sSql ="select sum(price) from rent_agent_reward where user_id='"+user_id+"'";
		total =queryField(sSql);
		if(StringUtils.isEmptyOrNull(total))
		{
			total ="0.00";
		}
		total =NumberUtils.getTwoDecimal(total);
		return total;
	}
	
	/**
	 * 根据用户编号，查询用户当月的奖金总额
	 * @return
	 */
	public String queryMonthReward(String user_id,String cur_month)
	{
		String total ="0.00";
		String year =cur_month.substring(0,4);
		String month =cur_month.substring(5,7);
		String sSql ="select sum(price) from rent_agent_reward where user_id='"+user_id+"' and year='"+year+"' and month='"+month+"';";
		total =queryField(sSql);
		if(StringUtils.isEmptyOrNull(total))
		{
			total ="0.00";
		}
		total =NumberUtils.getTwoDecimal(total);
		return total;
	}
	
	
	/*********************************管理后台**********************************/
	/**
	 * 分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public Page queryPage(String status,Pageable pageable) throws Exception {
		Page page = null;
		sql ="select * from ( "
		+" select a.*,b.`name` as user_name,b.telephone  from rent_agent_reward a " 
		+" left join rent_agent b on a.user_id=b.id "
		+" ) as tab where 1=1 "; 
		if(! StringUtils.isEmptyOrNull(status))
		{
			sql +=" and status='"+status+"' ";
		}
		//对中介姓名搜索字段加密
		String search_pro =pageable.getSearchProperty();
		String search_value =pageable.getSearchValue();
		if(! StringUtils.isEmptyOrNull(search_pro))
		{
			if(search_pro.equalsIgnoreCase("USER_NAME"))
			{
				if(! StringUtils.isEmptyOrNull(search_value))
				{
					search_value =AesUtil.encode(search_value);
					pageable.setSearchValue(search_value);
				}
			}
		}
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql ="select count(*) from ( "
				+" select a.*,b.`name` as user_name,b.telephone  from rent_agent_reward a " 
				+" left join rent_agent b on a.user_id=b.id "
				+" ) as tab   where 1=1 "; 
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
		if(dataSet != null && dataSet.size() >0)
		{
			for(int i=0;i<dataSet.size();i++)
			{
				Row temp =(Row)dataSet.get(i);
				String user_name =temp.getString("user_name","");
				if(! StringUtils.isEmptyOrNull(user_name))
				{
					try {
						user_name =AesUtil.decode(user_name);
					} catch (Exception e) {
						user_name ="";
					}
					temp.put("user_name", user_name);
				}
				dataSet.set(i, temp);
			}
		}
		search_pro =pageable.getSearchProperty();
		search_value =pageable.getSearchValue();
		if(! StringUtils.isEmptyOrNull(search_pro))
		{
			if(search_pro.equalsIgnoreCase("USER_NAME"))
			{
				if(! StringUtils.isEmptyOrNull(search_value))
				{
					search_value =AesUtil.decode(search_value);
					pageable.setSearchValue(search_value);
				}
			}
		}
		page = new Page(dataSet, total, pageable);
		return page;
	}
	
	public int insert(Row row)
	{
		int rowNum =0;
		int id=getTableSequence("rent_agent_reward", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		rowNum =insert("rent_agent_reward", row);
		return rowNum;
	}
	
	public Row find(String id)
	{
		Row row =null;
		String sql ="select a.*,b.`name` as username from rent_agent_reward a ,rent_agent b  where a.id='"+id+"' and a.user_id=b.id ";
		row =queryRow(sql);
		return row;
	}
	
	public int update(Row row)
	{
		int rowNum =0;
		String id=row.getString("id","");
		String modify_time =DateUtil.getCurrentDateTime();
		row.put("modify_time", modify_time);
		rowNum = update("rent_agent_reward", row, " id='"+id+"'");
		return rowNum;
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
			sql = "delete from rent_agent_reward where id in(" + id + ")";
			update(sql);
		}
	}
	
	/*********************************管理后台**********************************/
}