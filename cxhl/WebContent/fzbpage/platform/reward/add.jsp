<%@ page language="java" import="java.util.*" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/cctaglib" prefix="cc"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>录入奖励</title>
<link href="<%=basePath%>/res/admin/css/common_pop.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/input.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/datePicker/WdatePicker.js"></script>
<link href="<%=basePath%>/res/css/diymen/tipswindown.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/css/diymen/tipswindown.js?version=1.4"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	
	//[@flash_message /]
	${flash_message}
	// 表单验证
	$inputForm.validate({
		rules: {
			USER_ID: "required",
			PRICE: "required"
		}
	});
	
});
</script>
</head>
<body>
	<div class="path">
		中介管理 &raquo;录入奖励
	</div>
	<form id="inputForm" action="save.do" method="post">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>请选择人员:
				</th>
				<td>
					<input type="hidden" id="USER_ID" name="USER_ID" class="text" maxlength="200" value="" readonly />
					<input type="text" id="USER_NAME" name="USER_NAME" class="text" maxlength="200" value="" readonly />
					<img id="select_target_id" name="select_target_id" src="<%=basePath%>res/images/select_window.gif" onclick="selectTarget();" />
				</td>
			</tr>
			<tr>
				<th>
					奖励名称:
				</th>
				<td>
					<input type="text" name="NAME" class="text" maxlength="200" value="租房中介奖励"/>
				</td>
			</tr>
			<tr>
				<th>
					奖励数量:
				</th>
				<td>
					<input type="text" name="NUM" class="text" maxlength="200"  value="1"/>
				</td>
			</tr>
			<tr>
				<th>
					奖励金额:
				</th>
				<td>
					<input type="text" name="PRICE" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					状态:
				</th>
				<td>
					<select id="STATUS" name="STATUS" class="text" style="width:190px;" >
						<option value="1" selected>录入</option>
						<option value="2" >已提现</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" class="button" value="<cc:message key="admin.common.submit" />" />
					<input type="button" id="backButton" class="button" value="<cc:message key="admin.common.back" />" />
				</td>
			</tr>
		</table>
	</form>
</body>
<script type="text/javascript">
var title;
	var url;
	var width=520;
	var height=400;
	var drag="true";
	var time="";
	var showBg="true";
	var cssName="leotheme";
	var iframeName="selectIframeId";
	function popWindow(title,url,width,height,drag,time,showBg,cssName,iframeName)
	{
		tipsWindown(title,url,width,height,drag,time,showBg,cssName,iframeName);
	}

	 function closeTipWindow()
	 {
		tipsWindown.close();
	 }

	 function iframeSelectTarget()
	 {
	 	//var iframe =window.frames["selectWindowIframeId"].document.selectTarget;
	 	//window.frames["selectWindowIframeId"].document.selectTarget();
	 	window.frames["selectWindowIframeId"].selectTarget();
	 	
	 }
	 
	 function selectTarget(){
		title ="选择人员";
		url="iframe:<%=basePath%>/fzbpage/platform/agent/SelectAgent.do?id=";
		iframeName="selectWindowIframeId";
		popWindow(title,url,width,height,drag,time,showBg,cssName,iframeName);
	}

	function setSelectedWindow(id,target)
	{
		$("#USER_ID").val(id);
		$("#USER_NAME").val(target);
	}
</script>
</html>