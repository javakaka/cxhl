package com.cxhl.controller.shop;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cxhl.service.ShopCouponService;
import com.cxhl.service.ShopService;
import com.cxhl.service.ShopTypeService;
import com.cxhl.service.UserCouponService;
import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.ResponseVO;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;

@Controller("cxhlShopCouponController")
@RequestMapping("/cxhlpage/shop/coupon")
public class ShopCouponContrller  extends BaseController{
	
	@Resource(name = "cxhlShopTypeService")
	private ShopTypeService shopTypeService;
	
	@Resource(name = "cxhlShopService")
	private ShopService shopService;
	
	@Resource(name = "cxhlShopCouponService")
	private ShopCouponService shopCouponService;
	
	@Resource(name = "cxhlUserCouponService")
	private UserCouponService userCouponService;
	
	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(Pageable pageable, RedirectAttributes redirectAttributes,ModelMap model) {
		HttpSession session =getSession();
		Row staff =(Row)session.getAttribute("staff");
		if(staff == null)
		{
			addFlashMessage(redirectAttributes, Message.error("请先登录"));
			return "redirect:/login/ShopLogin.do";
		}
		String shop_id =staff.getString("shop_id", "");
		userCouponService.getRow().put("pageable", pageable);
		Page page = userCouponService.queryPageForShopAdmin(pageable,shop_id);
		model.addAttribute("page", page);
		return "/cxhlpage/shop/coupon/list";
	}
	
	@RequestMapping(value = "/createOrder")
	public String createOrder( RedirectAttributes redirectAttributes,ModelMap model) {
		HttpSession session =getSession();
		Row staff =(Row)session.getAttribute("staff");
		if(staff == null)
		{
			addFlashMessage(redirectAttributes, Message.error("请先登录"));
			return "redirect:/login/ShopLogin.do";
		}
		return "/cxhlpage/shop/coupon/CreateOrder";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveOrder")
	public @ResponseBody ResponseVO saveOrder(String data, RedirectAttributes redirectAttributes,ModelMap model) {
		ResponseVO ovo =new ResponseVO();
		HttpSession session =getSession();
		String errorMsg ="";
		Row staff =(Row)session.getAttribute("staff");
		if(staff == null)
		{
			ovo =new ResponseVO(-1, "请重新登录");
			return ovo;
		}
		if(StringUtils.isEmptyOrNull(data))
		{
			ovo =new ResponseVO(-1, "请求参数为空");
			return ovo;
		}
		String array[] =data.split(";");
		boolean isValid =true;
		DataSet batchDs =new DataSet();
		for(int i=0;i<array.length;i++)
		{
			String itemArray[] =array[i].split(",");
			String code =itemArray[0];
			String num =itemArray[1];
			//检查code 是否存在 num 是否足够
			Row row =userCouponService.findNotUsedByPayCode(code);
			if(row == null)
			{
				errorMsg +="消费验证码["+code+"]不存在;";
				isValid =false;
				break;
			}
			String coupon_num =row.getString("num","");
			int icoupon_num =Integer.parseInt(coupon_num);
			int inum =Integer.parseInt(num);
			int minus =icoupon_num -inum;
			if(minus < 0)
			{
				errorMsg +="消费验证码["+code+"]的库存不足，实际有["+icoupon_num+"],消费数量是["+inum+"]张；";
				isValid =false;
				break;
			}
			Row temp =new Row();
			temp.put("code", code);
			temp.put("num", num);
			batchDs.add(temp);
		}
		if(! isValid)
		{
			ovo =new ResponseVO(-1, "请求参数错误："+errorMsg);
			return ovo;
		}
		String sql ="";
		for(int i=0;i<batchDs.size();i++)
		{
			Row row =(Row)batchDs.get(i);
			String code =row.getString("code");
			String num =row.getString("num");
			sql ="update cxhl_user_coupon set num=num-"+num +" where pay_code='"+code+"' ";
			userCouponService.update(sql);
		}
		sql ="update cxhl_user_coupon set state=2 where num=0";
		userCouponService.update(sql);
		ovo =new ResponseVO(0, "支付成功");
		return ovo;
	}
	
	
}
