var myApp = angular.module('demo', []);
var mainURL = "http://localhost:8080/springDBProject/mainPage/";

myApp.controller('Hello', ['$scope', '$http', function ($scope, $http) {
	$scope.id = 0;
	$scope.name = "nothing";

	$scope.records = [];
	$scope.columns = [];

	$scope.tables = [
		{ name: "-- choose table --", value: "" },
		{ name: "kategorie wiekowe", value: "AgeCategory" },
		{ name: "gry konsolowe", value: "ConsoleGame" },
		{ name: "tryby gry", value: "GameplayMode" },
		{ name: "gatunki", value: "Genre" },
		{ name: "platformy sprzętowe", value: "HardwarePlatform" },
		{ name: "języki", value: "Language" },
		{ name: "producenci", value: "Producer" },
		{ name: "wydawcy", value: "Publisher" }
	];

	$scope.tableCBox = $scope.tables[0];

	$scope.toString = function (obj) {
		var keys=Object.keys(obj);
		if (keys.length > 0 && typeof obj != 'string') {
			var result = [], i;
			for (i=1;i<keys.length;i++) {
				result.push(obj[keys[i]]);
			}
			return result.join(", ");
		}
		return obj;
	}

	$scope.findAll = function () {
		if ($scope.tableCBox != $scope.tables[0]) {
			$http.get(mainURL + $scope.tableCBox.value)
				.then(function (response) {
					console.log(response.data);
					$scope.records = response.data;
					if (response.data.length > 0) {
						$scope.columns = Object.keys($scope.records[0]);
					}
				});
		}
	}

	$scope.findById = function () {
		console.log($scope.id);
		// if($scope.tableCBox!=$scope.tables[0]){
		// 	$http.get("http://localhost:8080/springDBProject/mainPage/"+$scope.tableCBox.value)
		// 	.then(function(response){
		// 		console.log(response);
		// 		$scope.records=response.data;
		// 	});
		// }
	}
}]);