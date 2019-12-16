var myApp = angular.module('myApp', []); //utworzenie modułu o danej nazwie
const mainURL = "http://localhost:8080/springDBProject/mainPage/";
const ENTER_KEYCODE = 13;

myApp.controller('myAppController', ['$scope', '$http', function ($scope, $http) {
	$scope.records = []; //wszystkie aktualne rekordy głównej tablicy UI
	$scope.columns = []; //wszystkie nazwy kolumn aktualnie wyświetlanej tabeli

	$scope.boolTab;

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
	$scope.idInputStyle = "error";

	$scope.processContent = function (obj) {
		return $scope.toString(obj);
	}

	$scope.toString = function (obj) {
		var keys = Object.keys(obj);
		if (keys.length > 0 && typeof obj != 'string') {
			var result = [], i;
			for (i = 1; i < keys.length; i++) {
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
					// console.log(response.data);
					$scope.boolTab = new BooleanTable(response);
					if (response.data.length > 0) {
						$scope.records = $scope.boolTab.records;
						$scope.columns = $scope.boolTab.columns;
					} else {
						alert("Table " + $scope.tableCBox.name + " is empty!");
					}
				});
		}
	}

	$scope.findById = function () {
		if ($scope.tableCBox != $scope.tables[0] && $scope.id > 0) {
			$http.get(mainURL + $scope.tableCBox.value + "/" + $scope.id)
				.then(function (response) {
					if (response.status != 404) {
						$scope.records = [];
						$scope.records[0] = response.data;
						$scope.columns = Object.keys($scope.records[0]);
					} else {
						alert("Table " + $scope.tableCBox.name + " doesn't contain record with id=" + $scope.id + "!");
					}
				});
		} else if ($scope.tableCBox == $scope.tables[0]) {
			alert("Table was't chosen!");
		} else if ($scope.id < 1 || $scope.id == undefined) {
			alert("Id value must be greater than 0, but is " + $scope.id + "!");
		}
	}

	$scope.idFieldChanged = function () {
		$scope.idInputStyle = ($scope.id > 0) ? "correct" : "error";
	}

	$scope.addRecordClickHandler = function () {
		// if($scope.tableCBox!=$scope.tables[0]){
		// 	$scope.records.push();
		// }else{
		// 	alert("No table was chosen!");
		// }
	}

	$scope.updateValue = function ($event, input,column) {
		if ($event.keyCode == ENTER_KEYCODE) {
			var previousValue=$scope.boolTab.getUpdatedRecord()[column];
			if (input.updateInputValue.length > 0) {
				$scope.boolTab.setRecordValue(input.updateInputValue);
			}
			var updatedRecord=$scope.boolTab.getUpdatedRecord();
			$http.put(mainURL + $scope.tableCBox.value + "/" + updatedRecord["id"], updatedRecord).then(function (response) {
				$scope.boolTab.removeUpdatingElement();
				if(response.status!=200){
					$scope.boolTab.setRecordValue(previousValue);				
				}
			});
		}
	}

}]);