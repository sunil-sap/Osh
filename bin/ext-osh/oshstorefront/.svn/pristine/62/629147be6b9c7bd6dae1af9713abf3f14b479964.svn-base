<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="searchUrl" required="true" %>
<%@ attribute name="searchPageData" required="true" type="de.hybris.platform.commerceservices.search.pagedata.SearchPageData" %>
<%@ attribute name="top" required="true" type="java.lang.Boolean" %>
<%@ attribute name="supportShowAll" required="true" type="java.lang.Boolean" %>
<%@ attribute name="requestShowAll" required="true" type="java.lang.Boolean" %>
<%@ attribute name="maxPageLimit" required="true" type="java.lang.Integer" %>
<%@ attribute name="msgKey" required="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>


<script type="text/javascript">
	/*<![CDATA[*/
		$(document).ready(function() {
			$('#sort_form1').change(function() {
				$('#sort_form1').submit();
			});
			$('#sort_form2').change(function() {
				$('#sort_form2').submit();
			});
		});
	/*]]>*/
</script>

<c:set var="themeMsgKey" value="${not empty msgKey ? msgKey : 'search.page'}"/>

<div class="prod_refine">
	<ycommerce:testId code="searchResults_productsFound_label">
		<p class="strong"><spring:theme code="${themeMsgKey}.totalResults" arguments="${searchPageData.pagination.totalNumberOfResults}"/></p>
	</ycommerce:testId>
	<c:if test="${not empty searchPageData.sorts}">
		<form id="sort_form${top ? '1' : '2'}" name="sort_form${top ? '1' : '2'}" method="get" action="#">
			<label for="sortOptions${top ? '1' : '2'}"><spring:theme code="${themeMsgKey}.sortTitle"/></label>
			<select id="sortOptions${top ? '1' : '2'}" name="sort" class="sortOptions">
				<c:forEach items="${searchPageData.sorts}" var="sort">
					<option class="orderDrop" value="${sort.code}" ${sort.selected ? 'selected="selected"' : ''}>
						<c:choose>
							<c:when test="${not empty sort.name}">
								${sort.name}
							</c:when>
							<c:otherwise>
								<spring:theme code="${themeMsgKey}.sort.${sort.code}"/>
							</c:otherwise>
						</c:choose>
					</option>
				</c:forEach>
			</select>
			<c:catch var="errorException">
				<spring:eval expression="searchPageData.currentQuery.query" var="dummyVar"/><%-- This will throw an exception is it is not supported --%>
				<input type="hidden" name="q" value="${searchPageData.currentQuery.query}"/>
			</c:catch>
		</form>
	</c:if>
	<c:set var="showAllLinkVisible" value="${not requestShowAll && searchPageData.pagination.totalNumberOfResults > searchPageData.pagination.pageSize}"   />
	<c:set  var="showPageLinkVisible" value="${requestShowAll && not supportShowAll && (searchPageData.pagination.numberOfPages != 1 || searchPageData.pagination.pageSize == 100)}" />
	
		<c:if test="${showAllLinkVisible}">
			<spring:url value="${searchUrl}" var="showAllUrl">
				<spring:param name="show" value="All" />
			</spring:url>
			<ycommerce:testId code="searchResults_showAll_link">
				<a href="${showAllUrl}"><spring:theme
						code="${themeMsgKey}.showAllResults" />
				</a>
			</ycommerce:testId>
		</c:if>

		<c:if test="${showPageLinkVisible}">
			<spring:url value="${searchUrl}" var="showPageUrl">
				<spring:param name="show" value="Page" />
			</spring:url>
			<ycommerce:testId code="searchResults_showPage_link">
				<a href="${showPageUrl}"><spring:theme
						code="${themeMsgKey}.showPageResults" />
				</a>
			</ycommerce:testId>
		</c:if>

	<c:if test="${(searchPageData.pagination.numberOfPages != 1)}">
	<ul class="pager">
		<li>
			<c:set var="hasPreviousPage" value="${searchPageData.pagination.currentPage > 0}"/>
			<c:if test="${hasPreviousPage}">
				<spring:url value="${searchUrl}" var="previousPageUrl">
					<spring:param name="page" value="${searchPageData.pagination.currentPage - 1}"/>
				</spring:url>
				<ycommerce:testId code="searchResults_previousPage_link">
					<a href="${previousPageUrl}">
						<spring:theme code="${themeMsgKey}.linkPreviousPage"/>
					</a>
				</ycommerce:testId>
			</c:if>
			<c:if test="${not hasPreviousPage}">
				<a href="#" class="hidden" onclick="return false">
					<spring:theme code="${themeMsgKey}.linkPreviousPage"/>
				</a>
			</c:if>
		</li>
		<li><p><spring:theme code="${themeMsgKey}.currentPage" arguments="${searchPageData.pagination.currentPage + 1},${searchPageData.pagination.numberOfPages}"/></p></li>
		<li>
			<c:set var="hasNextPage" value="${(searchPageData.pagination.currentPage + 1) < searchPageData.pagination.numberOfPages}"/>
			<c:if test="${hasNextPage}">
				<spring:url value="${searchUrl}" var="nextPageUrl">
					<spring:param name="page" value="${searchPageData.pagination.currentPage + 1}"/>
				</spring:url>
				<ycommerce:testId code="searchResults_nextPage_link">
					<a href="${nextPageUrl}">
						<spring:theme code="${themeMsgKey}.linkNextPage"/>
					</a>
				</ycommerce:testId>
			</c:if>
			<c:if test="${not hasNextPage}">
				<a href="#" class="hidden" onclick="return false">
					<spring:theme code="${themeMsgKey}.linkNextPage"/>
				</a>
			</c:if>
		</li>
	</ul>
	</c:if>
</div>
