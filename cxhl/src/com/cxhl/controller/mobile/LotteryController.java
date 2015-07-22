package com.cxhl.controller.mobile;

import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.LotteryCouponService;
import com.cxhl.service.LotteryGiftService;
import com.cxhl.service.LotteryRecordService;
import com.cxhl.service.ShopCouponService;
import com.cxhl.service.ShopGiftService;
import com.cxhl.service.UserCouponService;
import com.cxhl.service.UserGiftService;
import com.cxhl.service.UserLotteryNumService;
import com.cxhl.service.UserTokenService;
import com.ezcloud.framework.service.system.SystemConfigService;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
import com.ezcloud.utility.DateUtil;

/**
 * 客户端抽奖业务处理
 * 1 优惠券抽奖
 * 2 电台抽奖
 * 3 二维码扫描抽奖
 * @author Administrator
 */
@Controller("mobileLotteryController")
@RequestMapping("/api/user/lottery")
public class LotteryController extends BaseController {
	
	private static Logger logger = Logger.getLogger(LotteryController.class); 
	
	@Resource(name = "frameworkSystemConfigService")
	private SystemConfigService systemConfigService;
	
	@Resource(name = "cxhlLotteryGiftService")
	private LotteryGiftService lotteryGiftService;
	
	@Resource(name = "cxhlLotteryCouponService")
	private LotteryCouponService lotteryCouponService;
	
	@Resource(name = "cxhlUserTokenService")
	private UserTokenService userTokenService;
	
	@Resource(name = "cxhlUserLotteryNumService")
	private UserLotteryNumService userLotteryNumService;
	
	@Resource(name = "cxhlLotteryRecordService")
	private LotteryRecordService lotteryRecordService;
	
	@Resource(name = "cxhlShopCouponService")
	private ShopCouponService shopCouponService;
	
	@Resource(name = "cxhlShopGiftService")
	private ShopGiftService shopGiftService;
	
	@Resource(name = "cxhlUserCouponService")
	private UserCouponService userCouponService;
	@Resource(name = "cxhlUserGiftService")
	private UserGiftService userGiftService;
	
	/**
	 * 优惠券抽奖
	 * 每人每天可抽奖3次，分享之后，再获得一次抽奖机会（最多可抽奖4次）
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/coupon")
	public @ResponseBody
	String couponLottery(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		OVO ovo =null;
		String user_id =ivo.getString("user_id","");
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-11000,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row tokenRow =userTokenService.find(user_id);
		if(tokenRow == null)
		{
			ovo =new OVO(-11000,"用户未登录，请先登录","用户未登录，请先登录");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		logger.info("用户："+user_id+",开始进行优惠券抽奖."+DateUtil.getCurrentDateTime());
//		String page_size =ivo.getString("page_size","10");
		Row glableRow =getLotteryGloableInfo();
		if(glableRow == null)
		{
			ovo =new OVO(-11000,"系统未配置抽奖信息，请联系管理员","系统未配置抽奖信息，请联系管理员");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//保存抽奖记录到系统记录表
		String lottery_switch =glableRow.getString("lottery_switch","0");
		//判断抽奖次数是否已经达到3次
		String time =DateUtil.getCurrentDateTime();
		String date =time.substring(0, 10);//yyyy-mm-dd
		//1每日抽奖2电台抽奖3二维码抽奖
		Row lotteryNumRow =userLotteryNumService.findByUserId(user_id, date, "1");
		if(lotteryNumRow == null )
		{
			lotteryNumRow =new Row();
			lotteryNumRow.put("user_id", user_id);
			lotteryNumRow.put("date", date);
			lotteryNumRow.put("total_num", "3");
			lotteryNumRow.put("used_num", "0");
			lotteryNumRow.put("reward_type", "1");
			userLotteryNumService.insert(lotteryNumRow);
		}
		//减去相应的抽奖次数
		int total_num =Integer.parseInt(lotteryNumRow.getString("total_num","0"));
		int used_num =Integer.parseInt(lotteryNumRow.getString("used_num","0"));
		int minus =total_num - used_num;
		if(minus >0)
		{
			String sql ="update cxhl_lottery_num_controller set used_num=used_num+1 where user_id='"+user_id+"' and date='"+date+"' and reward_type='1' ";
			userLotteryNumService.update(sql);
		}
		else
		{
			ovo =new OVO(-11000,"已超过抽奖次数限制，请明天继续抽奖","已超过抽奖次数限制，请明天继续抽奖");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//返回抽奖结果
		ovo =new OVO(0,"抽奖完成","抽奖完成");
		if(lottery_switch.equals("0"))
		{
			// 1 中奖 0不中奖
			ovo.set("result", "0");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//计算当前已中奖的数量，包括当天、当月、当星期
		String cur_date =DateUtil.getCurrentDateTime();
		String year =cur_date.substring(0, 4);
		String month =cur_date.substring(5, 7);
		String day =cur_date.substring(8, 10);
		int day_total_num =lotteryRecordService.getCurrentDayRewardNum(year, month, day);
		int month_total_num =lotteryRecordService.getCurrentMonthRewardNum(year, month);
//		int week_total_num =1;
		String probability =glableRow.getString("probability","0");
		String day_num =glableRow.getString("day_num", "0");
//		String week_num =glableRow.getString("week_num",  "0");
		String month_num =glableRow.getString("month_num",  "0");
		double dprobability =Double.parseDouble(probability);
		int iday_num =Integer.parseInt(day_num);
//		int iweek_num =Integer.parseInt(week_num);
		int imonth_num =Integer.parseInt(month_num);
		//本月是否已经抽出设定的阀值
		if(month_total_num >= imonth_num)
		{
			// 1 中奖 0不中奖
			ovo.set("result", "0");
			//保存抽奖记录
			Row lotteryRecordRow =new Row();
			lotteryRecordRow.put("user_id", user_id);
			lotteryRecordRow.put("lottery_type", "1");
			lotteryRecordRow.put("is_win","0" );
			lotteryRecordRow.put("year", year);
			lotteryRecordRow.put("month", month);
			lotteryRecordRow.put("day", day);
			lotteryRecordService.insert(lotteryRecordRow);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//本周是否已经抽出设定的阀值
		//本日是否已经抽出设定的阀值
		if(day_total_num >= iday_num)
		{
			// 1 中奖 0不中奖
			ovo.set("result", "0");
			//保存抽奖记录
			Row lotteryRecordRow =new Row();
			lotteryRecordRow.put("user_id", user_id);
			lotteryRecordRow.put("lottery_type", "1");
			lotteryRecordRow.put("is_win","0" );
			lotteryRecordRow.put("year", year);
			lotteryRecordRow.put("month", month);
			lotteryRecordRow.put("day", day);
			lotteryRecordService.insert(lotteryRecordRow);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//根据概率计算是否中奖
		int right_result =5;//默认按十分之一的概率抽奖
		int lottery_result =0;
		Random random =new Random();
		if(probability.equals("0.5"))
		{
			right_result =1;
			lottery_result =random.nextInt(2);
		}
		else if(probability.equals("0.25"))
		{
			right_result =1;
			lottery_result =random.nextInt(4);
		}
		else if(probability.equals("0.1"))
		{
			right_result =5;
			lottery_result =random.nextInt(10);
		}
		else if(probability.equals("0.01"))
		{
			right_result =50;
			lottery_result =random.nextInt(100);
		}
		else if(probability.equals("0.001"))
		{
			right_result =500;
			lottery_result =random.nextInt(1000);
		}
		else if(probability.equals("0.0001"))
		{
			right_result =5000;
			lottery_result =random.nextInt(10000);
		}
		logger.info("抽奖结果-============》》"+lottery_result+"<>"+right_result);
		//不中奖
		if(lottery_result != right_result)
		{
			ovo.set("result", "0");
			//保存抽奖记录
			Row lotteryRecordRow =new Row();
			lotteryRecordRow.put("user_id", user_id);
			lotteryRecordRow.put("lottery_type", "1");
			lotteryRecordRow.put("is_win","0" );
			lotteryRecordRow.put("year", year);
			lotteryRecordRow.put("month", month);
			lotteryRecordRow.put("day", day);
			lotteryRecordService.insert(lotteryRecordRow);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//选取奖品,先查出优惠券和奖品，再随机分配是优惠券还是奖品
		DataSet coupon_list =lotteryCouponService.getCanLotteryCouponList(year,month);
		DataSet gift_list =lotteryGiftService.getCanLotteryGiftList(year,month);
		if((coupon_list == null || coupon_list.size()==0) && (gift_list == null || gift_list.size()==0))
		{
			ovo.set("result", "0");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		int lottery_choice=random.nextInt(2);
		int lottery_result_index =0;
		int lottery_size =0;
		//送优惠券，从可抽奖列表随机选取一个
		if(lottery_choice == 0)
		{
			ovo.set("result", "1");
			ovo.set("reward_type", "1");//1优惠券2礼品
			lottery_size =coupon_list.size();
			lottery_result_index =random.nextInt(lottery_size);
			Row couponRow =(Row)coupon_list.get(lottery_result_index);
			//获取优惠券详情
			String coupon_id =couponRow.getString("coupon_id","");
			Row detailRow =shopCouponService.findDetail(coupon_id);
			String raw_price =detailRow.getString("raw_price","0");
			String file_path =detailRow.getString("file_path","");
//			String coupon_price =detailRow.getString("coupon_price","0");
			ovo.set("reward_id",coupon_id);//优惠券编号
			ovo.set("price",raw_price);//优惠券金额 
			ovo.set("file_path",file_path);//优惠券图片
			//减少奖池中的优惠券的数量（减一），增加用户的优惠券
			int left_num =Integer.parseInt(couponRow.getString("left_num","0"));
			left_num =left_num-1;
			couponRow.put("left_num", left_num);
			couponRow =MapUtils.convertMaptoRowWithoutNullField(couponRow);
			lotteryCouponService.update(couponRow);
			// add to user coupon
			Row userCouponRow =new Row();
			userCouponRow.put("user_id", user_id);
			userCouponRow.put("coupon_id", coupon_id);
			userCouponRow.put("state", "1");//0未支付1未使用2已使用3已过期
			userCouponRow.put("price", raw_price);
			userCouponRow.put("num", 1);
			userCouponRow.put("channel", "1");//1刮奖获得2购买获得3赠送
			String pay_code =userCouponService.getPayCode();
			userCouponRow.put("pay_code", pay_code);
			userCouponService.insert(userCouponRow);
			//保存抽奖记录
			Row lotteryRecordRow =new Row();
			lotteryRecordRow.put("user_id", user_id);
			lotteryRecordRow.put("lottery_type", "1");
			lotteryRecordRow.put("is_win","1" );
			lotteryRecordRow.put("year", year);
			lotteryRecordRow.put("month", month);
			lotteryRecordRow.put("day", day);
			lotteryRecordRow.put("reward_type", "1");
			lotteryRecordRow.put("reward_id", coupon_id);
			lotteryRecordService.insert(lotteryRecordRow);
		}
		//送礼品，从可抽奖列表随机选取一个
		else if(lottery_choice == 1)
		{
			ovo.set("result", "1");
			ovo.set("reward_type", "2");//1优惠券2礼品
			lottery_size =gift_list.size();
			lottery_result_index =random.nextInt(lottery_size);
			Row giftRow =(Row)gift_list.get(lottery_result_index);
			//获取礼品详情
			String gift_id =giftRow.getString("gift_id");
			Row giftDetailRow =shopGiftService.findDetail(gift_id);
			ovo.set("gift_id", gift_id);
			ovo.set("gift_name", giftDetailRow.getString("name",""));
			ovo.set("file_path", giftDetailRow.getString("file_path",""));
			//减少奖池中的礼品的数量（减一），增加用户的礼品
			int left_num =Integer.parseInt(giftRow.getString("left_num","0"));
			left_num =left_num-1;
			giftRow.put("left_num", left_num);
			giftRow =MapUtils.convertMaptoRowWithoutNullField(giftRow);
			lotteryGiftService.update(giftRow);
			//add to user gift
			Row userGiftRow =userGiftService.find(user_id, gift_id);
			if(userGiftRow == null)
			{
				userGiftRow =new Row();
				userGiftRow.put("user_id", user_id);
				userGiftRow.put("gift_id", gift_id);
				userGiftRow.put("shop_id", giftDetailRow.getString("shop_id",""));
				userGiftRow.put("total_num", 1);
				userGiftRow.put("exchange_num", 0);
				userGiftRow.put("left_num", 1);
				userGiftService.insert(userGiftRow);
			}
			else
			{
				int gtotal_num =Integer.parseInt(userGiftRow.getString("total_num","0"));
				int gleft_num =Integer.parseInt(userGiftRow.getString("left_num","0"));
				gtotal_num =gtotal_num+1;
				gleft_num =gleft_num+1;
				userGiftRow.put("total_num", gtotal_num);
				userGiftRow.put("left_num", gleft_num);
				userGiftRow=MapUtils.convertMaptoRowWithoutNullField(userGiftRow);
				userGiftService.update(userGiftRow);
			}
			//保存抽奖记录
			Row lotteryRecordRow =new Row();
			lotteryRecordRow.put("user_id", user_id);
			lotteryRecordRow.put("lottery_type", "1");
			lotteryRecordRow.put("is_win","1" );
			lotteryRecordRow.put("year", year);
			lotteryRecordRow.put("month", month);
			lotteryRecordRow.put("day", day);
			lotteryRecordRow.put("reward_type", "2");
			lotteryRecordRow.put("reward_id", gift_id);
			lotteryRecordService.insert(lotteryRecordRow);
		}
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 电台抽奖
	 * 抽奖时间段内，不限次数抽奖
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/radio")
	public @ResponseBody
	String radioLottery(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		OVO ovo =null;
		String user_id =ivo.getString("user_id","");
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-11000,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row tokenRow =userTokenService.find(user_id);
		if(tokenRow == null)
		{
			ovo =new OVO(-11000,"用户未登录，请先登录","用户未登录，请先登录");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		logger.info("用户："+user_id+",开始进行二维码抽奖."+DateUtil.getCurrentDateTime());
//		String page_size =ivo.getString("page_size","10");
		Row glableRow =getLotteryGloableInfo();
		if(glableRow == null)
		{
			ovo =new OVO(-11000,"系统未配置抽奖信息，请联系管理员","系统未配置抽奖信息，请联系管理员");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//保存抽奖记录到系统记录表
		String lottery_switch =glableRow.getString("lottery_switch","0");
		//判断抽奖次数是否已经达到1次
		String time =DateUtil.getCurrentDateTime();
		String date =time.substring(0, 10);//yyyy-mm-dd
//		计算当前时间是否在设定的抽奖时间段内
		String radio_times =glableRow.getString("radio_times","");
		if(StringUtils.isEmptyOrNull(radio_times) || radio_times.replace(" ", "").length()==0)
		{
			ovo =new OVO(-11000,"电台抽奖时间无效,请联系管理员","电台抽奖时间无效,请联系管理员");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String time_arr[] =radio_times.split(";");
		String begin_end_time_arr []=null;
		boolean isTimeValid =false;
		for(int i=0;i<time_arr.length; i++)
		{
			begin_end_time_arr =time_arr[i].split(",");
			String begin_time =begin_end_time_arr[0]+":00";
			String end_time =begin_end_time_arr[1]+":00";
			String current_time =DateUtil.getCurrentDateTime();
			String cur_date =current_time.substring(0, 10);
			begin_time =cur_date+" "+begin_time;
			end_time =cur_date+" "+end_time;
//			DateUtil.compare(begin_time, current_time, sdf)
			//当前时间是否大于指定的开始时间
			long minus_begin =DateUtil.compare(current_time, begin_time);
			//结束时间是否大于当前时间
			long minus_end =DateUtil.compare(end_time, current_time);
			if(minus_begin>0 && minus_end>0)
			{
				isTimeValid =true;
				break;
			}
		}
		if( ! isTimeValid)
		{
			ovo =new OVO(-11000,"当前时间不在电台抽奖有效时间段内","当前时间不在电台抽奖有效时间段内");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
//		123
		//当天、当月、当星期
		String cur_date =DateUtil.getCurrentDateTime();
		String year =cur_date.substring(0, 4);
		String month =cur_date.substring(5, 7);
		String day =cur_date.substring(8, 10);
		//返回抽奖结果
		ovo =new OVO(0,"抽奖完成","抽奖完成");
		if(lottery_switch.equals("0"))
		{
			// 1 中奖 0不中奖
			ovo.set("result", "0");
			//保存抽奖记录
			Row lotteryRecordRow =new Row();
			lotteryRecordRow.put("user_id", user_id);
			lotteryRecordRow.put("lottery_type", "1");
			lotteryRecordRow.put("is_win","0" );
			lotteryRecordRow.put("year", year);
			lotteryRecordRow.put("month", month);
			lotteryRecordRow.put("day", day);
			lotteryRecordService.insert(lotteryRecordRow);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		int day_total_num =lotteryRecordService.getCurrentDayRewardNum(year, month, day);
		int month_total_num =lotteryRecordService.getCurrentMonthRewardNum(year, month);
//		int week_total_num =1;
		String probability =glableRow.getString("probability","0");
		String day_num =glableRow.getString("day_num", "0");
//		String week_num =glableRow.getString("week_num",  "0");
		String month_num =glableRow.getString("month_num",  "0");
		double dprobability =Double.parseDouble(probability);
		int iday_num =Integer.parseInt(day_num);
//		int iweek_num =Integer.parseInt(week_num);
		int imonth_num =Integer.parseInt(month_num);
		//本月是否已经抽出设定的阀值
		if(month_total_num >= imonth_num)
		{
			// 1 中奖 0不中奖
			ovo.set("result", "0");
			//保存抽奖记录
			Row lotteryRecordRow =new Row();
			lotteryRecordRow.put("user_id", user_id);
			lotteryRecordRow.put("lottery_type", "1");
			lotteryRecordRow.put("is_win","0" );
			lotteryRecordRow.put("year", year);
			lotteryRecordRow.put("month", month);
			lotteryRecordRow.put("day", day);
			lotteryRecordService.insert(lotteryRecordRow);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//本周是否已经抽出设定的阀值
		//本日是否已经抽出设定的阀值
		if(day_total_num >= iday_num)
		{
			// 1 中奖 0不中奖
			ovo.set("result", "0");
			//保存抽奖记录
			Row lotteryRecordRow =new Row();
			lotteryRecordRow.put("user_id", user_id);
			lotteryRecordRow.put("lottery_type", "1");
			lotteryRecordRow.put("is_win","0" );
			lotteryRecordRow.put("year", year);
			lotteryRecordRow.put("month", month);
			lotteryRecordRow.put("day", day);
			lotteryRecordService.insert(lotteryRecordRow);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//根据概率计算是否中奖
		int right_result =5;//默认按十分之一的概率抽奖
		int lottery_result =0;
		Random random =new Random();
		if(probability.equals("0.5"))
		{
			right_result =1;
			lottery_result =random.nextInt(2);
		}
		else if(probability.equals("0.25"))
		{
			right_result =1;
			lottery_result =random.nextInt(4);
		}
		else if(probability.equals("0.1"))
		{
			right_result =5;
			lottery_result =random.nextInt(10);
		}
		else if(probability.equals("0.01"))
		{
			right_result =50;
			lottery_result =random.nextInt(100);
		}
		else if(probability.equals("0.001"))
		{
			right_result =500;
			lottery_result =random.nextInt(1000);
		}
		else if(probability.equals("0.0001"))
		{
			right_result =5000;
			lottery_result =random.nextInt(10000);
		}
		//不中奖
		if(lottery_result != right_result)
		{
			ovo.set("result", "0");
			//保存抽奖记录
			Row lotteryRecordRow =new Row();
			lotteryRecordRow.put("user_id", user_id);
			lotteryRecordRow.put("lottery_type", "1");
			lotteryRecordRow.put("is_win","0" );
			lotteryRecordRow.put("year", year);
			lotteryRecordRow.put("month", month);
			lotteryRecordRow.put("day", day);
			lotteryRecordService.insert(lotteryRecordRow);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//选取奖品,先查出优惠券和奖品，再随机分配是优惠券还是奖品
		DataSet coupon_list =lotteryCouponService.getCanLotteryCouponList(year,month);
		DataSet gift_list =lotteryGiftService.getCanLotteryGiftList(year,month);
		if((coupon_list == null || coupon_list.size()==0) && (gift_list == null || gift_list.size()==0))
		{
			ovo.set("result", "0");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		int lottery_choice=random.nextInt(2);
		int lottery_result_index =0;
		int lottery_size =0;
		//送优惠券，从可抽奖列表随机选取一个
		if(lottery_choice == 0)
		{
			ovo.set("result", "1");
			ovo.set("reward_type", "1");//1优惠券2礼品
			lottery_size =coupon_list.size();
			lottery_result_index =random.nextInt(lottery_size);
			Row couponRow =(Row)coupon_list.get(lottery_result_index);
			//获取优惠券详情
			String coupon_id =couponRow.getString("coupon_id","");
			Row detailRow =shopCouponService.findDetail(coupon_id);
			String raw_price =detailRow.getString("raw_price","0");
			String file_path =detailRow.getString("file_path","");
//			String coupon_price =detailRow.getString("coupon_price","0");
			ovo.set("reward_id",coupon_id);//优惠券编号
			ovo.set("price",raw_price);//优惠券金额 
			ovo.set("file_path",file_path);//优惠券图片
			//减少奖池中的优惠券的数量（减一），增加用户的优惠券
			int left_num =Integer.parseInt(couponRow.getString("left_num","0"));
			left_num =left_num-1;
			couponRow.put("left_num", left_num);
			couponRow =MapUtils.convertMaptoRowWithoutNullField(couponRow);
			lotteryCouponService.update(couponRow);
			// add to user coupon
			Row userCouponRow =new Row();
			userCouponRow.put("user_id", user_id);
			userCouponRow.put("coupon_id", coupon_id);
			userCouponRow.put("state", "1");//0未支付1未使用2已使用3已过期
			userCouponRow.put("price", raw_price);
			userCouponRow.put("num", 1);
			userCouponRow.put("channel", "1");//1刮奖获得2购买获得3赠送
			String pay_code =userCouponService.getPayCode();
			userCouponRow.put("pay_code", pay_code);
			userCouponService.insert(userCouponRow);
			//保存抽奖记录
			Row lotteryRecordRow =new Row();
			lotteryRecordRow.put("user_id", user_id);
			lotteryRecordRow.put("lottery_type", "1");
			lotteryRecordRow.put("is_win","1" );
			lotteryRecordRow.put("year", year);
			lotteryRecordRow.put("month", month);
			lotteryRecordRow.put("day", day);
			lotteryRecordRow.put("reward_type", "1");
			lotteryRecordRow.put("reward_id", coupon_id);
		}
		//送礼品，从可抽奖列表随机选取一个
		else if(lottery_choice == 1)
		{
			ovo.set("result", "1");
			ovo.set("reward_type", "2");//1优惠券2礼品
			lottery_size =gift_list.size();
			lottery_result_index =random.nextInt(lottery_size);
			Row giftRow =(Row)gift_list.get(lottery_result_index);
			//获取礼品详情
			String gift_id =giftRow.getString("gift_id");
			Row giftDetailRow =shopGiftService.findDetail(gift_id);
			ovo.set("gift_id", gift_id);
			ovo.set("gift_name", giftDetailRow.getString("name",""));
			ovo.set("file_path", giftDetailRow.getString("file_path",""));
			//减少奖池中的礼品的数量（减一），增加用户的礼品
			int left_num =Integer.parseInt(giftRow.getString("left_num","0"));
			left_num =left_num-1;
			giftRow.put("left_num", left_num);
			giftRow =MapUtils.convertMaptoRowWithoutNullField(giftRow);
			lotteryGiftService.update(giftRow);
			//add to user gift
			Row userGiftRow =userGiftService.find(user_id, gift_id);
			if(userGiftRow == null)
			{
				userGiftRow =new Row();
				userGiftRow.put("user_id", user_id);
				userGiftRow.put("gift_id", gift_id);
				userGiftRow.put("shop_id", giftDetailRow.getString("shop_id",""));
				userGiftRow.put("total_num", 1);
				userGiftRow.put("exchange_num", 0);
				userGiftRow.put("left_num", 1);
				userGiftService.insert(userGiftRow);
			}
			else
			{
				int gtotal_num =Integer.parseInt(userGiftRow.getString("total_num","0"));
				int gleft_num =Integer.parseInt(userGiftRow.getString("left_num","0"));
				gtotal_num =gtotal_num+1;
				gleft_num =gleft_num+1;
				userGiftRow.put("total_num", gtotal_num);
				userGiftRow.put("left_num", gleft_num);
				userGiftRow=MapUtils.convertMaptoRowWithoutNullField(userGiftRow);
				userGiftService.update(userGiftRow);
			}
			//保存抽奖记录
			Row lotteryRecordRow =new Row();
			lotteryRecordRow.put("user_id", user_id);
			lotteryRecordRow.put("lottery_type", "1");
			lotteryRecordRow.put("is_win","1" );
			lotteryRecordRow.put("year", year);
			lotteryRecordRow.put("month", month);
			lotteryRecordRow.put("day", day);
			lotteryRecordRow.put("reward_type", "2");
			lotteryRecordRow.put("reward_id", gift_id);
		}
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 二维码抽奖
	 * 每人每天一次
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/qrcode")
	public @ResponseBody
	String qrcodeLottery(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		OVO ovo =null;
		String user_id =ivo.getString("user_id","");
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-11000,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row tokenRow =userTokenService.find(user_id);
		if(tokenRow == null)
		{
			ovo =new OVO(-11000,"用户未登录，请先登录","用户未登录，请先登录");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		logger.info("用户："+user_id+",开始进行二维码抽奖."+DateUtil.getCurrentDateTime());
//		String page_size =ivo.getString("page_size","10");
		Row glableRow =getLotteryGloableInfo();
		if(glableRow == null)
		{
			ovo =new OVO(-11000,"系统未配置抽奖信息，请联系管理员","系统未配置抽奖信息，请联系管理员");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//保存抽奖记录到系统记录表
		String lottery_switch =glableRow.getString("lottery_switch","0");
		//判断抽奖次数是否已经达到1次
		String time =DateUtil.getCurrentDateTime();
		String date =time.substring(0, 10);//yyyy-mm-dd
		//1每日抽奖2电台抽奖3二维码抽奖
		Row lotteryNumRow =userLotteryNumService.findByUserId(user_id, date, "3");
		if(lotteryNumRow == null )
		{
			lotteryNumRow =new Row();
			lotteryNumRow.put("user_id", user_id);
			lotteryNumRow.put("date", date);
			lotteryNumRow.put("total_num", "1");
			lotteryNumRow.put("used_num", "0");
			lotteryNumRow.put("reward_type", "3");
			userLotteryNumService.insert(lotteryNumRow);
		}
		//减去相应的抽奖次数
		int total_num =Integer.parseInt(lotteryNumRow.getString("total_num","0"));
		int used_num =Integer.parseInt(lotteryNumRow.getString("used_num","0"));
		int minus =total_num - used_num;
		if(minus >0)
		{
			String sql ="update cxhl_lottery_num_controller set used_num=used_num+1 where user_id='"+user_id+"' and date='"+date+"' and reward_type='3' ";
			userLotteryNumService.update(sql);
		}
		else
		{
			ovo =new OVO(-11000,"已超过抽奖次数限制，请明天继续抽奖","已超过抽奖次数限制，请明天继续抽奖");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//返回抽奖结果
		ovo =new OVO(0,"抽奖完成","抽奖完成");
		if(lottery_switch.equals("0"))
		{
			// 1 中奖 0不中奖
			ovo.set("result", "0");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//计算当前已中奖的数量，包括当天、当月、当星期
		String cur_date =DateUtil.getCurrentDateTime();
		String year =cur_date.substring(0, 4);
		String month =cur_date.substring(5, 7);
		String day =cur_date.substring(8, 10);
		int day_total_num =lotteryRecordService.getCurrentDayRewardNum(year, month, day);
		int month_total_num =lotteryRecordService.getCurrentMonthRewardNum(year, month);
//		int week_total_num =1;
		String probability =glableRow.getString("probability","0");
		String day_num =glableRow.getString("day_num", "0");
//		String week_num =glableRow.getString("week_num",  "0");
		String month_num =glableRow.getString("month_num",  "0");
		double dprobability =Double.parseDouble(probability);
		int iday_num =Integer.parseInt(day_num);
//		int iweek_num =Integer.parseInt(week_num);
		int imonth_num =Integer.parseInt(month_num);
		//本月是否已经抽出设定的阀值
		if(month_total_num >= imonth_num)
		{
			// 1 中奖 0不中奖
			ovo.set("result", "0");
			//保存抽奖记录
			Row lotteryRecordRow =new Row();
			lotteryRecordRow.put("user_id", user_id);
			lotteryRecordRow.put("lottery_type", "1");
			lotteryRecordRow.put("is_win","0" );
			lotteryRecordRow.put("year", year);
			lotteryRecordRow.put("month", month);
			lotteryRecordRow.put("day", day);
			lotteryRecordService.insert(lotteryRecordRow);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//本周是否已经抽出设定的阀值
		//本日是否已经抽出设定的阀值
		if(day_total_num >= iday_num )
		{
			// 1 中奖 0不中奖
			ovo.set("result", "0");
			//保存抽奖记录
			Row lotteryRecordRow =new Row();
			lotteryRecordRow.put("user_id", user_id);
			lotteryRecordRow.put("lottery_type", "1");
			lotteryRecordRow.put("is_win","0" );
			lotteryRecordRow.put("year", year);
			lotteryRecordRow.put("month", month);
			lotteryRecordRow.put("day", day);
			lotteryRecordService.insert(lotteryRecordRow);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//根据概率计算是否中奖
		int right_result =5;//默认按十分之一的概率抽奖
		int lottery_result =0;
		Random random =new Random();
		if(probability.equals("0.5"))
		{
			right_result =1;
			lottery_result =random.nextInt(2);
		}
		else if(probability.equals("0.25"))
		{
			right_result =1;
			lottery_result =random.nextInt(4);
		}
		else if(probability.equals("0.1"))
		{
			right_result =5;
			lottery_result =random.nextInt(10);
		}
		else if(probability.equals("0.01"))
		{
			right_result =50;
			lottery_result =random.nextInt(100);
		}
		else if(probability.equals("0.001"))
		{
			right_result =500;
			lottery_result =random.nextInt(1000);
		}
		else if(probability.equals("0.0001"))
		{
			right_result =5000;
			lottery_result =random.nextInt(10000);
		}
		//不中奖
		if(lottery_result != right_result)
		{
			ovo.set("result", "0");
			//保存抽奖记录
			Row lotteryRecordRow =new Row();
			lotteryRecordRow.put("user_id", user_id);
			lotteryRecordRow.put("lottery_type", "1");
			lotteryRecordRow.put("is_win","0" );
			lotteryRecordRow.put("year", year);
			lotteryRecordRow.put("month", month);
			lotteryRecordRow.put("day", day);
			lotteryRecordService.insert(lotteryRecordRow);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//选取奖品,先查出优惠券和奖品，再随机分配是优惠券还是奖品
		DataSet coupon_list =lotteryCouponService.getCanLotteryCouponList(year,month);
		DataSet gift_list =lotteryGiftService.getCanLotteryGiftList(year,month);
		if((coupon_list == null || coupon_list.size()==0) && (gift_list == null || gift_list.size()==0))
		{
			ovo.set("result", "0");
			//保存抽奖记录
			Row lotteryRecordRow =new Row();
			lotteryRecordRow.put("user_id", user_id);
			lotteryRecordRow.put("lottery_type", "1");
			lotteryRecordRow.put("is_win","0" );
			lotteryRecordRow.put("year", year);
			lotteryRecordRow.put("month", month);
			lotteryRecordRow.put("day", day);
			lotteryRecordService.insert(lotteryRecordRow);
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		int lottery_choice=random.nextInt(2);
		int lottery_result_index =0;
		int lottery_size =0;
		//送优惠券，从可抽奖列表随机选取一个
		if(lottery_choice == 0)
		{
			ovo.set("result", "1");
			ovo.set("reward_type", "1");//1优惠券2礼品
			lottery_size =coupon_list.size();
			lottery_result_index =random.nextInt(lottery_size);
			Row couponRow =(Row)coupon_list.get(lottery_result_index);
			//获取优惠券详情
			String coupon_id =couponRow.getString("coupon_id","");
			Row detailRow =shopCouponService.findDetail(coupon_id);
			String raw_price =detailRow.getString("raw_price","0");
			String file_path =detailRow.getString("file_path","");
//			String coupon_price =detailRow.getString("coupon_price","0");
			ovo.set("reward_id",coupon_id);//优惠券编号
			ovo.set("price",raw_price);//优惠券金额 
			ovo.set("file_path",file_path);//优惠券图片
			//减少奖池中的优惠券的数量（减一），增加用户的优惠券
			int left_num =Integer.parseInt(couponRow.getString("left_num","0"));
			left_num =left_num-1;
			couponRow.put("left_num", left_num);
			couponRow =MapUtils.convertMaptoRowWithoutNullField(couponRow);
			lotteryCouponService.update(couponRow);
			// add to user coupon
			Row userCouponRow =new Row();
			userCouponRow.put("user_id", user_id);
			userCouponRow.put("coupon_id", coupon_id);
			userCouponRow.put("state", "1");//0未支付1未使用2已使用3已过期
			userCouponRow.put("price", raw_price);
			userCouponRow.put("num", 1);
			userCouponRow.put("channel", "1");//1刮奖获得2购买获得3赠送
			String pay_code =userCouponService.getPayCode();
			userCouponRow.put("pay_code", pay_code);
			userCouponService.insert(userCouponRow);
			//保存抽奖记录
			Row lotteryRecordRow =new Row();
			lotteryRecordRow.put("user_id", user_id);
			lotteryRecordRow.put("lottery_type", "1");
			lotteryRecordRow.put("is_win","1" );
			lotteryRecordRow.put("year", year);
			lotteryRecordRow.put("month", month);
			lotteryRecordRow.put("day", day);
			lotteryRecordRow.put("reward_type", "1");
			lotteryRecordRow.put("reward_id", coupon_id);
			lotteryRecordService.insert(lotteryRecordRow);
		}
		//送礼品，从可抽奖列表随机选取一个
		else if(lottery_choice == 1)
		{
			ovo.set("result", "1");
			ovo.set("reward_type", "2");//1优惠券2礼品
			lottery_size =gift_list.size();
			lottery_result_index =random.nextInt(lottery_size);
			Row giftRow =(Row)gift_list.get(lottery_result_index);
			//获取礼品详情
			String gift_id =giftRow.getString("gift_id");
			Row giftDetailRow =shopGiftService.findDetail(gift_id);
			ovo.set("gift_id", gift_id);
			ovo.set("gift_name", giftDetailRow.getString("name",""));
			ovo.set("file_path", giftDetailRow.getString("file_path",""));
			//减少奖池中的礼品的数量（减一），增加用户的礼品
			int left_num =Integer.parseInt(giftRow.getString("left_num","0"));
			left_num =left_num-1;
			giftRow.put("left_num", left_num);
			giftRow =MapUtils.convertMaptoRowWithoutNullField(giftRow);
			lotteryGiftService.update(giftRow);
			//add to user gift
			Row userGiftRow =userGiftService.find(user_id, gift_id);
			if(userGiftRow == null)
			{
				userGiftRow =new Row();
				userGiftRow.put("user_id", user_id);
				userGiftRow.put("gift_id", gift_id);
				userGiftRow.put("shop_id", giftDetailRow.getString("shop_id",""));
				userGiftRow.put("total_num", 1);
				userGiftRow.put("exchange_num", 0);
				userGiftRow.put("left_num", 1);
				userGiftService.insert(userGiftRow);
			}
			else
			{
				int gtotal_num =Integer.parseInt(userGiftRow.getString("total_num","0"));
				int gleft_num =Integer.parseInt(userGiftRow.getString("left_num","0"));
				gtotal_num =gtotal_num+1;
				gleft_num =gleft_num+1;
				userGiftRow.put("total_num", gtotal_num);
				userGiftRow.put("left_num", gleft_num);
				userGiftRow=MapUtils.convertMaptoRowWithoutNullField(userGiftRow);
				userGiftService.update(userGiftRow);
			}
			//保存抽奖记录
			Row lotteryRecordRow =new Row();
			lotteryRecordRow.put("user_id", user_id);
			lotteryRecordRow.put("lottery_type", "1");
			lotteryRecordRow.put("is_win","1" );
			lotteryRecordRow.put("year", year);
			lotteryRecordRow.put("month", month);
			lotteryRecordRow.put("day", day);
			lotteryRecordRow.put("reward_type", "2");
			lotteryRecordRow.put("reward_id", gift_id);
			lotteryRecordService.insert(lotteryRecordRow);
		}
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 获取抽奖全局配置信息
	 * @return
	 */
	public Row getLotteryGloableInfo()
	{
		Row globalRow =new Row();
		DataSet ds =systemConfigService.getConfigData("CXHL_LOTTERY");
//		抽奖开关 1开启0关闭
		String lottery_switch ="";
//		中奖概率
		String probability ="";
//		每天中奖次数
		String day_num ="";
//		周中奖次数
		String week_num ="";
//		月中奖次数
		String month_num ="";
		String radio_times ="";
		String busi_code="";
		if( ds != null && ds.size()>0 )
		{
			for(int i=0; i< ds.size(); i++)
			{
				Row row =(Row)ds.get(i);
				busi_code =row.getString("busi_code","");
				if(busi_code.equals("SWITCH"))
				{
					lottery_switch =row.getString("busi_code_set","");
				}
				else if(busi_code.equals("PROBABILITY"))
				{
					probability =row.getString("busi_code_set","");
				}
				else if(busi_code.equals("DAY_NUM"))
				{
					day_num =row.getString("busi_code_set","");
				}
				else if(busi_code.equals("WEEK_NUM"))
				{
					week_num =row.getString("busi_code_set","");
				}
				else if(busi_code.equals("MONTH_NUM"))
				{
					month_num =row.getString("busi_code_set","");
				}
				else if(busi_code.equals("RADIO_TIMES"))
				{
					radio_times =row.getString("busi_code_set","");
				}
			}
		}
		globalRow.put("lottery_switch", lottery_switch);
		globalRow.put("probability", probability);
		globalRow.put("day_num", day_num);
		globalRow.put("week_num", week_num);
		globalRow.put("month_num", month_num);
		globalRow.put("radio_times", radio_times);
		return globalRow;
	}
	
//	
	/**
	 * 查询用户的优惠券抽奖次数，已抽奖次数、剩余抽奖次数
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="/lottery_num")
	public @ResponseBody
	String getUserLotteryNum(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		OVO ovo =null;
		String user_id =ivo.getString("user_id","");
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-11000,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row tokenRow =userTokenService.find(user_id);
		if(tokenRow == null)
		{
			ovo =new OVO(-11000,"用户未登录，请先登录","用户未登录，请先登录");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		//判断抽奖次数是否已经达到3次
		String time =DateUtil.getCurrentDateTime();
		String date =time.substring(0, 10);//yyyy-mm-dd
		Row row =userLotteryNumService.findByUserId(user_id, date, "1");
		int used_num =0;
		int total_num =3;
		int left_num =0;
		if(row == null)
		{
			used_num =0;
			total_num =3;
		}
		else
		{
			used_num =Integer.parseInt(row.getString("used_num","0"));
			total_num =Integer.parseInt(row.getString("total_num","0"));
		}
		left_num =total_num -used_num;
		ovo =new OVO(0,"操作成功","操作成功");
		ovo.set("used_num", used_num);
		ovo.set("left_num", left_num);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	@RequestMapping(value ="/list")
	public @ResponseBody
	String getUserLotteryList(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		OVO ovo =null;
		String user_id =ivo.getString("user_id","");
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-11000,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row tokenRow =userTokenService.find(user_id);
		if(tokenRow == null)
		{
			ovo =new OVO(-11000,"用户未登录，请先登录","用户未登录，请先登录");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet ds =lotteryRecordService.queryUserList(user_id, Integer.parseInt(page), Integer.parseInt(page_size));
		ovo =new OVO(0,"操作成功","操作成功");
		ovo.set("list", ds);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
}