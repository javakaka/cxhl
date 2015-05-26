package com.mcn.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.DateUtil;
import com.mcn.service.LeaveService;
import com.mcn.service.MeetingRoomBook;
import com.mcn.service.MeetingRoomStatus;

/**
 * 手机端请假接口
 * @author JianBoTong
 *
 */
@Controller("mobileLeaveController")
@RequestMapping("/api/leave")
public class LeaveController extends BaseController{
	@Resource(name = "mcnLeaveService")
	private  LeaveService leaveService;
	
//	@RequestMapping("/list")
//	public @ResponseBody String getMobileroomList(HttpServletRequest request) throws JException
//	{
//		String json ="";
//		parseRequest(request);
//		String token =ivo.getString("token",null);
//		String page =ivo.getString("page","1");
//		String pageSize =ivo.getString("pageSize","10");
//		DataSet ds =leaveService.getMobileRoomPage(token, page, pageSize);
//		ovo.set("room_list", ds);
//		json =VOConvert.ovoToJson(ovo);
//		return json;
//	} 
//	
	
	
	//新增请假记录
	// id
	@RequestMapping("/add")
	public @ResponseBody String addLeave(HttpServletRequest request) throws JException
	{
		String json ="";
		parseRequest(request);
		String token =ivo.getString("token",null);
		String id =ivo.getString("id",null);
		if(id == null)
		{
			ovo =new OVO(-20005,"人员编号:id不能为空","人员编号:id不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		if(token == null)
		{
			ovo =new OVO(-20006,"token不能为空","token不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		String leave_type=ivo.getString("leave_type","1");
		String start_date=ivo.getString("start_date",null);
		String end_date=ivo.getString("end_date",null);
		String reason=ivo.getString("reason",null);
		Row leaveRow =new Row();
		leaveRow.put("leave_type", leave_type);
		leaveRow.put("start_date", start_date);
		leaveRow.put("end_date",end_date );
		leaveRow.put("reason",reason );
		leaveRow.put("org_id",token );
		leaveRow.put("user_id",id );
		leaveService.mobileAdd(leaveRow);
		ovo =new OVO(1,"success","");
		json =VOConvert.ovoToJson(ovo);
		return json;
	} 
	
	//查询自己的请假记录
		// id
		@RequestMapping("/selfList")
		public @ResponseBody String querySelfLeaveList(HttpServletRequest request) throws JException
		{
			String json ="";
			parseRequest(request);
//			String token =ivo.getString("token",null);
			String id =ivo.getString("id",null);
			if(id == null)
			{
				ovo =new OVO(-20005,"人员编号:id不能为空","人员编号:id不能为空");
				json =VOConvert.ovoToJson(ovo);
				return json;
			}
			String page =ivo.getString("page","1");
			String page_size =ivo.getString("page_size","10");
			
			DataSet ds =leaveService.querySelfLeaveList(id,page,page_size);
			ovo =new OVO();
			ovo.set("list",ds );
			json =VOConvert.ovoToJson(ovo);
			return json;
		} 
	
		//分页查询属下的请假记录
		// id
		@RequestMapping("/followerList")
		public @ResponseBody String queryFollowerLeaveList(HttpServletRequest request) throws JException
		{
			String json ="";
			parseRequest(request);
//			String token =ivo.getString("token",null);
			String id =ivo.getString("id",null);
			if(id == null)
			{
				ovo =new OVO(-20005,"人员编号:id不能为空","人员编号:id不能为空");
				json =VOConvert.ovoToJson(ovo);
				return json;
			}
			String page =ivo.getString("page","1");
			String page_size =ivo.getString("page_size","10");
			
			DataSet ds =leaveService.queryFollowerLeaveList(id,page,page_size);
			ovo =new OVO();
			ovo.set("list",ds );
			json =VOConvert.ovoToJson(ovo);
			return json;
		} 
	
		
		//审批请假
		@RequestMapping("/audit")
		public @ResponseBody String auditFollowerLeave(HttpServletRequest request) throws JException
		{
			String json ="";
			parseRequest(request);
//			String token =ivo.getString("token",null);
			String id =ivo.getString("id",null);
			if(id == null)
			{
				ovo =new OVO(-20005,"人员编号:id不能为空","人员编号:id不能为空");
				json =VOConvert.ovoToJson(ovo);
				return json;
			}
			String log_id =ivo.getString("log_id",null);
			String audit_objection =ivo.getString("audit_objection","");
			if(log_id == null)
			{
				ovo =new OVO(-20015,"log_id请假记录不能为空","log_id请假记录不能为空");
				json =VOConvert.ovoToJson(ovo);
				return json;
			}
			
			String status =ivo.getString("status",null);
			if(status == null)
			{
				ovo =new OVO(-20016,"status审核状态不能为空","status审核状态不能为空");
				json =VOConvert.ovoToJson(ovo);
				return json;
			}
			Row leaveRow =new Row();
			leaveRow.put("status", status);
			leaveRow.put("id", log_id);
			leaveRow.put("audit_objection", audit_objection);
			leaveRow.put("audit_id", id);
			leaveRow.put("modify_time", DateUtil.getCurrentDateTime());
			leaveService.auditLeave(leaveRow);
			ovo =new OVO(1,"success","");
			json =VOConvert.ovoToJson(ovo);
			return json;
		} 
}
