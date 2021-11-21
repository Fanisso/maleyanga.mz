<%@ page import="mz.maleyanga.cliente.Cliente" %>
<!DOCTYPE html>
<html>

<head>
    <meta name="layout" content="kickstart"/>
    <g:set var="entityName" value="${message(code: 'cliente.label', default: 'Cliente')}"/>
    <title><g:message code="default.index.label" args="[entityName]"/></title>
</head>

<body>
<sec:ifNotGranted roles="CLIENTE_INDEX">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('CLIENTE_INDEX')">

    <z:body/>
</sec:access>
<script>
    window.addEventListener("pageshow", function (event) {
        var historyTraversal = event.persisted ||
            (typeof window.performance != "undefined" &&
                window.performance.navigation.type === 2);
        if (historyTraversal) {
            // Handle page restore.
            window.location.reload();
        }
    });
</script>
</body>

</html>
