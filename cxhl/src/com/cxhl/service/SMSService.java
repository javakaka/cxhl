package com.cxhl.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2015-1-1 下午4:24:59  
 * 类说明:发短信业务处理类 
 */
@Component("cxhlSMSService")
public class SMSService extends Service {

	public SMSService() 
	{
		
	}

	public int insert(Row row) throws JException
	{
		int rowNum =0;
		int id =getTableSequence("cxhl_sms", "id", 1);
		row.put("id", id);
		row.put("send_time", DateUtil.getCurrentDateTime());
		rowNum =insert("cxhl_sms", row);
		return rowNum;
	}
	
	
	public boolean findByCodeAndTelphone(String code,String telphone)
	{
		boolean existed =true;
		sql ="select count(*) from cxhl_sms where sms_code='"+code+"' and to_account='"+telphone+"' and status='1' ";
		int num =Integer.parseInt(queryField(sql));
		if(num == 0)
		{
			existed =false;
		}
		return existed;
	}
	
	public boolean findByCodeAndTelphone(String code,String telphone,int type)
	{
		boolean existed =true;
		sql ="select count(*) from cxhl_sms where sms_code='"+code+"' and to_account='"+telphone+"' and status='1'  and type='"+type+"'";
		int num =Integer.parseInt(queryField(sql));
		if(num == 0)
		{
			existed =false;
		}
		return existed;
	}
	
	public int findCodeNumByTelphone(String telephone)
	{
		int num =0;
		sql ="select count(*) from cxhl_sms where to_account='"+telephone+"' and status='1' ";
		num =Integer.parseInt(queryField(sql));
		return num;
	}
	
	public void setCodeTimeOut()
	{
		//将发送成功，时间超过5分钟，还未使用的记录的状态设置为已过期
		String sql ="select * from cxhl_sms where status='1' ";
		DataSet ds =queryDataSet(sql);
		if(ds !=null && ds.size()>0)
		{
			String now_time =DateUtil.getCurrentDateTime();
			for(int i=0;i<ds.size();i++)
			{
				Row temp =(Row)ds.get(i);
				String send_time =temp.getString("send_time",DateUtil.getCurrentDateTime());
				long minus =DateUtil.getMinuteMinusOfTwoTime(send_time, now_time);
				if(minus >=5)
				{
					String id =temp.getString("id");
					String t_sql ="update cxhl_sms set status='2' where id='"+id+"' ";
					update(t_sql);
				}
			}
		}
	}
	
	public Row getLastedSms(String telephone)
	{
		Row row =null;
		String sql ="select * from cxhl_sms where to_account='"+telephone+"' and status='1' order by send_time desc limit 0,1";
		row =queryRow(sql);
		return row;
	}
	
	public void setSmsTimeOut(String telephone)
	{
		String sql ="update cxhl_sms set status='2' where to_account='"+telephone+"' and status='1'";
		update(sql);
	}
	
	public DataSet list(String user_id,int page,int page_size)
	{
		DataSet ds =new DataSet();
		return ds;
	}
	
	
}