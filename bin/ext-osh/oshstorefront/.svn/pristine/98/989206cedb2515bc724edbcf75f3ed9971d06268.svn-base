<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/desktop/nav/breadcrumb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<template:page pageTitle="${pageTitle}">
	<div id="middleContent">
		<div class="innermiddleContent">
			<div class="breadcrumb">
				<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
			</div>
			<div>
				<cms:slot var="feature" contentSlot="${slots.GlobalSection}">
					<cms:component component="${feature}" />
				</cms:slot>
			</div>

			<div class="mainmiddleContent">
				<div class="leftmaincontent">

					<div class="leftnav">
						<nav:categoryNav pageData="${searchPageData}" />
					</div>

					<div class="helpchat">
						<cms:slot var="feature" contentSlot="${slots.ES_HowToSection}">
							<cms:component component="${feature}" />
						</cms:slot>
					</div>
					<div>
						<cms:slot var="feature"
							contentSlot="${slots.ES_ProjectGuidesSection}">
							<cms:component component="${feature}" />
						</cms:slot>
					</div>
					<div class="helpchat">
						<cms:slot var="feature"
							contentSlot="${slots.ES_CatagorySaleSection}">
							<cms:component component="${feature}" />
						</cms:slot>
					</div>
				</div>

				<div class="middlemaincontent">
					
					<div class="middlemainheaderbanner">
						<img src="${categoryUrl}"></img>
					</div>

					<div class="toppagination">
						<span class="floatl">${categoryName}</span>
						<c:if test="${fn:length(allSubCategory) ge 8}">
							<span class="floatr" id="more"
								onclick="$('#less').show();$(this).hide();"> <a
								class="viewallcat" href="javascript:void(0)"> <spring:theme
										code="show.viewAll.category" /></a>
							</span>
							<span class="floatr" id="less" style="display: none"
								onclick="$('#more').show();$(this).hide();"> <a
								class="viewallcat" href="javascript:void(0)"> <spring:theme
										code="show.viewLess.category" /></a>
							</span>
						</c:if>
					</div>

					<div class="productList">
						<ul class="catagoryList">
							<c:forEach items="${allSubCategory}" var="category" begin="0"
								end="7">
								<li>
									<div class="productthumb">
										<c:choose>
											<c:when test="${not empty category.thumbnails}">
												<c:forEach items="${category.thumbnails}" var="media">

													<a
														href="${category.code}?q=%3A${searchPageData.sorts[0].name}%3Aprodlist%3Atrue&text=">
														<img style="height: 150px; width: 150px;"
														src="${media.url}" alt="" />
													</a>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<a
													href="${category.code}?q=%3A${searchPageData.sorts[0].name}%3Aprodlist%3Atrue&text=">
														<img  src="${request.contextPath}/_ui/desktop/theme-blue/images/missing-product-150x150.jpg"/>
												</a>
													</c:otherwise>
														</c:choose>
															</div>

									<div class="cat_producttitle">
										<a
											href="${category.code}?q=%3A${searchPageData.sorts[0].name}%3Aprodlist%3Atrue&text=">
											<span>${category.name}</span>
										</a>
									</div>
								</li>
							</c:forEach>
						</ul>
						<ul class="allcatagoryList" style="display: none">
							<c:forEach items="${allSubCategory}" var="category" begin="8">
								<li>
									<div class="productthumb">
										<c:choose>
											<c:when test="${not empty category.thumbnails}">

												<c:forEach items="${category.thumbnails}" var="media">
													<a
														href="${category.code}?q=%3A${searchPageData.sorts[0].name}%3Aprodlist%3Atrue&text=">
														<img src="${media.url}" alt="" />
													</a>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<a
													href="${category.code}?q=%3A${searchPageData.sorts[0].name}%3Aprodlist%3Atrue&text=">
												<img  src="${request.contextPath}/_ui/desktop/theme-blue/images/missing-product-150x150.jpg"/>
												</a>

											</c:otherwise>
										</c:choose>
									</div>



									<div class="cat_producttitle">
										<a
											href="${category.code}?q=%3A${searchPageData.sorts[0].name}%3Aprodlist%3Atrue&text=">
											<span>${category.name}</span>
										</a>
									</div>
								</li>
							</c:forEach>
						</ul>
					</div>

					<div style="padding: 10px 0px">
						<span><cms:slot var="feature"
								contentSlot="${slots.ES_CatagorySeoSection}">
								<cms:component component="${feature}" />
							</cms:slot></span>
					</div>

					<div class="toppagination">
						<span class="floatl"> <spring:theme
								code="show.category.brand" /></span>
					</div>

					<div class="productList">
						<ul>
							<c:forEach items="${brands}" var="brand" begin="0" end="11">
								<li>
								<c:choose>
								<c:when test="${not empty brand.thumbnails}">
								<c:forEach items="${brand.thumbnails}" var="thumbnail">
										<a
											href="?q=%3A${searchPageData.sorts[0].name}%3Abrand%3A${brand.code}%3Aprodlist%3Atrue&text=">
											<img style="height: 150px; width: 150px;"
											src="${thumbnail.url}" />
										</a>
									</c:forEach>
								</c:when>
								<c:otherwise>
								<a
											href="?q=%3A${searchPageData.sorts[0].name}%3Abrand%3A${brand.code}%3Aprodlist%3Atrue&text=">
											 <img  src="${request.contextPath}/_ui/desktop/theme-blue/images/missing-product-150x150.jpg"/>
											 </a>
								</c:otherwise>
								</c:choose>
									<div class="producttitle">
										<a
											href="?q=%3A${searchPageData.sorts[0].name}%3Abrand%3A${brand.code}%3Aprodlist%3Atrue&text=">
											<span>${brand.name}</span>
										</a>
									</div></li>
							</c:forEach>
						</ul>
					</div>
					<div style="padding: 10px 0px">
						<span><cms:slot var="feature"
								contentSlot="${slots.ES_BrandSeoSection}">
								<cms:component component="${feature}" />
							</cms:slot></span>
					</div>
				</div>

			</div>
		</div>
	</div>
</template:page>
<script>
	$(document).ready(
			function() {

				$('.viewallcat').bind('click', function() {
					$('.allcatagoryList').toggle();
				});
				// drop down menu

				$('.dir.maindir').bind(
						'mouseenter',
						function() {
							var max = -1;
							$(this).find(">ul").css('width', 'auto').center(
									true).find('.dir').each(function() {
								var h = $(this).height();
								max = h > max ? h : max;
							}).css('min-height', max);

						});
			});
</script>