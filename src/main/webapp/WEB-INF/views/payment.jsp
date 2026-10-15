<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
<h2>Payment</h2>
<p>Amount to pay: <strong>&#8377;<fmt:formatNumber value="${total}" minFractionDigits="2"/></strong></p>
<form class="form" method="post" action="${ctx}/payment" autocomplete="off">
  <label>Cardholder name <input type="text" name="cardholder" maxlength="100" required></label>
  <label>Card number <input type="text" name="cardNumber" inputmode="numeric" maxlength="23"
         pattern="[0-9 -]{13,23}" placeholder="4111 1111 1111 1111" required></label>
  <label>Expiry (MM/YY) <input type="text" name="expiry" pattern="(0[1-9]|1[0-2])/[0-9]{2}"
         placeholder="12/30" maxlength="5" required></label>
  <label>CVV <input type="password" name="cvv" inputmode="numeric" pattern="[0-9]{3,4}" maxlength="4" required></label>
  <button type="submit">Pay Now</button>
  <p class="muted">Demo only: card details are validated and never stored (only the last 4 digits are kept).</p>
</form>
<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>