<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/includes/header.jsp"/>
<c:set var="p" value="${product}"/>
<h2>${empty p ? 'Add' : 'Update'} Product</h2>
<form class="form" method="post"
      action="${ctx}/admin/${empty p ? 'addProduct' : 'updateProduct'}">
  <c:if test="${not empty p}"><input type="hidden" name="productId" value="${p.productId}"></c:if>
  <label>Name <input type="text" name="name" maxlength="150" value="<c:out value='${p.name}'/>" required></label>
  <label>Description <textarea name="description" rows="3"><c:out value="${p.description}"/></textarea></label>
  <label>Price (&#8377;) <input type="number" step="0.01" min="0" name="price" value="${p.price}" required></label>
  <label>Category <input type="text" name="category" maxlength="60" value="<c:out value='${p.category}'/>"></label>
  <label>Quantity <input type="number" min="0" name="quantity" value="${empty p ? 0 : p.quantity}" required></label>
  <label>Image URL
    <input type="url" name="imageUrl" maxlength="500" placeholder="https://example.com/image.jpg"
           value="<c:out value='${p.imagePath}'/>"></label>
  <c:if test="${not empty p.imagePath}">
    <p><img src="<c:out value='${p.imagePath}'/>" alt="Preview" style="max-width:160px;border-radius:6px"></p>
  </c:if>
  <button type="submit">${empty p ? 'Add' : 'Update'} Product</button>
</form>
<jsp:include page="/WEB-INF/views/includes/footer.jsp"/>