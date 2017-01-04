<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<form:form id="registration" modelAttribute="user" action="registration" method="POST" enctype="multipart/form-data">
    Username: <form:input class="field" path="username" value=""/><br/>
    Password: <form:input class="field" path="password" value="" type="password"/><br/>
    Email: <form:input class="field" path="email" value=""/><br/>
    First name: <form:input class="field" path="firstName" value=""/><br/>
    Last name: <form:input class="field" path="lastName" value=""/><br/>

    <button id="registration_submit" type="submit">Submit</button>
</form:form>



