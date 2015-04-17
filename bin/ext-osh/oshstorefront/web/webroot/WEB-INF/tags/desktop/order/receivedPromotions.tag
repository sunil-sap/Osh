<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

<%@ attribute name="containerCSS" required="false" type="java.lang.String" %>

<c:choose>
<c:when test="${(not empty order.appliedProductPromotions || not empty order.appliedOrderPromotions) && not empty order.voucherCode}">
	<div class="order_conf_promo" style="margin-left: 20px;">
		<div class="order_conf_title_holder">
			<div class="title">
				<div class="title-top">
					<span></span>
				</div>
			</div>
			<h2><spring:theme code="text.account.order.receivedPromotions" text="Received Promotions"/></h2>
		</div>
		<div class="item_container">
			<ul>
				<c:forEach items="${order.appliedProductPromotions}" var="promotion">
					<li class="cart-promotions-applied">${promotion.description}</li>
				</c:forEach>
				<li class="cart-promotions-applied">Voucher Applied - You Save ${order.orderDiscounts.formattedValue}</li>
			</ul>
		</div>
	</div>
</c:when>

<c:otherwise>
<c:if test="${not empty order.appliedOrderPromotions}">
	<div class="order_conf_promo" style="margin-left: 20px;">
		<div class="order_conf_title_holder">
			<div class="title">
				<div class="title-top">
					<span></span>
				</div>
			</div>
			<h2><spring:theme code="text.account.order.receivedPromotions" text="Received Promotions"/></h2>
		</div>
		<div class="item_container">
			<ul>
				<c:forEach items="${order.appliedOrderPromotions}" var="promotion">
					<li class="cart-promotions-applied">${promotion.description}</li>
				</c:forEach>
			</ul>
		</div>
	</div>
</c:if>

<c:if test="${not empty order.appliedProductPromotions}">
	<div class="order_conf_promo" style="margin-left: 20px;">
		<div class="order_conf_title_holder">
			<div class="title">
				<div class="title-top">
					<span></span>
				</div>
			</div>
			<h2><spring:theme code="text.account.order.receivedPromotions" text="Received Promotions"/></h2>
		</div>
		<div class="item_container">
			<ul>
				<c:forEach items="${order.appliedProductPromotions}" var="promotion">
					<li class="cart-promotions-applied">${promotion.description}</li>
				</c:forEach>
			</ul>
		</div>
	</div>
</c:if>


<c:if test="${not empty order.voucherCode}">
	<div class="order_conf_promo" style="margin-left: 20px;">
		<div class="order_conf_title_holder">
			<div class="title">
				<div class="title-top">
					<span></span>
				</div>
			</div>
			<h2><spring:theme code="text.account.order.receivedPromotions" text="Received Promotions"/></h2>
		</div>
		<div class="item_container">
			<ul>
					<li class="cart-promotions-applied">You Save ${order.orderDiscounts.formattedValue}</li>
			</ul>
		</div>
	</div>
</c:if>
</c:otherwise>
</c:choose>

