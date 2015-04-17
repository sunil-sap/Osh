<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="deliveryAddress" required="true" type="de.hybris.platform.commercefacades.user.data.AddressData"%>
<%@ attribute name="deliveryMode" required="true" type="de.hybris.platform.commercefacades.order.data.DeliveryModeData"%>
<%@ attribute name="paymentInfo" required="true" type="de.hybris.platform.commercefacades.order.data.CCPaymentInfoData"%>
<%@ attribute name="requestSecurityCode" required="true" type="java.lang.Boolean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="checkout" tagdir="/WEB-INF/tags/mobile/checkout/multi"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="checkout_summary_flow">
	<div class="ui-grid-a" data-theme="d">
		<h3 class="infotext">Please Review Your Order</h3>
	</div>
	<checkout:summaryFlowDeliveryAddress deliveryAddress="${deliveryAddress}" />
	<checkout:summaryFlowDeliveryMode deliveryMode="${deliveryMode}" />
	<checkout:summaryFlowPayment paymentInfo="${paymentInfo}" requestSecurityCode="${requestSecurityCode}" />
</div>
