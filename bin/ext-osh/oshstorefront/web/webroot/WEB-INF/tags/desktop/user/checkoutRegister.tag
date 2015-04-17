<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="actionNameKey" required="true"
	type="java.lang.String"%>
<%@ attribute name="action" required="true" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>

<h2><spring:theme code="register.new.customer"/></h2>
<div class="clearb"></div>
<div class="formwrap">
	<p><spring:theme code="register.checkout"/></p>
	<p><spring:theme code="register.conform"/></p>

	<br clear="all" /> <br clear="all" /> <br clear="all" /> <br
		clear="all" /> <br clear="all" /> <br clear="all" /> <br
		clear="all" /> <br clear="all" />
	<div class="clearb"></div>
	<div class="btn2 floatl">
		<input type="button" value="Checkout as a Guest"
			name="Checkout as a Guest" />
	</div>
	<div class="clearb"></div>
</div>