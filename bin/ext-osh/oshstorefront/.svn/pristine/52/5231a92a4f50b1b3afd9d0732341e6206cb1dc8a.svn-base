<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/mobile/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>

<div class="ui-grid-a" data-theme="b" data-role="content">
	<div class="ui-grid-a" data-theme="d">
		<h4 class="subItemHeader">
			<spring:theme code="text.deliveryAddress" text="Delivery Address" />
		</h4>
	</div>
	<div class="ui-grid-a" data-theme="d">
		<ul class="mFormList">
			<li>${order.deliveryAddress.title}&nbsp;${order.deliveryAddress.firstName}&nbsp;${order.deliveryAddress.lastName}</li>
			<li>${order.deliveryAddress.line1}</li>
			<c:if test="${not empty order.deliveryAddress.line2}">
				<li>${order.deliveryAddress.line2}</li>
			</c:if>
			<li>${order.deliveryAddress.town}</li>
			<li>${order.deliveryAddress.postalCode}</li>
			<li>${order.deliveryAddress.country.name}</li>
		</ul>
	</div>
</div>
