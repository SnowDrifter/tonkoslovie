<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<header>
    <security:authorize access="isAnonymous()">
        <span><a href="${pageContext.request.contextPath}/login"> <spring:message code="menu.login"/></a></span>
        <span><a href="${pageContext.request.contextPath}/registration"> <spring:message code="menu.registration"/></a></span>
    </security:authorize>

    <security:authorize access="isAuthenticated()">
        <span><a href="${pageContext.request.contextPath}/edit"><spring:message code="menu.edit"/></a></span>
        <span><a href="${pageContext.request.contextPath}/logout"> <spring:message code="menu.logout"/></a></span>
    </security:authorize>
</header>