package com.cxhl.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.LetterService;
import com.cxhl.service.UserService;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;

/**
 * 站内信业务处理
 * @author Administrator
 *
 */
@Controller("mobileLetterController")
@RequestMapping("/api/core/letter")
public class LetterController extends BaseController {
	
	private static Logger logger = Logger.getLogger(LetterController.class); 
	
	@Resource(name = "fzbLetterService")
	private LetterService letterService;

	@Resource(name = "fzbUserService")
	private UserService userService;
	
	/**
	 * 根据用户身编号分页查询站内信
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
		DataSet ds =letterService.list(Integer.parseInt(page),
				Integer.parseInt(page_size),user_id);
		ovo =new OVO(0,"","");
		ovo.set("list", ds);
		int totalReceivedLetterNum =0;
		int totalSendedLetterNum =0;
		int totalNotReadLetterNum =0;
		int totalReadedLetterNum =0;
		if(page.equals("1"))
		{
			totalReceivedLetterNum =letterService.queryAllReceivedLetterNumByUserId(user_id);
			totalSendedLetterNum =letterService.queryAllSendLetterNumByUserId(user_id);
			totalNotReadLetterNum =letterService.queryNotReadLetterNumByUserId(user_id);
			totalReadedLetterNum =letterService.queryReadedLetterNumByUserId(user_id);
			ovo.set("totalReceivedLetterNum", totalReceivedLetterNum);
			ovo.set("totalSendedLetterNum", totalSendedLetterNum);
			ovo.set("totalNotReadLetterNum", totalNotReadLetterNum);
			ovo.set("totalReadedLetterNum", totalReadedLetterNum);
		}
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 发送/回复站内信
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/add")
	public @ResponseBody
	String add(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("发送/回复站内信");
		//发送者id,当前登录用户的编号
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
		//接收者id
		String to_id=ivo.getString("to_id",null);
		if(StringUtils.isEmptyOrNull(to_id))
		{
			ovo =new OVO(-11000,"接收者id不能为空","接收者id不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		staffRow =userService.find(to_id);
		if(staffRow == null )
		{
			ovo =new OVO(-11000,"用户不存在","用户不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String up_id=ivo.getString("up_id","");
		//标题
		String title=ivo.getString("title","");
		String content=ivo.getString("content","");
		Row row =new Row();
		row.put("from_id", user_id);
		row.put("to_id", to_id);
		row.put("up_id", up_id);
		row.put("title", title);
		row.put("content", content);
//		1待审核、2审核通过、0审核不通过、-1删除
		row.put("audit_status", "1");
//		1已读2未读
		row.put("read_status", "2");
		row =MapUtils.convertMaptoRowWithoutNullField(row);
		letterService.insert(row);
		ovo =new OVO(0,"发送成功","发送成功");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

}
