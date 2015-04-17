<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="pageData" required="true" type="de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/mobile/nav"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/mobile/template"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/mobile/common/footer"%>

<template:mobilePage pageId="facetRefinements-page" dataSearchQuery="${pageData.currentQuery.query}">
	<jsp:attribute name="header">
		<h3 role="heading" class="ui-li-has-count-checkbox">
			<spring:theme code="search.nav.refinements"/> &nbsp;<span>${pageData.pagination.totalNumberOfResults}</span>
		</h3>
		<a href="#" data-role="button" id="applyFilter" data-icon="check" data-theme="c" class="ui-btn-right"><spring:theme code="search.nav.done.button"/></a>
	</jsp:attribute>
	<jsp:attribute name="footer">
		<footer:simpleFooter/>
	</jsp:attribute>
	<jsp:body>
		<nav:facetNavRefinements pageData="${pageData}"/>
	</jsp:body>
</template:mobilePage>
<script id="refinementFacetPageTemplate" type="text/x-jquery-tmpl">
	<div id="{{= $data.name}}-page"
		class="{{if $data.multiSelect}}multiSelectFacetPage{{else}}facetPage{{/if}} refinementFacetPageHeader"
		data-role="page"
		data-theme="d"
		data-url="<c:url value="/"/>">
		<div data-role="header" data-position="fixed" class="ui-bar" data-position="inline">
			<a href="#" class="backToFacets" data-role="button" data-rel="back" data-icon="arrow-l"
				data-transition="slideIn" data-theme="a">
				<spring:theme code="search.nav.refine.button"/>
			</a>
			<h3><spring:theme code="search.nav.refinements"/></h3>
			<a href="#" data-role="button" id="applyFilter" rel="external" data-icon="check" data-theme="c">
				<spring:theme code="search.nav.done.button"/>
			</a>
		</div>
		{{tmpl($data) "#refinementFacetContentTemplate"}}
	</div>
</script>
<script id="refinementFacetContentTemplate" type="text/x-jquery-tmpl">
	<div data-role="content" data-theme="d" class="ui-li-has-count-checkbox">
		<fieldset data-role="controlgroup" class="facetValueList" data-facet="{{= $data.name}}">
			{{each $data.values}}
			<input type="checkbox" data-theme="d" data-query="{{= $data.code}}:{{= code}}" id="{{= name}}" {{if selected}} checked="checked" {{/if}} />
			<label for="{{= name}}">{{= name}} {{if count>0}}<span class="ui-li-count ui-btn-up-c ui-btn-corner-all">{{= count}}</span>{{/if}}</label>
			{{/each}}
		</fieldset>
	</div>
</script>
