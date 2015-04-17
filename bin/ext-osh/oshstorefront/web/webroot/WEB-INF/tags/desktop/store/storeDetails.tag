<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="store" required="true"
	type="de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="store" tagdir="/WEB-INF/tags/desktop/store"%>


<div class="gmap_leftcolume">
	<div class="marbottom overflow">
		<div class="btn2 floatl">
			<input type="button" class="btn_back" value="Back" onclick="history.back()">
		</div>
	</div>
	<table id="store_details" cellspacing="0" cellpadding="0" border="0">
		<tbody>
			<tr>
				<%-- <th id="header1" class="maker_kmi"><spring:theme
						code="storeFinder.table.store" /></th> --%>

				<th id="header3"><spring:theme code="storeFinder.table.address" /></th>
				<th id="header4" class="store_hours"><spring:theme
						code="storeFinder.table.opening" /></th>
			</tr>
			<tr>
				<td colspan="3">
					<div class="gmapdirection_wrapper">
						<table width="100%" cellspacing="0" cellpadding="0" border="0">
							<tbody>
								<tr class="sl_list1">
									<td>
										<div class="ls_store_name">${store.name}</div> <c:if
											test="${not empty store.address.line1 || not empty store.address.line2 ||
						  not empty store.address.town || not empty store.address.country.name ||
						  not empty store.address.postalCode}">
											<div class="ls_store_number">${store.address.line2}</div>
											<div class="ls_store_add">${store.address.line1}</div>
											<div class="ls_city_state">${store.address.town},${store.state}</div>
											<div class="ls_zip">${store.address.postalCode}</div>
											<div class="ls_tel_number">${store.address.country.name}</div>

										</c:if> <c:if test="${not empty store.address.phone}">
				${store.address.phone}
			</c:if> <c:if test="${not empty store.address.email}">
											<a href="mailto:${store.address.email}">${store.address.email}</a>
										</c:if>
									</td>
									<td class="store_hours">
										<div class="ls_workinghours">
											<c:if test="${not empty store.openingHours}">

												<store:openingSchedule
													openingSchedule="${store.openingHours}" />

											</c:if>
											<c:if
												test="${not empty store.openingHours.specialDayOpeningList}">
												<store:openingSpecialDays
													openingSchedule="${store.openingHours}" />
											</c:if>
										</div>
									</td>
									<c:if test="${not empty store.features}">

										<td class="strong"><spring:theme
												code="storeDetails.table.features" /></td>
										<td>
											<ul>
												<c:forEach items="${store.features}" var="feature">
													<li>${feature.value}</li>
												</c:forEach>
											</ul>
										</td>
									</c:if>
								</tr>
							</tbody>
						</table>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
	<h2 class="gma_sideheading">
		<spring:theme code="osh.storeLocator.page.getDirectionMessage" />
	</h2>
	<div class="dir_search">
		<div class="icon_searchadd1"></div>
		<div class="floatl">
			<input type="text" id="source"/> 
		</div>
	</div>
	<div class="dir_search">
		<div class="icon_searchadd2"></div>
		<input type="text" id="dest" value="${store.address.line2} &nbsp ${store.address.line1} &nbsp ${store.address.town} &nbsp ${store.state}"/>
	</div>
	<div class="btn_direction">
		<button class="storeLocator_Search_button" onclick="getDirection();">
			<spring:theme code="osh.storeLocator.page.getDirection" />
		</button>
	</div>
	<store:getDirections />

</div>


