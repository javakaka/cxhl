package com.fbsf.fzb.test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mcn.service.CompanyUser;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2015-3-20 下午4:35:51  
 * 类说明: 
 */
public class TxTest {

	private ApplicationContext ctx = null;
    
    @Before
    public void init() {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml"); 
    }
     
//    @Test
//    public void testSave() {
//    	TxService txService = (TxService)ctx.getBean("fzbTxTestService");
//    	txService.testTx();
//    }
    
//    @Test
//    public void testMakeGeographyFile() {
//    	TxService txService = (TxService)ctx.getBean("fzbTxTestService");
//    	txService.testTx();
//    }

}