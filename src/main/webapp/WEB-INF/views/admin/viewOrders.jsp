<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
<h2>All Orders</h2>
<div class="table-wrap">
<table>
  <tr><th>Order</th><th>Customer</th><th>Date</th><th>Items</th><th>Ship To</th>
      <th>Total</th><th>Payment</th><th>Status</th></tr>
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
      <td>
        <c:choose>
          <c:when test="${not empty o.shipAddress}">
            <c:out value="${o.shipName}"/> (<c:out value="${o.shipPhone}"/>)<br>
            <span class="muted"><c:out value="${o.shipAddress}"/></span>
          </c:when>
          <c:otherwise><span class="muted">-</span></c:otherwise>
        </c:choose>
      </td>
      <td>&#8377;<fmt:formatNumber value="${o.totalAmount}" minFractionDigits="2"/></td>
      <td><c:out value="${o.paymentStatus}"/><br>
          <span class="muted"><c:out value="${o.cardholderName}"/> ****<c:out value="${o.cardLast4}"/></span></td>
      <td>
        <span class="badge ${o.status}">${o.status}</span>
        <c:if test="${not empty o.nextStatuses}">
          <form method="post" action="${ctx}/admin/updateOrderStatus" class="status-form"
                onsubmit="return this.status.value !== 'CANCELLED' || confirm('Cancel this order? Stock will be restored and the payment marked as refunded.');">
            <input type="hidden" name="orderId" value="${o.orderId}">
            <select name="status">
              <c:forEach var="s" items="${o.nextStatuses}">
                <option value="${s}">${s}</option>
              </c:forEach>
            </select>
            <button type="submit" class="small">Update</button>
          </form>
        </c:if>
      </td>
    </tr>
  </c:forEach>
</table>
</div>
<c:if test="${empty orders}"><p>No orders yet.</p></c:if>
<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>