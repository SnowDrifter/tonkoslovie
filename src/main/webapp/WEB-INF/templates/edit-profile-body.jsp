<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<form:form id="edit" modelAttribute="user" method="POST" enctype="multipart/form-data">

    <div class="error">
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
    </div>

    <div class="message">
        <c:if test="${not empty message}">
            <div class="message">${message}</div>
        </c:if>
    </div>

    <spring:message code="user.firstName"/>: <form:input class="field" path="firstName" value=""/>
    <form:errors path="firstName" cssClass="error" element="div"/><br/>

    <spring:message code="user.lastName"/>: <form:input class="field" path="lastName" value=""/>
    <form:errors path="lastName" cssClass="error" element="div"/><br/>

    <form:hidden path="id"/>
    <form:hidden path="email"/>
    <form:hidden path="username"/>
    <form:hidden path="password"/>

    <button id="edit_submit" type="submit"><spring:message code="submit"/></button>
</form:form>