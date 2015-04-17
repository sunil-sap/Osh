<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json"%>

<c:url value="${oshVariantProduct.url}" var="productUrl" />
<%-- <link rel="stylesheet" type="text/css" media="screen"
	href="${request.contextPath}/_ui/desktop/osh/css/prettyPhoto.css"
	title="prettyPhoto main stylesheet" />
<link rel="stylesheet" type="text/css" media="screen"
	href="${request.contextPath}/_ui/desktop/osh/css/skin.css" />
<link href="media/style/slider/skin.css" media="screen" rel="stylesheet"
	type="text/css" />
<script type="text/javascript"
	src="${request.contextPath}/_ui/desktop/osh/js/jquery.jcarousel.min.js"></script>
<script type="text/javascript"
	src="${request.contextPath}/_ui/desktop/osh/js/jquery.prettyPhoto.js"></script>

<script type="text/javascript">
	/*         */
	//$(document).ready(function() {
	//ACC.product.bindToAddToCartForm();
	//});
	/*   */
</script> --%>
<script type="text/javascript">
	$('#mycarousel').jcarousel({
		scroll : 1
	});
	$('#mycarousel li img').click(
			function() {
				var selectedImage = $(this).attr('rel');
				var selectedMainImage = $(this).attr('mainImage');
				//alert("selectedImage");
				$('.link_enlarger').attr('href', selectedImage);
				$(".product_detail_view a").attr('href', selectedImage);
				
				var img = $("<img />").attr('src', selectedImage);
				$(".product_detail_view a").empty();
				$(".product_detail_view a").append(img);

		/* 		var img = $("<img />").attr('src', selectedImage).load(
						function() {
							if (!this.complete
									|| typeof this.naturalWidth == "undefined"
									|| this.naturalWidth == 0) {
								//alert('broken image!'); 
							} else {
								$(".product_detail_view a").empty();
								$(".product_detail_view a").append(img);
							}
						}); */
			});
	$("a[rel^='prettyPhoto']").prettyPhoto({
		social_tools : ' '
	});
</script>
<!-- <div class="mainmiddleContent">
          	<div class=""> -->
<div class="middlemainheaderbanner" style="margin-top: 50px;">
	<div class="leftproductdetail">
		<div class="product_detail_view">
				<span class="product_image" style="margin-left: 10px;">
								<a href="${productUrl}">
									<product:productPrimaryImage product="${oshProduct}" format="quickView"/>
								</a>
							</span>
		</div>
		<div class="clearb"></div>
		<div class="clearboth martop overflow">
		
		<div class="product_crousel">
		<c:if test="${not empty galleryImages[1].miniCart.url}">
			<ul id="mycarousel" class="jcarousel-skin-tango"> 
				 <c:forEach items="${galleryImages}" var="container"
					varStatus="varStatus">
					<li><img src="${container.miniCart.url}"
						rel="${container.quickView.url}" 
						data-primaryimagesrc="${container.product.url}"
						data-galleryposition="${varStatus.index}"
						alt="${container.miniCart.altText}"
						title="${container.miniCart.altText}" width="50" height="50" />
					</li>
				</c:forEach> 
				
			</ul> 
</c:if>
		</div>
			<c:if test="${not empty oshProduct.video}">
			<div class="product_video">
				<!-- <a class="link_video" href="javascript:void(0);">VIDEO</a> -->
				<a class="link_video" href="${oshProduct.video}" target="_blank"><spring:message
						code="osh.product.page.video" /> </a>
			</div>
			</c:if>
		</div>
	</div>
	<div class="rightproductdetail">
		<h2 class="productname quickView_prodName">
			<a href="${productUrl}">${oshVariantProduct.name}</a>
		</h2>
		<div class="product_shortdetail">
			<span class="sku"><spring:theme code="checkout.sku"/> ${oshVariantProduct.code}</span> 
			</div>
			<c:choose>
				<c:when test="${oshVariantProduct.mapPriceType}">
						<p class="product_saleprice">* <spring:theme code="add.to.cart.price"/>	</p>
						<span class="strike" style=" color: #D4D4D4;">
							<format:price priceData="${oshVariantProduct.regPriceData}" />
						</span>
				</c:when>
				
				<c:otherwise>
				<c:choose>
				 <c:when test="${not empty oshVariantProduct.salePriceData}">
					<p class="product_saleprice">
						<spring:theme code="osh.productListing.page.sale"/>&nbsp;<span><format:fromPrice priceData="${oshVariantProduct.salePriceData}" /></span>
					</p>
					<p class="product_reg_price">
						<spring:theme code="checkout.reg"/> <span><format:fromPrice
							priceData="${oshVariantProduct.regPriceData}" /></span>
					</p>	
				</c:when>
					<c:otherwise>
						<p class="product_saleprice">
							<spring:theme code="basket.page.price"/> &nbsp; <span><format:fromPrice
									priceData="${oshVariantProduct.regPriceData}" /></span>
						</p>
					</c:otherwise>
				</c:choose>
			</c:otherwise>	
			</c:choose>


		<%-- <c:if test="${not empty oshVariantProduct.salePriceData }">
		<p class="product_reg_price">
			Reg. <span><format:fromPrice
					priceData="${oshVariantProduct.regPriceData}" /></span>
		</p>
		</c:if> --%>
		<div class="shipping_info">
			<c:if test="${oshVariantProduct.newSKU}">
				<span><spring:theme code="osh.product.page.product.newarrivail"/></span>
			</c:if>
			
			<c:if test="${oshVariantProduct.freeShipping}">
			<span class="dot_divider">.</span>
				<span><spring:message
					code="osh.product.page.product.freeshipping" /></span>
			</c:if>
			
			<%-- <c:if test="${oshProduct.availabilityInd eq 'W'}">
			<span class="dot_divider">.</span>
			<span><spring:message
					code="osh.product.page.message.onlineonly" /></span>
		</c:if> --%>
		<%-- <c:if test="${oshProduct.availabilityInd eq 'S'}">
			<span class="dot_divider">.</span>
			<span><spring:message
					code="osh.product.page.message.instore" /></span>
		</c:if> --%>
		</div>
		
		 <c:if test="${oshProduct.availabilityInd eq 'WEB'}">
				<span class="flagquick"><spring:message
					code="osh.product.page.message.onlineonly" /></span>
			</c:if> 
			<c:if test="${oshProduct.availabilityInd eq 'STORE'}">
				<span class="flagquick"><spring:message
					code="osh.product.page.message.instore" /></span>
			</c:if>
			<c:if test="${oshProduct.availabilityInd eq 'ALL'}">
				<span class="flagquick"><spring:message
						code="osh.product.page.message.avail" /></span>
			</c:if>
		<div class="p_shortdescription">
		<%-- 	<div class="restrictiion_areas"><spring:theme code="osh.product.page.product.availablity"/></div> --%>
				<c:if test="${not empty oshVariantProduct.colour}">
			<div class="color_option"><spring:theme code="quick.view.popup.color"/> ${oshVariantProduct.colour}</div>
			</c:if>
			<c:if test="${not empty oshVariantProduct.size}">
			<div class="size_option"><spring:theme code="quick.view.popup.size"/> ${oshVariantProduct.size}</div>
			</c:if>
			<%-- <c:if test="${oshProduct.sizeChart eq true}">
				<div class="color_option">

					<a class="link_sizechart btnlink" href="javascript:void(0);">Size
						Chart</a>
				</div>
			</c:if> --%>
			<%-- <a class="moreinfo"
				href="${request.contextPath}/p/${oshProduct.code}">View Product
				Details</a> --%>
		</div>
	</div>
	<%-- <div id="sizechartpopup" style="display: none">
		<img alt="size chart" src="${request.contextPath}/_ui/desktop/osh/images/home/sizechart.jpg">
	</div> --%>
	<c:set var="variantProduct" value="${oshVariantProduct}"></c:set>
	<c:set var="styleClass" value="left_cart"></c:set>
	<product:productAddToCartQuickView product="${oshProduct}" variantProduct="${variantProduct}"
		styleClass="${styleClass}" />
		
		</div>
	<!-- 	</div>
			</div> -->