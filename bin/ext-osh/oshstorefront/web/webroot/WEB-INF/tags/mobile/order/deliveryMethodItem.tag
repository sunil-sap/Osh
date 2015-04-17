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
			<spring:theme code="text.deliveryMethod" text="Delivery Method" />
		</h4>
	</div>
	<div class="ui-grid-a" data-theme="d">
		<ul class="mFormList">
			<li>${order.deliveryMode.name}</li>
			<li>${order.deliveryMode.description}</li>
		</ul>
	</div>
</div>
