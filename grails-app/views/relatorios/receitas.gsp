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
    <title>Relatórios-Receitas</title>

</head>

<body>

<sec:ifNotGranted roles="RELATORIOS_RECEITAS">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('RELATORIOS_RECEITAS')">
    <g:render template="/_menu/menurelatorios"/>
<section>


</section>
</sec:access>

</body>
</html>