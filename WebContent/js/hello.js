var myApp=angular.module('demo',[]);

myApp.controller('Hello',['$scope', function($scope){
	$scope.id=0;
	$scope.name="nothing";
	
//	$scope.getGenre=function(){
//		$http.get('http://localhost:8080/springDBProject/genre').then(function(response) {
//			$scope.id=response.data.id;
//			$scope.name=response.data.name;
//		});
//	}
}]);