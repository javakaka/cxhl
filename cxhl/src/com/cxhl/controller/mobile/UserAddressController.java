package com.cxhl.controller.mobile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxhl.service.UserAddressService;
import com.cxhl.service.UserService;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;
/**
 * 用户收货地址
 * @author TongJianbo
 *
 */
@Controller("mobileUserAddressController")
@RequestMapping("/api/user/address")
public class UserAddressController extends BaseController {
	
	private static Logger logger = Logger.getLogger(UserAddressController.class); 
	
	@Resource(name = "cxhlUserService")
	private UserService userService;
	
	@Resource(name = "cxhlUserAddressService")
	private UserAddressService userAddressService;
	
	/**
	 * 用户分页查询自己的收货地址列表
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/list")
	public @ResponseBody
	String queryPage(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String user_id =ivo.getString("user_id",null);
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-1,"","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String page =ivo.getString("page","1");
		String page_size =ivo.getString("page_size","10");
		DataSet list =userAddressService.list(user_id, page, page_size);
		ovo =new OVO(0,"","");
		ovo.set("list", list);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	@RequestMapping(value ="/add")
	public @ResponseBody
	String add(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String user_id =ivo.getString("user_id","");
		if(StringUtils.isEmptyOrNull(user_id))
		{
			ovo =new OVO(-1,"用户编号不能为空","用户编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row userRow =userService.find(user_id);
		if(userRow == null)
		{
			ovo =new OVO(-1,"用户不存在","用户不存在");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String province_id =ivo.getString("province_id","");
		if(StringUtils.isEmptyOrNull(province_id))
		{
			ovo =new OVO(-1,"省份编号不能为空","省份编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String city_id =ivo.getString("city_id","");
		if(StringUtils.isEmptyOrNull(city_id))
		{
			ovo =new OVO(-1,"城市编号不能为空","城市编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String region_id =ivo.getString("region_id","");
		String address =ivo.getString("address","");
		if(StringUtils.isEmptyOrNull(address))
		{
			ovo =new OVO(-1,"详细地址不能为空","详细地址不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String receive_name =ivo.getString("receive_name","");
		if(StringUtils.isEmptyOrNull(receive_name))
		{
			ovo =new OVO(-1,"收货人姓名不能为空","收货人姓名不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String receive_tel =ivo.getString("receive_tel","");
		if(StringUtils.isEmptyOrNull(receive_tel))
		{
			ovo =new OVO(-1,"收货人电话不能为空","收货人电话不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String zip_code =ivo.getString("zip_code","");
		if(StringUtils.isEmptyOrNull(zip_code))
		{
			ovo =new OVO(-1,"邮编不能为空","邮编不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String is_default =ivo.getString("is_default","");
		if(StringUtils.isEmptyOrNull(is_default))
		{
			ovo =new OVO(-1,"是否是默认收货地址不能为空（1是默认0否）","是否是默认收货地址不能为空（1是默认0否）");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row row =new Row();
		row.put("user_id", user_id);
		row.put("province_id", province_id);
		row.put("city_id", city_id);
		row.put("region_id", region_id);
		row.put("address", address);
		row.put("receive_name", receive_name);
		row.put("receive_tel", receive_tel);
		row.put("zip_code", zip_code);
		row.put("is_default",is_default );
		if(is_default.equals("1"))
		{
			//
			userAddressService.updateUserDefaultAddressByUserId(user_id);
		}
		userAddressService.insert(row);
		ovo =new OVO(0,"操作成功","");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	
	/**
	 * 查询收货地址详情
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/detail")
	public @ResponseBody
	String find(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("查询收货地址详情");
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"编号不能为空","编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row row = userAddressService.find(id);
		String province_id =row.getString("province_id","");
		String province_name =row.getString("province_name","");
		String city_id =row.getString("city_id","");
		String city_name =row.getString("city_name","");
		String region_id =row.getString("region_id","");
		String region_name =row.getString("region_name","");
		String address =row.getString("address","");
		String receive_name =row.getString("receive_name","");
		String receive_tel =row.getString("receive_tel","");
		String zip_code =row.getString("zip_code","");
		String is_default =row.getString("is_default","");
		ovo =new OVO(0,"操作成功","");
		ovo.set("id", id);
		ovo.set("province_id", province_id);
		ovo.set("province_name", province_name);
		ovo.set("city_id", city_id);
		ovo.set("city_name", city_name);
		ovo.set("region_id", region_id);
		ovo.set("region_name", region_name);
		ovo.set("address", address);
		ovo.set("receive_name", receive_name);
		ovo.set("receive_tel", receive_tel);
		ovo.set("zip_code", zip_code);
		ovo.set("is_default", is_default);
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * 修改收货地址详情
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/update")
	public @ResponseBody
	String update(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		logger.info("修改收货地址详情");
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"编号不能为空","编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String province_id =ivo.getString("province_id","");
		if(StringUtils.isEmptyOrNull(province_id))
		{
			ovo =new OVO(-1,"省份编号不能为空","省份编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String city_id =ivo.getString("city_id","");
		if(StringUtils.isEmptyOrNull(city_id))
		{
			ovo =new OVO(-1,"城市编号不能为空","城市编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String region_id =ivo.getString("region_id","");
//		if(StringUtils.isEmptyOrNull(region_id))
//		{
//			ovo =new OVO(-1,"辖区编号不能为空","辖区编号不能为空");
//			return AesUtil.encode(VOConvert.ovoToJson(ovo));
//		}
		String address =ivo.getString("address","");
		if(StringUtils.isEmptyOrNull(address))
		{
			ovo =new OVO(-1,"详细地址不能为空","详细地址不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String receive_name =ivo.getString("receive_name","");
		if(StringUtils.isEmptyOrNull(receive_name))
		{
			ovo =new OVO(-1,"收货人姓名不能为空","收货人姓名不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String receive_tel =ivo.getString("receive_tel","");
		if(StringUtils.isEmptyOrNull(receive_tel))
		{
			ovo =new OVO(-1,"收货人电话不能为空","收货人电话不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String zip_code =ivo.getString("zip_code","");
		if(StringUtils.isEmptyOrNull(zip_code))
		{
			ovo =new OVO(-1,"邮编不能为空","邮编不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		String is_default =ivo.getString("is_default","");
		if(StringUtils.isEmptyOrNull(is_default))
		{
			ovo =new OVO(-1,"是否是默认收货地址不能为空（1是默认0否）","是否是默认收货地址不能为空（1是默认0否）");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		Row row =new Row();
		row.put("id", id);
		row.put("province_id", province_id);
		row.put("city_id", city_id);
		row.put("region_id", region_id);
		row.put("address", address);
		row.put("receive_name", receive_name);
		row.put("receive_tel", receive_tel);
		row.put("zip_code", zip_code);
		row.put("is_default",is_default );
		if(is_default.equals("1"))
		{
			//
			userAddressService.updateUserDefaultAddressById(id);
		}
		userAddressService.update(row);
		ovo =new OVO(0,"操作成功","");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}
	
	/**
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value ="/delete")
	public @ResponseBody
	String delete(HttpServletRequest request) throws Exception
	{
		parseRequest(request);
		String id =ivo.getString("id",null);
		if(StringUtils.isEmptyOrNull(id))
		{
			ovo =new OVO(-1,"编号不能为空","编号不能为空");
			return AesUtil.encode(VOConvert.ovoToJson(ovo));
		}
		userAddressService.delete(id);
		ovo =new OVO(0,"操作成功","");
		return AesUtil.encode(VOConvert.ovoToJson(ovo));
	}

	
}
