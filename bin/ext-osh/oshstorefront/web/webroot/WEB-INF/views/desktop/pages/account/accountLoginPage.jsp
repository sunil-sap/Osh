<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/desktop/user"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>

<template:page pageTitle="${pageTitle}">
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
			
			
			<div class="mainmiddleContent page_signin">
				<div class="leftmaincontent">
					<div class="left_img">
						<cms:slot var="feature" contentSlot="${slots.SideContent}">
							<cms:component component="${feature}" />
						</cms:slot>
					</div>
				</div>
				<div class="middlemaincontent">
					<div class="middlemainheaderbanner">
						<cms:slot var="feature" contentSlot="${slots.CatagoryNameSection}">
							<cms:component component="${feature}" />
						</cms:slot>
					</div>
					<div class="clearb"></div>
					
					<div class="signinpage">
						<div class="middle_form">
							<div class="loginbox1">
								<c:url value="/login/register" var="registerActionUrl" />
								<user:register actionNameKey="register.submit"
									action="${registerActionUrl}" />
							</div>
							<div class="loginbox2">
								<c:url value="/j_spring_security_check" var="loginActionUrl" />
								<user:login actionNameKey="login.login"
									action="${loginActionUrl}" />
							</div>

						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template:page>