<%@ page import="mz.maleyanga.Taxa.Taxa" %>
<!DOCTYPE html>
<html>

<head>
	<meta name="layout" content="kickstart" />
	<g:set var="entityName" value="${message(code: 'taxa.label', default: 'Taxa')}" />
	<title><g:message code="default.create.label" args="[entityName]" /></title>
</head>

<body>
<sec:ifNotGranted roles="TAXA_CREATE">
	<g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('TAXA_CREATE')">
    <section title="Taxas" id="create-taxa" class="first">


    <z:body/>

</sec:access>
</body>

</html>
