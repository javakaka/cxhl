package com.cxhl.test;

import com.ezcloud.utility.DateUtil;

public class Test {

	public static void main(String[] args) {
		System.out.println("----");
		String ss ="D:/WORK_SYSTEM/TOMCAT/apache-tomcat-7.0.53/webapps/fangzubao/resources/fangzubao_app_file/3/wCXnXKh1ede8.apk";
		int iPos =ss.indexOf("resources");
		ss =ss.substring(iPos);
		System.out.println("----"+ss);
		System.out.println("----"+DateUtil.getCurrentDateTime().replace("-", "").replace(" ", "").replace(":", ""));
	}
}
