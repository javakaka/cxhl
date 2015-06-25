package com.cxhl.controller.admin;

import java.io.File;
import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cxhl.service.VersionService;
import com.ezcloud.framework.common.Setting;
import com.ezcloud.framework.controller.BaseController;
import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.page.jdbc.Page;
import com.ezcloud.framework.page.jdbc.Pageable;
import com.ezcloud.framework.service.system.SystemUpload;
import com.ezcloud.framework.util.MapUtils;
import com.ezcloud.framework.util.Message;
import com.ezcloud.framework.util.ResponseVO;
import com.ezcloud.framework.util.SettingUtils;
import com.ezcloud.framework.util.StringUtils;
import com.ezcloud.framework.vo.DataSet;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.utility.FileUtil;

@Controller("fzbPlatformVersionController")
@RequestMapping("/fzbpage/platform/version")
public class VersionContrller  extends BaseController{

	@Resource(name = "fzbVersionService")
	private VersionService versionService;
	
	@Resource(name = "frameworkUploadService")
	private SystemUpload systemUploadService;

	/**
	 * 分页查询
	 * @param pageable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/VersionList")
	public String list(String app_id,String device,Pageable pageable, ModelMap model) {
		Page page = versionService.queryPage(app_id,device,pageable);
		model.addAttribute("page", page);
		model.addAttribute("app_id", app_id);
		model.addAttribute("device", device);
		versionService.getRow().clear();
		return "/fzbpage/platform/version/VersionList";
	}
	
	/**
	 * 检查广告名称是否已存在
	 */
	@RequestMapping(value = "/check_name", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkAdName(String NAME) {
		if (StringUtils.isEmptyOrNull(NAME)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 检查广告名称是否已存在
	 */
	@RequestMapping(value = "/check_extra_name", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkAdExtraName(String ID,String NAME) {
		if (StringUtils.isEmptyOrNull(ID)) {
			return false;
		}
		if (StringUtils.isEmptyOrNull(NAME)) {
			return false;
		}
		return true;
	}
	
	@RequestMapping(value = "/add")
	public String add(ModelMap model) {
		return "/fzbpage/platform/version/add";
	}	

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody 
	ResponseVO save(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) throws JException {
		Row row =MapUtils.convertMaptoRowWithoutNullField(map);
		String id =row.getString("id","");
		if(StringUtils.isEmptyOrNull(id))
		{
			versionService.insert(row);
		}
		else
		{
			versionService.update(row);
		}
		id =row.getString("id","");
		ResponseVO ovo =new ResponseVO(0,"保存成功");
		System.out.println("------------------>>"+row);
		
		ovo.put("id", id);
		return ovo;
	}

	@RequestMapping(value = "/edit")
	public String edit(String id, ModelMap model) {
		Assert.notNull(id);
		model.addAttribute("row", versionService.findById(id));
		return "/fzbpage/platform/version/edit";
	}
	
	@RequestMapping(value = "/edit_version")
	public String edit_version(String id, ModelMap model) {
		Assert.notNull(id);
//		model.addAttribute("row", versionService.findById(id));
		model.addAttribute("id", id);
		return "/fzbpage/platform/version/EditVersion";
	}

	@RequestMapping(value = "/update")
	public String update(@RequestParam HashMap<String,String> map,RedirectAttributes redirectAttributes) {
		Row adRow=MapUtils.convertMaptoRowWithoutNullField(map);
		versionService.update(adRow);
		addFlashMessage(redirectAttributes,SUCCESS_MESSAGE);
		return "redirect:VersionList.do";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Message delete(Long[] ids) {
		versionService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
	/**
	 * 
	 * @param id
	 * @param deal_type
	 * @param type
	 * @param sub_type
	 * @return
	 */
	@RequestMapping(value = "/copyVersionFile")
	public @ResponseBody
	Message CopyVersionFile(String id,String deal_type,String type,String sub_type) {
		Assert.notNull(id,"id can not be null");
		Row version =versionService.findById(id);
		String app_id =version.getString("app_id","");
		String device =version.getString("device","");
		String is_current_version =version.getString("is_current_version","");
		Setting setting =SettingUtils.get();
		String dir =setting.getPhysicalPath();
		System.out.println("user dir------------------>>"+dir);
		String device_path =dir+"assets/";
		String device_file ="";
		if(is_current_version != null && is_current_version.equalsIgnoreCase("1"))
		{
			versionService.updateVersionStatus(app_id, device, id);
			DataSet fileList =systemUploadService.getAttachList(id, deal_type);
			if(fileList != null && fileList.size() > 0)
			{
				Row fileRow =(Row)fileList.get(0);
				//将此版本的文件拷贝到assets
				//ios
				if(device.equals("1"))
				{
					device_path =device_path +"ios/";
					//1房租宝房东租客版2中介版
					if(app_id != null && app_id.equals("1"))
					{
						device_file =device_path +"fangzubao.ipa";
					}
					else if(app_id != null && app_id.equals("2"))
					{
						device_file =device_path +"fangzubao-agent.ipa";
					}
				}
				//android
				else if(device.equals("2"))
				{
					device_path =device_path +  "android/";
					if(app_id != null && app_id.equals("1"))
					{
						device_file =device_path +"fangzubao.apk";
					}
					else if(app_id != null && app_id.equals("2"))
					{
						device_file =device_path +"fangzubao-agent.apk";
					}
					
				}
				//wp
				else if(device.equals("3"))
				{
					device_path =device_path +"wp/";
					if(app_id != null && app_id.equals("1"))
					{
						device_file =device_path +"fangzubao.wp";
					}
					else if(app_id != null && app_id.equals("2"))
					{
						device_file =device_path +"fangzubao-agent.wp";
					}
					
				}
				String cur_version_file =fileRow.getString("file_path","");
				if(StringUtils.isEmptyOrNull(cur_version_file))
				{
					return SUCCESS_MESSAGE;
				}
				
				File downloadFile =new File(device_file); 
				if(downloadFile.exists())
				{
					FileUtil.delete(device_file);
				}
				System.out.println("cur_version_file------------------>>"+cur_version_file);
				System.out.println("device_file------------------>>"+device_file);
				FileUtil.copy(cur_version_file, device_file);
			}   
		}
		return SUCCESS_MESSAGE;
	}
}
