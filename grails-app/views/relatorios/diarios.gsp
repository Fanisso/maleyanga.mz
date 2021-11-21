<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 11/05/2016
  Time: 05:26
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="kickstart"/>
    <title>Relatórios-Diários</title>
</head>

<body>

<g:render template="/_menu/menurelatorios"/>
<sec:ifNotGranted roles="RELATORIOS_IMPRIMIR_PRESTACAO">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('RELATORIOS_IMPRIMIR_PRESTACAO')">
    <z:body/>
</sec:access>

</body>
</html>