<%@ page import="mz.maleyanga.settings.Settings" %>
<!DOCTYPE html>
<html>

<head>
    <meta name="layout" content="kickstart"/>
    <g:set var="entityName" value="${message(code: 'settings.label', default: 'Settings')}"/>
    <title><g:message code="default.index.label" args="[entityName]"/></title>
    <z:head/>
</head>

<body>
<sec:ifNotGranted roles="SETTINGS_INDEX">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('SETTINGS_INDEX')">

    <section id="index-settings" class="first">
        <z:body/>

    </section>
</sec:access>
</body>

</html>
