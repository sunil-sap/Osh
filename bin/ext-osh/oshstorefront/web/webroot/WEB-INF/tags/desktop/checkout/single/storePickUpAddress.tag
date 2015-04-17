  <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
  	 <h2><spring:theme code="checkout.summary.store.pickup.address"/></h2>
              <div class="bopisborderbox">
                <div class="cartformwarp">
                  <div class="cartformwarp_inner">
                    <p>${posData.name}</p>
                    <p>${posData.address.line1}</p>
                    <p>${posData.address.town}, ${posData.address.postalCode}</p>
                    <p>${posData.address.phone}</p>
                  </div>
                  <a class="linkgetdirection" href="${storeUrl}"><spring:theme code="osh.storeLocator.page.getDirection" /></a></div>
              </div>
      