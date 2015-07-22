package com.cxhl.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.UserLotteryNumService;
import com.cxhl.service.UserService;
import com.cxhl.service.UserShareService;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.DateUtil;
/**
 * @author TongJianbo
 *
 */
@Controller("mobileUserShareController")
@RequestMapping("/api/user/share")
public class UserShareController extends BaseController {
	
	private static Logger logger = Logger.getLogger(UserShareController.class); 
	
	@Resource(name = "cxhlUserService")
	private UserService userService;
	
	@Resource(name = "cxhlUserShareService")
	private UserShareService userShareService;
	
	@Resource(name = "cxhlUserLotteryNumService")
	private UserLotteryNumService userLotteryNumService;
	
	/**
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/list")
	public @ResponseBody
	String queryPage(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-1,"","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet list =userShareService.list(user_id, page, page_size);
		ovo =new OVO(0,"","");
		ovo.set("list", list);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	@RequestMapping(value ="/add")
	public @ResponseBody
	String add(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String user_id =ivo.getString("user_id","");
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-1,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row userRow =userService.find(user_id);
		if(userRow == null)
		{
			ovo =new OVO(-1,"用户不存在","用户不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String type =ivo.getString("type","");
		if(StringUtils.isEmptyOrNull(type))
		{
			ovo =new OVO(-1,"分享类型不能为空，1表示微博2表示微信","分享类型不能为空，1表示微博2表示微信");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		
		Row row =new Row();
		row.put("user_id", user_id);
		row.put("type", type);
		userShareService.insert(row);
		//判断是否需要给用户增加抽奖次数
		String time =DateUtil.getCurrentDateTime();
		String date =time.substring(0, 10);//yyyy-mm-dd
		Row lotteryNumRow =userLotteryNumService.findByUserId(user_id, date,"1");//1每日抽奖2电台抽奖3二维码抽奖
		if(lotteryNumRow != null)
		{
			String total_num =lotteryNumRow.getString("total_num","0");
			int t_num =Integer.parseInt(total_num);
			if(t_num <4)
			{
				lotteryNumRow.put("total_num", "4");
				userLotteryNumService.update(lotteryNumRow);
			}
		}
		ovo =new OVO(0,"操作成功","");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/update")
	public @ResponseBody
	String update(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("修改收货地址详情");
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"编号不能为空","编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		ovo =new OVO(0,"操作成功","");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/delete")
	public @ResponseBody
	String delete(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"编号不能为空","编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		userShareService.delete(id);
		ovo =new OVO(0,"操作成功","");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

	
}
