<%@ page import="mz.maleyanga.diario.Diario" %>
<!DOCTYPE html>
<html>

<head>
    <meta name="layout" content="kickstart"/>
    <g:set var="entityName" value="${message(code: 'diario.label', default: 'Diario')}"/>
    <title><g:message code="Diário" args="[entityName]"/></title>
</head>

<body>

<section id="create-diario" class="first">

    <sec:ifNotGranted roles="DIARIO_EDIT">
        <g:render template="/layouts/acessoNegado"/>
    </sec:ifNotGranted>
    <sec:access expression="hasRole('DIARIO_EDIT')">
        <section id="edit-credito" class="first">
            <z:body/>
        </section>
    </sec:access>
    <br/>
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

</section>

</body>

</html>
