<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/desktop/user"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="checkout" tagdir="/WEB-INF/tags/desktop/checkout"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>CheckOutPage</title>
<script type="text/javascript" charset="utf-8">
$(document).ready(function(){
	
	$('#linkeditpaymentmethod').click(function() {
		$('#update_payment_method').dialog({
		width:860,
		title:'Payment Method',
		modal: true
		});
    });
	
	$('#usethisaddleft1').click(function() {
        $('#update_shipping_method').dialog('close');
    });
	
	$('#linkaddaddress').click(function() {
		$('#update_delivery_add').dialog({
		width:860,
		title:'Delivery Address',
		modal: true
		});
    });
	
	$('#usethisaddleft1').click(function() {
        $('#update_shipping_method').dialog('close');
    });
	
	
	$('#addshippingmethod').click(function() {
		alert("working");
		$('#update_shipping_method').empty();
		$('#update_shipping_method').load("addShippingMethod.jsp");
		$('#update_shipping_method').dialog({
		width:500,
		title:'Shipping Method',
		modal: true
		});
    });
	
	$('#btn_updateshippingmethod').click(function() {
        $('#update_shipping_method').dialog('close');
    });
	
	$('#address').validate();
	
});
</script>
</head>
<template:checkoutLogin />
<body>
	<div id="loginmainBody">
		<div id="mainContent">
			<div id="loginheader">
				<div id="headerLogo" style="height: 60px">
					<cms:slot var="feature" contentSlot="${slots.CheckoutSiteLogo}">
						<cms:component component="${feature}" />
					</cms:slot>
				</div>
				<div class="right_text">
					<cms:slot var="feature" contentSlot="${slots.CheckoutRight}">
						<cms:component component="${feature}" />
					</cms:slot>
				</div>
			</div>
			<div id="loginmiddleContent">
				<div class="innermiddleContent">
					<div class="checkoutwrapper">
						<div id="errorwrapper">
							<label class="error"><spring:theme code="error.message.info"/></label>
						</div>
						<div class="content_left">
							<div class="formsection">
								<h2>
									<span class="step"><spring:theme code="text.one"/></span><spring:theme code="checkout.shippingAddress"/>
								</h2>
								<div class="borderbox">
									<div class="cartformwarp">
										<div class="cartformwarp_inner">
											<p><spring:theme code="checkout.multi.paymentMethod.paymentDetails.noneSelected"/></p>
										</div>
										<a href="#" class="linkarrow" id="linkaddaddress"><spring:theme code="add.address"/></a>
									</div>
								</div>
							</div>
						
							<div class="formsection">
								<h2>
									<span class="step"><spring:theme code="text.two"/></span><spring:theme code="checkout.shippingMethod"/>
								</h2>
								<div class="borderbox">
									<div class="cartformwarp">
										<div class="cartformwarp_inner">
											<p><spring:theme code="checkout.multi.paymentMethod.paymentDetails.noneSelected"/></p>
										</div>
										<a href="#" class="linkarrow" id="addshippingmethod"><spring:theme code="add.shipping.method"/></a>
									</div>
								</div>
							</div>
							<div class="formsection">
								<h2>
									<span class="step"><spring:theme code="text.three"/></span><spring:theme code="checkout.paymentMethod"/>
								</h2>
								<div class="borderbox">
									<div class="cartformwarp">
										<div class="cartformwarp_inner">
											<p><spring:theme code="checkout.multi.paymentMethod.paymentDetails.noneSelected"/></p>
										</div>
										<a href="#" class="linkarrow" id="linkeditpaymentmethod"><spring:theme code="checkout.multi.paymentMethod"/></a>
									</div>
								</div>
							</div>

							<div class="clearb"></div>
							<div class="formsection">
								<h2><spring:theme code="checkout.summary.store.pickup.address"/></h2>
								<div class="borderbox">
									<div class="cartformwarp">
										<div class="cartformwarp_inner">
											<p>{Store Name}</p>

											<p>{Store Address}</p>

											<p>{Store City}, {Store State} {Store Zip}</p>

											<p>{Store Phone}</p>
										</div>
										<a href="#" class="linkgetdirection"><spring:theme code="osh.storeLocator.page.getDirection"/></a>
									</div>
								</div>
							</div>
							<div class="placeorderwrapper">
								<div class="note_text">
									<spring:theme code="checkout.summary.verify"/><br/><spring:theme code="checkout.summary.verify.place"/>								</div>
								<div class="clearb"></div>
								<a href="#" class="floatl"><spring:theme code="checkout.summary.edit.shopping.bag"/></a><span
									class="btngreen floatr"><input type="submit"
									value="Place Order" /></span>

							</div>
							<hr />
							<div class="shoppingbaglist">
								<h2><spring:theme code="shopping.cart"/></h2>
								<div class="cart_tablewrapper">
									<form name="productname" id="productcart"
										novalidate="novalidate">
										<table width="100%" cellspacing="0" cellpadding="0"
											border="0">
											<thead>
												<tr>
													<th scope="col" class="firstcolume"><spring:theme code="osh.shopping.cart.header.item.description"/></th>
													<th scope="col" class="shippingcolume"><spring:theme code="osh.shopping.cart.header.store.pickup"/></th>
													<th scope="col" class="quantitycolume"><spring:theme code="osh.shopping.cart.header.quantity"/></th>
													<th scope="col" class="unitcolume"><spring:theme code="osh.shopping.cart.header.unitprice"/></th>
													<th scope="col" class="lastcolume"><spring:theme code="osh.shopping.cart.header.subtotal"/></th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td class="descriptioncolume"><div class="product">
															<div class="thumbwrap">
																<img src="media/images/cartpage/thumbnail_umbrella.gif">
															</div>
															<div class="productname">Brand Name 10' Dupione
																offset Umbrella</div>
															<div class="skunumber">
																<span><spring:theme code="checkout.sku"/></span><span>109283746</span>
															</div>
															<div class="size">
																<span><spring:theme code="checkout.size"/></span><span>6 foot</span>
															</div>
															<div class="color">
																<span><spring:theme code="checkout.color"/></span><span>Pebble</span>
															</div>

														</div></td>
													<td>&nbsp;</td>
													<td>1</td>
													<td><div class="unit_price">
															<span>$179.99</span>
														</div></td>
													<td><div class="subtotal">
															<span>$179.99</span>
														</div></td>
												</tr>
											</tbody>
										</table>
									</form>
								</div>
								<div class="clearb"></div>
								<hr>
								<div class="promotionwrapper">
									<h3><spring:theme code="basket.potential.promotions"/></h3>
									<div class="viewpromotion">
										<div class="">
											<p>{Promotion Description}</p>
											<input class="textfield" type="text"> <input
												class="button" type="submit" value="APPLY">
										</div>
										<div class="clearb"></div>
										<div class="martop">
											<a class="linkarrow" href="#"><spring:theme code="checkout.summary.deliveryMode.useThisDeliveryMethod"/></a>
										</div>
									</div>
								</div>
								<div class="order_summerywrapper">
									<table border="0" width="100%">
										<tbody>
											<tr>
												<td><spring:theme code="checkout.confirmation.orderSubtotal"/></td>
												<td class="totalvalue">$000.00</td>
											</tr>
											<tr>
												<td><spring:theme code="checkout.confirmation.productDiscount"/></td>
												<td class="totalvalue">$000.00</td>
											</tr>
											<tr>
												<td><spring:theme code="checkout.summary.promoDiscount"/></td>
												<td class="totalvalue">$000.00</td>
											</tr>
											<tr>
												<td><spring:theme code="checkout.summary.clubRewards"/></td>
												<td class="totalvalue">$000.00</td>
											</tr>
											<tr>
												<td>
													<div class="estimated_shipping">
													<span><spring:theme code="checkout.summary.estimateShipping"/></span>
													</div>
												</td>
												<td class="totalvalue">$000.00</td>
											</tr>
										</tbody>
									</table>
								</div>
								<div class="order_total">
									<span><spring:theme code="checkout.confirmation.orderTotal"/>:</span><span class="totalvalue">$000.00</span>
								</div>
								<div class="note_text">
									<b><spring:theme code="checkout.summary.tax"/></b>
								</div>
								<div class="secure_checkout">
									<a herf="#"><spring:theme code="osh.storeLocator.page.button.secure.checkout"/></a>
								</div>
								<div></div>
							</div>
							<div></div>
						</div>
						<div class="content_right">
							<div class="bannnerwrap">
								 <cms:slot var="feature"
									contentSlot="${slots.CheckoutSideImg}">
									<cms:component component="${feature}" />
								</cms:slot> 
							</div>
						</div>
						<div class="clearfix"></div>
					</div>
				</div>
			</div>
		   <%-- <div id="footer">
				<div class="copyrightsbox overflow">
					<div class="">
						<span width="101" height="53" align="right"> <cms:slot var="feature"
									contentSlot="${slots.CheckoutFooterRight}">
									<cms:component component="${feature}" />
								</cms:slot> 
								<cms:slot var="feature"
									contentSlot="${slots.CheckoutFooterLeft}">
									<cms:component component="${feature}" />
								</cms:slot>
							</span>
					</div>
				</div>
			</div>   --%>
			  <div id="footer">
				<div class="copyrightsbox overflow">
					<div class="">
						<span><img width="101" height="53" align="right"
							src="media/images/global/nortonsecired_icon.gif">Copyright
							&copy; 2012 Orchard Supply Hardware. <br><spring:theme code="rights.reserved"/></span> <a href="#"><spring:theme code="privacy.policy"/></a>
					</div>
				</div>
			</div> 
		</div>
	</div>
</body>
</html>
