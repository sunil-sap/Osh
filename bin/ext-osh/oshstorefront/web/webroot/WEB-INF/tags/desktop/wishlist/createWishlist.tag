<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<spring:url value="/login/wishlist/changeWishName.json"
	var="changeWishName"/>
 <script type="text/javascript">
 $(document).ready(function() {
	 $("#submit").click(function() {
		 var name = $("#createwishlistbox").val();
		
		 if(name=="")
			 {
			 $("#name_error").show();
			 return false;
			 }
	 })
		});
	
	

</script>	

	
<div class="popup_contain">
<div class="title_holder ">
<h2 class="wishlist_heading"><b><spring:theme code="text.wishlist.create"/></b></h2></div>
<div class="middle">
<br clear="all">

<div class="popup_contain_inner"><div class="upper">
<div class="inputrow"><label class="inputlabel"><spring:theme code="text.wishlist.new.name"/></label>
<form:form method="post" commandName="createWishListForm" action="" class="renameWishlistform" id="renameWishlistform">
			<form:input path="wishListName" maxlength="20" class="create_update_address_id" id ="createwishlistbox"/>
			<input type="submit" value="Create" class="left wishlist_button" id="submit" >
			<p id = "name_error" style="display: none;"><spring:theme code="text.wishlist.name.error"/> </p>
	</form:form>
	
</div>
<br clear="all">
<div class="btn_wish">
<div class="btnwish_left">
</div>
</div>
</div>
</div></div>
</div>

	