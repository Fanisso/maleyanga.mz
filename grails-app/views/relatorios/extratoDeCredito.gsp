<%@ page import="mz.maleyanga.credito.Credito" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="kickstart" />
    <title>Relatórios-Extrado de créditos</title>

</head>

<body>
<g:render template="/_menu/menurelatorios"/>
<sec:ifNotGranted roles="RELATORIOS_EXTRATO_DE_CREDITO">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('RELATORIOS_EXTRATO_DE_CREDITO')">
<g:form action="imprimirExtratoDeCredito" class="button-bar" role="form"  >
    <div class="panel-body">
        <div class="col-lg-2">
            <div class="${hasErrors(bean: creditoInstance, field: 'credito', 'error')} required">
                <label for="credito" class="control-label"><g:message code="pedidoDeCredito.credito.label" default="Credito" /><span class="required-indicator">*</span></label>
            </div>
        </div>
        <div class="col-lg-6">
            <g:select class="form-control" id="credito" name="credito.id" from="${mz.maleyanga.credito.Credito.list()}" optionKey="id" required="" value="${creditoInstance?.id}" class="many-to-one"/>
            <span class="help-inline">${hasErrors(bean: creditoInstance, field: 'credito', 'error')}</span>
        </div>

    </div>
    <div class="panel-body">
        <div class="col-lg-2">
            <div >
                <label class="control-label"><g:message code="cliente.dataDeExpiracao.label" default="Entensão" /><span class="required-indicator">*</span></label>
            </div>
        </div>
        <div class="col-lg-6">
            <g:select  name="ext" from="${["pdf","doc","docx","html","xls","xlsx","ppt"]}" />

        </div>

    </div>
    <div class="form-actions margin-top-medium">
        <g:submitButton name="create" class="btn btn-danger" value="Imprimir"/>
    </div>
</g:form>
</sec:access>
</body>
</html>