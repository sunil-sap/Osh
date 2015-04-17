<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>

<spring:theme code="text.addToCart" var="addToCartText"/>
<spring:theme code="text.popupCartTitle" var="popupCartTitleText"/>
<c:url value="/cart" var="cartUrl"/>
<c:url value="/cart/checkout" var="checkoutUrl"/>

<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
	<span id="ui-id-1" class="ui-dialog-title">
	<span class="dialogtitlebox">
		<span><img
				class="dialogtitleimage" alt=""
				src="${request.contextPath}/_ui/desktop/osh/images/shooping_cart_icon_black.png"></span>
		<spring:message code="osh.storeLocator.page.heading"/>
	</span>
	<a href="#" id="ajax_cart_close" class="ui-dialog-titlebar-close ui-corner-all">
		<span class="ui-icon ui-icon-closethick" style="background-image: url(${request.contextPath}/_ui/desktop/osh/css/images/ui-icons_222222_256x240.png)/*{iconsHeader}*/;"></span>
	</a>
	</span>
</div>
<div class="ui-dialog-content ui-widget-content"
	style="width: auto; min-height: 109px; height: auto;" scrolltop="0"
	scrollleft="0">
	<div>
		<div class="productList" id="cart-popup-product">
			<c:choose>
				<c:when test="${not empty cartData.entries}">

					<span><spring:theme code="popup.cart.showing" arguments="${numberShowing},${numberItemsInCart}"/>
						&nbsp;<a href="${cartUrl}"><spring:theme code="review.show.all"/></a>
					</span>
					<ul>
						<c:forEach items="${entries}" var="entry" end="1">
							<li><c:url value="${entry.product.url}"
									var="entryProductUrl" /> <!--  for temporary testing,it nedds to remove -->


								<%--   <c:forEach items="${entry.product.images}" var="image">
                      <div class="productthumb floatl">
                   <a href="${entryProductUrl}">  <img  src="${image.url}"></a>
                      </div>
                      </c:forEach> --%>
								<div class="productthumb floatl">
									<span class="product_image"><a href="${productUrl}">
											<product:productPrimaryImage product="${entry.product}"
												format="cartPage"/>
									</a>
									</span>
								</div>
								<div class="floatl">
									<div class="producttitle productcarttitle">
										<span>${entry.product.name}</span>
									</div>
									<div class="productcartdetails">
										<div><spring:message code="osh.shopping.cart.header.quantity" />: ${entry.quantity}
										</div>
										<div>
										<c:if test="${not empty entry.product.size}"> 
											<spring:message code="product.variants.size" />:${entry.product.size}</c:if>
										</div>
										<%--  <div>
									<spring:message code="product.variants.colour" /> ${entry.product.colour} <span class="lightorangecolor"></span>
								</div> --%>
									</div>
								</div>
								<div class="pricelabel floatr">
									
										<c:choose>
						<c:when test="${not empty entry.mapPriceData}">
							<div class="unit_price">
								 <span><format:fromPrice priceData="${entry.mapPriceData}" /></span>
							</div>
						</c:when>
						<c:otherwise>
						<c:choose>
						<c:when test="${not empty entry.salePriceData}">
										
							<div class="sale_unit_price">
								 <span><spring:theme code="osh.productListing.page.sale"/> &nbsp;<format:fromPrice priceData="${entry.salePriceData}" /></span>
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
						<%-- 				<format:fromPrice priceData="${entry.totalPrice}" /> --%>
								</div></li>
						</c:forEach>
					</ul>
					<div class="totallabel">
						<spring:message code="basket.page.total" />
						:
						<format:fromPrice priceData="${cartData.totalPrice}" />
					</div>
					<div class="cartpopupbutton">
						<span class="clearb floatr"> <span class="viewcartbutton">
								<a href="${request.contextPath}/cart"><spring:message
										code="osh.storeLocator.page.button.view.cart" /></a>
						</span> <span class="securecheckoutbutton"> <a
								href="${request.contextPath}/cart/checkout"><spring:message
										code="osh.storeLocator.page.button.secure.checkout" /></a>
						</span>
						</span>
					</div>
					<c:choose>
					<c:when
						test="${not empty lightboxBannerComponent && lightboxBannerComponent.visible}">
						<cms:component component="${lightboxBannerComponent}" />
					</c:when>
					<c:otherwise>
						<div class="blank_banner"></div>
					</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<span>
						<h2 class="cartpopupemptymsg">
							<spring:theme code="text.empty.cart" />
						</h2>
					</span>
				</c:otherwise>
			</c:choose>
		</div>

	</div>
</div>


<%--  leave for future reference--%>
<%-- 
<div class="title">
	<theme:image code="img.addToCartIcon" alt="${popupCartTitleText}" title="${popupCartTitleText}"/>
	<h3><spring:theme code="popup.cart.title"/></h3>
	<a href="#" class="close" id="ajax_cart_close"></a>
</div>

<c:if test="${numberShowing > 0 }">
	<p class="legend">
		<spring:theme code="popup.cart.showing" arguments="${numberShowing},${numberItemsInCart}"/>
		<c:if test="${numberItemsInCart > numberShowing}">
			<a href="${cartUrl}">Show All</a>
		</c:if>
	</p>
</c:if>
<c:if test="${empty numberItemsInCart}">
	<div class="cart_modal_popup empty-popup-cart">
		<spring:message code="popup.cart.empty"/>
	</div>
</c:if>
<c:if test="${numberShowing > 0 }">
	<c:forEach items="${entries}" var="entry" end="${numberShowing - 1}">
		<c:url value="${entry.product.url}" var="entryProductUrl"/>
		<div class="cart_modal_popup">
			<div class="prod_image">
				<a href="${entryProductUrl}">
					<product:productPrimaryImage product="${entry.product}" format="cartIcon"/>
				</a>
			</div>
			<div class="prod_info">
				<a href="${entryProductUrl}"><p class="prod_name">${entry.product.name}</p></a>
				<p class="prod_price"><format:price priceData="${entry.basePrice}"/></p>
				<p class="prod_quantity"><span class="product-variant-label"><spring:message code="popup.cart.quantity"/></span>${entry.quantity}</p>
				<p class="prod_options">
					<c:forEach items="${entry.product.baseOptions}" var="baseOptions">
						<c:forEach items="${baseOptions.selected.variantOptionQualifiers}" var="baseOptionQualifier">
							<c:if test="${baseOptionQualifier.qualifier eq 'style' and not empty baseOptionQualifier.image.url}">
								<span class="prod_color"><span class="product-variant-label"><spring:theme code="product.variants.colour"/></span><img src="${baseOptionQualifier.image.url}"/></span>
							</c:if>
							<c:if test="${baseOptionQualifier.qualifier eq 'size'}">
								<span class="prod_size"><span class="product-variant-label"><spring:theme code="product.variants.size"/></span>${baseOptionQualifier.value}</span>
							</c:if>
						</c:forEach>
					</c:forEach>
				</p>
			</div>
		</div>
	</c:forEach>
</c:if>

<div  class="prod_cart-total">
	<spring:message code="popup.cart.total"/><format:price priceData="${cartData.totalPrice}"/>
</div>

<c:if test="${not empty lightboxBannerComponent && lightboxBannerComponent.visible}">
	<div class="content_slot">
		<cms:component component="${lightboxBannerComponent}"/>
	</div>
</c:if>
<div class="links">
	<a href="${cartUrl}" class="neutral large">
		<spring:theme code="basket.view.basket" />
	</a>
	<a href="${checkoutUrl}" class="positive large">
		<theme:image code="img.addToCartIcon" alt="${addToCartText}" title="${addToCartText}"/>
		<spring:theme code="checkout.checkout" />
	</a>
</div>
 --%>