<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="order" required="true"
	type="de.hybris.platform.commercefacades.order.data.OrderData"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>

<%@ attribute name="containerCSS" required="false"
	type="java.lang.String"%>

<div class="order_summerywrapper">
	<table border="0" width="100%"
		style="font-size: 12px; text-align: right;">
		<tbody>
			<tr>
				<td><spring:theme code="checkout.confirmation.orderSubtotal"
						text="Subtotal:" /></td>
				<td class="totalvalue"><format:price
						priceData="${order.subTotal}" /></td>
			</tr>

			<tr>
				<td><spring:theme code="checkout.confirmation.productDiscount"
						text="Savings:" /></td>
				<td class="totalvalue">
				<c:choose>
					<c:when test="${order.productDiscounts.value gt 0}">
						<span class="redcolor">-<format:price priceData="${order.productDiscounts}" /></span>
					</c:when>
					<c:otherwise>
						<format:price priceData="${order.productDiscounts}" />
					</c:otherwise>
				</c:choose>
				</td>
			</tr>

			<tr>
				<td><spring:theme
						code="checkout.confirmation.promotionalDiscount" text="Savings:" /></td>
				<td class="totalvalue">
								<c:choose>
									<c:when test="${order.voucherCode ne 'LOYALTYVOUCHER' && order.orderDiscounts.value gt 0}">
										<span class="redcolor">-<format:price priceData="${order.orderDiscounts}"  /></span>
									</c:when>
									<c:otherwise>
										$0.00
									</c:otherwise>
								</c:choose>
			</tr>

			<tr>
				<td><spring:theme code="checkout.confirmation.estimateShipping"
						text="Delivery:" /></td>
				<td class="totalvalue"><format:price
						priceData="${order.deliveryCost}" displayFreeForZero="false" /></td>
			</tr>

			<tr>
				<td><spring:theme code="checkout.confirmation.clubOrchardRewards" text="clubOrchard Rewards:" /></td>
				<c:choose>
				  <c:when test="${order.voucherCode eq 'LOYALTYVOUCHER' && order.orderDiscounts.value gt 0}">
				      <td class="totalvalue redcolor">-<format:price priceData="${order.orderDiscounts}"/></td>
				  </c:when>   
				  <c:otherwise>
			              <td class="totalvalue">$0.00</td>
				  </c:otherwise>   
				</c:choose>
			</tr>

			<tr>
				<td><spring:theme code="checkout.confirmation.Tax"
						text="Your order includes tax" /></td>
				<td class="totalvalue">${order.totalTax.formattedValue}</td>
			</tr>
		</tbody>
	</table>
</div>
<div class="order_total">
	<span><spring:theme code="checkout.confirmation.orderTotal"
			text="Total:" /></span> <span class="totalvalue">
			<format:price priceData="${order.totalAmount}" /></span>
</div>

