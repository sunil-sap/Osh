<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="store" tagdir="/WEB-INF/tags/desktop/store"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>

<template:page pageTitle="${store.name} | ${siteName}">
	<div id="middleContent">
		<div class="innermiddleContent">
			<div id="breadcrumb" class="breadcrumb">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			</div>
			<div id="globalMessages">
				<common:globalMessages />
			</div>
			<div id="top_fullwidth_banner">
				<cms:slot var="feature" contentSlot="${slots.ES_Globle}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>

			<div id="top_fullwidth_banner" style="width: 960px; height: 120px;">
				<cms:slot var="feature" contentSlot="${slots.ES_StoreLocatorTop}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>

			<div class="Store_Locatorwrapper">
				<store:storeSearch errorNoResults="${errorNoResults}" />



				<div class="sl_contentwrapper">
					<div class="gmap_mainwrapper">
						<ycommerce:testId code="storeFinder_storeDetails_label">
							<div class="store_details_left_col">

								<store:storeDetails store="${store}" />
								<store:storeMap store="${store}" />
							</div>
						</ycommerce:testId>
						<div class="store_details_right_col">

							<div class="store_paragraph_content">${store.storeContent}
							</div>
						</div>
					</div>
				</div>
				<div class="top_fullwidth_banner" >
				<cms:slot var="feature" contentSlot="${slots.ES_StoreLocator}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>
			</div>
		</div>
	</div>
</template:page>