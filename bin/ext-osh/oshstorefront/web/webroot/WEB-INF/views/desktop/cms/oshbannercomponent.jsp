<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/theme" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="${not empty page ? page.label : urlLink}" var="encodedUrl" />
<c:choose>
		<c:when test="${empty encodedUrl || encodedUrl eq '#'}">	
			<div class="title">
				<h2>${headline}</h2>
			
			</div>
			<div>
				<img title="${headline}" alt="${media.altText}" src="${media.url}">
			</div>
			<div>
				<p>${content}</p>
			</div>
			<div>
				<theme:image code="img.iconArrowCategoryTile" alt="${media.altText}"/>
			</div>
			
		</c:when>
		<c:otherwise>
		<a href="${encodedUrl}">
			<div>
				<h2>${headline}</h2>
			</div>
			<div>
					<img title="${headline}" alt="${media.altText}" src="${media.url}">
			</div>
			<div>
					<p>${content}</p>
		<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/theme" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="${not empty page ? page.label : urlLink}" var="encodedUrl" />
<c:choose>
		<c:when test="${empty encodedUrl || encodedUrl eq '#'}">	
			<div class="title">
				<h2>${headline}</h2>
			</div>
			<div>
				<img title="${headline}" alt="${media.altText}" src="${media.url}">
			</div>
			<div>
				<p>${content}</p>
			</div>
			<div>
				<theme:image code="img.iconArrowCategoryTile" alt="${media.altText}"/>
			</div>
			
		</c:when>
		<c:otherwise>
		<a href="${encodedUrl}">
			<div>
				<h2>${headline}</h2>
			</div>
			<div>
					<img title="${headline}" alt="${media.altText}" src="${media.url}">
			</div>
			<div>
					<p>${content}</p>
			</div>
			<div>
					<theme:image code="img.iconArrowCategoryTile" alt="${media.altText}"/>
			</div>
		</a>	
		</c:otherwise>
	</c:choose>

			</div>
			<div>
					<theme:image code="img.iconArrowCategoryTile" alt="${media.altText}"/>
			</div>
		</a>	
		</c:otherwise>
	</c:choose>

