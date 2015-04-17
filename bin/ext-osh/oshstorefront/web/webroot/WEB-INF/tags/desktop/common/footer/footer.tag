<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld" %>

<cms:slot var="feature" contentSlot="${slots['FooterSection']}">
	<cms:component component="${feature}"/>
</cms:slot>
