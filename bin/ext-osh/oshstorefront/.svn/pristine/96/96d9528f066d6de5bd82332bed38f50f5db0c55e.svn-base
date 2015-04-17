ACCMOB.cart = {
		bindCycleFocusEvent: function() {
			$('#lastInTheForm').blur(function() {
				$('#paymentDetailsForm [tabindex$="10"]').focus();
			})
		},

		updateBillingAddressForm: function() {
			var editMode = ("true" == $('#newBillingAddressFields').attr('edit-mode'));
			var newAddress = $('#differentAddress').attr("checked");

			if (editMode || newAddress) {
				$("#newBillingAddressFields :input").removeAttr('disabled');
			} else {
				$("#newBillingAddressFields :input").attr('disabled', 'disabled');
			}
		},

		clickDifferentAddress: function() {
			$("#differentAddress").click(function() {
				var differentAddress = $("input:checkbox[id='differentAddress']:checked").val();
				if (differentAddress) {
					$('#addBillingAddressForm').removeAttr('style');
					$('#addBillingAddressForm').trigger('expand');
				} else {
					$('#addBillingAddressForm').attr('style','display:none');
					$('#addBillingAddressForm').trigger('collapse');
				}
			})
		},

		bindToQuantitySelector: function() {
			$('.quantitySelector').live("change", function() {
				$.mobile.ajaxEnabled = false;
				$('#updateCartForm' + $(this).attr('entryNumber')).get(0).submit();
			});
		},

		bindPlaceOrderWithSecurityCodeButton: function() {
			$('.placeOrderWithSecurityCode').live("click",function() {
				var securityCode = $("#SecurityCode").val();
				$(".securityCodeClass").val(securityCode);
				$("#placeOrderForm1").submit();			
			});
		},

		bindToCvv2DescriptionLink: function() {
			$('#cvv2Description').live("click", function() {
				event.preventDefault();
				var dialog = $(document.createElement('div'));
				dialog.simpledialog2({
					mode:         'blank',
					dialogForce:  true,
					showModal:    true,
					headerText:   '',
					headerClose:  true,
					animate:      false,
					zindex:       9999,
					blankContent: $(this).data("cvv2description")
				});
				ACCMOB.common.lockScreen();
			});
		},

		bindToRadioButtons: function() {
			$('input[type="radio"]').live("click", function() {
				this.checked = true;
			});
		},

		defaultDialogConfig: {
			mode:			'blank',
			dialogForce:	false,
			showModal:		true,
			headerText:		'',
			headerClose:	true,
			animate:		false,
			zindex:			9999,
			blankContent:	'',
			themeDialog:	'd',
			callbackOpen:	ACCMOB.common.lockScreen,
			callbackClose:	ACCMOB.common.unlockScreen
		},

		createDialog: function() {

			var headerText = "Help";
			var basketId = $('#helpLink').data("cartcode");
			var data = $('#modalHelpMessage').html();
			$.extend(ACCMOB.cart.defaultDialogConfig, {headerText: headerText, blankContent: data});
			var dialog = $(document.createElement('div'));
			dialog.simpledialog2(ACCMOB.cart.defaultDialogConfig);

			return $.mobile.sdCurrentDialog;
		},

		bindToHelpButton: function() {
			$("#helpLink").click(function() {
				ACCMOB.cart.createDialog();
			});
		}
};

$(document).ready(function() {	
	with (ACCMOB.cart) {
		updateBillingAddressForm();
		clickDifferentAddress();
		bindCycleFocusEvent();
		bindToQuantitySelector();
		bindPlaceOrderWithSecurityCodeButton();
		bindToCvv2DescriptionLink();
		bindToRadioButtons();
		bindToHelpButton();
	}
});
