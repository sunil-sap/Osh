<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="deliveryMode" required="true" type="de.hybris.platform.commercefacades.order.data.DeliveryModeData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ attribute name="deliveryModeCode" required="true" type="java.lang.String" %>
<spring:url value="/checkout/single/summary/getDeliveryModes.json" var="getDeliveryModesUrl" />
<spring:url value="/checkout/single/summary/setDeliveryMode.json" var="setDeliveryModeUrl" />
<%@ attribute name="surcharge" required="true" type="java.lang.Boolean" %>
<script type="text/javascript">
 var deliverycode="${deliveryModeCode}"; 
/* var deliverycode=""; */
/*<![CDATA[*/
	$(document).ready(function() {
		bindEditDeliveryMethodButton();
		
		
		$("#isgift").click(function() {
			var isGift = $('#isgift').attr("checked");
			if (isGift) {
				$("#giftMessage").removeAttr('disabled');
			} else {
				$("#giftMessage").attr('disabled', 'disabled');
				$("#giftMessage").val('');
			}
					
		})
	});
	
	
	
	
	function bindEditDeliveryMethodButton()
	{
		
		$('div.checkout_summary_flow_b .change_mode_button').click(function() {
			
			$.ajax({
				url: '${getDeliveryModesUrl}',
				type: 'POST',
				dataType: 'json',
				success: function(data) {
								
					// Fill the available delivery addresses and select button
					$('#delivery_modes_dl').html($('#deliveryModesTemplate').tmpl({deliveryModes: data}));
					$('#delivery_modes_button').html($('#deliveryModeButton').tmpl({deliveryModes: data}));
					// Show the delivery modes popup
					$.colorbox({inline:true, href:"#popup_checkout_delivery_modes",width:false, height: false, overlayClose: false});
					changeDeliveryMode();
					bindUseThisDeliveryMode();					
				},
				error: function(xht, textStatus, ex) {
					alert("Failed to get delivery modes. Error details [" + xht + ", " + textStatus + ", " + ex + "]");
				}
			});
			return false;
		});
	}

	function bindUseThisDeliveryMode()
	{
		
		$('#use_this_delivery_method').click(function() {
			$('#popup_checkout_delivery_modes').block({ message: $("#Shippingloadingimg") });
			var selectedCode = $('input:radio[name=delivery]:checked').val();
			if(selectedCode)
			{
				$.ajax({
					url: '${setDeliveryModeUrl}',
					type: 'POST',
					dataType: 'json',
					data: {modeCode: selectedCode},
					success: function(data) {
						$('#popup_checkout_delivery_modes').unblock();
						if(data != null)
						{
							refreshPage(data);
							parent.$.colorbox.close();
							 var deliveryMode=data['deliveryMode'];
							 deliverycode=deliveryMode.code;
							 refreshDeliveryMethodSection(data);
						}
						else
						{
							alert("Failed to set delivery mode");
						}
						
		                $("#use_this_delivery_method").attr("disabled", false);
				    },
					error: function(xht, textStatus, ex) {
						alert("Ajax call failed while trying to set delivery mode. Error details [" + xht + ", " + textStatus + ", " + ex + "]");
					}
				});

			}
			return false;
		});
	}
	/* function updatedDeliveryMode(code)
	 {
		 deliveryCode=code;
	 }
	 */
	function refreshDeliveryMethodSection(data)
	{
		
		$('#checkout_summary_deliverymode_div').replaceWith($('#deliveryModeSummaryTemplate').tmpl(data));
		bindEditDeliveryMethodButton();		
		refreshEstimatedShipping(data);
		var deliveryMode=data['deliveryMode'];
		var deliveryCode=deliveryMode.code;
		 changeDeliveryMode();
		 $("#isgift").click(function() {
				var isGift = $('#isgift').attr("checked");
				if (isGift) {
					$("#giftMessage").removeAttr('disabled');
				} else {
					$("#giftMessage").attr('disabled', 'disabled');
					$("#giftMessage").val('');
				}
						
			})
	}
	function changeDeliveryMode()
	{
		if(deliverycode =='')
			{
			$('#colorbox').find("#ground-gross").attr('checked','checked');
			}
		else
		{
		$('#colorbox').find("#"+deliverycode).attr('checked','checked');
		}
		
	}	
	
/*]]>*/
</script>

<script id="deliveryModeSummaryTemplate" type="text/x-jquery-tmpl">
<div class="checkout_summary_flow_b {{if deliveryMode}}complete{{/if}}" id="checkout_summary_deliverymode_div">
	<div>
		<ycommerce:testId code="checkout_deliveryModeData_text">
			<div>
				<h2>
					<span class="step">2</span>
					Shipping Method
				</h2>
				<div class="borderbox">
				<div class="cartformwarp">
				<div class="cartformwarp_inner">
				<ul>
					{{if shippingSurchargeStatus}}
											<div class="required_field">
												<spring:message	code="order.surcharge.status" />
											</div>
					{{/if}}
					{{if deliveryMode}}
						<li>{{= deliveryMode.description}} - {{= deliveryMode.deliveryCost.formattedValue}}</li>
								<div class="giftData1" style="display: block">
										<p></p>	
										<form:form action="${request.contextPath}/checkout/single/placeOrder" id="giftbox"
									commandName="placeOrderForm">										
									<form:checkbox id="isgift" value="true" path="gift" />&nbsp
									<label for="SaveAddress">This is a Gift</label>
									<p class="giftmessage">Gift Message</p>
									<form:textarea path="giftMessage" cssStyle="gifttextarea" id="giftMessage" disabled="true" maxlength="100"/>
									</form:form>
								</div>
					{{else}}
						<li><spring:theme code="checkout.summary.deliveryMode.noneSelected"/></li>
					{{/if}}
				</ul>

			</div>
		{{if deliveryMode}}
		<ycommerce:testId code="checkout_changeDeliveryMode_element">
		<a href="#" class="edit_complete change_mode_button linkarrow" ><spring:theme code="checkout.summary.deliveryMode.editDeliveryMethod"/></a>
		</ycommerce:testId>
		{{else}}
		<ycommerce:testId code="checkout_changeDeliveryMode_element linkarrow">
		<button class="form change_mode_button" ><spring:theme code="checkout.summary.deliveryMode.editDeliveryMethod"/></button>
		</ycommerce:testId>
		{{/if}}
			</div>
			</div>
		</div>
		</ycommerce:testId>
	</div>
</div>
</script>

<script id="deliveryModesTemplate" type="text/x-jquery-tmpl">
	{{if !deliveryModes.length}}
		<spring:theme code="text.checkout.noDeliveryModes"/>
	{{/if}}
	{{if deliveryModes.length}}
		{{each deliveryModes}}
			<dt class="left">
				{{if $index == 0}}
					<input type="radio" name="delivery" value="{{= $value.code}}" checked="checked"/>
				{{else}}
					<input type="radio" name="delivery" id="{{= $value.code}}" value="{{= $value.code}}"/>
				{{/if}}
				<label for='{{= $value.code}}'>{{= $value.name}} - {{= $value.description}} - {{= $value.deliveryCost.formattedValue}}</label>
			</dt>
			<dd></dd>
		{{/each}}
	{{/if}}
</script>

<script id="deliveryModeButton" type="text/x-jquery-tmpl">
	{{if deliveryModes.length}}
		<ycommerce:testId code="checkout_chooseSelectedDeliveryMethod">
			<button class="form" id="use_this_delivery_method"><spring:theme code="checkout.summary.deliveryMode.useThisDeliveryMethod"/></button>
			<img src="/oshstorefront/_ui/desktop/common/images/spinner.gif" style="display: none;" id="Shippingloadingimg">
			</ycommerce:testId>
	{{/if}}
</script>


<c:set value="${not empty deliveryMode}" var="deliveryModeOk"/>

<div class="checkout_summary_flow_b ${deliveryModeOk ? 'complete' : ''}" id="checkout_summary_deliverymode_div">
	<div>
		<ycommerce:testId code="checkout_deliveryModeData_text">
			<div>
				<h2>
					<span class="step"><spring:theme code="text.two"/></span>
					<spring:theme code="checkout.summary.deliveryMode.shippingmethod"/>
				</h2>
				<div class="borderbox">
					<div class="cartformwarp">
					<div class="cartformwarp_inner">
						<ul>
							<c:choose>
								<c:when test="${deliveryModeOk}">
									<script type="text/javascript">
										$(document)
												.ready(
														function() {
																	var str = $(
																	".deliverymode-description")
																	.text();
															if (str.length > 180) {
																str = str
																		.substr(
																				0,
																				180)
																		+ '...';
															}
															$(
																	".deliverymode-description")
																	.html(str);
														});
									</script>
										
										<c:if test="${surcharge}">
											<div class="required_field">
												<spring:message	code="order.surcharge.status" />
											</div>
										</c:if>
										<li class="deliverymode-description"
										title="${deliveryMode.description} - ${deliveryMode.deliveryCost.formattedValue}">${deliveryMode.description}
										- ${deliveryMode.deliveryCost.formattedValue}</li>									
										<div class="giftData1">
										<p></p>
										 <form:form action="${request.contextPath}/checkout/single/placeOrder" id="giftbox"
									commandName="placeOrderForm">									
									<form:checkbox id="isgift" value="true" path="gift"/>&nbsp;
									<label for="SaveAddress"><spring:theme code="text.gift"/></label>
									<p class="giftmessage"><spring:theme code="gift.message"/></p>
									<form:textarea path="giftMessage" cssStyle="gifttextarea" id="giftMessage" disabled="true" maxlength="100"/>
									 </form:form> 
								</div>
								</c:when>
								<c:otherwise>
									<li><spring:theme
											code="checkout.summary.deliveryMode.noneSelected" /></li>
								</c:otherwise>
							
							</c:choose>
						</ul>
						</div> 
						<ycommerce:testId code="checkout_changeDeliveryMode_element">
							<c:choose>
								<c:when test="${deliveryModeOk}">
									<a href="#" class="edit_complete change_mode_button linkarrow"><spring:theme code="checkout.summary.deliveryMode.editDeliveryMethod"/></a>
								</c:when>
								<c:otherwise>
									<a href="#" class="form change_mode_button linkarrow"><spring:theme code="checkout.summary.deliveryMode.addDeliveryMethod"/></a>
								</c:otherwise>
							</c:choose>
						</ycommerce:testId>
					</div>
				</div>

			</div>
		</ycommerce:testId>
	</div>
</div>
<%-- <c:set id="dude" var="mode" value=""></c:set> --%>

<div style="display:none">
	<div id="popup_checkout_delivery_modes">
		<div class="item_container_holder">
				<div class="cartformwarp">
				  <div class="cartformwarp_inner">
				<p><spring:theme code="checkout.summary.deliveryMode.selectDeliveryMethodForOrder"/></p>
				<form>
					<ycommerce:testId code="checkout_deliveryModes">
						<dl id="delivery_modes_dl">
							<!-- available delivery modes for the cart -->
						</dl>
					</ycommerce:testId>
					<span style="display: block; clear: both;" id="delivery_modes_button">
						<!-- delivery mode select button -->
					</span>
				</form>
			</div>
		</div>
	</div>
  </div>
</div>


<%-- <div style="display:none">
   <div id="popup_checkout_delivery_modes">
	  <div class="">
			<div class="cartformwarp">
				<div class="cartformwarp_inner">
				<p><spring:theme code="checkout.summary.deliveryMode.selectDeliveryMethodForOrder"/></p>
				<form>
					<ycommerce:testId code="checkout_deliveryModes">
						<dl id="delivery_modes_dl">
							<!-- available delivery modes for the cart -->
						</dl>
					</ycommerce:testId>
					<span style="display: block; clear: both;" id="delivery_modes_button">
						<!-- delivery mode select button -->
					</span>
				</form>
				</div>
			</div>
		</div>
	</div> --%>