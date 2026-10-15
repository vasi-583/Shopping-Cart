<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
<h2>Checkout - Order Summary</h2>
<div class="table-wrap">
<table>
  <tr><th>Product</th><th>Qty</th><th>Subtotal</th></tr>
  <c:forEach var="i" items="${items}">
    <tr><td><c:out value="${i.productName}"/></td><td>${i.quantity}</td>
        <td>&#8377;<fmt:formatNumber value="${i.subtotal}" minFractionDigits="2"/></td></tr>
  </c:forEach>
  <tr class="total"><td colspan="2">Total payable</td>
      <td>&#8377;<fmt:formatNumber value="${total}" minFractionDigits="2"/></td></tr>
</table>
</div>
<p><a href="${ctx}/cart">&larr; Back to cart</a> &nbsp; <a class="btn" href="${ctx}/payment">Continue to Payment</a></p>
<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>