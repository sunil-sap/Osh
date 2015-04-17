<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

	
	<h2 style="margin-left: 20px;"><spring:theme code="text.account.order.yourOrder" text="Your Order"/></h2>
		<div class="orderdetail" style="margin-left: 20px;">
		<p><spring:theme code="text.account.order.orderNumber" text="Order number is {0}" arguments="${order.code}"/></p>
		<p><spring:theme code="text.account.order.orderPlaced" text="Placed on {0}" arguments="${order.created}"/></p>
		<c:if test="${not empty order.status}">
			<p><spring:theme code="text.account.order.orderStatus" text="The order is {0}" arguments="${order.statusDisplay}"/></p>
		</c:if>
		</div>		
	<div class="cart_tablewrapper" style="width: 740px; margin-left: 15px;">
		<table   id="your_order" width="100%" cellspacing="0" cellpadding="0" border="0">
			<thead>
				<tr>
					<th scope="col" class="firstcolume"><spring:theme code="checkout.confirmation.itemDescripton"
						text="Item Description:" /></th>
					<th scope="col" class="shippingcolume"><spring:theme code="checkout.confirmation.StorePickupOrShipping"
						text="Store Pickup Or Shipping:" /></th>
					<th scope="col" class="quantitycolume"><spring:theme code="checkout.confirmation.Quantity"
						text="Quantity:" /></th>
					<th scope="col" class="unitcolume"><spring:theme code="checkout.confirmation.UnitPrice"
						text="Unit Price:" /></th>
					<th scope="col" class="lastcolume"><spring:theme code="checkout.confirmation.Subtotal"
						text="Subtotal:" /></th>	
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${order.entries}" var="entry">
				<c:forEach items="${entry.product.baseOptions}" var="variants">
					<c:url value="${entry.product.url}" var="productUrl"/>
					<tr>
					<td class="descriptioncolume">
						<div class="product">
						<div class="thumbwrap">
							<a href="${productUrl}">
								<product:productPrimaryImage product="${entry.product}" format="cartPage"/>
							</a>
						</div>
						<div class="productname">
								<ycommerce:testId code="orderDetails_productName_link">
									<a href="${entry.product.purchasable ? productUrl : ''}">${entry.product.name}</a>
								</ycommerce:testId>
						</div>
							<div class="skunumber">
								<span><spring:theme code="checkout.sku"/></span><span>${entry.product.code}</span>
							</div>
							<c:if test="${not empty variants.selected.size}">
							<div class="size">
								<span><spring:theme code="checkout.size"/></span><span>${variants.selected.size}</span>
							</div>
							</c:if>
							<c:if test="${not empty variants.selected.colour}">
							<div class="color">
								<span><spring:theme code="checkout.color"/></span><span>${variants.selected.colour}</span>
							</div>
							</c:if>
						 
								 <ul class="cart-promotions" style="color: red;">
									<%-- <c:forEach items="${order.potentialProductPromotions}" var="promotion"> --%>
										<%-- <c:set var="displayed" value="false"/>
										<c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
											<c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
												<c:set var="displayed" value="true"/>
												<li class="cart-promotions-potential"><span>${promotion.description}</span></li>
											</c:if>
										</c:forEach> --%>
									<%-- </c:forEach> --%>
								</ul> 
							
							 <ul class="cart-promotions" style="color: red;">
									<c:forEach items="${order.appliedProductPromotions}" var="promotion">
										 <c:set var="displayed" value="false"/>
										<c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
											<c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
												<c:set var="displayed" value="true"/>
												<li class="cart-promotions-applied"><span>${promotion.description}</span></li>
											</c:if>
										</c:forEach> 
									</c:forEach>
								</ul> 								
						</div>
						</td>
						
						<td headers="header2" style="text-align:center;">
						<c:choose>
						<c:when test="${entry.orderType eq 'online' || entry.orderType eq 'dropship'}">
					  <span><spring:theme code="osh.product.page.tab.shipping"/></span>					
						</c:when>
						<c:otherwise>
					 <span><spring:theme code="store.pickup"/></span><br/>
					 	${storeName}			
						</c:otherwise>		
						</c:choose>
						</td>
						
						<td headers="header3" class="quantity">
							<ycommerce:testId code="orderDetails_productQuantity_label">${entry.quantity}</ycommerce:testId>
						</td>
						<td headers="header4" class="unit_price">
							<ycommerce:testId code="orderDetails_productItemPrice_label"><format:price priceData="${entry.basePrice}" displayFreeForZero="true"/></ycommerce:testId>
						</td>
						<td headers="header5" class="subtotal">
							<ycommerce:testId code="orderDetails_productTotalPrice_label"><format:price priceData="${entry.totalPrice}" displayFreeForZero="true"/></ycommerce:testId>
						<div class="promotion_shopping_val" style="margin-top: 67px;">
						<c:if test="${not empty entry.promotionAmount}">
							<span>-${entry.promotionAmount.formattedValue}</span></c:if>
						</div>
						</td>
					</tr>
					</c:forEach>
				</c:forEach>
			</tbody>
		</table>
	</div>


