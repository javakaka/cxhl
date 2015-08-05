package com.cxhl.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.InfoService;
import com.cxhl.service.UserInfoCollectionService;
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
 * 资讯
 * @author TongJianbo
 *
 */
@Controller("mobileInfoController")
@RequestMapping("/api/info/profile")
public class InfoController extends BaseController {
	
	private static Logger logger = Logger.getLogger(InfoController.class); 
	
	@Resource(name = "cxhlInfoService")
	private InfoService infoService;
	
	@Resource(name = "cxhlInfoCollectionService")
	private UserInfoCollectionService userInfoCollectionService;
	
	/**
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/list")
	public @ResponseBody
	String queryPage(HttpServletRequest request) throws Exception
	{
		logger.info("分页查询资讯列表");
		parseRequest(request);
		String type_id =ivo.getString("type_id","");
		String key_words =ivo.getString("key_words","");
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet list =infoService.list(type_id,key_words,page,page_size);
		ovo =new OVO(0,"查询成功","查询成功");
		ovo.set("list", list);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	
	
	/**
	 * 查询资讯详情,图文
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/detail")
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
		String user_id =ivo.getString("user_id",null);
		String remark =infoService.findRemark(id);
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
		ovo.set("detail", remark);
		String is_collected ="0";
		if(! StringUtils.isEmptyOrNull(user_id))
		{
			Row urow =userInfoCollectionService.findByUserIdAndInfoId(user_id, id);
			if(urow != null)
			{
				is_collected ="1";
			}
		}
		ovo.set("is_collected", is_collected);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
}
