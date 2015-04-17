<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<template:page pageTitle="${pageTitle}">
	<div id="globalMessages">
		<common:globalMessages />
	</div>
	<div class="span-24">
		<div class="span-20 last">
			<div data-role="collapsible-set" data-theme="d">
				<ul data-role="listview" data-inset="true" data-split-theme="b" data-theme="d">
					<cms:slot var="feature" contentSlot="${slots['Section1']}"><cms:component component="${feature}" /></cms:slot>
					<cms:slot var="feature" contentSlot="${slots['Section2']}"><cms:component component="${feature}" /></cms:slot>
					<cms:slot var="feature" contentSlot="${slots['Section3']}"><cms:component component="${feature}" /></cms:slot>
					<cms:slot var="feature" contentSlot="${slots['Section4']}"><cms:component component="${feature}" /></cms:slot>
				</ul>
			</div>
		</div>
	</div>
</template:page>
