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

<div id='cartTotals'>
	<div class="ui-grid-a" data-theme="b" data-role="content">
		<table cellpadding="0" cellspacing="0" class="order_totals">
			<!--  ALWAYS SHOW SUBTOTAL -->
			<tr>
				<td><span><spring:theme code="basket.page.totals.subtotal" /></span></td>
				<td class="order_totals_Sum"><span><format:price priceData="${cartData.subTotal}" /></span></td>
			</tr>
			<!--  SHOW SAVINGS IF AVAILABLE -->
			<c:if test="${cartData.orderDiscounts.value > 0}">
				<tr>
					<td class="savings"><span><spring:theme code="basket.page.totals.savings" /></span></td>
					<td class="savings order_totals_Sum"><span><format:price priceData="${cartData.orderDiscounts}" /></span></td>
				</tr>
			</c:if>
			<!--  SHOW DELIVERY COST IF AVAILABLE  -->
			<c:if test="${not empty cartData.deliveryCost}">
				<tr>
					<td><span><spring:theme code="basket.page.totals.delivery" /></span></td>
					<td class="order_totals_Sum"><span><format:price priceData="${cartData.deliveryCost}" displayFreeForZero="TRUE" /></span></td>
				</tr>
			</c:if>
			<!--  SHOW TAXES IF AVAILABLE -->
			<c:if test="${cartData.net}">
				<tr>
					<td><span><spring:theme code="basket.page.totals.netTax" /></span></td>
					<td class="order_totals_Sum"><span><format:price priceData="${cartData.totalTax}" /></span></td>
				</tr>
			</c:if>
			<!--  SHOW TOTAL -->
			<tr>
				<td class="completePrice"><span><spring:theme code="basket.page.totals.total" /></span></td>
				<td class="completePrice order_totals_Sum">
					<span>
						<ycommerce:testId code="cart_totalPrice_label">
							<format:price priceData="${cartData.totalPrice}" />
						</ycommerce:testId>
					</span>
				</td>
			</tr>
		</table>
	</div>
	<div class="ui-grid-a" data-theme="b" data-role="content">
		<c:if test="${not cartData.net}">
			<ycommerce:testId code="cart_taxes_label">
				<p><spring:theme code="basket.page.totals.grossTax" arguments="${cartData.totalTax.formattedValue}" argumentSeparator="!!!!" /></p>
			</ycommerce:testId>
		</c:if>
	</div>
</div>
