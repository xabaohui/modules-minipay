<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>下单</title>
    

  </head>
  
  <body> 
  <div align="center"> 
   <form action="miniPay/createOrder" method="post">
   
      买家id  buyerId<input type="text" name="buyerId">  <br>
         productionId<input type="text" name="productionId">  <br>
        skuId<input type="text" name="skuId">  <br>
    单价    skuPrice<input type="text" name="skuPrice">  <br>
   数量     skuAmmount<input type="text" name="skuAmmount">  <br>
 商品名     productionName<input type="text" name="productionName">  <br>
    
    <input type="submit" value="提交">
   </form>
   
   </div>
  </body>
</html>
