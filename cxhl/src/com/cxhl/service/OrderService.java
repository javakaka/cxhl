/**
 * 
 */
package com.cxhl.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ezcloud.framework.common.Setting;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.SettingUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2014-12-26 下午3:14:51  
 * 类说明: order 
 */

@Component("cxhlOrderService")
public class OrderService extends Service{

	public OrderService() {
		
	}

	
	/**
	 * 点击进入商家详情
	 * @param id
	 * @return
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row find(String id)
	{
		Row row =null;
		String sSql ="select a.id,a.type,a.c_name as shop_name,a.link_name,a.link_tel,a.longitude, "
				+" a.latitude,a.star,a.address,a.detail,a.average_cost from cxhl_order a  "
				+" where a.id='"+id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	/**
	 * 点击进入商家详情
	 * @param id
	 * @return
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public DataSet findShopPicture(String id)
	{
		DataSet ds =new DataSet();
		Row row =null;
		String sSql ="select c.FILE_PATH from file_attach_control b "
				+" left join file_attach_upload c on b.CONTROL_ID=c.CONTROL_ID "
				+" where b.DEAL_TYPE='shop_detail_top_picture' and b.DEAL_CODE='"+id+"' ";
		ds =queryDataSet(sSql);
		Setting setting =SettingUtils.get();
		String url =setting.getSiteUrl();
		if(StringUtils.isEmptyOrNull(url))
		{
			url ="";
		}
		if(ds != null && ds.size() >0 )
		{
			for(int i=0;i<ds.size(); i++)
			{
				row =(Row)ds.get(i);
				String file_path =row.getString("file_path","");
				int iPos =-1;
				if(!StringUtils.isEmptyOrNull(file_path))
				{
					iPos =file_path.indexOf("resources");
					if(iPos != -1)
					{
						file_path =url+"/"+file_path.substring(iPos);
					}
				}
				else
				{
					file_path ="";
				}
				row.put("file_path", file_path);
				ds.set(i, row);
			}
		}
		return ds;
	}
	
	public Row findByUserIdAndShopId(String user_id,String shop_id)
	{
		Row row =null;
		String sSql =" select * from cxhl_order where user_id='"+user_id+"' and c_id='"+shop_id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int insert(Row row)
	{
		int num =0;
		int id =getTableSequence("cxhl_order", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		num =insert("cxhl_order", row);
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int update(Row row)
	{
		int num =0;
		String id =row.getString("id",null);
		row.put("modify_time", DateUtil.getCurrentDateTime());
		Assert.notNull(id);
		num =update("cxhl_order", row, " id='"+id+"'");
		return num;
	}
	
	
	/**
	 * 分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	@SuppressWarnings("unchecked")
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Page queryPage() {
		Page page = null;
		Pageable pageable = (Pageable) row.get("pageable");
		sql = "select * from cxhl_order where 1=1 ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from cxhl_order where 1=1 ";
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
		if(dataSet != null && dataSet.size()>0)
		{
			for(int i=0; i<dataSet.size(); i++)
			{
				Row temp =(Row)dataSet.get(i);
				String name =temp.getString("name","");
				String bank_card_no =temp.getString("bank_card_no","");
				String credit_card_no =temp.getString("credit_card_no","");
				try {
					if(! StringUtils.isEmptyOrNull(name))
					{
						name =AesUtil.decode(name);
					}
					if(! StringUtils.isEmptyOrNull(bank_card_no))
					{
						bank_card_no =AesUtil.decode(bank_card_no);
					}
					if(! StringUtils.isEmptyOrNull(credit_card_no))
					{
						credit_card_no =AesUtil.decode(credit_card_no);
					}
				} catch (Exception e) {
					name="";
					bank_card_no="";
					credit_card_no="";
				}
				temp.put("name", name);
				temp.put("bank_card_no", bank_card_no);
				temp.put("credit_card_no", credit_card_no);
				dataSet.set(i, temp);
			}
		}
		page = new Page(dataSet, total, pageable);
		return page;
	}
	
	
	public DataSet list(String user_id,String state,String page,String page_size)
	{
		int iStart =(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		
		DataSet ds =new DataSet();
		String sSql ="select * from cxhl_order "
				+" where user_id='"+user_id+"' " ;
		if( !StringUtils.isEmptyOrNull(state))
		{
			sSql +=" and state='"+state+"' ";
		}
		sSql +=" order by create_time desc ";
		sSql +=" limit "+iStart+" , "+page_size;	
		ds =queryDataSet(sSql);
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
			sql = "delete from cxhl_order where id in(" + id + ")";
			update(sql);
		}
	}
	
	//精确到秒的订单数量
	public int getOrderNumByCreateTime(String time)
	{
		int num =0;
		String sql ="select count(*) from cxhl_order where create_time='"+time+"' ";
		num =Integer.parseInt(queryField(sql));
		return num;
	}
}