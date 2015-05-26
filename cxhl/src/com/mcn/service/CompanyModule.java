package com.mcn.service;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**
 * 企业功能选项
 * @author JianBoTong
 *
 */
@Component("adminCompanyModuleService")
public class CompanyModule  extends Service{

	public void save(Row row )
	{
		int id =getTableSequence("mcn_org_modules", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		System.out.print("row===>>"+row);
		insert("mcn_org_modules",row);
	}
	
	public Row edit(String id)
	{
		Row module =new Row();
		String sSql ="select * from mcn_org_modules where org_id='"+id+"'";
		module =queryRow(sSql);
		return module;
	}
	
	public void update(Row row,String org_id)
	{
		String sSql ="update mcn_org_modules set punch='0' ,reimburse='0', task='0', meeting_room='0' where org_id='"+org_id+"'";
		update(sSql);
		row.put("modify_time", DateUtil.getCurrentDateTime());
		update("mcn_org_modules", row, " org_id ='"+org_id+"'");
		
	}

}
