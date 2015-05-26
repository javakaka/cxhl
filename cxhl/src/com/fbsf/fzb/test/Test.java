package com.fbsf.fzb.test;

import java.io.File;
import java.math.BigDecimal;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2015-2-10 下午2:36:08  
 * 类说明: 
 */
public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

//	public static void main(String[] args) {
////		String ss="2015-02";
////		System.out.println(ss.substring(0,4));
////		System.out.println(ss.substring(5,7));
////		String cc="fgsjdf\"ahdjkad\"983172";
////		cc =cc.replaceAll("\"", "\\\\\"");
////		System.out.println("cc==============>>"+cc);
////		String dir =System.getProperty("user.dir");
//		System.out.println("cc==============>>"+DateUtil.getCurrentDateTime());
//	}
	
//  public static void main(String[] args) {
//		String name=OSUtil.getOSName();
//		System.out.println(name);
//		System.out.println(Md5Util.Md5("pppppp"));
//	}
  
  public static void main(String[] args) throws Exception {
//		String money ="1000.0";
//		System.out.println("money---->>"+NumberUtils.getTwoDecimal(money));
//		BigDecimal moneyBig =new BigDecimal(NumberUtils.getTwoDecimal(money));
//		moneyBig.setScale(2, BigDecimal.ROUND_HALF_UP);
//		System.out.println("dmoney---->>"+moneyBig.doubleValue());
//		double dmoney =moneyBig.doubleValue();
//		System.out.println("dmoney---->>"+dmoney);
//		double ss =Double.valueOf("1000.00");
//		ss =ss*100;
//		System.out.println("ss---->>"+ss);
//		System.out.println("ss i---->>"+(int)ss);
//		java.text.DecimalFormat df =new java.text.DecimalFormat("#.00");
//		System.out.println("ss---->>"+df.format(ss));
//	  	String now_day="2015-04-15 00:00:00";
//	  	String period_end_date="2015-04-20 00:00:00";
//		long days =DateUtil.getDayMinusOfTwoTime(now_day, period_end_date);
//		System.out.println("ss---->>"+days);
//		System.out.println("ss-www--->>"+AesUtil.decode("NmCI1JQpdEEfj/EA/Krheg=="));
//		System.out.println("ss-www--->>"+AesUtil.decode("cIKNVQpS+o3COJJ47wy2VQ=="));
//		long minus =DateUtil.getMinuteMinusOfTwoTime("2015-05-07 16:00:00", "2015-05-07 16:04:03");
//		System.out.println("ss-minus--->>"+minus);
//		System.out.println("ss-minus--->>"+"12345".indexOf("1"));
//		String amount ="1";
//		double am=Double.parseDouble(amount)/100;
//		String amount2=String.valueOf(am);
//		System.out.println("ss-am--->>"+am);
//		System.out.println("ss-amount2--->>"+amount2);
//		BigDecimal moneyBig =new BigDecimal(NumberUtils.getTwoDecimal("0.01")).setScale(2, BigDecimal.ROUND_HALF_UP);
//		double money =moneyBig.doubleValue();
//		System.out.println("ss-NumberUtils--->>"+NumberUtils.getTwoDecimalByDecimalFormat(money));
		long minuss =DateUtil.getMinuteMinusOfTwoTime("2015-05-10 10:00:00", "2015-05-10 10:30:00");
		System.out.println("ss-minuss--->>"+minuss);
	}
}
