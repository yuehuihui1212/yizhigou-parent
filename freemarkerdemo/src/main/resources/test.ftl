<html>
<head>
    <title>freemarker入门小demo</title>
    <meta charset="UTF-8">
</head>
<body>
<#include "head.ftl">
<#--我只是个注释-->
${name},你好，欢迎来到freemarker
<br>
<#assign linkName="喬先生">
聯繫人：${linkName}
<br>
<#assign info={'mobile':'1234445','address':'北京市吉利大學'}>
電話：${info.mobile} 住址：${info.address}


---商品價格表---<br>
<#list goodsList as goods>
    ${goods_index+1} 商品名稱：${goods.name}  商品價格：${goods.price} <br>
</#list>
</body>
</html>