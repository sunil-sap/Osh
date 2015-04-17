<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="actionNameKey" required="true"
	type="java.lang.String"%>
<%@ attribute name="action" required="true" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="formUtil" tagdir="/WEB-INF/tags/form"%>
<form:form action="${action}" method="post" commandName="loginForm"
	id="wishlistloginForm">
	<div class="heading">
		<h1>
			<b style="font-size: 18px;"><spring:theme
					code="wishlistLogin.yourList"></spring:theme></b>
		</h1>
		<spring:theme code="wishlistLogin.youListMessage"></spring:theme>
	</div>
	<div class="clearb"></div>
	<br clear="all">
	<div class="email">
		<spring:theme code="wishlistLogin.email"></spring:theme>
		<form:input path="j_username" mandatory="true" id="j_username" onblur="$('#error-msg-email').hide();" 
			cssStyle="margin-left: 12px;" />
		<br /> <span class="error-msg wishlist_errortext"
			id="error-msg-email" style="display: none"><spring:message
				code="register.email.invalid" /></span> <br>
		<spring:theme code="wishlistLogin.password"></spring:theme>
		<form:password path="j_password" id="j_password" mandatory="true" onblur="$('#error-msg-password').hide();"
			cssStyle="margin-left: 36px;" />
		<a href="<c:url value='/login/pw/request'/>"
			class="password-forgotten" style="margin-left: 11px; color: #0000FF;"><spring:theme
				code="login.link.forgottenPwd" /></a>
	</div>
	<span class="error-msg wishlist_errortext" id="error-msg-password"
		style="display: none"><spring:message
			code="register.password.invalid" /></span>
	<br clear="all" />

	<div class="wishlist_sign_btn input wishlist_login_btn">
		<input type="submit" value="Sign In" id="wishlistsubmit" />
	</div>
	<div class="WishList_last">
		<span class="wishlistlogin_text"><spring:theme
				code="text.wishlist.login" /> </span> <a href="<c:url value='/login'/>"
			class="password-forgotten forgot_password"><spring:theme
				code="text.wishlist.createlogin" /></a>
	</div>

</form:form>
<div class="clearb"></div>
<script type="text/javascript">
$(document).ready(function(){

	$("#j_username , #j_password").val('');
	$("#wishlistsubmit").click(function(e) {
		e.preventDefault();
		var allfilled = true;
		if (!$("#j_username").val()) {
			$("#error-msg-email").show();
			allfilled = false;
		}
		var email = $("#j_username").val();
		var emailRegex = new RegExp(
		/^[+a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i);
		var email1 = emailRegex.test(email);
		if (email !="" && !email1) {
			$("#error-msg-email").show();										
			allfilled = false;
		}
		if (!$("#j_password").val()) {
			$("#error-msg-password").show();
			allfilled = false;
		}
		if (allfilled) {
			$("#wishlistloginForm").submit();
		}
	});
	
});



</script>