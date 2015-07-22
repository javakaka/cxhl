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
import com.ezcloud.framework.util.SettingUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2014-12-26 下午3:14:51  
 * 类说明: 商家优惠券
 */

@Component("cxhlLotteryCouponService")
public class LotteryCouponService extends Service{

	public LotteryCouponService() {
		
	}

	
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row find(String id)
	{
		Row row =null;
		String sSql ="select * from cxhl_lottery_coupon  "
				+" where id='"+id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isExisted(String id)
	{
		boolean bool =true;
		String sSql ="select count(*) from cxhl_lottery_coupon  "
				+" where id='"+id+"' ";
		int num =Integer.parseInt(queryField(sSql));
		if(num == 0)
		{
			bool =false;
		}
		return bool;
	}
	
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row findRemark(String id)
	{
		Row row =null;
		String sSql ="select remark from cxhl_lottery_coupon  "
				+" where id='"+id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int insert(Row row)
	{
		int num =0;
		int id =getTableSequence("cxhl_lottery_coupon", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		num =insert("cxhl_lottery_coupon", row);
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int update(Row row)
	{
		int num =0;
		String id =row.getString("id",null);
		row.put("modify_time", DateUtil.getCurrentDateTime());
		Assert.notNull(id);
		num =update("cxhl_lottery_coupon", row, " id='"+id+"'");
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
		+" select a.*,b.`name`,c.c_name as shop_name  from cxhl_lottery_coupon a " 
		+" left join cxhl_coupon b on a.coupon_id=b.id "
		+" left join cxhl_shop c on b.shop_id=c.id "
		+" ) as tab  where 1=1 ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql ="select count(*) from "
				+" ( "
				+" select a.*,b.`name`,c.c_name as shop_name  from cxhl_lottery_coupon a " 
				+" left join cxhl_coupon b on a.coupon_id=b.id "
				+" left join cxhl_shop c on b.shop_id=c.id "
				+" ) as tab  where 1=1 ";
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
	
	
	public DataSet list(String shop_id,String page,String page_size)
	{
		int iStart =(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		
		DataSet ds =new DataSet();
//		String sSql ="select id,coupon_no,shop_id,name,summary,raw_price,coupon_price,coupon_unit,left_num"
//				+ " from cxhl_lottery_coupon where shop_id='"+shop_id+"' and left_num > 0 ";
		String sSql ="select id,coupon_no,shop_id,name,summary, "
				+" raw_price,coupon_price,coupon_unit,left_num,c.FILE_PATH "
		+" from cxhl_lottery_coupon a "
		+" left join file_attach_control b on b.DEAL_CODE=a.id and b.DEAL_TYPE='coupon_icon' "
		+" left join file_attach_upload c on b.CONTROL_ID=c.CONTROL_ID "
		+" where shop_id='1' and left_num > 0 ";
		sSql +=" limit "+iStart+" , "+page_size;	
		ds =queryDataSet(sSql);
		Setting setting =SettingUtils.get();
		String site_url =setting.getSiteUrl();
		if(ds != null && ds.size() >0 )
		{
			for(int i=0;i<ds.size(); i++)
			{
				Row row =(Row)ds.get(i);
				String file_path =row.getString("file_path","");
				int iPos =-1;
				if(!StringUtils.isEmptyOrNull(file_path))
				{
					iPos =file_path.indexOf("resources");
					if(iPos != -1)
					{
						file_path =file_path.substring(iPos);
						file_path =site_url+"/"+file_path;
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
			sql = "delete from cxhl_lottery_coupon where id in(" + id + ")";
			update(sql);
		}
	}
	
	/**
	 * 查询可参与抽奖的优惠券列表
	 * @param shop_id
	 * @param page
	 * @param page_size
	 * @return
	 */
	public DataSet getCanLotteryCouponList(String year,String month)
	{
		
		DataSet ds =new DataSet();
		String sSql ="select * from cxhl_lottery_coupon  where left_num > 0 ";
		if(!StringUtils.isEmptyOrNull(year))
		{
			sSql +="and year='"+year+"' ";
		}
		if(!StringUtils.isEmptyOrNull(month))
		{
			sSql +=" and month='"+month+"' ";
		}
		ds =queryDataSet(sSql);
		return ds;
	}
}