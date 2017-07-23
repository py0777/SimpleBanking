<%@page import= "sb.service.om.OmAcnoGen"%>
<%@page import="nexcore.framework.core.data.IRecordSet"%>
<%@page import="nexcore.framework.core.util.StringUtils"%>
<%@page import= "nexcore.framework.core.data.RecordSet"%>
<%@ page import="nexcore.framework.core.data.DataSet, nexcore.framework.core.data.IDataSet" language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
String result = null;

IRecordSet resultRs = new RecordSet("RS_LIST", new String[]{"ACNO","DACA", "CUST_NM", "OPRT_TR_DTM"});
int resultRsCnt = 0;
String resultMessage = "";
request.setCharacterEncoding("UTF-8");/* 한글깨짐 현상 없애기 위함*/

IDataSet requestData = new DataSet();

OmAcnoGen oag = new OmAcnoGen();

IDataSet responseData = oag.omAcnoGen(requestData);


%>

<form action="Test.jsp" id ="myForm" name="myForm" method="POST" >
<input type="submit" value="변환" >
</form>
</body>
</html>