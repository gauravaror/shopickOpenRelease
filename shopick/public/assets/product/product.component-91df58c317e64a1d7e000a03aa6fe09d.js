
function ProductController($scope, $location, $routeParams, $http) {
	  var self = this;
	  self.globalID = $routeParams.id;
	  $scope.product = {};
	  $scope.activated = true;
	  console.log($routeParams);

	  $http.get('/api/v1/product_global/' + $routeParams.product_id).then(function(response) {
	  	console.log(response);
        $scope.product = response.data;
        $scope.activated = false;
      });

      $scope.openProduct = function (globalID) {
	      console.log("open products globalID : "+ globalID);
	      $location.path( '/product/' + globalID );

      }


};

ProductController.$inject = ['$scope', '$location', '$routeParams' ,'$http'];

angular.
  module('ShopickApp').
  controller("ProductCtrl", ProductController).
  component('productDetail', {
	  templateUrl: 'product/product.template.html',
	  controller: ProductController
});
