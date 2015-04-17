<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/mobile/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:if test="${not empty productReferences}">
	<c:if test="${component.maximumNumberProducts > 0}">
		<div data-role="collapsible" data-theme="e" data-content-theme="c" data-collapsed="false">
			<h3><spring:theme code="product.related.products" /></h3>
			<div id="referenceGallery">
				<c:forEach end="${component.maximumNumberProducts}" items="${productReferences}" var="productReference" varStatus="varStatus">
					<c:url value="${productReference.target.url}/quickView" var="productQuickViewUrl" />
					<c:if test="${varStatus.index % 2 == 0}">
					<div class="referencedSlide"></c:if>
						<div class="thumb">
							<a href="#" class="referencedProduct" data-url="${productQuickViewUrl}" data-rel="dialog" data-transition="pop">
								<div class="productTitle">${productReference.target.name}</div>
								<product:productPrimaryImage product="${productReference.target}" format="thumbnail" zoomable="false" />
							</a>
							<div class='referencePrice'>
								<format:fromPrice priceData="${productReference.target.price}" />
							</div>
						</div>
					<c:if test="${(varStatus.last) || (varStatus.index % 2 == 1)}">
					</div></c:if>
				</c:forEach>
			</div>
			<div id="refDots">
				<c:if test="${fn:length(productReferences) gt 2}">
					<c:forEach end="${component.maximumNumberProducts}" items="${productReferences}" var="productReference" varStatus="varStatus">
						<c:if test="${(varStatus.index % 2 == 0) && (varStatus.index < 2)}">
							<span><img src='${commonResourcePath}/images/closeddot.png' /></span>
						</c:if>
						<c:if test="${(varStatus.index % 2 == 0) && (varStatus.index >= 2)}">
							<span><img src='${commonResourcePath}/images/opendot.png' /></span>
						</c:if>
					</c:forEach>
				</c:if>
			</div>
		</div>
	</c:if>
</c:if>
