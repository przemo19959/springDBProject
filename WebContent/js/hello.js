var myApp = angular.module('demo', []);

myApp.controller('Hello', ['$scope','$http',function($scope, $http) {
	$scope.id = 0;
	$scope.name = "nothing";
	$scope.records=[];
	
	$scope.tables=[
		{name:"-- choose table --",value:""},
		{name:"kategorie wiekowe",value:"ageCategory"},
		{name:"gry konsolowe",value:"consoleGame"},
		{name:"tryby gry",value:"gameplayMode"},
		{name:"gatunki",value:"genre"},
		{name:"platformy sprzętowe", value:"hardwarePlatform"},
		{name:"języki",value:"language"},
		{name:"producenci",value:"producer"},
		{name:"wydawcy",value:"publisher"}
	];

	$scope.tableCBox=$scope.tables[0];

	$scope.findAll=function(){
		if($scope.tableCBox!=$scope.tables[0]){
			$http.get("http://localhost:8080/springDBProject/mainPage/"+$scope.tableCBox.value)
			.then(function(response){
				console.log(response);
				$scope.records=response.data;
			});
		}
	}

	$scope.findById=function(){
		console.log($scope.id);
		// if($scope.tableCBox!=$scope.tables[0]){
		// 	$http.get("http://localhost:8080/springDBProject/mainPage/"+$scope.tableCBox.value)
		// 	.then(function(response){
		// 		console.log(response);
		// 		$scope.records=response.data;
		// 	});
		// }
	}
} ]);