

<!DOCTYPE html>
<html>

<head>
    <meta name="layout" content="kickstart" />
    <g:set var="entityName" value="${message(code: 'cliente.label', default: 'Cliente')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>

<body>
<sec:ifNotGranted roles="UTILITARIOS_SMS">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('UTILITARIOS_SMS')">
<section id="list-cliente" class="first">

    <g:form action="sendNexmo" class="form-horizontal" role="form"  >
        <g:render template="form"/>

        <div class="form-actions margin-top-medium">
            <g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.button.send.label', default: 'Enviar')}" />
            <button class="btn" type="reset"><g:message code="default.button.reset.label" default="Reset" /></button>
        </div>
    </g:form>

</section>
</sec:access>
</body>

</html>
