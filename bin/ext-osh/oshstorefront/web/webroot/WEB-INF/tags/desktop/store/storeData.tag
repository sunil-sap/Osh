<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
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
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>

<div class="clearb"></div>
<div class="containscroll">
	<c:forEach var="store" items="${stores}">
		<div class="storecontainer">
			<div class="right_lowmiddle_contn">
				<div class="contn hiddenstoreinfo" style="display: none">
					<div class="onehalf">
						<p>
							<span class="head_span"><spring:theme code="store.address" /></span><br>
							<br><span class="head_down_span">${store.key.address.line2}</span><br>
							<span class="head_down_span">${store.key.address.line1}</span><br>
							<span class="head_down_span">${store.key.address.town},${store.key.state}</span><br>
							<span class="head_down_span">${store.key.address.postalCode}</span><br>
							<span class="head_down_span">${store.key.address.country.name}</span><br>
							<span class="head_down_span">${store.key.address.phone}</span>
						</p>
					</div>
					<div class="onehalf_last">
						<p>
							<span class="head_span"><spring:theme
									code="storeFinder.table.opening" /></span>
							<c:forEach items="${store.key.openingHours.weekDayOpeningList}"
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
								<c:when test="${store.key.name eq storeName}">
									<p style="color: green">
										<spring:theme code="text.my.store" />
									</p>
								</c:when>
								<c:otherwise>
									<p>
										<a
											href="${request.contextPath}/p/${productCode}/myStore/${store.key.name}">Make
											this my store</a>
									</p>
								</c:otherwise>
							</c:choose>

							<p>
								<a href="javascript:void(0);" class="link_viewstoreinfo"
									id="hide"><spring:theme code="show.store.info" /></a>
							</p>
							<p>
								<a href="${request.contextPath}${store.key.url}"><spring:theme
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
								<c:when test="${store.value.code eq 'inStock'}">
									<div class="limitedstock">
										<span><b><spring:theme
													code="osh.product.page.product.limitedStock" /></b></span>
									</div>
									<p class="storenamenumber">
										<b>${store.key.name}</b>
									</p>
									<p class="storedistance">${store.key.formattedDistance}</p>
									<c:choose>
										<c:when test="${store.key.name eq storeName}">
											<p style="color: green">
												<spring:theme code="text.my.store" />
											</p>
										</c:when>
										<c:otherwise>
											<p>
												<a
													href="${request.contextPath}/p/${productCode}/myStore/${store.key.name}">Make
													this my store</a>
											</p>
										</c:otherwise>
									</c:choose>
									<p>
										<a href="javascript:void(0);" class="link_viewstoreinfo"><spring:theme
												code="show.store.info" /></a>
									</p>
									<p>
										<a href="${request.contextPath}${store.key.url}"><spring:theme
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
										<c:when test="${store.key.name eq storeName}">
											<p style="color: green">
												<spring:theme code="text.my.store" />
											</p>
										</c:when>
										<c:otherwise>
											<p>
												<a
													href="${request.contextPath}/p/${productCode}/myStore/${store.key.name}"><spring:theme
														code="make.this.my.store" /></a>
											</p>
										</c:otherwise>
									</c:choose>
									<p>
										<a href="javascript:void(0);" class="link_viewstoreinfo"><spring:theme
												code="show.store.info" /></a>
									</p>
									<p>
										<a href="${request.contextPath}${store.key.url}"><spring:theme
												code="osh.storeLocator.page.getDirection" /></a>
									</p>
									<%-- <div class="add_cart_btn_outofstock ">
								<a class="add_cart_btn_outofstock_link" style="color: #ffffff"><spring:theme code="text.add.to.cart"/></a>											
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