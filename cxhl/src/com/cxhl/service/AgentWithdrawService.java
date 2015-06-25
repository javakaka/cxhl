package com.cxhl.service;

import org.springframework.stereotype.Component;

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
 * 类说明:广告业务处理类 
 */
@Component("fzbWithdrawService")
public class AgentWithdrawService extends Service {

	public AgentWithdrawService() 
	{
		
	}

	public int insert(Row row) 
	{
		int rowNum =0;
		int id =getTableSequence("rent_withdraw", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		rowNum =insert("rent_withdraw", row);
		return rowNum;
	}
	
	public Row findById(String id)
	{
		Row row =null;
		sql ="select a.*,b.name,b.telephone from rent_withdraw a left join rent_agent b on a.user_id=b.id where a.id='"+id+"'";
		row =queryRow(sql);
		return row;
	}
	
	
	public int update(Row row)
	{
		int rowNum =0;
		String id=row.getString("id");
		row.put("modify_time", DateUtil.getCurrentDateTime());
		update("rent_withdraw", row, " id='"+id+"'");
		return rowNum;
	}
	
	public DataSet list()
	{
		DataSet ds =new DataSet();
		String now_time =DateUtil.getCurrentDateTime();
		sql ="select id,name,picture,width,height from rent_withdraw where status=1 and (start_time<='"+now_time+"' and end_time>='"+now_time+"')";
		ds =queryDataSet(sql);
		return ds;
	}
	
	public DataSet getPageListByUserId(String id,String page ,String page_size)
	{
		DataSet ds =new DataSet();
		int iStart =(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		sql ="select id,(0-money) as money,status,remark,create_time,settle_money from rent_withdraw where user_id='"+id+"' order by create_time desc limit "+iStart+" ,"+page_size;
		ds =queryDataSet(sql);
		return ds;
	}
	
	/**
	 * 查询用户的所有已提现成功的金额
	 * @return
	 */
	public double queryWithdrawSuccessTotalMoney(String user_id)
	{
		double money =0;
		String sql ="select sum(money) from rent_withdraw where user_id='"+user_id+"' and status in ('3','4') ";
		String sum =queryField(sql);
		if(StringUtils.isEmptyOrNull(sum))
		{
			money =0;
		}
		else
		{
			money =Double.parseDouble(sum);
		}
		return money;
	}
	
	/**
	 * 查询用户的所有申请提现中的金额
	 * @return
	 */
	public double queryWithdrawProcessingTotalMoney(String user_id)
	{
		double money =0;
		String sql ="select sum(money) from rent_withdraw where user_id='"+user_id+"' and status='1' ";
		String sum =queryField(sql);
		if(StringUtils.isEmptyOrNull(sum))
		{
			money =0;
		}
		else
		{
			money =Double.parseDouble(sum);
		}
		return money;
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
	public Page queryPage(String user_id,String status, Pageable pageable) throws Exception {
		Page page = null;
		sql ="select * from (select a.*,b.name,b.telephone from rent_withdraw a left join rent_agent b on a.user_id=b.id ) as tab where 1=1 "; 
		if(! StringUtils.isEmptyOrNull(status))
		{
			sql +=" and status='"+status+"' ";
		}
		if(! StringUtils.isEmptyOrNull(user_id))
		{
			sql +=" and user_id='"+user_id+"' ";
		}
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		sql+=" order by create_time desc ";
		String countSql ="select count(*) from (select a.*,b.name,b.telephone from rent_withdraw a left join rent_agent b on a.user_id=b.id ) as tab where 1=1 "; 
		if(! StringUtils.isEmptyOrNull(status))
		{
			countSql +=" and status='"+status+"' ";
		}
		if(! StringUtils.isEmptyOrNull(user_id))
		{
			countSql +=" and user_id='"+user_id+"' ";
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
		//decode agent name field
		for(int i=0; i<dataSet.size(); i++)
		{
			Row temp =(Row)dataSet.get(i);
			String name =temp.getString("name","");
			if(! StringUtils.isEmptyOrNull(name))
			{
				name =AesUtil.decode(name);
				temp.put("name", name);
				dataSet.set(i, temp);
			}
		}
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
			sql = "delete from rent_withdraw where id in (" + id + ")";
			update(sql);
		}
	}
	
	/*********************************管理后台**********************************/
}