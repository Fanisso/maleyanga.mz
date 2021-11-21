<%@ page import="mz.maleyanga.credito.Credito" %>
<!DOCTYPE html>
<html>

<head>
    <meta name="layout" content="kickstart"/>
    <g:set var="entityName" value="${message(code: 'credito.label', default: 'Credito')}"/>
    <title><g:message code="Créditos" args="[entityName]"/></title>
</head>

<body>
<sec:ifNotGranted roles="CREDITO_SAVE">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('CREDITO_SAVE')">
    <section id="edit-credito" class="first">
        <z:body/>
    </section>
</sec:access>
<br/>
%{--<script>
    window.addEventListener("pageshow", function (event) {
        var historyTraversal = event.persisted ||
            (typeof window.performance != "undefined" &&
                window.performance.navigation.type === 2);
        if (historyTraversal) {
            // Handle page restore.
            window.location.reload();
        }
    });
</script>--}%
</body>

</html>
