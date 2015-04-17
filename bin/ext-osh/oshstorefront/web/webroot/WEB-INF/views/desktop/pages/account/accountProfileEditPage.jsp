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
<%@ taglib prefix="store" tagdir="/WEB-INF/tags/desktop/store"%>
<template:page pageTitle="${pageTitle}">
	<div class="middleContent">
		<div class="innermiddleContent">
			<div class="acc_breadcrumb">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			</div>
			<div class="acc_banner">
				<cms:slot var="feature" contentSlot="${slots.GlobalSection}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>
			<div id="globalMessages">
				<common:globalMessages />
			</div>
			<div class="acc_middlewrap">
				<div class="leftmaincontent">
					<nav:accountNav />
				</div>
				<div class="span-20 last multicheckout editprofile">
					<div class="item_container_holder1">
						<div class="title_holder">
							<div class="title">
								<div class="title-top">
									<span></span>
								</div>
							</div>
							<h2>
								<spring:theme code="text.account.profile.updatePersonalDetails"
									text="Profile" />
							</h2>
						</div>
						<div class="item_container">
							<p>
								<spring:theme code="text.account.profile.updateForm"
									text="Please use this form to update your personal details" />
							</p>
							<p class="required" style="color: red;">
								<spring:theme code="form.required"
									text="Fields marked * are required" />
							</p>
							<form:form action="update-profile" method="post"
								id="updateProfile" commandName="updateProfileForm">

								<div class="editcolumeleft">
									<%-- 									<formElement:formSelectBox idKey="profile.title"
										labelKey="profile.title" path="titleCode" mandatory="false"
										selectCSSClass="editprofile_text" skipBlank="false"
										skipBlankMessageKey="form.select.empty" items="${titleData}" /> 
									<br>--%>
									<formElement:formInputBox idKey="firstName"
										labelKey="profile.firstName" path="firstName"
										inputCSS="text editprofile_text" maxlength="15"
										mandatory="true" />
									<span class="error-msg" id="error-msg-firstName"
										style="display: none"><spring:message
											code="address.firstName.invalid" /></span> <br>
									<formElement:formInputBox idKey="lastName"
										labelKey="profile.lastName" path="lastName"
										inputCSS="text editprofile_text" maxlength="15"
										mandatory="true" />
									<span class="error-msg" id="error-msg-surname"
										style="display: none"><spring:message
											code="address.lastName.invalid" /></span> <br>
									<%-- <formElement:formInputBox idKey="email"
										labelKey="profile.email" path="addressForm.email"
										inputCSS="text editprofile_text" mandatory="false" /> --%>
									<%-- <span class="error-msg" id="error-msg-email" style="display: none"><spring:message code="register.email.invalid" /></span> --%>
									<spring:theme code="profile.email" />
									<div class="text editprofile_text">
										<form:input type="text" id="email" path="addressForm.email"
											disabled="true" />
									</div>
									<br>
									<formElement:formInputBox idKey="phoneNo"
										labelKey="profile.phoneno" path="phone"
										inputCSS="text editprofile_text" mandatory="true" maxlength="13" />
										<span class="error-msg" id="error-msg-phoneNo"
						style="display: none"><spring:message
							code="address.phoneNo.invalid" /></span>
									<br> <span><spring:theme
											code="text.account.profile.birthDate" /></span> <br>
									<form:select id="birthmonth" path="month"
										cssClass="editprofile_ddMM_text">
										<option value="" />
										<form:options items="${months}" itemValue="code"
											itemLabel="name" />
									</form:select>
									&nbsp;
									<form:select id="birthday" path="day"
										cssClass="editprofile_ddMM_text">
										<option value="" />
										<form:options items="${days}" itemValue="code"
											itemLabel="name" />
									</form:select>
									<br> <br>
									<formElement:formInputBox idKey="loyaltyNumber"
										labelKey="profile.loyaltyNumber" path="loyaltyNumber"
										inputCSS="text editprofile_text" mandatory="false" />
									<br>
								</div>
								<div class="editcolumeright">
									<formElement:formInputBox idKey="line1"
										labelKey="profile.line1" path="addressForm.line1"
										inputCSS="text editprofile_text" maxlength="40"
										mandatory="true" />
									<span class="error-msg" id="error-msg-line1"
										style="display: none"><spring:message
											code="address.line1.invalid" /></span> <br>
									<formElement:formInputBox idKey="profile.line2"
										labelKey="profile.line2" path="addressForm.line2"
										inputCSS="text editprofile_text" maxlength="40"
										mandatory="false" />
									<br>
									<formElement:formInputBox idKey="townCity"
										labelKey="profile.townCity" path="addressForm.TownCity"
										inputCSS="text editprofile_text" mandatory="true" />
									<span class="error-msg" id="error-msg-townCity"
										style="display: none"><spring:message
											code="address.townCity.invalid" /></span> <br>
									
									<spring:theme code="profile.postcode"/><br>
									
									<form:input id="postcode" 
										labelKey="profile.postcode" path="addressForm.Postcode"
										cssClass="text editprofile_text" onkeypress="return validate(event)" /><br>
									<span id="error-invalid-zipcode">
									<template:errorSpanField path="addressForm.Postcode">
									</template:errorSpanField></span>
									<span class="error-msg" id="error-msg-postcode-error"
										style="display: none"> <spring:message
											code="address.postcode.invalid" /></span>
									<span class="error-msg" id="error-msg-postcode"
										style="display: none"><br></span>
									<label
										id="Country" for="enter-state"><spring:theme
											code="address.country" /></label>
									<dd>
										<c:set var="shippingCountry"
											value="${updateProfileForm.addressForm.countryIso}"></c:set>
										<form:select idKey="Country" labelKey="Country"
											id="select-country" cssClass="text editprofile_statetext"
											path="addressForm.countryIso" mandatory="true"
											skipBlank="false" skipBlankMessageKey="address.selectCountry">
											<c:forEach items="${countries}" var="eachCountry">
												<option
													${shippingCountry eq eachCountry.isocode ? 'selected' : ''}
													value="${eachCountry.isocode}"
													stateList="${countriesStateMap[eachCountry.isocode]}">${eachCountry.name}</option>
											</c:forEach>

										</form:select>
									</dd>
									<br>
									<c:set var="shippingState"
										value="${updateProfileForm.addressForm.stateIso}"></c:set>
									<label id="statelabel" for="enter-state"><spring:theme
											code="address.state" /> </label>
									<dd>

										<form:select idKey="address.state" id="enter-state"
											cssClass="text editprofile_statetext"
											path="addressForm.stateIso" mandatory="true"
											skipBlank="false" skipBlankMessageKey="State">
											<form:options items="${states}"
												itemValue="${'isocode'}" itemLabel="${'name'}" />
										</form:select>
									</dd>
									<br /> <br>
								</div>
								<div class="clearboth"></div>
								<div>
									<div class="updateprofile">
										<form:checkbox path="storenewletter" cssClass="updateprofile" />
										&nbsp;
										<spring:theme code="text.account.signup" />
									</div>
									<%-- <div>
										<form:checkbox path="specialoffer"/>
										<spring:theme code="text.account.promotion"/>
									</div>
									<div>
										<form:checkbox path="cluborchardinfo"/>
										<spring:theme code="text.account.information"/>
									</div>	 --%>
									<%-- <formElement:formCheckbox idKey="address.default"
											labelKey="text.account.signup" path="storenewletter"
											mandatory="false" inputCSS="editprofile_checbox" />>
										<formElement:formCheckbox idKey="address.default"
											labelKey="text.account.promotion" path="specialoffer"
											mandatory="false" inputCSS="editprofile_checbox" />
										<formElement:formCheckbox idKey="address.default"
											labelKey="text.account.information" path="cluborchardinfo"
											mandatory="false" inputCSS="editprofile_checbox" /> --%>

									<span style="display: block; clear: both; float: right;">
										<%-- <ycommerce:testId code="profilePage_SaveUpdatesButton">
											<button class="form" id="saveProfiledetails">
												<spring:theme code="text.account.profile.saveUpdates"
													text="Save Updates" />
											</button>
										</ycommerce:testId> --%> <input type="button"
										class="form_save" style="cursor: pointer;"
										value="<spring:theme code="text.account.profile.saveUpdates"/>"
										id="saveProfiledetails" onclick="getInvalidZipcode();"/>

									</span>
								</div>
								<hr>
								<div class="mystore">
									<div class="img">
										<img
											src="${request.contextPath}/_ui/desktop/osh/images/store_locator/store-front.jpg"
											alt="${textAccountProfile}" title="${textAccountProfile}" />
										<%-- <store:storeImage store="${storeData}" format="store" /> --%>
									</div>
									<div class="content_store_editprofile">
										<span><b>My Store: </b></span><br> <span><b>${fn:escapeXml(storeData.name)}
										</b></span><br> <span>${fn:escapeXml(storeData.address.line1)}&nbsp;${fn:escapeXml(storeData.address.line2)}</span><br>
										<span>${fn:escapeXml(storeData.address.town)},
											${fn:escapeXml(storeData.address.country.name)}
											,${fn:escapeXml(storeData.address.postalCode)}</span><br> <span>${fn:escapeXml(storeData.address.phone)}</span>

									</div>
									<div class="changestore_link">
										<a href="${request.contextPath}/store-finder"
											style="color: blue"><spring:message
												code="osh.product.page.product.changestore" /></a>
									</div>

								</div>
								<c:if test="${not empty customerData.loyaltyNumber}">
									<hr>
									<div class="last_upper">
										<div class="last_logo" style="margin-top: 13px;">
											<img
												src="${request.contextPath}/_ui/desktop/osh/images/cartpage/u40_normal.png"
												alt="${textAccountProfile}" title="${textAccountProfile}" />
										</div>
										<div class="lastupper_middle" style="margin-top: 13px;">
											<span><b>${customerData.name}</b></span><br> <span>${customerData.loyaltyNumber}</span><br>
										</div>
									</div>
									<br clear="all">
									<div class="last_down">
										<p>
											<span class="pointText"><spring:theme
													code="text.account.point" />&nbsp;&nbsp; </span> <span
												class="pointNumber">${customerPoints}</span> <span
												class="point_doller_text">250 <spring:theme
													code="text.account.points" /> = $5
											</span> <br> <br>
											<span class="pointText"><spring:theme
													code="text.account.you" /> </span> <span class="pointNumber1">${remainingPoints}</span>
											<span class="pointText"> <spring:theme
													code="text.account.certificate" /></span>
										</p>
									</div>
								</c:if>
								<hr>
								<c:if test="${empty customerData.loyaltyNumber}">
									<div class="last_upper">
										<div class="last_logo" style="margin-top: 13px;">
											<img
												src="${request.contextPath}/_ui/desktop/osh/images/cartpage//u40_normal.png"
												alt="${textAccountProfile}" title="${textAccountProfile}" />
										</div>
										<div class="lastupper_right">
											<a href="http://cluborchard.osh.com" target="_blank"
												style="color: blue"><spring:theme
													code="text.account.member" /></a>
										</div>
										<div class="lastupper_middle" style="margin-top: 13px;">
											<spring:theme code="text.account.loyalty.advice.msg" />
										</div>
									</div>
								</c:if>

							</form:form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template:page>
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
				$('#enter-state').val(
						'${updateProfileForm.addressForm.stateIso}');
				
				
			});
	
	function getInvalidZipcode()
	{	
		var allfilled = true;
		if (!$("#postcode").val()) {
			$("#error-msg-postcode-error").show();
			$("#error-invalid-zipcode").hide();
			
			allfilled = false;
		}
		
	} 
	//Function to allow only numbers to textbox
	function validate(key)
	{
	//getting key code of pressed key
	var keycode = (key.which) ? key.which : key.keyCode;
	//var phn = document.getElementById('postcode');
	//comparing pressed keycodes
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

