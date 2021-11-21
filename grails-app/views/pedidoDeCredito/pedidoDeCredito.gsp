<%@ page import="mz.maleyanga.pedidoDeCredito.PedidoDeCredito" %>
<!DOCTYPE html>
<html>

<head>
    <meta name="layout" content="kickstart"/>
    <g:set var="entityName" value="${message(code: 'pedidoDeCredito.label', default: 'Pedido De Credito')}"/>
    <title><g:message code="default.index.label" args="[entityName]"/></title>
</head>

<body>
<sec:ifNotGranted roles="PEDIDO_DE_CREDITO_LIST_INDEX">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('PEDIDO_DE_CREDITO_LIST_INDEX')">
%{--<g:render template="/_menu/menupedidodecredito"/>--}%
    <z:body/>
</sec:access>
</body>

</html>
