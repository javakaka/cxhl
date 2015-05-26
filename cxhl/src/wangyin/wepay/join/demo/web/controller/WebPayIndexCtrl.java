package wangyin.wepay.join.demo.web.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sun.misc.BASE64Decoder;
import wangyin.wepay.join.demo.utils.BASE64;
import wangyin.wepay.join.demo.utils.DESUtil;
import wangyin.wepay.join.demo.utils.JsonUtil;
import wangyin.wepay.join.demo.web.constant.MerchantConstant;
import wangyin.wepay.join.demo.web.domain.request.AsynNotificationReqDto;

import com.ezcloud.framework.service.system.SystemConfigService;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.NumberUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.DateUtil;
import com.ezcloud.utility.XmlUtil;
import com.fbsf.fzb.service.RenterDepositService;
import com.fbsf.fzb.service.RenterMonthlyMoneyService;
import com.fbsf.fzb.service.RoomService;
import com.fbsf.fzb.service.UserService;

/**
 * Created by wywangzhenlong on 14-4-27.
 * <p>
 * 模拟支付-交易信息构造
 * demo 入口
 * </p>
 */
@Controller
@RequestMapping(value = "/demo")
public class WebPayIndexCtrl {

	private static Logger logger = Logger.getLogger(WebPayIndexCtrl.class); 

	@Resource
    private MerchantConstant merchantConstant;
	
	@Resource
    private HttpServletRequest request;
	@Resource(name = "fzbRoomService")
	private RoomService roomService;
	
	@Resource(name = "fzbUserService")
	private UserService userService;
	
	@Resource(name = "fzbRentDepositService")
	private RenterDepositService paymentService;
	
	@Resource(name = "fzbRentMonthlyMoneyService")
	private RenterMonthlyMoneyService monthlyMoneyService;
	
	@Resource(name = "frameworkSystemConfigService")
	private SystemConfigService systemConfigService;
    /**
     * 
     * @param u_id 用户编号
     * @param r_id 房源编号
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/pay/index", method = RequestMethod.GET)
    public String execute(String u_id,String r_id,String p_id, HttpServletRequest httpServletRequest) {
    	logger.info("订单支付首页");
        /**
         * 设置一些初始数据,也可从页面上填写
         */
        httpServletRequest.setAttribute("tradeNum", merchantConstant.getMerchantNum() + System.currentTimeMillis());
        httpServletRequest.setAttribute("merchantNum", merchantConstant.getMerchantNum());
        httpServletRequest.setAttribute("successCallbackUrl", merchantConstant.getSuccessCallbackUrl());
        httpServletRequest.setAttribute("failCallbackUrl", merchantConstant.getFailCallbackUrl());
        httpServletRequest.setAttribute("notifyUrl", merchantConstant.getNotifyUrl());
        httpServletRequest.setAttribute("tradeTime", new Date());
        
        //验证请求参数的正确性
        if(StringUtils.isEmptyOrNull(u_id))
        {
        	httpServletRequest.setAttribute("error", "请求参数不合法");
        	return "payIndex";
        }
        if(StringUtils.isEmptyOrNull(r_id))
        {
        	httpServletRequest.setAttribute("error", "请求参数不合法");
        	return "payIndex";
        }
        if(StringUtils.isEmptyOrNull(p_id))
        {
        	httpServletRequest.setAttribute("error", "请求参数不合法");
        	return "payIndex";
        }
        Row userRow =userService.find(u_id);
        if(userRow == null )
        {
        	httpServletRequest.setAttribute("error", "错误参数:u_id");
        	return "payIndex";
        }
        Row roomRow =roomService.findById(r_id);
        if(roomRow == null )
        {
        	httpServletRequest.setAttribute("error", "错误参数:r_id");
        	return "payIndex";
        }
        Row recordRow =monthlyMoneyService.find(p_id);
        if(recordRow == null )
        {
        	httpServletRequest.setAttribute("error", "错误参数:p_id");
        	return "payIndex";
        }
        //判断是否需要交租
        String pay_status =recordRow.getString("pay_status","0");
        if("12345".indexOf(pay_status) != -1)
        {
        	httpServletRequest.setAttribute("error", "已交租，不能重复交租");
        	return "payIndex";
        }

//        month_pay_row =monthlyMoneyService.find(p_id);
        //找出当前月的交租月记录
		/*
        String start_date =roomRow.getString("start_date","");
		String end_date =roomRow.getString("end_date","");
		String appointed_date =DateUtil.getCurrentDate();
  		Row periodRow =null;
		try 
		{
			periodRow = paymentService.calculateRentPeriodByAppointedDate(start_date, end_date, appointed_date);
	  		String p_start_date =periodRow.getString("period_start_date","");
	  		String p_end_date =periodRow.getString("period_end_date","");
	  		if(p_start_date.equals("-1") || p_end_date.equals("-1"))
	  		{
	  			httpServletRequest.setAttribute("error", "当前时间不在交租日期范围内");
	  			return "payIndex";
	  		}
	  		month_pay_row =monthlyMoneyService.find(r_id, p_start_date, p_end_date);
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
			httpServletRequest.setAttribute("error", "系统处理故障，请稍后再试");
			return "payIndex";
		} 
		catch (JException e) 
		{
			e.printStackTrace();
			httpServletRequest.setAttribute("error", "系统处理故障，请稍后再试");
			return "payIndex";
		}
		*/

        Row month_pay_row =null;
		month_pay_row =recordRow;
		//获取当月租金,元
		BigDecimal moneyBig =new BigDecimal(NumberUtils.getTwoDecimal(month_pay_row.getString("money","0"))).setScale(2, BigDecimal.ROUND_HALF_UP);
		double money =moneyBig.doubleValue();
		System.out.println("money---->>"+money);
        String now_time =DateUtil.getCurrentDateTime();
		String year =now_time.substring(0,4);
		String month =now_time.substring(5,7);
		String day =now_time.substring(8,10);
		String to_account_type =systemConfigService.querySingleConfig("APP_COMPANY_BANK_ACCOUNT", "BANK_TYPE");
		String to_account =systemConfigService.querySingleConfig("APP_COMPANY_BANK_ACCOUNT", "BANK_NO");
		month_pay_row.put("year", year);
		month_pay_row.put("month", month);
		month_pay_row.put("day", day);
		month_pay_row.put("real_money", String.valueOf(money));
//		month_pay_row.put("pay_status", "1");//1租客通过app已交租; 2先下交租;3押金抵扣;
		month_pay_row.put("money_type", "1");//1租金
		month_pay_row.put("to_account", to_account);
		month_pay_row.put("to_account_type", to_account_type);
		//计算订单号、交租金额
		String cur_time =DateUtil.getCurrentDateTime();
		String order_cur_time =cur_time.replace("-", "").replace(" ", "").replace(":", "");
		String order_no ="";
		String order_last_four_no =monthlyMoneyService.getOrderLastFourNo(cur_time);
		order_no =order_cur_time+order_last_four_no;
		month_pay_row =MapUtils.convertMaptoRowWithoutNullField(month_pay_row);
		month_pay_row.put("order_no", order_no);
		month_pay_row.put("order_pay_confirm", "0");
		monthlyMoneyService.update(month_pay_row);
		//将元转成分
		int money_fen =(int)(money*100);
        httpServletRequest.setAttribute("tradeAmount", money_fen);
        System.out.println("money_fen---->>"+money_fen);
        httpServletRequest.setAttribute("tradeAmountFen", NumberUtils.getTwoDecimalByDecimalFormat(money));
        httpServletRequest.setAttribute("tradeNum", order_no);
        return "payIndex";
    }

    @RequestMapping(value = "/pay/success")
    public String success(String order_no, HttpServletRequest httpServletRequest)
    {
    	logger.info("order_no---------------->>"+order_no);
    	monthlyMoneyService.updateOrderPayConfirmStatus(order_no,"1");
    	monthlyMoneyService.updateOrderPaySuccess(order_no,"1");
    	httpServletRequest.setAttribute("order_no", order_no);
    	return "paySuccess";
    }

    @RequestMapping(value = "/pay/fail")
    public String fail(String order_no, HttpServletRequest httpServletRequest)
    {
    	logger.info("order_no---------------->>"+order_no);
    	monthlyMoneyService.updateOrderPayConfirmStatus(order_no,"-1");
    	monthlyMoneyService.updateOrderNotPay(order_no);
    	httpServletRequest.setAttribute("order_no", order_no);
    	return "payFail";
    }
    
    @RequestMapping(value = "/pay/notify")
    public @ResponseBody  String notify(String order_no, HttpServletRequest httpServletRequest)
    {
    	logger.info("**********接收异步通知开始。**********");
        //获取通知原始信息
        String resp = request.getParameter("resp");
        logger.info("异步通知原始数据:"+resp);
        System.out.println("异步通知原始数据:"+resp);
        System.out.println("订单编号:"+resp);
        if(null == resp){
            return "failure" ;
        }
      //获取配置密钥
        String desKey = merchantConstant.getMerchantDESKey();
        String md5Key = merchantConstant.getMerchantMD5Key();
        logger.info("desKey:"+ desKey);
        logger.info("md5Key:"+ md5Key);
        try {
            //首先对Base64编码的数据进行解密
            byte[] decryptBASE64Arr =  BASE64.decode(resp);
            //解析XML
            AsynNotificationReqDto dto = parseXML(decryptBASE64Arr);
            logger.info("解析XML得到对象:"+ JsonUtil.write2JsonStr(dto));
            //验证签名
            String ownSign = generateSign(dto.getVersion(), dto.getMerchant(), dto.getTerminal(), dto.getData(), md5Key);
            logger.info("根据传输数据生成的签名:"+ownSign);
            if (!dto.getSign().equals(ownSign)) {
                //验签不对
                logger.info("签名验证错误!");
                throw new RuntimeException();
            }else{
                logger.info("签名验证正确!");
            }
            //验签成功，业务处理
            //对Data数据进行解密
            byte[] rsaKey = decryptBASE64(desKey);
            String decryptArr = DESUtil.decrypt(dto.getData(), rsaKey, "utf-8");
            //解密出来的数据也为XML文档，可以用dom4j解析
            logger.info("对<DATA>进行解密得到的数据:"+decryptArr);
            dto.setData(decryptArr);
            logger.info("最终数据:"+ JsonUtil.write2JsonStr(dto));
            logger.info("**********接收异步通知结束。**********");
            //处理业务逻辑
            org.w3c.dom.Document document =XmlUtil.toXml(decryptArr);
            //订单编号
            String id =document.getElementsByTagName("ID").item(0).getTextContent();
            //交易金额
            String amount =document.getElementsByTagName("AMOUNT").item(0).getTextContent();
            //支付处理状态成功：0 处理中：6 失败：7
            String status =document.getElementsByTagName("STATUS").item(0).getTextContent();
            logger.info("deal id ------>>"+id);
            logger.info("deal amount ------>>"+amount);
            logger.info("deal status ------>>"+status);
            System.out.println("deal id ------>>"+id);
            System.out.println("deal amount ------>>"+amount);
            System.out.println("deal status ------>>"+status);
            double am=Double.parseDouble(amount)/100;
    		String amount2=String.valueOf(am);
            String order_pay_confirm ="0";//0支付未通过银行确认1支付成功，已通过银行确认，-1支付失败
            String pay_status ="0";//0租客未交租; 1租客通过app已交租; 2线下交租;3押金抵扣;4平台已转账;5房东已到账;
            if(status.equals("0"))
            {
            	order_pay_confirm ="1";
            	pay_status ="1";
            	monthlyMoneyService.modifyOrderStateAfterBankInterfaceNotify(id,amount2,order_pay_confirm,pay_status);
            }
            else if(status.equals("7"))
            {
            	order_pay_confirm ="-1";
            	pay_status ="0";
            	monthlyMoneyService.modifyOrderStateAfterBankInterfaceNotify(id,amount2,order_pay_confirm,pay_status);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return "failure" ;
        }
    	return "success";
    }
    
  //XML解析为Java对象
    private static AsynNotificationReqDto parseXML(byte[] xmlString) {
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            InputStream is = new ByteArrayInputStream(xmlString);
            SAXReader sax = new SAXReader(false);
            document = sax.read(is);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        AsynNotificationReqDto dto = new AsynNotificationReqDto();
        Element rootElement = document.getRootElement();
        if (null == rootElement) {
            return dto;
        }
        Element versionEliment = rootElement.element("VERSION");
        if (null != versionEliment) {
            dto.setVersion(versionEliment.getText());
        }
        Element merchantEliment = rootElement.element("MERCHANT");
        if (null != merchantEliment) {
            dto.setMerchant(merchantEliment.getText());
        }
        Element terminalEliment = rootElement.element("TERMINAL");
        if (null != terminalEliment) {
            dto.setTerminal(terminalEliment.getText());
        }
        Element datalEliment = rootElement.element("DATA");
        if (null != datalEliment) {
            dto.setData(datalEliment.getText());
        }
        Element signEliment = rootElement.element("SIGN");
        if (null != signEliment) {
            dto.setSign(signEliment.getText());
        }
        return dto;
    }
    
    /**
     * 签名
     */
    public static String generateSign(String version, String merchant, String terminal, String data, String md5Key) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(version);
        sb.append(merchant);
        sb.append(terminal);
        sb.append(data);
        String sign = "";
        sign = md5(sb.toString(), md5Key);
        return sign;
    }
    
    
    public static String md5(String text, String salt) throws Exception {
        byte[] bytes = (text + salt).getBytes();

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(bytes);
        bytes = messageDigest.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] & 0xff) < 0x10) {
                sb.append("0");
            }
            sb.append(Long.toString(bytes[i] & 0xff, 16));
        }
        return sb.toString().toLowerCase();
    }
    
    
    //对Base64进行解密
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }
    
}
