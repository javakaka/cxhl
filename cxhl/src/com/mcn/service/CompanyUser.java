package com.mcn.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

@Component("companyUserService")
public class CompanyUser extends Service{

	public void delete (String org_id)
	{
		String sSql ="delete from mcn_users where org_id='"+org_id+"'";
		update(sSql);
	}
	
	public DataSet selectAll(String org_id)
	{
		DataSet ds=new DataSet();
		String sSql="select * from mcn_users where org_id='"+org_id+"'";
		ds =queryDataSet(sSql);
		return ds;
	}
	
	/**
	 * mobile login 
	 * @return
	 */
	public Row login(String org_id ,String username)
	{
		Row row =null;
		String sSql =" select * from mcn_users where ( username='"+username+"'  or telephone='"+username+"' ) and org_id ='"+org_id+"'";
		row =queryRow(sSql);
		return row;
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
		String sSql ="select * from mcn_users where id='"+user_id+"'";
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
				sSql ="update mcn_users set password='"+newPwd+"' where id='"+user_id+"'";
				int rowNum = update(sSql);
				if(rowNum ==0 ){
					status =3;// update failure
				}
			}
		}
		return status;
	}
	
}
