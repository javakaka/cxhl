package com.cxhl.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.AdService;
import com.ezcloud.framework.common.Setting;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.HtmlUtils;
import com.ezcloud.framework.util.SettingUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;

@Controller("mobileAdController")
@RequestMapping("/api/ad")
public class AdController extends BaseController {
	
	private static Logger logger = Logger.getLogger(AdController.class); 
	@Resource(name = "cxhlAdService")
	private AdService adService;
	
	/**
	 * 查询广告列表
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value ="/list")
	public @ResponseBody
	String queryAllAd(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("广告列表");
		
		DataSet adDataSet =adService.list();
		Setting setting =SettingUtils.get();
		String siteUrl =setting.getSiteUrl();
		String siteDomain ="";
		int iPos =siteUrl.lastIndexOf("/");
		if(iPos !=-1)
		{
			siteDomain =siteUrl.substring(0,iPos);
		}
		if(! StringUtils.isEmptyOrNull(siteDomain))
		{
			for(int i=0; i<adDataSet.size(); i++)
			{
				Row adRow =(Row)adDataSet.get(i);
				String picture =adRow.getString("picture","");
				if(! StringUtils.isEmptyOrNull(picture))
				{
					picture	=siteDomain+picture;
					adRow.put("picture", picture);
					adDataSet.set(i, adRow);
				}
			}
		}
		OVO ovo =new OVO(0,"","");
		ovo.set("ad_list", adDataSet);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 查询广告详情
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/find")
	public @ResponseBody
	String findById(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("广告详情");
		String id=ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-10011,"id不能为空","id不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row row =adService.findById(id);
		if(row == null)
		{
			OVO ovo =new OVO(-10011,"对应的数据不存在","对应的数据不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String content =row.getString("content","");
		Setting setting =SettingUtils.get();
		String siteUrl =setting.getSiteUrl();
		String domain =siteUrl;
		int iPos =siteUrl.lastIndexOf("/");
		if(iPos != -1)
		{
			domain =siteUrl.substring(0,iPos);
		}
		//替换图片标签的url为http全路径
		content =HtmlUtils.fillImgSrcWithDomain(domain, content);
		// 转义字符串中的换行，不然在转成json对象时会报错
		content =StringUtils.string2Json(content);
		OVO ovo =new OVO(0,"","");
		ovo.set("content", content);
		ovo.set("id", id);
		//浏览次数+1
		Row adRow =new Row();
		adRow.put("id", id);
		String view_num =row.getString("view_num","");
		if(StringUtils.isEmptyOrNull(view_num))
		{
			view_num ="0";
		}
		int num =Integer.parseInt(view_num);
		num ++;
		adRow.put("view_num", num);
		adService.update(adRow);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

}
