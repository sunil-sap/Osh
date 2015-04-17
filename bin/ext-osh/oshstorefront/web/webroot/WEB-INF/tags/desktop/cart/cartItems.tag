<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="cartData" required="true"
	type="de.hybris.platform.commercefacades.order.data.CartData"%>
<%@ attribute name="storeStock" required="true"
	type="java.lang.Integer"%>
	<%@ attribute name="storeName" required="true"
	type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>

 <script type="text/javascript">
//Function to allow only numbers to textbox
function validate(key)
{
//getting key code of pressed key
var keycode = (key.which) ? key.which : key.keyCode;
var phn = document.getElementById('quantity${entry.entryNumber}');
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


<div class="cart_tablewrapper">

	<table cellpadding="0" cellspacing="0" width="100%" border="0">
		<thead>
			<tr>
				<th class="firstcolume" scope="col"><spring:message
						code="osh.shopping.cart.header.item.description" /></th>
				<th class="shippingcolume" scope="col"><spring:message
						code="osh.shopping.cart.header.store.pickup" /></th>
				<th class="quantitycolume" scope="col"><spring:message
						code="osh.shopping.cart.header.quantity" /></th>
				<th class="unitcolume" scope="col"><spring:message
						code="osh.shopping.cart.header.unitprice" /></th>
				<th class="lastcolume" scope="col"><spring:message
						code="osh.shopping.cart.header.subtotal" /></th>
			</tr>
		</thead>
		<c:forEach items="${cartData.entries}" var="entry" varStatus="status">
			<c:forEach items="${entry.product.baseOptions}" var="variant">
				<tr>
					<td class="descriptioncolume">
						<div class="product">
							<div class="thumbwrap">
							<c:url value="${entry.product.url}" var="prodUrl"/>
								<span class="product_image">
									<a href="${prodUrl}">
										<product:productPrimaryImage product="${entry.product}" format="cartPage"/>
									</a>
								</span>
							</div>
							<div class="productname"><a href="${prodUrl}">${entry.product.name}</a></div>
							<div class="skunumber">
								<span><spring:theme code="checkout.sku"/></span><span>${entry.product.code}</span>
							</div>
							<c:if test="${ not empty variant.selected.colour}">	
								<div class="color_option"><spring:theme code="quick.view.popup.color"/> ${variant.selected.colour}</div>
							</c:if>							
							<c:if test="${ not empty variant.selected.size}">	
								<div class="size">
									<span><spring:message code="product.variants.size" /></span><span>
										${variant.selected.size}</span>
								</div>
							</c:if>
							<c:if test="${not empty entry.promotionMessage}">						
								<div class="promotion_shopping">
									<span>${entry.promotionMessage}</span>
								</div>
							</c:if>
						</div>
					</td>
					<td>
						<div class="storeorshippingwrapper">
							<div class="shippingoption freepickup selectedshipping">
								<div class="pickupoptionwrapper">
								<c:choose>
									<c:when test="${entry.orderType ne 'online'}">
										<%-- <input type="radio" name="${entry.product.code}_pickupoption1_${status.count}" id="freepickup1"
											value="FREE Pickup in Your Store" checked="checked" onclick="submitProduct('${entry.entryNumber}','${entry.quantity}','bopis')"/> --%> <label
											class="freepickuplabel"><spring:theme code="free.pick.up.store"/></label>
											<div class="standeredstock">
												<c:if test="${entry.stockLevelStatus.code eq 'inStock'}">
				   									<div class="instock">
														<span><spring:theme code="product.variants.in.stock"/></span>
													</div>
				   								</c:if>
				   								<c:if test="${entry.stockLevelStatus.code eq 'lowStock'}">
				   									<div class="almost_out_of_stock">
														<span><spring:theme code="osh.product.page.product.limitedStock"/></span>
													</div>
				   								</c:if>
				   								<c:if test="${entry.stockLevelStatus.code eq 'outOfStock'}">	
													<div class="out_of_stock">
														<span><spring:theme code="product.variants.out.of.stock"/></span>
													</div>
				   								</c:if>
											</div>
											<div class="shippingadd">
												<span>${storeName}</span>
											</div>
									</c:when>
									<c:otherwise>
										<%-- <input type="radio" name="${entry.product.code}_pickupoption1_${status.count}" id="freepickup1"
											value="FREE Pickup in Your Store" onclick="submitProduct('${entry.entryNumber}','${entry.quantity}','bopis')"/> --%> <label
										class="shipping_out_of_stock">&nbsp;&nbsp;&nbsp;&nbsp;<spring:theme code="osh.product.page.tab.shipping"/></label>
											<div class="standeredstock">
											<c:choose>
												<c:when
													test="${entry.product.stockLevelStatus.code eq 'inStock'  }">
													<div class="instock">
														<span><spring:message
																code="osh.product.page.product.instock" /></span>
													</div>
												</c:when>
												<c:when
													test="${entry.product.stockLevelStatus.code eq 'outOfStock' }">
													<div class="out_of_stock">
														<span><spring:message
																code="osh.product.page.product.outofstock" /></span>
													</div>
												</c:when>
												<c:when
													test="${entry.product.stockLevelStatus.code eq 'lowStock'}">
													<div class=" almost_out_of_stock">
														<span><spring:message
																code="osh.product.page.product.limitedStock" /></span>
													</div>
												</c:when>
											</c:choose>
								</div>
									</c:otherwise>
								</c:choose>
								</div>
								
							</div>
							<%-- <div class="shippingoption">
								<c:choose>
									<c:when test="${entry.orderType eq 'online'}">
										<input type="radio" name="${entry.product.code}_pickupoption1_${status.count}"
										value="Shipping" id="Shipping" checked="checked"  onclick="submitProduct('${entry.entryNumber}','${entry.quantity}','online')"/> <label
										class="shipping_out_of_stock">Shipping</label>
									</c:when>
									<c:otherwise>
										<input type="radio" name="${entry.product.code}_pickupoption1_${status.count}"
										value="Shipping" id="Shipping"  onclick="submitProduct('${entry.entryNumber}','${entry.quantity}','online')"/> <label
										class="shipping_out_of_stock">Shipping</label>	
									</c:otherwise>
								</c:choose>
								
							</div> --%>
						</div>
					</td>
				<td>
					<div class="update_quantitywrapper">
						<c:url value="/cart/update" var="cartUpdateFormAction" />
						<form:form id="updateCartForm${entry.entryNumber}"
							action="${cartUpdateFormAction}" method="post"
							commandName="updateQuantityForm${entry.product.code}">
							<input type="hidden" name="entryNumber" 
								value="${entry.entryNumber}" />
							<input type="hidden" name="productCode"
								value="${entry.product.code}" />
							<input type="hidden" name="initialQuantity"
								value="${entry.quantity}" />

							<div>
								<ycommerce:testId code="cart_product_quantity">
									<form:label cssClass="skip" path="quantity"></form:label>
									<form:input disabled="${not entry.updateable}" type="text"
										size="1" id="quantity${entry.entryNumber}" class="qty"
										value="${entry.quantity}" path="quantity" maxlength="2" onkeypress="return validate(event)" />
								</ycommerce:testId>
							</div>
							<c:if test="${entry.updateable}">
								<div class="link_update">
									<ycommerce:testId code="cart_product_updateQuantity">
										<a href="javascript:submitUpdate(${entry.entryNumber});">Update</a>
									</ycommerce:testId>
								</div>
							</c:if>


							<div class="link_update">
								<c:if test="${entry.updateable}">
									<ycommerce:testId code="cart_product_removeProduct">
										<a href="javascript:submitRemove(${entry.entryNumber});">
											<spring:theme code="basket.page.remove"/></a>
									</ycommerce:testId>
								</c:if>
							</div>
						</form:form>
					</div>
				</td>
				<td>
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
								 <span><format:fromPrice priceData="${entry.salePriceData}" /></span>
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
					<c:if test="${not empty entry.salePriceData and empty entry.mapPriceData }">
						<div class="reg_price">
							<spring:theme code="checkout.reg"/> <span><format:fromPrice priceData="${entry.regPriceData}" /></span>
						</div>
					</c:if>
				</td>
				<td>
					<div class="subtotal">
						<span><format:fromPrice priceData="${entry.totalPrice}" /></span>
					</div>
					<c:if test="${not empty entry.promotionAmount}">
						<div class="promotion_shopping_val">
							<span>-<format:fromPrice priceData="${entry.promotionAmount}" /></span>
						</div>
					</c:if>
				</td>
			</tr>
		</c:forEach>
	</c:forEach>
</table>
	<div class="clearb"></div>
	<div></div>
</div>