<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
<h2>Your Cart</h2>
<c:choose>
  <c:when test="${empty items}">
    <p>Your cart is empty. <a href="${ctx}/products">Continue shopping</a></p>
  </c:when>
  <c:otherwise>
    <div class="table-wrap">
    <table>
      <tr><th>Product</th><th>Price</th><th>Qty</th><th>Subtotal</th><th></th></tr>
      <c:forEach var="i" items="${items}">
        <tr>
          <td>
            <c:if test="${not empty i.imagePath}">
              <img class="thumb" src="<c:out value='${i.imagePath}'/>" alt="">
            </c:if>
            <c:out value="${i.productName}"/>
          </td>
          <td>&#8377;<fmt:formatNumber value="${i.price}" minFractionDigits="2"/></td>
          <td>
            <div class="qty">
              <form method="post" action="${ctx}/updateCart">
                <input type="hidden" name="cartId" value="${i.cartId}">
                <input type="hidden" name="action" value="dec">
                <button type="submit" class="qbtn" title="Decrease">&minus;</button>
              </form>
              <strong>${i.quantity}</strong>
              <form method="post" action="${ctx}/updateCart">
                <input type="hidden" name="cartId" value="${i.cartId}">
                <input type="hidden" name="action" value="inc">
                <button type="submit" class="qbtn" title="Increase"
                        ${i.quantity >= i.stock ? 'disabled' : ''}>+</button>
              </form>
            </div>
            <c:if test="${i.quantity >= i.stock}"><span class="muted">Max stock reached</span></c:if>
          </td>
          <td>&#8377;<fmt:formatNumber value="${i.subtotal}" minFractionDigits="2"/></td>
          <td>
            <form method="post" action="${ctx}/removeFromCart">
              <input type="hidden" name="cartId" value="${i.cartId}">
              <button type="submit" class="danger">Remove</button>
            </form>
          </td>
        </tr>
      </c:forEach>
      <tr class="total"><td colspan="3">Total</td>
          <td colspan="2">&#8377;<fmt:formatNumber value="${total}" minFractionDigits="2"/></td></tr>
    </table>
    </div>
    <p><a class="btn" href="${ctx}/checkout">Proceed to Checkout</a></p>
  </c:otherwise>
</c:choose>
<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>