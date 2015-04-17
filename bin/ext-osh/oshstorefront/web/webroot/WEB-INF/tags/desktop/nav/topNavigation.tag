<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld" %>

<div id="headerNav">
	<ul id="nav" class="dropdown dropdown-linear dropdown-columnar">
		<cms:slot var="component" contentSlot="${slots['NavigationBar']}">
			<cms:component component="${component}"/>
		</cms:slot>
	</ul>
</div>