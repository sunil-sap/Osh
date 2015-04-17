<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/mobile/common"%>
<template:page pageTitle="${pageTitle}">
	<div id="globalMessages">
		<common:globalMessages />
	</div>
	<div id="top-banner" class="homebanner">
		<cms:slot var="feature" contentSlot="${slots.Section1}">
			<div class="span-24 section1 advert">
				<cms:component component="${feature}" />
			</div>
		</cms:slot>
	</div>
	<div class="mainNavigation" data-role="content" data-theme="e" data-content-theme="e">
		<ul data-role="collapsible-set" data-inset="true">
			<cms:slot var="component" contentSlot="${slots['NavigationBar']}">
				<cms:component component="${component}" />
			</cms:slot>
		</ul>
	</div>
	<div id="bottom-banner" class="homebanner">
		<cms:slot var="feature" contentSlot="${slots.Section5}">
			<div class="span-24 section1 advert">
				<cms:component component="${feature}" />
			</div>
		</cms:slot>
	</div>
</template:page>
