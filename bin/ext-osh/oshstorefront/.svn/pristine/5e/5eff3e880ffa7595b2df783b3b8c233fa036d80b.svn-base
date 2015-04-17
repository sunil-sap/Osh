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


<template:page pageTitle="${pageTitle}">
<div id="middleContent">
		<div class="innermiddleContent">
			<div>
   			 <cms:slot var="feature" contentSlot="${slots.GlobalSection}">
   			 <cms:component component="${feature}" />
   			 </cms:slot> 
		  
		  <div style="width: 100%;">
				<table width="100%" height="100%" >
					<tbody>
						<tr>
							<c:if test="${not empty slots.MiddleBodySection}">
							<td>
								<cms:slot var="feature" contentSlot="${slots.MiddleBodySection}">
									<cms:component component="${feature}" />
								</cms:slot>
							</td>
							</c:if>
						</tr>
					</tbody>
				</table>
			</div>
			</div>
			</div>
			</div>
			
</template:page>