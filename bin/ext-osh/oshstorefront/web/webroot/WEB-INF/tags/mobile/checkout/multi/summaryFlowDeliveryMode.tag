<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="deliveryMode" required="true" type="de.hybris.platform.commercefacades.order.data.DeliveryModeData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>

<div class="ui-grid-a" data-theme="b" data-role="content">
	<ycommerce:testId code="checkout_deliveryModeData_text">
		<div>
			<h4 class="subItemHeader">
				<spring:theme code="checkout.summary.deliveryMode.header" htmlEscape="false" />
				<span></span>
			</h4>
		</div>
		<div class="ui-grid-a" data-theme="b" style="width: 100%">
			<div class="ui-block-a" style="width: 85%">
				<ul class="mFormList">
					<li>${deliveryMode.name} (${deliveryMode.code})</li>
					<li class="deliverymode-description" title="${deliveryMode.description} - ${deliveryMode.deliveryCost.formattedValue}">
						${deliveryMode.description}&nbsp;-&nbsp;${deliveryMode.deliveryCost.formattedValue}
					</li>
				</ul>
			</div>
			<div class="ui-block-b" style="width: 15%">
				<ycommerce:testId code="checkout_changeDeliveryMode_element">
					<c:url value="/checkout/multi/choose-delivery-method" var="chooseDeliveryMethodUrl" />
					<a href="${chooseDeliveryMethodUrl}" data-theme="c">
						<spring:theme code="mobile.checkout.edit.link" />
					</a>
				</ycommerce:testId>
			</div>
		</div>
	</ycommerce:testId>
</div>
