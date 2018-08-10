
function CollectionController($scope, $location, $routeParams, $http) {
	  var self = this;
	  self.globalID = $routeParams.id;
	  $scope.activated = true;
	  $scope.posts = [];
	  console.log($routeParams);
	  $http.get('/api/v1/post_collection/' + $routeParams.collection_id).then(function(response) {
	  	console.log(response);
        $scope.posts = response.data;
        $scope.activated = false;
      });
      $scope.openPost = function (globalID) {
	      console.log("open products globalID : "+ globalID);
	      $location.path( '/post/' + globalID );

      }

};

CollectionController.$inject = ['$scope', '$location', '$routeParams' ,'$http'];

angular.
  module('ShopickApp').
  controller("CollectionCtrl", CollectionController).
  component('collectionDetail', {
	  templateUrl: 'collection/collection.template.html',
	  controller: CollectionController
});
