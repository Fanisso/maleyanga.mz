<%@ page import="mz.maleyanga.cliente.Cliente" %>
<!DOCTYPE html>
<html>

<head>
    <meta name="layout" content="kickstart"/>
    <g:set var="entityName" value="${message(code: 'relatorios.cliente.label', default: 'Relatórios/Clientes')}"/>
    <title><g:message code="default.index.label" args="[entityName]"/></title>
</head>

<body>
<g:render template="/_menu/menurelatorios"/>
<sec:ifNotGranted roles="CLIENTE_INDEX">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('CLIENTE_INDEX')">
%{--   <g:render template="/_menu/menuclientes"/>--}%
    <z:body/>
</sec:access>

</body>

</html>
