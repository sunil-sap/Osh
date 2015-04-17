<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="breadcrumbs" required="true" type="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:url value="/" var="homeUrl"/>

	<span>
		<a href="${homeUrl}"><spring:message code="breadcrumb.home"/></a>
	</span>

	<c:set var="counter" value="0"/>
	<c:forEach items="${breadcrumbs}" var="breadcrumb">&#062;	
	<c:set var="counter">${counter+1}</c:set>
          <span>
			<c:choose>

				 <c:when test="${counter == fn:length(breadcrumbs)}">
					 ${breadcrumb.name}
				</c:when> 
				<c:when test="${breadcrumb.url eq '#'}">&#062;
					 ${breadcrumb.name}
				</c:when>

				<c:otherwise>
					<c:url value="${breadcrumb.url}" var="breadcrumbUrl"/>
					<a href="${breadcrumbUrl}"> ${breadcrumb.name}</a>
				</c:otherwise>

			</c:choose>
			</span>
		
	</c:forEach>
