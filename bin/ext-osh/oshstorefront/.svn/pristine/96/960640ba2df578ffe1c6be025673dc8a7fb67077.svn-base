
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ attribute name="specifications" required="true"
	type="java.util.Collection"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 				
 <c:forEach items="${specifications}" var="spec">
 
 <c:forEach items="${spec.features}" var="feature">
 <p><strong>${feature.name} : </strong>
 <c:forEach items="${feature.featureValues}" var="values" varStatus="index">
 <c:if test="${index.count eq 1}">
  ${values.value}
 </c:if>
 
 </c:forEach>
</p>
 </c:forEach>
 
 </c:forEach>