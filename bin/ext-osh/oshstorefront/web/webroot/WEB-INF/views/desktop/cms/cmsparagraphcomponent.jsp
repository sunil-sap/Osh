<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
<c:when test="${component.uid eq 'ClubOrchardNumberTxt'}">
<span>${content}</span>
</c:when>
<c:otherwise>
<div class="content">${component.content}</div></c:otherwise>
</c:choose>