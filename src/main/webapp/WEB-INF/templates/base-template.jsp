<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html" charset="UTF-8">
        <title>${title} | ÞΣԿb †﻿Ø|-|ҠØᛈ/\ØβUମ ∏Ø/\bᛈҠØՐØ</title>
        <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/images/favicon.ico" type="image/x-icon">
        <link rel="stylesheet/less" type="text/css" href="${pageContext.request.contextPath}/resources/css/main.less"/>
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/less.js"><jsp:text/></script>
        <link rel='stylesheet' href='/webjars/bootstrap/3.2.0/css/bootstrap.min.css'>
    </head>

    <body>
        <tiles:insertAttribute name="header"/>
        <div class="container">
                <tiles:insertAttribute name="body"/>
        </div>
        <tiles:insertAttribute name="footer"/>
    </body>
</html>