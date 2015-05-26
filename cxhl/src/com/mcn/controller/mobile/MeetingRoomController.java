package com.mcn.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.DateUtil;
import com.mcn.service.MeetingRoom;
import com.mcn.service.MeetingRoomBook;
import com.mcn.service.MeetingRoomStatus;

/**
 * 手机端会议室接口
 * @author JianBoTong
 *
 */
@Controller("mobileMeetingRoomController")
@RequestMapping("/api/room")
public class MeetingRoomController extends BaseController{
	@Resource(name = "meetingRoomService")
	private  MeetingRoom roomService;
	
	@Resource(name = "meetingRoomStatusService")
	private   MeetingRoomStatus meetingRoomStatusService;
	
	@Resource(name = "meetingRoomBookService")
	private   MeetingRoomBook roomBookService;
	
	
	@RequestMapping("/list")
	public @ResponseBody String getMobileroomList(HttpServletRequest request) throws JException
	{
		String json ="";
		parseRequest(request);
		String token =ivo.getString("token",null);
		String page =ivo.getString("page","1");
		String pageSize =ivo.getString("pageSize","10");
		DataSet ds =roomService.getMobileRoomPage(token, page, pageSize);
		ovo.set("room_list", ds);
		json =VOConvert.ovoToJson(ovo);
		return json;
	} 
	
	//获取会议室的预定时间状态，24小时，分48个时间段，每个时间段有0，1两个状态，0已预定1未预定
	// id
	@RequestMapping("/room_status")
	public @ResponseBody String getRoomTimeStatus(HttpServletRequest request) throws JException
	{
		String json ="";
		parseRequest(request);
		String token =ivo.getString("token",null);
		String id =ivo.getString("id",null);
		String date =ivo.getString("date",DateUtil.getCurrentDate());
//		String curDate =DateUtil.getCurrentDate();
		Row statusRow = meetingRoomStatusService.queryRoomStatus(id, date);
		if(statusRow == null)
		{
			meetingRoomStatusService.initRoomStatus(token, id, date);
		}
		statusRow = meetingRoomStatusService.queryRoomStatus(id, date);
		ovo =new OVO();
		ovo.set("status_row", statusRow);
		json =VOConvert.ovoToJson(ovo);
		return json;
	} 
	
	//预定会议室
	@RequestMapping("/book_room")
	public @ResponseBody String bookRoom(HttpServletRequest request) throws JException
	{
		String json ="";
		parseRequest(request);
		String token =ivo.getString("token",null);
		if(token == null)
		{
			ovo =new OVO(-20000,"token不能为空","token不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		String user_id =ivo.getString("user_id",null);//预定者编号,yyyy-mm-dd hh:mm:ss
		String room_id =ivo.getString("room_id",null);//会议室编号,yyyy-mm-dd hh:mm:ss
		String start_time =ivo.getString("start_time",null);
		if(start_time == null)
		{
			ovo =new OVO(-20001,"开始时间不能为空","开始时间不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		String end_time =ivo.getString("end_time",null);
		if(end_time == null)
		{
			ovo =new OVO(-20002,"结束时间不能为空","结束时间不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		String date =ivo.getString("date",null);//yyyy-mm-dd
		if(date == null)
		{
			ovo =new OVO(-20003,"预定日期date不能为空","预定日期date不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		String use_for =ivo.getString("use_for","");
		String meeters =ivo.getString("meeters","");
		String remark =ivo.getString("remark","");
		String status ="1";
		Row bookRow =new Row();
		bookRow.put("book_user_id",user_id );
		bookRow.put("org_id",token );
		bookRow.put("room_id",room_id );
		bookRow.put("start_time",start_time );
		bookRow.put("end_time",end_time );
		bookRow.put("use_for",use_for );
		bookRow.put("meeters",meeters );
		bookRow.put("remark",remark );
		bookRow.put("year",date.substring(0, 4) );
		bookRow.put("month",date.substring(5, 7) );
		bookRow.put("day",date.substring(8,10) );
		bookRow.put("status",status);
		bookRow.put("create_time",DateUtil.getCurrentDateTime());
		int rowNum = roomBookService.bookRoom(bookRow);
		if(rowNum !=1)
		{
			ovo =new OVO(-20004,"预定失败","预定失败");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		ovo =new OVO(0,"预定成功","");
		json =VOConvert.ovoToJson(ovo);
		return json;
	} 
	
	//根据人员编号查询预定记录
	// id
	@RequestMapping("/userbooklist")
	public @ResponseBody String queryUserBooklist(HttpServletRequest request) throws JException
	{
		String json ="";
		parseRequest(request);
//		String token =ivo.getString("token",null);
		String id =ivo.getString("id",null);
		if(id == null)
		{
			ovo =new OVO(-20005,"人员编号:id不能为空","人员编号:id不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		DataSet bookDs = roomBookService.queryUserBooklist(id);
		ovo =new OVO();
		ovo.set("book_list", bookDs);
		json =VOConvert.ovoToJson(ovo);
		return json;
	} 
}
