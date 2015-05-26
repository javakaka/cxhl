package com.mcn.interceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ezcloud.framework.exp.JException;
import com.ezcloud.framework.service.system.Bureau;
import com.ezcloud.framework.util.AesUtil;
import com.ezcloud.framework.vo.IVO;
import com.ezcloud.framework.vo.OVO;
import com.ezcloud.framework.vo.Row;
import com.ezcloud.framework.vo.VOConvert;

/**
 * 移动端接口拦截器，主要用于将post数据流解析成请求对象
 * @author JianBoTong
 *
 */
public class ApiInterceptor  implements HandlerInterceptor{

	private OVO ovo =null;
	
	@Resource(name = "frameworkSystemBureauService")
	private Bureau bureau;
	
	public ApiInterceptor() {  
		
    } 
	
	private String mappingURL;//利用正则映射到需要拦截的路径    
    public void setMappingURL(String mappingURL) {    
           this.mappingURL = mappingURL;    
   }   
    
    /** 
     * 在DispatcherServlet完全处理完请求后被调用  
     *   当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion() 
     */  
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception arg3)
			throws Exception {
		System.out.println("==============执行顺序: 3、afterCompletion================");
		System.out.println("============== type ================"+response.getCharacterEncoding());
	}

	//在业务处理器处理请求执行完成后,生成视图之前执行的动作
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler, ModelAndView modelAndView) throws Exception {
		System.out.println("==============执行顺序: 2、postHandle================");
	}

	/** 
     * 在业务处理器处理请求之前被调用 
     * 如果返回false 
     *     从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链 
     *  
     * 如果返回true 
     *    执行下一个拦截器,直到所有的拦截器都执行完毕 
     *    再执行被拦截的Controller 
     *    然后进入拦截器链, 
     *    从最后一个拦截器往回执行所有的postHandle() 
     *    接着再从最后一个拦截器往回执行所有的afterCompletion() 
     */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		System.out.println("==============执行顺序: 1、preHandle================");
		String url=request.getRequestURL().toString();    
//        if(mappingURL==null || url.matches(mappingURL)){    
//            request.getRequestDispatcher("/msg.jsp").forward(request, response);  
//            return false;   
//        }  
        IVO ivo =proccessRequest(request);
        //检查是否有token 参数
        String token =ivo.getString("token",null);
        if(token == null || token.replace(" ", "").length() ==0)
        {
        	ovo =new OVO();
        	ovo.iCode =-10001;
        	ovo.sExp ="token 不能为空";
        	ovo.sMsg ="token 不能为空";
        	sendError(response, ovo);
        	return false;   
        }
        else
        {
        	token =URLDecoder.decode(token);
        	token =AesUtil.decode(token);
        	bureau.getRow().put("id", token);
        	Row row =bureau.find();
        	if(row == null || row.getString("bureau_no",null) == null)
        	{
        		ovo =new OVO();
            	ovo.iCode =-10002;
            	ovo.sExp ="非法token";
            	ovo.sMsg ="非法token";
            	sendError(response, ovo);
            	return false;   
        	}
        	ivo.set("token", token);
        }
        request.setAttribute("ivo", ivo);
        return true;  
	}

	/**
	 * 将请求的post数据解析到请求对象
	 * @param request
	 * @throws JException 
	 */
	public IVO proccessRequest(HttpServletRequest request) throws JException
	{
		String sRequestJson =null;
		BufferedReader oBufferedReader = null;

	    if (request.getAttribute("_CLIENT_XML") != null)
	    {
	    	sRequestJson = request.getAttribute("_CLIENT_XML").toString();
	    }
	    else
	    {
	      String str2 ="";
	      String strLine ="";
	      try {
	        oBufferedReader = request.getReader();
	        strLine = oBufferedReader.readLine();
	        str2 = "";
	       while (strLine != null)
	        {
	          str2 = str2 + strLine;
	          strLine = oBufferedReader.readLine();
	        }
	      }
	      catch (IOException e)
	      {
	        e.printStackTrace();
				throw new JException(-1, "读取客户端的数据出错", e);
	      }
	      sRequestJson = str2;
	    }

	    try
	    {
	      System.out.print("客户端请求数据------------>>"+sRequestJson);
	      sRequestJson = URLDecoder.decode(sRequestJson, "UTF-8");
	      System.out.print("客户端请求数据------------>>"+sRequestJson);
	      //sRequestXml = StringUtil.gbkToISO(sRequestXml, true);
	    }
	    catch (Exception localException1)
	    {
	      throw new JException(-1, "客户端传来的参数为空", localException1);
	    }
	    if (sRequestJson == null)
	    {
	      throw new JException(-1, "客户端传来的参数为空");
	    }
	    
	    IVO iivo = VOConvert.jsonToIvo(sRequestJson);
	    System.out.print("ivo ====>>"+iivo);
	    return iivo;
	}
	
	public void sendError(HttpServletResponse response,OVO ovo) throws IOException, JException
	{
		PrintWriter oPrintWriter = null;
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("pragma", "no-cache");
		response.setCharacterEncoding("UTF-8");
		oPrintWriter = response.getWriter();
		String msg =VOConvert.ovoToJson(ovo);
		oPrintWriter.write(msg);
	}
}
