<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld" %>	
 <template:master pageTitle="${pageTitle}">          	
                  
<div id="storeMainTopBanner"> 
<span id="bphId_2065">
<style>
#storeMain div#textBlock #storeMainTopBanner {
margin: 0 0 8px 0;
}
 div#textBlock #storeMainTopBanner {
  width: 960px;
  height: 390px;
  overflow: hidden;
  position: relative;
 }

 div#textBlock #storeMainTopBanner ul.slides {
  width: 960px;
  height: 390px;
  margin: 0;
  position: relative;
 }

 div#textBlock #storeMainTopBanner ul.slides li {
  position: absolute;
  display: block;
  width: 960px;
  height: 390px;
  margin: 0;
  padding: 0;
  top: 0;
  left: 0;
  list-style: none;
  background: #fff;
 }
 div#textBlock #storeMainTopBanner ul.slides li:first-child {
  z-index: 20;
 }

 div#textBlock #storeMainTopBanner ul.slides .slideLink {
  position: absolute;
  display: block;
  width: 730px;
  height: 390px;
  text-indent: 100%;
  white-space: nowrap;
  overflow: hidden;
  z-index: 1;
  text-decoration: none;
  border: none;
 }
 div#textBlock #storeMainTopBanner ul.slides .slideLink:hover {
  text-decoration: none;
  border: none;
 }

 div#textBlock #storeMainTopBanner ul.slides .button {
  position: absolute;
  display: block;
  padding: 0 10px 0 10px;
  height: 28px;
  width: 110px;
  font: bold 12px/28px Verdana, Geneva, Arial, Helvetica, sans-serif;
  text-transform: lowercase;
  text-decoration: none;
  text-align: center;
  background: #778f03;
  color: #fff;
  border: 1px solid #bdccb8;
  z-index: 5;
 }

 div#textBlock #storeMainTopBanner ul.slides .button:hover {
  background: #7a724c;
 }

 div#textBlock #storeMainTopBanner .indicators {
 padding: 0;
 margin: 0;
 position: absolute;
 top: 220px;
 left: 770px;
 z-index: 25;
 }

 div#textBlock #storeMainTopBanner .indicators a {
  display: block;
  float: left;
  list-style: none;
  width: 181px;
  height: 55px;
  margin-right: 10px;
  background: transparent;
  border: 0px solid #aaa;
  text-indent: 100%;
  white-space: nowrap;
  overflow: hidden;
 }

 div#textBlock #storeMainTopBanner .indicators a.activeSlide, div#textBlock #storeMainTopBanner .indicators a.activeSlide:hover, div#textBlock #storeMainTopBanner:hover .indicators a.activeSlide, div#textBlock #storeMainTopBanner .indicators a:hover {
  background: transparent;
 }
</style>

<div id="textBlock">
 <div id="storeMainTopBanner">
  <ul class="slides">
                                      
       <c:forEach items="${banners}" var="banner" varStatus="status">
   <li data-timeout="${timeOut}" ><img src="${banner.media.url}" alt="${not empty banner.headline ? banner.headline : banner.media.altText}"/></li>
    </c:forEach>      
  
  </ul>
  <div class="indicators">
  </div>
 </div>
</div>
<script src="${request.contextPath}/_ui/desktop/osh/js/jquery.cycle.all.js"></script>
<script src="${request.contextPath}/_ui/desktop/osh/js/slideshow.js"></script>

</span>
</div>                               
            
</template:master>   

