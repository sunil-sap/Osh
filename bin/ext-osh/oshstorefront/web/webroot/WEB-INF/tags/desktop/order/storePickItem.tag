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
<spring:theme code="checkout.summary.store.pickup.address"/>
</h2>
<div class="borderbox">
	<div class="cartformwarp">
		<div class="cartformwarp_inner_orderdetail">
			<div class="billingadderess">
				
				<p>${fn:escapeXml(posData.name)}</p>
				<p>${fn:escapeXml(posData.address.line1)}</p>
				<p>${fn:escapeXml(posData.address.line2)}</p>
				<p>${fn:escapeXml(posData.address.town)}</p>
				<p>${fn:escapeXml(posData.address.postalCode)}</p>
				
			</div>
		</div>
	</div>
</div>