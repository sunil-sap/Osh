<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>

<spring:theme code="text.addToCart" var="addToCartText" />
<c:url value="${product.url}" var="productUrl" />
<c:set value="${not empty product.potentialPromotions}" var="hasPromotion" />
<ycommerce:testId code="product_wholeProduct">
	<div class="productthumb"> 
		<a href="${productUrl}"> 
		<product:productPrimaryImage product="${product}" format="category" />
		</a>

		<c:if
			test="${not empty product.potentialPromotions and not empty product.potentialPromotions[0].productBanner}">
			<img class="promo" src="${product.potentialPromotions[0].productBanner.url}" alt="${product.potentialPromotions[0].description}" title="${product.potentialPromotions[0].description}" />
		</c:if>
	</div>

	<div class="product_quickview">
		<a class='ajax'
			href="${request.contextPath}/p/${product.code}/quickView"> <img title="Quick View"
			src="${request.contextPath}/_ui/desktop/osh/images/icon_quickview.jpg">
		</a>
	</div>
	<div
		style="outline: 0px none; z-index: 1002; height: auto; width: 820px; top: 992.183px; left: 261px; display: none;">
		<div id="quickview"
			style="display: block; width: auto; min-height: 97px; height: auto;"
			scrolltop="0" scrollleft="0"></div>
	</div>
	<div class="producttitle searchPage_ProdName">
		<a href="${productUrl}"><span>${product.name}</span></a>
	</div>

	<div>
		<c:choose>
			<c:when test="${product.mapPriceType}">
				<div class="pricelabel" style="color: #CC0000;">*
					<span><spring:theme code="add.to.cart.price" /></span><br/>
					&nbsp;
					<span class="strike" style=" color: #D4D4D4;">
							<format:price priceData="${product.regPriceData}" />
						</span>
				</div>
			</c:when>

			<c:otherwise>
				<c:if test="${not empty product.salePriceData && not empty product.regPriceData  }">
					<div class="pricelabel" style="color: #CC0000;">
						<spring:theme code="osh.productListing.page.sale" />
						&nbsp;
						<format:price priceData="${product.salePriceData}" />
					</div>
					<div class="pricelabel" style=" color: #D4D4D4;">
						<spring:theme code="osh.productListing.page.Register" />
						&nbsp;
					    <span class="strike">
							<format:price priceData="${product.regPriceData}" />
						</span>
					</div>

				</c:if>
				<c:if test="${not empty product.regPriceData && empty product.salePriceData}">
					<div class="pricelabel">
						<format:price priceData="${product.regPriceData}" />
					</div>&nbsp; 
				</c:if>
			</c:otherwise>
		</c:choose>
		<div class="shipping_info">
		<c:if test="${product.freeShipping}">
			<span><spring:message
					code="osh.product.page.product.freeshipping" /></span>
		</c:if>
		</div>
		
		 <c:if test="${product.availabilityInd eq 'WEB'}">
			<span class="flag"><spring:message
					code="osh.product.page.message.onlineonly" /></span>
		</c:if> 
				<c:if test="${product.availabilityInd eq 'STORE'}">
			<span class="flag"><spring:message
					code="osh.product.page.message.instore" /></span>
		</c:if>
		<c:if test="${product.availabilityInd eq 'ALL'}">
			<span class="flag"><spring:message
					code="osh.product.page.message.avail" /></span>
		</c:if>
		
	</div>

</ycommerce:testId>

