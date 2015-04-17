<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ attribute name="galleryImages" required="true" type="java.util.List"%>
<%@ attribute name="variantProduct" required="true"
	type="de.hybris.platform.commercefacades.product.data.VariantOptionData"%>
<%@ attribute name="summary" required="true"
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
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<spring:theme code="text.addToCart" var="addToCartText" />


<c:set var="showAddToCart" value="${true}" />
<div class="leftproductdetail">
	<div class="product_detail_view">
	<span class="product_image">
								<a href="${galleryImages[0].productDetail.url}"  rel="prettyPhoto">
									<product:productPrimaryImage product="${product}"  format="productDetail"/>
								</a>
							</span>
	</div>
	
	<div class="clearb">
	<c:if test="${not empty galleryImages[0].largeView.url}">
	
		<a class="link_enlarger" title="${product.name}" rel="prettyPhoto"
			href="${galleryImages[0].largeView.url}"><spring:message code="osh.product.page.image.viewlarger" /></a>
			</c:if>
	</div>
	<div class="clearb"></div>
	
	<div class="clearboth martop overflow">
		<div class="product_crousel">
		<c:if test="${not empty galleryImages[1].miniCart.url}">
			<ul id="mycarousel" class="jcarousel-skin-tango"> 
				 <c:forEach items="${galleryImages}" var="container"
					varStatus="varStatus">
					<li><img src="${container.miniCart.url}"
						rel="${container.largeView.url}" 
						mainImage="${container.productDetail.url}" 
						data-primaryimagesrc="${container.product.url}"
						data-galleryposition="${varStatus.index}"
						
						alt="${container.miniCart.altText}"
						title="${container.miniCart.altText}" width="50" height="50" />
					</li>
				</c:forEach> 
				
			</ul> 
</c:if>
		</div>
		
		<c:if test="${not empty product.video}">

		<div class="product_video ">

			<!-- <a class="link_video" href="javascript:void(0);">VIDEO</a> -->
			<a class="link_video" href="${product.video}" target="_blank"><spring:message
					code="osh.product.page.video" /></a>
		</div>
		</c:if>
	</div>
	
	<div class="buyonline" style="margin-top: 16px;">
		<cms:slot var="feature" contentSlot="${slots.PDPBuyOnlineSection}">
			<cms:component component="${feature}" />
		</cms:slot>
	</div>
</div>
<div class="rightproductdetail">
	<h2 class="productname">${variantProduct.name}</h2>
	<div class="product_shortdetail">
		<span class="sku"><spring:theme code="checkout.sku"/>&nbsp;${variantProduct.code}</span>
		 <%-- <span class="modal"><spring:theme code="checkout.model"/> 10293845756</span> --%>
	</div>
	
	<c:choose>
	<c:when test="${variantProduct.mapPriceType}">
			<p class="product_saleprice">*<spring:theme code="add.to.cart.price"/>	</p><span class="strike" style=" color: #D4D4D4;">
							<format:price priceData="${variantProduct.regPriceData}" />
						</span>
	</c:when>
	
	<c:otherwise>
	<c:choose>
		<c:when test="${not empty variantProduct.salePriceData}">
			<p class="product_saleprice">
				<spring:theme code="osh.productListing.page.sale"/>&nbsp;<span><format:fromPrice priceData="${variantProduct.salePriceData}" /></span>
			</p>
			<p class="product_reg_price">
			<spring:theme code="checkout.reg"/> <span><format:fromPrice
					priceData="${variantProduct.regPriceData}" /></span>
			</p>	
		</c:when>
		<c:otherwise>
			<p class="product_saleprice">
				<spring:theme code="basket.page.price"/> &nbsp; <span><format:fromPrice
						priceData="${variantProduct.regPriceData}" /></span>
			</p>
		</c:otherwise>
	</c:choose>
	</c:otherwise>	
</c:choose>

	<div class="shipping_info">
		<c:if test="${variantProduct.newSKU eq true}">
			<span><spring:message
					code="osh.product.page.product.newarrivail" /></span>
		</c:if>
		<c:if test="${variantProduct.freeShipping eq true}">
			<span class="dot_divider">.</span>
			<span><spring:message
					code="osh.product.page.product.freeshipping" /></span>
		</c:if>
	</div>
	 <c:if test="${product.availabilityInd eq 'WEB'}">
			<span class="flagquick"><spring:message
					code="osh.product.page.message.onlineonly" /></span>
		</c:if> 
				<c:if test="${product.availabilityInd eq 'STORE'}">
			<span class="flagquick"><spring:message
					code="osh.product.page.message.instore" /></span>
		</c:if>
		<c:if test="${product.availabilityInd eq 'ALL'}">
			<span class="flagquick"><spring:message
					code="osh.product.page.message.avail" /></span>
		</c:if>
	<div class="p_shortdescription">
	<c:if test="${!empty product.description}">
	    ${summary}
	 </c:if>   
		<a class="moreinfo" href="#contenttab"><spring:message
				code="osh.product.page.product.moreproductinfo" /></a>
		
		<c:if test="${ not empty variantProduct.colour}">	
			<div class="color_option"><spring:theme code="quick.view.popup.color"/>&nbsp;${variantProduct.colour}</div>
		</c:if>
		<c:if test="${ not empty variantProduct.size}">	
		<div class="size_option"><spring:theme code="quick.view.popup.size"/>&nbsp;${variantProduct.size}</div>
		</c:if>
		
		<c:if test="${product.sizeChart eq true}">
			<div class="color_option">
				<a class="link_sizechart btnlink" href="javascript:void(0);"><spring:theme code="text.size.chart"/></a>
			</div>
		</c:if>
	</div>
</div>
<div id="sizechartpopup" style="display: none">
	<cms:slot var="feature" contentSlot="${slots.SizeChart}">
		<cms:component component="${feature}" />
	</cms:slot>
</div>
<%--
		Determine if product is one of apparel style or size variant
		<c:if test="${product.variantType eq 'ApparelStyleVariantProduct'}">
			<c:set var="variantStyles" value="${product.variantOptions}" />
		</c:if>
		<c:if test="${(not empty product.baseOptions[0].options) and (product.baseOptions[0].variantType eq 'ApparelStyleVariantProduct')}">
			<c:set var="variantStyles" value="${product.baseOptions[0].options}" />
			<c:set var="variantSizes" value="${product.variantOptions}" />
			<c:set var="currentStyleUrl" value="${product.url}" />
		</c:if>
		<c:if test="${(not empty product.baseOptions[1].options) and (product.baseOptions[0].variantType eq 'ApparelSizeVariantProduct')}">
			<c:set var="variantStyles" value="${product.baseOptions[1].options}" />
			<c:set var="variantSizes" value="${product.baseOptions[0].options}" />
			<c:set var="currentStyleUrl" value="${product.baseOptions[1].selected.url}" />
		</c:if>
		<c:url value="${currentStyleUrl}" var="currentStyledProductUrl"/>
		
		Determine if product is other variant
		<c:if test="${empty variantStyles}">
			<c:if test="${not empty product.variantOptions}">
				<c:set var="variantOptions" value="${product.variantOptions}" />
			</c:if>
			<c:if test="${not empty product.baseOptions[0].options}">
				<c:set  var="variantOptions" value="${product.baseOptions[0].options}" />
			</c:if>
		</c:if>

		<c:if test="${not empty variantStyles}">
			<c:set var="showAddToCart" value="${false}" />
			<div class="variant_options">
				<div class="colour">
					<p><spring:theme code="product.variants.colour"/></p>
					<ul>
						<c:forEach items="${variantStyles}" var="variantStyle">
							<c:forEach items="${variantStyle.variantOptionQualifiers}" var="variantOptionQualifier">
								<c:if test="${variantOptionQualifier.qualifier eq 'style'}">
									<c:set var="styleValue" value="${variantOptionQualifier.value}" />
									<c:set var="imageData" value="${variantOptionQualifier.image}" />
								</c:if>
							</c:forEach>
							<li <c:if test="${variantStyle.url eq currentStyleUrl}">class="selected"</c:if>>
								<c:url value="${variantStyle.url}" var="productStyleUrl"/>
								<a href="${productStyleUrl}" class="colorVariant" name="${variantStyle.url}">
									<c:if test="${not empty imageData}">
										<img src="${imageData.url}" title="${styleValue}"/>
									</c:if>
									<c:if test="${empty imageData}">
										<span class="swatch_colour_a" title="${styleValue}"></span>
									</c:if>
								</a>
							</li>
						</c:forEach>
					</ul>
				</div>
				<div class="size">
					<form>
						<dl>
							<dt class="left"><label for="Size"><spring:theme code="product.variants.size"/></label></dt>
							<dd class="left">
								<select id="Size">
									<c:if test="${empty variantSizes}">
										<option selected="selected"><spring:theme code="product.variants.select.style"/></option>
									</c:if>
									<c:if test="${not empty variantSizes}">
										<option value="${currentStyledProductUrl}" <c:if test="${empty variantParams['size']}">selected="selected"</c:if>>
											<spring:theme code="product.variants.select.size"/>
										</option>
										<c:forEach items="${variantSizes}" var="variantSize">

											<c:set var="optionsString" value="" />
											<c:forEach items="${variantSize.variantOptionQualifiers}" var="variantOptionQualifier">
												<c:if test="${variantOptionQualifier.qualifier eq 'size'}">
													<c:set var="optionsString">${optionsString} ${variantOptionQualifier.name} ${variantOptionQualifier.value}, </c:set>
												</c:if>
											</c:forEach>

											<c:if test="${(variantSize.stockLevel gt 0) and (variantSize.stockLevelStatus ne 'outOfStock')}">
												<c:set var="stockLevel">${variantSize.stockLevel} <spring:theme code="product.variants.in.stock"/></c:set>
											</c:if>
											<c:if test="${(variantSize.stockLevel le 0) and (variantSize.stockLevelStatus eq 'inStock')}">
												<c:set var="stockLevel"><spring:theme code="product.variants.available"/></c:set>
											</c:if>
											<c:if test="${(variantSize.stockLevel le 0) and (variantSize.stockLevelStatus ne 'inStock')}">
												<c:set var="stockLevel"><spring:theme code="product.variants.out.of.stock"/></c:set>
											</c:if>

											<c:if test="${(variantSize.url eq product.url)}">
												<c:set var="showAddToCart" value="${true}" />
											</c:if>

											<c:url value="${variantSize.url}" var="variantOptionUrl"/>
											<option value="${variantOptionUrl}" ${(variantSize.url eq product.url) ? 'selected="selected"' : ''}>
												${optionsString}&nbsp;<format:price priceData="${variantSize.priceData}"/>&nbsp;&nbsp;${stockLevel}
											</option>
										</c:forEach>
									</c:if>
								</select>
							</dd>
						</dl>
					</form>
					<a href="#"><spring:theme code="product.variants.size.guide"/></a>
				</div>
			</div>
		</c:if>
		
		<c:if test="${not empty variantOptions}">
			<c:set var="showAddToCart" value="${false}" />
			<div class="variant_options">
				<div class="size">
					<select id="variant">
						<option selected="selected"><spring:theme code="product.variants.select.variant"/></option>
						<c:forEach items="${variantOptions}" var="variantOption">
	
							<c:set var="optionsString" value="" />
							<c:forEach items="${variantOption.variantOptionQualifiers}" var="variantOptionQualifier">
								<c:set var="optionsString">${optionsString} ${variantOptionQualifier.name} ${variantOptionQualifier.value}, </c:set>
							</c:forEach>
	
							<c:if test="${(variantOption.stockLevel gt 0) and (variantSize.stockLevelStatus ne 'outOfStock')}">
								<c:set var="stockLevel">${variantOption.stockLevel} <spring:theme code="product.variants.in.stock"/></c:set>
							</c:if>
							<c:if test="${(variantOption.stockLevel le 0) and (variantSize.stockLevelStatus eq 'inStock')}">
								<c:set var="stockLevel"><spring:theme code="product.variants.available"/></c:set>
							</c:if>
							<c:if test="${(variantOption.stockLevel le 0) and (variantSize.stockLevelStatus ne 'inStock')}">
								<c:set var="stockLevel"><spring:theme code="product.variants.out.of.stock"/></c:set>
							</c:if>
	
										
							<c:if test="${variantOption.url eq product.url}">
								<c:set var="showAddToCart" value="${true}" />
							</c:if>
			
							<c:url value="${variantOption.url}" var="variantOptionUrl"/>
							<option value="${variantOptionUrl}" ${(variantOption.url eq product.url) ? 'selected="selected"' : ''}>
								${optionsString}&nbsp;<format:price priceData="${variantOption.priceData}"/>&nbsp;&nbsp;${stockLevel}
							</option>
						</c:forEach>
					</select>
				</div>
			</div> 
		</c:if>--%>

<%-- 
<product:productAddToCartPanel product="${product}"
	allowAddToCart="${showAddToCart}" />

AddThis Button BEGIN
<a style="width: 135px"
	href="http://www.addthis.com/bookmark.php?v=250&amp;pub=xa-4afd47c25121d881"
	class="addthis_button"> <img width="135" height="18"
	style="border: 0pt none;"
	alt="<spring:theme code="product.bookmark.and.share"/>"
	title="<spring:theme code="product.bookmark.and.share"/>"
	src="${commonResourcePath}/images/addthis.gif">
</a> 
<script
	src="http://s7.addthis.com/js/250/addthis_widget.js#pub=xa-4afd47c25121d881"
	type="text/javascript"></script>
	--%>
<%-- AddThis Button END --%>


