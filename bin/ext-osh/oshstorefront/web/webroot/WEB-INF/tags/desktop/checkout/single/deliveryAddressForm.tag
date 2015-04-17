<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<spring:url
	value="/checkout/single/summary/createUpdateDeliveryAddress.json"
	var="createUpdateDeliveryAddressUrl" />
<div class="delevery_address_form">
	<span> <spring:theme code="checkout.single.shipping" />
	</span>
</div>
<!-- <div class="borderbox1 shipping_add"> -->
<p>
	<spring:theme code="checkout.summary.deliveryAddress.useForNewAddress" />
</p>

<p>
	<spring:theme code="address.required" />
</p>
<form:form method="post" commandName="addressForm"
	action="${createUpdateDeliveryAddressUrl}"
	class="create_update_address_form">
	<dl>
		<form:hidden path="addressId" class="create_update_address_id"
			id="create_update_address_form_id"
			status="${not empty createUpdateStatus ? createUpdateStatus : ''}" />

		<%-- <formElement:formSelectBox idKey="address.title" labelKey="address.title" selectCSSClass="textmargin" path="titleCode" mandatory="true" skipBlank="false" skipBlankMessageKey="address.title.pleaseSelect" items="${titles}"/>
			<br/> --%>
		<formElement:formInputBox idKey="Email" labelKey="Email Address"
			path="email" inputCSS="email required textfield textmargin"
			mandatory="true" />
		<br />
		<formElement:formInputBox idKey="address.firstName"
			labelKey="address.firstName" path="firstName" maxlength="15"
			inputCSS="first name required textfield textmargin" mandatory="true" />
		<br />
		<formElement:formInputBox idKey="address.surname"
			labelKey="address.surname" path="lastName" maxlength="15"
			inputCSS="last name required textfield textmargin" mandatory="true" />
		<br />
		<formElement:formInputBox idKey="address.line1"
			labelKey="address.line1" path="line1" maxlength="40"
			inputCSS="Address line1 required textfield textmargin"
			mandatory="true" />
		<br />
		<formElement:formInputBox idKey="address.line2"
			labelKey="address.line2" path="line2" maxlength="40"
			inputCSS="Address line2 textfield textmargin" mandatory="false" />
		<br />
		<formElement:formInputBox idKey="address.townCity"
			labelKey="address.townCity" path="townCity"
			inputCSS="town/city required textfield textmargin" mandatory="true" />
		<br />
		<label id="statelabel" for="enter-state">State</label>
		<dd class="dropdownWidth">
			<form:select idKey="address.state" id="enter-state"
				cssClass="statedropdown" path="stateIso" mandatory="true"
				skipBlank="false" skipBlankMessageKey="State">
				<form:options items="${state}" itemValue="${'isocode'}"
					itemLabel="${'name'}" />
			</form:select>
		</dd>
		<br />
		<div>
			<spring:theme code="address.postcode" />
		</div>
		<form:input id="address.postcode" path="postcode"
			cssClass="zipcode number required textfield textmargin"
			mandatory="true" onkeypress="return validate(event)" maxlength="10" />
		<br>
		<%-- <formElement:formInputBox idKey="address.postcode"
			labelKey="address.postcode" path="postcode"
			inputCSS="zipcode number required textfield textmargin"
			mandatory="true" /> --%>
		<c:if test="${zipCodeErrorMsg}">
			<span class="error-msg"><spring:message
					code="address.postcode.invalid" /></span>
		</c:if>
		<br />
		<c:set var="shippingCountry" value="${addressForm.countryIso}"></c:set>
		<label id="Country" for="enter-state"><spring:theme
				code="address.country" /></label>
		<dd class="dropdownWidth">
			<form:select idKey="Country" labelKey="Country" id="select-country"
				cssClass="statedropdown" path="countryIso" mandatory="true"
				skipBlank="false" skipBlankMessageKey="address.selectCountry"
				class="shipping-address-fields">
				<c:forEach items="${countries}" var="eachCountry">
					<option ${shippingCountry eq eachCountry.isocode ? 'selected' : ''}
						value="${eachCountry.isocode}"
						stateList="${countriesStateMap[eachCountry.isocode]}">${eachCountry.name}</option>
				</c:forEach>

			</form:select>
		</dd>
		</br>
		<div>
			<spring:theme code="address.phoneNo" />
		</div>
		<form:input id="address.phoneNo" path="phoneNo" maxlength="13"
			cssClass="first name required textfield textmargin" mandatory="true" onkeypress="return validate(event)"/>
			
		<form:hidden path="billingAddress" />
		<br />
		<sec:authorize ifNotGranted="ROLE_GUESTCUSTOMERGROUP">
			<%-- <c:if test="${!edit}"> --%>
			<dt class="left">
				<form:checkbox id="SaveAddress" value="true" checked="Checked"
					path="saveInAddressBook" />
				&nbsp; <label for="SaveAddress"><spring:theme
						code="checkout.summary.deliveryAddress.saveAddressInMyAddressBook" /></label>
			</dt>
			<%--  </c:if>  --%>
			<dd></dd>
			<%-- <form:checkbox id="SaveAddress" value="true" path="saveInAddressBook"  /> --%>
		</sec:authorize>

	</dl>
	<p>
		<spring:theme code="text.address.msg" />
	</p>
	<span>
		<button class="btngreen1">
			<spring:theme code="checkout.summary.deliveryAddress.useThisAddress" />
		</button>
	</span>
</form:form>
<script type="text/javascript">
	$(document).ready(
			function() {
				$('#select-country').change(
						function() {
							var selectedStateList = $(
									'#select-country option:selected').attr(
									"stateList");
							var selectedCountry = $(
									"#select-country option:selected").val();
							$("#zipcode").empty();
							if (selectedCountry == 'US') {

								$('#zipcode').append('Zip Code*');
								$("#enter-state").empty();
								$("#enter-state-input").empty();
								if (selectedStateList != "") {
									var ar = selectedStateList.split('#')
									for ( var i = 0; i < ar.length; i++) {
										var tokens = ar[i].split('|');
										var value = tokens[0];
										var label = tokens[1];
										$("#enter-state").append(
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
								$("#enter-state").empty();
								/* 	$('#zipcode').append(zipCodeLabel); */
								$("#enter-state-input").empty();
								if (selectedStateList != "") {
									var ar = selectedStateList.split('#')
									for ( var i = 0; i < ar.length; i++) {
										var tokens = ar[i].split('|');
										var value = tokens[0];
										var label = tokens[1];
										$("#enter-state").append(
												"<option value='"+value+"'>"
														+ label + "</option>");
										$('#div-inputstate').hide();
										$('#div-selectstate').show();
									}
								} else {
									$('#div-inputstate').show();
									$('#div-selectstate').hide();
								}
							} else {
								/* $('#statelabel').append('State'); */
								if (selectedCountry == 'MX') {
									$("#enter-state").empty();
									/* 	$('#zipcode').append(zipCodeLabel); */
									$("#enter-state-input").empty();
									if (selectedStateList != "") {
										var ar = selectedStateList.split('#')
										for ( var i = 0; i < ar.length; i++) {
											var tokens = ar[i].split('|');
											var value = tokens[0];
											var label = tokens[1];
											$("#enter-state").append(
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
				$('#select-country').trigger('change');
				$('#enter-state').val('${addressForm.stateIso}');
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
