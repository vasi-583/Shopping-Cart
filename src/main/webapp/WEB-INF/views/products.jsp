<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
<h2>Products</h2>
<div class="grid">
  <c:forEach var="p" items="${products}">
    <c:set var="liked" value="${not empty wishlistIds and wishlistIds.contains(p.productId)}"/>
    <div class="card">
      <c:if test="${not empty p.imagePath}">
        <img src="<c:out value='${p.imagePath}'/>" alt="<c:out value='${p.name}'/>">
      </c:if>
      <h3><c:out value="${p.name}"/></h3>
      <p class="muted"><c:out value="${p.category}"/></p>
      <p><c:out value="${p.description}"/></p>
      <p class="price">&#8377;<fmt:formatNumber value="${p.price}" minFractionDigits="2"/></p>
      <div class="card-actions">
        <c:choose>
          <c:when test="${p.quantity > 0}">
            <form method="post" action="${ctx}/addToCart">
              <input type="hidden" name="productId" value="${p.productId}">
              <button type="submit">Add to Cart</button>
            </form>
          </c:when>
          <c:otherwise><span class="out">Out of stock</span></c:otherwise>
        </c:choose>
        <form method="post" action="${ctx}/toggleWishlist">
          <input type="hidden" name="productId" value="${p.productId}">
          <input type="hidden" name="back" value="products">
          <button type="submit" class="heart" title="${liked ? 'Remove from wishlist' : 'Add to wishlist'}">${liked ? '&#9829;' : '&#9825;'}</button>
        </form>
      </div>
    </div>
  </c:forEach>
</div>
<c:if test="${empty products}"><p>No products available.</p></c:if>
<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>