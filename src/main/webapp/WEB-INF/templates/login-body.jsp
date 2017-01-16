<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<form name="loginForm" id="login" action="login" method="POST">
    <div class="error">
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
    </div>

    <spring:message code="user.username"/>:
    <input class="field" title="" type="text" name="username"><br/>
    <spring:message code="user.password"/>:
    <input class="field" title="" type="password" name="password"><br/>
    <label for="remember-me" class="css-label lite-gray-check"><spring:message code="login.rememberMe"/></label>
    <input type="checkbox" checked class="css-checkbox" id="remember-me" name="remember-me"/><br/>

    <button id="login_submit" type="submit"><spring:message code="submit"/></button>
    <input type="hidden" name="<c:out value="${_csrf.parameterName}"/>" value="<c:out value="${_csrf.token}"/>"/>
</form>
