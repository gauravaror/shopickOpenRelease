
function LikedCollectionController($scope, $location,$http) {
	  
	  var self = this;
	  $scope.activated = true;
	  $scope.posts = [];
	  $http.get('/api/v1/feed/105').then(function(response) {
	  	console.log(response);
        $scope.posts = response.data;
       	$scope.activated = false;
      });
       $scope.openPost = function (globalID) {
	      console.log("open products globalID : "+ globalID);
	      $location.path( '/post/' + globalID );

      }

};

LikedCollectionController.$inject = ['$scope', '$location','$http'];

angular.
  module('ShopickApp').
  controller("LikedCollectionCtrl", LikedCollectionController).
  component('likedCollection', {
	  templateUrl: 'liked_collection/liked_collection.template.html',
	  controller: LikedCollectionController
});
