<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="actionNameKey" required="true"
	type="java.lang.String"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ attribute name="action" required="true" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="formUtil" tagdir="/WEB-INF/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>
<h2><spring:theme code="register.customer"/></h2>
<div class="clearb"></div>
<div class="formwrap_checkoutlogin">
	<div class="red_note"><spring:theme code="address.required"/></div><br>
	<form:form action="${action}" method="post" commandName="loginForm">

			<formElement:formInputBox idKey="j_username" labelKey="register.member" path="j_username" inputCSS="email required textfield3" mandatory="true" />
			<formElement:formPasswordBox idKey="j_password" labelKey="register.return" path="j_password" inputCSS="required password textfield3" mandatory="true"/>

		<div>
			<a href="<c:url value='/login/pw/request'/>" class="password-forgotten" style="color:blue"><spring:theme code="login.link.forgottenPwd"/></a>
		</div>
		<p>
			<input type="checkbox" name="remeberpassword" value="remeberpassword" />
			<spring:theme code="register.remember"/>
		</p>
		<br clear="all" />
		<div class="clearb"></div>
		<div class="btn3 floatl btnsz1">
			<input type="submit" value="Secure Sign In and Checkout" id="submit" />
		</div>
	</form:form>
	<div class="clearb"></div>
	<div class="error_message">
	<common:globalMessages />
	</div>
	</div>