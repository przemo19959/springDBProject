class BooleanTable {
	constructor(response) {
		this.records = response.data;
		this.columns = Object.keys(response.data[0]);
		this.rowCount = this.records.length;
		this.columnCount = this.columns.length;

		this.currentRowIndex = -1;
		this.currentColumnIndex = -1;

		var result = [];
		for (var i = 0; i < this.rowCount; i++) {
			result[i] = [];
		}
		for (var i = 0; i < this.rowCount; i++) {
			for (var j = 0; j < this.columnCount; j++) {
				result[i][j] = false;
			}
		}
		this.table = result;
	}

	getUpdatedRecord() {
		if (this.currentRowIndex != -1) {
			return this.records[this.currentRowIndex];
		}
		return null;
	}

	setRecordValue(value) {
		this.records[this.currentRowIndex][this.columns[this.currentColumnIndex]] = value;
	}

	removeUpdatingElement(){
		this.table[this.currentRowIndex][this.currentColumnIndex] = false;
	}

	setValue(record, column, value) {
		if (this.isOneAlreadyUpdated()) {
			alert("One is already updated! Update one first, then next one.");
			return;
		}

		for (var i = 0; i < this.rowCount; i++) {
			if (this.records[i] == record) {
				this.currentRowIndex = i;
			}
		}

		for (var i = 0; i < this.columnCount; i++) {
			if (this.columns[i] == column) {
				this.currentColumnIndex = i;
			}
		}
		this.table[this.currentRowIndex][this.currentColumnIndex] = value;
	}

	getValue(record, column) {
		var rowIndex = -1;
		for (var i = 0; i < this.rowCount; i++) {
			if (this.records[i] == record) {
				rowIndex = i;
			}
		}

		var columnIndex = -1;
		for (var i = 0; i < this.columnCount; i++) {
			if (this.columns[i] == column) {
				columnIndex = i;
			}
		}
		return this.table[rowIndex][columnIndex];
	}

	isOneAlreadyUpdated() {
		for (var i = 0; i < this.rowCount; i++) {
			for (var j = 0; j < this.columnCount; j++) {
				if (this.table[i][j] == true) {
					return true;
				}
			}
		}
		return false;
	}

	printTable() {
		for (var i = 0; i < this.rowCount; i++) {
			for (var j = 0; j < this.columnCount; j++) {
				console.log(this.table[i][j]);
			}
		}
	}
}