<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ attribute name="specifications" required="true"
	type="java.util.Collection"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
	
<div id="contenttab" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
	<ycommerce:testId code="productOptions_tabs"> 
		<ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
			<li class="ui-state-default ui-corner-top ui-tabs-active"><a class="ui-tabs-anchor" href="javascript:void(0);" id="#contenttabs-1"><spring:message code="osh.product.page.tab.description"/></a></li>
			<li class="ui-state-default ui-corner-top"><a class="ui-tabs-anchor" href="javascript:void(0);" id="#contenttabs-2"><spring:message code="osh.product.page.tab.specifications"/></a></li>
			<li class="ui-state-default ui-corner-top"><a class="ui-tabs-anchor" href="javascript:void(0);" id="#contenttabs-3"><spring:message code="osh.product.page.tab.shipping"/></a></li>
			<li class="ui-state-default ui-corner-top"><a class="ui-tabs-anchor" href="javascript:void(0);" id="#contenttabs-4"><spring:message code="osh.product.page.tab.helpfuladvice"/></a></li>
		</ul>
	 </ycommerce:testId> 


	<div id="contenttabs-1" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
		<product:productDescriptionTab product="${product}"/>
	</div>


	<div id="contenttabs-2" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
		<product:productSpecificationsTab product="${product}" specifications="${specifications}"/>
	</div>

	<div id="contenttabs-3" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
		<product:productShippingTab product="${product}"/>
	</div>

	<div id="contenttabs-4" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
		<product:productHelpfulAdviceTab product="${product}"/>
	</div>
</div>

<%-- 
<c:if test="${showReviewForm}">

	<script>
		/*         */
		
		$(function() {
			$("#prod_tabs").tabs({
				selected : $('#tab_strip').children().size() - 1
			});
		});
		/*   */
	</script>
</c:if>
<c:if test="${!showReviewForm}">
	<script>
		/*         */
		$(function() {
			$("#prod_tabs").tabs({
				selected : 0
			});
		});
		/*   */
	</script>
</c:if> --%>