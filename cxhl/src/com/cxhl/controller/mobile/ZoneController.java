package com.cxhl.controller.mobile;

import java.io.File;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.common.Setting;
import com.ezcloud.framework.service.system.SystemZoneService;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.SettingUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.FileUtil;

@Controller("mobileZoneController")
@RequestMapping("/api/zone")
public class ZoneController extends BaseController {
	
	private static Logger logger = Logger.getLogger(ZoneController.class); 
	@Resource(name = "frameworkSystemZoneService")
	private SystemZoneService zoneService;
	
	/**
	 * 查询全部城市以及城市区域列表
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/CityAndZone")
	public @ResponseBody
	String queryCityAndZone(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("查询城市以及区域");
		Row row =zoneService.queryAllCityAndZone();
		DataSet cityDataSet =(DataSet)row.get("city");
		DataSet zoneDataSet =(DataSet)row.get("zone");
		OVO ovo =new OVO(0,"","");
		ovo.set("city_list", cityDataSet);
		ovo.set("zone_list", zoneDataSet);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
//	/**
//	 * 检查文件版本信息，如果需要更新，则返回文件的下载路径
//	 * json 格式文件
//	 * 共3个文件：
//	 * 省份
//	 * 城市
//	 * 城市区域
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value ="/checkVersion")
//	public @ResponseBody
//	String checkVersion(HttpServletRequest request) throws Exception
//	{
//		OVO ovo =null;
//		parseRequest(request);
//		String versionNo =ivo.getString("version");
//		if(StringUtils.isEmptyOrNull(versionNo))
//		{
//			versionNo ="0";
//		}
//		Setting setting =SettingUtils.get();
//		String path =setting.getPhysicalPath()+"/resources/geography_files/";
//		String version_file =path+"version.json";
//		File file =new File(version_file);
//		if(!file.exists())
//		{
//			ovo =new OVO(0,"","");
//			ovo.set("update", "0");//不用更新
//			return AesUtil.encode(VOConvert.ovoToJson(ovo));
//		}
//		String version_content =FileUtil.readText(version_file);
//		JSONObject jsonObj =JSONObject.fromObject(version_content);
//		String timestamp =jsonObj.getString("VERSION");
//		if(StringUtils.isEmptyOrNull(timestamp) || versionNo.equals(timestamp))
//		{
//			ovo =new OVO(0,"","");
//			ovo.set("update", "0");//不用更新
//			return AesUtil.encode(VOConvert.ovoToJson(ovo));
//		}
//		String siteUrl =setting.getSiteUrl()+"/resources/geography_files/";
//		String province_path =siteUrl+"province.json";
//		String city_path =siteUrl+"city.json";
//		String zone_path =siteUrl+"zone.json";
//		String version_path =siteUrl+"version.json";
//		ovo =new OVO(0,"","");
//		ovo.set("update", "1");//需要更新
//		ovo.set("province_path", province_path);
//		ovo.set("city_path", city_path);
//		ovo.set("zone_path", zone_path);
//		ovo.set("version_path", version_path);
//		return AesUtil.encode(VOConvert.ovoToJson(ovo));
//	}
	
	/**
	 * 检查文件版本信息，如果需要更新，则返回文件的下载路径
	 * json 格式文件
	 * 共3个文件：
	 * 省份
	 * 城市
	 * 城市区域
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/checkVersion")
	public @ResponseBody
	String checkVersion(HttpServletRequest request) throws Exception
	{
		OVO ovo =null;
		parseRequest(request);
		String versionNo =ivo.getString("version");
		if(StringUtils.isEmptyOrNull(versionNo))
		{
			versionNo ="0";
		}
		Setting setting =SettingUtils.get();
		String path =setting.getPhysicalPath()+"/resources/geography_files/";
		String version_file =path+"version.json";
		File file =new File(version_file);
		if(!file.exists())
		{
			ovo =new OVO(0,"","");
			ovo.set("update", "0");//不用更新
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String version_content =FileUtil.readText(version_file);
		JSONObject jsonObj =JSONObject.fromObject(version_content);
		String timestamp =jsonObj.getString("VERSION");
		if(StringUtils.isEmptyOrNull(timestamp) || versionNo.equals(timestamp))
		{
			ovo =new OVO(0,"","");
			ovo.set("update", "0");//不用更新
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String siteUrl =setting.getSiteUrl()+"/resources/geography_files/";
//		String province_path =siteUrl+"province.json";
//		String city_path =siteUrl+"city.json";
//		String zone_path =siteUrl+"zone.json";
//		String version_path =siteUrl+"version.json";
		String geography_path =siteUrl+"geography.json";
//		String geography_content =FileUtil.readText(geography_path);
//		ovo =VOConvert.jsonToOvo(geography_content);
		ovo =new OVO(0,"","");
		ovo.set("update", "1");//需要更新
//		ovo.set("province_path", province_path);
//		ovo.set("city_path", city_path);
//		ovo.set("zone_path", zone_path);
//		ovo.set("version_path", version_path);
		ovo.set("geography_path", geography_path);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

}
