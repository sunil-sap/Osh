<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="actionNameKey" required="true"
	type="java.lang.String"%>
<%@ attribute name="action" required="true" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="formUtil" tagdir="/WEB-INF/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="wishListform" tagdir="/WEB-INF/tags/desktop/wishlist"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<spring:url value="/login/wishlist/getCreateWishListForm.json"
	var="createNewWishListForm" />
<spring:url value="/login/wishlist/getRenameWishListForm.json"
	var="renameWishListForm" />
<script type="text/javascript">
	function sebdWishList() {

		var options = {
			url : '${sendwWishListForm}',
			data : {
				createUpdateStatus : ''
			},
			target : '#popup_sendWishList',
			type : 'GET',
			success : function(data) {
				//alert(data);
				//bindCreateUpdatePaymentDetailsForm();

				// Show the payment method popup

				$.colorbox({
					inline : true,
					href : "#popup_sendWishList",
					width : 300,
					height : 220,
					overlayClose : false
					
				});
				//$.colorbox({inline:true, href:"#popup_checkout_delivery_modes",width:false, height: false, overlayClose: false});
			},
			error : function(xht, textStatus, ex) {
				alert("Failed to get send Wish List form. Error details ["
						+ xht + ", " + textStatus + ", " + ex + "]");
			}
		};

		$(this).ajaxSubmit(options);

		return false;
	};

</script>

<div class="mainmiddleContent">
	<div class="leftmaincontent">

		<div class="leftmaincontent">
			<nav:accountNav />
		</div>
	</div>
	<div class="middlemaincontent">
		<div class="middlewrapper">
			<div class="middle_upper" style="margin-top: 0px; padding-top: 5px;">
				<h2>
					<b>${currentWishListName}</b>
				</h2>
				<br clear="all">
				<div style="${fn:length(listOfWishList) > 0 ? "display:block;" : "display:none;"}">
					<form:form method="post" commandName="currentWishListForm"
						action="${request.contextPath}/login/wishlist/changeDefaultWishList"
						id="changeList">
						<form:select idKey="currentWishList" id="currentWishList"
							path="wishlist2Model.name" maxlength="20" mandatory="false" skipBlank="false"
							skipBlankMessageKey="Selsect Wish List">
							<c:forEach items="${listOfWishList}" var="eachEntry">
								<option value="${eachEntry.name}"
									${eachEntry.name == currentWishListName ? 'selected="selected"' : ''}>${eachEntry.name}</option>
							</c:forEach>
						</form:select>
					</form:form>
				</div>
				<br clear="all">
				<div class="sel_list">

					<span class="createWishList"><a onclick="createWishList()">
							<spring:theme code="wishList.createNewList" />
					</a> 
					<c:if test="${not empty listOfWishList}">
					<span class="lin">|</span> <a onclick="changeWishListName()"><spring:theme
								code="wishList.changeListName" /></a><span class="lin">|</span> <a
						href="${request.contextPath}/login/wishlist/removeWishList">
						
						<spring:theme
								code="wishList.deleteList" /> </a></c:if></span>
				</div>

				<div class="wishList_item_container_holder">
					<div class="title_holder">
						<div class="title">
							<div class="title-top">
								<span></span>
							</div>
						</div>
						<h2><spring:theme code="text.shopping.list"/></h2>
						<c:choose>
							<c:when test="${empty currentWishList }">
								<div class="uppre_wish">
									<span><spring:theme code="wishList.emptyListMessage"></spring:theme></span>
								</div>
							</c:when>
							<c:otherwise>
								<c:forEach var="product" items="${currentWishList}">
									<div class="low_wish">
										<div class="product">
											<div class="thumbwrap">
												<c:forEach items="${product.images}" var="image" begin="2"
													end="2">
													<a href="${request.contextPath}/p/${product.code}"> <img id="wishList_productthumbpopup"
														src="${image.url}"></a>
												</c:forEach>
											</div>
											<div class="productname">${product.name}</div>
											<div class="skunumber">
												<span><a href="${request.contextPath}/p/${product.code}">${product.code}</a></span>
											</div>
											<div class="size">
												<c:choose>
													<c:when test="${not empty product.mapPriceData}">
															<p>* <spring:theme code="add.to.cart.price"/></p>
													</c:when>
													
													<c:otherwise>
													<c:choose>
														<c:when test="${not empty product.salePriceData}">
															<p class="product_saleprice_wishlist"><spring:theme code="osh.productListing.page.sale"/>&nbsp<span><format:fromPrice priceData="${product.salePriceData}" /></span></p>
														</c:when>
														<c:otherwise>
															<p class="product_saleprice_wishlist"><spring:theme code="basket.page.price"/>&nbsp<span><format:fromPrice priceData="${product.regPriceData}" /></span></p>
														</c:otherwise>
													</c:choose>
													<c:if test="${not empty product.salePriceData }">
														<p class="product_reg_wishlist"><spring:theme code="checkout.reg"/> <span><format:fromPrice priceData="${product.regPriceData}" /></span></p>
													</c:if>
												</c:otherwise>	
												</c:choose>
											</div>
										</div>
										<div class="last_wish">
										<div class="remove_wish">
											<a href="${request.contextPath}/login/wishlist/removeProduct?productCode=${product.code}">
												<span class="remove_wishlist"><spring:theme code="basket.page.remove"/></span>
											</a>
										</div>
									</div>
									</div>
								</c:forEach>

							</c:otherwise>
						</c:choose>

					</div>
				</div>
				<%-- </c:otherwise>
				</c:choose> --%>

				<div style="display: none">
					<div id="popup_createWishList">
						<wishListform:createWishlist />
					</div>
				</div>

				<div style="display: none">
					<div id="popup_renameWishList">
						<wishListform:changeWishlistNameForm />
					</div>
				</div>


			</div>
		</div>
	</div>
</div>