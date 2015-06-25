package com.cxhl.service;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2015-3-20 下午3:25:26  
 * 类说明: 
 */
@Component("fzbTxTestService")
public class TxService {

	@Resource(name = "jdbcTemplate")
	protected JdbcTemplate jdbcTemplate;
	

	@Transactional(value="jdbcTransactionManager",propagation=Propagation.REQUIRED)
	public void testTx()
	{
		System.out.println("begin.............");
		String sql ="insert into sm_test (id,name) values ('1','tong') ";
		jdbcTemplate.execute(sql);
		System.out.println("tx.............");
		sql ="insert into sm_test (id,name) values ('2','cc') ";
		jdbcTemplate.execute(sql);
		System.out.println(1/0);//exp
		System.out.println("roll back.............");
	}
	
}
