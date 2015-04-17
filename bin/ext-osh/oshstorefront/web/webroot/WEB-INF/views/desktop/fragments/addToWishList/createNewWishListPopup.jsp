<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="wishlist" tagdir="/WEB-INF/tags/desktop/wishlist" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
     <c:url value="${request.contextPath}/addWishList/createAndUpdateWishList.json" var="action" />
    <wishlist:createUpdateWishlist/>