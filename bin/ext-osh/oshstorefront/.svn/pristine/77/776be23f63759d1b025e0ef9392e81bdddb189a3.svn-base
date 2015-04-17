<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ attribute name="pageTitle" required="false" rtexprvalue="true"%>
<%@ attribute name="pageCss" required="false" fragment="true"%>
<%@ attribute name="pageScripts" required="false" fragment="true"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="header" tagdir="/WEB-INF/tags/desktop/common/header"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/desktop/common/footer"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>

<template:master pageTitle="${pageTitle}">
	<jsp:body>
		<div id="mainBody">
		  <!-- <div id="topLinksContent"> -->
	<div id="topLinksMain" style="height: 44px;">
    	<div class="login" style="height: 30px;">
			<div class="marb10 overflow">
 				<span class="floatl" style="margin-top: 3px;">
 				 				<sec:authorize ifNotGranted="ROLE_CUSTOMERGROUP">
    	         <ycommerce:testId code="header_Login_link">
				<a style="margin-right: 4px;" href="${request.contextPath}/login"><spring:theme
											code="header.link.login" /> </a>
				</ycommerce:testId>
                  </sec:authorize>
                  
                  <sec:authorize ifAllGranted="ROLE_GUESTCUSTOMERGROUP">
    	         		<ycommerce:testId code="header_Login_link">
							<a style="margin-right: 4px;" href="${request.contextPath}/login"><spring:theme
											code="header.link.login" /> </a>
							</ycommerce:testId>
                  </sec:authorize>
                  
                  <span>
                  <sec:authorize ifNotGranted="ROLE_CUSTOMERGROUP">
    	         		<ycommerce:testId code="header_Login_link">
								<spring:theme code="storelocator.or.text" />
							</ycommerce:testId>
                  </sec:authorize>
                  
                   <sec:authorize ifAllGranted="ROLE_GUESTCUSTOMERGROUP">
    	         		<ycommerce:testId code="header_Login_link">
								<spring:theme code="storelocator.or.text" />
							</ycommerce:testId>
                  </sec:authorize>
                  </span>
                 
                  <sec:authorize ifNotGranted="ROLE_CUSTOMERGROUP">
    	         		<ycommerce:testId code="header_Login_link">
				   			<a style="margin-left: 4px;"
										href="${request.contextPath}/login"><spring:theme
											code="header.link.Register" /> </a>
							</ycommerce:testId>
                  </sec:authorize>
                  
                   <sec:authorize ifAllGranted="ROLE_GUESTCUSTOMERGROUP">
    	         		<ycommerce:testId code="header_Login_link">
				   			<a style="margin-left: 4px;"
										href="${request.contextPath}/login"><spring:theme
											code="header.link.Register" /> </a>
							</ycommerce:testId>
                  </sec:authorize>
                  
 				</span>
			   
			   <span class="floatl" style="margin-top: 4px; position: relative">
					<sec:authorize
								access="hasRole('ROLE_CUSTOMERGROUP') and !hasRole('ROLE_GUESTCUSTOMERGROUP')"
								ifAllGranted="ROLE_CUSTOMERGROUP">
				  <ycommerce:testId code="header_LoggedUser">
				  <spring:theme code="header.welcome"
										arguments="${user.firstName},${user.lastName}"
										htmlEscape="true" />
										
				</ycommerce:testId>
			   </sec:authorize>
			   </span>
			   <span class="floatl" style="margin-top: 3px;">
     
       <span class="dot">|</span>
    <ycommerce:testId code="header_LoggedUser">
    <a href="${request.contextPath}/login/wishlist"><spring:theme
										code="header.welcome.wishlist" /></a>&nbsp;</ycommerce:testId>
     
      </span>
 				<span class="floatl" style="margin-top: 2px;">
 				<cms:slot var="link" contentSlot="${slots.LeftHeaderNavTop}"> 		
				<cms:component component="${link}" />		
			   	</cms:slot>			  
			   	</span>
			   	<div class="clearboth"></div>
			<div class="myaccountsection">
				<span class="floatleft1" style="position: relative; bottom:2px">	
 				<cms:slot var="link" contentSlot="${slots.LeftHeaderNavBottom}">
			    <sec:authorize
									access="hasRole('ROLE_CUSTOMERGROUP') and !hasRole('ROLE_GUESTCUSTOMERGROUP')"
									ifAllGranted="ROLE_CUSTOMERGROUP">
 				<ycommerce:testId code="header_myAccount">  							
				<cms:component component="${link}" />
				</ycommerce:testId>
				</sec:authorize>		
			   	</cms:slot> &nbsp;
			   	<cms:slot var="link"
								contentSlot="${slots.RightHeaderNavMiddleBottom}">
			    <sec:authorize
									access="hasRole('ROLE_CUSTOMERGROUP') and !hasRole('ROLE_GUESTCUSTOMERGROUP')"
									ifAllGranted="ROLE_CUSTOMERGROUP">
 				<ycommerce:testId code="header_LoggedUser">  							
				<cms:component component="${link}" />
				<c:if test="${not empty customerPoints}">
				<span class="customerPoints"><spring:theme
													code="text.account.points" /> : ${customerPoints}</span>
				</c:if>
				</ycommerce:testId>
				</sec:authorize>
			   	</cms:slot>
			   </span>
				
			</div>
			</div>
			
		</div>

		<div class="oshNumber"
					style="margin-left: 325px; width: 140px; margin-top: -30px;">
			<div class="marb10">
						<span style="position: relative; top: 2px;"><cms:slot var="link"
								contentSlot="${slots.MiddleHeaderNavTop}"> 				
				<cms:component component="${link}" />
							</cms:slot></span>
					</div>
		</div>
		<div class="storedetails"
					style="padding-top: 10px; margin-top: -39px;">
				 <c:choose>
				<c:when test="${!empty storeName && storeName ne 'noStore'}">
				<div class="marb13"
								style="margin-left: -78px; width: 280px; float: left;">
			    <span class="float1" style="position: relative; top:1px;">
			    
			    <span><spring:theme code="text.your.store" /></span>
				 <span> ${fn:substring(storeName, 0, 20)}</span>
				 
				 <span style="position: relative; bottom: 0px; vertical-align: top;">
				 <cms:slot var="link" contentSlot="${slots.RightHeaderNavBottom}"> 	
					<cms:component component="${link}" />
				</cms:slot> 
				</span>
				 
				</span>
				  </div>
				 <div class="marb12" style="margin-left: 206px;"> 
				  <span class="topHeadericon1" style="padding-top: 0; float: left;position: relative; top:1px">
				  <cms:slot var="link" contentSlot="${slots.MiddleHeaderNavBottom}"> 				
						<cms:component component="${link}" />
				  </cms:slot>
				<cms:slot var="link" contentSlot="${slots.RightHeaderNavTop}"> 				
				<cms:component component="${link}" />
				</cms:slot>
				</span>
				</div>
				</c:when>
					
				<c:otherwise> 
				<div class="marb11">			  
				  <cms:slot var="link" contentSlot="${slots.MiddleHeaderNavBottom}"> 				
						<cms:component component="${link}" />
				  </cms:slot>
				<cms:slot var="link" contentSlot="${slots.RightHeaderNavTop}"> 				
				<cms:component component="${link}" />
				</cms:slot>
				</div>			
				 </c:otherwise>
				</c:choose>  
				</div>
		</div>
	 </div>
          <!-- </div>  -->  
          
          <div id="home_mainContent"> 
          <div id="header">
				<header:header />
				<cart:rolloverCartPopup />
			</div>	
				
				<%-- <cart:addToCart /> --%>
				
					<jsp:doBody />
					<footer:footer />
					</div>
		
		</div>
	</jsp:body>

</template:master>
