<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>

<div class="ui-grid-a" data-theme="b" data-role="content">
	<div class="ui-grid-a" data-theme="b">
		<div class="checkoutOverviewItemsHeadline"><spring:theme code="order.order.totals"/></div>
	</div>
	<div class="ui-grid-a" data-theme="b">
		<div class="ui-grid-a" data-theme="b">
			<div class="ui-block-a">
				<spring:theme code="basket.page.totals.subtotal"/>
			</div>
			<div class="ui-block-b">
				<format:price priceData="${cartData.subTotal}"/>
			</div>
		</div>
		<div class="ui-grid-a" data-theme="b">
			<div class="ui-block-a">
				<spring:theme code="basket.page.totals.savings"/>
			</div>
			<div class="ui-block-b">
				<format:price priceData="${cartData.totalDiscounts}"/>
			</div>
		</div>
		<c:if test="${not empty cartData.deliveryCost}">
			<div class="ui-grid-a" data-theme="b">
				<div class="ui-block-a">
					<spring:theme code="basket.page.totals.delivery"/>
				</div>
				<div class="ui-block-b">
					<c:choose>
						<c:when test="${cartData.deliveryCost.value > 0}">
							<format:price priceData="${cartData.deliveryCost}"/>
						</c:when>
						<c:otherwise>
							<spring:theme code="basket.page.free"/>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</c:if>
		<div class="ui-grid-a" data-theme="b">
			<div class="ui-block-a">
				<spring:theme code="basket.page.totals.total"/>
			</div>
			<div class="ui-block-b">
				<ycommerce:testId code="checkout_totalPrice_label"><format:price priceData="${cartData.totalPrice}"/></ycommerce:testId>
			</div>
		</div>
		<br/>
		<c:if test="${not cartData.net}">
			<div class="ui-grid-a" data-theme="b">
				<ycommerce:testId code="checkout_tax_label">
					<spring:theme code="basket.page.totals.grossTax" arguments="${cartData.totalTax.formattedValue}" argumentSeparator="!!!!"/>
				</ycommerce:testId>
			</div>
		</c:if>
	</div>
</div>
