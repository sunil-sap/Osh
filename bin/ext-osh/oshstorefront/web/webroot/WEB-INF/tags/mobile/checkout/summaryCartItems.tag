<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData"%>
<%@ attribute name="showPotentialPromotions" required="false" type="java.lang.Boolean"%>
<%@ attribute name="showAllItems" type="java.lang.Boolean"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/mobile/product"%>

<div class="ui-grid-a" data-theme="b" data-role="content">
	<div class="ui-grid-a" data-theme="b">
		<div class="checkoutOverviewItemsHeadline">
			<spring:theme code="basket.page.title.yourItems" />
		</div>
	</div>
	<div class="ui-grid-a" data-theme="b">
		<div class="ui-grid-a">
			<div class="ui-block-a" style="width: 70%">
				<span class="cart_id">
					<spring:theme code="basket.page.cartId" />
					<span class="cart-id-nr">${cartData.code}</span>
				</span>
			</div>
			<div class="ui-block-b" style="width: 30%">
				<c:if test="${not showAllItems and fn:length(cartData.entries) gt 2}">
					<a href="javascript:void(null);" class="showAllItems" data-role="button" data-theme="d">
						<spring:theme code="mobile.checkout.cart.viewFullCart" />
					</a>
				</c:if>
			</div>
		</div>
		<ul class="mFormList itemsList productItemListDetailsHolder " numberOfItems="1" liFilterClass='cartLi'>
			<c:forEach items="${cartData.entries}" var="entry" varStatus="status">
				<c:url value="${entry.product.url}" var="entryProductUrl" />
				<li ${not showAllItems and status.count>2?"style='display:none'":"" } class='cartLi'>
					<ycommerce:testId code="cart_product_name">
						<h4 class="ui-li-heading cartProductTitle">
							<a href="${entryProductUrl}" data-transition="slide">${entry.product.name}</a>
						</h4>
					</ycommerce:testId>
					<div class="ui-grid-a cartItemproductImage">
						<div class="ui-block-a">
							<a href="${entryProductUrl}" data-transition="slide">
								<product:productCartImage product="${entry.product}" format="thumbnail" />
							</a>
						</div>
						<div class="ui-block-b">
							<div class="ui-grid-a" data-role="content">
								<div class="ui-block-a">
									<p>${entry.product.description}</p>
								</div>
							</div>
							<c:forEach items="${entry.product.baseOptions}" var="option">
								<c:if test="${not empty option.selected and option.selected.url eq entry.product.url}">
									<c:forEach items="${option.selected.variantOptionQualifiers}" var="selectedOption">
										<div class="ui-grid-a">
											${selectedOption.name}:&nbsp;<span class="quantityItemHolder">${selectedOption.value}</span>
										</div>
									</c:forEach>
								</c:if>
							</c:forEach>
							<div class="ui-grid-a">
								<spring:theme code="basket.page.quantity" />
								:&nbsp;<span class="quantityItemHolder">${entry.quantity}</span>
							</div>
							<div class="ui-grid-a">
								<spring:theme code="basket.page.itemPrice" />
								:&nbsp;<span class="priceItemHolder"><format:price priceData="${entry.basePrice}" displayFreeForZero="true" /></span>
							</div>
							<div class="ui-grid-a">
								<spring:theme code="basket.page.total" />
								:&nbsp;<span class="priceItemHolder"><format:price priceData="${entry.totalPrice}" displayFreeForZero="true" /></span>
							</div>
						</div>
					</div>
					<div class="ui-grid-a">
						<c:if test="${not empty cartData.potentialProductPromotions && showPotentialPromotions}">
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
						<c:if test="${not empty cartData.appliedProductPromotions}">
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
			<li class="viewLess" style="display: none">
				<div class="ui-grid-a">
					<div class="ui-block-a" style="width: 70%"></div>
					<div class="ui-block-b" style="width: 30%">
						<a data-role="button" data-theme="d" href="javascript:void(null);">
							<spring:theme code="mobile.checkout.cart.viewLess" />
						</a>
					</div>
				</div>
			</li>
		</ul>
	</div>
</div>
