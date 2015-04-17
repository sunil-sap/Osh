<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<h6 class="descriptionHeadline">
	<spring:theme code="text.headline.navigationbar" text="Browse through the navigation bar"/>
</h6>
<div id="top-nav-bar" class="top-nav-bar" data-theme="f">
	<a href="#" id="top-nav-bar-menu" data-role="button" role="button" data-iconpos="notext" data-theme="f"
		data-icon="home" title="<spring:theme code="text.button.menu"/>">
		<spring:theme code="text.button.menu" />
	</a> 
	<a href="<c:url value="/store-finder"/>" id="top-nav-bar-home" data-role="button" role="button" 
		data-iconpos="notext" data-theme="f" data-icon="custom-stores-w" title="Store Finder">
		Store Finder
	</a>
	<cms:slot var="cart" contentSlot="${slots.MiniCart}">
		<cms:component component="${cart}"/>
	</cms:slot>
	<h6 class="descriptionHeadline"><spring:theme code="text.headline.myaccount" text="Click here to login or get to your Account"/></h6>
	<a href="#" id="top-nav-bar-account" data-role="button" role="button" data-theme="f" 
		data-iconpos="notext" data-icon="user" title="Login and Account">
		Login and Account
	</a>
	<c:if test="${fn:length(currencies) > 1 and fn:length(languages) > 1}">
		<h6 class="descriptionHeadline">Click here to change the language</h6>
		<a href="#" id="top-nav-bar-settings" data-role="button" role="button" data-theme="f" 
			data-iconpos="notext" data-icon="globe" title="Language and Currency">
			Language and Currency
		</a>
	</c:if>
</div>
<h6 class="descriptionHeadline"><spring:theme code="text.headline.search" text="Here you can search for products"/></h6>
<div id="header" data-role="header" data-theme="d">
	<div class="siteLogo">
		<c:url value="/" var="homeUrl"/>
		<a href="${homeUrl}">
			<cms:slot var="logo" contentSlot="${slots.SiteLogo}">
				<cms:component component="${logo}"/>
			</cms:slot>
		</a>
	</div>
	<c:url value="/search/autocomplete" var="autocompleteUrl"/>
	<form name="search_form" method="get" action="<c:url value="/search"/>">
		<spring:theme code="text.search" var="searchText"/>
		<spring:theme code="search.placeholder" var="searchPlaceholder"/>
		<ycommerce:testId code="header_search_input">
			<label for="search"></label>
			<input id="search" class="text accmob-mainSearch-input" type="search" name="text" value="" maxlength="100"
				placeholder="${searchPlaceholder}" data-autocomplete-url="${autocompleteUrl}" />
		</ycommerce:testId>
		<div class="accmob-mainSearch-button">
			<ycommerce:testId code="header_search_button">
				<spring:theme code="img.searchButton" text="/" var="searchButtonPath"/>
				<input class="button" data data-iconpos="notext" data-icon="custom-search" data-theme="c"
					type="submit" value="${searchText}" alt="${searchText}" />
			</ycommerce:testId>
		</div>
	</form>
</div>
<!-- the following elements are hidden and should show up by clicking on the corresponding tob-nav-bar button -->
<div id="menuContainer" class="top-nav-bar-layer accmob-topMenu header-popup menu-container" style="display:none" data-theme="f">
	<h6 class="descriptionHeadline"><spring:theme code="text.headline.categories" text="Click here the menu button to get to the categories"/></h6>
	<ul>
		<li class="La  auto ui-btn ui-btn-up-f ui-btn-icon-right ui-li-has-arrow ui-li">
			<div class="ui-btn-inner ui-li">
				<div class="ui-btn-text">
					<a title="Home" href="<c:url value="/"/>" class="ui-link-inherit">Home</a>
				</div>
				<span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span>
			</div>
		</li>
	</ul>
	<ul data-role="listview" data-inset="true" id="menulist" data-theme="f">
		<cms:slot var="component" contentSlot="${slots['NavigationBar']}">
			<cms:component component="${component}"/>
		</cms:slot>
	</ul>
</div>
<div id="currencyLanguageSelector" class="top-nav-bar-layer accmob-currencyLanguageSelector header-popup menu-container" style="display:none">
	<template:currencylanguage currencies="${currencies}" currentCurrency="${currentCurrency}" languages="${languages}" currentLanguage="${currentLanguage}" />
</div>
<div id="userSettings" class="top-nav-bar-layer user-settings header-popup menu-container" style="display:none">
	<ul data-role="listview" data-inset="true" data-theme="f">
		<sec:authorize ifNotGranted="ROLE_CUSTOMERGROUP">
			<li>
				<ycommerce:testId code="header_Login_link">
					<a href="<c:url value='/login'/>"><spring:theme code="header.mobile.link.login"/></a>
				</ycommerce:testId>
			</li>
		</sec:authorize>
		<li>
			<spring:url value="/my-account" var="encodedUrl"/>
			<a href="${encodedUrl}">
				<spring:theme code="text.account.account" text="Account"/>
			</a>
		</li>
		<sec:authorize ifAnyGranted="ROLE_CUSTOMERGROUP">
			<li>
				<ycommerce:testId code="header_signOut">
					<a href="<c:url value='/logout'/>"><spring:theme code="text.logout"/></a>
				</ycommerce:testId>
			</li>
		</sec:authorize>
	</ul>
</div>
