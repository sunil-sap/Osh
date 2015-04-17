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
	<spring:theme code="checkout.single.shipping"/>
</h2>
<div class="borderbox">
	<div class="cartformwarp">
		<div class="cartformwarp_inner_orderdetail">
			<c:if test="${not empty order.deliveryAddress.title}"><p>${fn:escapeXml(order.deliveryAddress.title)}&nbsp;</c:if>${fn:escapeXml(order.deliveryAddress.firstName)}&nbsp;${fn:escapeXml(order.deliveryAddress.lastName)}</p>
			<p>${fn:escapeXml(order.deliveryAddress.line1)}</p>
			<p>${fn:escapeXml(order.deliveryAddress.line2)}</p>
			<p>${fn:escapeXml(order.deliveryAddress.town)}</p>
			<p>${fn:escapeXml(order.deliveryAddress.state.name)}</p>
			<p>${fn:escapeXml(order.deliveryAddress.postalCode)}</p>
			<p>${fn:escapeXml(order.deliveryAddress.country.name)}</p>
			<p>${fn:escapeXml(order.deliveryAddress.phone)}</p>
		</div>
	</div>
</div>

