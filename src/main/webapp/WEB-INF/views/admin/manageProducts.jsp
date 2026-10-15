<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
<h2>Manage Products</h2>
<p><a class="btn" href="${ctx}/admin/addProduct">+ Add Product</a></p>
<div class="table-wrap">
<table>
  <tr><th>ID</th><th>Image</th><th>Name</th><th>Category</th><th>Price</th><th>Stock</th><th>Actions</th></tr>
  <c:forEach var="p" items="${products}">
    <tr>
      <td>${p.productId}</td>
      <td><c:if test="${not empty p.imagePath}">
            <img class="thumb" src="<c:out value='${p.imagePath}'/>" alt="">
          </c:if></td>
      <td><c:out value="${p.name}"/></td>
      <td><c:out value="${p.category}"/></td>
      <td>&#8377;<fmt:formatNumber value="${p.price}" minFractionDigits="2"/></td>
      <td>${p.quantity}</td>
      <td class="actions">
        <a href="${ctx}/admin/updateProduct?id=${p.productId}">Edit</a>
        <form method="post" action="${ctx}/admin/deleteProduct"
              onsubmit="return confirm('Delete this product?');">
          <input type="hidden" name="id" value="${p.productId}">
          <button type="submit" class="danger">Delete</button>
        </form>
      </td>
    </tr>
  </c:forEach>
</table>
</div>
<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>