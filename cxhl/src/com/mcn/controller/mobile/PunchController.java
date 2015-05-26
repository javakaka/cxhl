package com.mcn.controller.mobile;

import java.io.File;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.Base64Util;
import com.ezcloud.utility.FileUtil;
import com.ezcloud.utility.StringUtil;
import com.mcn.service.PunchLogService;
import com.mcn.service.PunchRuleService;

/**
 * 手机端打卡接口
 * @author JianBoTong
 *
 */
@Controller("mobilePunchController")
@RequestMapping("/api/punch")
public class PunchController extends BaseController{
	
	@Resource(name = "mcnPunchRuleService")
	private   PunchRuleService punchRuleService;
	
	@Resource(name = "mcnPunchLogService")
	private   PunchLogService punchLogService;
	
	//根据人员编号查询打卡时间
	@RequestMapping("/times")
	public @ResponseBody String queryDepartPunchTimes(HttpServletRequest request) throws JException
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
		Row timeRow =punchRuleService.queryDepartTimes(id);
		ovo =new OVO();
		String am_start =timeRow.getString("am_start","");
		String am_end =timeRow.getString("am_end","");
		String pm_start =timeRow.getString("pm_start","");
		String pm_end =timeRow.getString("pm_end","");
		ovo.set("am_start", am_start);
		ovo.set("am_end", am_end);
		ovo.set("pm_start", pm_start);
		ovo.set("pm_end", pm_end);
		json =VOConvert.ovoToJson(ovo);
		return json;
	} 
	
	
	//打卡
	@RequestMapping("/add")
	public @ResponseBody String punch(HttpServletRequest request) throws JException
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
		ovo =new OVO();
		String punch_type =ivo.getString("punch_type",null);
		if(punch_type == null)
		{
			ovo =new OVO(-20025,"punch_type不能为空","punch_type不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		String punch_time =ivo.getString("punch_time",null);
		if(punch_time == null)
		{
			ovo =new OVO(-20026,"punch_time打卡时间不能为空","punch_time打卡时间不能为空");
			json =VOConvert.ovoToJson(ovo);
			return json;
		}
		String longitude =ivo.getString("longitude","");
		String latitude =ivo.getString("latitude","");
		String place_name =ivo.getString("place_name","");
		//base64 编码图片
		String userPic =ivo.getString("picture",null);
		String imgPath ="";
		String imgName =ivo.getString("picture_name",null);
		if(imgName == null || imgName.trim().equals(""))
		{
			imgName =StringUtil.getRandKeys(12);
		}
		if(userPic != null && !userPic.trim().equals(""))
		{
//			String basePath =request.getRealPath("/resources");
			String basePath =request.getSession().getServletContext().getRealPath("/resources");
			basePath +="/"+token;
			File file =new File(basePath);
			if(! file.isDirectory())
			{
				FileUtil.mkdir(basePath);
			}
			imgPath=basePath+"/"+imgName+".jpg";
			imgPath =imgPath.replace("\\\\","\\");
			imgPath =imgPath.replace("\\","/");
			Base64Util.GenerateImage(userPic, imgPath);
		}
		Row punchRow =new Row();
		punchRow.put("punch_type", punch_type);
		punchRow.put("punch_time", punch_time);
		punchRow.put("longitude", longitude);
		punchRow.put("latitude", latitude);
		punchRow.put("place_name", place_name);
//		punchRow.set("punch_result", "1");
		punchRow.put("org_id", token);
		punchRow.put("user_id", id);
		punchRow.put("img_path", imgPath);
		punchLogService.mobilePunch(punchRow);
		ovo =new OVO(1,"success","");
		json =VOConvert.ovoToJson(ovo);
		return json;
	} 
		
	//根据人员编号和月份查询打卡记录
	@RequestMapping("/user_month_log")
	public @ResponseBody String queryUserPunchLog(HttpServletRequest request) throws JException
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
		String date =ivo.getString("date",null);//2014-10
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet ds = punchLogService.queryUserMonthLog(id,date,page,page_size);
		ovo =new OVO();
		ovo.set("list", ds);
		json =VOConvert.ovoToJson(ovo);
		return json;
	} 
	
	
}
