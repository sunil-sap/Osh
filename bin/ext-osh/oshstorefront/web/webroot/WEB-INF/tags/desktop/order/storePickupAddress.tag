<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="posData" required="true" type="de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData" %>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>




<h2 style="margin-bottom: 5px;"><spring:theme code="Store Pickup Address"/></h2>
<div class="orderborderbox">
	<div class="cartformwarp">
		<div class="cartformwarp_inner_orderdetail">
			 <p>${posData.name}</p>
                    <p>${posData.address.line1}</p>
                    <p>${posData.address.town}, ${posData.address.postalCode}</p>
                    <p>${posData.address.phone}</p>
                  </div>
                  <a class="linkgetdirection" href="${storeUrl}"><spring:theme code="osh.storeLocator.page.getDirection"/></a>
	</div>
</div>