<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>

<c:if test="${not empty productReferences}">
	<div class="recommendation_box">
		<div class="headerlist">Recommendations</div>
		<div class="recommendation_innerwrap">
			<c:if test="${component.maximumNumberProducts > 0}">
				<c:forEach end="${component.maximumNumberProducts}"
					items="${productReferences}" var="productReference">
					<c:url value="${productReference.target.url}"
						var="productQuickViewUrl" />
					<div class="product">
						<div class="thumbwrap">
							<product:productPrimaryImage product="${productReference.target}"
								format="cartPage" />
						</div>
						<c:if test="${component.displayProductTitles}">
							<div class="productname">
								<a href="${productQuickViewUrl}">${productReference.target.name}</a>
							</div>
						</c:if>
						<c:if test="${component.displayProductPrices}">
							<div class="amount">
								<format:fromPrice priceData="${productReference.target.price}" />
							</div>
						</c:if>
					</div>
				</c:forEach>
			</c:if>
		</div>
	</div>
</c:if>





