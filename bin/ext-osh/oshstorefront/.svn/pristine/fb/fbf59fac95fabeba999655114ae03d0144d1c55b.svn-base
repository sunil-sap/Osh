<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="storeSearchPageData" required="false" type="de.hybris.platform.acceleratorservices.storefinder.data.StoreFinderSearchPageData"%>
<%@ attribute name="locationQuery" required="false" type="java.lang.String"%>
<%@ attribute name="showMoreUrl" required="false" type="java.lang.String"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="store" tagdir="/WEB-INF/tags/mobile/store"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:if test="${storeSearchPageData ne null and not empty storeSearchPageData.results}">
	<ul data-role="listview" data-split-theme="b" data-theme="e" data-content-theme="c">
		<li data-role="list-divider" role="heading">
			<div class="ui-grid-a">
				<div class="ui-block-a" style="width: 63%">
					<h2>
						<spring:theme code="storeFinder.table.store"/>
					</h2>
				</div>
			</div>
		</li>
		<li>
			<div class="ui-grid-a">
				<spring:theme code="storelocator.search.totalResults" arguments="${fn:length(storeSearchPageData.results)}"/>
			</div>
		</li>
		<c:forEach items="${storeSearchPageData.results}" var="pos" varStatus="row">
			<c:url value="${pos.url}" var="posUrl"/>
			<li>
				<div class="ui-grid-b">
					<div class="ui-block-a" style="width: 10%">
						<img class="mapMarker" data-index="${row.count}"/>
					</div>
					<div class="ui-block-b" style="width: 70%">
						<a href="${posUrl}" data-transition="slide"><span class="storeName">${pos.name}</span></a>
					</div>
					<div class="ui-block-c" style="width: 20%">
						<span>${pos.formattedDistance}</span>
					</div>
				</div>
				<div class="ui-grid-a">
					<div class="ui-block-a" style="width: 10%"></div>
					<div class="ui-block-b" style="width: 90%">
						<ycommerce:testId code="storeFinder_result_address_label">
							<c:if test="${not empty pos.address}">
								<div class="ui-grid-a">
									<span>${pos.address.line1}</span><span>${pos.address.line2}</span>
								</div>
								<div class="ui-grid-a">
									<span>${pos.address.town}</span><span>${pos.address.postalCode}</span>
								</div>
							</c:if>
						</ycommerce:testId>
					</div>
				</div>
				<div class="ui-grid-a">
					<div class="ui-block-a" style="width: 10%"></div>
					<div class="ui-block-b">
						<span class="stores-tel"><a href="tel:${pos.address.phone}">${pos.address.phone}</a></span>
					</div>
				</div>
			</li>
		</c:forEach>
		<c:if test="${not empty showMoreUrl}">
			<c:url value="${showMoreUrl}" var="showMoreLink"/>
			<li>
				<div class="storeFinderShowMore">
					<a href="${showMoreLink}" data-role="button" data-theme="c"><spring:theme code="storeFinder.see.more"/></a>
				</div>
			</li>
		</c:if>
	</ul>
</c:if>
