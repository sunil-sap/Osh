<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>
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
			
			
			<div id="globalMessages" class="removePaymentDetails">
				<common:globalMessages />
			</div>
			
			<div class="acc_middlewrap">
				<div class="leftmaincontent">
					<nav:accountNav />
				</div>
				
			<div class="span-20password multicheckout">
				<div class="item_container_holder_password">
			<div class="title_holder">
				<div class="title">
					<div class="title-top">
						<span></span>
					</div>
				</div>
				<h2><spring:theme code="text.account.paymentDetails" text="Payment Details"/></h2>
			</div>
			<div class="item_container_myacc_addressBook">
				<p><spring:theme code="text.account.paymentDetails.managePaymentDetails" text="Manage your saved payment details."/>
					 <%-- <ycommerce:testId code="addressBook_addNewAddress_button">
						<a href="javascript:addDeliveryAddress('${request.contextPath}/my-account/add-paymentdetail');" id="addaddresspopup">
							<button class="positive right address_button" type="submit"><spring:theme code="text.address.button"/></button>
						</a>
					</ycommerce:testId>  --%>
					<c:url value="${request.contextPath}/my-account/add-paymentdetail" var="addPaymentUrl"/>
						<a class="positive right address_button ajax" href="${request.contextPath}/my-account/add-paymentdetail" id="addaddresspopup">
							<spring:theme code="text.address.button"/>
						</a>
						</p>
				<c:if test="${not empty paymentInfoData}">
				<div id="addressbookprofile_table">
					<table id="payment_details">
						<thead>
							
							<tr>
								<th id="header1"><spring:theme code="text.account.paymentDetails.paymentCard" text="Payment Card"/></th>
								<th id="header2"><spring:theme code="text.account.paymentDetails.billingAddress" text="Billing Address"/></th>
								<th id="header3"><spring:theme code="text.updates" text="Updates"/></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${paymentInfoData}" var="paymentInfo">
								<tr>
									<td headers="header1">
										<ul>
											<li>${fn:escapeXml(paymentInfo.cardNumber)}</li>
											<li>${fn:toUpperCase(paymentInfo.cardType)}</li>
											<li><spring:theme code="text.expires" text="Expires"/>&nbsp;${fn:escapeXml(paymentInfo.expiryMonth)} / ${fn:escapeXml(paymentInfo.expiryYear)}</li>
										</ul>
									</td>
									<td headers="header2">
										<ul>
											<li><c:out value="${fn:escapeXml(paymentInfo.billingAddress.title)} ${fn:escapeXml(paymentInfo.billingAddress.firstName)} ${fn:escapeXml(paymentInfo.billingAddress.lastName)}"/></li>
											<li>${fn:escapeXml(paymentInfo.billingAddress.line1)}</li>
											<li>${fn:escapeXml(paymentInfo.billingAddress.line2)}</li>
											<li>${fn:escapeXml(paymentInfo.billingAddress.town)}</li>
											<li>${fn:escapeXml(paymentInfo.billingAddress.postalCode)}</li>
											<li>${fn:escapeXml(paymentInfo.billingAddress.country.name)}</li>
											<li>${fn:escapeXml(paymentInfo.billingAddress.phone)}</li>
										</ul>
									</td>
									<td headers="header3">
										<ul class="updates">
										<c:if test="${paymentInfo.defaultPaymentInfo}">
												<li><spring:theme code="text.default" text="Default"/></li>
										</c:if>
										
										<c:if test="${not paymentInfo.defaultPaymentInfo}">
											<c:url value="/my-account/set-default-payment-details" var="setDefaultPaymentActionUrl"/>
											<form:form id="setDefaultPaymentDetails${paymentInfo.id}" action="${setDefaultPaymentActionUrl}" method="post">
												<input type="hidden" name="paymentInfoId" value="${paymentInfo.id}"/>
												<li><a href="javascript:submitSetDefault(${paymentInfo.id});" style="color:blue;"><spring:theme code="text.setDefault" text="Set as default"/></a></li>
											</form:form>
										</c:if>

											<c:url value="/my-account/remove-payment-method" var="removePaymentActionUrl"/>
											<form:form id="removePaymentDetails${paymentInfo.id}" action="${removePaymentActionUrl}" method="post">
												<input type="hidden" name="paymentInfoId" value="${paymentInfo.id}"/>
												<li><a href="javascript:submitRemove(${paymentInfo.id});" style="color:blue;"><spring:theme code="text.remove" text="Remove"/></a></li>
											</form:form>
										</ul>
									</td>
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
	<div id="add_delivery_address" style="display: none">
</div>
</template:page>
<script type="text/javascript">
	 /*<![CDATA[*/
		function submitRemove(id){
			document.getElementById('removePaymentDetails'+id).submit();
		}
		function submitSetDefault(id){
			document.getElementById('setDefaultPaymentDetails'+id).submit();
		}
		
		function addDeliveryAddress(url){
			$('#add_delivery_address').empty();
			$('#add_delivery_address').load(url);
			$('#add_delivery_address').dialog({ 
				modal: true,
				width: 900,
				resizable: false,
				position: "top"
			});
		}
		$(document).ready(function() {
			
			$('#addaddresspopup').click(function(){
//				$.colorbox({href:this.href, height: 'auto', innerHeight:"930px" ,width: 'auto'});
			});
		});	
		
	/*]]>*/
</script>