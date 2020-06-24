<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<title>Spring DB Project</title>

<link rel="stylesheet" href='<c:url value="/css/list.css"></c:url>' />
<script type="text/javascript"
	src='<c:url value="/js/example.js"></c:url>' charset="utf-8"></script>
</head>
<body onload="init()">
	<div style="text-align: center;">
		<h1>Baza danych gier konsolowych</h1>
	</div>
	<div>
		<table>
			<tr>
				<td>
					<select id="tableSelector" onchange="changeTable(this)">
							<option value="">Wybierz tabelę</option>
							<option value="AgeCategory">Kategorie wiekowe</option>
							<option value="ConsoleGame">Gry konsolowe</option>
							<option value="GameplayMode">Tryby gry</option>
							<option value="Genre">Gatunki</option>
							<option value="HardwarePlatform">Platformy sprzętowe</option>
							<option value="Language">Języki</option>
							<option value="Producer">Producenci</option>
							<option value="Publisher">Wydawcy</option>
					</select>
				</td>
				<td id="load" style="display: none;">
					<div id="loader"></div>
					<p class="p">Operacja w toku...</p>
				</td>
				<td>
				<input id="addRecord" type="button" value="Add Record"
					style="display: none;">
				</td>
				<td>
					<p id="field1" class="p">Wybrana tabela:</p>
				</td>
			</tr>
		</table>

		<table id="mainTable"></table>
	</div>
</body>
</html>