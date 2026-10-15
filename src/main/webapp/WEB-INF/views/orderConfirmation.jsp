<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
<div class="alert success">Payment successful! Thank you for your order.</div>
<h2>Order #${order.orderId}</h2>
<p>Date: <fmt:formatDate value="${order.orderDate}" pattern="dd MMM yyyy, HH:mm"/> |
   Status: <strong><c:out value="${order.status}"/></strong> |
   Paid with card ending <c:out value="${order.cardLast4}"/></p>
<div class="table-wrap">
<table>
  <tr><th>Product</th><th>Price</th><th>Qty</th></tr>
  <c:forEach var="it" items="${order.items}">
    <tr><td><c:out value="${it.productName}"/></td>
        <td>&#8377;<fmt:formatNumber value="${it.price}" minFractionDigits="2"/></td>
        <td>${it.quantity}</td></tr>
  </c:forEach>
  <tr class="total"><td colspan="2">Total paid</td>
      <td>&#8377;<fmt:formatNumber value="${order.totalAmount}" minFractionDigits="2"/></td></tr>
</table>
</div>
<p><a class="btn" href="${ctx}/products">Continue Shopping</a></p>
<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>