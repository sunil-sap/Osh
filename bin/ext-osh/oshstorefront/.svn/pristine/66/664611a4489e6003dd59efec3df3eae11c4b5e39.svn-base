<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
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
				
				
				<div class="middle_form">
					<div class="loginbox2">
						<h2>
							<spring:theme code="forgottenPwd.title" />
						</h2>
						<div class="formwrap">

							<div class="item_container">
								<p>
									<spring:theme code="forgottenPwd.description" />
								</p>
								<div class="red_note">
									<p class="required">
										<spring:theme code="form.required" />
									</p>
								</div>
								<form:form method="post" commandName="forgottenPwdForm">
									<dl>
										<formElement:formInputBox idKey="forgottenPwd.email"
											labelKey="forgottenPwd.email" path="email" inputCSS="email required textfield"
											mandatory="true" />
									</dl>
									<div class="btn2 floatl btnsendemail">
										<span style="display: block; clear: both;">
											<input id="submit" type="submit" value='<spring:theme code="forgottenPwd.submit"/>'/>
										</span>
									</div>
								</form:form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>