<%@ page import="mz.maleyanga.credito.Credito" %>
<html>

<head>
	<title><g:message code="default.welcome.title" args="[meta(name:'app.name')]"/> </title>
	<meta name="layout" content="kickstart" />


</head>

<body>

<section class="section-table" >

	<table class="table table-bordered margin-top-medium">
		<thead>
		<tr>

			<g:sortableColumn property="mes" title="${message(code: 'mes.label', default: 'Mês')}" />
			<g:sortableColumn property="saldoDevedor" title="${message(code: 'saldo.label', default: 'Saldo Devedor')}" />

			<g:sortableColumn property="prestacoes" title="${message(code: 'prestacoes.label', default: 'PrestaçÕes')}" />

			<g:sortableColumn property="Juros" title="${message(code: 'juros.label', default: 'Juros')}" />


		</tr>
		</thead>
		<tbody>

		</tbody>
	</table>

</section>


</body>

</html>
