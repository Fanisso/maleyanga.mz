<!DOCTYPE html>
<html>

<head>

    <g:set var="entityName" value="${message(code: 'pagamento.label', default: 'Pagamento')}"/>
    <title>Prestações do dia ${new Date()}</title>
    <z:head/>
</head>

<body>
<sec:ifNotGranted roles="PARCELA_EDIT">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('PARCELA_EDIT')">
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
