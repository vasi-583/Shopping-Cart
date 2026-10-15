<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
<h2>Create Account</h2>
<form class="form" method="post" action="${ctx}/register">
  <label>Full name <input type="text" name="name" maxlength="100" required></label>
  <label>Email <input type="email" name="email" maxlength="120" required></label>
  <label>Password <input type="password" name="password" minlength="8" required></label>
  <p class="muted">Min 8 characters, with at least one letter and one digit.</p>
  <button type="submit">Register</button>
</form>
<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>