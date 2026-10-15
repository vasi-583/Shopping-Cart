<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
<h2>Registered Users</h2>
<div class="table-wrap">
<table>
  <tr><th>ID</th><th>Name</th><th>Email</th><th>Role</th><th>Joined</th></tr>
  <c:forEach var="u" items="${users}">
    <tr>
      <td>${u.userId}</td>
      <td><c:out value="${u.name}"/></td>
      <td><c:out value="${u.email}"/></td>
      <td>${u.role}</td>
      <td><fmt:formatDate value="${u.createdAt}" pattern="dd MMM yyyy"/></td>
    </tr>
  </c:forEach>
</table>
</div>
<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>