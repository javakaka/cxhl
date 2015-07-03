package com.ezcloud.framework.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

import com.ezcloud.framework.plugin.pay.Unifiedorder;
import com.ezcloud.framework.vo.Row;

public class FieldUtil {

	public static String getObjectNotEmptyFieldsUrlParamsStr(Object obj)
	{
		ArrayList list = new ArrayList();
		Row row =new Row();
		String str ="";
		Field fields[] =null;
		fields =obj.getClass().getDeclaredFields();
		for(int i=0;i<fields.length;i++)
		{
			fields[i].setAccessible(true);
			String field_type =fields[i].getType().toString();
//			System.out.println("field_type>>"+field_type);
			try {
				String value =(String)fields[i].get(obj);
				String name =fields[i].getName();
				if(! StringUtils.isEmptyOrNull(value))
				{
					row.put(name, value);
					list.add(name);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
//		System.out.println("before sort -----");
//		for(int i=0;i<list.size();i++)
//		{
//			System.out.println("--"+list.get(i));
//		}
		Collections.sort(list);
//		System.out.println("after sort -----");
		for(int i=0;i<list.size();i++)
		{
			String sort_name =(String)list.get(i);
			String value =row.getString(sort_name);
			if(str.length()>0)
			{
				str +="&"+sort_name+"="+value;
			}
			else
			{
				str +=sort_name+"="+value;
			}
		}
		return str;
	}
	
	public static String appendFiledToUrlParam(String urlParam,String name,String value)
	{
		if(!StringUtils.isEmptyOrNull(name) && !StringUtils.isEmptyOrNull(value))
		{
			if(!StringUtils.isEmptyOrNull(urlParam))
			{
				urlParam +="&"+name+"="+value;
			}
			else
			{
				urlParam =name+"="+value;
			}
		}
		return urlParam;
	}
	public static void main(String[] args) {
		Unifiedorder obj =new Unifiedorder();
		obj.setAppid("123");
		obj.setMch_id("12312");
		obj.setBody("ahskdhaksd");
		String str =FieldUtil.getObjectNotEmptyFieldsUrlParamsStr(obj);
		System.out.println("str====>>"+str);
	}
}
