<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="header" tagdir="/WEB-INF/tags/desktop/common/header"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>

<template:page pageTitle="${pageTitle}">
	<div id="middleContent">
		<div class="innermiddleContent">
			<div class="breadcrumb">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			</div>
			<div id="top_fullwidth_banner">
				<cms:slot var="feature" contentSlot="${slots.ES_Globle}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>
			
			<div id="top_fullwidth_banner" style="width: 960px;height: 120px;">
				<cms:slot var="feature" contentSlot="${slots.ES_StoreLocatorTop}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>
			
       <div class="tabbanner2">
        <div class="middlemainheader"> <span><b><spring:theme code="mobile.storelocator.title"/></b></span> </div>
        </div>
        
        
		<div class= "Store_Locatorwrapper">
		<div class="searchbarwrapper">	
        	
	
		<form name="search_form" method="get"
			action="<c:url value="/search"/>">
			 <label class="skip" for="search">${searchText}</label>
					<spring:theme code="search.placeholder" var="searchPlaceholder" />
					<ycommerce:testId code="header_search_input">
						<span class="searchfield"> <input id="search" class="searchbar"
							name="text" type="text" value="Enter keyword or model number"
							maxlength="100" placeholder="${searchPlaceholder}" />
						</span>
					</ycommerce:testId> <ycommerce:testId code="header_search_button">
						<spring:theme code="img.searchButton" text="/"
							var="searchButtonPath" />
						
						 <div class="search_store_btn">
						 <input name="search"
							type="submit" value="Search" class="button"
							src="<c:url value="/store-finder"/>" alt="${searchText}" />
						</div>
					</ycommerce:testId>
						
		</form>
		<div class="or_textwrapper">
        - <spring:theme code="storelocator.or.text"/> - </div>
        <div class="use_my_location">
        <a href="${request.contextPath}/store-finder?q=${zipCode}"><spring:theme code="text.location"/></a>
        </div>	
		</div>
		
		
			<div class="sl_contentwrapper">
			<div class="img_wrapper" style="width: 170px;height: 290px;">
			<div>
				<cms:slot var="feature" contentSlot="${slots.LeftBannerComponent}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>
			</div>
			<div class="sl_content_write">
			<p><spring:theme code="text.store.distance"/></p>
			<p><spring:theme code="text.store.search.suggestions"/></p>
			<ul>
			<li><spring:theme code="text.city"/></li>
			</ul>
			<div class="link_sshopmore"><a href="#"><spring:theme code="osh.storeLocator.page.button.shop.more"/></a></div>
       		 </div>
			</div>
			
			   		 
			
			<div class="top_fullwidth_banner" >
				<cms:slot var="feature" contentSlot="${slots.ES_StoreLocator}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>
			
			
			
		</div>
	</div>

</template:page>