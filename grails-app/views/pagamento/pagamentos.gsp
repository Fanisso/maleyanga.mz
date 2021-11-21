<%@ page import="mz.maleyanga.pagamento.Pagamento" %>
<!DOCTYPE html>
<html>

<head>
    <meta name="layout" content="kickstart"/>
    <g:set var="entityName" value="${message(code: 'pagamento.label', default: 'Pagamento')}"/>
    <title><g:message code="default.index.label" args="[entityName]"/></title>
    <z:head/>
</head>

<body>
<sec:ifNotGranted roles="PAGAMENTO_PAGAMENTOS">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('PAGAMENTO_PAGAMENTOS')">
<section id="index-pagamento" class="first">
    <z:body/>
</section>
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
