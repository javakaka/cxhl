package com.mcn.test;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.vo.IVO;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.Base64Util;
import com.ezcloud.utility.DateUtil;
import com.ezcloud.utility.NetUtil;
import sun.misc.BASE64Decoder;  
import sun.misc.BASE64Encoder;
public class TestApi {
//	String url ="http://211.154.151.145:8080/mcn/api/action/changePassword.do";
	
	// login 
	public static void login()
	{
//		String url ="http://localhost:8080/mcn/api/action/login.do";
		String url ="http://211.154.151.145:8080/mcn/api/action/login.do";
		IVO ivo =new IVO();
		   try {
//			ivo.set("username", "1001");
			ivo.set("username", "13629900136");
			ivo.set("password", "123456");
			ivo.set("token", "nQd9zWGtvBeGWqdnZ%2F9ZfQ%3D%3D");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.print("ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("========================================\n");
			System.out.print(res);
		   } catch (JException e) {
				e.printStackTrace();
			}
	}
	
	// change password
	public static void changePwd()
	{
		String url ="http://localhost:8080/mcn/api/action/changePassword.do";
		IVO ivo =new IVO();
		   try {
			ivo.set("user_id", "1");
			ivo.set("oldPwd", "123456");
			ivo.set("newPwd", "000000");
			ivo.set("token", "nQd9zWGtvBeGWqdnZ%2F9ZfQ%3D%3D");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("========================================\n");
			System.out.print(res);
		   } catch (JException e) {
				e.printStackTrace();
			}
	}
	
	// room list
	public static void getRoomList()
	{
//		String url ="http://localhost:8080/mcn/api/room/list.do";
		String url ="http://211.154.151.145:8080/mcn/api/room/list.do";
		IVO ivo =new IVO();
		   try {
			ivo.set("page", "1");
			ivo.set("pageSize", "10");
			ivo.set("token", "nQd9zWGtvBeGWqdnZ%2F9ZfQ%3D%3D");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("========================================\n");
			System.out.print(res);
		   } catch (JException e) {
				e.printStackTrace();
			}
	}
	
	// room  status
	public static void getRoomStatus()
	{
		String url ="http://localhost:8080/mcn/api/room/room_status.do";
//			String url ="http://211.154.151.145:8080/mcn/api/room/room_status.do";
		IVO ivo =new IVO();
		   try {
			ivo.set("id", "1");
			ivo.set("date", DateUtil.getCurrentDate());
			ivo.set("token", "nQd9zWGtvBeGWqdnZ%2F9ZfQ%3D%3D");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("========================================\n");
			System.out.print(res);
		   } catch (JException e) {
				e.printStackTrace();
			}
	}
	
	// book room 
	public static void bookRoom()
	{
		String url ="http://localhost:8080/mcn/api/room/book_room.do";
//		String url ="http://211.154.151.145:8080/mcn/api/room/room_status.do";
		IVO ivo =new IVO();
		   try {
			ivo.set("user_id", "1");
			ivo.set("room_id", "1");
			ivo.set("start_time", "2014-09-14 10:00:00");
			ivo.set("end_time", "2014-09-14 11:00:00");
			ivo.set("date", DateUtil.getCurrentDate());
			ivo.set("use_for", "use");
			ivo.set("meeters", "meeters");
			ivo.set("remark", "remark");
			ivo.set("token", "nQd9zWGtvBeGWqdnZ%2F9ZfQ%3D%3D");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("========================================\n");
			System.out.print(res);
		   } catch (JException e) {
				e.printStackTrace();
			}
	}
	
	
	// 查询人员的预定记录
	public static void queryUserBookList()
	{
//		String url ="http://localhost:8080/mcn/api/room/userbooklist.do";
		String url ="http://211.154.151.145:8080/mcn/api/room/userbooklist.do";
		IVO ivo =new IVO();
		   try {
			ivo.set("id", "1");
			ivo.set("token", "nQd9zWGtvBeGWqdnZ%2F9ZfQ%3D%3D");
			String json =  VOConvert.ivoToJson(ivo);
			System.out.println("\n ivo to json ====>>"+json);
			String res =NetUtil.getNetResponse(url, json,"UTF-8");
			System.out.println("========================================\n");
			System.out.print(res);
		   } catch (JException e) {
				e.printStackTrace();
			}
	}
	
	
	// 请假
		public static void mobileleaveAdd()
		{
			String url ="http://localhost:8080/mcn/api/leave/add.do";
//			String url ="http://211.154.151.145:8080/mcn/api/leave/add.do";
			IVO ivo =new IVO();
			   try {
				ivo.set("id", "2");
				ivo.set("token", "nQd9zWGtvBeGWqdnZ%2F9ZfQ%3D%3D");
				ivo.set("leave_type","1");
				ivo.set("start_date","2014-10-15");
				ivo.set("end_date","2014-10-16");
				ivo.set("reason", "调休1天");
				
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		// 查询自己的请假列表
		public static void querySelfLeaveList()
		{
//			String url ="http://localhost:8080/mcn/api/leave/selfList.do";
			String url ="http://211.154.151.145:8080/mcn/api/leave/selfList.do";
			IVO ivo =new IVO();
			   try {
				ivo.set("id", "1");
				ivo.set("token", "nQd9zWGtvBeGWqdnZ%2F9ZfQ%3D%3D");
				ivo.set("page","1");
				ivo.set("page_size","10");
				
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		// 分页查询属下的请假列表
		public static void queryFollowerLeaveList()
		{
//			String url ="http://localhost:8080/mcn/api/leave/followerList.do";
			String url ="http://211.154.151.145:8080/mcn/api/leave/followerList.do";
			IVO ivo =new IVO();
			   try {
				ivo.set("id", "1");
				ivo.set("token", "nQd9zWGtvBeGWqdnZ%2F9ZfQ%3D%3D");
				ivo.set("page","1");
				ivo.set("page_size","10");
				
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		// 审核请假
		public static void auditLeave()
		{
//			String url ="http://localhost:8080/mcn/api/leave/audit.do";
			String url ="http://211.154.151.145:8080/mcn/api/leave/audit.do";
			IVO ivo =new IVO();
			   try {
				ivo.set("id", "1");
				ivo.set("token", "nQd9zWGtvBeGWqdnZ%2F9ZfQ%3D%3D");
				ivo.set("log_id","2");
				ivo.set("status","2");
				ivo.set("audit_objection","同意请假");
				
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		
		// 查询企业的部门和人员
		public static void queryCompanySitesAndUsers()
		{
//			String url ="http://localhost:8080/mcn/api/company/all.do";
			String url ="http://211.154.151.145:8080/mcn/api/company/all.do";
			IVO ivo =new IVO();
			   try {
				ivo.set("token", "nQd9zWGtvBeGWqdnZ%2F9ZfQ%3D%3D");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		//查询打卡时间
		public static void queryPunchTimes()
		{
//			String url ="http://localhost:8080/mcn/api/punch/times.do";
			String url ="http://211.154.151.145:8080/mcn/api/punch/times.do";
			IVO ivo =new IVO();
			   try {
				ivo.set("token", "nQd9zWGtvBeGWqdnZ%2F9ZfQ%3D%3D");
				ivo.set("id", "1");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		
		//打卡
		public static void punch()
		{
//			String url ="http://localhost:8080/mcn/api/punch/add.do";
			String url ="http://211.154.151.145:8080/mcn/api/punch/add.do";
			IVO ivo =new IVO();
			   try {
				ivo.set("token", "nQd9zWGtvBeGWqdnZ%2F9ZfQ%3D%3D");
				ivo.set("id", "1");
				ivo.set("punch_type", "4");
				ivo.set("punch_time", "2014-10-19 19:00:00");
				ivo.set("LONGITUDE", "1");
				ivo.set("LATITUDE ", "1");
				ivo.set("PLACE_NAME", "罗湖");
				String pictrue = Base64Util.GetImageStr("/Users/JianBoTong/Downloads/1111.jpg");
				ivo.set("picture", pictrue);
//				ivo.set("picture_name", "1001");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
		
		//根据人员编号和月份查询打卡记录
		public static void queryUserMonthPunchLog()
		{
//			String url ="http://localhost:8080/mcn/api/punch/user_month_log.do";
			String url ="http://211.154.151.145:8080/mcn/api/punch/user_month_log.do";
			IVO ivo =new IVO();
			   try {
				ivo.set("token", "nQd9zWGtvBeGWqdnZ%2F9ZfQ%3D%3D");
				ivo.set("id", "1");
				ivo.set("date", "2014-10");
				ivo.set("page", "1");
				ivo.set("page_size", "10");
				String json =  VOConvert.ivoToJson(ivo);
				System.out.println("\n ivo to json ====>>"+json);
				String res =NetUtil.getNetResponse(url, json,"UTF-8");
				System.out.println("========================================\n");
				System.out.print(res);
			   } catch (JException e) {
					e.printStackTrace();
				}
		}
				
		
	 public static void main(String[] args)  {
		 //登陆
//		 login();
		 //修改密码
//		 changePwd();
		// room list
//		getRoomList();
		 //会议室状态
//		getRoomStatus();
		 //预定会议室
//		 bookRoom();
		// 查询人员的预定记录
//		queryUserBookList();
		// 请假
//		mobileleaveAdd();
		// 查询自己的请假列表
//		querySelfLeaveList();
		// 分页查询属下的请假列表
//		 queryFollowerLeaveList();
//		 审核请假
//			auditLeave();
//		 查询企业的部门和人员
//		 queryCompanySitesAndUsers();
//		查询打卡时间
//		queryPunchTimes();
//		打卡
		punch();
//		根据人员编号和月份查询打卡记录
//		queryUserMonthPunchLog();
	 }
	 }
