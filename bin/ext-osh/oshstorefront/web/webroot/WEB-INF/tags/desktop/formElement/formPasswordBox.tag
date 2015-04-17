<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="idKey" required="true" type="java.lang.String" %>
<%@ attribute name="labelKey" required="true" type="java.lang.String" %>
<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="mandatory" required="false" type="java.lang.Boolean" %>
<%@ attribute name="labelCSS" required="false" type="java.lang.String" %>
<%@ attribute name="inputCSS" required="false" type="java.lang.String" %>
<%@ attribute name="errorPath" required="false" type="java.lang.String" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>

<%-- <template:errorSpanField path="${path}" errorPath="${errorPath}"> --%>
	<ycommerce:testId code="LoginPage_Item_${idKey}">
		<div>
			<label class="${labelCSS}" for="${idKey}">
				<spring:theme code="${labelKey}"/>
				<c:if test="${mandatory != null && mandatory == true}">
					<span class="mandatory">
						<spring:theme code="login.required" var="loginrequiredText" />
						<img width="5" height="6" alt="${loginrequiredText}" title="${loginrequiredText}" src="${commonResourcePath}/images/mandatory.gif">
					</span>
				</c:if>
			</label>
		</div>

		<div>
			<form:password cssClass="${inputCSS}" id="${idKey}" path="${path}"/>
		</div>
		<div class="clearboth"></div>
	</ycommerce:testId>
<%-- </template:errorSpanField> --%>
<span class="skip mandetoryattribute"><form:errors path="${path}"/></span>