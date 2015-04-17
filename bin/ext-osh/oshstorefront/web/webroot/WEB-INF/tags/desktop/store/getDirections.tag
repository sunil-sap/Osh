<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="store" required="false"
	type="de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript"
	src="http://maps.google.com/maps/api/js?sensor=false"></script>


<div id="panel"></div>


<script type="text/javascript"> 
   document.getElementById('source').value="";
   function getDirection(){
	  
	document.getElementById('panel').innerHTML="";
   var source=document.getElementById('source').value;
   var dest=document.getElementById('dest').value;
   
     var directionsService = new google.maps.DirectionsService();
     var directionsDisplay = new google.maps.DirectionsRenderer();

     var map = new google.maps.Map(document.getElementById('map_canvas'), {
       zoom:7,
       mapTypeId: google.maps.MapTypeId.ROADMAP
     });

     directionsDisplay.setMap(map);
     directionsDisplay.setPanel(document.getElementById('panel'));

     var request = {
       origin: source, 
       destination: dest,
       travelMode: google.maps.DirectionsTravelMode.DRIVING
     };

     directionsService.route(request, function(response, status) {
       if (status == google.maps.DirectionsStatus.OK) {
         directionsDisplay.setDirections(response);
       }
     });
   }
   </script>

