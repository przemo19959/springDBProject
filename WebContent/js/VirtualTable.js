/**
 * This class represents virtual table.
 */
class VirtualTable {
	constructor(response, findById) {
		if (findById) {
			this.records = [];
			this.records[0] = response.data;
		} else {
			this.records = response.data; //otrzymane rekordy
		}

		this.columns = Object.keys(this.records[0]); //nazwy kolumn rekordów
		this.foreignColumns = [];
		this.foreignRecordsForEachForeignColumn = [];
		forRange(this.columns.length, i => {
			var keys = Object.keys(this.records[0][this.columns[i]]);
			if (keys.length > 0 && typeof this.records[0][this.columns[i]] != 'string') {
				this.foreignColumns.push(this.columns[i][0].toUpperCase() + this.columns[i].slice(1));
			}
		});
		this.rowCount = this.records.length;
		this.columnCount = this.columns.length;

		//aktualnie aktualizowany rekord
		this.currentRowIndex = -1;
		this.currentColumnIndex = -1;

		this.table = makeTwoDimArray(this.rowCount, this.columnCount, false);
	}

	getForeignColumnRecords(column) {
		var records=this.foreignRecordsForEachForeignColumn[this.getIndexOfForeignColumnIfExists(column)];
		return records;
	}

	getIndexOfForeignColumnIfExists(column) {
		var result = -1;
		if (column != undefined) {
			var firstUpperCaseLetterColumn = column.charAt(0).toUpperCase() + column.slice(1);
			forRange(this.foreignColumns.length, i => {
				if (firstUpperCaseLetterColumn == this.foreignColumns[i]) {
					result = i;
				}
			});
		}
		return result;
	}

	addForeignRecordsForColumn(response, arrayIndex) {
		this.foreignRecordsForEachForeignColumn[arrayIndex]=(response.data);
	}

	getUpdatedRecord() {
		return (this.currentRowIndex != -1) ? this.records[this.currentRowIndex] : null;
	}

	getUpdatedColumnName() {
		return (this.currentColumnIndex != -1) ? this.columns[this.currentColumnIndex] : null;
	}

	setUpdatedFieldValue(value) {
		// console.log(value);
		this.records[this.currentRowIndex][this.columns[this.currentColumnIndex]] = value;
	}

	removeUpdatingUIElement() {
		this.table[this.currentRowIndex][this.currentColumnIndex] = false;
	}

	setTableValue(record, column, value) {
		if (this.isOneAlreadyUpdated()) {
			alert("One is already updated! Update one first, then next one.");
			return;
		}
		forRange(this.rowCount, i => { if (this.records[i] == record) this.currentRowIndex = i; });
		forRange(this.columnCount, i => { if (this.columns[i] == column) this.currentColumnIndex = i; });
		this.table[this.currentRowIndex][this.currentColumnIndex] = value;
	}

	getTableValue(record, column) {
		var rowIndex = -1;
		for (var i = 0; i < this.rowCount; i++) {
			if (this.records[i] == record) {
				rowIndex = i;
				break;
			}
		}

		var columnIndex = -1;
		for (var i = 0; i < this.columnCount; i++) {
			if (this.columns[i] == column) {
				columnIndex = i;
				break;
			}
		}

		return this.table[rowIndex][columnIndex];
	}

	isEditedNormalColumn(record, column) {
		return (this.getTableValue(record, column) && this.getIndexOfForeignColumnIfExists(column) == -1) ? true : false;
	}

	isEditedForeignColumn(record, column) {
		return (this.getTableValue(record, column) && this.getIndexOfForeignColumnIfExists(column) != -1) ? true : false;
	}

	isOneAlreadyUpdated() {
		for(var i=0;i<this.rowCount;i++){
			for(var j=0;j<this.columnCount;j++){
				if (this.table[i][j])
					return true;
			}
		}
		return false;
	}

	printTable() {
		forRange(this.rowCount, i => {
			forRange(this.columnCount, j => {
				console.log(this.table[i][j]);
			});
		});
	}
}

const forRange = function (to, body) {
	for (var i = 0; i < to; i++) {
		body(i);
	}
}

const makeTwoDimArray = function (rowCount, columnCount, initialValue) {
	var result = [];
	forRange(rowCount, i => { result[i] = []; });
	forRange(rowCount, i => { forRange(columnCount, j => { result[i][j] = initialValue; }); });
	return result;
}