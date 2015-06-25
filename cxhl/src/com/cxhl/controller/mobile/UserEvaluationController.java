package com.cxhl.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.UserEvaluationService;
import com.cxhl.service.UserService;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;

/**
 * 房东租客点评业务处理
 * @author Administrator
 *
 */
@Controller("mobileEvaluationController")
@RequestMapping("/api/core/evaluation")
public class UserEvaluationController extends BaseController {
	
	private static Logger logger = Logger.getLogger(UserEvaluationController.class); 
	
	@Resource(name = "fzbEvaluationService")
	private UserEvaluationService userEvaluationService;
	
	@Resource(name = "fzbUserService")
	private UserService userService;
	
	/**
	 * 根据用户身份证号码和姓名分页查询评价
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/list")
	public @ResponseBody
	String page(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		OVO ovo =null;
		String id_card_no =ivo.getString("id_card_no","");
		if(StringUtils.isEmptyOrNull(id_card_no))
		{
			ovo =new OVO(-11000,"身份证号码不能为空","身份证号码不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String name =ivo.getString("name","");
		if(StringUtils.isEmptyOrNull(name))
		{
			ovo =new OVO(-11000,"姓名不能为空","姓名不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String type =ivo.getString("type","");
		if(StringUtils.isEmptyOrNull(type))
		{
			ovo =new OVO(-11000,"查询类型不能为空，1房东对租客的评价2租客对房东的评价","查询类型不能为空，1房东对租客的评价2租客对房东的评价");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		name =AesUtil.encode(name);
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet ds =userEvaluationService.list(Integer.parseInt(page),
				Integer.parseInt(page_size),name,id_card_no,type);
		ovo =new OVO(0,"","");
		ovo.set("list", ds);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 添加评价
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/add")
	public @ResponseBody
	String add(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("添加评价");
		//评价者id,当前登录用户的编号
		String user_id=ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-11000,"登录用户id不能为空","登录用户id不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row staffRow =userService.find(user_id);
		if(staffRow == null )
		{
			ovo =new OVO(-11000,"用户不存在","用户不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//被评价者id
		String to_id=ivo.getString("to_id",null);
		if(StringUtils.isEmptyOrNull(to_id))
		{
			ovo =new OVO(-11000,"被评价者id不能为空","被评价者id不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		staffRow =userService.find(to_id);
		if(staffRow == null )
		{
			ovo =new OVO(-11000,"用户不存在","用户不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//1房东评价租客;2租客评价房东;
		String type=ivo.getString("type",null);
		if(StringUtils.isEmptyOrNull(type))
		{
			ovo =new OVO(-11000,"评论类型不能为空，1房东对租客的评价2租客对房东的评价","评论类型不能为空，1房东对租客的评价2租客对房东的评价");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String room_id=ivo.getString("room_id","");
		String content=ivo.getString("content","");
		if(user_id.equals(to_id))
		{
			ovo =new OVO(-11000,"用户不能自己评论自己","用户不能自己评论自己");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row row =new Row();
		row.put("from_id", user_id);
		row.put("to_id", to_id);
		row.put("type", type);
		row.put("room_id", room_id);
		row.put("content", content);
		row.put("status", "1");
		row.put("title", "");
		row =MapUtils.convertMaptoRowWithoutNullField(row);
		userEvaluationService.insert(row);
		ovo =new OVO(0,"评论成功","评论成功");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

}
