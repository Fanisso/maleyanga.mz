<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 11/10/2015
  Time: 20:47
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="kickstart" />
    <title>Relatórios</title>
</head>

<body>
<sec:ifNotGranted roles="RELATORIOS_INDEX">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('RELATORIOS_INDEX')">
    <g:render template="/_menu/menurelatorios"/>
    <section>
        <br/>
        <br/>
        <br/>
        <br/>
        <br/>
        <br/>
        <br/>
        <br/>
        <br/>
        <br/>
        <br/>
        <br/>
        <br/>
        <br/>
        <br/>
        <br/>
    </section>

</sec:access>

</body>
</html>