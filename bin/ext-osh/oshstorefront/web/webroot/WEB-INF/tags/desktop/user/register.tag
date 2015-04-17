<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="actionNameKey" required="true" type="java.lang.String" %>
<%@ attribute name="action" required="true" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>


<h2><spring:theme code="register.new.customer" /></h2>
	<div class="clearb"></div>
<div class="formwrap">
	<p><spring:theme code="register.description"/></p>
	<div class="red_note"><spring:theme code="form.required"/></div>
	<form:form method="post" commandName="registerForm" action="${action}" id="register">
		<dl>
			<%-- <formElement:formSelectBox idKey="register.title" labelKey="register.title" path="titleCode"  mandatory="true" skipBlank="false" skipBlankMessageKey="form.select.empty" items="${titles}"/> 
 --%>		<formElement:formInputBox idKey="firstName" labelKey="register.firstName" path="firstName" inputCSS="name required textfield" maxlength="15" mandatory="true"/>
			<span class="error-msg" id="error-msg-firstName" style="display: none"><spring:message code="address.firstName.invalid" /></span>
			<formElement:formInputBox idKey="lastName" labelKey="register.lastName" path="lastName" inputCSS="lastname required textfield" maxlength="15" mandatory="true"/>
			<span class="error-msg" id="error-msg-surname" style="display: none"><spring:message code="address.lastName.invalid" /></span>
			<formElement:formInputBox idKey="email" labelKey="register.email" path="email" inputCSS="email required textfield" mandatory="true"/>
			<span class="error-msg" id="error-msg-email" style="display: none"><spring:message code="register.email.invalid" /></span>
			<formElement:formPasswordBox idKey="password" labelKey="register.pwd" path="pwd" inputCSS="required password textfield" mandatory="true"/>
			<span class="error-msg" id="error-msg-password" style="display: none"><spring:message code="register.password.invalid" /></span>
			<formElement:formPasswordBox idKey="checkPwd" labelKey="register.checkPwd" path="checkPwd" inputCSS="required password textfield" mandatory="true" errorPath="registerForm"/>
			<span class="error-msg" id="error-msg-conform-password" style="display: none"><spring:message code="register.conform.password.invalid" /></span>
		</dl>
		<span style="display: block; clear: both;">
			<ycommerce:testId code="register_Register_button">
				
				<div class="btn2 floatl btnszres">
					<input id="submitregister" type="submit" value='<spring:theme code='${actionNameKey}'/>'>
				</div>
			</ycommerce:testId>
		</span> 
	</form:form>
</div>
<script type="text/javascript">

  $('#submitregister')
	  .click(
	  		function(e) {
	  			e.preventDefault();
	  			var allfilled = true;
	  			var password = $("#password").val();
	  			var confirmpassword = $("#checkPwd").val();
				$(".error-msg").hide();
				if (!$("#email").val()) {										
					$("#error-msg-email").show();
					allfilled = false;
				}
				var email = $("#email").val();
				var emailRegex = new RegExp(
				/^[+a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i);
				var email1 = emailRegex.test(email);
				if (email !="" && !email1) {
					$("#error-msg-email").show();										
					allfilled = false;
				}
				
				
				if (!$("#firstName").val()) {
					$("#error-msg-firstName").show();
					allfilled = false;
				}
				if (!$("#lastName").val()) {
					$("#error-msg-surname").show();
					allfilled = false;
				}
				if(!$("#password").val() ||!$("#checkPwd").val())
				{  
					if (!$("#password").val()) {
						$("#error-msg-password").show();
						allfilled = false;
					}
					
					if (!$("#checkPwd").val()) {
						$("#error-msg-conform-password").show();
						allfilled = false;
					}
				}
				else if ($("#password").val().length < 6)
				{
					$("#password").after('<br> <span class="error-msg">Please enter a strong Password (at least 6 characters)</span>');
					 allfilled = false;	
				}
				else if(password != confirmpassword)
					{
					 $("#checkPwd").after('<br> <span class="error-msg">Password and Confirmation Password  do not match</span>');
					 allfilled = false;
					}
				if (allfilled) {
									
					$("#register").submit();
				}					
				
	  		});
</script>