<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<template:page pageTitle="${pageTitle}">
	<cms:slot var="feature" contentSlot="${slots.Section1}">
		<div class="span-24 section1 advert">
			<cms:component component="${feature}" />
		</div>
	</cms:slot>
	<div class="span-24 section2">
		<div class="span-4 zone_a advert">
			<cms:slot var="feature" contentSlot="${slots.Section2A}">
				<cms:component component="${feature}" />
			</cms:slot>
		</div>
		<div class="span-20 zone_b last">
			<cms:slot var="feature" contentSlot="${slots.Section2B}">
				<cms:component component="${feature}" />
			</cms:slot>
		</div>
	</div>
	<div class="span-24 section3 advert">
		<cms:slot var="feature" contentSlot="${slots.Section3}">
			<cms:component component="${feature}" />
		</cms:slot>
	</div>
	<div class="span-24 section3 advert">
		<cms:slot var="feature" contentSlot="${slots.Section5}">
			<cms:component component="${feature}" />
		</cms:slot>
	</div>
</template:page>
