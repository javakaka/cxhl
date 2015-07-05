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
<title>编辑商家分类</title>
<link href="<%=basePath%>/res/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=basePath%>/res/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/jquery.validate.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/input.js"></script>
<script type="text/javascript" src="<%=basePath%>/res/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {
	var $inputForm = $("#inputForm");
	
	${flash_message}
	// 表单验证
	$inputForm.validate({
		rules: {
			ID: "required",
			NAME: {
				required: "required",
				/** remote: {
					url: "<%=basePath%>cxhlpage/platform/member/profile/check_telephone.do",
					cache: false
				}
				*/
			},
			LEVEL_INDEX: false
		},
		messages:{
			NAME:{
				required: "名称不能为空",
				remote: ""
			}
		}
	});
	
});
</script>
</head>
<body>
	<div class="path">
		商家管理 &raquo; 编辑商家分类
	</div>
	<form id="inputForm" action="update.do" method="post">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>分类名称:
				</th>
				<td>
					<input type="text" name="NAME" class="text" maxlength="200" value="${row.NAME }" />
					<input type="hidden" name="ID" class="text" maxlength="200" value="${row.ID }" />
				</td>
			</tr>
			<tr>
				<th>
					排序:
				</th>
				<td>
					<input type="text" name="LEVEL_INDEX" class="text" maxlength="200" value="${row.LEVEL_INDEX }" />
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
</html>