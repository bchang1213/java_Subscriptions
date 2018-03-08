<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Admin Page</title>
	<style>
		fieldset{
			width: 10em;
		}
	</style>
</head>
<body>
	<header>
		<h1>Hello Admin</h1>
	    <form id="logoutForm" method="POST" action="/logout">
	        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	        <input type="submit" value="Logout!" />
	    </form>		
	</header>
	<main>
		<table>
			<thead>
				<tr>
					<th>Name</th>
					<th>Next Due Date</th>
					<th>Amount Due</th>
					<th>Package Type</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items = "${allUsers}" var = "user">
				<tr>
					<td>${user.getFirst_name()} ${user.getLast_name()}</td>
					<td>${user.getDuedate()}</td>
					<td>$${user.getSubpackage().getCost()}.00</td>
					<td>${user.getSubpackage().getName()}</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<table>
			<thead>
				<tr>
					<th>Package Name</th>
					<th>Package Cost</th>
					<th>Availability</th>
					<th>Users</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items = "${allpacks}" var = "pack">
				<tr>
					<td>${pack.getName()}</td>
					<td>$${pack.getCost()}.00</td>
					<td>
					<c:if test="${pack.getStatus().equals(true)}">
					Available.
					</c:if>
					<c:if test="${pack.getStatus().equals(false)}">
					Unavailable.
					</c:if>
					</td>
					<td>${pack.getUsers().size()}</td>
					<td>
						<c:if test="${pack.getStatus().equals(false)}">
						<a href="/admin/packages/activate/${pack.getId()}">Activate</a>
						</c:if>
						<c:if test="${pack.getStatus().equals(true)}">
						<a href="/admin/packages/deactivate/${pack.getId()}">Deactivate</a>
						</c:if>
					</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
	<fieldset>
		<legend>Create package</legend>
		<form:form action="/admin/packages/new" method="POST" modelAttribute="pack">
			<p><form:errors path="pack.*"/></p>
			<form:label path="name">Package Name:
				<form:input type="text" path="name" />
			</form:label>
			
			<form:label path="cost">Cost:
				<form:input type="number" path="cost" name="cost" />	
			</form:label>
			
			<form:input type="hidden" path="status" value="false" />
			
			<input type="submit" />
		</form:form>
	</fieldset>
	</main>
</body>
</html>