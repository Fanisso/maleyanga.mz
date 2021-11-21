<%@ page import="mz.maleyanga.feriado.Feriado" %>
<!DOCTYPE html>
<html>

<head>
    <meta name="layout" content="kickstart"/>
    <g:set var="entityName" value="${message(code: 'feriados.label', default: 'Feriados')}"/>
    <title><g:message code="default.index.label" args="[entityName]"/></title>

</head>

<body>
<sec:ifNotGranted roles="FERIADO_FERIADO_CRUD">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('FERIADO_FERIADO_CRUD')">
    <section id="index-feriados" class="first">

        <section id="index-feraido" class="first">

            <z:body/>

        </section>
    </section>
</sec:access>
</body>

</html>
