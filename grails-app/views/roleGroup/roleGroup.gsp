
<%@ page import="mz.maleyanga.security.Utilizador" %>
<!DOCTYPE html>
<html>

<head>
	<meta name="layout" content="kickstart" />
	<g:set var="entityName" value="${message(code: 'utilizador.label', default: 'Utilizador')}" />
	<title><g:message code="default.index.label" args="[entityName]" /></title>
	<z:head/>
</head>

<body>
<sec:ifNotGranted roles="ROLE_GROUP">
	<g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('ROLE_GROUP')">
%{--<div class="">
	<ul class="nav nav-tabs" data-role="listview" data-split-icon="gear" data-filter="true">
		<g:link controller="utilizador" action="utilizadorCrud"><glyph:icon iconName="role"/> Utilizadores <g:message code="default.new.label"  args="[entityName]"/></g:link>
	</ul>
</div>--}%
<section id="index-utilizador" class="first">
<z:body/>

</section>
</sec:access>
</body>

</html>
