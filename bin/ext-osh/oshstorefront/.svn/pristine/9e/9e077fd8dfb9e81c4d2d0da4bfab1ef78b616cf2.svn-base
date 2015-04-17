<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>
<c:if test="${empty component.navigationNode.children}">
	<ul data-role="listview" data-inset="true" data-theme="e">
		<c:set value="${component.styleClass} ${dropDownLayout}" var="bannerClasses" />
		<li class="La ${bannerClasses}"><component:cmsLinkComponent component="${component.link}" /></li>
	</ul>
</c:if>
<c:if test="${not empty component.navigationNode.children}">
	<div data-role="collapsible" class="navigationNodeParent">
		<h3 class="subcategory-headline">${component.navigationNode.title}</h3>
		<ul data-role="listview" data-inset="true" data-theme="d">
			<c:forEach items="${component.navigationNode.children}" var="child">
				<c:forEach items="${child.links}" var="childlink">
					<li class="navigationNodeChild"><component:cmsLinkComponent component="${childlink}" /></li>
				</c:forEach>
			</c:forEach>
		</ul>
	</div>
</c:if>
