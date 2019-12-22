var myApp = angular.module('myApp', []); //utworzenie modułu o danej nazwie

//stałe
const mainURL = "http://localhost:8080/springDBProject/mainPage/";
const ENTER_KEYCODE = 13;
const ESC_KEYCODE = 27;

myApp.controller('myAppController', ['$scope', '$http', '$filter', function ($scope, $http, $filter) {
	$scope.virtualTab;

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
			var result = [];
			//i>0 pomiń kolumnę z id
			forRange(keys.length, i => { if (i > 0 && keys[i] != "$$hashKey") result.push(obj[keys[i]]); });
			return result.join(", ");
		}
		return obj;
	}

	$scope.findAll = function () {
		if ($scope.tableCBox != $scope.tables[0]) {
			$scope.recordSB = [];
			$http.get(mainURL + $scope.tableCBox.value)
				.then(function (response) {
					console.log(response.data);
					if (response.data.length > 0) {
						$scope.virtualTab = new VirtualTable(response, false);
						forRange($scope.virtualTab.foreignColumns.length, i => {
							$http.get(mainURL + $scope.virtualTab.foreignColumns[i])
								.then(function (response) {
									$scope.virtualTab.addForeignRecordsForColumn(response, i);
								});
						});
					} else {
						alert("Table " + $scope.tableCBox.name + " is empty!");
					}
					// console.log($scope.virtualTab.foreignRecordsForEachForeignColumn);
				});
		}
	}

	$scope.findById = function () {
		if ($scope.tableCBox != $scope.tables[0] && $scope.id > 0) {
			$http.get(mainURL + $scope.tableCBox.value + "/" + $scope.id)
				.then(function (response) {
					if (response.status != 404) {
						$scope.virtualTab = new VirtualTable(response, true);
						forRange($scope.virtualTab.foreignColumns.length, i => {
							$http.get(mainURL + $scope.virtualTab.foreignColumns[i])
								.then(function (response) {
									$scope.virtualTab.addForeignRecordsForColumn(response, i);
								});
						});
					}
				}).catch(function (error) {
					printErrorFromServer(error);
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
		if ($scope.tableCBox != $scope.tables[0] && $scope.virtualTab.wasNewRecordAdded == false) {
			forRange($scope.virtualTab.columnCount, i => {
				$scope.recordSBStyle[i] = "error";
			});
			$scope.virtualTab.addNotSetRecord();
		} else {
			alert("No table was chosen or one new record was already added!");
		}
	}

	$scope.updateInputValue = { value: "" };
	$scope.updateValueHandler = function () {

		const previousValue = $scope.virtualTab.getUpdatedColumnName();
		var updatingUIContent = $scope.updateInputValue.value;
		if ($scope.virtualTab.getUpdatedColumnName() == "dateOfRelease") //na siłę wpisana kolumna
			updatingUIContent = $filter('date')(updatingUIContent, "yyyy-MM-dd");

		if (updatingUIContent.length > 0 ||
			Object.keys(updatingUIContent).length > 0) {
			$scope.virtualTab.setUpdatedFieldValue(updatingUIContent);

			const updatedRecord = $scope.virtualTab.getUpdatedRecord();
			$http.put(mainURL + $scope.tableCBox.value + "/" + updatedRecord["id"], updatedRecord)
				.then(function (response) {
					$scope.virtualTab.removeUpdatingUIElement();
					if (response.status != 200) {
						//przywróc starą wartość
						$scope.virtualTab.setUpdatedFieldValue(previousValue);
					}
				}).catch(function (error) {
					printErrorFromServer(error);
				});
		} else {
			alert("Updated field is empty or no item was chosen from combo box!");
		}
	}

	$scope.undoAction = function () {
		$scope.virtualTab.removeUpdatingUIElement();
		$scope.updateInputValue.value = "";
	}


	$scope.recordSB = [];
	$scope.recordSBStyle = [];
	$scope.inputChanged = function ($index, column) {
		$scope.recordSBStyle[$index] = ($scope.virtualTab.doesInputCorrect(column, $scope.recordSB[$index])) ? "correct" : "error";
	}

	$scope.saveRecordHandler = function () {
		if ($scope.recordSB.length != $scope.virtualTab.columnCount){
			alert("Not all fields where set!");
			return;
		}
		for (var i = 0; i < $scope.recordSBStyle.length; i++) {
			if ($scope.recordSBStyle[i] == "error") {
				alert("At least one field has invalid value!");
				return;
			}
		}

		var recordToSave={};
		forRange($scope.recordSB.length,i=>{
			recordToSave[$scope.virtualTab.columns[i]]=$scope.recordSB[i];
		});

		$http.post(mainURL+$scope.tableCBox.value,recordToSave)
			.then(function(response){
				console.log(response);
				if(response.status==200){
					$scope.findAll();
				}
		}).catch(function(error){
			printErrorFromServer(error);
		})

	}
}]);


const printErrorFromServer = function (error) {
	alert(error.data.errorMessage + "\n\t" + error.data.solutions);
}