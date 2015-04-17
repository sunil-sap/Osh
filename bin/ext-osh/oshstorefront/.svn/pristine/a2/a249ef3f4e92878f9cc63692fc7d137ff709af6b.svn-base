<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageData" required="true" type="de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<c:if test="${not empty pageData.breadcrumbs}">
<div class="selectedfilter">
<c:choose>
<c:when test="${pageData.breadcrumbs[0].facetName eq 'Sale' && fn:length(pageData.breadcrumbs) gt 1 }">
	<div class="headerlist first">	
		<spring:theme code="search.nav.appliedFilters"/>
	</div>
</c:when>
<c:otherwise>
<c:if test="${pageData.breadcrumbs[0].facetName ne 'Sale'}">
	<div class="headerlist first">	
		<spring:theme code="search.nav.appliedFilters"/>
	</div>
</c:if>	
</c:otherwise>	
</c:choose>
	<div class="navlist">
			
			<c:forEach items="${pageData.breadcrumbs}" var="breadcrumb">
				<c:if test="${breadcrumb.facetName ne 'Sale'}">
			<ul>
			<li>						
				<span class="filterlabel">
					<c:url value="${breadcrumb.removeQuery.url}" var="removeQueryUrl"/>
					<a href="${removeQueryUrl}"><spring:theme code="search.nav.appliedFacet" arguments="${breadcrumb.facetName}^${breadcrumb.facetValueName}" argumentSeparator="^"/></a>
					<span class="removefilter">					
						<a href="${removeQueryUrl}">X						
							<spring:theme code="search.nav.removeAttribute" var="removeFacetAttributeText"/>		
						</a>
					</span>
				</span>
				</li></ul>
				</c:if>
			</c:forEach>
		
	</div>
</div>
</c:if>
