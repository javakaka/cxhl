package com.cxhl.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.ShopService;
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
 * 商家信息
 * @author TongJianbo
 *
 */
@Controller("mobileShopController")
@RequestMapping("/api/shop/profile")
public class ShopController extends BaseController {
	
	private static Logger logger = Logger.getLogger(ShopController.class); 
	
	@Resource(name = "cxhlUserService")
	private UserService userService;
	
	@Resource(name = "cxhlShopService")
	private ShopService shopService;
	
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
		parseRequest(request);
		String type =ivo.getString("type","");
		String key_word =ivo.getString("key_word","");
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","3");
		DataSet list =shopService.list(type,key_word,page,page_size);
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
		String c_id =ivo.getString("shop_id","");
		if(StringUtils.isEmptyOrNull(c_id))
		{
			ovo =new OVO(-1,"商家编号不能为空","商家编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row userRow =userService.find(user_id);
		if(userRow == null)
		{
			ovo =new OVO(-1,"用户不存在","用户不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		
		Row collection_row =shopService.findByUserIdAndShopId(user_id,c_id);
		if(collection_row != null)
		{
			ovo =new OVO(-10021,"用户已收藏此商家，不能重复收藏","用户已收藏此商家，不能重复收藏");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row row =new Row();
		row.put("user_id", user_id);
		row.put("c_id", c_id);
		row.put("c_type", "0");
		shopService.insert(row);
		ovo =new OVO(0,"操作成功","");
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
		Row row = shopService.find(id);
		String type =row.getString("type","");
		String shop_name =row.getString("shop_name","");
		String link_name =row.getString("link_name","");
		String link_tel =row.getString("link_tel","");
		String longitude =row.getString("longitude","");
		String latitude =row.getString("latitude","");
		String star =row.getString("star","");
		String address =row.getString("address","");
		String remark =row.getString("remark","");
		String detail =row.getString("detail","");
//		String file_path =row.getString("file_path","");
		String average_cost =row.getString("average_cost","");
		Setting setting =SettingUtils.get();
		String siteUrl =setting.getSiteUrl();
		String domain =siteUrl;
		int iPos =siteUrl.lastIndexOf("/");
		if(iPos != -1)
		{
			domain =siteUrl.substring(0,iPos);
		}
		//替换图片标签的url为http全路径
		detail =HtmlUtils.fillImgSrcWithDomain(domain, detail);
		// 转义字符串中的换行，不然在转成json对象时会报错
		detail =StringUtils.string2Json(detail);
		//pics 
		DataSet ds =shopService.findShopPicture(id);
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
		ovo.set("average_cost", average_cost);
		ovo.set("remark", remark);
		ovo.set("picture_ds", ds);
		//
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
			ovo =new OVO(-1,"收藏编号不能为空","收藏编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		shopService.delete(id);
		ovo =new OVO(0,"操作成功","");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

	
}
