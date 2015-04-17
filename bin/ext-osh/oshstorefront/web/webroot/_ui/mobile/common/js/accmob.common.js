var ACCMOB = {};

ACCMOB.common = {
		currentPath : window.location.pathname,
		visiblePopupId : '',
		timeouts : [],
		dialogTopOffset : 45,

		clearAllTimeouts : function() {
			for (key in ACCMOB.common.timeouts) {
				clearTimeout(ACCMOB.common.timeouts[key]);
			}
			ACCMOB.common.timeouts = [];
		},

		lockScreen : function() {
			$("#body").css({
				overflow : 'hidden',
				height : window.outerHeight
			});
			$('.ui-simpledialog-container').css({
				top : window.body.scrollTop + ACCMOB.common.dialogTopOffset
			});
		},

		unlockScreen : function() {
			$("#body").css({
				overflow : 'visible'
			});
			ACCMOB.common.clearAllTimeouts();
		},

		bindFormLinks : function() {
			$('.formLink').live("click", function() {
				$(this).parent("form").submit();
				return false;
			});
		},

		/*
		 * Show all items or View Less items for <ul>. numberOfItems(numbering starts from 0) is a mandatory
		 * attribute in ul tag.
		 */
		bindShowAllItems : function() {
			$('.showAllItems').live("click", function() {
				$('#cartTotals').show();

				var numberOfItems = $('.itemsList').attr('numberOfItems');
				var liFilterClass = $('.itemsList').attr('liFilterClass');
				$('.viewLess').removeAttr('style');
				$(this).hide();
				$('.itemsList').find('li').show().end().append($('.viewLess')
						.live("click", function() {
							$('.itemsList li').filter('.' + liFilterClass)
							.filter('li:gt(' + numberOfItems + ')').hide();
							$(this).attr('style', 'display:none');
							$('.showAllItems').show();
							$('#cartTotals').hide();
						}));

			});
		},

		/* Show spinner on all links */
		allLinks : function() {
			// select all links that are not #, '' or javascript:void();
			$("a:not([href*='#'], [href^=''], [href^='javascript'])")
			.live("click", function() {
				$.mobile.showPageLoadingMsg();
			});
		},

		/* Show spinner on all form posts */
		allFormPosts : function() {
			// select all links that are not #, '' or javascript:void();
			$('form[method="post"]').live("submit", function() {
				$.mobile.showPageLoadingMsg();
			});
		},

		bindLangAndCurrencySelectors : function() {
			$('#lang-selector').live("change", function() {
				$('#lang-form').submit();
			});

			$('#currency-selector').live("change", function() {
				$('#currency-form').submit();
			});
		},

		bindCarouselLink : function() {
			var carouselLink = $('ul[id|="carousel_"] > li a');
			if (carouselLink.length > 0) {
				carouselLink.colorbox();
			}
		},
		preventDefault : function(event) {
			if (!(typeof event === "undefined") && !!event) {
				event.preventDefault();
			}
		},

		bindToSortForm : function(event) {
			$('#sort_form1').live("change", function() {
				$('#sort_form1').submit();
			});
			$('#sort_form2').live("change", function() {
				$('#sort_form2').submit();
			});
		}

};

$(document).ready(function() {
	with (ACCMOB.common) {
		bindFormLinks();
		bindShowAllItems();
		allLinks();
		allFormPosts();
		bindLangAndCurrencySelectors();
		bindCarouselLink();
		bindToSortForm();
	}
});

$(document).bind("mobileinit", function() {
	var currentUrl = $.mobile.path.parseUrl(window.location.href);

	if (currentUrl.hash !== "" && !!currentUrl.hash.match(/^./)) {
		$.mobile.hashListeningEnabled = false;
	}
});