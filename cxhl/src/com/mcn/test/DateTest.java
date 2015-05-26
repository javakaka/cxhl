package com.mcn.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 * @version 创建时间：2015-1-8 下午6:19:10  
 * 类说明: 
 */
public class DateTest {

	public DateTest() {
	}
	
	public static Calendar getDateOfLastMonth(Calendar date) {  
	    Calendar lastDate = (Calendar) date.clone();  
	    lastDate.add(Calendar.MONTH, -1);  
	    return lastDate;  
	}  
	  
	public static Calendar getDateOfLastMonth(String dateStr) {  
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	    try {  
	        Date date = sdf.parse(dateStr);  
	        Calendar c = Calendar.getInstance();  
	        c.setTime(date);  
	        return getDateOfLastMonth(c);  
	    } catch (ParseException e) {  
	        throw new IllegalArgumentException("Invalid date format(yyyy-MM-dd): " + dateStr);  
	    }  
	}  
	  
	public static void main(String[] args) {  
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	    System.out.println(sdf.format(getDateOfLastMonth("2014-12-02").getTime()));  
//	    System.out.println(sdf.format(getDateOfLastMonth("2000-03-31").getTime()));  
	}

}
