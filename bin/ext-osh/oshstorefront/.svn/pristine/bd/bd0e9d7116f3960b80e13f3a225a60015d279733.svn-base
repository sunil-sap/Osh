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
	uri="http://www.springframework.org/security/tags"%>

<template:page pageTitle="${store.name} | ${siteName}">
	<div id="middleContent">
		<div class="innermiddleContent">
			<div class="breadcrumb">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			</div>
			<div>
				<cms:slot var="feature" contentSlot="${slots.GlobalSection}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>
			
			
			
		 <sec:authorize	access="!hasRole('ROLE_CUSTOMERGROUP')">
		 <div class="middlemainheaderbanner"
				style="width: 960px; height: 120px;">
				<cms:slot var="feature" contentSlot="${slots.WishListTopSection}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>
			<div id="globalMessages">
				<common:globalMessages />
			</div>
			<div class="mainmiddleContent">
				<div class="middlemaincontent">
					<div class="middlemainheaderbanner"></div>
					<div class="middlewrapper">
						<div class="uppermiddle_wishlist"
							style="margin-left: -20px; margin-top: 25px;">
							<div class="form">
							<c:url value="/wishlist/j_spring_security_check"
								var="loginWishlistAction" />
							<c:url value="/wishlist/j_spring_security_check" var="loginWishlistAction" />
							<wishlist:wishlistLogin actionNameKey="checkout.login.loginAndCheckout" action="${loginWishlistAction}" />
							</div>
						</div>
					</div>
				</div>
			</div>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_CUSTOMERGROUP">
			

			<wishlist:userWishlist action="" actionNameKey=""/>
			
			</sec:authorize>
			
		</div>
	</div>
</template:page>