/**
 * 
 */
package com.cxhl.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.Md5Util;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2014-12-26 下午3:14:51  
 * 类说明: 房租宝用户业务处理类，登陆、注册..
 */

@Component("fzbAgentService")
public class AgentService extends Service{

	public AgentService() {
		
	}

	/**
	 * mobile login 
	 * @return
	 */
	public Row login(String username)
	{
		Row row =null;
		String sSql =" select * from rent_agent where ( username='"+username+"'  or telephone='"+username+"' )";
		row =queryRow(sSql);
		return row;
	}
	
	public Row findByTelephone(String telephone)
	{
		Row row =null;
		String sSql =" select * from rent_agent where  telephone='"+telephone+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	public int findTotalNum()
	{
		int num =0;
		String sSql =" select count(*) from rent_agent ";
		num =Integer.parseInt(queryField(sSql));
		return num;
	}
	
	public Row findByUserName(String username)
	{
		Row row =null;
		String sSql =" select * from rent_agent where  username='"+username+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	public Row find(String id)
	{
		Row row =null;
		String sSql =" select * from rent_agent where  id='"+id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	public int insert(Row row)
	{
		int num =0;
		int id =getTableSequence("rent_agent", "id", 1);
		row.put("id", id);
		String time =DateUtil.getCurrentDateTime();
		row.put("create_time", time);
		num =insert("rent_agent", row);
		return num;
	}
	
	public int update(Row row)
	{
		int num =0;
		String id =row.getString("id",null);
		Assert.notNull(id);
		num =update("rent_agent", row, " id='"+id+"'");
		return num;
	}
	
	/**
	 * change password
	 * @param user_id
	 * @param oldPwd
	 * @param newPwd
	 * @return 
	 */
	public int changePassword(String user_id,String oldPwd,String newPwd)
	{
		int status =0;
		String sSql ="select * from rent_agent where id='"+user_id+"'";
		Row staff =queryRow(sSql);
		if(staff == null){
			status =1;// user not exist
		}
		else
		{
			String password =staff.getString("password","");
			if( !password.equals(oldPwd)){
				status =2;// oldpassword not correct
			}
			else 
			{
				sSql ="update rent_agent set password='"+newPwd+"' where id='"+user_id+"'";
				int rowNum = update(sSql);
				if(rowNum ==0 ){
					status =3;// update failure
				}
			}
		}
		return status;
	}
	
	public boolean isEmailExisted(String id,String email)
	{
		boolean existed =false;
		String sSql =" select count(*) from rent_agent where  email='"+email+"' and id !='"+id+"'";
		System.out.println("sql---------->"+sSql);
		String num =queryField(sSql);
		if(Integer.parseInt(num)>0)
		{
			existed =true;
		}
		return existed;
	}
	public boolean isEmailExisted(String email)
	{
		boolean existed =false;
		String sSql =" select count(*) from rent_agent where  email='"+email+"' ";
		String num =queryField(sSql);
		if(Integer.parseInt(num)>0)
		{
			existed =true;
		}
		return existed;
	}
	
	public boolean isPhoneExisted(String telephone)
	{
		boolean existed =false;
		String sSql =" select count(*) from rent_agent where  telephone='"+telephone+"' ";
		String num =queryField(sSql);
		if(Integer.parseInt(num)>0)
		{
			existed =true;
		}
		return existed;
	}
	
	public boolean isUsernameExisted(String id,String username)
	{
		boolean existed =false;
		String sSql =" select count(*) from rent_agent where  username='"+username+"' and id !='"+id+"'";
		String num =queryField(sSql);
		if(Integer.parseInt(num)>0)
		{
			existed =true;
		}
		return existed;
	}
	
	public Row findByEmail(String email)
	{
		Row row =null;
		String sSql =" select * from rent_agent where  email='"+email+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	/**
	 * 分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	@SuppressWarnings("unchecked")
	public Page queryPage() {
		Page page = null;
		Pageable pageable = (Pageable) row.get("pageable");
		sql = "select * from rent_agent where 1=1 ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from rent_agent where 1=1 ";
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
		if(dataSet != null && dataSet.size() > 0)
		{
			for(int i=0; i<dataSet.size(); i++)
			{
				Row temp =(Row)dataSet.get(i);
				try {
					String name =temp.getString("name","");
					if(! StringUtils.isEmptyOrNull(name))
					{
						name =AesUtil.decode(name);
						temp.put("name", name);
					}
					String bank_card_no =temp.getString("bank_card_no","");
					if(! StringUtils.isEmptyOrNull(bank_card_no))
					{
						bank_card_no =AesUtil.decode(bank_card_no);
						temp.put("bank_card_no", bank_card_no);
					}
				} catch (Exception e) {
					temp.put("name", "");
					temp.put("bank_card_no", "");
				}
				dataSet.set(i, temp);
			}
		}
		page = new Page(dataSet, total, pageable);
		return page;
	}
	
	@SuppressWarnings("unchecked")
	public DataSet queryTop5Agents() {
		DataSet dataSet=new DataSet();
		sql = "select * from rent_agent order by create_time desc limit 0,5  ";
		dataSet = queryDataSet(sql);
		if(dataSet != null && dataSet.size() > 0)
		{
			for(int i=0; i<dataSet.size(); i++)
			{
				Row temp =(Row)dataSet.get(i);
				try {
					String name =temp.getString("name","");
					if(! StringUtils.isEmptyOrNull(name))
					{
						name =AesUtil.decode(name);
						temp.put("name", name);
					}
					String bank_card_no =temp.getString("bank_card_no","");
					if(! StringUtils.isEmptyOrNull(bank_card_no))
					{
						bank_card_no =AesUtil.decode(bank_card_no);
						temp.put("bank_card_no", bank_card_no);
					}
				} catch (Exception e) {
					temp.put("name", "");
					temp.put("bank_card_no", "");
				}
				dataSet.set(i, temp);
			}
		}
		return dataSet;
	}
	
	/**
	 * 删除
	 * 
	 * @Title: delete
	 * @param @param ids
	 * @return void
	 */
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public void delete(Long... ids) {
		String id = "";
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				if (id.length() > 0) {
					id += ",";
				}
				id += "'" + String.valueOf(ids[i]) + "'";
			}
			sql = "delete from rent_agent where id in(" + id + ")";
			update(sql);
		}
	}
	
	/**
	 * reset pwd
	 * 
	 * @Title: delete
	 * @param @param ids
	 * @return void
	 */
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public void resetPassword(String id) {
		String password =Md5Util.Md5("000000");
		sql = "update rent_agent set password='"+password+"'  where id='"+id+"'";
		System.out.println("sql-------->>"+sql);
		update(sql);
	}
}