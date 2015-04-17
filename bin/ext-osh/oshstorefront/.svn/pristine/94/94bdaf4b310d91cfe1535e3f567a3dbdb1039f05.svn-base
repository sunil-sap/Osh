<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="${component.url}" var="encodedUrl" />
<span>
<c:choose>
<c:when test="${not empty component.content}">${component.content} </c:when>
<c:when test="${component.style}">
<span class="dot">|</span>
<a href="${encodedUrl}" title="${component.linkName}" ${component.target == null || component.target == 'SAMEWINDOW' ? '' : 'target="_new"'}>${component.linkName}</a>
 </c:when>
 <c:when test="${component.uid eq 'YourAccountLink' || component.uid eq 'LogoutLink'|| component.uid eq 'ChangeStoreLink'}">
<a  href="${encodedUrl}" title="${component.linkName}" ${component.target == null || component.target == 'SAMEWINDOW' ? '' : 'target="_new"'}  style="font-style: italic; padding-left: 8px; color: rgb(255, 255, 255); text-decoration: underline;">${component.linkName}</a>
 </c:when> 
<c:otherwise> 
<a href="${encodedUrl}" title="${component.linkName}" ${component.target == null || component.target == 'SAMEWINDOW' ? '' : 'target="_new"'}>${component.linkName}</a>
</c:otherwise>
</c:choose>
</span>

