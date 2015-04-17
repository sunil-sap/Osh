<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/mobile/formElement"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/mobile/product"%>

<ul data-role="listview" data-split-theme="d" data-theme="d">
	<li data-role="list-divider" role="heading">
		<div class="ui-grid-a">
			<h2 class="cartItemsHeadline">
				<spring:theme code="basket.page.title.yourItems" />
				<span class="cart-id-nr">(<spring:theme code="basket.page.number" /> ${cartData.code})</span>
			</h2>
			<div class="cartItemsHelp">
				<a href="#" data-cartcode='${cartData.code}' id='helpLink'>
					<spring:theme code="text.help" />
				</a>
			</div>
			<div id='modalHelpMessage'>
				<spring:theme code="basket.page.cartHelpMessageMobile" text="Help? Call us with cart ID: ${cartData.code}" arguments="${cartData.code}" />
			</div>
		</div>
	</li>
	<c:forEach items="${cartData.entries}" var="entry">
		<c:url value="${entry.product.url}" var="entryProductUrl" />
		<li class="cartListItem">
			<!--- HEADER TITLE START --> 
			<ycommerce:testId code="cart_product_name">
				<h3 class="ui-li-heading cartProductTitle">
					<a href="${entryProductUrl}" data-transition="slide">${entry.product.name}</a>
				</h3>
			</ycommerce:testId> <!--- HEADER TITLE END -->
			<div class="ui-grid-a">
				<div class="ui-block-a cartItemproductImage" style="width: 38%">
					<a href="${entryProductUrl}" data-transition="slide">
						<product:productCartImage product="${entry.product}" format="thumbnail" />
					</a>
				</div>
				<div class="ui-block-b" style="width: 62%">
					<p>${entry.product.description}</p>
					<spring:theme code="basket.page.itemPrice" />
					<span class="itemPrice"><format:price priceData="${entry.basePrice}" displayFreeForZero="true" /></span>
					<div class="clear"></div>
					<div class="qtyForm">
						<c:url value="/" var="updateCartFormAction" />
						<form:form id="updateCartForm${entry.entryNumber}" data-ajax="false" action="${updateCartFormAction}cart/update" method="post" commandName="updateQuantityForm${entry.product.code}">
							<input type="hidden" name="entryNumber" value="${entry.entryNumber}" />
							<ycommerce:testId code="cart_product_quantity">
								<form:select disabled="${not entry.updateable}" id="quantity${entry.entryNumber}" class="quantitySelector" entryNumber="${entry.entryNumber}" path="quantity" data-theme="d" data-ajax="false">
									<formElement:formProductQuantitySelectOption stockLevel="${entry.product.stockLevel}" quantity="${entry.quantity}" />
								</form:select>
							</ycommerce:testId>
						</form:form>
					</div>
					<div class="clear"></div>
					<spring:theme code="basket.page.total" />
					<span class="itemTotalPrice"> <format:price priceData="${entry.totalPrice}" displayFreeForZero="true" /> </span>
				</div>
			</div>
			<div class="ui-grid-a">
				<c:if test="${not empty cartData.potentialProductPromotions}">
					<c:forEach items="${cartData.potentialProductPromotions}" var="promotion">
						<c:set var="displayed" value="false" />
						<c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
							<c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber && not empty promotion.description}">
								<c:set var="displayed" value="true" />
								<ul class="cart-promotions itemPromotionBox">
									<li class="cart-promotions-potential">
										<ycommerce:testId code="cart_promotion_label">
											<span>${promotion.description}</span>
										</ycommerce:testId>
									</li>
								</ul>
							</c:if>
						</c:forEach>
					</c:forEach>
				</c:if>
				<c:set var="hasAppliedPromotions" value="false" />
				<c:if test="${not empty cartData.appliedProductPromotions}">
					<c:forEach items="${cartData.appliedProductPromotions}" var="promotion">
						<c:if test="${not hasAppliedPromotions}">
							<c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
								<c:if test="${not hasAppliedPromotions && consumedEntry.orderEntryNumber == entry.entryNumber}">
									<c:set var="hasAppliedPromotions" value="true" />
								</c:if>
							</c:forEach>
						</c:if>
					</c:forEach>
				</c:if>
				<c:if test="${hasAppliedPromotions}">
					<ul class="cart-promotions  itemPromotionBox">
						<c:forEach items="${cartData.appliedProductPromotions}" var="promotion">
							<c:set var="displayed" value="false" />
							<c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
								<c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
									<c:set var="displayed" value="true" />
									<li class="cart-promotions-applied">
										<ycommerce:testId code="cart_appliedPromotion_label">
											<span>${promotion.description}</span>
										</ycommerce:testId>
									</li>
								</c:if>
							</c:forEach>
						</c:forEach>
					</ul>
				</c:if>
			</div>
		</li>
	</c:forEach>
</ul>
