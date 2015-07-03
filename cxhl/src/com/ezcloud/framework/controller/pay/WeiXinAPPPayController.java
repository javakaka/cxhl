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
@Controller("frameworkWeiXinPayController")
@RequestMapping("/api/pay/weixin/app")
public class WeiXinAPPPayController extends ApiBaseController{
	
	private static Logger logger = Logger.getLogger(WeiXinAPPPayController.class);
	
	/**
	 * 客户端发起支付请求，到服务器端认证
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/validate")
	public @ResponseBody
	String validate(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("客户端发起微信支付请求，服务器端开始验证请求是否合法...");
		String user_id =ivo.getString("user_id","");
		String order_id =ivo.getString("order_id","");
		String app_ip =ivo.getString("app_ip","");
		String  service_name=ivo.getString("service_name","");
		if(StringUtils.isEmptyOrNull(service_name))
		{
			ovo =new OVO(-20010,"参数错误，请指定支付服务名称[service_name]","参数错误，请指定支付服务名称[service_name]");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-20010,"参数错误，用户编号[user_id]不能为空","参数错误，用户编号[user_id]不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(StringUtils.isEmptyOrNull(order_id))
		{
			ovo =new OVO(-20010,"参数错误，订单编号[order_id]不能为空","参数错误，订单编号[order_id]不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(StringUtils.isEmptyOrNull(app_ip))
		{
			ovo =new OVO(-20010,"参数错误，客户端IP[app_ip]不能为空","参数错误，客户端IP[app_ip]不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Object serviceObj =null;
		try
		{
			serviceObj =SpringUtils.getBean(service_name);
		}catch(NoSuchBeanDefinitionException exp)
		{
			ovo =new OVO(-20010,"系统不存在[service_name]所对应的服务，请和管理员联系","系统不存在[service_name]所对应的服务，请和管理员联系");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		if(serviceObj == null)
		{
			ovo =new OVO(-20010,"系统不存在[service_name]所对应的服务，请和管理员联系","系统不存在[service_name]所对应的服务，请和管理员联系");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Method method =serviceObj.getClass().getDeclaredMethod("validate",IVO.class);
		ovo =(OVO)method.invoke(serviceObj,ivo);
		System.out.println("ovo----------"+ovo);
//		ovo =new OVO(0,"","");
//		ovo.set("list","1221");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
}
