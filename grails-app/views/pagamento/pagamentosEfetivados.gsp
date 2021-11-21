<%@ page import="mz.maleyanga.pagamento.Pagamento" %>
<!DOCTYPE html>
<html>

<head>
    <meta name="layout" content="kickstart"/>
    <g:set var="entityName" value="${message(code: 'pagamento.label', default: 'Pagamento')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<sec:ifNotGranted roles="PAGAMENTO_PAGAMENTOS_EFECTIVADOS">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('PAGAMENTO_PAGAMENTOS_EFECTIVADOS')">
    <section id="list-pagamento" class="first">

        <z:body/>
    </section>
</sec:access>
</body>

</html>
