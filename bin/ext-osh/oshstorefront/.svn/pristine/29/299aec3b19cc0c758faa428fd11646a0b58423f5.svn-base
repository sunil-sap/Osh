<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="actionNameKey" required="true" type="java.lang.String" %>
<%@ attribute name="action" required="true" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>

<h2><spring:theme code="login.title"/></h2>
	<div class="clearb"></div>
<div class="formwrap">
		<p><spring:theme code="login.description"/></p>
		<div class="red_note"><spring:theme code="login.required.message"/></div>

		<form:form action="${action}" method="post" commandName="loginForm">
			<c:if test="${not empty message}">
				<span class="errors">
					<spring:theme code="${message}"/>
				</span>
			</c:if>
			<dl>
				<c:if test="${not empty accErrorMsgs}">
					<span class="form_field_error">
				</c:if>

				<formElement:formInputBox idKey="j_username" labelKey="login.email" path="j_username" inputCSS="email required textfield" mandatory="true"/>
				<formElement:formPasswordBox idKey="j_password" labelKey="login.password" path="j_password" inputCSS="required password textfield" mandatory="true"/>

				<dd>
					<a href="<c:url value='/login/pw/request'/>" class="password-forgotten "><p><spring:theme code="login.link.forgottenPwd"/></p></a>
				</dd>

				<c:if test="${not empty accErrorMsgs}">
					</span>
				</c:if>
			</dl>
			<span style="display: block; clear: both;">
			<ycommerce:testId code="login_Login_button">
			<div class="btn2 floatl btnsz" >
					<input id="submit" type="submit" value='<spring:theme code='${actionNameKey}'/>'>
				</div>
				<%-- <button type="submit" class="btn2 floatl"><spring:theme code="${actionNameKey}"/></button> --%>
			</ycommerce:testId>
			</span>
			<div class="error_message">
			<div id="globalMessages">
				<common:globalMessages />
			</div>
			</div>
		</form:form>
</div>

