<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>ShopEasy</title>
  <link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
<nav class="nav">
  <a class="brand" href="${ctx}/products">ShopEasy</a>
  <div class="links">
    <c:choose>
      <c:when test="${sessionScope.user.role == 'ADMIN'}">
        <a href="${ctx}/admin/products">Products</a>
        <a href="${ctx}/admin/addProduct">Add Product</a>
        <a href="${ctx}/admin/users">Users</a>
        <a href="${ctx}/admin/orders">Orders</a>
      </c:when>
      <c:otherwise>
        <a href="${ctx}/products">Shop</a>
        <c:if test="${not empty sessionScope.user}">
          <a href="${ctx}/wishlist">Wishlist</a>
          <a href="${ctx}/myOrders">My Orders</a>
        </c:if>
        <a href="${ctx}/cart">Cart<c:if test="${cartCount > 0}"><span class="cart-badge">${cartCount}</span></c:if></a>
      </c:otherwise>
    </c:choose>
    <c:choose>
      <c:when test="${not empty sessionScope.user}">
        <span>Hi, <c:out value="${sessionScope.user.name}"/></span>
        <a href="${ctx}/logout">Logout</a>
      </c:when>
      <c:otherwise>
        <a href="${ctx}/login">Login</a>
        <a href="${ctx}/register">Register</a>
      </c:otherwise>
    </c:choose>
  </div>
</nav>
<main class="container">
<c:if test="${not empty sessionScope.flash}">
  <div class="alert"><c:out value="${sessionScope.flash}"/></div>
  <c:remove var="flash" scope="session"/>
</c:if>
<c:if test="${not empty error}">
  <div class="alert error"><c:out value="${error}"/></div>
</c:if>