<%@ page import="mz.maleyanga.security.Utilizador" %>
<!DOCTYPE html>
<html>

<head>
    <meta name="layout" content="kickstart"/>
    <g:set var="entityName" value="${message(code: 'utilizador.label', default: 'Utilizador')}"/>
    <title><g:message code="default.index.label" args="[entityName]"/></title>
    <z:head/>
</head>

<body>
%{--<div class="">
	<ul class="nav nav-tabs" data-role="listview" data-split-icon="gear" data-filter="true">
		<g:link controller="roleGroup" action="roleGroup"><glyph:icon iconName="role"/> Definir Perfil do  <g:message code="default.new.label"  args="[entityName]"/></g:link>
	</ul>
</div>--}%
<sec:ifNotGranted roles="UTILIZADOR_UTILIZADOR_CRUD">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('UTILIZADOR_UTILIZADOR_CRUD')">
    <section id="index-utilizador" class="first">
        <z:body/>

    </section>
</sec:access>
</body>

</html>
