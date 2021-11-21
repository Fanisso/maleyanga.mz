<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 11/10/2015
  Time: 20:47
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="kickstart" />
    <title>Papgamentos por receber</title>

</head>

<body>
<g:render template="/_menu/menurelatorios"/>
<sec:ifNotGranted roles="RELATORIOS_EXTRATO_PRESTACOES_EM_DIVIDA">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('RELATORIOS_EXTRATO_PRESTACOES_EM_DIVIDA')">
<section>
    <g:form action="imprimirPagamentosPorReceber" class="button-bar" role="form"  >
        <div class="panel-body">


        <div class="panel-body">
            <div class="col-lg-2">
                <div >
                    <label class="control-label"><g:message code="cliente.dataDeExpiracao.label" default="De" /><span class="required-indicator">*</span></label>
            </div>
        </div>
        <div class="col-lg-6">
            <calendar:datePicker  name="inicio" precision="day"  defaultValue="${new Date()}"  />
        </div>

        </div>
        <div class="panel-body">
            <div class="col-lg-2">
                <div >
                    <label class="control-label"><g:message code="cliente.dataDeExpiracao.label" default="A" /><span class="required-indicator">*</span></label>
                </div>
            </div>
            <div class="col-lg-6">
                <calendar:datePicker  name="fim" precision="day"  defaultValue="${new Date()+1}"  />
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
</section>
</sec:access>
</body>
</html>