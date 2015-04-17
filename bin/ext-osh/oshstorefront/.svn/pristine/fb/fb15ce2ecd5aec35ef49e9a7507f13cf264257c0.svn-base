<%@ page trimDirectiveWhitespaces="true" contentType="application/json"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/mobile/product"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>
<json:object>
	<json:object name="cartData">
		<json:property name="total" value="${cartData.totalPrice.value}"/>
		<json:array name="products" items="${cartData.entries}" var="cartEntry">
			<json:object>
				<json:property name="sku" value="${cartEntry.product.code}"/>
				<json:property name="name" value="${cartEntry.product.name}"/>
				<json:property name="qty" value="${cartEntry.quantity}"/>
				<json:property name="price" value="${cartEntry.basePrice.value}"/>
				<json:array name="categories" items="${cartEntry.product.categories}" var="category">
					<json:property value="${category.name}"/>
				</json:array>
			</json:object>
		</json:array>
	</json:object>
	<json:property name="cartPopupHtml" escapeXml="false">
		<spring:theme code="text.addToCart" var="addToCartText"/>
		<c:url value="/cart" var="cartUrl"/>
		<c:url value="/cart/checkout" var="checkoutUrl"/>
		<div class="dialog_container">
			<c:if test="${not empty errorMsg}">
				<div class="cart_popup_error_msg">
					<spring:theme code="${errorMsg}"/>
				</div>
			</c:if>
			<div>
				<h3>${product.name}</h3>
				<span class="prod_productimage">
					<product:productPrimaryImage product="${product}" format="cartIcon" zoomable="false"/>
				</span>
				<div class="ui-grid-a addToCartSummary" data-theme="b">
					<div class="ui-block-a prod_baseprice">
						<spring:theme code="basket.page.price"/>
					</div>
					<div class="ui-block-b prod_baseprice">
						<format:price priceData="${entry.basePrice}"/>
					</div>
					<c:forEach items="${product.baseOptions}" var="baseOptions">
						<c:forEach items="${baseOptions.selected.variantOptionQualifiers}" var="baseOptionQualifier">
							<c:if test="${baseOptionQualifier.qualifier eq 'style' and not empty baseOptionQualifier.image.url}">
								<div class="ui-block-a prod_color">
									<spring:theme code="product.variants.colour"/>
								</div>
								<div class="ui-block-b prod_color">
									<img src="${baseOptionQualifier.image.url}"/>
								</div>
							</c:if>
							<c:if test="${baseOptionQualifier.qualifier eq 'size'}">
								<div class="ui-block-a prod_size">
									<spring:theme code="product.variants.size"/>
								</div>
								<div class="ui-block-b prod_size">
									${baseOptionQualifier.value}
								</div>
							</c:if>
						</c:forEach>
					</c:forEach>
					<div class="ui-block-a prod_quantity">
						<spring:message code="popup.cart.quantity"/>
					</div>
					<div class="ui-block-b prod_quantity">
						${quantity}
					</div>
				</div>
				<div class="clear"></div>
			</div>
			<a href="${cartUrl}" data-role="button" data-theme="b">
				<spring:theme code="basket.view.basket"/>
			</a>
		</div>
	</json:property>
</json:object>
