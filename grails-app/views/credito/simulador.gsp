<%@ page import="mz.maleyanga.simulador.Simulador" %>
<!DOCTYPE html>
<html>

<head>
    <meta name="layout" content="kickstart"/>
    <g:set var="entityName" value="${message(code: 'simulador.label', default: 'Simulador')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>
<sec:ifNotGranted roles="CREDITO_CREATE_SIMULADOR">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('CREDITO_CREATE_SIMULADOR')">
    <section id="create-simulador" class="first">
        <div class="col-md-12">

            <div class="panel panel-primary">
                <p style="background: #00b3ee"/>

                <div class="panel-heading">
                    <h3 class="panel-title">Simulador de Crédito</h3>
                    <z:body/>
                </div>
            </div>
        </div>
    </section>

%{--<g:hasErrors bean="${simuladorInstance}">
<div class="alert alert-danger">
    <g:renderErrors bean="${simuladorInstance}" as="list" />
</div>
</g:hasErrors>

<g:form action="save" class="form-horizontal" role="form" >
    <g:render template="form"/>

    <div class="form-actions margin-top-medium">
        <g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" />
        <button class="btn" type="reset"><g:message code="default.button.reset.label" default="Reset" /></button>
    </div>
</g:form>--}%

    </section>
</sec:access>
</body>

</html>
