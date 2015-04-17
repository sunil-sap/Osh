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
<h2 style="margin-bottom: 5px;">
	<spring:theme code="checkout.summary.deliveryMode.shippingmethod"/>
</h2>
<div class="borderbox">
	<div class="cartformwarp">
		<div class="cartformwarp_inner_orderdetail">
		<p>${order.deliveryMode.description} &nbsp;${order.deliveryCost.formattedValue} </p> 		
			<c:if test="${not empty order.giftMessage}">
			<div class="giftmessage">
				<b><spring:theme code="text.gift.message"/></b>
				<p>
				${order.giftMessage}
				</p>
			</div>
			</c:if>
		</div>
	</div>
</div>