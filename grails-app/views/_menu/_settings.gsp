<li class="dropdown dropdown-btn">
    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
        <glyph:icon iconName="glyphicons_439_wrench"/>
        <g:message code="default.settings.label"/> <b></b>
    </a>

    <ul class="dropdown-menu">
        <%-- Note: Links to pages without controller are redirected in conf/UrlMappings.groovy --%>
        <li class="">
            <g:link controller="diario" target="${blank}" action="diario">Diário</g:link>
            <g:link controller="utilizador" action="utilizadorCrud" target="${blank}">Gestão de Utilizadores</g:link>
            <g:link controller="settings" action="defCredito" target="${blank}">Defenições de crédito</g:link>
            <g:link controller="entidade" action="index" target="${blank}">Minha Entidade</g:link>
            <g:link controller="entidade" action="contrato" target="${blank}">Contrato De Crédito</g:link>
            <g:link controller="taxa" action="taxas" target="${blank}">Taxas</g:link>
            <g:link controller="settings" action="index"
                    target="_blank">Selecionar Dias permitidos para a geração de crédito</g:link>
            <g:link controller="feriado" action="feriadoCrud" target="${blank}">Definição de Feriádos</g:link>
            <g:link controller="utilitarios" action="index" target="${blank}">Utilitarios</g:link>
            <g:link controller="utilitarios" action="userguide" target="${blank}">Manual do Utilizador</g:link>

        </li>

    </ul>
</li>
