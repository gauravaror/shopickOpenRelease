function LatestLaunchController(e,t,o){e.brand_updates=[],o.get("/api/v1/featured_brand_updates").then(function(t){console.log(t),e.brand_updates=t.data}),e.openUpdate=function(e){console.log("open Update : "+e),t.path("/updates/"+e)}}LatestLaunchController.$inject=["$scope","$location","$http"],angular.module("ShopickApp").controller("LatestLaunchCtrl",LatestLaunchController).component("latestLaunch",{templateUrl:"assets/latest_launch/latest_launch.template.html.erb",controller:LatestLaunchController});