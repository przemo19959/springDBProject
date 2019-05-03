let projectPath="/springDBProject/list/";

let wordPattern="\\w\+";

let tableSelector;
let loadingCircle;
let mainTable;
let addRecordButton;

let tableInXML;

//Początkowa metoda inicjalizacyjna, uruchamiana, gdy ciało <body> strony zostanie załadowane. W celu
//inicjalizacji referencji do globalnych elementów HTML.
function init(){
	tableSelector=document.getElementById("tableSelector");
	loadingCircle=document.getElementById("load");
	mainTable=document.getElementById("mainTable");
	addRecordButton=document.getElementById("addRecord");
	addRecordButton.addEventListener("click",addRecordFuntion); //dodanie obsługi zdarzenia kliknięcia
	console.log("Init finished!");
}

//funkcja dodająca formatkę do dodania nowego rekordu w tabeli
function addRecordFuntion(){
	let lastRow=mainTable.firstChild.lastChild;
	let newRow=document.createElement("tr"); //nowy wiersz
	let newCell;
	for(i=0;i<lastRow.getElementsByTagName("td").length;i++){
		newCell=document.createElement("td");
		newCell.setAttribute("class","data");
		if(i==0)
			newCell.textContent=1+parseInt(lastRow.firstChild.textContent,10);
		newRow.appendChild(newCell); //dodaj komórki do nowego wiersza
	}
	let acceptButton=document.createElement("input");
	setAttributes(acceptButton,{"type":"button","value":"Accept"});
	newCell=document.createElement("td");
	newCell.appendChild(acceptButton);
	newRow.appendChild(newCell);
	// newRow.appendChild(acceptButton); //dodaj przycisk do dodatkowej komórki

	let cancelButton=document.createElement("input");
	setAttributes(cancelButton,{"type":"button","value":"Cancel"});
	cancelButton.addEventListener("click",removeRow);
	// newRow.appendChild(cancelButton);
	newCell=document.createElement("td");
	newCell.appendChild(cancelButton);
	newRow.appendChild(newCell);

	mainTable.firstChild.appendChild(newRow);
	
	let cells=newRow.getElementsByTagName("td");
	for(i=2;i<cells.length;i++){
		cells[i].ondblclick=doubleClickCellFunction(this);
	}
}

//funkcja pomocnicza ustawiająca kilka atrybutów na podstawie tabeli
function setAttributes(el, attrs) {
	for(var key in attrs) {
	  el.setAttribute(key, attrs[key]);
	}
}

//funkcja usuwająca ostatni wiersz 
function removeRow(){
	let lastRow=mainTable.firstChild.lastChild;
	mainTable.firstChild.removeChild(lastRow);
}

//Ta metoda jest wyzwalana, gdy wartość selektora <select> ulegnie zmianie.
function changeTable(selector) {
	let selectedValue = selector.options[selector.selectedIndex].value;
	if(selectedValue!=""){
		showLoaderHideSelector();
		getRequest(projectPath + selectedValue,printTable,""); //wyślij zapytanie do serwera, a ten do DB zapytanie SELECT * FROM
	}
}

//Ta metoda dodaje ciało tabeli, które otrzymała w postaci pliku XML od serwera w wyniku zapytania SQL SELECT * FROM.
function printTable(responseObj){
	mainTable.innerHTML="";
	tableInXML=responseObj.responseXML; //pobierz ciało odpowiedzi w postaci XML
	let record = tableInXML.getElementsByTagName("record");
	let columns;

	let newRow;
	let idOfRecord;
	let newCell;
	for (i = 0; i <record.length; i++) { //dla wszystkich znaczników <record> w XML
		columns=record[i].getElementsByTagName("column");
		idOfRecord=columns[1].textContent; //pobierz id rekordu

		newRow=document.createElement("tr");
		if(i!=0)
			newRow.setAttribute("id",idOfRecord);
			
		for(j=0;j<columns.length;j++){ //dla wszystkich znaczników <column> w danym <record>
			if(i==0){ //dla pierwszego wiersza z nagłówkami
				newCell=document.createElement("td");
				newCell.setAttribute("class","header");
				newCell.textContent=columns[j].textContent;
				newRow.appendChild(newCell);
			}else if(j==0 || j==1){ //dla pierwszej kolumny z Lp. oraz kolumny id
				newCell=document.createElement("td");
				newCell.setAttribute("class","data");
				newCell.textContent=columns[j].textContent;
				newRow.appendChild(newCell);
			}else{
				let newCell=document.createElement("td");
				let rowIndex=i;
				newCell.setAttribute("class","data");
				newCell.textContent=columns[j].textContent;
				newRow.appendChild(newCell);
				newCell.addEventListener("dblclick",function(){doubleClickCellFunction(newCell,rowIndex)});
			}
		}
		mainTable.appendChild(newRow);
	}
	addRecordButton.style.display="block";
//	console.log("Button: "+addRecordButton.style.display);

	let field = document.getElementById("field1");
	field.textContent="Wybrana tabela: "+tableSelector.options[tableSelector.selectedIndex].text;

	hideLoaderShowSelector();
}

//funkcja wysyłająca dane data w zapytaniu POST pod wskazany adres
function getRequest(url, cFunction, data) {
	let xhttp = new XMLHttpRequest();
	xhttp.onload = function() {
	  if (this.status == 200)
		  cFunction(this);
	};
	xhttp.open("GET", url, true);
	xhttp.send(data);
}

//funkcja wysyłająca dane data w zapytaniu POST pod wskazany adres
function postRequest(url, cFunction,data) { 
	let xhttp = new XMLHttpRequest();
	xhttp.onload = function() {
		if (this.status == 200)
			cFunction(this);
	};
	xhttp.open("POST", url, true);
	xhttp.send(data);
}

//Ta metoda jest wyzwalana, gdy komórka danych w tablicy zostanie naciśnięta dwu-krotnie. Dodaje ona pole tekstowe (input type-text)
//w miejsce klikniętej komórki. 
function doubleClickCellFunction(cell,rowIndex){
	let textField=document.createElement("input");
	setAttributes(textField,{"type":"text","class":"none"});
	textField.addEventListener("input",function(){validateWord(textField,wordPattern);});
	cell.textContent="";
	cell.appendChild(textField);
	cell.setAttribute("class","hoverOff"); //wyłącz podświetlanie komórki
	let row=cell.parentNode;
	if(!isButtonInLastCell(row)){ //dodaj przycisk, jeśli taki nie istnieje dla danego wiersza
		let newCell=document.createElement("td");
		let acceptButton=document.createElement("input");
		setAttributes(acceptButton,{"type":"button","value":"Accept"});
		acceptButton.addEventListener("click",update);
		newCell.appendChild(acceptButton);

		let cancelButton=document.createElement("input");
		setAttributes(cancelButton,{"type":"button","value":"Cancel"});
		cancelButton.addEventListener("click",function(){cancelUpdate(newCell,rowIndex)});
		newCell.appendChild(cancelButton);

		row.appendChild(newCell);
	}
}

//ta metoda jest wywoływana, gdy wciśnięty zostanie przycisk anulujący edycję rekordu.
//ma za zadanie wszystkie zamienione pola tekstowe (input-text) przywrócić do domyślnych wartości
function cancelUpdate(cell,rowIndex){
	let record=tableInXML.getElementsByTagName("record");
	let columns=record[rowIndex].getElementsByTagName("column");
	for(i=0;i<columns.length;i++){ //dla wszystkich kolumn z danego wiersza
		let cell=mainTable.getElementsByTagName("tr")[rowIndex].getElementsByTagName("td")[i];
		if(cell.getElementsByTagName("input").length>0){
			cell.setAttribute("class","data");
			cell.addEventListener("dblclick",function(){doubleClickCellFunction(cell,rowIndex)});
			cell.innerHTML=columns[i].textContent;
		}
	}
	mainTable.getElementsByTagName("tr")[rowIndex].removeChild(cell);
}

//Ta metoda sprawdza, czy wskazany wierz posiada w ostatniej komórce przycisk 
function isButtonInLastCell(row) {
	let lastCellElements=row.lastChild.getElementsByTagName("input");
	for(i=0;i<lastCellElements.length;i++){
		if(lastCellElements[i].getAttribute("type")=="button")
			return true;
	}
	return false;
}

//Metoda przeprowadzająca aktualizację rekordu. Pole tekstowe znajduje się w węźle <td>, który znajduje się
//w weźle <tr>. 
function update() {
	let row=this.parentNode.parentNode; //pobierz wiersz tabeli
	let cells=row.getElementsByTagName("td"); //pobierz wszystkie komórki wiersza
	let queryString="";
	let value="";
	for (let i = 1; i < cells.length-1; i++) { //pomiń pierwszą(Lp.) i ostanią komórkę(input type=button)
		value=getInputValueIfExists(cells[i]);
		if(value==""){
			console.log("Empty fields or not matching pattern!");
			return; //jeśli któreś pole jest puste
		}else
			queryString+=value+"&"; //dodaj wartość z pól wiersza do zapytania
	}
	queryString=queryString.substring(0,queryString.length-1); //usuń ostatni znak &

	console.log("Zapytanie: "+queryString);
	let selectedValue = tableSelector.options[tableSelector.selectedIndex].value;
	if(selectedValue!=""){
		showLoaderHideSelector();
		postRequest(projectPath + selectedValue,updateCallbackFunction,queryString); //wyślij zapytanie POST do aktualizacji
		console.log("POST Sended!");
	}
}

//funkcja ukrywająca selektor tabel a pokazująca pasek ładowania
function showLoaderHideSelector(){
	loadingCircle.style.display="block";
	tableSelector.style.display="none";
}

//funkcja ukrywająca pasek ładowania a pokazująca selektor tabel
function hideLoaderShowSelector(){
	loadingCircle.style.display="none";
	tableSelector.style.display="block";
}

//Funkcja obsługi po otrzymaniu odpowiedzi od serwera po żądaniu POST aktualizacji
function updateCallbackFunction(response){
	let responseString=response.responseText;
	hideLoaderShowSelector();
	if(responseString.match("Successful update on record id:\\d\+")!=null){ //poprawna odpowiedź

		let updatedRow=document.getElementById(responseString.substring(responseString.indexOf(":")+1)); //pobierz zaktualizowany wiersz
		let cells=updatedRow.getElementsByTagName("td"); //wszystkie komórki tego wiersza
		for(i=0;i<cells.length-1;i++){ //bez ostatniej komórki z przyciskiem
			if(cells[i].getElementsByTagName("input").length>0){
				cells[i].setAttribute("class","data");
				cells[i].addEventListener("dblclick",function(){doubleClickCellFunction(cells[i],rowIndex)});
				cells[i].innerHTML=cells[i].firstChild.value; //jeśli w komórce jest pole tekstowe pobierz wartość
			}
		}
		updatedRow.removeChild(cells[cells.length-1]); //usuń ostatnią komórkę z przyciskiem

		console.log("Update Success!");
	}else if(responseString.match("Error while updating:.*")!=null){
		console.log(responseString);
	}else{
		console.log("Wrong response!"+ responseString);
	}
}

//Ta metoda pobiera wartość tekstową komórki, zarówno gdy jest to czysty tekst, jak i wartość value
//w polu tekstowym (input type=text). Zakłada się, że komórka zawiera jeden element tekstowy lub zwykłe pole tekstu.
function getInputValueIfExists(cell){
	let inputs=cell.getElementsByTagName("input");
	for(i=0;i<inputs.length;i++){
		if(inputs[i].getAttribute("class")=="error") //jeśli pole nie spełnia wzorca
			return "";
		if(inputs[i].getAttribute("type")=="text")
			return inputs[i].value; //jeśli komórka zawiera edytowalne pole tekstowe
	}
	return cell.textContent; //jeśli brak pola tesktowego to zwróć czysty tekst
}

//ta metoda sprawdza, czy dane pole tekstowe (input z type=text) spełnia wskazany wzorzec i w zależności
//od tego czy pole jest puste czy spełnia wzorzec lub nie zmienia klasę elementu, klasa zawiera styl
function validateWord(textField,pattern){
	let content=textField.value; //pobierz wartość pola tekstowego
	let result=content.match(pattern);
	textField.className=(content!="")?((result==null)?"error":"valid"):"none";
}
