<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
<h2>My Wishlist</h2>
<c:if test="${empty products}">
  <p>Your wishlist is empty. <a href="${ctx}/products">Browse products</a></p>
</c:if>
<div class="grid">
  <c:forEach var="p" items="${products}">
    <div class="card">
      <c:if test="${not empty p.imagePath}">
        <img src="<c:out value='${p.imagePath}'/>" alt="<c:out value='${p.name}'/>">
      </c:if>
      <h3><c:out value="${p.name}"/></h3>
      <p class="muted"><c:out value="${p.category}"/></p>
      <p class="price">&#8377;<fmt:formatNumber value="${p.price}" minFractionDigits="2"/></p>
      <div class="card-actions">
        <c:choose>
          <c:when test="${p.quantity > 0}">
            <form method="post" action="${ctx}/addToCart">
              <input type="hidden" name="productId" value="${p.productId}">
              <input type="hidden" name="back" value="wishlist">
              <button type="submit">Add to Cart</button>
            </form>
          </c:when>
          <c:otherwise><span class="out">Out of stock</span></c:otherwise>
        </c:choose>
        <form method="post" action="${ctx}/toggleWishlist">
          <input type="hidden" name="productId" value="${p.productId}">
          <input type="hidden" name="back" value="wishlist">
          <button type="submit" class="danger">Remove</button>
        </form>
      </div>
    </div>
  </c:forEach>
</div>
<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>