var receta = angular.module('receta', []);



var Controller_ = 	function($scope, $http) {
	var geocoder = new google.maps.Geocoder;

    $scope.count = 0;
    $scope.focused =  false;
    $scope.loadinglocation = false;
    $scope.location_ = "";

    $scope.showLocation = function() {
	if (navigator.geolocation) {
	$scope.loadinglocation = true;
    navigator.geolocation.getCurrentPosition(function(position) {
      var pos = {
        lat: position.coords.latitude,
        lng: position.coords.longitude
      };
      var latlng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);

      $scope.lat_ = pos.lat;
      $scope.lon_ = pos.lon;
      console.log($scope.lat_);
      //$('#offer_lat_').value = pos.lat;
      //$('#offer_lon_').value = pos.lon;
      //$scope.$apply();
        geocoder.geocode({'location': latlng}, function(results, status) {
    if (status === google.maps.GeocoderStatus.OK) {
      if (results[0].formatted_address) {
      	console.log(results[0].formatted_address);
      	$scope.location_ = results[0].formatted_address;
      	 $scope.loadinglocation = false;
      	// $('#offer_location_').value = results[0].formatted_address;
      	 $scope.$apply();
      } else {
        console.log('No results found');
        $scope.loadinglocation = false;
        $scope.$apply();
      }
    } else {
      console.log('Geocoder failed due to: ' + status);
      $scope.loadinglocation = false;
      $scope.$apply();
    }
    $scope.$apply();
  });

    }, function() {
      handleLocationError(true);
    });
  } else {
    // Browser doesn't support Geolocation
    handleLocationError(false);
  }
}
};


function handleLocationError(browserHasGeolocation) {
	console.log(browserHasGeolocation);
};
Controller_.$inject = ['$scope', '$http'];



angular.module('receta').controller('myCtrl', Controller_);


