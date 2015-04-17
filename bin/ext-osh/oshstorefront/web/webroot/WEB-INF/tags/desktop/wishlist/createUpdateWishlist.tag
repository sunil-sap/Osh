<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:url value="/addWishList/createAndUpdateWishlistname.json" var="action" />


<form method="post" id="addtowishlistform" action="${action}" class="" >
<div class="wishlist_popup">
<div class="title_holder" style="margin-left: 10px; width: 419px;">
<h2><b><spring:theme code="text.wishlist.create"/></b></h2></div>
<div class="middle">
<br clear="all">

<div class="popup_contain_inner"><div class="upper">
<div class="inputrow"><label class="inputlabel"><spring:theme code="text.wishlist.name"/></label>

   <input class="create_update_address_id" id="name" path="email"  tabindex="1"/>
</div>
<br clear="all">
<div class="btn_wish">
<div class="btnwish_left">
<!-- <input type="submit" value="Create" class="right" id="submit"> -->
<a class="btn-pill-secondary"  onclick="submitWishListName(this)" id="emailsignup" href="#"><spring:theme code="text.wishlist.create"/></a>                            
</div>
<div class="btnwish_right">
<a value="Cancel" onclick="closePopup(this)" class="right" id="submit"><spring:theme code="text.wishlist.cancel"/></a></div></div>
 
</div>

</div></div>
</div>
 </form>
 
<script type="text/javascript">
	
	
	function submitWishListName(obj){
		var name=$("#name").val();
		var productCode='${productCode}'
		$.ajax(
				{
					url :	"${request.contextPath}/addWishList/createAndUpdateWishlistname.json",
					type : 'POST',
					dataType : 'json',
					data : {name : name , productCode:productCode},
					success : function(data){
						if(data == 'Available')
							{
							$('.title_holder').html("<h2 class='prod_wishlist'>Product is already present in this wish list.</h2>");	
						/* 	setTimeout($.colorbox.close, 3000) */
							}
						if(data == 'Created')
						{
						$('.wishlist_popup').html("<H2>" +"<p>Product has been successfully</p> added to your wish list."+ "</H2>");
						setTimeout($.colorbox.close, 3000)
						}		
					}
				});
	}
	
	function closePopup(obj){
		$.colorbox.close();
	}

</script>