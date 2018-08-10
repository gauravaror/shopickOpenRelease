
function UpdatesController($scope, $location, $routeParams, $http) {
	  var self = this;
	  self.globalID = $routeParams.id;
	  $scope.activated = true;
	  $scope.products = [];
	  console.log($routeParams);
	  $http.get('/api/v1/updates/' + $routeParams.update_id).then(function(response) {
	  	console.log(response);
        $scope.products = response.data;
        $scope.activated = false;
      });

      $scope.openProduct = function (globalID) {
	      console.log("open products globalID : "+ globalID);
	      $location.path( '/product/' + globalID );

      }

};

UpdatesController.$inject = ['$scope', '$location', '$routeParams' ,'$http'];

angular.
  module('ShopickApp').
  controller("UpdatesCtrl", UpdatesController).
  component('updatesDetail', {
	  templateUrl: 'updates/updates.template.html',
	  controller: UpdatesController
});
