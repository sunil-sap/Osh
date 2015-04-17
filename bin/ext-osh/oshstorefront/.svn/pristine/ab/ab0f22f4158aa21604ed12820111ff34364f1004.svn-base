<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>

<c:set value="${oshProduct}" var="product" />
<c:set value="${oshVariantProduct}" var="variantProduct" />
<template:page pageTitle="${pageTitle}">

<jsp:attribute name="pageScripts">
	<product:productDetailsJavascript product="${product}" />
</jsp:attribute>
<jsp:body>
	<link rel="stylesheet" type="text/css" media="print" href="${request.contextPath}/_ui/desktop/osh/css/product_details_print.css" />
		<c:if test="${not empty message}">
			<spring:theme code="${message}" />
		</c:if>
		<div id="middleContent">
			<div class="logoforprint">
				<a href="#">
					<img border="0" src="${request.contextPath}/_ui/desktop/osh/images/main_logo.png" alt="">
				</a>
			</div>
			<div class="innermiddleContent">
				<div id="breadcrumb" class="breadcrumb">
					<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
				</div>
		 		<div class="product_banner">
   					<cms:slot var="feature" contentSlot="${slots.GlobalSection}">
   			 			<cms:component component="${feature}" />
  			        </cms:slot>
 		 		</div>
				<div class="mainmiddleContent" style="width: 800px;">	
					<div class="leftmaincontent">
			  		<product:productShareLinks sendEmailForm="${sendEmailForm}" />            
						<div class="middlemainheaderbanner" style="width: 800px;">            
							<product:productDetailsPanel product="${product}" summary="${summary}" variantProduct="${variantProduct}" galleryImages="${galleryImages}" />
							<div class="clearb"></div>
							<div id="producttabs">
								<product:productPageTabs product="${product}" specifications="${specifications}" />
							</div>
							<div class="vendor_information">
								<table width="100%" border="0">
									<tbody>
										<tr>
											<td width="220">
												<cms:slot var="feature" contentSlot="${slots.PDPVendorCraftsmanSection}">
													<cms:component component="${feature}" />
												</cms:slot>
											</td>
											<td>
												<cms:slot var="feature" contentSlot="${slots.PDPCraftsManTxtSection}">
													<cms:component component="${feature}" />
												</cms:slot>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			 	<div class="rightmaincontent">	
		        	<product:productAddToCartPanel product="${product}" variantProduct="${variantProduct}" />
      	 			<div>
      					<cms:slot var="feature" contentSlot="${slots.ES_PDPRight}">
							<cms:component component="${feature}" />
						</cms:slot>
       				</div>
          				
                	<cms:slot var="comp" contentSlot="${slots.CrossSell}">
						<cms:component component="${comp}" />
					</cms:slot>
                </div>
       		 </div>
        </div>
	</jsp:body>
</template:page>
<script type="text/javascript"
	src="${request.contextPath}/_ui/desktop/osh/js/product_details.js"></script>
