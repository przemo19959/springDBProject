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
			// $scope.recordSB = ["*"]; //dla kolumny id
			$scope.recordSB = { id: "*" };
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
					$scope.updateInputValue.value = "";
					$scope.virtualTab.removeUpdatingUIElement();
					if (response.status == 200) {
						printResponseFromServer(response);
					} else {//przywróc starą wartość
						$scope.virtualTab.setUpdatedFieldValue(previousValue);
					}
				}).catch(function (error) {
					$scope.updateInputValue.value = "";
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

	$scope.addRecordClickHandler = function () {
		if ($scope.tableCBox != $scope.tables[0] && $scope.virtualTab.wasNewRecordAdded == false) {
			forRange($scope.virtualTab.columnCount, i => {
				if (i > 0) {	//pomijamy kolumnę id => wymuszono correct
					if ($scope.virtualTab.columns[i] == "dateOfRelease") {
						$scope.recordSB["dateOfRelease"] = new Date(); //początkowa inicjalizacja aktualną datą
						$scope.recordSBStyle[i] = "correct";
					} else
						$scope.recordSBStyle[i] = "error";
				}
			});
			console.log($scope.recordSB);
			$scope.virtualTab.addNotSetRecord();
		} else {
			alert("No table was chosen or one new record was already added!");
		}
	}

	//inicjowane wartościami dla kolumny id o wymuszonej zawartości *
	// $scope.recordSB = ["*"];
	$scope.recordSB = { id: "*" };
	$scope.recordSBStyle = ["correct"];
	$scope.inputChanged = function ($index, column, isForeign) {
		// console.log($scope.recordSB);
		// if (column == "dateOfRelease") //na sztywno wpisane
		// 	$scope.recordSB[$index] = $filter('date')($scope.recordSB[$index], "yyyy-MM-dd");
		$scope.recordSBStyle[$index] =
			($scope.virtualTab.doesInputCorrect(column,
				//  $scope.recordSB[$index]
				getFormatedDateIfDateObject($scope.recordSB[column])
				, isForeign)) ? "correct" : "error";
	}

	$scope.saveRecordHandler = function () {
		// if ($scope.recordSB.length != $scope.virtualTab.columnCount) {
		if (Object.keys($scope.recordSB).length != $scope.virtualTab.columnCount) {
			alert("Not all fields where set!");
			return;
		}
		for (var i = 0; i < $scope.recordSBStyle.length; i++) {
			if ($scope.recordSBStyle[i] == "error") {
				alert("At least one field has invalid value!");
				return;
			}
		}

		console.log($scope.recordSB);
		var recordToSave = {};
		forRange($scope.virtualTab.columnCount, i => {
			recordToSave[$scope.virtualTab.columns[i]] = getFormatedDateIfDateObject($scope.recordSB[$scope.virtualTab.columns[i]]);
		});
		console.log(recordToSave);
		$http.post(mainURL + $scope.tableCBox.value, recordToSave)
		// $http.post(mainURL + $scope.tableCBox.value, $scope.recordSB)
			.then(function (response) {
				console.log(response);
				if (response.status == 201) {
					$scope.findAll();
					printResponseFromServer(response);
				}
			}).catch(function (error) {
				console.log(error);
				printErrorFromServer(error);
			})

	}

	$scope.undoSaveAction = function () {
		$scope.virtualTab.wasNewRecordAdded = false;
		// $scope.recordSB = ["*"];
		$scope.recordSB = { id: "*" };
		$scope.recordSBStyle = ["correct"];
	}
}]);

//zwróc datę w formacie yyyy-mm-dd z obiektu Date
const getFormatedDateIfDateObject = function (value) {
	if (value instanceof Date) {
		var monthNumber = value.getMonth() + 1;
		var dayNumber = value.getDate();
		return value.getFullYear() + "-" + ((monthNumber < 10) ? "0" + monthNumber : monthNumber) + "-" + ((dayNumber < 10) ? "0" + dayNumber : dayNumber);
	}
	return value;
}

const printErrorFromServer = function (error) {
	alert(error.data.errorMessage + "\n\t" + error.data.solutions);
}

const printResponseFromServer = function (response) {
	alert(response.data.message);
}