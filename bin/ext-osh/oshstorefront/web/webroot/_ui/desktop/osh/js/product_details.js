// JavaScript Document
$.validator.setDefaults({
	submitHandler: function() { 
	$('.formtext').html('Thanks, You have sucessfully Signup');
	}
});



function stockOutofStock(){
	$('li.stock_status').removeClass('stockonline');
	$('li.stock_status').addClass('stocknotonline');
	$('li.stock_status').text('');
	$('li.stock_status').text('Out of stock');
}

function stockLimited(){
	$('li.stock_status').removeClass('stockonline');
	$('li.stock_status').removeClass('stocknotonline');
	$('li.stock_status').addClass('stockonline');
	$('li.stock_status').text('');
	$('li.stock_status').text('Limited Stock');
}

function stockAvailable(){
	$('li.stock_status').removeClass('stocknotonline');
	$('li.stock_status').addClass('stockonline');
	$('li.stock_status').text('');
	$('li.stock_status').text('In-Stock');
}


$(document).ready(function(){
	$('#emailthispage').validate();
	$("#addtocart").validate();
	$('body').addClass('page_productdetail');
	$('.link_sizechart').click(function(){
		$('#sizechartpopup').dialog({modal: true,title:'Size Chart', width:400, height:600});
	});
	
	$('.link_thispage').click(function(){
		$('#emailthis :text, #emailthis textarea').val('');
		$('#emailthis').dialog({modal: true});
		
	});
	$('.btn_cancel').click(function() { $("#emailthis").dialog("close"); });
	$('.btn_addtocart a').click(function() { 
		$("form#addtocart").submit(); 
	});
	
	$('#mycarousel').jcarousel({scroll:1});
	$('#mycarousel li img').click(function(){
		
		var selectedImage = $(this).attr('rel');
		var selectedMainImage = $(this).attr('mainImage');
		//alert(selectedImage);
		
		$('.link_enlarger').attr('href',selectedImage);
		$(".product_detail_view a").attr('href',selectedImage);
		
/*		var img = $("<img />").attr('src', selectedMainImage).load(function() {
    	    if (!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0) {
            //alert('broken image!');
    	    	$(".product_detail_view a").empty();
				$(".product_detail_view a").append(img);
        		} else {
		            $(".product_detail_view a").empty();
					$(".product_detail_view a").append(img);
        		}
		  });*/
		
	    var img = $("<img />").attr('src', selectedMainImage);
		
		$(".product_detail_view a").empty();
		$(".product_detail_view a").append(img);
		
		});
		
	
		
		$("a[rel^='prettyPhoto']").prettyPhoto({social_tools:' '});

		$("a.changestorelink").prettyPhoto({social_tools:' ',default_width: 850});
		
		stockAvailable();
		$( "#contenttab a" ).tabs();
		$('.moreinfo').click(function(){
			   $( "#contenttab a" ).tabs("select", "#contenttab a:first" );
			  });
		
		
		$( "#tabs" ).tabs();
		$('a.link_viewstoreinfo').live('click', function(){
			$(this).closest('.storecontainer').find('.hiddenstoreinfo').toggle();
		});

});