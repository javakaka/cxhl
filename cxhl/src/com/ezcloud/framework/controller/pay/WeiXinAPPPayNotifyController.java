package com.ezcloud.framework.controller.pay;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezcloud.framework.controller.ApiBaseController;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.SpringUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.IVO;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.VOConvert;

/**
 * 微信APP支付 
 * @author Thinkive.TongJianbo
 */
@Controller("frameworkWeiXinPayNotifyController")
@RequestMapping("/notify/weixin/pay")
public class WeiXinAPPPayNotifyController extends ApiBaseController{
	
	private static Logger logger = Logger.getLogger(WeiXinAPPPayNotifyController.class);
	
	/**
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/app")
	public @ResponseBody
	String notify(HttpServletRequest request) throws Exception
	{
		return "SUCCESS";
	}
}
