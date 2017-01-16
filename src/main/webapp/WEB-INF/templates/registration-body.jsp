<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<form:form id="registration" modelAttribute="user" action="registration" method="POST" enctype="multipart/form-data">
    <div class="error">
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
    </div>

    <spring:message code="user.username"/>:<form:input class="field" path="username" value=""/>
    <form:errors path="username" cssClass="error" element="div"/><br/>
    <spring:message code="user.password"/>: <form:input class="field" path="password" value="" type="password"/>
    <form:errors path="password" cssClass="error" element="div"/><br/>
    <spring:message code="user.email"/>: <form:input class="field" path="email" value=""/>
    <form:errors path="email" cssClass="error" element="div"/><br/>
    <spring:message code="user.firstName"/>: <form:input class="field" path="firstName" value=""/>
    <form:errors path="firstName" cssClass="error" element="div"/><br/>
    <spring:message code="user.lastName"/>: <form:input class="field" path="lastName" value=""/>
    <form:errors path="lastName" cssClass="error" element="div"/><br/>

    <button id="registration_submit" type="submit"><spring:message code="submit"/></button>
</form:form>


