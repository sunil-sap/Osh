<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="actionNameKey" required="false"
	type="java.lang.String"%>
<%@ attribute name="action" required="false" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ attribute name="order" required="true"
	type="de.hybris.platform.commercefacades.order.data.OrderData"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<div class="guestlectcontent" style="margin-right: 40px; margin-left: 25px;">
	<div class="padd10">
		<p>
			<spring:message code="checkout.confirmation.guestMessage1" />
			.
		</p>
		<p>
			<spring:message code="checkout.confirmation.guestMessage2" />
		</p>
	</div>
</div>
<div class="guestrightcontent" style="margin-left: 0px; margin-right: 65px;">
	<p style="margin-bottom: 3px; font-weight: bold; font-size: 15px;">
		<spring:theme code="text.account.guest.order" />
	</p>
	<strong><spring:theme code="guestlogin.email" /> ${email}</strong>
	<div class="formwrap">
		<div class="guestregistration">
			<form:form method="post" commandName="registerForm" id="guestUserReg"
				action="${request.contextPath}/login/easyRegister.json">

				<tr>
					<td><spring:theme code="guestlogin.password" /></td>
				</tr>
				<br />
				<tr>
					<td><form:password path="pwd" title="Password" id="pass"
							showPassword="true" /></td>
				</tr>
				<br />
				<tr>
					<spring:theme code="guestlogin.comform.password" />
				</tr>
				<tr>
					<br />
					<td><form:password path="checkPwd" id="c_pass" /></td>
					<%-- <td><form:errors path="confirmPassword" cssClass="error" />
				</td> --%>
					<p>
						<span class="error-msg" id="error-msg_confirm"
							style="display: none"><spring:message
								code="register.checkPwd.invalid" /></span> <span class="error-msg"
							id="error-msg_length" style="display: none"><spring:message
								code="register.pwd.invalid" /></span> <span class="error-msg"
							id="error-msg_invalid" style="display: none"><spring:message
								code="register.email.invalid" /></span>
					</p>
				</tr>
				<div style="display: none;">
					<form:input path="firstName" />
					<form:input path="lastName" />
					<form:input path="email" />
					<form:input path="orderCode" value="${order.code}" />
					<formElement:formSelectBox idKey="register.title"
						labelKey="register.title" path="titleCode" mandatory="true"
						skipBlank="true" items="${titles}" />
				</div>

				<p>
					<input type="checkbox" name="guestsubscribe"
						value="storenewsletter"> <label class="checkboxtext">
						<spring:theme code="text.account.signup" />
					</label>

				</p>
				<p></p>
				<span class="btngreen"
					style="margin-top: 22px;"><input
					type="submit" id="btncreateaccount" value="Create Account"></span>
				<br />
			</form:form>
			<div class="guest_pasword_img" style="margin-left: 545px;">
				<cms:slot var="feature"
					contentSlot="${slots.ES_PasswordPolicySection}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>
		</div>

	</div>

</div>
<script type="text/javascript">
	$('#btncreateaccount').click(function(e) {
		e.preventDefault();
		var allowSubmit = true;

		var pass = $("#pass").val();
		var c_pass = $("#c_pass").val();

		if ((pass != c_pass)) {
			$("#error-msg_confirm").show();
			$("#error-msg_length").hide();
			allowSubmit = false;
		}
		if (allowSubmit != false && pass.length < 6 && c_pass.length < 6) {
			$("#error-msg_length").show();
			$("#error-msg_confirm").hide();
			allowSubmit = false;
		}

		if (allowSubmit == true) {
			$("#guestUserReg").submit();
		}
	});
</script>