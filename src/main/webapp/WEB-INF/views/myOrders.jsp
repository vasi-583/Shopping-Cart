<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
<h2>My Orders</h2>
<c:choose>
  <c:when test="${empty orders}">
    <p>You have not placed any orders yet. <a href="${ctx}/products">Start shopping</a></p>
  </c:when>
  <c:otherwise>
    <c:forEach var="o" items="${orders}">
      <div class="order-card">
        <div class="order-head">
          <div>
            <strong>Order #${o.orderId}</strong><br>
            <span class="muted"><fmt:formatDate value="${o.orderDate}" pattern="dd MMM yyyy, HH:mm"/></span>
          </div>
          <span class="badge ${o.status}">${o.status}</span>
        </div>

        <c:if test="${o.status != 'CANCELLED'}">
          <div class="steps">
            <span class="step done">Placed</span>
            <span class="step ${o.status == 'SHIPPED' or o.status == 'DELIVERED' ? 'done' : ''}">Shipped</span>
            <span class="step ${o.status == 'DELIVERED' ? 'done' : ''}">Delivered</span>
          </div>
        </c:if>

        <ul class="order-items">
          <c:forEach var="it" items="${o.items}">
            <li><c:out value="${it.productName}"/> &times; ${it.quantity}
                &mdash; &#8377;<fmt:formatNumber value="${it.price}" minFractionDigits="2"/></li>
          </c:forEach>
        </ul>

        <c:if test="${not empty o.shipAddress}">
          <p class="muted">Ship to: <c:out value="${o.shipName}"/>, <c:out value="${o.shipAddress}"/></p>
        </c:if>

        <p><strong>Total: &#8377;<fmt:formatNumber value="${o.totalAmount}" minFractionDigits="2"/></strong></p>
        <a class="btn secondary" href="${ctx}/orderConfirmation?id=${o.orderId}">View Details</a>
      </div>
    </c:forEach>
  </c:otherwise>
</c:choose>
<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>