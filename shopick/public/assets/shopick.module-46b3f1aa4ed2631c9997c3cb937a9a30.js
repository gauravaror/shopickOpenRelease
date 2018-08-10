var shopickApp = angular.module('ShopickApp',['ngMaterial', 'ngMessages', 'ngRoute', 'ngMdIcons', 'templates']).config([ '$mdThemingProvider', function($mdThemingProvider) {
  $mdThemingProvider.theme('default')
    .primaryPalette('blue-grey')
    .accentPalette('orange');
}]).config(['$routeProvider', '$locationProvider',
  function($routeProvider, $locationProvider) {
    $routeProvider
      .when('/offer', {
        templateUrl: 'offer/offer.home.html',
        controller: 'SideNavCtrl',
        controllerAs: 'book'
      })
      .when('/liked_collection', {
        templateUrl: 'liked_collection/liked_collection.home.html',
        controller: 'SideNavCtrl',
        controllerAs: 'book'
      })
      .when('/collection/:collection_id', {
        templateUrl: 'collection/collection.home.html',
        controller: 'SideNavCtrl',
        controllerAs: 'book'
      }).when('/post/:post_id', {
        templateUrl: 'post/post.home.html',
        controller: 'SideNavCtrl',
        controllerAs: 'book'
      }).when('/product/:product_id', {
        templateUrl: 'product/product.home.html',
        controller: 'SideNavCtrl',
        controllerAs: 'book'
      })
      .when('/updates/:update_id', {
        templateUrl: 'updates/updates.home.html',
        controller: 'SideNavCtrl',
        controllerAs: 'book'
      })
      .when('/latest_launch', {
        templateUrl: 'latest_launch/latest_launch.home.html',
        controller: 'SideNavCtrl',
        controllerAs: 'book'
      })
      .when('/', {
        templateUrl: 'home.template.html',
        controller: 'SideNavCtrl',
        controllerAs: 'book'
      });

    $locationProvider.html5Mode(true);
}]);
shopickApp.$inject = ['ngMaterial', 'ngMessages', 'ngRoute', 'ngMdIcons', 'templates'];
