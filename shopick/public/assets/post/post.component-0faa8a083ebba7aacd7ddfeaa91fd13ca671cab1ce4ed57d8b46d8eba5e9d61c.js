function PostController(o,t,l,e){var n=this;n.globalID=l.post_id,o.post={},console.log(l),e.get("/api/v1/post_get_with_similar/"+l.post_id).then(function(t){console.log(t),o.post=t.data}),o.openPost=function(o){console.log("open products globalID : "+o),t.path("/post/"+o)}}PostController.$inject=["$scope","$location","$routeParams","$http"],angular.module("ShopickApp").controller("PostCtrl",PostController).component("postDetail",{templateUrl:"post/post.template.html",controller:PostController});