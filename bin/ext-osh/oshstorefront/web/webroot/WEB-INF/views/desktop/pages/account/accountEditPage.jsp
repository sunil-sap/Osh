<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>

<script type="text/javascript">
	$(document)
			.ready(
					function() { 
						$('#select-country1')
								.change(
										function() {
											var selectedStateList = $(
													'#select-country1 option:selected')
													.attr("stateList");
											var selectedCountry = $(
													"#select-country1 option:selected")
													.val();
											$("#zipcode").empty();
											if (selectedCountry == 'US') {

												$('#zipcode').append(
														'Zip Code*');
												$("#enter-state1").empty();
												$("#enter-state-input").empty();
												if (selectedStateList != "") {
													var ar = selectedStateList
															.split('#')
													for ( var i = 0; i < ar.length; i++) {
														var tokens = ar[i]
																.split('|');
														var value = tokens[0];
														var label = tokens[1];
														$("#enter-state1")
																.append(
																		"<option value='"+value+"'>"
																				+ label
																				+ "</option>");
														$('#div-inputstate')
																.hide();
														$('#div-selectstate')
																.show();
													}
												} else {
													$('#div-inputstate').show();
													$('#div-selectstate')
															.hide();
												}
											} else
											
											if (selectedCountry == 'CA') {
											$("#enter-state1").empty();
												$("#enter-state-input").empty();
												if (selectedStateList != "") {
													var ar = selectedStateList
															.split('#')
													for ( var i = 0; i < ar.length; i++) {
														var tokens = ar[i]
																.split('|');
														var value = tokens[0];
														var label = tokens[1];
														$("#enter-state1")
																.append(
																		"<option value='"+value+"'>"
																				+ label
																				+ "</option>");
														$('#div-inputstate')
																.hide();
														$('#div-selectstate')
																.show();
													}
												} else {
													$('#div-inputstate').show();
													$('#div-selectstate')
															.hide();
												}
											} else {
												
												if (selectedCountry == 'MX') {
													$("#enter-state1").empty();
												    $("#enter-state-input")
															.empty();
													if (selectedStateList != "") {
														var ar = selectedStateList
																.split('#')
														for ( var i = 0; i < ar.length; i++) {
															var tokens = ar[i]
																	.split('|');
															var value = tokens[0];
															var label = tokens[1];
															$("#enter-state1")
																	.append(
																			"<option value='"+value+"'>"
																					+ label
																					+ "</option>");
															$('#div-inputstate')
																	.hide();
															$(
																	'#div-selectstate')
																	.show();
														}
													} else {
														$('#div-inputstate')
																.show();
														$('#div-selectstate')
																.hide();
													}
												}
											}

										});
						$('#select-country1').trigger('change');
						$('#enter-state1').val('${addressForm.stateIso}');
					});
	
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
<script type="text/javascript">
$(document).ready(function() {
	
	function getZipcode(event)
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
			event.preventDefault();
			allfilled = false;
		}
		
		if (!$("#phoneNo").val()) {
			$("#error-msg-phoneNo")
					.show();
			event.preventDefault();
			allfilled = false;
		}
		var phoneNO = $("#phoneNo").val();
		if (phoneNO.length<10) {
			$("#error-msg-phoneNo")
					.show();
			event.preventDefault();
			allfilled = false;
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
		if (!$("#postcode").val()) {
			$("#error-msg-postcode").show();
			event.preventDefault();
			allfilled = false;
		}
		var postcode = $("#postcode").val();
		var postcodeRegex = new RegExp(
				/^[0-9]+$/);
		var postcode1 = postcodeRegex.test(postcode);
		if (!postcode1) {
			$("#error-msg-postcode").show();
			event.preventDefault();
			allfilled = false;
		}
		return allfilled;
	}
	
	$("#saveAddressdetails").click(function(event){
		var allfilled=getZipcode(event);
		if(allfilled)
			{
			//$("#account_create_update_payment_form").submit();
			var url = $("#accountEditAddaddress").attr("action");
			var form = $("#accountEditAddaddress");
		   $.ajax({
			   url:url,
				type :'POST',
				data : form.serialize(),
			  success:function(data)
			  {
				  if( data=="")
					  {
					  $.colorbox.close();
					  location.reload();
					  //window.location.href = '';
					  }
				  $('#edit_delivery_address').html(data);
			  },
			  error:function(xht, textStatus, ex)
			  {
				  //alert('error');
			  }
			  });
			}
	})
	
	});
</script>
<div class="span-20 last">
	<div class="addpopup_item_container_holder">
		<!-- <div class="container"> -->
		<div class="title_holder">
			<div class="title">
				<div class="title-top">
					<span></span>
				</div>
			</div>
			<h2>
				<spring:theme code="text.account.addressBook.addressDetails"
					text="Address Details" />
			</h2>
		</div>
		<div class="item_container">
			<p>
				<spring:theme code="text.account.addressBook.addEditform"
					text="Please use this form to add/edit an address." />
			</p>
			<p class="required_field">
				<spring:theme code="form.required"
					text="Fields marked * are required" />
			</p>

			<form:form action="edit-address/8796094201879" method="Post"
				commandName="addressForm" id="accountEditAddaddress">
				<dl>
					<form:hidden path="addressId" />
					<%-- <formElement:formSelectBox idKey="address.title" labelKey="address.title" path="titleCode" mandatory="true" skipBlank="false" items="${titleData}" selectedValue="${addressForm.titleCode}" selectCSSClass="addpop_text"/> --%>
					<formElement:formInputBox idKey="email" labelKey="Email Address"
						path="email" inputCSS="first name required textfield textmargin"
						mandatory="true" />
					<span class="error-msg" id="error-msg-email" style="display: none"><spring:message
							code="register.email.invalid" /></span>
					<formElement:formInputBox idKey="firstName"
						labelKey="address.firstName" path="firstName"
						inputCSS="first name required textfield textmargin" maxlength="15"
						mandatory="true" />
					<span class="error-msg" id="error-msg-firstName"
						style="display: none"><spring:message
							code="address.firstName.invalid" /></span>
					<formElement:formInputBox idKey="surname"
						labelKey="address.surname" path="lastName"
						inputCSS="text addresspop_text textfield textmargin"
						maxlength="15" mandatory="true" />
					<span class="error-msg" id="error-msg-surname"
						style="display: none"><spring:message
							code="address.lastName.invalid" /></span>
					<formElement:formInputBox idKey="line1" labelKey="address.line1"
						path="line1" inputCSS="text addresspop_text textfield textmargin"
						maxlength="40" mandatory="true" />
					<span class="error-msg" id="error-msg-line1" style="display: none"><spring:message
							code="address.line1.invalid" /></span>
					<formElement:formInputBox idKey="line2" labelKey="address.line2"
						path="line2" inputCSS="text addresspop_text textfield textmargin"
						maxlength="40" mandatory="false" />
					<formElement:formInputBox idKey="townCity"
						labelKey="address.townCity" path="townCity"
						inputCSS="text addresspop_text textfield textmargin"
						maxlength="40" mandatory="true" />
					<span class="error-msg" id="error-msg-townCity"
						style="display: none"><spring:message
							code="address.townCity.invalid" /></span>
					<div>
						<spring:theme code="address.postcode" />
					</div>
					<form:input id="postcode" path="postcode"
						cssClass="text textfield textmargin" mandatory="true"
						maxlength="10" onkeypress="return validate(event)" />
					<br>
					<c:if test="${zipCodeErrorMsg}">
						<span class="error-msg"><spring:message
								code="address.postcode.invalid" /></span>
					</c:if>
					<span class="error-msg" id="error-msg-postcode"
						style="display: none"> <spring:message
							code="address.postcode.invalid" />
					</span>
					


					<c:set var="shippingCountry" value="${addressForm.countryIso}"></c:set>
					<label id="Country" for="enter-state1"><spring:theme
							code="address.country" /></label>

					<dd>
						<form:select idKey="Country" labelKey="Country"
							id="select-country1"
							cssClass="text textfield textmargin_editAddress"
							path="countryIso" mandatory="true" skipBlank="false"
							skipBlankMessageKey="address.selectCountry">
							<c:forEach items="${countries}" var="eachCountry">
								<option
									${shippingCountry eq eachCountry.isocode ? 'selected' : ''}
									value="${eachCountry.isocode}"
									stateList="${countriesStateMap[eachCountry.isocode]}">${eachCountry.name}</option>
							</c:forEach>

						</form:select>
					</dd>


					<label id="statelabel" for="enter-state1"><spring:theme
							code="address.state" /></label>
					<dd>
						<form:select idKey="address.state" id="enter-state1"
							cssClass="text textfield textmargin_editAddress text addresspop_text"
							path="stateIso" mandatory="true" skipBlank="false"
							skipBlankMessageKey="State">
							<form:options items="${states}" itemValue="${'isocode'}"
								itemLabel="${'name'}" />
						</form:select>
					</dd>
					<div>
						<spring:theme code="address.phoneNo" />
					</div>
					<form:input id="phoneNo" path="phoneNo"
						cssClass="first name required textfield textmargin" maxlength="13"
						mandatory="true" onkeypress="return validate(event)" />
					</br>
					<span class="error-msg" id="error-msg-phoneNo"
						style="display: none"><spring:message
							code="address.phoneNo.invalid" /></span>
					<div id="defaultAddressCheckbox" style="display: none;">
						<c:if test="${addressForm.defaultAddress}">
							<formElement:formCheckbox idKey="address.default"
								labelKey="address.default" path="defaultAddress"
								inputCSS="add-address-left-input accountaddpopup_checbox"
								labelCSS="add-address-left-label" mandatory="false" />
						</c:if>
					</div>

					<c:if test="${not addressForm.defaultAddress}">
						<br>
						<br />
						<formElement:formCheckbox idKey="address.default"
							labelKey="address.default" path="defaultAddress"
							inputCSS="add-address-left-input accountaddpopup_checbox"
							labelCSS="add-address-left-label" mandatory="false" />
					</c:if>
				</dl>

			</form:form>
			<span style="display: block; clear: both;"> <ycommerce:testId
					code="accountEditAddress_SaveAddress_button">
					<button class="form" id="saveAddressdetails">
						<spring:theme code="text.account.addressBook.saveAddress"
							text="Save address" />
					</button>
				</ycommerce:testId>
			</span>
		</div>
	</div>
</div>
<!-- </div> -->
<!-- /template:page -->

