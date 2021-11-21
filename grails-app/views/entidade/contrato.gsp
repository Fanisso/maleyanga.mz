<%@ page import="mz.maleyanga.entidade.Entidade" %>
<!DOCTYPE html>
<html>

<head>
  <meta name="layout" content="kickstart"/>
  <g:set var="entityName" value="${message(code: 'entidade.label', default: 'Entidade')}"/>
  <title><g:message code="default.index.label" args="[entityName]"/></title>
</head>

<body>
<sec:ifNotGranted roles="ENTIDADE_INDEX">
  <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('ENTIDADE_INDEX')">
  <section id="index-entidade" class="first">
    <z:body/>
  </section>
</sec:access>
</body>

</html>
