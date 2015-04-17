<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>

<template:page pageTitle="${pageTitle}">
	<div id="middleContent">
		<div class="innermiddleContent">
			<div>
				<cms:slot var="feature" contentSlot="${slots.GlobalSection}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>
			<div id="storeMainTopBanner">
				<cms:slot var="feature" contentSlot="${slots.HomeTopSection}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>
			<div style="padding-top:20px;">
				<div class="bbanner first">
					<cms:slot var="feature" contentSlot="${slots.HomeMidLeftSection}">
						<cms:component component="${feature}" />
					</cms:slot>
				</div>
				<div class="bbanner">
					<cms:slot var="feature" contentSlot="${slots.HomeMidRightSection}">
						<cms:component component="${feature}" />
					</cms:slot>
				</div>
			</div>
			<div style="padding-top:20px;">
				<div class="cbanner">
					<cms:slot var="feature" contentSlot="${slots.HomeBtmLeftSection}">
						<cms:component component="${feature}" />
					</cms:slot>
				</div>
				<div class="cbanner middle">
					<cms:slot var="feature" contentSlot="${slots.HomeBtmMidSection}">
						<cms:component component="${feature}" />
					</cms:slot>
				</div>
				<div class="cbanner">
					<cms:slot var="feature" contentSlot="${slots.HomeBtmRightSection}">
						<cms:component component="${feature}" />
					</cms:slot>
				</div>
			</div>
		</div>
	</div>

</template:page>