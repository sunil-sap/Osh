<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<span> <span class="clearb floatl width100p"> <span
		class="cartlabel"> <span class="clearb floatl"><spring:message 
					code="osh.storeLocator.page.title.yourcart" /></span> <span
			class="clearb floatl bold"> <span>${totalItems} </span><spring:message
					code="osh.storeLocator.page.heder.items" />
				<span> <format:price priceData="${totalPrice}" /></span>
		</span>
	</span> <%-- <spring:theme text="items" code="cart.items" arguments="${totalItems}"/> </span></span> --%>
		<span class="carticon"><img
			src="${request.contextPath}/_ui/desktop/osh/images/shooping_cart_icon.gif"
			alt="" /></span>
</span> <span class="clearb floatl"> <span class="viewcartbutton">
			<a href="${request.contextPath}/cart"><spring:message
					code="osh.storeLocator.page.button.view.cart" /></a>
	</span> <span class="securecheckoutbutton"> <a
			href="${request.contextPath}/cart/checkout"><spring:message
					code="osh.storeLocator.page.button.secure.checkout" /></a>
	</span>
</span>
</span>