<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>
<%@ taglib prefix="breadcrumb" tagdir="/WEB-INF/tags/desktop/nav/breadcrumb" %>

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
			
			<div class="mainmiddleContent">
				<div class="leftmaincontent">
					<div class="left_img">
					<!-- <img width="156" height="555" align="left"/> -->
						<cms:slot var="feature" contentSlot="${slots.SideContent}">
							<cms:component component="${feature}" />
						</cms:slot>	
					</div>
				</div>
				<div class="middlemaincontent">
					<div class="middlemainheaderbanner">
						<span> <cms:slot var="feature"
								contentSlot="${slots.CatagoryNameSection}">
								<cms:component component="${feature}" />
							</cms:slot>
						</span>
					</div>
					<div>
					<div class="clearb"></div>
						<div class="middle_form">
							<div class="loginbox2">
								<h2><spring:theme code="osh.changePwd.page.change.password"/></h2>
								<div class="clearb"></div>
								<div class="formwrap">
									<form:form method="post" commandName="updatePwdForm">
										<dl>
											<formElement:formPasswordBox idKey="updatePwd-pwd" labelKey="updatePwd.pwd" path="pwd" inputCSS="new password required textfield" mandatory="true"/>
											<formElement:formPasswordBox idKey="updatePwd.checkPwd" labelKey="updatePwd.checkPwd" path="checkPwd" inputCSS="confnew password textfield" mandatory="true" errorPath="updatePwdForm"/>
										</dl>
										<span style="display: block; clear: both;">
											<ycommerce:testId code="update_update_button">
											<div class="btn2 floatl">
                  									<input id="submit" type="submit" value='<spring:theme code="updatePwd.submit"/>'/>
               								 </div>
											</ycommerce:testId>
										</span>
									</form:form>
									<div class="clearb"></div>
								</div>
								</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>