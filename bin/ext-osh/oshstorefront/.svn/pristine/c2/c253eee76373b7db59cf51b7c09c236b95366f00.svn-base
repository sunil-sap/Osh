<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="header" tagdir="/WEB-INF/tags/desktop/common/header"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
	<div id="headerContent">
		<div id="headerLogo">
			<c:url value="/" var="homeUrl" />
			<a href="${homeUrl}"> <cms:slot var="logo"
					contentSlot="${slots.SiteLogo}">
					<cms:component component="${logo}" />
				</cms:slot>
			</a>
		</div>

	<div id="headerKeywordSearch">
		<form name="search_form" method="get"
			action="<c:url value="/search"/>">
			<span class="keywordsearchbox"> <span
				class="innerkeywordsearchbox"> <label class="skip" for="search">${searchText}</label>
					<spring:theme code="search.placeholder" var="searchPlaceholder" />
					<ycommerce:testId code="header_search_input">
						<span class="searchfield small_serchfield" style="margin-top: 5px;"> <input id="search" class="text"
							name="text" type="text" maxlength="100" onclick="if(this.value=='${searchPlaceholder}'){this.value='';}" onblur="if(this.value==''){this.value='${searchPlaceholder}';}" value="${searchPlaceholder}" style="height: 21px; margin-left: 10px; margin-top: 0px;"/>
						</span>
					</ycommerce:testId> <ycommerce:testId code="header_search_button">
						<spring:theme code="img.searchButton" text="/"
							var="searchButtonPath" />
						<span class="searchbutton" style="height: 30px;">
						<input name="search" type="submit" value="Search" style="height: 30px;"
							src="<c:url value="${searchButtonPath}"/>" alt="${searchText}" onclick="checkSearchValue()"/>
						</span>
					</ycommerce:testId>
			</span>
			</span>
		</form>
	</div>
	<cms:slot var="cart" contentSlot="${slots.MiniCart}">
       		 <cms:component component="${cart}"/>
		</cms:slot>
		
	</div>
<nav:topNavigation />
<script>
	<c:if test="${request.secure}">
	<c:url value="/search/autocompleteSecure"  var="autocompleteUrl"/>
	</c:if>
	<c:if test="${not request.secure}">
	<c:url value="/search/autocomplete"  var="autocompleteUrl"/>
	</c:if>

	$(function() {
		$("#search").autocomplete({
			source : function(request, response) {
				$.getJSON("${autocompleteUrl}", {
					term : $('#search').val()
				}, function(data) {
					response(data);
				});
			},
			minLength : 2,
			open : function(event, ui) {
				$(".ui-menu").css("z-index", 10000);
			},
			close : function(event, ui) {
				$(".ui-menu").css("z-index", -1);
			},
			select : function(event, ui) {
				if (ui.item) {
					$('#search').val(ui.item.value.trim());

				}
				document.forms['search_form'].submit();
			},
			autoFocus : false

		});
	});
	
	//check search value: set empty if it is default value
	function checkSearchValue(){
		var str = $("#search").val();
		if(str == '${searchPlaceholder}')
			$("#search").val("");
	};

</script>
