<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="mproduct" tagdir="/WEB-INF/tags/mobile/product"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>

<spring:theme code="text.addToCart" var="addToCartText"/>
<c:url value="${product.url}" var="productUrl"/>
<ycommerce:testId code="product_wholeProduct">
	<a href="${productUrl}">
		<div class="productTitle">${product.name}</div>
		<mproduct:productPrimaryImage product="${product}" format="thumbnail" zoomable='false'/>
		<span id="productPrice" class="mlist-price"><format:price priceData="${product.price}"/></span>
		<c:choose>
			<c:when test="${product.stockLevel > 0}">
				<span class='listProductInStock mlist-stock'>${product.stockLevel}&nbsp;<spring:theme code="product.variants.in.stock"/></span>
			</c:when>
			<c:otherwise>
				<span class='listProductOutOfStock mlist-stock'><spring:theme code="product.variants.out.of.stock"/></span>
			</c:otherwise>
		</c:choose>
		<span class="mlist-rating"><mproduct:productAverageRatingListerItem product="${product}"/></span>
	</a>
</ycommerce:testId>
