<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Subscribe</title>
</head>
<body>
	<header>
		<h1>Welcome <c:out value="${currentUser.getFirst_name()}"/>! Subscribe!</h1>
		<p>Please choose a subscription and start date</p>
	</header>
	<main>
		<form:form action="/subscribe/${currentUser.getId()}" method="POST" modelAttribute="user">
		     <p><form:errors path="user.*"/></p>
		     
		     <form:label path="duedate">
		     	<form:input type="date" path="duedate"/>
		     </form:label>
		     
		     <form:label path="subpackage">
		     	<form:select name="package" path="subpackage">
		     	
		     	<c:forEach items = "${subpackages}" var = "subpack">	
		     		<option value="${subpack.getId()}">${subpack.getName()} ($${subpack.getCost()}.00)</option>
		     	</c:forEach>
		     	
		     	</form:select>
		     </form:label>
		     
		     <form:input type="hidden" value="${currentUser.getId()}" path="id"/>
		     
			<input type="submit" />
		</form:form>
	</main>
</body>
</html>