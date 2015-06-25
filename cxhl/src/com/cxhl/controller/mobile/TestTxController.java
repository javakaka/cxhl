package com.cxhl.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.TxService;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.VOConvert;

@Controller("mobileTxTestController")
@RequestMapping("/api/core/test")
public class TestTxController extends BaseController {
	
	@Resource(name = "fzbTxTestService")
	private TxService txService;
	
	/**
	 * 查询全部城市以及城市区域列表
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/testTx")
	public @ResponseBody
	String queryCityAndZone(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		txService.testTx();
		OVO ovo =new OVO(0,"","");
		ovo.set("result", "123");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

}
