<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<form name="loginForm" id="login" action="login" method="POST">

    <input class="field" title="" type="text" name="username">
    <input class="field" title="" type="password" name="password">
    <input type="checkbox" checked class="css-checkbox" id="remember-me" name="remember-me"/>
    <label for="remember-me" class="css-label lite-gray-check">Remember me</label>

    <button id="login_submit" type="submit">Submit</button>
    <input type="hidden" name="<c:out value="${_csrf.parameterName}"/>" value="<c:out value="${_csrf.token}"/>"/>
</form>
