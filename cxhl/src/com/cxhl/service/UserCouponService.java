/**
 * 
 */
package com.cxhl.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
 * 类说明: order item
 */

@Component("cxhlUserCouponService")
public class UserCouponService extends Service{

	public UserCouponService() {
		
	}
	
	/**
	 * @param id
	 * @return
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row find(String id)
	{
		Row row =null;
		String sSql ="select a.id,a.type,a.c_name as shop_name,a.link_name,a.link_tel,a.longitude, "
				+" a.latitude,a.star,a.address,a.detail,a.average_cost from cxhl_user_coupon a  "
				+" where a.id='"+id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	public Row findByUserIdAndCouponId(String user_id,String coupon_id)
	{
		Row row =null;
		String sSql ="select a.id,a.type,a.c_name as shop_name,a.link_name,a.link_tel,a.longitude, "
				+" a.latitude,a.star,a.address,a.detail,a.average_cost from cxhl_user_coupon a  "
				+" where a.user_id='"+user_id+"' and coupon_id='"+coupon_id+"' limit 0,1";
		row =queryRow(sSql);
		return row;
	}
	
	public Row findByPayCode(String pay_code)
	{
		Row row =null;
		String sSql ="select * from cxhl_user_coupon   "
				+" where pay_code='"+pay_code+"' ";
		row =queryRow(sSql);
		return row;
	}
	public Row findNotUsedByPayCode(String pay_code)
	{
		Row row =null;
		String sSql ="select * from cxhl_user_coupon   "
				+" where pay_code='"+pay_code+"' and state='1' ";
		row =queryRow(sSql);
		return row;
	}
	/**
	 * @param id
	 * @return
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public DataSet findOrderItems(String order_id)
	{
		DataSet ds =new DataSet();
		String sSql ="select a.*,b.`name` from cxhl_user_coupon a "
				+" left join cxhl_coupon b on a.coupon_id=b.id  "
				+" where a.order_id='"+order_id+"' ";
		ds =queryDataSet(sSql);
		return ds;
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
		String sSql =" select * from cxhl_user_coupon where user_id='"+user_id+"' and c_id='"+shop_id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int insert(Row row)
	{
		int num =0;
		int id =getTableSequence("cxhl_user_coupon", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		num =insert("cxhl_user_coupon", row);
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int update(Row row)
	{
		int num =0;
		String id =row.getString("id",null);
		row.put("modify_time", DateUtil.getCurrentDateTime());
		num =update("cxhl_user_coupon", row, " id='"+id+"'");
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int minusUserCouponNum(String user_id,String coupon_id,String minus_num)
	{
		int num =0;
		row.put("modify_time", DateUtil.getCurrentDateTime());
		String sql ="update cxhl_user_coupon set num=num-"+minus_num +" where user_id='"+user_id+"' and coupon_id='"+coupon_id+"' ";
		num =update(sql);
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int increaseUserCouponNum(String user_id,String coupon_id,String minus_num)
	{
		int num =0;
		row.put("modify_time", DateUtil.getCurrentDateTime());
		String sql ="update cxhl_user_coupon set num=num+"+minus_num +" where user_id='"+user_id+"' and coupon_id='"+coupon_id+"' ";
		num =update(sql);
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
		sql ="select * from "
		+" ( "
		+" select a.* ,b.telephone ,b.`name` as username,c.`name` as coupon_name,d.c_name as shop_name "
		+" from cxhl_user_coupon a  "
		+" left join cxhl_users b on a.user_id=b.id "
		+" left join cxhl_coupon c on a.coupon_id=c.id "
		+" left join cxhl_shop d on c.shop_id=d.id "
		+" ) as tab  "
		+" where 1=1 ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql ="select count(*) from "
				+" ( "
				+" select a.* ,b.telephone ,b.`name` as username,c.`name` as coupon_name,d.c_name as shop_name "
				+" from cxhl_user_coupon a  "
				+" left join cxhl_users b on a.user_id=b.id "
				+" left join cxhl_coupon c on a.coupon_id=c.id "
				+" left join cxhl_shop d on c.shop_id=d.id "
				+" ) as tab  "
				+" where 1=1 ";
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
	
	public Page queryPageForShopAdmin(Pageable pageable,String shop_id) {
		Page page = null;
		sql ="select * from "
		+" (  "
		+" select a.* ,b.telephone ,b.`name` as username,c.`name` as coupon_name,d.c_name as shop_name " 
		+" from cxhl_user_coupon a   "
		+" left join cxhl_users b on a.user_id=b.id " 
		+" left join cxhl_coupon c on a.coupon_id=c.id " 
		+" left join cxhl_shop d on c.shop_id=d.id  "
		+" where coupon_id in (select id from cxhl_coupon where shop_id='"+shop_id+"') "
		+" ) as tab   "
		+" where 1=1 ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql ="select count(*) from "
				+" (  "
				+" select a.* ,b.telephone ,b.`name` as username,c.`name` as coupon_name,d.c_name as shop_name " 
				+" from cxhl_user_coupon a   "
				+" left join cxhl_users b on a.user_id=b.id " 
				+" left join cxhl_coupon c on a.coupon_id=c.id " 
				+" left join cxhl_shop d on c.shop_id=d.id  "
				+" where coupon_id in (select id from cxhl_coupon where shop_id='"+shop_id+"') "
				+" ) as tab   "
				+" where 1=1 ";
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
	
	
	public DataSet list(String user_id,String state,String page,String page_size)
	{
		int iStart =(Integer.parseInt(page)-1)*Integer.parseInt(page_size);
		DataSet ds =new DataSet();
		String sSql ="select a.*,c.FILE_PATH from cxhl_user_coupon a "
				+" left join file_attach_control b on a.coupon_id=b.DEAL_CODE and b.DEAL_TYPE='coupon_icon' "
				+" left join file_attach_upload c on c.CONTROL_ID=b.CONTROL_ID "
				+" where a.user_id='"+user_id+"' ";
		if( !StringUtils.isEmptyOrNull(state))
		{
			sSql +=" and a.state='"+state+"' ";
		}
		sSql +=" limit "+iStart+" , "+page_size;	
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
				Row row =(Row)ds.get(i);
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
			sql = "delete from cxhl_user_coupon where id in(" + id + ")";
			update(sql);
		}
	}
	
	/**
	 * 生成支付验证码
	 * @return
	 */
	public String getPayCode()
	{
		String code ="";
		String random_code ="";
		String time =DateUtil.getCurrentDateTime().replace(" ", "").replace(":", "").replace("-", "").substring(0, 4);
		int rowNum =0;
		do
		{
			int rand = (int)(Math.random()*10000000);
			random_code =String.valueOf(rand);
			code =time+random_code;
			String sql ="select count(*) from cxhl_user_coupon where pay_code='"+code+"'";
			rowNum =Integer.parseInt(queryField(sql));
		}
		while(rowNum >0);
		return code;
	}
}