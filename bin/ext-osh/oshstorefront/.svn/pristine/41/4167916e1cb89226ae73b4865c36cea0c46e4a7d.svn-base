<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="store" tagdir="/WEB-INF/tags/desktop/store"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/mobile/user"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/mobile/formElement"%>
<%@ taglib prefix="wishlist" tagdir="/WEB-INF/tags/desktop/wishlist"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>'
	<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>

<template:page pageTitle="${store.name} | ${siteName}">
	<div id="middleContent">
		<div class="innermiddleContent">
			<div>
				<cms:slot var="feature" contentSlot="${slots.GlobalSection}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>
		
	</div>
	<div id="siteMapContentBlock">
	
	<h1 class="siteMapText"><b>Site Map</b></h1>
	
	<cms:slot var="component" contentSlot="${slots['SiteMapTopSection']}">
	        <ul class="siteMapTopCat"><li> <a href="${request.contextPath}/${component.link.url}"class="siteMapTopCat" >${component.link.linkName}</a></li></ul>
			<c:forEach items="${component.navigationNode.children}" varStatus="index"
											var="child">
											<c:if test="${child.name ne 'Shop By Brand'}">
											<div class="siteMaplevel2and3">
											<ul class="siteMaplevel2"><li>
												<a href="${request.contextPath}/${child.url}" >${child.name}</a></li></ul>	
													<ul class="siteMapLevel3">
														<c:forEach items="${child.links}" var="childlink"
															begin="${i.index}">
															<li>
															<a href="${request.contextPath}/${childlink.url}" class="siteMaplevel2">${childlink.linkName}</a>
															</li>
														</c:forEach>
													</ul>
											</div>
											</c:if>
				</c:forEach>
		</cms:slot>
 </div>
 </div>
</template:page>