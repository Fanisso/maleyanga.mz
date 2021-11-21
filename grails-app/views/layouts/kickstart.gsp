<!DOCTYPE html>
<%@ page contentType="text/html;charset=ISO-8859-1" %>
%{--<%-- <html lang="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).toString().replace('_', '-')}"> --%>--}%
<html lang="${session.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'}">
<g:set var="session" bean="sessionStorageService"/>
<head>
    <title><g:layoutTitle default="${meta(name: 'app.name')}"/></title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon"/>

    <r:require modules="bootstrap"/>
    <r:require modules="bootstrap_utils"/>
    <calendar:resources lang="en" theme="tiger"/>
    <r:layoutResources/>
    <g:layoutHead/>

    <style type="text/css">
    td {
        font: normal 16px courier !important;
    }

    label {
        font: normal 16px courier !important;
    }

    button {
        font-family: Calibri;
        font-size: medium;
    }


</style>
</head>

<body style="background-color:#222222">
<g:render template="/_menu/navbar"/>

<!-- Enable to overwrite Header by individual page -->
<g:if test="${pageProperty(name: 'page.header')}">
    <g:pageProperty name="page.header"/>
</g:if>
<g:else>
    <g:render template="/layouts/header"/>
</g:else>

<g:render template="/layouts/content"/>



<!-- Enable to insert additional components (e.g., modals, javascript, etc.) by any individual page -->
<g:if test="${pageProperty(name: 'page.include.bottom')}">
    <g:pageProperty name="page.include.bottom"/>
</g:if>
<g:else>
    <!-- Insert a modal dialog for registering (for an open site registering is possible on any page) -->
    <g:render template="/_common/modals/registerDialog" model="[item: item]"/>
</g:else>

<!-- Included Javascript files and other resources -->
<r:layoutResources/>
<!-- Enable to overwrite Footer by individual page -->

<g:if test="${pageProperty(name: 'page.footer')}">
    <g:pageProperty name="page.footer"/>
</g:if>
<g:else>
    <g:render template="/layouts/footer"/>
</g:else>

<script>
    $(document).ready(function(){
        var table =  $('.table');
        table.on('mouseenter','tr',function(){
            $(this).addClass('alert-danger')
        } );
        table.on('mouseleave','tr',function(){
            $(this).removeClass('alert-danger')
        } );
    });
</script>

</body>

</html>