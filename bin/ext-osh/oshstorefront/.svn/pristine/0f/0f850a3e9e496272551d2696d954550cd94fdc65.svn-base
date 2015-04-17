<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<spring:url value="/checkout/single/summary/createPaymentDetails.json"
	var="createPaymentUrl" />
<spring:url value="/checkout/single/summary/setPaymentDetails.json"
	var="setPaymentDetailUrl" />
<spring:url value="/checkout/single/summary/removeSavedCards.json"
	var="removePaymentDetailUrl" />
<spring:url
	value="/checkout/single/summary/updatePaymentDetailsForm.json"
	var="updatePaymentDetailUrl" />
<script type="text/javascript">
	/*         */
	var paymentId;
	$(document).ready(
			function() {
				$('#paymentId').val(
						$("#cardsListForm input[type='radio']:checked").val());
				$("#addcardbutton").click(function() {
					clearCardTextFields();
					$("#paymentId").val("");
					$("#cardsListForm").hide();
					$("#addcardbutton").hide();
					$("#creditCardForm").show();
					$("#useCard").hide();
					$("#lastButton").show();
					$("#lastInTheForm").show();
				});

				$('#useCard').click(function() {
					
					var allfilled=PaymentFormValidation(event);
					if(allfilled)
						{
					$.postJSON('${setPaymentDetailUrl}', {
						paymentId : paymentId
					}, handleSelectSavedCardSuccess);
					fromPayment = true;
					return false;
						}
				});

				$(':radio[name="cardTypeCode"]').on('change', function() {
					clearCardTextFieldsOnly();
					var cardName = $(this).val();
					if (cardName == 'amex') {
						$("#cardNumber").attr('maxlength', '15');
						$("#cvvNo").attr('maxlength', '4');
					} else {
						$("#cardNumber").attr('maxlength', '16');
						$("#cvvNo").attr('maxlength', '3');
					}
				});
				var cardName = $(this).val();
				if (cardName == 'amex') {
					$("#cardNumber").attr('maxlength', '15');
					$("#cvvNo").attr('maxlength', '4');
				}
				else {
					$("#cardNumber").attr('maxlength', '16');
					$("#cvvNo").attr('maxlength', '3');
				}
			
				updateBillingAddressForm();

				$("#differentAddress").click(function() {
					updateBillingAddressForm();
					$("#lastButton").show();
					$("#lastInTheForm").show();
					$("#useCard").hide();
				})

				bindCycleFocusEvent();
				$.fn.colorbox.resize({});
				
		
				
			})

	function bindCycleFocusEvent() {
		
		$('#lastInTheForm').blur(function() {
			$('#paymentDetailsForm [tabindex$="10"]').focus();
		})
	}
	
function clearCardTextFields() {
	$("#nameOnCard").val('');
	$("#cardNumber").val('');
	$("#ExpiryMonth").val('');
	$("#ExpiryYear").val('');
	$("#radioId").attr('checked',false);
	$("#cvvNo").val('');		
	}
	
function clearCardTextFieldsOnly() {
	$("#nameOnCard").val('');
	$("#cardNumber").val('');
	$("#ExpiryMonth").val('');
	$("#ExpiryYear").val('');
	$("#cvvNo").val('');		
	}

function updateBillingAddressForm() {
		var newAddress = $('#differentAddress').attr("checked");
		var storepickup = ${storepickup};
		if(${storepickup})
			{
			 $('#differentAddress').attr("checked" , "checked" );
			 newAddress = $('#differentAddress').attr("checked");
			}
		if (newAddress) {
			$("#newBillingAddressFields :input").removeAttr('disabled');
		} else {
			 $("#newBillingAddressFields :input").attr('disabled', 'disabled');
		}
	}

	function getPaymentID(paymentid) {
		paymentId = paymentid;
	}

	function removePaymentMethod(paymentid) {
		$.ajax({
			url : '${removePaymentDetailUrl}',
			type : 'POST',
			data : {
				paymentId : paymentid
			},
			dataType : 'json',
			success : function(data) {
				alert('remove successful !!!')
				$.colorbox.close();
				location.reload();
				//refreshPaymentDetailsSection(data);
				
			},
			error : function(xht, textStatus, ex) {
				alert("Failed to remove card Error details [" + xht + ", "
						+ textStatus + ", " + ex + "]");
			}
		});
		return false;
	}

	function fillBillingAddressForm() {
		var paymentId = $('.change_address_button').attr('id');
		var options = {
			url : '${getPaymentDetailsFormUrl}',
			data : {
				createUpdateStatus : ''
			},
			type : 'GET',
			success : function(data) {
				$('#payment_details_adderess_col').html(data);

			}
		};

		$.ajax(options);
	}
	
	$("#differentAddress").click(function() {

		$("#lastButton").show();
	})
	function updatePaymentMethod(paymentid) {
		paymentId = paymentid;
		$.ajax({
			url : '${updatePaymentDetailUrl}',
			type : 'GET',
			data : {
				paymentId : paymentid
			},
			success : function(data) {
				var billingAddress = data['billingAddress'];
				var paymentId = data['paymentId'];

				$("#paymentId").val(data['paymentId']);
				$("#issueNumber").val(data['issueNumber']);
				$("#title").val(billingAddress.titleCode);
				$("#firstName").val(billingAddress.firstName);
				$("#surname").val(billingAddress.lastName);
				$("#line1").val(billingAddress.line1);
				$("#line2").val(billingAddress.line2);
				$("#townCity").val(billingAddress.townCity);
				$("#postcode").val(billingAddress.postcode);
				$("#phoneNo").val(billingAddress.phoneNo);
				$("#select-countries").val(billingAddress.countryIso);
				$("#select-countries").trigger('change');
				$("#enter-states").val(billingAddress.stateIso);
				$("#email").val(billingAddress.email);
				$("#nameOnCard").val(data['nameOnCard']);
				$("#cardNumber").val(data['cardNumber']);
				$("#ExpiryMonth").val(data['expiryMonth']);
				$("#ExpiryYear").val(data['expiryYear']);
				$(":radio[value="+data['cardTypeCode']+"]").attr('checked',true);
				$("#cvvNo").val('123');			
				
			},
			error : function(xht, textStatus, ex) {
				alert("Failed to remove card Error details [" + xht + ", "
						+ textStatus + ", " + ex + "]");
			}
		});
		return false;
	}
	
	//Function to allow only numbers to textbox
	function validate(key)
	{
	//getting key code of pressed key
	var keycode = (key.which) ? key.which : key.keyCode;
	//var phn = document.getElementById('cardNumber');
	//comparing pressed keycodes
	if (keycode == 46)
    {
        return false;
    } 
	if (!(keycode==8 || keycode==46)&&(keycode < 48 || keycode > 57))
	{
	return false;
	}
	else
	{
	return true;
	}
	}
</script>

<div class="headingtitle_payment_method_form">
	<span> <spring:theme code="checkout.multi.paymentMethod"/> </span>
</div>
<div class="item_container">
	<c:if test="${not empty paymentInfoData }">
		<%-- <span class="saved_card">
			<button class="form left use_saved_card_button"><spring:theme code="checkout.summary.paymentMethod.addPaymentDetails.useSavedCard"/></button>
			<p><spring:theme code="checkout.summary.paymentMethod.addPaymentDetails.useSavedCard.description"/></p>
		</span> --%>
	</c:if>
	<form:form method="post" commandName="paymentDetailsForm" id = "newPaymentDetailsForm"
		action="${createPaymentUrl}" class="create_update_payment_form">
		<div class="error_paymentform">
			<common:globalMessages />
		</div>
		<span class="error-msg" id="misc-error-msg"
						style="display: none"><spring:message
							code="address.form.invalid" /></span>
		<div class="payment_details_left_col"
			id="payment_details_adderess_col">
			<div id="creditCardForm" style="${fn:length(paymentInfos) == 0 ? "display:block;" : "display:none;"}">
				<p>
					<spring:theme
						code="checkout.summary.paymentMethod.addPaymentDetails.enterYourCardDetails" />
				</p>
				<div class="required_field">
					<spring:theme code="form.required" />
				</div>
				<form:hidden path="paymentId" id="paymentId"
					class="create_update_payment_id"
					status="${not empty createUpdateStatus ? createUpdateStatus : ''}" />
				<div id="addcardwrapper">
					<div class="addcardwrapperinner">
						<div class="selectcardtype">
							<c:forEach items="${cardTypes}" var="card">
								<div class="cardbox">
									<div>
										<img
											src="${request.contextPath}/_ui/desktop/osh/images/global/${card.name}.gif">
									</div>
									<form:radiobutton id="radioId" path="cardTypeCode"
										value="${card.code}" />
								</div>
							</c:forEach>
							<br /> <br /> <br />
							<template:errorSpanField path="cardTypeCode">
							</template:errorSpanField>
						</div>
					</div>
				</div>
				<br /> <br />
				<div>
				<spring:theme code="payment.cardNumber"/>
				</div>
				<div>
				<form:input id="cardNumber" path="cardNumber"
					cssClass="text textcardnumber" maxlength="16" mandatory="true"
					tabindex="3" onkeypress="return validate(event)"/>
					</div>
					<template:errorSpanField path="cardNumber">
					</template:errorSpanField>
				<div class="expmargin">
					<label for="ExpiryMonth"><spring:theme
							code="payment.expiryDate" /></label>&nbsp;&nbsp;&nbsp;
				</div>
				<div class="expmargin_text">

					<form:select id="ExpiryMonth" path="expiryMonth"
						cssClass="card_date" tabindex="4">
						<option value=""><spring:theme code="payment.month" /></option>  
						<form:options items="${months}" itemValue="code" itemLabel="name" />
					</form:select>
					&nbsp;
					<form:select id="ExpiryYear" path="expiryYear"
						cssClass="card_date cardmargin" tabindex="5">
						<option value=""><spring:theme code="payment.year" /></option>  
						<form:options items="${expiryYears}" itemValue="code"
							itemLabel="name" />
					</form:select>
					<template:errorSpanField path="expiryMonth">
					</template:errorSpanField>
					<template:errorSpanField path="expiryYear">
					</template:errorSpanField>
				</div>
				<br/>
				<div>
				<spring:theme code="payment.cvvNo"/>
				</div>
				<div>			
				<form:input id="cvvNo" path="securityCode" cssClass="text cvvnomargin cvvText"
					tabindex="6" mandatory="true" onkeypress="return validate(event)" maxlength="4"/>
					</div>
				<template:errorSpanField path="securityCode">
					</template:errorSpanField><div class="clearboth"></div>	
			</div>
			<div id="cardsListForm" style="${fn:length(paymentInfos) > 0 ? "display:block;" : "display:none;"}">
				<h3><spring:theme code="text.select.card"/></h3>
				<br />
				<c:forEach items="${paymentInfos}" var="cardInfo">
					<tr>
						<c:choose>
							<c:when	test="${not empty paymentId and paymentId eq cardInfo.id}">
								<td headers="header3"><input type="radio" id="cardRadio"
									name="cardRadio" class="form use_this_saved_card_button"
									checked="checked" payment_id='${cardInfo.id}'
									value="${cardInfo.id}"
									onclick="updatePaymentMethod('${cardInfo.id}');">&nbsp; ${fn:toUpperCase(cardInfo.cardType)}
									&nbsp;${cardInfo.cardNumber}</td>	
							</c:when>
							<c:otherwise>
								<td headers="header3"><input type="radio" id="cardRadio"
									name="cardRadio" class="form use_this_saved_card_button"
									payment_id='${cardInfo.id}' value="${cardInfo.id}"
									onclick="updatePaymentMethod('${cardInfo.id}');">&nbsp; ${fn:toUpperCase(cardInfo.cardType)}
									&nbsp;${cardInfo.cardNumber}</td>
							</c:otherwise>
						</c:choose>
						<td headers="header1"><spring:theme
								code="checkout.summary.paymentMethod.paymentDetails.expires"
								arguments="${cardInfo.expiryMonth},${cardInfo.expiryYear}" />
							&nbsp;<a id="removeLink"
							onclick="removePaymentMethod('${cardInfo.id}')"
							style="color: red;cursor:pointer;"><spring:theme code="basket.page.remove"/></a><br></td>
					</tr>
					<br />
				</c:forEach>
				<span><input id="addcardbutton" type="button"
					value="Add a Card">
				</span> <br /> <br />
				<div id="useCard">
					<button class="saved_card_button">
						<spring:theme
							code="checkout.summary.paymentMethod.addPaymentDetails.useSavedCard" />
					</button>
				</div>
			</div>
			
			<div class="save_payment_details"
				style="${fn:length(paymentInfos) == 0 ? "
				display:block;" : "display:none;"}" id="lastButton">
				<sec:authorize ifNotGranted="ROLE_GUESTCUSTOMERGROUP">
					<dl>
						<dt class="left">
							<form:checkbox id="SaveDetails" path="saveInAccount"
								tabindex="7" />
								
							&nbsp;<label for="SaveDetails"><spring:theme
									code="checkout.summary.paymentMethod.addPaymentDetails.savePaymentDetailsInAccount" /></label>
						</dt>
						<dd></dd>
					</dl>
				</sec:authorize>
				<ycommerce:testId code="checkout_useThesePaymentDetails_button">
					<button class="form left use_these_payment_details_button"
						tabindex="8" id="lastInTheForm"
						style="${fn:length(paymentInfos) == 0 ? "display:block;" : "display:none;"}">
						<spring:theme
							code="checkout.summary.paymentMethod.addPaymentDetails.useThesePaymentDetails" />
					</button>
				</ycommerce:testId>		
				
				
			</div>

		</div>

		<div class="payment_details_right_col">

			<h3>
				<spring:theme
					code="checkout.summary.paymentMethod.addPaymentDetails.billingAddress" />
			</h3>
			<c:set var="isstorepickup" value = "${storepickup}"></c:set>
			<p style="${isstorepickup ? "display:none;" : "display:blocks;"}">
				<spring:theme
					code="checkout.summary.paymentMethod.addPaymentDetails.billingAddressDiffersFromDeliveryAddress" />
			</p>
			<dl>
				<dt class="left" style="${storepickup ? "display:none;" : "display:blocks;"}">
					<form:checkbox id="differentAddress" path="newBillingAddress"
						tabindex="9" />
					&nbsp;<label for="differentAddress"><spring:theme
							code="checkout.summary.paymentMethod.addPaymentDetails.enterDifferentBillingAddress" /></label>
				</dt>
				<dd></dd>
			</dl>

			<div class="required_field">
				<spring:theme code="form.required" />
			</div>
			
			<dl id="newBillingAddressFields">
				<form:hidden path="billingAddress.addressId"
					class="create_update_address_id"
					status="${not empty createUpdateStatus ? createUpdateStatus : ''}" />
				<formElement:formInputBox idKey="email" labelKey="address.email"
					path="billingAddress.email" inputCSS="text textmargin"
					mandatory="true" tabindex="11" />
						<span class="error-msg" id="error-msg-email" style="display: none"><spring:message
							code="register.email.invalid" /></span>
				<br />
				<formElement:formInputBox idKey="firstName"
					labelKey="address.firstName" path="billingAddress.firstName"
					inputCSS="text textmargin" maxlength="15" mandatory="true" tabindex="12" />
					<span class="error-msg" id="error-msg-firstName"
						style="display: none"><spring:message
							code="address.firstName.invalid" /></span>
				<br />
				
				<formElement:formInputBox idKey="surname" labelKey="address.surname"
					path="billingAddress.lastName" inputCSS="text textmargin" maxlength="15"
					mandatory="true" tabindex="13" />
					<span class="error-msg" id="error-msg-surname"
						style="display: none"><spring:message
							code="address.lastName.invalid" /></span>
				<br />
				<formElement:formInputBox idKey="line1" labelKey="address.line1"
					path="billingAddress.line1" inputCSS="text textmargin" maxlength="40"
					mandatory="true" tabindex="14" />
					<span class="error-msg" id="error-msg-line1" style="display: none"><spring:message
							code="address.line1.invalid" /></span>
				<br />
				<formElement:formInputBox idKey="line2" labelKey="address.line2"
					path="billingAddress.line2" inputCSS="text textmargin" maxlength="40"
					mandatory="false" tabindex="15" />
				<br />
				<formElement:formInputBox idKey="townCity"
					labelKey="address.townCity" path="billingAddress.townCity"
					inputCSS="text textmargin" mandatory="true" tabindex="16" />
					<span class="error-msg" id="error-msg-townCity"
						style="display: none"><spring:message
							code="address.townCity.invalid" /></span>
				<br />
				<label id="statelabel" for="enter-states"><spring:theme code="address.state"/></label>
				<dd class="dropdownWidth">
					<form:select idKey="address.state" id="enter-states"
						cssClass="statedropdown" tabindex="17"
						path="billingAddress.stateIso" mandatory="true" skipBlank="false"
						skipBlankMessageKey="State">
						<form:options items="${state}"
							itemValue="${'billingAddress.stateIso'}" itemLabel="${'name'}" />
					</form:select>
				</dd>
				</br>

				<formElement:formInputBox idKey="postcode"
					labelKey="address.postcode" path="billingAddress.postcode"
					inputCSS="text textmargin"  tabindex="18" />
					<span class="error-msg" id="error-msg-postcode"
						style="display: none"><spring:message
							code="address.postcode.invalid" /></span>
				<br />

				<c:set var="shippingCountry"
					value="${paymentDetailsForm.billingAddress.countryIso}"></c:set>
				<label id="Country" for="enter-states">Country</label>
				<dd class="dropdownWidth">
					<form:select idKey="Country" labelKey="Country"
						id="select-countries" cssClass="statedropdown"
						path="billingAddress.countryIso" mandatory="true"
						skipBlank="false" skipBlankMessageKey="address.selectCountry"
						tabindex="19" class="shipping-address-fields">
						<c:forEach items="${countries}" var="eachCountry">
							<option
								${shippingCountry eq
								eachCountry.isocode ? 'selected' : ''}
								value="${eachCountry.isocode}"
								stateList="${countriesStateMap[eachCountry.isocode]}">${eachCountry.name}</option>
						</c:forEach>

					</form:select>
				</dd>
				</br>
				<div>
						<spring:theme code="address.phoneNo" />
				</div>
				<form:input id="phoneNo" path="billingAddress.phoneNo"
					cssClass="text textmargin" maxlength="13" tabindex="20" onkeypress="return validate(event)"/><br/>
					<span class="error-msg" id="error-msg-phoneNo"
						style="display: none"><spring:message
							code="address.phoneNo.invalid" /></span>
					</br>
					
				
				
				<form:hidden path="billingAddress.shippingAddress" />
				<form:hidden path="billingAddress.billingAddress" />
			</dl>
		</div>

	</form:form>

</div>
<script type="text/javascript">
$(document).ready(function() {
	
	function PaymentFormValidation(event)
	{
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
		if (email != "" && !email1) {
			$("#error-msg-email").show();
			event.preventDefault();
			allfilled = false;
		}

		if (!$("#firstName").val()) {
			$("#error-msg-firstName")
					.show();
			allfilled = false;
			event.preventDefault();
		}
		if (!$("#surname").val()) {
			$("#error-msg-surname").show();
			event.preventDefault();
			allfilled = false;
		}
		if (!$("#line1").val()) {
			$("#error-msg-line1").show();
			event.preventDefault();
			allfilled = false;
		}
		if (!$("#townCity").val()) {
			$("#error-msg-townCity").show();
			event.preventDefault();
			allfilled = false;
		}
		
		if (!$("#phoneNo").val()) {
			$("#error-msg-phoneNo").show();
			event.preventDefault();
			allfilled = false;
		}
		
		var phoneNO = $("#phoneNo").val();
		 var phoneRegex = new RegExp(/^[0-9]+$/);
		  var phoneNo1=phoneRegex.test(phoneNO);
		  if(!phoneNo1){
			  $("#error-msg-phoneNo").show();
			  event.preventDefault();
				allfilled = false;
		  }
			if (phoneNO.length<10) {
				$("#error-msg-phoneNo")
						.show();
				event.preventDefault();
				allfilled = false;
			}
		
		if (!$("#postcode").val()) {
			$("#error-msg-postcode").show();
			event.preventDefault();
			allfilled = false;
		}
		var postcode = $("#postcode").val();
		var postcodeRegex = new RegExp(
				/^[0-9]{1,10}$/);
		var postcode1 = postcodeRegex.test(postcode);
		if (!postcode1) {
			$("#error-msg-postcode").show();
			event.preventDefault();
			allfilled = false;
		}
		if(!allfilled){
			$("#misc-error-msg").show();
			event.preventDefault();
			allfilled = false;
		}
		return allfilled;
	}
	
	$("#lastInTheForm").click(function(event){
		var allfilled=PaymentFormValidation(event);
		if(allfilled)
			{
			//$("#newPaymentDetailsForm").submit();
			}
	})
	
	 $(".saved_card_button").click(function(event){
		var allfilled=PaymentFormValidation(event);
		if(!allfilled)
			{
			event.preventDefault();
			}
		}) 
	
	});
	
	
</script>
<script type="text/javascript">
	$(document).ready(
			function() {
				$('#select-countries').change(
						function() {
							var selectedStateList = $(
									'#select-countries option:selected').attr(
									"stateList");
							var selectedCountry = $(
									"#select-countries option:selected").val();

							if (selectedCountry == 'US') {

								/* $('#zipcode').append('Zip Code*'); */
								$("#enter-states").empty();
								$("#enter-states-input").empty();
								if (selectedStateList != "") {
									var ar = selectedStateList.split('#')
									for ( var i = 0; i < ar.length; i++) {
										var tokens = ar[i].split('|');
										var value = tokens[0];
										var label = tokens[1];
										$("#enter-states").append(
												"<option value='"+value+"'>"
														+ label + "</option>");
										$('#div-inputstate').hide();
										$('#div-selectstate').show();
									}

								} else {
									$('#div-inputstate').show();
									$('#div-selectstate').hide();
								}
							} else
							/* if{
								$('#statelabel').append('State'); */
							if (selectedCountry == 'CA') {
								/* $('#statelabel').append('State'); */
								$("#enter-states").empty();
								/* 	$('#zipcode').append(zipCodeLabel); */
								$("#enter-states-input").empty();
								if (selectedStateList != "") {
									var ar = selectedStateList.split('#')
									for ( var i = 0; i < ar.length; i++) {
										var tokens = ar[i].split('|');
										var value = tokens[0];
										var label = tokens[1];
										$("#enter-states").append(
												"<option value='"+value+"'>"
														+ label + "</option>");

										/* $('#div-inputstate').hide();
										$('#div-selectstate').show(); */
									}
								} else {
									$('#div-inputstate').show();
									$('#div-selectstate').hide();
								}
							} else {
								/* $('#statelabel').append('State'); */
								if (selectedCountry == 'MX') {
									$("#enter-states").empty();
									/* 	$('#zipcode').append(zipCodeLabel); */
									$("#enter-states-input").empty();
									if (selectedStateList != "") {
										var ar = selectedStateList.split('#')
										for ( var i = 0; i < ar.length; i++) {
											var tokens = ar[i].split('|');
											var value = tokens[0];
											var label = tokens[1];
											$("#enter-states").append(
													"<option value='"+value+"'>"
															+ label
															+ "</option>");
											$('#div-inputstate').hide();
											$('#div-selectstate').show();
										}
									} else {
										$('#div-inputstate').show();
										$('#div-selectstate').hide();
									}
								}
							}

						});
				$('#select-countries').trigger('change');
				$('#enter-states').val(
						'${paymentDetailsForm.billingAddress.stateIso}');
			});

</script>

