<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ attribute name="errorNoResults" required="true" type="java.lang.String"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement"%>


<div class="searchbarwrapper">

	<c:url value="/store-finder" var="storeFinderFormAction" />
	<form:form action="${storeFinderFormAction}" method="get"
		commandName="storeFinderForm">

		<ycommerce:testId code="storeFinder_search_box">
			<div class="storefonderInputtext">
			
			<formElement:formSearchInputBox idKey="storelocator.query"
				labelKey="storelocator.query" path="q" inputCSS="searchbar"
				mandatory="true" />
			</div>
			<div class="search_store_btn">
				<button class="storeLocator_Search_button">
					<spring:theme code="storeFinder.search" />
				</button>
			</div>
		</ycommerce:testId>

	</form:form>
	<div class="or_textwrapper">- <spring:theme code="storelocator.or.text"/> -</div>
				
							<div class="use_my_location">
								<a href="${request.contextPath}/store-finder?q=${zipCode}" ><spring:theme
										code="text.location" /></a>
							</div>
						
</div>


