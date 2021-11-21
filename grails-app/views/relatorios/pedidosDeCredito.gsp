<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 11/10/2015
  Time: 20:47
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="kickstart"/>
    <title>Relatórios-Pedidos de Crédito</title>

</head>

<body>
<g:render template="/_menu/menurelatorios"/>
<sec:ifNotGranted roles="RELATORIOS_IMPRIMIR_EXTRATO_DO_PEDIDO_DE_CREDITO">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('RELATORIOS_IMPRIMIR_EXTRATO_DO_PEDIDO_DE_CREDITO')">
</sec:access>
</body>
</html>