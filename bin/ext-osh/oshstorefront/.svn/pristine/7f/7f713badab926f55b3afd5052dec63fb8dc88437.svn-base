<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>

<template:page pageTitle="${pageTitle}">
	<div class="middleContent">
		<div class="innermiddleContent">
			<div class="acc_breadcrumb">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			</div>
			<div class="acc_banner">
				<cms:slot var="feature" contentSlot="${slots.GlobalSection}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>
			
			<div class="acc_middlewrap">
				<div class="leftmaincontent">
					<nav:accountNav selected="orders" />
				</div>
				<div class="acc_middlemaincontent">
					
					<div class="span-20 last multicheckout">
						<div class="item_container_holder_order_history">
							<div class="title_holder">
								<div class="title">
									<div class="title-top">
										<span></span>
									</div>
								</div>
								<h2>
									<spring:theme code="text.account.orderHistory"
										text="Order History" />
								</h2>
							</div>
							<div class="item_container">
								<c:if test="${not empty searchPageData.results}">
									<h2>
										<b><p>
												<spring:theme code="text.account.orderHistory.viewOrders"
													text="View your orders" />
											</p></b>
									</h2>
									<div class="line_history">
										<nav:orderPagination top="true" maxPageLimit="${maxPageLimit}"
											requestShowAll="${isShowAllRequested}"
											supportShowAll="${isShowAllAllowed}"
											searchPageData="${searchPageData}"
											searchUrl="/my-account/orders?sort=${searchPageData.pagination.sort}"
											msgKey="text.account.orderHistory.page" />
									</div>
									<table id="order_history_middle" style="width: 659px;">
										<thead>
											<tr> 
												<th id="header1"><b></b><spring:theme
														code="text.account.orderHistory.orderNumber"
														text="Order Number" /></b></th>
												<th id="header2"><b><spring:theme
														code="text.account.orderHistory.orderStatus"
														text="Order Status" /></b></th>
												<th id="header3"><b><spring:theme
														code="text.account.orderHistory.datePlaced"
														text="Date Placed" /></b></th>
												<th id="header4"><b><spring:theme
														code="text.account.orderHistory.actions" text="Actions" /></b></th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${searchPageData.results}" var="order">

												<c:url value="/my-account/order/${order.code}"
													var="myAccountOrderDetailsUrl" />

												<tr>
													<td headers="header1"><ycommerce:testId
															code="orderHistory_orderNumber_link">
															<p><a href="${myAccountOrderDetailsUrl}">${order.code}</a></p>
														</ycommerce:testId></td>
													<td headers="header2"><ycommerce:testId
															code="orderHistory_orderStatus_label">
															<p>${order.status}</p>
														</ycommerce:testId></td>
													<td headers="header3"><ycommerce:testId
															code="orderHistory_orderDate_label">
															<p>
																<fmt:formatDate value="${order.placed}" dateStyle="long"
																	timeStyle="short" type="both" />
															</p>
														</ycommerce:testId></td>
													<td headers="header4"><ycommerce:testId
															code="orderHistory_Actions_links">
															<p>
																<a href="${myAccountOrderDetailsUrl}" style="color: blue;"><spring:theme
																		code="text.view" text="View" /></a>
															</p>
														</ycommerce:testId></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>

									<nav:orderPagination top="false" maxPageLimit="${maxPageLimit}"
										requestShowAll="${isShowAllRequested}"
										supportShowAll="${isShowAllAllowed}"
										searchPageData="${searchPageData}"
										searchUrl="/my-account/orders?sort=${searchPageData.pagination.sort}"
										msgKey="text.account.orderHistory.page" />

								</c:if>
								<c:if test="${empty searchPageData.results}">
									<p>
										<spring:theme code="text.account.orderHistory.noOrders"
											text="You have no orders" />
									</p>
								</c:if>
							</div>
						</div>
					</div>
				</div>
			</div>
			</div>
			</div>
</template:page>
