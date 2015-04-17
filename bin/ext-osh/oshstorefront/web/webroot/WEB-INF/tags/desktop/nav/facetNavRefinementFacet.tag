<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="facetData" required="true"
	type="de.hybris.platform.commerceservices.search.facetdata.FacetData"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<c:if test="${not empty facetData.values}">
	<div>
		<div class="headerlist">
			<a href="#"
				onclick="$(this).closest('div.item').find('div.facetValues').slideToggle();$(this).toggleClass('toggleArrow');return false;"
				title="${toggleFacetText}"> <c:choose>
					<c:when test="${facetData.name eq 'moreWaysToShop'}">
						<spring:theme code="search.nav.moreWays" />
					</c:when>
					<c:otherwise>
						<spring:theme code="search.nav.facetTitle"
							arguments="${facetData.name}" />
					</c:otherwise>
				</c:choose>
			</a> <a href="#"
				onclick="$(this).closest('div.item').find('div.facetValues').slideToggle();$(this).toggleClass('toggleArrow');return false;">
				<span class="dropdown"> <span class="dropdown-img"></span>
			</span>
			</a>
		</div>

		<ycommerce:testId code="facetNav_facet${facetData.name}_links">
			<div class="navlist">

				<c:if test="${not empty facetData.topValues}">
					<div class="topFacetValues">
						<ul>

							<c:forEach items="${facetData.topValues}" var="facetValue">
								<li><c:if test="${facetData.multiSelect}">
										<form action="#" method="get">
											<input type="hidden" name="q"
												value="${facetValue.query.query}" /> <input type="hidden"
												name="text" value="${searchPageData.freeTextSearch}" /> <input
												type="checkbox"
												${facetValue.selected ? 'checked="checked"' : ''}
												onchange="$(this).closest('form').submit()"
												style="margin-right: 3px; overflow: hidden; position: relative; width: 13px;" />
											<label class="subcategorylist"> ${facetValue.name} <span
												class="productcount"> <spring:theme
														code="search.nav.facetValueCount"
														arguments="${facetValue.count}" /></span>
											</label>
										</form>
									</c:if> <c:if test="${not facetData.multiSelect}">
										<c:url value="${facetValue.query.url}"
											var="facetValueQueryUrl" />
										<a
											href="${facetValueQueryUrl}&text=${searchPageData.freeTextSearch}">${facetValue.name}</a>
										<span class="productcount"><spring:theme code="search.nav.facetValueCount"
											arguments="${facetValue.count}" /></span>
									</c:if></li>
							</c:forEach>
						</ul>
						<span class="more"> <a href="#"
							onclick="$(this).closest('div.topFacetValues').hide().siblings('div.allFacetValues').show();return false;"
							style="padding-left: 13px; font-weight: bold;"><spring:theme
									code="search.nav.facetShowMore" /></a>
						</span>
					</div>

				</c:if>
				<div class="allFacetValues"
					style="${not empty facetData.topValues ? 'display:none' : ''}">
					<ul>
						<c:forEach items="${facetData.values}" var="facetValue">
							<li><c:if test="${facetData.multiSelect}">
									<form action="#" method="get">
										<input type="hidden" name="q"
											value="${facetValue.query.query}" /> <input type="hidden"
											name="text" value="${searchPageData.freeTextSearch}" /> <input
											type="checkbox"
											${facetValue.selected ? 'checked="checked"' : ''} /> <label>
											${facetValue.name}&nbsp<span class="productcount"><spring:theme
													code="search.nav.facetValueCount"
													arguments="${facetValue.count}" /></span>
										</label>
									</form>
								</c:if> <c:if test="${not facetData.multiSelect}">
									<c:url value="${facetValue.query.url}" var="facetValueQueryUrl" />
									<a href="${facetValueQueryUrl}">${facetValue.name}</a>
									<spring:theme code="search.nav.facetValueCount"
										arguments="${facetValue.count}" />
								</c:if></li>
						</c:forEach>
					</ul>
					<c:if test="${not empty facetData.topValues}">
						<span class="more"> <a href="#"
							onclick="$(this).closest('div.allFacetValues').hide().siblings('div.topFacetValues').show();return false;"
							style="padding-left: 13px; font-weight: bold;"><spring:theme
									code="search.nav.facetShowLess" /></a>
						</span>
					</c:if>
				</div>
			</div>
		</ycommerce:testId>
	</div>
</c:if>
<script>
	$(document).ready(function() {
		$("div.allFacetValues li input:checkbox").change(function() {
			$(this).closest('form').submit()
		});
	});
</script>