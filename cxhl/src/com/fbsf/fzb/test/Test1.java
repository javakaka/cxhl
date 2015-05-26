package com.fbsf.fzb.test;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.VOConvert;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2015-3-31 下午3:19:40  
 * 类说明: 
 */
public class Test1 {

	public Test1() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws JException {
		String sovo ="{\"HEADER\":{\"VERSION\":\"4.0\",\"COMPANY\":\"中国时刻\",\"COPYRIGHT\":\"2010-2020\"},\"RETURN\":{\"CODE\":\"0\",\"MSG\":\"\",\"EXP\":\"\"},\"RESPONSE\":{\"DEFAULT\":{\"PROVINCE.SUM\":\"1\"},\"DATASET\":{\"NAME\":\"PROVINCE\",\"ROWS\":\"1\",\"VALUE\":[{\"NAME\":\"广东省\",\"ID\":\"19\"}]}}}";
		OVO ovo =VOConvert.jsonToOvo(sovo);
		System.out.println(ovo.oForm);
		DataSet ds =(DataSet)ovo.get("PROVINCE");
		System.out.println("ds:"+ds);
	}
}
