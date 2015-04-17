<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:url value="${previousPage}" var="continueShoppingUrl" />
<c:url value="/cart/clearSession" var="clearSession" /> 

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>Cart Page</title>
</head>		
<template:page pageTitle="${pageTitle}">


	<div id="middleContent" class="page_shoppingcart">
		<div class="innermiddleContent">
			<div id="breadcrumb" class="breadcrumb">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			</div>
			<div>
				<cms:slot var="feature" contentSlot="${slots.GlobalSection}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>
			
				<label class="error"><common:globalMessages /></label>
			
			<c:choose>
			<c:when test="${not empty cartData.entries}">
			
			<div class="mainmiddleContent">
				<div class="fullmiddlemaincontent">
					<div class="middlemainheader">
						<span><spring:message code="osh.storeLocator.page.heading" /></span>
					</div>
					<div class="cart_reference">
						<span><spring:message code="osh.storeLocator.page.heading.cart.refrence" /></span><span>${cartData.code}</span>
					</div>
					<div class="link_shopmore">
						<a href="#" class="previousPage"><spring:message code="osh.storeLocator.page.button.shop.more" /></a>
					</div>
					<table width="300px" border="0" align="right">
					<tr>
						<td>
						<div class="secure_checkout">
							<a href="${request.contextPath}/cart/checkout"><spring:message code="osh.storeLocator.page.button.secure.checkout" /></a>
						</div>
						</td>
						</tr>
					</table>
					<div id="cartItem">
					<cart:cartItems cartData="${cartData}"  storeStock="${stock}" storeName="${storeName}" />
					</div>
					<hr style="width:100%">
					<br>
					<div class="promotionwrapper">
					
						<div id="accordian">
							
							
							<div class="accordiancontent">
							<c:choose>
							<c:when test="${empty cartData.voucherCode}">
							<div class="accordianhead promotion_code">
								<span><spring:theme code="question.promotion.code"/></span>
							</div>
							<div class = "viewpromotion">
								<form id="voucherForm" action="${request.contextPath}/cart/applyVoucher" method="get" onsubmit="return validateForm()" >
									<c:choose>
									<c:when test="${!empty cartData.voucherCode}">
									<input type="text" name="voucherCode"  disabled="disabled"/>
									<input class="button" type="submit" value="Apply" disabled="disabled"/>
									</c:when>
									<c:otherwise>
									<input type="text" name="voucherCode" class="voucher_code_search" id="voucherCode"/>
									<input class="button voucher_code_button" type="submit" value="Apply" />
									
									</c:otherwise>
									</c:choose>
						    	</form>
						    	<span id="errorMsg" style="display: none;"> <br><spring:theme code="enter.voucher.code"/></span>
								</div>
							</c:when>
							<c:otherwise>
							<div class ="appliedPromotion">
							     <div class="accordianhead active_promotion">
										<spring:message code="cart.voucher.info1" /><br/>
										<spring:message code="cart.voucher.info2" />
							     </div>
						     <form id="voucherForm" action="${request.contextPath}/cart/releaseVoucher" method="get" >
							  <spring:message code="cart.voucher.yourVoucherCode" />
								<c:choose>
								<c:when test="${cartData.voucherCode eq 'LOYALTYVOUCHER' }">
								${loyaltyVoucher}
								</c:when>
								<c:otherwise>
								 ${cartData.voucherCode}
								</c:otherwise>
								</c:choose>
								<input class="button" type="submit" value="Release Voucher" />
							</form>
						   </div>
							</c:otherwise>
							</c:choose>
							</div>
						</div>
					</div>
					<div class="order_summerywrapper" >
						<table width="100%" border="0" style="font-size: 12px;color: #000000;">
							<tr>
								<td><spring:theme code="checkout.summary.orderSubtotal"/></td>
								<td class="totalvalue"><format:fromPrice
										priceData="${cartData.subTotal}" /></td>
							</tr>
							<tr>
								<td><spring:theme code="checkout.confirmation.productDiscount"/></td>
								<c:if test="${empty cartData.productDiscounts.formattedValue }">
										<td class="totalvalue">$0.00</td>
									</c:if>
								<td class="totalvalue">
									<c:choose>
									<c:when test="${cartData.productDiscounts.value gt 0}">
									<span class="redcolor">	-${cartData.productDiscounts.formattedValue}</span>
									</c:when>
									<c:otherwise>
										${cartData.productDiscounts.formattedValue}
									</c:otherwise>
									</c:choose>
									</td>
							</tr>
							<tr>
								<td><spring:theme code="checkout.summary.promoDiscount"/></td>
								<c:if test="${empty cartData.orderDiscounts.formattedValue }">
										<td class="totalvalue">$0.00</td>
									</c:if>
									<c:choose>
									<c:when test="${cartData.voucherCode ne 'LOYALTYVOUCHER' && cartData.orderDiscounts.value gt 0 }">
										<td class="totalvalue redcolor">-${cartData.orderDiscounts.formattedValue}</td>
									</c:when>
									<c:otherwise>
										<td class="totalvalue">$0.00</td>
									</c:otherwise>
									</c:choose>
							</tr>
							<tr>
								<td><spring:theme code="checkout.summary.clubRewards"/></td>
								<c:choose>
									<c:when test="${cartData.voucherCode eq 'LOYALTYVOUCHER' && cartData.orderDiscounts.value gt 0}">
										<td class="totalvalue redcolor">-${cartData.orderDiscounts.formattedValue}</td>
									</c:when>
									<c:otherwise>
										<td class="totalvalue">$0.00</td>
									</c:otherwise>
									</c:choose>
							</tr>
							<tr>
								<%-- <td><div class="estimated_shipping">
										<span><spring:theme code="cart.estimate.shipping"/></span>
									</div></td>
									<c:if test="${empty cartData.deliveryCost.formattedValue }">
										<td class="totalvalue">$0.00 </td>
									</c:if>
								<td class="totalvalue">${cartData.deliveryCost.formattedValue}</td> --%>
							</tr>
						</table>
					</div>
					<table width="100%" border="0">
					<tr>
						<td>
					<div class="order_total">
						<span><spring:theme code="cart.order.total"/></span><span class="totalvalue"><format:fromPrice
								priceData="${cartData.totalPrice}" /></span>
					</div>
					</td>
						</tr>
					</table>
					
					<table width="100%" border="0">
					<tr>
						<td>
					<div class="note_text_cartPage">
						<b><spring:theme code="text.calculate.checkout" /></b>
					</div>
					</td>
						</tr>
					</table>
					
					<div class="safe_secure"></div>
					<div class="link_shopmore">
						<a href="#" class="previousPage"><spring:message code="osh.storeLocator.page.button.shop.more" /></a>
					</div>
					
					<table width="300px" border="0" align="right">
					<tr>
						<td>
						<div class="secure_checkout">
							<a href="${request.contextPath}/cart/checkout"><spring:message code="osh.storeLocator.page.button.secure.checkout" /></a>
						</div>
						</td>
						</tr>
					</table>		
						
					
					<br clear="all" /> <br clear="all" /> <br clear="all" />
				</div>
			</div>
			</c:when>
			
			<c:otherwise>
			<div class="middlemainheader">
			
					<div class="emptylink_shopmore">
						<a href="#" class="previousPage"><spring:message code="osh.storeLocator.page.button.shop.more" /></a>
					</div>
					<br/><br/>
						<span>
						<h2><spring:theme code="text.empty.cart"/></h2>
						</span>
					</div>
			
			</c:otherwise>
			</c:choose>
		</div>
	</div>
</template:page>

<script type="text/javascript">
	function submitRemove(id) {
		var productCode = $('#updateCartForm' + id).get(0).productCode.value;
		var initialCartQuantity = $('#updateCartForm' + id).get(0).initialQuantity.value;

		if (window.trackRemoveFromCart) {
			trackRemoveFromCart(productCode, initialCartQuantity);
		}

		$('#updateCartForm' + id).get(0).quantity.value = 0;
		$('#updateCartForm' + id).get(0).initialQuantity.value = 0;
		$('#updateCartForm' + id).get(0).submit();
	}
	function submitUpdate(id) {
		var productCode = $('#updateCartForm' + id).get(0).productCode.value;
		var initialCartQuantity = $('#updateCartForm' + id).get(0).initialQuantity.value;
		var newCartQuantity = $('#updateCartForm' + id).get(0).quantity.value;
		if (window.trackUpdateCart) {
			trackUpdateCart(productCode, initialCartQuantity, newCartQuantity);
		}

		$('#updateCartForm' + id).get(0).submit();
	}


</script>

<script type="text/javascript">	
	function submitProduct(code,qty,type){
		var allowSubmit = true;	
		var qty =qty;
		var entryNumber=code;
		var orderType=type;
			$.ajax(
				{		
					url :	"${request.contextPath}/cart/mergeCart",
					type : 'POST',
					dataType : 'json',
					data : {qty : qty,entryNumber : code,orderType : orderType},
					success : function(data){
						window.location.reload();
					}	
				});
		}
</script>
<script type="text/javascript">	

$('.previousPage').click(function(){		
	$.ajax({
		url: '${clearSession}',
		success: function(){
			window.location = "${continueShoppingUrl}";
		}
	});
});

function validateForm()
{
	if($('#voucherCode').val()=='')
  	{
		$('#errorMsg').show();
		return false;
	 }
		else
		{
			$('#errorMsg').hide();
		}	
}

$('form').submit(function(){
	$('#middleContent').block({ message: "" });
});



</script>