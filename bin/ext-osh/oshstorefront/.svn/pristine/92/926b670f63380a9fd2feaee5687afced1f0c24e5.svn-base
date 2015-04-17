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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



<h2 style="margin-bottom: 5px;">
	<spring:theme code="checkout.multi.paymentMethod"/>
</h2>
<div class="borderbox">
	<div class="cartformwarp">
		<div class="cartformwarp_inner_orderdetail">
			<p class="selectedcard">${fn:toUpperCase(order.paymentInfo.cardTypeData.name)}&nbsp;${fn:escapeXml(order.paymentInfo.cardNumber)}
			&nbsp;${fn:escapeXml(order.paymentInfo.expiryMonth)}/${fn:escapeXml(order.paymentInfo.expiryYear)}&nbsp;${fn:escapeXml(order.totalAmount.formattedValue )}
				</p>

			<div class="billingadderess">
				<p>
					<strong><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.billingAddress"/></strong>:
				</p>
				<p><c:if test="${not empty order.paymentInfo.billingAddress.title}">${fn:escapeXml(order.paymentInfo.billingAddress.title)}&nbsp;</c:if>${fn:escapeXml(order.paymentInfo.billingAddress.firstName)}&nbsp;${fn:escapeXml(order.paymentInfo.billingAddress.lastName)}</p>
				<p>${fn:escapeXml(order.paymentInfo.billingAddress.line1)}</p>
				<p>${fn:escapeXml(order.paymentInfo.billingAddress.line2)}</p>
				<p>${fn:escapeXml(order.paymentInfo.billingAddress.town)}</p>
				<p>${fn:escapeXml(order.paymentInfo.billingAddress.state.name)}</p>
				<p>${fn:escapeXml(order.paymentInfo.billingAddress.postalCode)}</p>
				<p>${fn:escapeXml(order.paymentInfo.billingAddress.country.name)}</p>
				<p>${fn:escapeXml(order.paymentInfo.billingAddress.phone)}</p>
			</div>
		</div>
	</div>
</div>