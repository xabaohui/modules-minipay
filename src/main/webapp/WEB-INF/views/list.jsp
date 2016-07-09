<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'list.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    <div align="center">
       这是订单${order.orderId }的信息   ,,,订单总价为${order.totalMoney} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="miniPay/payCheck?orderId=${order.orderId }&totalMoney=${order.totalMoney}">支付</a>  
                                          <br> <br>
         <c:forEach  items="${details }" var="detail">
               detailId: ${detail.detailId }<br>
               productionId:${detail.productionId }<br>
               skuId:${detail.skuId }<br>
               skuAmmount:${detail.skuAmmount }<br>
               skuName:${detail.productionName }<br>
               skuPrice:${detail.skuPrice }<br>
               status:${detail.status }<br>
               ------------------------------<br>
         </c:forEach>
         <hr/>
         再次添加商品： <br> 
         <form action="miniPay/addItem" method="post">
         <input type="hidden" name="orderId" value="${order.orderId }">
                 productionId<input type="text" name="productionId">  <br>
                 skuId<input type="text" name="skuId">  <br>
    单价    skuPrice<input type="text" name="skuPrice">  <br>
   数量     skuAmmount<input type="text" name="skuAmmount">  <br>
  商品名     productionName<input type="text" name="productionName">  <br>
  <input type="submit" value="再次添加">
         </form>
    </div>
  </body>
</html>
