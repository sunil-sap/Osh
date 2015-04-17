<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="searchUrl" required="true"%>
<%@ attribute name="pageUrl" required="true" type="java.lang.String"%>
<%@ attribute name="searchPageData" required="true"
	type="de.hybris.platform.commerceservices.search.pagedata.SearchPageData"%>
<%@ attribute name="top" required="true" type="java.lang.Boolean"%>
<%@ attribute name="supportShowAll" required="true"
	type="java.lang.Boolean"%>
<%@ attribute name="requestShowAll" required="true"
	type="java.lang.Boolean"%>
<%@ attribute name="maxPageLimit" required="true"
	type="java.lang.Integer"%>
<%@ attribute name="msgKey" required="false"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>



<ul>
	<li class="countsummary">
		<c:if test="${searchPageData.pagination.totalNumberOfResults eq 0}">
			<spring:theme code="searchPageData.pagination.pageSize" argumentSeparator="^" arguments="${0}-${0}^${searchPageData.pagination.totalNumberOfResults}"/></span></li>
		</c:if>
		<c:choose>
			<c:when test="${(searchPageData.pagination.pageSize)*(searchPageData.pagination.currentPage+1)>(searchPageData.pagination.totalNumberOfResults)}">
					<c:if test="${searchPageData.pagination.currentPage gt 0}">
						<span><spring:theme code="searchPageData.pagination.pageSize" argumentSeparator="^" arguments="${(searchPageData.pagination.currentPage)*((searchPageData.pagination.pageSize)+1)}-${searchPageData.pagination.totalNumberOfResults}^${searchPageData.pagination.totalNumberOfResults}"/></span>
					</c:if>
					<c:if test="${searchPageData.pagination.currentPage eq 0}">
						<span><spring:theme code="searchPageData.pagination.pageSize" argumentSeparator="^" arguments="${searchPageData.pagination.currentPage+1}-${searchPageData.pagination.totalNumberOfResults}^${searchPageData.pagination.totalNumberOfResults}"/></span>
					</c:if>
				</li>
			</c:when>	
			<c:otherwise>
			<span><spring:theme code="searchPageData.pagination.pageSize" argumentSeparator="^" arguments="${(searchPageData.pagination.currentPage)*(searchPageData.pagination.pageSize)+1}-${(searchPageData.pagination.pageSize)*(searchPageData.pagination.currentPage+1)}^${searchPageData.pagination.totalNumberOfResults}"/></span></li>
			</c:otherwise>
		</c:choose>		
		<li class="viewnumber" style="width: 170px;">
			<c:if test="${searchPageData.pagination.totalNumberOfResults > 0}"> 
		 		<c:forEach begin="1" end="3" var="index" varStatus="status">
					<span>
						<c:set var="themeMsgKey" value="${not empty msgKey ? msgKey : 'search.page'}" /> 
						<c:set var="showAllLinkVisible" value="${not requestShowAll && searchPageData.pagination.totalNumberOfResults > searchPageData.pagination.pageSize}" />
						<c:if test="${status.count<=3}">
						<c:if test="${pageType eq 'ProductSearch'}">
							<spring:url value="${searchUrl}" var="showAllUrl">
								<c:if test="${status.count lt 3 }">
									<spring:param name="showItem" value="${index * defaultPageSize}" />
								</c:if>
								<c:if test="${status.count eq 3 }">
									<spring:param name="showItem" value="${index * defaultPageSize +24}" />
								</c:if>
							</spring:url>
						</c:if>
							<c:if test="${pageType eq 'Category'}">
							<spring:url value="${searchUrl}:prodlist:true" var="showAllUrl">
								<c:if test="${status.count lt 3 }">
									<spring:param name="showItem" value="${index * defaultPageSize}" />
								</c:if>
								<c:if test="${status.count eq 3 }">
									<spring:param name="showItem" value="${index * defaultPageSize +24}" />
								</c:if>
							</spring:url>
							</c:if>
							<ycommerce:testId code="searchResults_showAll_link">
								<a href="${showAllUrl}" class="pageCount"><c:if test="${status.count eq 3}"> <span class="<c:if test="${(searchPageData.pagination.pageSize eq 96)}">selected96</c:if>">${index * defaultPageSize+24}</span></c:if><c:if test="${status.count eq 2}">
								<span class="<c:if test="${(searchPageData.pagination.pageSize eq 48)}">selected48</c:if>"> ${index * defaultPageSize}</span></c:if><c:if test="${(status.count eq 1)}"><span class="<c:if test="${searchPageData.pagination.pageSize eq 24}">selected24</c:if>"> ${index * defaultPageSize}</span></c:if></a>
							</ycommerce:testId>
						</c:if> 
					</span> 
		  		 </c:forEach>
		 	</c:if>
		 
		 	<span> 
		 		<c:set var="themeMsgKey" value="${not empty msgKey ? msgKey : 'search.page'}" /> 
				<c:set var="showAllLinkVisible" value="${not requestShowAll && searchPageData.pagination.totalNumberOfResults > searchPageData.pagination.pageSize}" />
				<c:set var="showPageLinkVisible" value="${requestShowAll && not supportShowAll && (searchPageData.pagination.numberOfPages != 1 || searchPageData.pagination.pageSize == 100)}" />
					
			<!-- Dont remove following commented code. Commented out temporarily -->
	<%-- 			<c:if test="${showAllLinkVisible}">
					<c:if test="${pageType eq 'ProductSearch'}">
					<spring:url value="${searchUrl}" var="showAllUrl">
						<spring:param name="show" value="All" />
					</spring:url>
					</c:if>
					<c:if test="${pageType eq 'Category'}">
					<spring:url value="${searchUrl}:prodlist:true" var="showAllUrl">
						<spring:param name="show" value="All" />
					</spring:url>
					</c:if>
					<ycommerce:testId code="searchResults_showAll_link">
						<a href="${showAllUrl}"><spring:theme code="${themeMsgKey}.showAllResults" /> </a>
					</ycommerce:testId>
				</c:if> --%>
			
	
			</span>
		</li>
		<c:set var="string1" value="${pageUrl}"/>
		<c:set var="string2" value="${fn:split(string1,'&')}" />
		<c:set value="${string2[1]}" var="s3" />
		<c:set var="string3" value="${fn:split(s3,'=')}" />
		<c:set value="${string3[1]}" var="s4" />
	
		<c:if test="${not empty searchPageData.sorts}">
			<li class="sortby">
				<form id="sort_form${top ? '1' : '2'}" name="sort_form${top ? '1' : '2'}" method="get" action="#">
			 <label for="sortOptions${top ? '1' : '2'}"><spring:theme code="${themeMsgKey}.sortTitle"/></label>
			<%-- <label for="sortOptions${top ? '1' : '2'}"></label> --%>
					<select id="sortOptions${top ? '1' : '2'}" name="sort" class="sortOptions">
						<c:forEach items="${searchPageData.sorts}" var="sort">
							<option value="${sort.code}" ${sort.selected ? 'selected="selected"' : ''}>
								<c:choose>
									<c:when test="${not empty sort.name}">${sort.name}</c:when>
									<c:otherwise>
										<spring:theme code="${themeMsgKey}.sort.${sort.code}"/>
									</c:otherwise>
								</c:choose>
							</option>
						</c:forEach>
					</select>
					<c:catch var="errorException">
						<spring:eval expression="searchPageData.currentQuery.query" var="dummyVar"/><%-- This will throw an exception is it is not supported --%>
						<c:choose>
							<c:when test="${fn:contains(pageUrl, 'showItem')}">
								<c:if test="${pageType eq 'Category'}">
									<input type="hidden" name="q" value="${searchPageData.currentQuery.query}:prodlist:true&showItem=${s4}"/>
								</c:if>
								<c:if test="${pageType eq 'ProductSearch'}">
									<input type="hidden" name="q" value="${searchPageData.currentQuery.query}&showItem=${s4}"/>
								</c:if>
							</c:when>
							<c:otherwise>
								<c:if test="${pageType eq 'ProductSearch'}">
									<input type="hidden" name="q" value="${searchPageData.currentQuery.query}"/>
								</c:if>
								<c:if test="${pageType eq 'Category'}">
									<input type="hidden" name="q" value="${searchPageData.currentQuery.query}:prodlist:true"/>
								</c:if>
							</c:otherwise>
						</c:choose>
					</c:catch>
				</form>
			</li>
		</c:if>	
		<c:if test="${(searchPageData.pagination.numberOfPages != 1)}">
			<li class="pagination">
			<span>
			 	<c:set var="hasPreviousPage" value="${searchPageData.pagination.currentPage > 0}" />
			    <c:if test="${hasPreviousPage}">
					 <c:choose>
						 <c:when test="${not empty pageUrl}">
						<spring:url value="${pageUrl}" var="previousPageUrl">
							<spring:param name="page" value="${searchPageData.pagination.currentPage - 1}" />
						</spring:url>
						</c:when>
						 <c:otherwise>
							<spring:url value="${searchUrl}" var="previousPageUrl">
								<spring:param name="page" value="${searchPageData.pagination.currentPage - 1}" />
							</spring:url>
						</c:otherwise>
					</c:choose>
					<c:if test="${searchPageData.pagination.numberOfPages >4}">
						<ycommerce:testId code="searchResults_previousPage_link">
							<a href="${previousPageUrl}"> 
								<img src="${request.contextPath}/_ui/desktop/osh/images/prev_arrow_icon.png" alt="" />
							</a>
						</ycommerce:testId>
					</c:if>
				</c:if>  
			<span> 
				<c:if test="${fn:length(pagination)>0 and searchPageData.pagination.numberOfPages gt 4}">
					<c:forEach items="${pagination}" var="pageData" varStatus="index" end="${searchPageData.pagination.numberOfPages}">
						<c:choose>
						 <c:when test="${not empty pageUrl}">
							<spring:url value="${pageUrl}" var="nextPageUrl">
								<spring:param name="page" value="${pageData.value}" />
							</spring:url>
						</c:when>
						 <c:otherwise>
							<spring:url value="${searchUrl}" var="nextPageUrl">
								<spring:param name="page" value="${pageData.value}" />
							</spring:url>
						</c:otherwise>	
						</c:choose>
						<a href="${nextPageUrl}" class="<c:if test="${pageData.value eq searchPageData.pagination.currentPage}">selected</c:if>"><spring:theme code="${pageData.key}" /></a>
					</c:forEach> 
				</c:if>
				<c:if test="${searchPageData.pagination.numberOfPages!=0 and searchPageData.pagination.numberOfPages le 4}">
					<c:forEach begin="1" end="${searchPageData.pagination.numberOfPages}" varStatus="subPage">
						 <c:choose>
						 <c:when test="${not empty pageUrl}">
							<spring:url value="${pageUrl}" var="nextPageUrl">
								<spring:param name="page" value="${subPage.index-1}" />
							</spring:url>
						</c:when>
						<c:otherwise>
							<spring:url value="${searchUrl}" var="nextPageUrl">
								<spring:param name="page" value="${subPage.index-1}" />
							</spring:url>
						</c:otherwise>
						</c:choose>
						<a href="${nextPageUrl}" class="<c:if test="${subPage.count-1 eq searchPageData.pagination.currentPage}">selected</c:if>">${subPage.count}</a>
					</c:forEach>
				</c:if>
				<c:set var="hasNextPage" value="${(searchPageData.pagination.currentPage + 1) < searchPageData.pagination.numberOfPages}" />
				<c:if test="${hasNextPage}">
			 		<c:choose>
						 <c:when test="${not empty pageUrl}">
							<spring:url value="${pageUrl}" var="nextPageUrl">
								<spring:param name="page" value="${searchPageData.pagination.currentPage + 1}" />
							</spring:url>
						</c:when>
						<c:otherwise>
							<spring:url value="${searchUrl}" var="nextPageUrl">
								<spring:param name="page" value="${searchPageData.pagination.currentPage + 1}" />
							</spring:url>
						</c:otherwise>
					</c:choose>
					<c:if test="${searchPageData.pagination.numberOfPages >4}">
						<ycommerce:testId code="searchResults_nextPage_link">
							<a href="${nextPageUrl}">
								<img src="${request.contextPath}/_ui/desktop/osh/images/next_arrow_icon.png" alt="" />
							</a>
						</ycommerce:testId>
					</c:if>
				</c:if>
				 <c:if test="${not hasNextPage && (searchPageData.pagination.numberOfPages >4)}">
					<a href="#" class="hidden" onclick="return false"> <img
						src="${request.contextPath}/_ui/desktop/osh/images/next_arrow_icon.png"
						alt="" />
					</a>
				</c:if>
			</span>
		</span>
	</li>
</c:if>

</ul>
<script type="text/javascript">
	/*         */
	$(document).ready(function() {
		$('#sort_form1').change(function() {
			$('#sort_form1').submit();
		});
		$('#sort_form2').change(function() {
			$('#sort_form2').submit();
		});
	});
	/*   */
</script>

