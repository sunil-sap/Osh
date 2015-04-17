<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:url value="/" var="siteRootUrl"/>
<%-- jQuery --%>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.1.7.1.min.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.tmpl.1.0.0pre.min.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery-ui-1.8.19.custom.min.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/accmob.common.js"></script>
<script type="text/javascript">
	/*<![CDATA[*/
	ACCMOB.common.contextPath = "${request.contextPath}";
	ACCMOB.common.commonResourcePath = "${commonResourcePath}";
	ACCMOB.common.themeResourcePath = "${themeResourcePath}";
	ACCMOB.common.siteResourcePath = "${siteResourcePath}";
	ACCMOB.common.rootPath = "${siteRootUrl}";

	$(document).bind("mobileinit", function() {
		$.mobile.ajaxEnabled = false;
		$.mobile.ajaxLinksEnabled = false;
		$.mobile.selectmenu.prototype.options.nativeMenu = true;
		$.mobile.defaultPageTransition = "none";
		$.mobile.defaultDialogTransition = "none";
		$.mobile.minScrollBack = 250;
		$.mobile.loadingMessageTheme = "f";
	});
	/*]]>*/
</script>
<%-- jQuery mobile --%>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.mobile-1.1.0.min.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.mobile.simpledialog2.min.1.0.1-2012022700.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.mobile.autocomplete.custom.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.ui.stars.min.3.0.1.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/jquery.form.2.67.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/mobile-tooltip.js"></script>
<%-- ACCMOB --%>
<script type="text/javascript" src="${commonResourcePath}/js/accmob.messages.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/accmob.menu.js"></script>
<script type="text/javascript" src="${commonResourcePath}/js/accmob.autocomplete.js"></script>

<c:if test="${currentLanguage.isocode eq 'en'}">
<script type="text/javascript" src="${commonResourcePath}/js/jquery.pstrength-min.1.2.js"></script>

<script type="text/javascript">
	/*<![CDATA[*/
	$(function() {
		$('.strength').pstrength();
	});
	/*]]>*/
</script>
</c:if>
