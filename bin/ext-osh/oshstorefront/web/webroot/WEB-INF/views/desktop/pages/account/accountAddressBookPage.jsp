
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
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

				<!--  <div class="account_password_address multicheckout"> -->
				<div class="span-20password multicheckout">
					<div class="item_container_holder_password">
						<div class="title_holder">
							<div class="title">
								<div class="title-top">
									<span></span>
								</div>
							</div>
							<h2>
								<spring:theme code="text.account.addressBook"
									text="Address Book" />
							</h2>
						</div>
						<div class="item_container_myacc_addressBook">
							<%-- <ycommerce:testId code="addressBook_addNewAddress_button"> --%>
							<%-- <a href="javascript:addDeliveryAddress('${request.contextPath}/my-account/add-address');" id="addaddresspopup">
							<button class="positive right address_button" type="submit">
								<spring:theme code="text.account.addressBook.addAddress" text="Add new address"/>
							</button>
						</a> --%>
							<%-- </ycommerce:testId> --%>
							<a
								<%-- href="javascript:addDeliveryAddress('${request.contextPath}/my-account/add-address');" --%>
								href="javascript:void(0)"
								id="addaddresspopup"> <input type="submit"
								class="positive right address_button"
								value="<spring:theme code="text.account.addressBook.addAddress"/>" onclick="addDeliveryAddress('${request.contextPath}/my-account/add-address');"/>
							</a>
							<p>
								<spring:theme
									code="text.account.addressBook.manageYourAddresses"
									text="Manage your address book" />
							</p>
							<c:if test="${not empty addressData}">
								<div id="addressbookprofile_table">
									<table id="address_book">
										<thead>
											<tr>
												<th id="header1"><spring:theme code="text.address"
														text="Address" /></th>
												<th id="header2" style="float: right; margin-right: -66px;"><spring:theme
														code="text.updates" text="Updates" /></th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${addressData}" var="address">
												<tr>
													<td headers="header1"><ycommerce:testId
															code="addressBook_address_label">
															<ul>
																<li>${fn:escapeXml(address.title)}${fn:escapeXml(address.firstName)}&nbsp;${fn:escapeXml(address.lastName)}</li>
																<li>${fn:escapeXml(address.line1)}</li>
																<li>${fn:escapeXml(address.line2)}</li>
																<li>${fn:escapeXml(address.town)}</li>
																<li>${fn:escapeXml(address.postalCode)}</li>
																<li>${fn:escapeXml(address.country.name)}</li>
																<li>${fn:escapeXml(address.phone)}</li>
															</ul>
														</ycommerce:testId></td>
													<td headers="header2"><ycommerce:testId
															code="addressBook_addressOptions_label">
															<ul class="updates">
																<li><ycommerce:testId
																		code="addressBook_editAddress_button">
																		<a href="javascript:void(0);"
																			onclick="editDeliveryAddress('${request.contextPath}/my-account/edit-address/${address.id}');"
																			style="color: blue"><spring:theme
																				code="text.edit" text="Edit" /></a>
																	</ycommerce:testId></li>
																<li><ycommerce:testId
																		code="addressBook_removeAddress_button">
																		<a href="remove-address/${address.id}"
																			style="color: blue"><spring:theme
																				code="text.remove" text="Remove" /></a>
																	</ycommerce:testId></li>
																<c:if test="${not address.defaultAddress}">
																	<li><ycommerce:testId
																			code="addressBook_isDefault_button">
																			<a href="set-default-address/${address.id}"
																				style="color: blue"><spring:theme
																					code="text.setDefault" text="Set as default" /></a>
																		</ycommerce:testId></li>
																</c:if>
																<c:if test="${address.defaultAddress}">
																	<li><ycommerce:testId
																			code="addressBook_isDefault_label">
																			<spring:theme code="text.default" text="Default" />
																		</ycommerce:testId></li>
																</c:if>
															</ul>
														</ycommerce:testId></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</c:if>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- </div> -->
	<div id="add_delivery_address" style="display: none"></div>
	<div id="edit_delivery_address" style="display: none"></div>
</template:page>
<script type="text/javascript">
function addDeliveryAddress(url){
	$('#add_delivery_address').empty();
	$('#add_delivery_address').load(url);
	$('#add_delivery_address').dialog({ 
		modal: false,
		width: 450,
		resizable: false,
		position: "top"
	});
}

function editDeliveryAddress(url){
	$('#edit_delivery_address').empty();
	$('#edit_delivery_address').load(url);
	$('#edit_delivery_address').dialog({ 
		modal: false,
		width: 450,
		resizable: false,
		position: "top"
	});
}
</script>
