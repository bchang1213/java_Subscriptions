<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>WElcome</title>
</head>
<body>
    <form id="logoutForm" method="POST" action="/logout">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="submit" value="Logout!" />
    </form>	
	<h1>Welcome <c:out value="${currentUser.getFirst_name()} ${currentUser.getLast_name()}"/></h1>
	<p>Current package: <c:out value="${currentUser.getSubpackage().getName()}"/></p>
	<p>Next due date: <c:out value="${currentUser.getDuedate()}"/></p>
	<p>Amount Due: <c:out value="$${currentUser.getSubpackage().getCost()}.00"/></p>
	<p>User Since: <c:out value="${currentUser.getCreatedAt()}"/></p>

</body>
</html>