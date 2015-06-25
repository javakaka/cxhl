package com.cxhl.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.UserGiftService;
import com.cxhl.service.UserService;
import com.ezcloud.framework.common.Setting;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.HtmlUtils;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.util.SettingUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;

/**
 * 用户的奖品控制器
 * @author TongJianbo
 */
@Controller("mobileUserGiftController")
@RequestMapping("/api/user/gift")
public class UserGiftController extends BaseController {
	
	private static Logger logger = Logger.getLogger(UserGiftController.class); 
	
	@Resource(name = "cxhlUserService")
	private UserService userService;
	
	@Resource(name = "cxhlUserGiftService")
	private UserGiftService userGiftService;
	
	/**
	 * 用户分页查询自己的礼品
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/list")
	public @ResponseBody
	String queryPage(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("用户分页查询自己的礼品");
		String user_id =ivo.getString("user_id","");
		String state =ivo.getString("state","");
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet list =userGiftService.list(user_id,state,page,page_size);
		ovo =new OVO(0,"","");
		ovo.set("list", list);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/detail")
	public @ResponseBody
	String find(HttpServletRequest request) throws Exception
	{
		//
		parseRequest(request);
		String id=ivo.getString("id","");
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-10041,"礼品记录编号［id］不能为空","礼品记录编号［id］不能为空");
		}
		Row giftRow =userGiftService.find(id);
		String user_id =giftRow.getString("user_id");
		String shop_id =giftRow.getString("shop_id");
		String gift_id =giftRow.getString("gift_id");
		String total_num =giftRow.getString("total_num","0");
		String exchange_num =giftRow.getString("exchange_num","0");
		String left_num =giftRow.getString("left_num","0");
		String gift_name =giftRow.getString("gift_name","");
		String address =giftRow.getString("address","");
		String link_tel =giftRow.getString("link_tel","");
		String shop_name =giftRow.getString("shop_name","");
		String remark =giftRow.getString("remark","");
		if(! StringUtils.isEmptyOrNull(remark))
		{
			Setting setting =SettingUtils.get();
			String siteUrl =setting.getSiteUrl();
			String domain =siteUrl;
			int iPos =siteUrl.lastIndexOf("/");
			if(iPos != -1)
			{
				domain =siteUrl.substring(0,iPos);
			}
			//替换图片标签的url为http全路径
			remark =HtmlUtils.fillImgSrcWithDomain(domain, remark);
			// 转义字符串中的换行，不然在转成json对象时会报错
			remark =StringUtils.string2Json(remark);
		}
		ovo =new OVO(0,"查询成功","查询成功");
		ovo.set("id", id);
		ovo.set("user_id", user_id);
		ovo.set("shop_id", shop_id);
		ovo.set("shop_name", shop_name);
		ovo.set("gift_id", gift_id);
		ovo.set("total_num", total_num);
		ovo.set("exchange_num", exchange_num);
		ovo.set("left_num", left_num);
		ovo.set("gift_name", gift_name);
		ovo.set("address", address);
		ovo.set("link_tel", link_tel);
		ovo.set("remark", remark);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	
	/**兑换礼品
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/exchange")
	public @ResponseBody
	String exchange(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
//		String id =ivo.getString("id",null);
//		if(StringUtils.isEmptyOrNull(id))
//		{
//			ovo =new OVO(-1,"用户奖品记录编号不能为空","用户奖品记录编号不能为空");
//			return AesUtil.encode(VOConvert.ovoToJson(ovo));
//		}
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-1,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String gift_id =ivo.getString("gift_id",null);
		if(StringUtils.isEmptyOrNull(gift_id))
		{
			ovo =new OVO(-1,"奖品编号不能为空","奖品编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String exchange_num =ivo.getString("exchange_num",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-1,"兑换数量不能为空","兑换数量不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		boolean bool =NumberUtils.isNumber(exchange_num);
		if(! bool )
		{
			ovo =new OVO(-1,"兑换数量应该是数字","兑换数量应该是数字");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row giftRow =userGiftService.find(user_id, gift_id);
		if(giftRow == null)
		{
			ovo =new OVO(-1,"参数错误，此用户没有对应的奖品","参数错误，此用户没有对应的奖品");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String total_num =giftRow.getString("total_num","0");
		String ex_num =giftRow.getString("exchange_num","0");
		int iTotalNum =Integer.parseInt(total_num);
		int iExNum =Integer.parseInt(ex_num);
		int iCurrentExNum =Integer.parseInt(exchange_num);
		int minus =iTotalNum -iExNum -iCurrentExNum;
		if(minus < 0)
		{
			ovo =new OVO(-1,"奖品剩余可兑换数量不足","奖品剩余可兑换数量不足");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		iExNum =iExNum+iCurrentExNum;
		int iLeftNum =iTotalNum -iExNum;
		Row row =new Row();
		row.put("id", giftRow.getString("id"));
		row.put("exchange_num", giftRow.getString("id"));
		row.put("left_num", iLeftNum);
		if(iLeftNum == 0)
		{
			row.put("state", "1");
		}
		userGiftService.update(row);
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
//		String id =ivo.getString("id",null);
//		if(StringUtils.isEmptyOrNull(id))
//		{
//			ovo =new OVO(-1,"收藏编号不能为空","收藏编号不能为空");
//			return AesUtil.encode(VOConvert.ovoToJson(ovo));
//		}
////		shopService.delete(id);
		ovo =new OVO(0,"操作成功","");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

}
