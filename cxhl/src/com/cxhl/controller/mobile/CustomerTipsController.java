package com.cxhl.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.CustomerTipsService;
import com.cxhl.service.UserService;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;

/**
 * 在线客服用户反馈业务处理
 * @author Administrator
 *
 */
@Controller("mobileCustomerTipsController")
@RequestMapping("/api/customer_tips")
public class CustomerTipsController extends BaseController {
	
	private static Logger logger = Logger.getLogger(CustomerTipsController.class); 
	
	@Resource(name = "cxhlCustomerTipsService")
	private CustomerTipsService customerTipsService;

	@Resource(name = "cxhlUserService")
	private UserService userService;
	
	/**
	 * 根据用户编号分页查询反馈记录
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
		String user_id =ivo.getString("user_id","");
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-11000,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet ds =customerTipsService.list(Integer.parseInt(page),
				Integer.parseInt(page_size),user_id);
		ovo =new OVO(0,"","");
		ovo.set("list", ds);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 发送反馈记录
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/add")
	public @ResponseBody
	String add(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("发送反馈记录");
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
		//标题
		String title=ivo.getString("title","");
		String content=ivo.getString("content","");
		if(StringUtils.isEmptyOrNull(content))
		{
			ovo =new OVO(-11000,"反馈内容不能为空","反馈内容不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row row =new Row();
		row.put("user_id", user_id);
		row.put("title", title);
		row.put("ask_content", content);
//		0未回复1已回复
		row.put("status", "0");
		row =MapUtils.convertMaptoRowWithoutNullField(row);
		customerTipsService.insert(row);
		ovo =new OVO(0,"操作成功","操作成功");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

}
