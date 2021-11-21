<%@ page import="mz.maleyanga.credito.Credito" %>
<html>

<head>
	<title><g:message code="default.welcome.title" args="[meta(name:'app.name')]"/> </title>
	<meta name="layout" content="kickstart" />
	<z:head/>

<z:head/>
</head>

<body>
<sec:ifNotLoggedIn>
    <h4>Efectuar login ao sistema!</h4>
    <g:render template="/_menu/logoin"/>
</sec:ifNotLoggedIn>

<section class="section-table" >
	%{--<g:render template="introducao"/>--}%
	<div class="container">
		<div class="row">
			<div class="col-md-12">
                <div class="panel panel-footer">
                    <div class="panel-danger">
                        <g:link controller="credito" action="simulador"><h3 class="panel-title"><glyph:icon
                                iconName="calculator"/> Simulador de crédito</h3></g:link>
                    </div>

                </div>

            </div>
        </div>
        %{--<div class="row">
            <div class="col-md-12">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">Gráficos</h3>
                    </div>
                    <div class="panel-body">

                        <g:render template="homecharts"/>

                    </div>
                </div>
            </div>
        </div>--}%

</div>

</section>

<section>

    <sec:access expression="hasRole('HOME_INDEX')">
        <div class="row">
            <div class="col-md-12">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">Notificações</h3>
                    </div>

                    <div class="panel-body">

                        <g:render template="notas_di_dia"/>
                        <z:body/>

                    </div>
                </div>
            </div>
        </div>
    </sec:access>

</section>

</body>

</html>
