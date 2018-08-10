
function LatestLaunchController($scope, $location, $http) {
	  var self = this;
	  $scope.brand_updates = [];
	  $scope.activated = true;
	  $http.get('/api/v1/featured_brand_updates').then(function(response) {
	  	console.log(response);
        $scope.brand_updates = response.data;
        $scope.activated = false;
      });
      $scope.openUpdate =  function ( id ) {
	      console.log("open Update : "+ id);
	      $location.path( '/updates/' + id );
    };

};

LatestLaunchController.$inject = ['$scope', '$location', '$http'];

angular.
  module('ShopickApp').
  controller("LatestLaunchCtrl", LatestLaunchController).
  component('latestLaunch', {
	  templateUrl: 'latest_launch/latest_launch.template.html',
	  controller: LatestLaunchController
});
