
function OfferController($scope, $location, $http) {
    var self = this;
    $scope.post_collections = [];
    $scope.activated = true;
    $scope.openOffer =  function ( path ) {
      console.log("open Offer path : "+ path);
      $location.path( '/collection/' + path );
    };

    $http.get('/api/v1/get_my_collections/105').then(function(response) {
      console.log(response);
        $scope.post_collections = response.data;
        $scope.activated = false;
      });

      $scope.openPost = function (globalID) {
        console.log("open products globalID : "+ globalID);
        $location.path( '/post/' + globalID );

      }

};

OfferController.$inject = ['$scope', '$location', '$http'];

angular.
  module('ShopickApp').
  controller("OfferControllerCtrl", OfferController).
  component('postCollections', {
    templateUrl: 'offer/offer.template.html',
    controller: OfferController
});
