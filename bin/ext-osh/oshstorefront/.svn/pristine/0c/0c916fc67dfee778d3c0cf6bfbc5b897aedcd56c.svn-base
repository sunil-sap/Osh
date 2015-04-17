ACCMOB.account = {
		defaultDialogConfig: {
			mode:				'button',
			forceInput:			true,
			headerText:			'',
			buttonPrompt:		'',
			headerClose:		true,
			inputPassword:		false,
			animate:			false,
			safeNuke:			true,
			opacity:			0.0,
			buttons: {
				'OK':			{ click: function() { }, icon: "ok", theme: "d"},
				'Cancel':		{ click: function() { }, icon: "delete", theme: "b"}
			},
			callbackClose:		true,
			themeDialog:		'b',
			themeInput:			'e',
			themeButtonDefault:	false,
			themeHeader:		'i'
		},

		createDialog: function(buttonPrompt, headerText, buttons, config) {
			$.extend(config, {buttonPrompt: buttonPrompt, buttons: buttons, headerText: headerText });
			var dialog = $(document.createElement('div'));
			dialog.simpledialog2(config);
			ACCMOB.common.lockScreen();
		},

		bindToRemoveAddressButton: function() {
			$('.remove_address').live("click", function (event) {
				var addressId = $(this).attr("addressId");
				ACCMOB.account.createDialog(
						$(this).data('message'),
						$(this).data('headertext'),
						{
							'OK':		{ click: function() { window.location.href = ACCMOB.common.contextPath + "/my-account/remove-address/" + addressId; return false; } },
							'Cancel':	{ click: function() { }, icon:  "delete", theme: "c" }
						},
						ACCMOB.account.defaultDialogConfig
				);
			});
		},

		bindToRemovePaymentCardButton:function ()
		{
			/* prevent payment remove form from submitting normaly */
			$('.removePaymentCardForm').live("submit", function(event) {
				if ($(this).data("removeConfirmed") == "true") {
					return true;
				} else {
					ACCMOB.common.preventDefault(event);
				}
			});

			$('.removePaymentCardButton').live("click", function(event) {
				var pid = $(this).attr("pid");
				var removePaymentCardForm = $("#removePaymentCardForm" + pid);
				ACCMOB.common.preventDefault(event);
				var config = ACCMOB.account.defaultDialogConfig;
				ACCMOB.account.createDialog( $(this).data('message'), $(this).data('headertext'), {
					'OK': { click: function() { removePaymentCardForm.data("removeConfirmed", "true"); removePaymentCardForm.submit(); return true; } },
					'Cancel': { click: function() { }, icon:  "delete", theme: "c" }
				} , config);
			});
		},

		bindToSetDefaultPaymentButton: function() {
			$('.setDefaultPayment').live("click", function() {
				$('#setDefaultPaymentDetails' + $(this).attr("pid")).submit();
				return false;
			});
		},

		bindToRemovePaymentDetailsButton: function() {
			$('.removePaymentDetail').live("click", function() {
				$('#removePaymentDetails' + $(this).attr("pid")).submit();
				return false;
			});
		}
};

$(document).ready(function() {
	with (ACCMOB.account) {
		bindToRemoveAddressButton();
		bindToSetDefaultPaymentButton();
		bindToRemovePaymentCardButton();

	}
});
