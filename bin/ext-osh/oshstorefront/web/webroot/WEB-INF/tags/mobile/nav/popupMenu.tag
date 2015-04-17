<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div style="display:none;"
	id="popupMenu"
	data-options='{"mode":"blank","headerText":"<spring:theme code="text.header.menu" text="Menu"/>","headerClose":true,"blankContent":true,"themeDialog":"f"}'>
	<ul data-role="listview" data-inset="true" id="menulist">
		<cms:slot var="component" contentSlot="${slots['NavigationBar']}">
			<cms:component component="${component}" />
		</cms:slot>
	</ul>
</div>
