function UpdatesController(o,t,e,l){var n=this;n.globalID=e.id,o.products=[],console.log(e),l.get("/api/v1/updates/"+e.update_id).then(function(t){console.log(t),o.products=t.data}),o.openProduct=function(o){console.log("open products globalID : "+o),t.path("/product/"+o)}}UpdatesController.$inject=["$scope","$location","$routeParams","$http"],angular.module("ShopickApp").controller("UpdatesCtrl",UpdatesController).component("updatesDetail",{templateUrl:"assets/updates/updates.template.html.erb",controller:UpdatesController});