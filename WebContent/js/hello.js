var myApp = angular.module('myApp', []); //utworzenie modułu o danej nazwie

//stałe
const mainURL = "http://localhost:8080/springDBProject/mainPage/";
const ENTER_KEYCODE = 13;
const ESC_KEYCODE = 27;

const HTTP_OK_CODE = 200;
const HTTP_CREATED_CODE = 201;
const HTTP_NOT_FOUND = 404;

const GET = "GET";
const PUT = "PUT";
const POST = "POST";
const DELETE = "DELETE";

myApp.controller('myAppController', ['$scope', '$http', function ($scope, $http) {
	$scope.loading = false;

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

	//R-Receive-findAll,findById
	$scope.virtualTab;
	$scope.findAll = function () {
		if ($scope.tableCBox != $scope.tables[0]) {
			// $scope.recordSB = { id: "*" };
			$scope.undoSave();
			$scope.loading = true;
			httpRequestTemplate($http, GET, mainURL + $scope.tableCBox.value, "",
				function (response) {
					// console.log(response.data);
					$scope.loading = false;

					if (response.data.length > 0) {
						$scope.virtualTab = new VirtualTable(response, false);
						forRange($scope.virtualTab.foreignColumns.length, i => {
							$scope.loading = true;
							httpRequestTemplate($http, GET, mainURL + $scope.virtualTab.foreignColumns[i]
								, "", function (response) {
									$scope.loading = false;
									$scope.virtualTab.addForeignRecordsForColumn(response, i);
								}, function (error) {
									$scope.loading = false;
									printErrorFromServer(error);
								});
						});
					} else {
						alert("Table " + $scope.tableCBox.name + " is empty!");
					}
				}, function (error) {
					$scope.loading = false;
					printErrorFromServer(error);
				});
		}
	}

	$scope.idInputStyle = "error";
	$scope.idFieldChanged = function () { $scope.idInputStyle = ($scope.id > 0) ? "correct" : "error"; }
	$scope.findById = function () {
		if ($scope.tableCBox != $scope.tables[0] && $scope.idInputStyle == "correct") {
			$scope.loading = true;
			httpRequestTemplate($http, GET, mainURL + $scope.tableCBox.value + "/" + $scope.id, "",
				function (response) {
					// console.log(response.data);
					$scope.loading = false;
					if (response.status != HTTP_NOT_FOUND) {
						$scope.virtualTab = new VirtualTable(response, true);
						forRange($scope.virtualTab.foreignColumns.length, i => {
							$scope.loading = true;
							httpRequestTemplate($http, GET, mainURL + $scope.virtualTab.foreignColumns[i]
								, "", function (response) {
									$scope.loading = false;
									$scope.virtualTab.addForeignRecordsForColumn(response, i);
								}, function (error) {
									$scope.loading = false;
									printErrorFromServer(error);
								});
						});
					}
				}, function (error) {
					$scope.loading = false;
					printErrorFromServer(error);
				});
		} else if ($scope.tableCBox == $scope.tables[0]) {
			alert("Table was't chosen!");
		} else if ($scope.id < 1 || $scope.id == undefined) {
			alert("Id value must be greater than 0, but is " + $scope.id + "!");
		}
	}


	//U-Update-update
	$scope.updateInputValue = { value: "" };
	$scope.update = function () {
		const previousValue = $scope.virtualTab.getUpdatedCellValue();
		const newValue = getFormatedDateIfDateObject($scope.updateInputValue.value);

		if (newValue.length > 0 || Object.keys(newValue).length > 0) {
			$scope.virtualTab.setUpdatedCellValue(newValue);
			const updatedRecord = $scope.virtualTab.getUpdatedRecord();
			$scope.loading = true;
			httpRequestTemplate($http, PUT,
				mainURL + $scope.tableCBox.value + "/" + updatedRecord["id"], updatedRecord
				, function (response) {
					// console.log(response);
					$scope.loading = false;
					$scope.updateInputValue.value = "";
					$scope.virtualTab.removeUpdatingUIElement();
					if (response.status == HTTP_OK_CODE) {
						printResponseFromServer(response);
					} else {
						$scope.virtualTab.setUpdatedCellValue(previousValue);
					}
				}, function (error) {
					// console.log(error);
					$scope.loading = false;
					$scope.updateInputValue.value = "";
					$scope.virtualTab.setUpdatedCellValue(previousValue);
					printErrorFromServer(error);
				});
		} else {
			alert("Updated field is empty or no item was chosen from combo box!");
		}
	}

	$scope.undoUpdate = function () {
		$scope.virtualTab.removeUpdatingUIElement();
		$scope.updateInputValue.value = "";
	}

	//C-Create-save
	//inicjowane wartościami dla kolumny id o wymuszonej zawartości *
	$scope.recordSB = { id: "*" };
	$scope.recordSBStyle = ["correct"];

	$scope.addRecordHandler = function () {
		if ($scope.virtualTab != undefined && $scope.virtualTab.isNewRecordAdded()) {
			alert("One record was already added! Fill values and save record to DB!");
			return;
		} else {

		}
		if ($scope.tableCBox != $scope.tables[0]) {
			forRange($scope.virtualTab.columnCount, i => {
				if (i > 0) {	//pomijamy kolumnę id => wymuszono correct
					if ($scope.virtualTab.columns[i] == "dateOfRelease") {
						//początkowa inicjalizacja aktualną datą
						$scope.recordSB["dateOfRelease"] = new Date();
						$scope.recordSBStyle[i] = "correct";
					} else
						$scope.recordSBStyle[i] = "error";
				}
			});
			$scope.virtualTab.setNewRecordAdded(true);
		} else {
			alert("No table was chosen!");
		}
	}

	$scope.inputChanged = function ($index, column, isForeign) {
		$scope.recordSBStyle[$index] =
			($scope.virtualTab.doesInputCorrect(column,
				getFormatedDateIfDateObject($scope.recordSB[column])
				, isForeign)) ? "correct" : "error";
	}

	$scope.save = function () {
		if (Object.keys($scope.recordSB).length != $scope.virtualTab.columnCount) {
			alert("Not all fields where set!");
			return;
		}
		if (isErrorStyleInArray($scope.recordSBStyle)) {
			alert("At least one field has invalid value!");
			return;
		}

		// console.log($scope.recordSB);
		var recordToSave = getFormattedCopyOf($scope.recordSB, $scope.virtualTab.columns);
		// console.log(recordToSave);

		$scope.loading = true;
		httpRequestTemplate($http, POST, mainURL + $scope.tableCBox.value, recordToSave
			, function (response) {
				// console.log(response);
				$scope.loading = false;
				if (response.status == HTTP_CREATED_CODE) {
					printResponseFromServer(response);
					$scope.findAll();
				}
			}, function (error) {
				// console.log(error);
				$scope.loading = false;
				printErrorFromServer(error);
			});
	}

	$scope.undoSave = function () {
		if ($scope.virtualTab != undefined) {
			$scope.virtualTab.setNewRecordAdded(false);
			$scope.recordSB = { id: "*" };
			$scope.recordSBStyle = ["correct"];
		}
	}

	//D-Delete-deleteById
	$scope.deleteById = function () {
		if ($scope.tableCBox != $scope.tables[0] && $scope.idInputStyle == "correct") {
			$scope.loading = true;
			httpRequestTemplate($http, DELETE, mainURL + $scope.tableCBox.value + "/" + $scope.id, "",
				function (response) {
					if (response.status == HTTP_OK_CODE) {
						$scope.loading = false;
						$scope.findAll();
						printResponseFromServer(response);
					}
				},
				function (error) {
					$scope.loading = false;
					printErrorFromServer(error);
				});
		} else if ($scope.tableCBox == $scope.tables[0]) {
			alert("Table was't chosen!");
		} else if ($scope.id < 1 || $scope.id == undefined) {
			alert("Id value must be greater than 0, but is " + $scope.id + "!");
		}
	}
}]);

const httpRequestTemplate = function ($http, methodType, requestURL, requestBody, successCallback, errorCallback) {
	$http({
		method: methodType,
		url: requestURL,
		data: requestBody
	}).then(successCallback, errorCallback);
}

const isErrorStyleInArray = function (stylesArray) {
	var result = false;
	for (var i = 0; i < stylesArray.length; i++) {
		if (stylesArray[i] == "error")
			return true;
	}
	return result;
}

const getFormattedCopyOf = function (item, itemColumns) {
	var recordToSave = {};
	forRange(itemColumns.length, i => {
		recordToSave[itemColumns[i]] = getFormatedDateIfDateObject(item[itemColumns[i]]);
	});
	return recordToSave;
}

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
const printResponseFromServer = function (response) { alert(response.data.message); }