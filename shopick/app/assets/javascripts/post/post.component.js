
function PostController($scope, $location, $routeParams, $http) {
	  var self = this;
	  self.globalID = $routeParams.post_id;
	  $scope.post = {};
	  $scope.activated = true;
	  console.log($routeParams);

	  $http.get('/api/v1/post_get_with_similar/' + $routeParams.post_id).then(function(response) {
	  	console.log(response);
        $scope.post = response.data;
        $scope.activated = false;
      });

      $scope.openPost = function (globalID) {
	      console.log("open products globalID : "+ globalID);
	      $location.path( '/post/' + globalID );

      }


};

PostController.$inject = ['$scope', '$location', '$routeParams' ,'$http'];

angular.
  module('ShopickApp').
  controller("PostCtrl", PostController).
  component('postDetail', {
	  templateUrl: 'post/post.template.html',
	  controller: PostController
});
