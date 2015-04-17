<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="sendEmailForm" required="true"
	type="com.hybris.osh.storefront.forms.SendEmailForm"%>
<c:set value="${oshVariantProduct}" var="product" />
<spring:url value="/p/${product.code}/sendEmailToFriend.json" var="sendEmailUrl" />
<div id="emailthis" title="Email a Friend"
	style="display: none; height: 500px;">
	<form:form id="emailthispage" method="post" commandName="sendEmailForm" action="${sendEmailUrl}">
		<div class="label"><spring:theme code="text.to"/></div>
		<form:textarea class="textfield required email" path="to_emailadd" />
		<div class="clearb"></div>
		<div class="label"><spring:theme code="text.subject"/></div>
		<form:input class="textfield required " type="text" path="subject"/>
		<%-- <form:hidden path="productModel" value="${sendEmailForm.productModel}" /> --%>
		<div class="clearb"></div>
		<div class="label"><spring:theme code="text.from"/></div>
		<form:input class="textfield email required " type="text"
			path="from_email"/>
		<div class="clearb"></div>
		<div class="label"><spring:theme code="text.note"/></div>
		<form:textarea class="textfield required " path="notes" />
		<!-- <div class="clearb"></div> -->
		<div class="btn_container">
			<button class="btn_sendemail" id="sendEmail" onclick="submitEmail()" ><spring:theme code="text.send"/></button>  <button
				class="btn_cancel" value="Cancel" ><spring:theme code="checkout.multi.cancel"/></button>
		</div>
	</form:form>
	<br clear="all">
	<div class="clearb"></div>
	<div class="footertextwrap">
		<a href="#"><spring:theme code="text.privacy"/></a>
	</div>
</div>

<script type="text/javascript">
	
	function submitEmail(){
		var form=$('#emailthispage');
		var SendEmailForm;
			$.ajax(
				{				
					url :'${sendEmailUrl}',
					type : 'POST',
					dataType : 'json',
					data : form.serialize(),
					success : function(data){
						 $.colorbox.close();	
					     window.location.href = '';
					}
				});
		}
	
			
</script>
