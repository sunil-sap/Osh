<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>

<c:url value="/cart/miniCart/${totalDisplay}" var="refreshMiniCartUrl" />
<c:url value="/cart/rollover/${component.uid}" var="rolloverPopupUrl" />
<script id="miniCartTemplate" type="text/x-jquery-tmpl">
/*<![CDATA[*/

	<dt>
		<ycommerce:testId code="miniCart_items_label">
			cart items ${totalItems}
		</ycommerce:testId>
		-
	</dt>
	<dd>
		<ycommerce:testId code="miniCart_total_label">
			<c:if test="${totalDisplay == 'TOTAL'}">
				{{= totalPrice.formattedValue}}
			</c:if>
			<c:if test="${totalDisplay == 'SUBTOTAL'}">
				{{= subTotal.formattedValue}}
			</c:if>
			<c:if test="${totalDisplay == 'TOTAL_WITHOUT_DELIVERY'}">
				{{= totalNoDelivery.formattedValue}}
			</c:if>
		</ycommerce:testId>
	</dd>

/*]]>*/
</script>
<script type="text/javascript">
	/*         */
	function refreshMiniCart() {
		var refrrshNewUrl = '${refreshMiniCartUrl}';
		$.ajax({
			async: true,
		    type: "GET",
		    cache: false,
		    dataType: "html",
		    url: refrrshNewUrl,
		    success: function(data){
		    	$('#minicart_data').html('');
				$('#minicart_data').html(data);
		    }
	});
		
		/* $.get(refrrshNewUrl, function(result) {
			alert(refrrshNewUrl+' refreshMiniCartUrl ');
			alert('result: '+result);
			$('#minicart_data').html('');
			$('#minicart_data').html(result);
		}); */
	}

	$(document)
			.ready(
					function() {

						$('#rollover_cart_popup').hide();

						$('#headerCart').hover(function() {
							$.data(this, 'hover', true);
						}, function() {
							$.data(this, 'hover', false);
						}).data('hover', false);

						$('#rollover_cart_popup').hover(function() {
							$.data(this, 'hover', true);
						}, function() {
							$.data(this, 'hover', false);
						}).data('hover', false);

						$('#headerCart')
								.mouseenter(function() {
											$('#shopcartpopup').hide();
											var rolloverUrl = '${rolloverPopupUrl}';
											$.ajax({
												async: true,
											    type: "GET",
											    cache: false,
											    dataType: "html",
											    url: rolloverUrl,
											    success: function(data){
											    	$('#rollover_cart_popup').html('');
											    	$('#rollover_cart_popup').html(data);
											    	$('#ajax_cart_close').click(function(e) {
														e.preventDefault();
														$('#rollover_cart_popup').hide();
													});
													$('#rollover_cart_popup').slideDown('slow');
											    }
											});
											/* $.get('${rolloverPopupUrl}',function(result) {
													$('#rollover_cart_popup').html(result);
													$('#ajax_cart_close').click(function(e) {
														e.preventDefault();
														$('#rollover_cart_popup').hide();
													});
													$('#rollover_cart_popup').slideDown('slow');
											}); */
										});
						$('#headerCart').mouseleave(function() {
							setTimeout(function() {
							if (!$('#headerCart').data('hover') && !$('#rollover_cart_popup').data('hover')) {
								$('#rollover_cart_popup').slideUp('fast');
							}
						}, 2000);
					});

						$('#rollover_cart_popup').mouseenter(function() {
							$('#rollover_cart_popup').show();
						});
						$('#rollover_cart_popup')
								.mouseleave(
										function() {
											setTimeout(
													function() {
														if (!$('#headerCart')
																.data('hover')
																&& !$(
																		'#rollover_cart_popup')
																		.data(
																				'hover')) {
															$(
																	'#rollover_cart_popup')
																	.fadeOut();
														}
													}, 1000);
										});
					});
	/*   */
</script>
<div id="headerCart">
	<div id="minicart_data">
		<span> <span class="clearb floatl width100p"> <span
				class="cartlabel"> <span class="clearb floatl"><spring:message
							code="osh.storeLocator.page.title.yourcart" /></span> <span
					class="clearb floatl bold"> <span>${totalItems} </span> <spring:message
							code="osh.storeLocator.page.heder.items" /> <span> <format:price
								priceData="${totalPrice}" />
					</span>
				</span>
			</span> <span class="carticon"><img
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
	</div>
</div>