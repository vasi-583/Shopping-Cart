<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
<h2>All Orders</h2>
<div class="table-wrap">
<table>
  <tr><th>Order</th><th>Customer</th><th>Date</th><th>Items</th><th>Total</th><th>Payment</th></tr>
  <c:forEach var="o" items="${orders}">
    <tr>
      <td>#${o.orderId}</td>
      <td><c:out value="${o.userName}"/></td>
      <td><fmt:formatDate value="${o.orderDate}" pattern="dd MMM yyyy HH:mm"/></td>
      <td>
        <c:forEach var="it" items="${o.items}">
          <c:out value="${it.productName}"/> &times; ${it.quantity}<br>
        </c:forEach>
      </td>
      <td>&#8377;<fmt:formatNumber value="${o.totalAmount}" minFractionDigits="2"/></td>
      <td><c:out value="${o.paymentStatus}"/><br>
          <span class="muted"><c:out value="${o.cardholderName}"/> ****<c:out value="${o.cardLast4}"/></span></td>
    </tr>
  </c:forEach>
</table>
</div>
<c:if test="${empty orders}"><p>No orders yet.</p></c:if>
<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>