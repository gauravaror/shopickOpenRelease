
function SideNavController($scope, $mdMedia, $mdSidenav, $route, $routeParams, $location, $mdDialog) {
	  	var self = this;
	  	this.$route = $route;
	    this.$location = $location;
	    this.$routeParams = $routeParams;
	    this.progress_activated = false;
		$scope.go = function ( path ) {
			console.log(path)
		  $location.path( path );
		};
		$scope.openLink = function ( path ) {
			console.log(path)
		  $location.path( path );
		  $mdSidenav('left')
          .toggle()

		};
		$scope.openMenu = function () {
			console.log("openMenu")
        $mdSidenav('left')
          .toggle()

		}

		$scope.showLogin = function(ev) {
		    var useFullScreen = ($mdMedia('sm') || $mdMedia('xs'))  && $scope.customFullscreen;
		    $mdDialog.show({
		      controller: DialogController,
		      templateUrl: 'login.template.html',
		      parent: angular.element(document.body),
		      targetEvent: ev,
		      clickOutsideToClose:true,
		      fullscreen: useFullScreen
		    })
		    .then(function(answer) {
		      $scope.status = 'You said the information was "' + answer + '".';
		    }, function() {
		      $scope.status = 'You cancelled the dialog.';
		    });
		    $scope.$watch(function() {
		      return $mdMedia('xs') || $mdMedia('sm');
		    }, function(wantsFullScreen) {
		      $scope.customFullscreen = (wantsFullScreen === true);
		    });
  		};



		/**
     * Supplies a function that will continue to operate until the
     * time is up.
     */
    function debounce(func, wait, context) {
      var timer;
      return function debounced() {
        var context = $scope,
            args = Array.prototype.slice.call(arguments);
        $timeout.cancel(timer);
        timer = $timeout(function() {
          timer = undefined;
          func.apply(context, args);
        }, wait || 10);
      };
    }
    /**
     * Build handler to open/close a SideNav; when animation finishes
     * report completion in console
     */
    function buildDelayedToggler(navID) {
      return debounce(function() {
        // Component lookup should always be available since we are not using `ng-if`
        $mdSidenav(navID)
          .toggle()
          .then(function () {
            $log.debug("toggle " + navID + " is done");
          });
      }, 200);
    }
    function buildToggler(navID) {
      return function() {
        // Component lookup should always be available since we are not using `ng-if`
        $mdSidenav(navID)
          .toggle()
          .then(function () {
            $log.debug("toggle " + navID + " is done");
          });
      }
    }

	  self.posts = [ {username: 'Gaurav', description: 'dsfdsf'},{username: 'Gaurav1', description: 'dsfdsf1'}];

		self.menuItem = [
			{
				name: 'Home',
				icon: 'home',
				url: '/'
			},
			{
				name: 'Offer',
				icon: 'local_offer',
				url: '/offer'
			},
			{
				name: 'Collection',
				icon: 'trending_up',
				url: '/liked_collection'
			},
			{
				name: 'Latest Launch',
				icon: 'view_quilt',
				url: '/latest_launch'
			},
			{
				name: 'Picks',
				icon: 'account_balance_wallet',
				url: ''
			},
			{
				name: 'Post',
				icon: 'camera_alt',
				url: ''
			},
			{
				name: 'Refer & Win',
				icon: 'share',
				url: ''
			},
			{
				name: 'Rate Us',
				icon: 'rate_review',
				url: ''
			},
			{
				name: 'Feedback',
				icon: 'email',
				url: ''
			},
			{
				name: 'Settings',
				icon: 'settings',
				url: ''
			},
			{
				name: 'Contact Us',
				icon: 'phone',
				url: ''
			},
			{
				name: 'Download',
				icon: 'get_app',
				url: ''
			}
		];

};

SideNavController.$inject = ['$scope', '$mdMedia', '$mdSidenav','$route', '$routeParams', '$location', '$mdDialog'];

angular.
  module('ShopickApp').
  controller("SideNavCtrl",SideNavController).
  component('shopickNav', {
	  templateUrl: 'side-nav/side-nav.template.html',
	  controller: SideNavController
});

  
function DialogController($scope, $mdDialog) {
  $scope.hide = function() {
    $mdDialog.hide();
  };
  $scope.cancel = function() {
    $mdDialog.cancel();
  };
  $scope.answer = function(answer) {
    $mdDialog.hide(answer);
  };
}
;
