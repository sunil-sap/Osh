<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="storeSearchPageData" required="false"
	type="de.hybris.platform.acceleratorservices.storefinder.data.StoreFinderSearchPageData"%>
<%@ attribute name="locationQuery" required="false"
	type="java.lang.String"%>
<%@ attribute name="showMoreUrl" required="false"
	type="java.lang.String"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="store" tagdir="/WEB-INF/tags/desktop/store"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:url value="/store-finder/myStore" var="storeLocatorUrl"/>

<script type="text/javascript">
/*<![CDATA[*/
function getImageUrl(img, loopIndex) {

	var imageSrc = 'http://maps.google.com/mapfiles/marker' + String.fromCharCode(loopIndex + 65) + '.png';
	if(img.src != imageSrc) { // don't get stuck in an endless loop
		img.src = imageSrc;
	}
}

$(document).ready(function() {
	response.setIntHeader("Refresh", 5);
});


/*]]>*/
</script>

 <div class="sl_contentwrapper">
 <div class="gmap_mainwrapper">
<div class="gmap_leftcolume">
	<c:if
		test="${storeSearchPageData ne null and !empty storeSearchPageData.results}">
		<table id="store_locator" cellpadding="0" cellspacing="0" border="0"
			style="width: 100%;">
			<tbody>
				<tr>
					<%-- <th id="header1" class="maker_kmi"><spring:theme
							code="storeFinder.table.store" /></th> --%>

					<th id="header3"><spring:theme
							code="storeFinder.table.address" /></th>
					<th id="header4" class="store_hours"><spring:theme
							code="storeFinder.table.opening" /></th>
				</tr>
				<tr>
					<td colspan="3">
						<div class="gmapresult_wrapper">
							<table width="100%" cellspacing="0" cellpadding="0" border="0">
								<tbody>
									<c:forEach items="${storeSearchPageData.results}" var="pos"
										varStatus="loopStatus">
										<c:url value="${pos.url}" var="posUrl" />
										<tr>
											<td class="maker_kmi" headers="header1">
												<div class="lsicon">
													<a href="${posUrl}" class="left"> <img
														src="${commonResourcePath}/images/lightbox-blank.gif"
														onload="getImageUrl(this, ${loopStatus.index})" /></a>
												</div>
												<div class="ls_distancekmi">${pos.formattedDistance}</div>

											</td>

											<td headers="header3">
												<div class="ls_store_name">
													<ycommerce:testId code="storeFinder_result_link">
														<div class="ls_store_name_new">${pos.name}</div>
														
													</ycommerce:testId>
												</div> 
												<ycommerce:testId code="storeFinder_result_address_label">
													<c:if test="${not empty pos.address}">
														<div class="ls_store_number">${pos.address.line2}</div>
														<div class="ls_store_street">${pos.address.line1}</div>
														<div class="ls_city_state">${pos.address.town},${pos.state}</div>
														<div class="ls_zip">${pos.address.postalCode}</div>
														<div class="ls_tel_number">${pos.address.country.name}</div>
														<div class="ls_tel_number">${pos.address.phone}</div>
													</c:if>
												</ycommerce:testId> <ycommerce:testId code="storeFinder_result_link">
													<div class="ls_getdirection">
														<a href="${posUrl}"><spring:theme code="osh.storeLocator.page.getDirection"/></a>
													</div>
												</ycommerce:testId>



												<div class="ls_make_mystore">
												<c:choose>
												<c:when test="${pos.name ne storeName}">
                                                <%--   <a href="#" onclick="submitStoreName('${pos.name}','${storeName}')" class="storeLocator"><spring:theme code="make.this.my.store"/></a>--%>											
 												   	<a href="#" onclick="submitStoreName('${pos.name}')" class="storeLocator"><spring:theme code="make.this.my.store"/></a>		
 												</c:when>
												<c:otherwise >
												     <span style="color: green"><spring:theme code="text.my.store" /></span>
												</c:otherwise>
												</c:choose>
												</div>
											</td>
											<td headers="header4"><store:openingSchedule
													openingSchedule="${pos.openingHours}" /></td>
										</tr>

									</c:forEach>
								</tbody>
							</table>
						</div>
					</td>
				</tr>

			</tbody>
		</table>
	</c:if>
</div>

 <div class="gmap_right_detailcolume">
<store:storesMap storeSearchPageData="${storeSearchPageData}" />  </div>
</div>
</div>

<script>

function submitStoreName(storeName){
	$.ajax({
		url:'${storeLocatorUrl}',
		dataType : 'json',
		type : 'GET',
		data:{storeName:storeName},
		success : function(data){
			window.location.reload();
		}
	}); 
};
/* function submitStoreName(storeSelect,myStore){
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

	 if (allowSubmit) { 	

	$.ajax({
		url:'${storeLocatorUrl}',
		dataType : 'json',
		type : 'GET',
		data:{storeName:storeSelect},
		success : function(data){
			window.location.reload();
		}
	}); 
	 }
}; */
</script>