package com.cxhl.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.InfoTypeService;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.VOConvert;
/**
 * 用户收货地址
 * @author TongJianbo
 *
 */
@Controller("mobileInfoTypeController")
@RequestMapping("/api/info/category")
public class InfoTypeController extends BaseController {
	
	private static Logger logger = Logger.getLogger(InfoTypeController.class); 
	
	@Resource(name = "cxhlInfoTypeService")
	private InfoTypeService infoTypeService;
	
	
	/**
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/list")
	public @ResponseBody
	String queryPage(HttpServletRequest request) throws Exception
	{
		logger.info("查询全部资讯分类");
		parseRequest(request);
		DataSet list =infoTypeService.list();
		ovo =new OVO(0,"","");
		ovo.set("list", list);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
}