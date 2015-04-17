<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div class="wishlist_popup">
  <div class="title_holder" style="margin-left: 10px; width: 419px;">
    <h2 class="wishlist_title"><b><spring:theme code="text.wishlist.select"/></b></h2>
  </div>
  <div class="middle">
    <div class="popup_contain_inner">
      <label class="selectlabel"><spring:theme code="text.wishlist.add"/></label>
      <div class="select">
       <form method="post"action="${request.contextPath}/login/wishlist/changeDefaultWishList" id="changeList" >
		<select id="currentWishList" class="wishlist_Names" path="wishlistname" skipBlank="false">
		<%-- <form:options items="${listOfWishList}" itemLabel="${'name'}" /> --%>
		<c:forEach items="${listOfWishList}" var="eachEntry">
       <option id="currentWishListName" value="${eachEntry.name}">${eachEntry.name}</option>       
        </c:forEach>
		</select>
		</form>
		<div class="btnwish_left wishlist_button">
<!-- <input type="submit" value="Create" class="right" id="submit"> -->
<a class="btn-pill-secondary"  onclick="submitWishListName(this)" id="submitName" href="#"><spring:theme code="text.wishlist.ok"/></a>                            
</div>
<div class="btnwish_right wishlist_button">
<a class="right " href="#" onclick="closePopup(this)" id="submit"><spring:theme code="text.wishlist.cancel"/></a></div>
	
      </div>
    </div>
  </div>
</div>


<script type="text/javascript">
	
	
	function submitWishListName(obj){
		var name=$("#currentWishList").val();
		
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
						$('.title_holder').html("<h2 class='prod_wishlist'>Your Wish List already contains this item</h2>");	
						/* setTimeout($.colorbox.close, 3000) */
						}
					if(data == 'Created')
					{
					$('.wishlist_popup').html("<H2>" +"<p>This item has been successfully</p> added to your Wish List"+ "</H2>");	
					setTimeout($.colorbox.close, 3000)
					}		
				}
				});
	}
	function closePopup(obj){
		$.colorbox.close();
	}

</script>