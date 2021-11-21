<%@ page import="mz.maleyanga.conta.Conta" %>
<!DOCTYPE html>
<html>

<head>
    <meta name="layout" content="kickstart"/>
    <g:set var="entityName" value="${message(code: 'conta.label', default: 'Conta')}"/>
    <title><g:message code="default.index.label" args="[entityName]"/></title>
    <z:head/>
</head>

<body>
<sec:ifNotGranted roles="CONTA_CONTAS">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('CONTA_CONTAS')">
<section id="index-conta" class="first">
    <z:body/>
</section>
</sec:access>
</body>

</html>
