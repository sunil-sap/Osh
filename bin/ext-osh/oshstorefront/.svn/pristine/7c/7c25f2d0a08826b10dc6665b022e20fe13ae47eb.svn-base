<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/cart" var="basketUrl"/>
<a href="${basketUrl}" id="top-nav-bar-cart" data-role="button" data-theme="b" 
	data-iconpos="left" data-icon="cart" title="Cart" class="ui-btn ui-btn-up-b ui-btn-icon-left ui-btn-corner-all ui-shadow">
	<span class="ui-btn-inner ui-btn-corner-all">
		<span class="ui-btn-text"> 
			<ycommerce:testId code="miniCart_items_label">
				&nbsp;<spring:theme text="items" code="cart.count" arguments="${totalItems}" />
			</ycommerce:testId>
		</span>
		<span class="ui-icon ui-icon-cart ui-icon-shadow"></span> 
	</span>
</a>
