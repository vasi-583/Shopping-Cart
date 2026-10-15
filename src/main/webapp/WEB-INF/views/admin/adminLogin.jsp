<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
<h2>Admin Login</h2>
<form class="form" method="post" action="${ctx}/admin/login">
  <label>Email <input type="email" name="email" required></label>
  <label>Password <input type="password" name="password" required></label>
  <button type="submit">Login</button>
</form>
<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>