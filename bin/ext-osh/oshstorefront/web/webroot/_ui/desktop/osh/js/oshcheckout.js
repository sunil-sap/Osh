/*checkout page js*/

function placeOrderWithSecurityCode()
{
	var securityCode = $("#SecurityCode").val();
	$(".securityCodeClass").val(securityCode);
	document.getElementById("placeOrderForm1").submit();
}

$(document).ready(function(){
	$("#Terms1").click(function() {
		var terms1enable = $('#Terms1').attr("checked");
		if(terms1enable == undefined || terms1enable == 'false'){
			$('#Terms2').attr("checked",false);
		} else {
			$('#Terms2').attr("checked",true);
		}
	});
	
	$("#Terms2").click(function() {
		var terms2enable = $('#Terms2').attr("checked");
		if(terms2enable == undefined || terms2enable == 'false')
		{				
			$('#Terms1').attr("checked",false);
		} else {
			$('#Terms1').attr("checked",true);
		}
	});
});

$(document).ready(function() {
	updatePlaceOrderButton();
});

function updatePlaceOrderButton()
{
	var deliveryAddress = $("#checkout_summary_deliveryaddress_div").hasClass("complete");
	var deliveryMode = $("#checkout_summary_deliverymode_div").hasClass("complete");
	var paymentDetails = $("#checkout_summary_payment_div").hasClass("complete");

	if (deliveryAddress && deliveryMode && paymentDetails)
	{
		$(".place-order").removeAttr('disabled');
	}
	else
	{
		$(".place-order").attr('disabled', true);
	}
}


$(document).ready(function(){
	
	$('#linkeditpaymentmethod').click(function() {
		$('#update_payment_method').dialog({
		width:820,
		title:'Payment Method',
		modal: true
		});
    });
	
	$('#usethisaddleft1').click(function() {
        $('#update_shipping_method').dialog('close');
    });
	
	$('#linkaddaddress').click(function() {
		alert("working");
		$('#update_delivery_add').dialog({
		width:860,
		title:'Delivery Address',
		modal: true
		});
		$('#update_delivery_add').load("addAddress.jsp");
    });
	
	$('#usethisaddleft1').click(function() {
        $('#update_shipping_method').dialog('close');
    });
	
	
	$('#addshippingmethod').click(function() {
		$('#update_shipping_method').dialog({
		width:500,
		title:'Shipping Method',
		modal: true
		});
    });
	
	$('#btn_updateshippingmethod').click(function() {
        $('#update_shipping_method').dialog('close');
    });
	
	
	$('#address,#usepaymentdetails,#updateProfile').validate();
	//alert('f');
	$('#saveProfiledetails').click(function(e) {
					e.preventDefault();
					var allfilled = true;
					/* $(".error-msg").hide();
					if (!$("#email").val()) {										
						$("#error-msg-email").show();
						allfilled = false;
					}
					var email = $("#email").val();
					var emailRegex = new RegExp(
					/^[+a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i);
					var email1 = emailRegex.test(email);
					if (email !="" && !email1) {
						$("#error-msg-email").show();										
						allfilled = false;
					} */
					var firstName = $("#firstName").val();
					var lastName = $("#lastName").val();
					var line1 = $("#line1").val();
					var townCity = $("#townCity").val();
					var postcode = $("#postcode").val();
					var phoneNo = $("#phoneNo").val();
					
					
					
					if (firstName=='') {
						$("#error-msg-firstName").show();
						allfilled = false;
					}
					if (lastName=='') {
						$("#error-msg-surname").show();
						allfilled = false;
					}
					if (line1=='') {
						$("#error-msg-line1").show();
						allfilled = false;
					}
					if (townCity=='') {
						$("#error-msg-townCity").show();
						allfilled = false;
					}
					if (postcode=='') {
						$("#error-msg-postcode").show();
						allfilled = false;
					}
					if (phoneNo=='') {
						$("#error-msg-phoneNo").show();
						allfilled = false;
					}
					
					var phoneNumber = phoneNo;
					 var phoneRegex = new RegExp(/^[0-9]+$/);
					  var phoneNo1=phoneRegex.test(phoneNumber);
					  
					  if(!phoneNo1){
						  $("#error-msg-phoneNo").show();
							allfilled = false;
					  }
					  if (phoneNo.length<10) {
					   $("#error-msg-phoneNo")
					     .show();
					  
					   allfilled = false;
					  }
					
					if (allfilled) {

						$("#updateProfile").submit();
					}

				});
	
	
});