package com.cxhl.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.ShopCouponService;
import com.cxhl.service.UserService;
import com.ezcloud.framework.common.Setting;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.HtmlUtils;
import com.ezcloud.framework.util.SettingUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
/**
 * 商家优惠券
 * @author TongJianbo
 *
 */
@Controller("mobileShopCouponController")
@RequestMapping("/api/shop/coupon")
public class ShopCouponController extends BaseController {
	
	private static Logger logger = Logger.getLogger(ShopCouponController.class); 
	
	@Resource(name = "cxhlUserService")
	private UserService userService;
	
	@Resource(name = "cxhlShopCouponService")
	private ShopCouponService shopCouponService;
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/list")
	public @ResponseBody
	String queryPage(HttpServletRequest request) throws Exception
	{
		logger.info("分页查询商家的优惠券列表");
		parseRequest(request);
		String shop_id =ivo.getString("shop_id","");
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","3");
		DataSet list =shopCouponService.list(shop_id,page,page_size);
		ovo =new OVO(0,"查询成功","查询成功");
		ovo.set("list", list);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	
	/**
	 * 查询商家详情
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/detail")
	public @ResponseBody
	String find(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"编号不能为空","编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row row = shopCouponService.find(id);
		String type =row.getString("type","");
		String shop_name =row.getString("shop_name","");
		String link_name =row.getString("link_name","");
		String link_tel =row.getString("link_tel","");
		String longitude =row.getString("longitude","");
		String latitude =row.getString("latitude","");
		String star =row.getString("star","");
		String address =row.getString("address","");
		String detail =row.getString("detail","");
		ovo =new OVO(0,"操作成功","");
		ovo.set("id", id);
		ovo.set("type", type);
		ovo.set("shop_name", shop_name);
		ovo.set("link_name", link_name);
		ovo.set("link_tel", link_tel);
		ovo.set("longitude", longitude);
		ovo.set("latitude", latitude);
		ovo.set("star", star);
		ovo.set("address", address);
		ovo.set("detail", detail);
		//
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 查询商家优惠券详情,图文
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/remark")
	public @ResponseBody
	String remark(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"编号不能为空","编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row row = shopCouponService.findRemark(id);
		String remark =row.getString("remark","");
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
		ovo =new OVO(0,"操作成功","");
		ovo.set("remark", remark);
		//
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
}
