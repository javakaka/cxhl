<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Encode Test</title>
</head>
<body>
<% 
String sms ="1074吃香喝辣带您免费吃遍南宁，感谢您的注册，您的验证码是：888888【吃香喝辣】";
String sSend_UTF8 = new String(sms.getBytes(), "UTF-8");
String sSend_GBK = new String(sms.getBytes(), "GBK");
String sSend_IOS = new String(sms.getBytes("iso-8859-1"), "GBK");
String sJava_ENCODING = System.getProperty("file.encoding");
%>
===================>><%=sms %><br/>
===================>><%=sSend_UTF8 %><br/>
===================>><%=sSend_GBK %><br/>
===================>><%=sSend_IOS %><br/>
===================>><%=sJava_ENCODING %><br/>
</body>
</html>