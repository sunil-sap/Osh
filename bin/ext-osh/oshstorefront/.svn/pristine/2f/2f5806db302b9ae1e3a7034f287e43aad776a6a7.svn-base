<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib prefix="account"
	tagdir="/WEB-INF/tags/desktop/account"%>    
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
  
   <div id="addPaymentDetailsPoppup">
   <div id="loding" style="display: none" class="spinnerwidth">
	<img src="${commonResourcePath}/images/spinner.gif"/>
	</div>
	      <account:addNewPatmentCard/>
   </div>
   
   <script type="text/javascript">
/* 	var form = $('#createaccount');
	//$('.passworderror').hide();

	$.ajax({
		url : form.attr('action'),
		type : 'POST',
		dataType : 'json',
		data : form.serialize(),
		error:function(data){alert("KKK")},
		success : function(data) {
			alert("111");
			$('.addPaymentDetails').html("");
			$('.addPaymentDetails').html(data);

			$('.order_confirmation_msg').removeAttr('style');
		}
	}); */

	$(document).ready(function() {
		$('#useCardDetail').click(function(e) {
			$('#loding').show();
			var form = $('#create_update_payment_form');
			e.preventDefault();
			$.ajax({
				url : form.attr('action'),
				type : 'POST',
				data : form.serialize(),
				target :'#addPaymentDetailsPoppup',
				beforeSubmit: function() {
					alert('before submit');
					
				},
				success : function(data) {
					$('#loding').hide();
					if(data=='')
					{
						$.colorbox.close();	
					window.location.href = '';
					}					
						$('#addPaymentDetailsPoppup').html(data);						
					},
			 		error: function () {
			 			$('#loding').hide();
			        alert("Failed to create subscription");
			      }
			});

		});
	});
</script>