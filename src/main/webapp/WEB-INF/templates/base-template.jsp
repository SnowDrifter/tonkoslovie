<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE html>
<html>
<head>
</head>

<body>
    <div>
        <tiles:insertAttribute name="header"/>
        <div class="content">
            <tiles:insertAttribute name="body"/>
        </div>
    </div>

    <tiles:insertAttribute name="footer"/>
</body>
</html>