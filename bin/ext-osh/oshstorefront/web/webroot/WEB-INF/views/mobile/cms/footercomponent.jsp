<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:forEach items="${navigationNodes}" var="node">
	<c:forEach items="${node.links}" step="${component.wrapAfter}" varStatus="i">
		<c:forEach items="${node.links}" var="childlink" begin="${i.index}" end="${i.index + component.wrapAfter - 1}">
			<li><component:cmsLinkComponent component="${childlink}" /></li>
		</c:forEach>
	</c:forEach>
</c:forEach>
