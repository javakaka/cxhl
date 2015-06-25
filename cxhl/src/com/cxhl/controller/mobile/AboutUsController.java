package com.cxhl.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.AboutUsService;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
/**
 * 房租宝规则消息业务处理
 * @author Administrator
 */

@Controller("mobileAboutUsController")
@RequestMapping("/api/core/about_us")
public class AboutUsController extends BaseController {
	
	private static Logger logger = Logger.getLogger(AboutUsController.class); 
	@Resource(name = "fzbAboutUsService")
	private AboutUsService aboutUsService;
	
	/**
	 * 查询
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/find")
	public @ResponseBody
	String findById(HttpServletRequest request) throws Exception
	{
		logger.info("查询关于我们");
		parseRequest(request);
		Row row =aboutUsService.findById("1");
		String remark =row.getString("remark","");
		OVO ovo =new OVO(0,"","");
		ovo.set("content", remark);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

}
