package com.cxhl.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ezcloud.framework.common.Setting;
import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.plugin.pay.Unifiedorder;
import com.ezcloud.framework.service.Service;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.FieldUtil;
import com.ezcloud.framework.util.SettingUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.util.XmlUtil;
import com.ezcloud.framework.vo.IVO;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.StringUtil;

/**   
 * @author shike001 
 * E-mail:510836102@qq.com   
 */
@Component("cxhlWeiXinAppPayService")
public class WeiXinAppPayService extends Service {

	@Resource(name = "cxhlUserService")
	private UserService userService;
	
	@Resource(name = "cxhlOrderService")
	private OrderService orderService;
	
	public WeiXinAppPayService() 
	{
		
	}
	
/**
 * 字段名		变量名			必填	类型		示例值					描述
公众账号ID		appid			是	String(32)	wx8888888888888888			微信分配的公众账号ID
商户号		mch_id			是	String(32)	1900000109				微信支付分配的商户号
设备号		device_info		否	String(32)	013467007045764				终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
随机字符串		nonce_str		是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位。推荐随机数生成算法
签名			sign			是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	签名，详见签名生成算法
商品描述		body			是	String(32)	Ipad mini  16G  白色		商品或支付单简要描述
商品详情		detail			否	String(8192)	Ipad mini  16G  白色		商品名称明细列表
附加数据		attach			否	String(127)	说明	附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
商户订单号		out_trade_no	是	String(32)	1217752501201407033233368018	商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
货币类型		fee_type		否	String(16)	CNY	符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
总金额		total_fee		是	Int	888	订单总金额，只能为整数，详见支付金额
终端IP		spbill_create_ip是	String(16)	8.8.8.8	APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
交易起始时间	time_start		否	String(14)	20091225091010	订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
交易结束时间	time_expire		否	String(14)	20091227091010	订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则
注意：最短失效时间间隔必须大于5分钟
商品标记		goods_tag		否	String(32)	WXG		商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
通知地址		notify_url		是	String(256)	http://www.baidu.com/	接收微信支付异步通知回调地址
交易类型		trade_type		是	String(16)	JSAPI	取值如下：JSAPI，NATIVE，APP，WAP,详细说明见参数规定
商品ID		product_id		否	String(32)	12235413214070356458058	trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
用户标识		openid			否	String(128)	oUpF8uMuAJO_M2pxb1Q9zNjWeS6o	trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。
 * 
 * 共10个必填字段
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * @param ivo
 * @return
 * @throws Exception 
 */
	public OVO validate(IVO ivo) throws Exception
	{
		OVO ovo =new OVO();
		String user_id =ivo.getString("user_id");
		String order_id =ivo.getString("order_id");
		String app_ip =ivo.getString("app_ip");
		Row userRow =userService.find(user_id);
		if(userRow == null)
		{
			ovo =new OVO(-20011,"用户不存在","用户不存在");
			return ovo;
		}
		Row orderRow =orderService.find(order_id);
		if(orderRow == null)
		{
			ovo =new OVO(-20011,"订单不存在","订单不存在");
			return ovo;
		}
		String order_no=orderRow.getString("order_no");
		String order_state =orderRow.getString("state","");
		if(order_state.equals("2"))
		{
			ovo =new OVO(-20011,"订单已完成之后，不能重复支付","订单已完成之后，不能重复支付");
			return ovo;
		}
		Unifiedorder unifiiedorder =new Unifiedorder();
		unifiiedorder.setAppid("wx44e3ee46a26f4e21");
		unifiiedorder.setMch_id("1251662201");
		String nonce_str =StringUtil.getRandKeys(28).toUpperCase();
		unifiiedorder.setNonce_str(nonce_str);
		String sign ="";
		String body ="订单:"+order_no+"支付备注";
		String out_trade_no =order_no;
		unifiiedorder.setOut_trade_no(out_trade_no);
		unifiiedorder.setBody(body);
		String fee_type ="CNY";
		unifiiedorder.setFee_type(fee_type);
		String total_fee ="0";
		String money =orderRow.getString("money");
		double dmoney =Double.parseDouble(money);
		dmoney =dmoney*100;//(分)
		total_fee =StringUtils.subStrBeforeDotNotIncludeDot(String.valueOf(dmoney));
		unifiiedorder.setTotal_fee(total_fee);
		String spbill_create_ip=app_ip;
		unifiiedorder.setSpbill_create_ip(spbill_create_ip);
		Setting setting =SettingUtils.get();
		String site_url =setting.getSiteUrl();
		String notify_url =site_url+"/notify/weixin/pay/app.do";
		notify_url +="?order_no="+AesUtil.encode(order_no);
		unifiiedorder.setNotify_url(notify_url);
		String trade_type ="APP";
		unifiiedorder.setTrade_type(trade_type);
		String secret ="ced7357666afb8b245e68a610a9c50da";
		unifiiedorder.calculateSign(unifiiedorder, secret);
		String xml =unifiiedorder.getXmlStr();
		System.out.println("weixin xml====>>"+xml);
		ovo.set("status", "test");
		System.out.println("test...........");
		return ovo;
	}
}