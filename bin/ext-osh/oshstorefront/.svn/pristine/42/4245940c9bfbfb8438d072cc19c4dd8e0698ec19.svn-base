// JavaScript Document

var defaultFieldValue = function(fieldName){
		var input = $('input[name="'+fieldName+'"]'),
					defaulttext = input.attr('data-default');
			
				input.val(input.attr('data-default'));
			
				input.on('focus', function(){
						if(defaulttext == input.val()) input.val('');
				}).on('keydown', function(){        
					if(defaulttext == input.val()) input.val('');
				}).on('blur', function(){
					if(input.val() == '') input.val(defaulttext);
				});
	}
function showQView(url){
	$('#quickview').empty();
	$('#quickview').load(url);
	$('#quickview').dialog({ 
		modal: true,
		width: 820,
		height:200,
		resizable: false,
		position: "center"
	});
}

$(document).ready(function(){
	$('#headerNav ul li:first').addClass('first');
	
	$(document).bind('cbox_open', function(){ $('div#colorbox button#cboxClose').hide(); });
	$(document).bind('cbox_complete', function(){ $('div#colorbox button#cboxClose').show(); });
	
	$("a.ajax").colorbox({resizable: false,speed:0,scrolling:true,transition:"elastic",speed:400});
	$(".product_quickview a.ajax").colorbox({width:"800px", height:"450px",resizable: false,speed:0});
	//alert('aftr colorbox');
	defaultFieldValue('keyword');
	defaultFieldValue('emailsignup');
	var imageWidth = 0;
	var imageHeight = 0;
	$('.pix_diapo img').each(function(){
		var img = new Image();
			img.onload = function() {
			  if(this.width>imageWidth){
			  	imageWidth = this.width;
			  }
			  if(this.height>imageHeight){
			  	imageHeight = this.height;
			  }
			  $('.pix_diapo').width(imageWidth+'px').height(imageHeight+'px');
			
			}
			img.src = $(this).attr('src');
	});
	
	/* Product Listing Page */
	$('.productList .pclearb').remove();
	$(".productList .product-item:eq(3)").after('<div class="clearboth pclearb"></div>');
	$(".productList .product-item:eq(7)").after('<div class="clearboth pclearb"></div>');
	$(".productList .product-item:eq(11)").after('<div class="clearboth pclearb"></div>');
	$(".productList .product-item:eq(15)").after('<div class="clearboth pclearb"></div>');
	
	
	//$('.pix_diapo').diapo();
	//$( "#contenttab" ).tabs();
	
	$('#contenttab a').click(function(){
		var currentTab = $(this).attr('id'); 
		$('#contenttab li').removeClass('ui-tabs-active');
		$(this).parent().addClass('ui-tabs-active');
		$('#contenttab .ui-tabs-panel').hide();
		$('#contenttab div'+currentTab).show();
		
	});
	$('#contenttab .ui-tabs-panel').hide();
	$('#contenttab a:first').trigger('click');
	
	/* Cart Page JS */
	$('.storeorshippingwrapper input').change(function(){
		if($(this).is(':checked')){
			$(this).closest('.storeorshippingwrapper').find('.shippingoption').removeClass('selectedshipping');
			$(this).closest('.shippingoption').addClass('selectedshipping');
		}
	});
	

	$('.link_remove a').click(function(){
		$(this).closest('tr').css('background','#ffc').fadeOut('slow').addClass('removedit');
		setTimeout(function() { $('.removedit').remove(); }, 800);
		
		var productRow = $('.cart_tablewrapper tr').not('.cart_tablewrapper tr.removedit').length; 
		//alert('productRow ' + productRow);
		if(productRow == '1'){
			
			$('.cart_tablewrapper tbody').html('<tr><td class="no_item" colspan="5" style="text-align: center; font-weight: bold; padding: 25px 0px; border: 1px solid rgb(221, 221, 221);">No Item in Cart</td></tr>a');
			
		}
		
	});
});

