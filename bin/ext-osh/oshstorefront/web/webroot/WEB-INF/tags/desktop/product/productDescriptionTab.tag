<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div id="productdescription" class="tableftcontent">
	${product.description}
</div>