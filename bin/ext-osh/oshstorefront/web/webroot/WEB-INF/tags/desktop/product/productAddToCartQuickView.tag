<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ attribute name="styleClass" required="false" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ attribute name="variantProduct" required="true"
	type="de.hybris.platform.commercefacades.product.data.VariantOptionData"%>
<spring:url value="/login/wishlist/summary/createAndUpdateWishList.json"
	var="createAndUpdateWishList" />
<%@ taglib prefix="wishlist" tagdir="/WEB-INF/tags/desktop/wishlist"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	
<c:if test="${not (variantProduct.availabilityInd eq 'STORE' and empty stores)}">
<div class="rightnav1 ${styleClass}1">
	<c:set var="store" value="${stores}" />
	
	<c:choose>
		 <c:when
				test="${(variantProduct.stockLevelStatus.code eq 'inStock' || variantProduct.stockLevelStatus.code eq 'lowStock') && (variantProduct.availabilityInd eq 'ALL' || variantProduct.availabilityInd eq 'WEB')}">
	<div class="quantitybox first">
		<label><spring:message
				code="osh.product.page.product.quantity" /></label> <input type="text"
			id="quantity" name="quantity" maxlength="2" class="number required"
			value="1" onkeypress="return validate(event)" />
	</div>
	</c:when>
	</c:choose>
	<span class="errorMsg"></span>
	<div id="addTocartPanel">
		<c:choose>
			<%-- <c:when
				test="${variantProduct.stockLevel gt 0 && (variantProduct.availabilityInd eq 'ALL' || variantProduct.availabilityInd eq 'WEB') && (sessionScope.orderType eq 'online' or sessionScope.orderType eq '')}">
 --%>		

 	<c:when test="${(variantProduct.stockLevelStatus.code eq 'inStock' || variantProduct.stockLevelStatus.code eq 'lowStock') && (variantProduct.availabilityInd eq 'ALL' || variantProduct.availabilityInd eq 'WEB')}">
 	<div id="addtocartbtn" class="btn_addtocart">
					<a onclick="submitProduct(this,'online')" class="addtocart"
						href="javaScript:void(0);"><spring:message
							code="osh.product.page.product.button.addtocart" /></a>
				</div>
				<p></p>
			</c:when>
			<c:otherwise>
				<%-- <div class="btn_addtocartstoreonly">
					<div id="addtocartbtn" class="btn_addtocart_outofstock">
						<a class="addtocart"><spring:message
								code="osh.product.page.product.button.addtocart" /></a>
					</div>
				</div>
 --%>			</c:otherwise>
		</c:choose>


		<ul class="navlink">
			<c:if
				test="${variantProduct.availabilityInd eq 'ALL' || variantProduct.availabilityInd eq 'WEB'}">
				<c:choose>
					<c:when
						test="${oshVariantProduct.stockLevelStatus.code eq 'inStock'}">
						<li title="<spring:message code="onlinestockAvailable.tooltip"/>"
							class=""><spring:message
								code="osh.product.page.product.instock" /></li>

					</c:when>
					<c:when
						test="${variantProduct.stockLevelStatus.code eq 'lowStock'}">
						<li class="stocklimited"><spring:message
								code="osh.product.page.product.limitedStock" /></li>
					</c:when>
					<c:otherwise>
						<li class="stocknotonline"><spring:message
								code="osh.product.page.product.outofstock" /></li>
					</c:otherwise>
				</c:choose>
			</c:if>
			<br />
			<br />
			<c:if test="${bopisEnabled}">
				<c:if test="${fn:length(stores) == 0}">
					<li class="pickstore_outofstock"><a id="addtocart"
						title="<spring:message code="online.bopis.only"/> "><spring:message
								code="osh.product.page.product.pickupinstore" /></a></li>
				</c:if>

				<c:forEach var="store" items="${stores}" end="0">
					<c:choose>
						<c:when
							test="${(store.value.code eq 'inStock' || store.value.code eq 'lowStock') && (variantProduct.availabilityInd eq 'ALL' || variantProduct.availabilityInd eq 'STORE') && (sessionScope.orderType eq 'bopis' or sessionScope.orderType eq '') }">
							<li class="pickstore"><a
								onclick="submitProduct(this,'bopis')" class="pickUpbtn"
								href="javaScript:void(0);"> <spring:message
										code="osh.product.page.product.pickupinstore" /></a></li>
						</c:when>
						<c:otherwise>
							<li class="pickstore_outofstock"><a class="pickUpbtn"><spring:message
										code="osh.product.page.product.pickupinstore" /></a></li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</c:if>
			<c:if
				test="${variantProduct.availabilityInd eq 'ALL' || variantProduct.availabilityInd eq 'STORE'}">
				<c:forEach var="store" items="${stores}" end="0">
					<c:choose>
						<c:when test="${store.value.code eq 'inStock'}">
							<li title="<spring:message code="stockAvailable.tooltip"/>"
								class="stockonline">${store.key.name}</li>
						</c:when>
						<c:when test="${store.value.code eq 'lowStock'}">
							<li title="<spring:message code="limstockAvailable.tooltip"/>"
								class="stocklimited">${store.key.name}</li>
						</c:when>
						<c:otherwise>
							<li title="<spring:message code="outofstockAvailable.tooltip"/>"
								class="stocknotonline">${store.key.name}</li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:if test="${not empty stores}">
					<div class="btnchangestore">
						<a class="changestorelink" href="${request.contextPath}/store-finder"><spring:message
							code="osh.product.page.product.changestore" /></a>
					</div>
				</c:if>
			</c:if>
		</ul>
	</div>

	<%-- <div class="btnwishlist">
		<div class="headerlist">
			<sec:authorize ifAllGranted="ROLE_ANONYMOUS ">
				<a class="addtowishlist"
					href="${request.contextPath}/login/wishlist"><spring:message
						code="osh.product.page.product.addtowishlist" /> </a>
			</sec:authorize>
			<sec:authorize ifAllGranted="ROLE_CUSTOMERGROUP">
				<a class="addtowishlist" onclick="createWishList(this)" href="#"><spring:message
						code="osh.product.page.product.addtowishlist" /></a>
			</sec:authorize>
		</div>
	</div>
 --%>
</div>
</c:if>
<div id="changestorepopup"
	style="display: none; width: 769px; height: 900px">
	<div id="maincontain" class="popup_changestore">
		<div id="header">
			<h2>
				<b><spring:theme code="text.product.store" /></b>
			</h2>
			<a class="pp_close" href="#"><spring:theme code="product.close" /></a>
			<hr />
		</div>
		<div class="popup_changestorewrap">
			<div class="left_conatian">
				<c:forEach items="${product.images}" var="image" begin="1" end="1">
					<div class="productthumb">
						<a href="#"> <img id="productthumbpopup" src="${image.url}"></a>
					</div>
				</c:forEach>

				<div class="producttitle">
					<span>${product.name}</span>
				</div>
				<div>
					<c:choose>
						<c:when test="${not empty variantProduct.mapPriceData}">
							<p class="product_saleprice">
								*
								<spring:theme code="add.to.cart.price" />
							</p>
						</c:when>
						<c:otherwise>
							<div class="salepricelabel">
								<spring:theme code="osh.productListing.page.sale" />
								${variantProduct.salePriceData.formattedValue}
							</div>
							<div class="regpricelabel">
								<spring:theme code="checkout.reg" />
								<span class="strike">${product.price.formattedValue}</span>
							</div>

						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="right_contain">
				<div class="searchbarwrapper">
					<input class="searchbar" type="text" value="" name="searchbar" />
					<div class="search_btn">
						<input type="button" value="Search" class="button"
							name="search_btn" />
					</div>
					<div class="or_textwrapper">
						-
						<spring:theme code="or.text" />
						-
					</div>
					<div class="use_my_location">
						<a href="${request.contextPath}/store-finder?q=${location}"><spring:theme
								code="text.location" /></a>
					</div>
				</div>
				<div class="right_contain">
					<p>
						<spring:theme code="text.store.distance" />
					</p>
					<p>
						<spring:theme code="text.store.search.suggestions" />
					</p>
					<ul>
						<li><spring:theme code="text.city" /></li>
						<li><spring:theme code="text.city.state" /></li>
						<li><spring:theme code="text.zipcode" /></li>
						<li><spring:theme code="text.longitude" /></li>
					</ul>
					<hr />
				</div>
				<div class="low_middle">
					<div class="clearb"></div>
					<div class="containscroll">


						<c:forEach var="store" items="${stores}">
							<div class="storecontainer">
								<div class="right_lowmiddle_contn">
									<div class="contn hiddenstoreinfo" style="display: none">
										<div class="onehalf">
											<p>
												<span class="head_span"><spring:theme
														code="store.address" /></span><br> <br> <span
													class="head_down_span">${store.key.address.line1}</span><br>
												<span class="head_down_span">${store.key.address.town}</span><br>
												<span class="head_down_span">${store.key.address.postalCode}</span><br>
												<span class="head_down_span">${store.key.address.phone}</span>
											</p>
										</div>
										<div class="onehalf_last">
											<p>
												<span class="head_span"><spring:theme
														code="storeFinder.table.opening" /></span>
												<c:forEach
													items="${store.key.openingHours.weekDayOpeningList}"
													var="weekDay">
													<div>${weekDay.weekDay}&nbsp;
														${weekDay.openingTime.formattedHour} -
														${weekDay.closingTime.formattedHour}</div>
												</c:forEach>
											</p>
										</div>
									</div>
								</div>
								<div class="left_lowmiddle_contn">
									<div class="contain">
										<c:choose>
											<c:when test="${store.value.code eq 'inStock'}">
												<div class="storeavailable">
													<span><b><spring:theme
																code="product.variants.in.stock" /> </b></span>
												</div>
												<p class="storenamenumber">
													<b>${store.key.name}</b>
												</p>
												<p class="storedistance">${store.key.formattedDistance}</p>
												<c:choose>
													<c:when test="${store.key.name eq myStore}">
														<p style="color: green">
															<spring:theme code="text.my.store" />
														</p>
													</c:when>
													<c:otherwise>
														<%-- <p>
															<a
																href="${request.contextPath}/p/${product.code}/myStore/${store.key.name}">Make
																this my store</a>
														</p> --%>
													</c:otherwise>
												</c:choose>

												<p>
													<a href="javascript:void(0);" class="link_viewstoreinfo"
														id="hide"><spring:theme code="show.store.info" /></a>
												</p>
												<p>
													<a href="${store.key.url}"><spring:theme
															code="osh.storeLocator.page.getDirection" /></a>
												</p>
												<%-- <div class="add_cart_btn">
													<input type="button" value="Add to Cart" class="button"
														name="add_cart_btn"
														onclick="submitProductPopup(this,'bopis','${store.key.name}','${myStore}')"
														style="cursor: pointer;" />
												</div> --%>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${store.value.code eq 'lowStock'}">
														<div class="limitedstock">
															<span><b><spring:theme
																		code="osh.product.page.product.limitedStock" /> </b></span>
														</div>
														<p class="storenamenumber">
															<b>${store.key.name}</b>
														</p>
														<p class="storedistance">${store.key.formattedDistance}</p>
														<c:choose>
															<c:when test="${store.key.name eq myStore}">
																<p style="color: green">
																	<spring:theme code="text.my.store" />
																</p>
															</c:when>
															<%-- <c:otherwise>
																<p>
																	<a
																		href="${request.contextPath}/p/${product.code}/myStore/${store.key.name}">Make
																		this my store</a>
																</p>
															</c:otherwise> --%>
														</c:choose>
														<p>
															<a href="javascript:void(0);" class="link_viewstoreinfo"><spring:theme
																	code="show.store.info" /></a>
														</p>
														<p>
															<a href="${store.key.url}"><spring:theme
																	code="osh.storeLocator.page.getDirection" /></a>
														</p>
														<%-- <div class="add_cart_btn">
															<input type="button" value="Add to Cart" class="button"
																name="add_cart_btn"
																onclick="submitProductPopup(this,'bopis','${store.key.name}','${myStore}')"
																style="cursor: pointer;" />
														</div> --%>
													</c:when>
													<c:otherwise>
														<div class="outofstock">
															<span><b><spring:theme
																		code="product.variants.out.of.stock" /> </b></span>
														</div>
														<p class="storenamenumber">
															<b>${store.key.name}</b>
														</p>
														<p class="storedistance">${store.key.formattedDistance}</p>
														<c:choose>
															<c:when test="${store.key.name eq myStore}">
																<p style="color: green">
																	<spring:theme code="text.my.store" />
																</p>
															</c:when>
															<c:otherwise>
																<p>
																	<a
																		href="${request.contextPath}/p/${product.code}/myStore/${store.key.name}"><spring:theme
																			code="make.this.my.store" /></a>
																</p>
															</c:otherwise>
														</c:choose>
														<p>
															<a href="javascript:void(0);" class="link_viewstoreinfo"><spring:theme
																	code="show.store.info" /></a>
														</p>
														<p>
															<a href="${store.key.url}"><spring:theme
																	code="osh.storeLocator.page.getDirection" /></a>
														</p>
														<%-- <div class="add_cart_btn">
															<input type="button" value="Add to Cart" class="button"
																onclick="submitProductPopup(this,'bopis','${store.key.name}','${myStore}')"
																name="add_cart_btn" style="cursor: pointer;" />
														</div> --%>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
									</div>
								</div>
								<hr />
							</div>
						</c:forEach>

					</div>
				</div>
			</div>
		</div>

	</div>

</div>
<div style="display: none">
	<div id="popup_createWishList">
		<wishlist:createUpdateWishlist />
		<wishlist:createUpdateMultipleWishlist />
	</div>
</div>

<script type="text/javascript">


	var thankyouMsg = '<spring:message code="registration.confirmation.message.title" />';
	
	function changeStore(pcode,storeName){

		var allowSubmit = true;
		var productCode=pcode
		var storeName=storeName
		
			$.ajax(
				{				
					url :	"${request.contextPath}/p/"+productCode+"/myStore/"+storeName,
					type : 'GET',
					dataType : 'json',
					data : {storeName :storeName ,productCode : productCode},
					success : function(data){
						if(!json.error) location.reload(true);
						if(data){
					/* to refresh mini cart */
						
						}
					}
				});
		
	}
</script>


<script type="text/javascript">

	var thankyouMsg = '<spring:message code="registration.confirmation.message.title" />';
	
	function submitProduct(obj,type){
		var allowSubmit = true;
		var quantity = $("#quantity").val(); 
		var productCodePost='${variantProduct.code}'
		var orderType=type
		var availblity='${product.availabilityInd}'
			$.ajax(
				{	
					async:true,
					url :	"${request.contextPath}/cart/add",
					type : 'POST',
					dataType : 'json',
					data : {qty : quantity,productCodePost : productCodePost,orderType : orderType},
					success : function(data){
						if(data){
							var errormsg=data['errorMsg'];
							 if(errormsg!=null)
							 {
							  	$('.errorMsg').html(errormsg);
							 } 
							 else{
					/* to refresh mini cart */
						refreshMiniCart();
						$('#headerCart').trigger('mouseenter');
						$('#headerCart').trigger('mouseleave'); 
							if(data.orderType==='online'){
								var pickStoreButton = $(".pickstore");
								pickStoreButton.removeClass("pickstore");
								pickStoreButton.removeClass("btnchangestore");
								pickStoreButton.addClass("pickstore_outofstock");
								pickStoreButton.addClass("pickUpbtn");
								$(".pickUpbtn").removeAttr("onClick");
								}
								
								else if(data.orderType==='bopis'){
										var onlineButton = $(".btn_addtocart");
										onlineButton.removeClass("btn_addtocart");
										onlineButton.addClass("btn_addtocart_outofstock");
										onlineButton.addClass("addtocart");
										$(".addtocart").removeAttr("onClick");
								}
							 }
						}
						
						///$.prettyPhoto.close();
					}
				});

	}

	function submitProductPopup(obj,type,store,mystore){
		var allowSubmit = true;
		var quantity = $("#quantity").val();	
		var productCodePost='${variantProduct.code}'
		var orderType=type
		var myStore=mystore
		var storeSelect=store
		var availblity='${product.availabilityInd}'
			if(myStore==storeSelect)
			{
			allowSubmit=true;
			}
		else
			{
			var r=confirm("Only one store may be associated with your order for items noted as Pickup In Store. Changing your store location will automatically check inventory against the new store location for all items currently in cart. Do you want to continue");
			if (r==true)
			  {
				allowSubmit=true;
			  }
			else
			  {
				allowSubmit=false;
			  }
			}
		if (allowSubmit && ((availblity == 'W')||(availblity == 'A'))) {
			$.ajax(
				{				
					url :	"${request.contextPath}/cart/add",
					type : 'POST',
					dataType : 'json',
					data : {qty : quantity,productCodePost : productCodePost,orderType : orderType,storeName:storeSelect},
					success : function(data){
						if(data){
					/* to refresh mini cart */
						refreshMiniCart();
						}
						$.prettyPhoto.close();
					}
				});
		}
	}
	function createWishList(obj) {
		var productCodePost='${variantProduct.code}'
		 var options = {
		  url: '${request.contextPath}/addWishList/createAndUpdateWishList.json',
		  data: {productCode : productCodePost},
		  target: '#popup_createWishList',
		  type: 'GET',
		  success: function(data) {
		   
		   $.colorbox({inline:true, href:"#popup_createWishList", width:"450px", height: "180px",overlayClose: false});
		  },
		  error: function() {
		   alert("Failed to get create Wish List form. Error details");
		  }
		 };

		 $(this).ajaxSubmit(options);

		 return false;
		};
		/* 

		$(document).ready(function () {
			function createupdatewishlist(obj){
				var options = {
					type: 'POST',
					beforeSubmit: function() {
						$('#addtowishlistform').block({ message: "<img src='${commonResourcePath}/images/spinner.gif' />" });
									},
					success: function(data) {
						if(data)
							{
							alert('success alla');
							 $('#popup_createWishList').html("Item is successfully added to you Wishlist");
							}
					},
					
				};

				$(this).ajaxForm(options);

			}
			
		});
	
 */		
</script>

<script type="text/javascript">
//Function to allow only numbers to textbox
function validate(key)
{
//getting key code of pressed key
var keycode = (key.which) ? key.which : key.keyCode;
var phn = document.getElementById('quantity');
//comparing pressed keycodes
if (!(keycode==8 || keycode==46)&&(keycode < 48 || keycode > 57))
{
return false;
}
else
{
return true;
}
}

</script>


