<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/component"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/common/footer"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/theme"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/common/footer"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><spring:theme code="insert.title"/></title>
</head>
<body>
<div id="footer">
<div>
<form:form commandName="cheetahMailForm" action="${request.contextPath}/subscribe/cheetah" method="Post" id="cheetahMailSubscibe">
	<div class="floatl">
		<ul class="first">
			<li class="footerspecialfont"><spring:message code="osh.callcenter.number.message"/></li>
			<li class="footerspecialfont signupinlist"><spring:message code="osh.signup.email.receive"/></li>
			<li class="footerspecialfont signupforminlist">
					<span class="keywordsearchbox"> <span
						class="innerkeywordsearchbox"> <span class="searchfield"><form:input path="emailId" id="emailAddress" 
								name="emailsignup" type="text" 
								data-default="Your Email Address" value="Your Email Address" onfocus="this.value=''" onblur="javascript:if(this.value==''){this.value='Your Email Address';}"/></span>
							<span class="searchbutton">
							 <input name="search" type="submit" id="emailSubscriberd" value="Sign Up"/>
						<!-- 	<a href="#" onclick="subscribeEmaiId(this)">Sign Up</a> -->
							</span>
							<span class="error-msg"
							id="error-msg_invalid" style="display: none"><spring:message
								code="register.email.invalid" /></span>
					</span>
					</span>
			</li>
			<c:forEach items="${banners}" var="banner">
				<li><a href="${banner.urlLink}" target="_blank"><img src="${banner.media.url}" alt=""
						class="shareicon" />${banner.name}</a></li>
			</c:forEach>
		</ul>
	</div>
	</form:form>
	<c:forEach items="${component.navigationNodes}" var="node">
		<div class="floatl">
			<ul>
				<li class="footerspecialfont">${node.name}</li>
				<c:forEach items="${node.links}" var="childLink">
					<li><a href="${childLink.url}" title="${childLink.name}">${childLink.name}</a></li>
				</c:forEach>
			</ul>
		</div>
	</c:forEach>
	<div class="floatl">
		<ul class="last">
			<c:forEach items="${oshFooterImageText}" var="imageText">
				<li class="footerspecialfont">${imageText.content}</li>
				<li><img src="${imageText.media.url}" alt="" /></li>
			</c:forEach>
		</ul>
	</div>
	<div class="copyrightsbox">
		<div class="innercopyrightsbox">
			<span>${component.notice}</span>
		</div>
		</div>
	</div>
	</div>
</body>
<script type="text/javascript">

$('#emailSubscriberd')
.click(
		function(e) {
			e.preventDefault();
			var allowSubmit = true;
			
			 var emailId=$("#emailAddress").val();
				var emailRegex = new RegExp(
				/^[+a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i);
				var email1 = emailRegex.test(emailId);
				var subscriber=true;
				if(!email1)
					{
					$("#error-msg_invalid").show();
					}
					else
					{
					$("#cheetahMailSubscibe").submit();
					}
		});
</script>
</html>

