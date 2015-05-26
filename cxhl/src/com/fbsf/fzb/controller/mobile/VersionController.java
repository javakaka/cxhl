package com.fbsf.fzb.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.fbsf.fzb.service.VersionService;

@Controller("mobileVersionController")
@RequestMapping("/api/core/version")
public class VersionController extends BaseController {
	
	private static Logger logger = Logger.getLogger(VersionController.class); 
	@Resource(name = "fzbVersionService")
	private VersionService versionService;
	
	/**
	 * 查询最新版本
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/lastest")
	public @ResponseBody
	String queryAllAd(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("查询最新版本");
		String app =ivo.getString("app",null);
		if(StringUtils.isEmptyOrNull(app))
		{
			ovo =new OVO(-10001,"请指定app[1房租宝房东租客版2中介版]","请指定app[1房租宝房东租客版2中介版]");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String device =ivo.getString("device",null);
		if(StringUtils.isEmptyOrNull(device))
		{
			ovo =new OVO(-10001,"请指定设备类型[1 ios 2 android 3 wp]","请指定设备类型[1 ios 2 android 3 wp]");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row versionRow = versionService.findLastestVersion(app, device);
		String version =versionRow.getString("version","0");
		String url =versionRow.getString("url","");
		String remark =versionRow.getString("remark","");
		String size =versionRow.getString("size","");
		ovo =new OVO(0,"","");
		ovo.set("version",version );
		ovo.set("url", url);
		ovo.set("remark", remark);
		//安装包大小 单位/bit
		ovo.set("size", size);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	

}
