ACCMOB.menu = {
		bindMenuButton: function(menuButton) {
			if (menuButton.length > 0) {
				menuButton.live("click", function() {
					var options = $("#popupMenu").data("options");
					$.extend(options, {blankContent: $("#popupMenu").html()});
					var dialog = $(document.createElement('div'));
					dialog.simpledialog2(options);
				});
			}
		},

		bindConnectButton: function(connectButton) {
			if (connectButton.length > 0) {
				connectButton.live("click", function(event) {
					var options = $("#connect-popup").data("options");
					$.extend(options, {blankContent: $("#connect-popup").html()});
					var dialog = $(document.createElement('div'));
					dialog.simpledialog2(options);
				});
			}
		},

		/* Header buttons and popups */
		togglePopup: function(popupId) {
			if (ACCMOB.menu.visiblePopupId != '') {
				$(ACCMOB.menu.visiblePopupId).css("display", "none");
				$("#"+ACCMOB.menu.clickPreventLayerSelector).remove();

				if (popupId != ACCMOB.menu.visiblePopupId) {
					ACCMOB.menu.showPopup(popupId);
				}
				else {
					ACCMOB.menu.visiblePopupId = '';
				}
			}
			else {
				ACCMOB.menu.showPopup(popupId);
			}
		},

		showPopup: function(popupId) {
			$("body").append($("<div id='"+ACCMOB.menu.clickPreventLayerSelector+"'>"));
			$(popupId).show();
			ACCMOB.menu.visiblePopupId = popupId;
		},

		headerButtonClicked: function(buttonId, popupId) {
			var button = $(buttonId);
			var buttonPosition = button.position();
			var buttonLeft = buttonPosition.left;
			var buttonRight = buttonLeft + button.width();
			var windowWidth = $(window).width();
			var popupLeft = buttonLeft;
			var popup = $(popupId);

			if ((buttonLeft + popup.outerWidth()) > windowWidth) {
				var popupLeft = buttonRight - popup.width();
			}

			popup.css("left", popupLeft);
			popup.css("top", $("#top-nav-bar").outerHeight());

			ACCMOB.menu.togglePopup(popupId);
		},

		footerButtonClicked: function(buttonId, popupId) {
			var button = $(buttonId);
			var buttonOffset = button.offset();
			var buttonLeft = buttonOffset.left;
			var buttonRight = buttonLeft + button.outerWidth();
			var buttonTop = buttonOffset.top;
			var windowWidth = $(window).outerWidth();
			var popup = $(popupId);
			var popupTop = (buttonTop - popup.outerHeight()) - 1;
			var marginWidth = $("#body").width() - $("[data-role='content']").width();
			var popupLeft = (buttonRight - popup.outerWidth()) - marginWidth;

			if (buttonRight > windowWidth) {
				var popupLeft = buttonRight - $(popupId).width();
			}

			$(popupId).css("left", popupLeft);
			$(popupId).css("top", popupTop);

			ACCMOB.menu.togglePopup(popupId);
		},

		bindNavigationButtons: function() {
			if ($("#top-nav-bar-settings").length > 0) {
				$("#top-nav-bar-settings").live("click", function(event) {
					ACCMOB.menu.headerButtonClicked("#top-nav-bar-settings", "#currencyLanguageSelector");
					return false;
				});
			}

			if ($("#top-nav-bar-account").length > 0) {
				$("#top-nav-bar-account").live("click", function(event) {
					ACCMOB.menu.headerButtonClicked("#top-nav-bar-account", "#userSettings");
					return false;
				});
			}

			if ($("#top-nav-bar-menu").length > 0) {
				$("#top-nav-bar-menu").live("click", function(event) {
					ACCMOB.menu.headerButtonClicked("#top-nav-bar-menu", "#menuContainer");
					return false;
				});
			}
		},

		bindClickPreventLayer: function(clickPreventLayerSelector) {
			ACCMOB.menu.clickPreventLayerSelector = clickPreventLayerSelector;

			$("#"+clickPreventLayerSelector).live("vclick", function() {
				ACCMOB.menu.togglePopup();
				$("#"+clickPreventLayerSelector).remove();
			});
		}

		/* End header buttons and popups */

};

$(document).ready(function() {
	with(ACCMOB.menu) {
		bindMenuButton($("#menuButton"));
		bindNavigationButtons();
		bindConnectButton($("#connect-button"));
		bindClickPreventLayer("clickPreventLayer");
	}
});
