<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>

<script id="cartTotalsTemplate" type="text/x-jquery-tmpl">

	<dl class="order_totals">
		<dt><spring:theme code="basket.page.totals.subtotal"/></dt>
		<dd>{{= subTotal.formattedValue}}</dd>
		<dt class="savings"><spring:theme code="basket.page.totals.savings"/></dt>
		<dd class="savings">{{= totalDiscounts.formattedValue}}</dd>
		{{if deliveryCost}}
			<dt><spring:theme code="basket.page.totals.delivery"/></dt>
			<dd>
				{{if deliveryCost.value > 0}}
					{{= deliveryCost.formattedValue}}
				{{else}}
					<spring:theme code="basket.page.free"/>
				{{/if}}
			</dd>
		{{/if}}
		<dt class="total"><spring:theme code="basket.page.totals.total"/></dt>
		<dd class="total">{{= totalPrice.formattedValue}}</dd>
	</dl>
	{{if !net}}
		<p><spring:theme code="basket.page.totals.grossTax" arguments="{{= totalTax.formattedValue}}" argumentSeparator="!!!!" /></p>
	{{/if}}

</script>
<script>
function refreshEstimatedShipping(data)
{
 
 var deliveryCost=data['deliveryCost'];
 var total=data['totalAmount'];

 var totalTax=data['totalTax'];
 var taxAmt=totalTax.formattedValue;
 var orderPromo=data['orderDiscounts'];
 var orderDisc=orderPromo.formattedValue;
 var totalPrice=total.formattedValue;
 

 if(deliveryCost!=null)
{
 var estimatedShipping=deliveryCost.formattedValue;
 $('.estimated').html(estimatedShipping);
}
 if(orderDisc!= "$0.00")
{
	 $('.orderDiscvalue').addClass('redcolor').html("-".concat(orderDisc));
}
else
{
$('.orderDiscvalue').html(orderDisc); 
$('.orderDiscvalue').removeClass('redcolor').html(orderDisc);
} 

$('.taxValue').html(taxAmt);
$('.ordertotalvalue').html(totalPrice); 
 
 }
</script>
			<table border="0" width="100%" style="font-size:12px;text-align: right;">
				<tbody >
					<tr>
						<td><spring:theme code="checkout.summary.orderSubtotal" /></td>
						<td class="totalvalue"><format:price priceData="${cartData.subTotal}"/></td>
					</tr>
					<tr>
						<td><spring:theme code="checkout.summary.productDiscount" /></td>
						<c:if test="${empty cartData.productDiscounts.formattedValue}">
							<td class="totalvalue">$0.00</td>
							</c:if>
						<td class="totalvalue">
									<c:choose>
									<c:when test="${cartData.productDiscounts.value gt 0}">
									<span class="redcolor">	-${cartData.productDiscounts.formattedValue}</span>
									</c:when>
									<c:otherwise>
										${cartData.productDiscounts.formattedValue}
									</c:otherwise>
									</c:choose>
									</td>
					</tr>
					<tr>
								<td><spring:theme code="checkout.summary.promoDiscount"/></td>
								<c:if test="${empty cartData.orderDiscounts.formattedValue }">
										<td class="orderDiscvalue">$0.00</td>
									</c:if>
									<c:choose>
									<c:when test="${cartData.voucherCode ne 'LOYALTYVOUCHER' && cartData.orderDiscounts.value gt 0}">
										<td class="orderDiscvalue redcolor">-${cartData.orderDiscounts.formattedValue}</td>
									</c:when>
									<c:otherwise>
										<td class="orderDiscvalue">$0.00</td>
									</c:otherwise>
									
									</c:choose>
							</tr>
							<tr>
								<td><spring:theme code="checkout.summary.clubRewards"/></td>
								<c:choose>
									<c:when test="${cartData.voucherCode eq 'LOYALTYVOUCHER' && cartData.orderDiscounts.value gt 0}">
										<td class="totalvalue redcolor">-${cartData.orderDiscounts.formattedValue}</td>
									</c:when>
									<c:otherwise>
										<td class="totalvalue">$0.00</td>
									</c:otherwise>
									</c:choose>
							</tr>
					<tr>
						<td>
							<div class="estimated_shipping">
								<span><spring:theme code="checkout.summary.estimateShipping" /></span>
							</div>
						</td>
						 <td class="totalvalue estimated"><format:price priceData="${cartData.deliveryCost}"/></td>				
					</tr>
					<tr>
						<td>
							<div class="estimated_shipping">
								<spring:theme code="basket.total.tax"/>
							</div>
						</td>
						 <c:if test="${empty cartData.totalTax.formattedValue }">
										<td class="totalvalue">$0.00</td>
									</c:if>
						<td class="taxValue"><format:price priceData="${cartData.totalTax}"/></td>
					</tr>
					</tbody>
				</table>
		

			

	