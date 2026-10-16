<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
<h2>Checkout</h2>

<h3>Order Summary</h3>
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

<h3>Delivery Address</h3>
<c:if test="${empty addresses}">
  <p class="muted">You have no saved address yet. Add one below to continue.</p>
</c:if>

<%-- Radio buttons are linked to this form via the form="selectForm" attribute --%>
<form id="selectForm" method="post" action="${ctx}/checkout"></form>

<c:forEach var="a" items="${addresses}">
  <div class="addr">
    <label>
      <input type="radio" form="selectForm" name="addressId" value="${a.addressId}"
             ${a.addressId == selectedAddressId ? 'checked' : ''} required>
      <span>
        <strong><c:out value="${a.fullName}"/></strong> (<c:out value="${a.phone}"/>)<br>
        <c:out value="${a.formatted}"/>
      </span>
    </label>
    <form method="post" action="${ctx}/deleteAddress" onsubmit="return confirm('Delete this address?');">
      <input type="hidden" name="addressId" value="${a.addressId}">
      <button type="submit" class="danger small">Delete</button>
    </form>
  </div>
</c:forEach>

<c:if test="${not empty addresses}">
  <p><a href="${ctx}/cart">&larr; Back to cart</a> &nbsp;
     <button type="submit" form="selectForm">Continue to Payment</button></p>
</c:if>

<h3>Add New Address</h3>
<form class="form" method="post" action="${ctx}/addAddress">
  <label>Full name <input type="text" name="fullName" maxlength="100" required></label>
  <label>Mobile number <input type="text" name="phone" pattern="[6-9][0-9]{9}" maxlength="10"
         inputmode="numeric" placeholder="10-digit number" required></label>
  <label>Address (house no, street, area) <textarea name="line1" rows="2" maxlength="200" required></textarea></label>
  <label>City <input type="text" name="city" maxlength="80" required></label>
  <label>State <input type="text" name="state" maxlength="80" required></label>
  <label>PIN code <input type="text" name="pincode" pattern="[0-9]{6}" maxlength="6"
         inputmode="numeric" required></label>
  <button type="submit">Save Address</button>
</form>
<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>