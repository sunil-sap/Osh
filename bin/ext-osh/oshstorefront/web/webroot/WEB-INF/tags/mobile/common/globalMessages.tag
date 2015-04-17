<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%-- Information (confirmation) messages --%>
<c:if test="${not empty accConfMsgs}">
	<div id="accConfMsgs" style="display: none;" data-headertext='<spring:theme code="text.headertext.conf"/>'>
		<ul class='success mFormList'>
			<c:forEach items="${accConfMsgs}" var="confMsg">
				<li><spring:theme code="${confMsg}" /></li>
			</c:forEach>
		</ul>
	</div>
</c:if>
<%-- Warning messages --%>
<c:if test="${not empty accInfoMsgs}">
	<div id="accInfoMsgs" style="display: none;" data-headertext='<spring:theme code="text.headertext.info"/>'>
		<ul class="info mFormList">
			<c:forEach items="${accInfoMsgs}" var="infoMsg">
				<li><spring:theme code="${infoMsg}" /></li>
			</c:forEach>
		</ul>
	</div>
</c:if>
