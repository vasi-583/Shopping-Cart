<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
<c:if test="${param.placed == '1'}">
  <div class="alert success">Payment successful! Thank you for your order.</div>
</c:if>
<h2>Order #${order.orderId} <span class="badge ${order.status}">${order.status}</span></h2>
<p>Date: <fmt:formatDate value="${order.orderDate}" pattern="dd MMM yyyy, HH:mm"/> |
   Payment: <c:out value="${order.paymentStatus}"/> (card ending <c:out value="${order.cardLast4}"/>)</p>

<c:if test="${not empty order.shipAddress}">
  <h3>Delivery Address</h3>
  <p><strong><c:out value="${order.shipName}"/></strong> (<c:out value="${order.shipPhone}"/>)<br>
     <c:out value="${order.shipAddress}"/></p>
</c:if>

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
<p>
  <a class="btn secondary" href="${ctx}/myOrders">My Orders</a>
  <a class="btn secondary" href="${ctx}/products">Continue Shopping</a>
</p>
<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>