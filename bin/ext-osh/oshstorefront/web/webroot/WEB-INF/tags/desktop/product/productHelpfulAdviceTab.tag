<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="tableftcontent">
	${product.helpfulAdvice}
</div>	
<div class="sidecolume">	
		<c:if test="${not empty product.productAttachment}">
		<h4><spring:message code="osh.product.page.tab.description.attachment"/></h4>
			<a href="${product.productAttachment}" class="linkpdf"><spring:message code="osh.product.page.tab.description.freepdf"/></a>
		</c:if>
	<ul class="linklist">
		<c:if test="${not empty product.msds}">
			<li><a href="${product.msds}" target="_blank"><spring:message code="osh.product.page.tab.description.msdssheet"/></a></li>
		</c:if>
		<c:if test="${not empty product.ownersManual}">	
			<li><a href="${product.ownersManual}" target="_blank"><spring:message code="osh.product.page.tab.description.ownersmanual"/></a></li>
		</c:if>	
		<c:if test="${not empty product.rebateInformation}">
			<li><a href="${product.rebateInformation}" target="_blank"><spring:message code="osh.product.page.tab.description.rebateinfo"/></a></li>
		</c:if>	
		<c:if test="${not empty product.warrantyInformation}"> 
			<li><a href="${product.warrantyInformation}" target="_blank"><spring:message code="osh.product.page.tab.description.Warrantyinfo"/></a></li>
		</c:if>
	</ul>
</div>