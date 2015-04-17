<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>


<h2>
	<spring:theme code="guestlogin.title" />
</h2>
<div class="clearb"></div>
<div class="formwrap_checkoutlogin">
	<p><spring:theme code="register.checkout"/></p>
	<p><spring:theme code="register.conform"/></p>

	<form:form id="guestLoginForm"
		action="${request.contextPath}/login/checkout/registerGuest"
		commandName="registerForm" method="post">
		
		<div class="sapce"></div>

		<div class="btn3 floatl btnsz2" >
			<input type="submit" value="Checkout as a Guest"
				name="Checkout as a Guest" />
		</div>
		
	
	</form:form>
</div>

<script type="text/javascript">
$(document)
.ready(
		function() {
			$('#GuestCheckout')
			.click(
					function(e) {
						if (!$("#enter-email").val()) {
							
							$("#enter-email").parent().find(".error-msg").show();
							$("#enter-email").parent().find(".error-msg").css('color','#C52615');
							$('label[for=enter-email-address]').css('color','#C52615');	
							return false;
						}
						$('#guestLoginForm').submit();
					});
				});	
 </script>
