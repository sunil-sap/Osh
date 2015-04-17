<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>

<%@ attribute name="openingSchedule" required="true" type="de.hybris.platform.commercefacades.storelocator.data.OpeningScheduleData" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="/WEB-INF/tld/ycommercetags.tld"%>
<%@ taglib prefix="store" tagdir="/WEB-INF/tags/desktop/store"%>


<c:if test="${not empty openingSchedule}">
	<ycommerce:testId code="storeDetails_table_openingSchedule_label">
		<td class="store_hours">
			<div class="ls_workinghours">
				<c:forEach items="${openingSchedule.weekDayOpeningList}"
					var="weekDay">

					<div>
						${weekDay.weekDay}&nbsp;
						<c:choose>
							<c:when test="${weekDay.closed}">
								<spring:theme code="storeDetails.table.opening.closed" />
							</c:when>
							<c:otherwise>
									${weekDay.openingTime.formattedHour} - ${weekDay.closingTime.formattedHour}
								</c:otherwise>
						</c:choose>
					</div>
				</c:forEach>
			</div>
		</td>
	</ycommerce:testId>
</c:if>
