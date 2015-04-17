ACCMOB.product = {

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

		redirect: function(url) {

			if (url.indexOf("http://") == -1) {
				url = "http://" + url;
			}
			window.location = url;
		},

		initializeVariantSelectors: function(variantSelectorElement) {

			variantSelectorElement.change(function() {
				if ($(this).val() != "") {
					var url = $(location).attr('host') + $(this).val();
					ACCMOB.product.redirect(url);
				}
			});

		},

		changeDot: function(selectedIndex, galleryElement) {

			var dots = null;
			if (galleryElement.attr("id") == "imageGallery") {
				dots = $("#dots > span");
			}
			else if (galleryElement.attr("id") == "referenceGallery") {
				dots = $("#refDots > span");
			}

			$.each(dots, function(index) {
				var src = "";
				if (index == selectedIndex) {
					src = ACCMOB.common.commonResourcePath + "/images/closeddot.png";
				}
				else {
					src = ACCMOB.common.commonResourcePath + "/images/opendot.png";
				}
				$(this).html("<img src=\'" + src + "\'/>");
			});
			return false;
		},

		configureImageGallery: function(galleryElement) {
			if (!$.isEmptyObject(galleryElement)) {
				galleryElement.cycle({
					timeout: 0,
					fx:		'scrollHorz',
					next:	'#next',
					prev:	'#prev'
				});
			}
		},

		initializeGallery: function(galleryElement) {

			if (!$.isEmptyObject(galleryElement)) {
				ACCMOB.product.configureImageGallery(galleryElement);

				var numSlides;
				if (galleryElement.attr("id") == "imageGallery") {
					numSlides = galleryElement.find(".imageGallerySlide").size();
				}
				else if (galleryElement.attr("id") == "referenceGallery") {
					numSlides = galleryElement.find(".referencedSlide").size();
				}

				var index = 0;

				galleryElement.swipeleft(function() {
					if ($(this).data("enabled") == true) {
						index = Math.abs((index + 1 ) % numSlides);
						galleryElement.cycle("next");
						ACCMOB.product.changeDot(index, galleryElement);
					}
				});

				galleryElement.swiperight(function() {
					if ($(this).data("enabled") == true) {
						if (index == 0) {
							index = Math.abs(index - numSlides + 1);
						}
						else {
							index = Math.abs((index - 1) % numSlides);
						}
						galleryElement.cycle("prev");
						ACCMOB.product.changeDot(index, galleryElement);
					}
				});
			}
		},

		makeImageDraggable: function(image, slide) {

			var imageWidth		= $(slide).find('img').width();
			var imageHeight		= $(slide).find('img').height();
			var containerLeft	= $(slide).find(".imageContainer").offset().left;
			var containerTop	= $(slide).find(".imageContainer").offset().top;
			var containerWidth	= $(slide).find(".imageContainer").width();
			var containerHeight	= $(slide).find(".imageContainer").height();

			var boundary = [(containerLeft + containerWidth - imageWidth), (containerTop + containerHeight - imageHeight), (containerLeft), (containerTop)];

			$(slide).find(".imageContainer").draggable({ containment: boundary });
		},

		zoomToggle: function(slide) {

			if ($(slide).data('zoom') == 'in') {
				ACCMOB.product.zoomOut(slide);
				$(slide).data('zoom', 'out');
			}
			else if ($(slide).data('zoom') == 'out') {
				ACCMOB.product.zoomIn(slide);
				$(slide).data('zoom', 'in');
			}
		},

		zoomOut: function(slide) {

			$("#imageGallery").data('enabled', true);

			$(slide).find(".imageContainer").draggable({disabled: true});

			var image = "<div class='imageContainer'><img src='" + $(slide).data("primaryimagesrc") + "' alt='" + $(slide).data("alt") + "' title='" + $(slide).data("title") + "'/></div>";
			$(slide).find('.imageContainer').replaceWith(image);

			var zoomButton = $(slide).find('.zoomOutButton');
			$(zoomButton).removeClass('zoomOutButton');
			$(zoomButton).addClass('zoomInButton');
		},

		zoomIn: function(slide) {

			$("#imageGallery").data('enabled', false);

			var image = "<img src='" + $(slide).data("zoomimagesrc") + "' alt='" + $(slide).data("alt") + "' title='" + $(slide).data("title") + "'/>";
			var container = $(slide).find('.imageContainer');

			var centeringWidth = ($(image).width() - $(container).width()) / 2;
			var centeringHeight = ($(image).height() - $(container).height()) / 2;
			$(container).css('top', centeringHeight);
			$(container).css('left', centeringWidth);
			$(container).html(image);

			var zoomButton = $(slide).find('.zoomInButton');
			$(zoomButton).removeClass('zoomInButton');
			$(zoomButton).addClass('zoomOutButton');

			$(slide).imagesLoaded(function() {ACCMOB.product.makeImageDraggable(image, slide);});
		},

		initializeImageGalleryZoom: function() {

			var slides = $("#imageGallery .imageGallerySlide");
			slides.each(function() {
				var slide = $(this);

				$(slide).find(".zoomButton").bind("tap", function() {
					ACCMOB.product.zoomToggle(slide);
				});
			});
		},

		createDialog: function(headerText, data) {
			$.extend(ACCMOB.product.defaultDialogConfig, {headerText: headerText, blankContent: data});
			var dialog = $(document.createElement('div'));
			var param = ACCMOB.product.defaultDialogConfig;

			if ($(window).height() < 250) {
				param.callbackOpen = ACCMOB.common.unlockScreen();
			}

			console.log(param);
			dialog.simpledialog2(param);

			return $.mobile.sdCurrentDialog;
		},

		displayAddToCartOverlay: function(cartResult, statusText, xhr, formElement) {
			$.mobile.hidePageLoadingMsg();

			var currentDialog = $.mobile.sdCurrentDialog;

			if (typeof currentDialog !== "undefined" && currentDialog !== null) {
				currentDialog.close();
			}

			var productCode = $('[name=productCodePost]', formElement).val();
			var quantityField = $('[name=qty]', formElement).val();

			var quantity = 1;
			if (quantityField != undefined) {
				quantity = quantityField;
			}

			ACCMOB.product.trackAddToCart(productCode, quantity, cartResult.cartData);

			ACCMOB.product.refreshMiniCart();
			var addToCartPopup = cartResult.cartPopupHtml;
			var addToCartDialog = ACCMOB.product.createDialog($(addToCartPopup).first('h2').data('dialogheader'), addToCartPopup);
			$.mobile.hidePageLoadingMsg();
		},

		initAddToCart: function() {
			var addToCartForm = $('.add_to_cart_form');
			addToCartForm.ajaxForm({success: ACCMOB.product.displayAddToCartOverlay});
		},

		trackAddToCart: function(productCode, quantity, cartData) {
			if(window.trackAddToCart_jirafe) {
				trackAddToCart_jirafe(cartData);
			}

			if(window.trackAddToCart_google) {
				trackAddToCart_google(productCode, quantity);
			}
		},

		displayRelatedProductOverlay: function(data) {
			$.mobile.hidePageLoadingMsg();
			var relatedProductDialog = ACCMOB.product.createDialog($(data).first('h2').data('dialogheader'), data);
			ACCMOB.product.initAddToCart();
			$.mobile.hidePageLoadingMsg();
		},

		initDisplayRelatedProduct: function() {

			$('.referencedProductImage').live("click", function() {
				$.ajax({
					type:		"GET",
					url:		$(this).data("url"),
					cache:		false,
					success:	ACCMOB.product.displayRelatedProductOverlay
				});
			});
		},

		refreshMiniCart: function() {
			$.get(ACCMOB.common.contextPath + '/cart/miniCart/${totalDisplay}', function(result) {
				$('#minicart_data').html(result)
			});
		},

		bindShowReviews: function() {

			if ($('.review').size() == 1) {
				$(".showReviews").addClass('ui-disabled');
			}

			var multiple = 1;

			$('.showReviews').live("tap", function() {
				var reviews = $('.productReviews li');

				var i;
				for(i = 0; i < reviews.size(); i++) {
					if (i < multiple * 5) {
						$(reviews[i]).show();
					}
					else {
						multiple++;
						break;
					}
				}

				//disables 'show more' button if there are no more review to display.
				if (i == reviews.size() || reviews.size() == 1) {
					$(".showReviews").addClass('ui-disabled');
				}
			});
		},

		scrollToReviewBinder: function(e) {
			e.preventDefault();
			$("#review_detail").trigger('expand');
			var offset = $("#review_detail").offset().top;
			$('html, body').animate({scrollTop: offset}, 500);
		},

		clickToScrollToReviews: function() {
			$("#averageRatingTopOfPage").bind("tap",  function(e) {
				e.preventDefault();
				$("#review_detail").trigger('expand');
				var offset = $("#review_detail").offset().top;
				$('html, body').animate({scrollTop: offset}, 500);
			});

			$("#seeReviewsLink").bind("tap", function(e) {
				e.preventDefault();
				$("#review_detail").trigger('expand');
				var offset = $("#review_detail").offset().top;
				$('html, body').animate({scrollTop: offset}, 500);
			});
		},

		preloadProductImages: function(imageHolder) {
			imageHolder.each(function(i, element) {
				var src = $(element).data("thumbnail");
				var image = $('<img />').attr('src', src);
			});
		}
}

$(document).ready(function() {
	with(ACCMOB.product) {
		initializeGallery($('#imageGallery'));
		initializeGallery($('#referenceGallery'));
		initializeImageGalleryZoom();
		initializeVariantSelectors($(".variantSelector"));
		initDisplayRelatedProduct();
		preloadProductImages($('.hasThumbnail'));
		initAddToCart();
		bindShowReviews();
		clickToScrollToReviews();
	}
});
