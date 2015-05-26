package com.fbsf.fzb.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2015-3-24 下午4:57:50  
 * 类说明: 
 */
public class ChangeData {
	
//	private static String URL="jdbc:mysql://localhost:3306/fangzubao";
//	private static String USER_NAME="root";
//	private static String PASSWORD="root";
	private static String URL="jdbc:mysql://113.105.76.195:53306/fangzubao";
	private static String USER_NAME="root";
	private static String PASSWORD="yaoyaole";
	
	public static void changeUsers() throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
		Statement stmt =conn.createStatement();
		String sql ="select * from rent_users ";
		ResultSet rs =stmt.executeQuery(sql);
		DataSet ds =new DataSet();
		Row row =null;
		while(rs.next())
		{
			row =new Row();
			String name =rs.getString("name");
			String id =rs.getString("id");
			row.put("id", id);
			if(! StringUtils.isEmptyOrNull(name))
			{
				name =AesUtil.encode(name);
				row.put("name", name);
			}
			String bank_card_no =rs.getString("bank_card_no");
			if(! StringUtils.isEmptyOrNull(bank_card_no))
			{
				bank_card_no =AesUtil.encode(bank_card_no);
				row.put("bank_card_no", bank_card_no);
			}
			String credit_card_no =rs.getString("credit_card_no");
			if(! StringUtils.isEmptyOrNull(credit_card_no))
			{
				credit_card_no =AesUtil.encode(credit_card_no);
				row.put("credit_card_no", credit_card_no);
			}
			
			ds.add(row);
		}
		rs.close();
		stmt.close();
		stmt =conn.createStatement();
		for(int i=0; i< ds.size(); i++)
		{
			Row temp =(Row)ds.get(i);
			String id =temp.getString("id");
			String name =temp.getString("name");
			String bank_card_no =temp.getString("bank_card_no");
			String credit_card_no =temp.getString("credit_card_no");
			String ssql ="update rent_users set ";
			boolean bname =false;
			boolean bbank =false;
			boolean bcredit =false;
			if(! StringUtils.isEmptyOrNull(name))
			{
				ssql +=" name='"+name+"' ";
				bname =true;
			}
			if(! StringUtils.isEmptyOrNull(bank_card_no))
			{
				if(bname)
				{
					ssql +=" ,";
				}
				ssql +=" bank_card_no='"+bank_card_no+"' ";
				bbank =true;
			}
			if(! StringUtils.isEmptyOrNull(credit_card_no))
			{
				if(bbank)
				{
					ssql +=" ,";
				}
				else
				{
					if(bname)
					{
						ssql +=" ,";
					}
				}
				ssql +=" credit_card_no='"+credit_card_no+"' ";
				bcredit =true;
			}
			ssql +=" where id='"+id+"'";
			System.out.println("sql ========>>"+ssql);
			if(bname || bbank || bcredit)
			{
				stmt.executeUpdate(ssql);
			}
		}
	}
	
	public static void changeAgents() throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
		Statement stmt =conn.createStatement();
		String sql ="select * from rent_agent ";
		ResultSet rs =stmt.executeQuery(sql);
		DataSet ds =new DataSet();
		Row row =null;
		while(rs.next())
		{
			row =new Row();
			String name =rs.getString("name");
			String id =rs.getString("id");
			row.put("id", id);
			if(! StringUtils.isEmptyOrNull(name))
			{
				name =AesUtil.encode(name);
				row.put("name", name);
			}
			String bank_card_no =rs.getString("bank_card_no");
			if(! StringUtils.isEmptyOrNull(bank_card_no))
			{
				bank_card_no =AesUtil.encode(bank_card_no);
				row.put("bank_card_no", bank_card_no);
			}
			ds.add(row);
		}
		rs.close();
		stmt.close();
		stmt =conn.createStatement();
		for(int i=0; i< ds.size(); i++)
		{
			Row temp =(Row)ds.get(i);
			String id =temp.getString("id");
			String name =temp.getString("name");
			String bank_card_no =temp.getString("bank_card_no");
			String ssql ="update rent_agent set ";
			boolean bname =false;
			boolean bbank =false;
			if(! StringUtils.isEmptyOrNull(name))
			{
				ssql +=" name='"+name+"' ";
				bname =true;
			}
			if(! StringUtils.isEmptyOrNull(bank_card_no))
			{
				if(bname)
				{
					ssql +=" ,";
				}
				ssql +=" bank_card_no='"+bank_card_no+"' ";
				bbank =true;
			}
			ssql +=" where id='"+id+"'";
			System.out.println("sql ========>>"+ssql);
			if(bname || bbank )
			{
				stmt.executeUpdate(ssql);
			}
		}
	}
	
	public static void main(String[] args) throws Exception{
		
//		changeUsers();
//		changeAgents();
	}
}
