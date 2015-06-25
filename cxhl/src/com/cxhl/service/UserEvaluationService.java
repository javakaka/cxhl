package com.cxhl.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
 * 类说明:房东租客评价业务处理类 
 */
@Component("fzbEvaluationService")
public class UserEvaluationService extends Service {

	public UserEvaluationService() 
	{
		
	}

	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int insert(Row row) 
	{
		int rowNum =0;
		int id =getTableSequence("rent_evaluation", "id", 1);
		row.put("id", id);
		row.put("create_time", DateUtil.getCurrentDateTime());
		rowNum =insert("rent_evaluation", row);
		return rowNum;
	}
	
	@Transactional(value="jdbcTransactionManager")
	public Row findById(String id)
	{
		Row row =null;
		sql ="select * from rent_evaluation where id='"+id+"'";
		row =queryRow(sql);
		return row;
	}
	
	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public int update(Row row)
	{
		int rowNum =0;
		String id=row.getString("id");
		row.put("modify_time", DateUtil.getCurrentDateTime());
		update("rent_evaluation", row, " id='"+id+"'");
		return rowNum;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(value="jdbcTransactionManager")
	public DataSet list(int page,int page_size,String name,String id_card_no,String type)
	{
		DataSet ds =new DataSet();
		int iStart =(page-1)*page_size;
		sql =" select a.content ,a.create_time,a.from_id, " 
			+" b.`name` FROM rent_evaluation a "
			+" left join rent_users b on a.from_id=b.id "
			+" where a.to_id in  "
			+" ( "
			+" SELECT id from rent_users  where id_card_no='"+id_card_no+"' "
			+" and name='"+name+"' "
			+" ) and a.type='"+type+"' "
			+" ORDER BY a.create_time DESC " 
			+" LIMIT "+iStart+","+page_size +" ";
		ds =queryDataSet(sql);
		if( ds != null && ds.size() > 0 )
		{
			for( int i=0;i<ds.size(); i++)
			{
				Row temp =(Row)ds.get(i);
				String uname =temp.getString("name","");
				String content =temp.getString("content","");
				if(! StringUtils.isEmptyOrNull(uname))
				{
					try {
						uname =AesUtil.decode(uname);
					} catch (Exception e) {
						uname ="";
					}
					temp.put("name", uname);
				}
				if(! StringUtils.isEmptyOrNull(content))
				{
					content =StringUtils.string2Json(content);
					temp.put("content", content);
				}
				ds.set(i, temp);
			}
		}
		return ds;
	}
	
	/*********************************管理后台**********************************/
	/**
	 * 分页查询
	 * 
	 * @Title: queryPage
	 * @return Page
	 */
	public Page queryPage(Pageable pageable) {
		Page page = null;
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
			sql = "delete from rent_evaluation where id in(" + id + ")";
			update(sql);
		}
	}
	
	/*********************************管理后台**********************************/
}