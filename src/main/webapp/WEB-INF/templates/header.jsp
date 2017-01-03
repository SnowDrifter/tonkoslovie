<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<header>
    <security:authorize access="isAnonymous()">
        <a href="${pageContext.request.contextPath}/login">Login</a>
        <a href="${pageContext.request.contextPath}/registration">Registration</a>
    </security:authorize>
    <security:authorize access="isAuthenticated()">
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </security:authorize>
</header>