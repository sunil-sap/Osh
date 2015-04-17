<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="showPotentialPromotions" required="false" type="java.lang.Boolean" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>

<%--<div class="item_container_holder">
	 <div class="title_holder">
		<div class="title">
			<div class="title-top">
				<span></span>
			</div>
		</div>
		<h2><spring:theme code="basket.page.title.yourItems"/></h2>
	</div> --%>
	
	
		<h2><spring:theme code="shopping.cart"/></h2>
		<div class="cart_tablewrapper">
			<form name="productname" id="productcart" novalidate="novalidate">
			<table  width="100%" cellspacing="0" cellpadding="0" border="0">
			<thead>
				<tr>
					<%-- <th id="header2"><span class="hidden"><spring:theme code="basket.page.title"/></span></th>
					<th id="header3"><spring:theme code="basket.page.quantity"/></th>
					<th id="header4"><spring:theme code="basket.page.itemPrice"/></th>
					<th id="header5"><spring:theme code="basket.page.total"/></th> --%>
					
					<th scope="col" class="R linkarrow"><a href="${request.contextPath}/cart"><spring:theme code="checkout.summary.edit.shopping.cart"/></a></th>
					<th scope="col" class="shippingcolume"><spring:theme code="osh.shopping.cart.header.store.pickup"/></th>
					<th scope="col" class="quantitycolume"><spring:theme code="order.quantity"/></th>
					<th scope="col" class="unitcolume"><spring:theme code="osh.shopping.cart.header.unitprice"/></th>
					<th scope="col" class="lastcolume"><spring:theme code="osh.shopping.cart.header.subtotal"/></th>	
				</tr>
			</thead>
			<tbody id="cart_items_tbody">
				<c:forEach items="${cartData.entries}" var="entry">
					<c:url value="${entry.product.url}" var="productUrl"/>
					<c:forEach items="${entry.product.baseOptions}" var="variant">
					<tr>
						<td class="descriptioncolume">
						<div class="product">
						<div class="thumbwrap">
								<a href="${productUrl}">
									<product:productPrimaryImage product="${entry.product}" format="cartPage"/>
								</a>
							</div>
							
							<div class="productname">
								<a href="${productUrl}">${entry.product.name}</a>
							</div>

							<div class="skunumber">
								<span><spring:theme code="checkout.sku"/></span><span>${entry.product.code}</span>
							</div>
							
							<c:if test="${not empty variant.selected.size}">
							<div class="size">
								<span><spring:theme code="checkout.size"/></span><span>${variant.selected.size}</span>
							</div>
							</c:if>
							<c:if test="${not empty variant.selected.colour}">
							<div class="color">
								<span><spring:theme code="checkout.color"/></span><span>${variant.selected.colour}</span>
							</div>
							</c:if>

							 <c:forEach items="${entry.product.baseOptions}" var="option">
								<c:if test="${not empty option.selected and option.selected.url eq entry.product.url}">
									<c:forEach items="${option.selected.variantOptionQualifiers}" var="selectedOption">
										<dl>
											<dt>${selectedOption.name}:</dt>
											<dd>${selectedOption.value}</dd>
										</dl>
									</c:forEach>
								</c:if>
							</c:forEach>
	
							<c:if test="${ycommerce:doesPotentialPromotionExistForOrderEntry(cartData, entry.entryNumber) && showPotentialPromotions}">
								<ul class="cart-promotions" style="color: red;">
									<c:forEach items="${cartData.potentialProductPromotions}" var="promotion">
										<c:set var="displayed" value="false"/>
										<c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
											<c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
												<c:set var="displayed" value="true"/>
												<li class="cart-promotions-potential"><span>${promotion.description}</span></li>
											</c:if>
										</c:forEach>
									</c:forEach>
								</ul>
							</c:if>
							<c:if test="${ycommerce:doesAppliedPromotionExistForOrderEntry(cartData, entry.entryNumber)}">
								<ul class="cart-promotions" style="color: red;">
									<c:forEach items="${cartData.appliedProductPromotions}" var="promotion">
										<c:set var="displayed" value="false"/>
										<c:forEach items="${promotion.consumedEntries}" var="consumedEntry">
											<c:if test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
												<c:set var="displayed" value="true"/>
												<li class="cart-promotions-applied"><span>${promotion.description}</span></li>
											</c:if>
										</c:forEach>
									</c:forEach>
								</ul>
							</c:if> 
							</div>
						</td>
						<td style="text-align:center;">
						<c:choose>
						<c:when test="${entry.orderType eq 'online'}">
					  <span><spring:theme code="osh.product.page.tab.shipping"/></span>					
						</c:when>
						<c:otherwise>
					 <span><spring:theme code="store.pickup"/></span>	<br/>
					 	${storeName}			
						</c:otherwise>		
						</c:choose>
						</td>
						<td style="text-align:center;">
							<span>${entry.quantity}</span>
						</td>
				<td class="unit_price">
				<c:choose>
					<c:when test="${not empty entry.mapPriceData}">
						<div class="unit_price">
							 <span><format:fromPrice priceData="${entry.mapPriceData}" /></span>
						</div>
					</c:when>
					<c:otherwise>
					<c:choose>
					<c:when test="${not empty entry.salePriceData}">
						<div class="unit_price">
							 <span><format:fromPrice priceData="${entry.salePriceData}"  /></span>
						</div>
					</c:when>
					<c:otherwise>
						<div class="unit_price">
							<span><format:fromPrice
									priceData="${entry.regPriceData}" /></span>
						</div>
						</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose>
			
				<c:if
					test="${not empty entry.salePriceData and empty entry.mapPriceData }">
					<div class="reg_price">
						<spring:theme code="checkout.reg"/><span><format:fromPrice
								priceData="${entry.regPriceData}" /></span>
					</div>
				</c:if>
					</td>	
						<td class="subtotal">
							<format:price priceData="${entry.totalPrice}" displayFreeForZero="true"/>
							<div class="promotion_shopping_val_cart">
					<c:if test="${not empty entry.promotionAmount}">
							<span>-${entry.promotionAmount.formattedValue}</span></c:if>
						</div>
						</td>
					</tr>
					</c:forEach>
				</c:forEach>
			</tbody>
		</table>
		</form>
	</div>
	<div class="clearb"></div>
