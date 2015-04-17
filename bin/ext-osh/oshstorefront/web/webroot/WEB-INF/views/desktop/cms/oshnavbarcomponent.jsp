<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>

	<li class="dir maindir"><span class="dropmenuahead"> <component:cmsLinkComponent
				component="${component.link}" />
	</span> <c:if test="${not empty component.navigationNode.children}">

			<ul>
				<li style="float: left; width: 100%;">
					<table class="tables" width="929">
						<tbody>
							<tr>

								<td class="firstcolume" valign="top">
									<ul>
										<c:forEach items="${component.navigationNode.children}" varStatus="index"
											var="child">
											<c:set var="styleClass" value=""></c:set>
											<c:if test="${index.count eq 3}">
											<c:set var="styleClass" value="third"> </c:set>
											</c:if>
											<c:if test="${index.count eq 5}">
											<c:set var="styleClass" value="fifth"> </c:set>
											</c:if>
											<%-- ${styleClass} --%>
											<c:if test="${child.name ne 'Shop By Brand'}">
												<li class="dir" style="min-height: 146px;">
												<a href="${request.contextPath}/${child.url}">${child.name}</a>	
													<ul>
														<c:forEach items="${child.links}" var="childlink"
															begin="${i.index}">
															<li><component:cmsLinkComponent
																	component="${childlink}" /></li>
														</c:forEach>
													</ul>
											</c:if>
										</c:forEach>
									</ul>
								<!-- </td>

								<td class="secondcolume" valign="top"> -->
									<%-- <ul>
										<c:forEach items="${component.navigationNode.children}"
											var="child" varStatus="index">
											<c:if test="${child.name ne 'Shop By Brand'}">
												<li class="dir ${styleClass}" style="min-height: 146px;">
												<a href="${request.contextPath}/${child.url}">${child.name}</a>	
													<ul>
														<c:forEach items="${child.links}" var="childlink"
															begin="${i.index}">
															<li><component:cmsLinkComponent
																	component="${childlink}" /></li>
														</c:forEach>
													</ul>
											</c:if>
										</c:forEach>
									</ul> --%>
								</td>
								<td class="thirdcolume" valign="top">
									<ul>
										<c:forEach items="${component.navigationNode.children}"
											var="child">
											 <c:if test="${child.name eq 'Shop By Brand'}">
												<li class="dir" style="min-height: 146px;">
													${child.name}
													<ul>
														<c:forEach items="${child.links}" var="childlink"
															begin="${i.index}">
															<li><component:cmsLinkComponent
																	component="${childlink}" /></li>
														</c:forEach>
													</ul>
												</li>
											</c:if> 
										</c:forEach>
									</ul>
								</td>
								<td class="lastcolume" valign="top"><a href="#"><img
										width="148" alt="" src="${component.media.url}"></a></td>
							</tr>
						</tbody>
					</table>
			</ul>
		</c:if>
