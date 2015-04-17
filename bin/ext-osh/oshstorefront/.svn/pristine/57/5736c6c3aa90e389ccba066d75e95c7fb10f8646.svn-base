<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="header" tagdir="/WEB-INF/tags/desktop/common/header"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>


<template:page pageTitle="${pageTitle}">
	<div id="middleContent">
		<div class="breadcrumb"></div>
		<div class="innermiddleContent">
			<div>
			<cms:slot var="feature" contentSlot="${slots.GlobalSection}">
   			 <cms:component component="${feature}" />
   			 </cms:slot>  
			</div>

			<div class="middlewrap_contact_us">
				<h2><spring:theme code="contactus.header"></spring:theme></h2>
				<div class="left_contact_us">
					<div class="img_contact_us">
					<cms:slot var="contactUsImage" contentSlot="${slots.ContactUsImage}">
						<cms:component component="${contactUsImage}"/>
					</cms:slot> 
					</div>
				</div>
				<div class="right_contact_us">
					<div class="form_contact_us">
						<div class="red_note_contact_us">
							<p><spring:theme code="contactus.required.field"></spring:theme></p>
						</div>
						<c:url value="/contactUs" var="contactUsActionUrl" />
						<form:form id="contactUs" method="post"
							action="${contactUsActionUrl}" commandName="contactUsEmailForm">
							<div class="firs_last_name">
								<div class="editcolumeleft_contact_us">
									<span><spring:theme code="contactus.first.name"></spring:theme></span> <br> 
									<input id="firstname" class="first name required textfield" type="text" name="firstname">
									<span class="error-msg" id="error-msg-firstName" style="display: none">
									<spring:message code="address.firstName.invalid" /></span>
								</div>
								
								<div class="editcolumeright_contact_us">
									<span><spring:theme code="contactus.last.name"></spring:theme></span> <br> 
									<input id="lastname" class="last_name required textfield" type="text" name="lastname">
								</div>
								<span class="error-msg" id="error-msg-surname" style="display: none">
									<spring:message code="address.lastName.invalid" /></span>
							</div>
							<br clear="all">
							<br clear="all">
							<div class="email_address_contact_us">
								<span><spring:theme code="contactus.email.address"></spring:theme></span> <br> 
								<input id="email" class="email required textfield" type="text" name="email">
							</div>
							<span class="error-msg" id="error-msg-email" style="display: none"><spring:message code="register.email.invalid" /></span>
							<p><spring:theme code="contactus.subject"></spring:theme></p>
							<div class="contact_radio">
								<input type="radio" name="contact_us_subject" value="club_orchard" id="contact_us_subject"><spring:theme code="contactus.subject.club.orchard"></spring:theme><br> 
									<input type="radio" name="contact_us_subject" value="commercial" id="contact_us_subject"><spring:theme code="contactus.subject.commercial.special.order"></spring:theme><br> 
									<input type="radio"name="contact_us_subject" value="consumer" id="contact_us_subject"><spring:theme code="contactus.subject.consumer.special.order"></spring:theme><br>
									 <input type="radio"name="contact_us_subject" value="marketig" id="contact_us_subject"><spring:theme code="contactus.subject.marketing"></spring:theme><br> 
									 <input type="radio" name="contact_us_subject" value="online" id="contact_us_subject"><spring:theme code="contactus.subject.online.orders"></spring:theme><br> 
									 <input type="radio" name="contact_us_subject" value="product" id="contact_us_subject"><spring:theme code="contactus.subject.product.questions"></spring:theme><br> 
									 <input type="radio" name="contact_us_subject"value="store" id="contact_us_subject"><spring:theme code="contactus.subject.store.feedback"></spring:theme>
								<div class="number_phone_contact_us">
									<div class="number_store_contact_us">
										<span><spring:theme code="contactus.store.number"></spring:theme></span> <select class="select_contact" name="store_name" id="store_name">
										<option></option>
											<c:forEach var="store" items="${posData}" >
											<c:if test="${store.name !='online'}">
											  <option >${store.name}</option>
										    </c:if>
											</c:forEach>
										</select>
									</div>
									<div class="phone_store">
										<p><span class="phone_contact"><spring:theme code="contactus.store.phone"></span><input id="storenumber" class="store_phone_contact" type="text" name="storenumber"></spring:theme>
											
										</p>
									</div>
								</div>
								<input type="radio" name="contact_us_subject" value="wesite"><spring:theme code="contactus.website.experience"></spring:theme><br> 
								<input type="radio"	name="contact_us_subject" value="other"><spring:theme code="contactus.other"></spring:theme><br>
							</div>
							<%-- <span class="error-msg" id="error-msg-contact_us_subject" style="display: none"><spring:message code="contactus.subject.invalid" /></span> --%>
							<div class="message_contact">
								<p><spring:theme code="contactus.email.content"></spring:theme>
									<textarea id="message" class="message" name="email_content" maxlength="3000"></textarea>
								</p>
							</div>
							<span class="error-msg" id="error-msg-content" style="display: none"><spring:message code="contactus.msg.invalid" /></span>
							
							<div class="btn2 osh_btn_msg_contact">
								<input id="submit" type="submit" value="Submit">
							</div>
						</form:form>

					</div>
				</div>
			</div>
		</div>
	</div>
	
	
  <script type="text/javascript">	
	  $('#submit')
	  .click(
	  		function(e) {
	  			
	  			var allfilled = true;
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
				
				if (!$("#firstname").val()) {
					e.preventDefault();
					$("#error-msg-firstName").show();
					allfilled = false;
				}
				if (!$("#lastname").val()) {
					
					$("#error-msg-surname").show();
					allfilled = false;
				}
				 /*  if ($("#contact_us_subject").val()!=true) {
					 e.preventDefault();
					$("#error-msg-contact_us_subject").show();
					allfilled = false; 
				}   */
				if (!$("#message").val()) {
					
					$("#error-msg-content").show();
					allfilled = false;
				}
				
				if (allfilled) {
					
					$('#contactUs').submit();
					
				}					
	  		});
	  
	 </script> 
	

</template:page>