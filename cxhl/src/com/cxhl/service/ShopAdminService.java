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
 */

@Component("cxhlShopAdminService")
public class ShopAdminService extends Service{

	public ShopAdminService() {
		
	}

	/**
	 * mobile login 
	 * @return
	 */
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row login(String username)
	{
		Row row =null;
		String sSql =" select * from cxhl_shop_admin where ( username='"+username+"'  or telephone='"+username+"' )";
		row =queryRow(sSql);
		return row;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row findByTelephone(String telephone)
	{
		Row row =null;
		String sSql =" select * from cxhl_shop_admin where  telephone='"+telephone+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public int getUserTotalNum()
	{
		int total=0;
		String sSql =" select count(*) from cxhl_shop_admin ";
		total =Integer.parseInt(queryField(sSql));
		return total;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row findByUserName(String username)
	{
		Row row =null;
		String sSql =" select * from cxhl_shop_admin where  username='"+username+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row findInviteCode(String invite_code)
	{
		Row row =null;
		String sSql =" select * from cxhl_shop_admin where invite_code='"+invite_code+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isInviteUserCodeExsited(String user_code)
	{
		boolean bool =false;
		String sSql =" select count(*) from cxhl_shop_admin where user_code='"+user_code+"' ";
		int num =Integer.parseInt(queryField(sSql));
		if(num > 0 )
		{
			bool =true;
		}
		return bool;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row findByEmail(String email)
	{
		Row row =null;
		String sSql =" select * from cxhl_shop_admin where  email='"+email+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row find(String id)
	{
		Row row =null;
		String sSql =" select * from cxhl_shop_admin where id='"+id+"' ";
		row =queryRow(sSql);
		return row;
	}
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public Row findByShopId(String shop_id)
	{
		Row row =null;
		String sSql =" select * from cxhl_shop_admin where shop_id='"+shop_id+"' ";
		row =queryRow(sSql);
		return row;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public int findTotalNum()
	{
		int num =0;
		String sSql =" select count(*) from cxhl_shop_admin ";
		num =Integer.parseInt(queryField(sSql));
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int insert(Row row)
	{
		int num =0;
		int id =getTableSequence("cxhl_shop_admin", "id", 1);
		row.put("id", id);
		row.put("create_time",DateUtil.getCurrentDateTime());
		num =insert("cxhl_shop_admin", row);
		return num;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int update(Row row)
	{
		int num =0;
		String id =row.getString("id",null);
		row.put("modify_time", DateUtil.getCurrentDateTime());
		Assert.notNull(id);
		System.out.print("user====>>"+row);
		num =update("cxhl_shop_admin", row, " id='"+id+"'");
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
		sql = "select * from cxhl_shop_admin where 1=1 ";
		String restrictions = addRestrictions(pageable);
		String orders = addOrders(pageable);
		sql += restrictions;
		sql += orders;
		String countSql = "select count(*) from cxhl_shop_admin where 1=1 ";
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
	
	@SuppressWarnings("unchecked")
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public DataSet queryLatestTop5Users() {
		DataSet dataSet =new DataSet();
		String sql="select * from cxhl_shop_admin order by create_time desc limit 0,5 ";
		dataSet =queryDataSet(sql);
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
		return dataSet;
	}
	
	/**
	 * change password
	 * @param user_id
	 * @param oldPwd
	 * @param newPwd
	 * @return 
	 */
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int changePassword(String user_id,String oldPwd,String newPwd)
	{
		int status =0;
		String sSql ="select * from cxhl_shop_admin where id='"+user_id+"'";
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
				sSql ="update cxhl_shop_admin set password='"+newPwd+"' where id='"+user_id+"'";
				int rowNum = update(sSql);
				if(rowNum ==0 ){
					status =3;// update failure
				}
			}
		}
		return status;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isEmailExisted(String id,String email)
	{
		boolean existed =false;
		String sSql =" select count(*) from cxhl_shop_admin where  email='"+email+"' and id !='"+id+"'";
		System.out.println("sql---------->"+sSql);
		String num =queryField(sSql);
		if(Integer.parseInt(num)>0)
		{
			existed =true;
		}
		return existed;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isUsernameExisted(String id,String username)
	{
		boolean existed =false;
		String sSql =" select count(*) from cxhl_shop_admin where  username='"+username+"' and id !='"+id+"'";
		String num =queryField(sSql);
		if(Integer.parseInt(num)>0)
		{
			existed =true;
		}
		return existed;
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
			sql = "delete from cxhl_shop_admin where id in(" + id + ")";
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
		sql = "update cxhl_shop_admin set password='"+password+"'  where id='"+id+"'";
		System.out.println("sql-------->>"+sql);
		update(sql);
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isTelephoneExisted(String telephone)
	{
		boolean bool =true;
		String sql ="select count(*) from cxhl_shop_admin where telephone ='"+telephone+"'";
		String count =queryField(sql);
		int sum =Integer.parseInt(count);
		if(sum >0)
			bool =false;
		else
			bool =true;
		return bool;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isUserNameExisted(String user_name)
	{
		boolean bool =true;
		String sql ="select count(*) from cxhl_shop_admin where username ='"+user_name+"'";
		String count =queryField(sql);
		int sum =Integer.parseInt(count);
		if(sum >0)
			bool =false;
		else
			bool =true;
		return bool;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isExtraUserNameExisted(String id, String user_name)
	{
		boolean bool =true;
		String sql ="select count(*) from cxhl_shop_admin where username ='"+user_name+"' and id !='"+id+"'";
		String count =queryField(sql);
		int sum =Integer.parseInt(count);
		if(sum >0)
			bool =false;
		else
			bool =true;
		return bool;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isEmailExisted(String email)
	{
		boolean bool =true;
		String sql ="select count(*) from cxhl_shop_admin where email ='"+email+"'";
		String count =queryField(sql);
		int sum =Integer.parseInt(count);
		if(sum >0)
			bool =false;
		else
			bool =true;
		return bool;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isInviteCodeExisted(String INVITE_CODE)
	{
		boolean bool =true;
		String sql ="select count(*) from cxhl_shop_admin where user_code ='"+INVITE_CODE+"'";
		String count =queryField(sql);
		int sum =Integer.parseInt(count);
		if(sum >0)
			bool =true;
		else
			bool =false;
		return bool;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isExtraEmailExisted(String id, String email)
	{
		boolean bool =true;
		String sql ="select count(*) from cxhl_shop_admin where email ='"+email+"' and id !='"+id+"'";
		String count =queryField(sql);
		int sum =Integer.parseInt(count);
		if(sum >0)
			bool =false;
		else
			bool =true;
		return bool;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isIdCardNoExisted(String id_card_no)
	{
		boolean bool =true;
		String sql ="select count(*) from cxhl_shop_admin where id_card_no ='"+id_card_no+"'";
		String count =queryField(sql);
		int sum =Integer.parseInt(count);
		if(sum >0)
			bool =false;
		else
			bool =true;
		return bool;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isExtraIdCardNoExisted(String id,String id_card_no)
	{
		boolean bool =true;
		String sql ="select count(*) from cxhl_shop_admin where id_card_no ='"+id_card_no+"' and id !='"+id+"'";
		String count =queryField(sql);
		int sum =Integer.parseInt(count);
		if(sum >0)
			bool =false;
		else
			bool =true;
		return bool;
	}
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isExtraBankCardNoExisted(String id,String bank_card_no)
	{
		boolean bool =true;
		String sql ="select count(*) from cxhl_shop_admin where bank_card_no ='"+bank_card_no+"' and id !='"+id+"'";
		System.out.println("sql--------->>"+sql);
		String count =queryField(sql);
		int sum =Integer.parseInt(count);
		if(sum >0)
			bool =false;
		else
			bool =true;
		return bool;
	}
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isExtraCreditCardNoExisted(String id,String credit_card_no)
	{
		boolean bool =true;
		String sql ="select count(*) from cxhl_shop_admin where credit_card_no ='"+credit_card_no+"' and id !='"+id+"'";
		System.out.println("sql--------->>"+sql);
		String count =queryField(sql);
		int sum =Integer.parseInt(count);
		if(sum >0)
			bool =false;
		else
			bool =true;
		return bool;
	}
	
	@Transactional(value="jdbcTransactionManager",readOnly = true)
	public boolean isIdCardNoIsExisted(String user_id,String id_card_no)
	{
		boolean bool =false;
		String sql ="select count(*) from cxhl_shop_admin where id !='"+user_id+"' and id_card_no='"+id_card_no+"' ";
		String count =queryField(sql);
		int sum =Integer.parseInt(count);
		if(sum >0)
			bool =true;
		else
			bool =false;
		return bool;
	}
}