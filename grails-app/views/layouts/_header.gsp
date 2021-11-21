<header id="Header" class="-info-circle">
    <div class="container">
        <g:if test="${controllerName == 'conta'}"><br/> <br/></g:if>
        <g:if test="${controllerName == 'pagamento'}"><br/> <br/></g:if>
        <g:if test="${controllerName == 'home'}"><br/> <br/></g:if>
        <g:if test="${controllerName == 'utilizador'}"><br/> <br/></g:if>
        <g:if test="${controllerName == 'roleGroup'}"><br/> <br/></g:if>
        <g:if test="${params.controller == 'cliente'}">
            <g:if test="${params.action == 'gestorDeClientes'}">
                <br/>
                <br/>
            </g:if>
        </g:if>
        <g:if test="${params.controller == 'settings'}">
            <g:if test="${params.action == 'defCredito'}">
                <br/>
                <br/>
            </g:if>
        </g:if>
        <g:if test="${params.controller == 'credito'}">
            <g:if test="${params.action == 'credito'}">
                <br/>
                <br/>
            </g:if>
        </g:if>
        <g:if test="${params.controller == 'credito'}">
            <g:if test="${params.action == 'simulador'}">
                <br/>
                <br/>
            </g:if>
        </g:if>
        <g:if test="${params.controller == 'diario'}">
            <g:if test="${params.action == 'diario'}">
                <br/>
                <br/>
            </g:if>
        </g:if>
        <g:if test="${params.controller == 'pedidoDeCredito'}">
            <g:if test="${params.action == 'pedidoDeCredito'}">
                <br/>
                <br/>
            </g:if>
        </g:if>
        <g:if test="${params.controller == 'parcela'}">
            <g:if test="${params.action == 'recibos'}">
                <br/>
                <br/>
            </g:if>
        </g:if>
        <g:if test="${params.controller == 'parcela'}">
            <g:if test="${params.action == 'printPrestacoesDoDia'}">
                <br/>
                <br/>
            </g:if>
        </g:if>
        <g:if test="${params.controller == 'cliente'}">
            <g:if test="${params.action == 'novoCliente'}">
                <br/>
                <br/>
            </g:if>
        </g:if>
        <g:if test="${params.controller == 'feriado'}">
            <g:if test="${params.action == 'feriadoCrud'}">
                <br/>
                <br/>
            </g:if>
        </g:if>
        <g:if test="${params.controller == 'taxa'}">
            <g:if test="${params.action == 'taxas'}">
                <br/>
                <br/>
            </g:if>
        </g:if>
        <g:if test="${params.controller == 'relatorios'}">
            <g:if test="${params.action == 'prestacoes'}">
                <br/>
                <br/>
            </g:if>
        </g:if>
        <g:if test="${params.controller == 'relatorios'}">
            <g:if test="${params.action == 'diarios'}">
                <br/>
                <br/>
            </g:if>
        </g:if>
        <g:if test="${params.controller == 'relatorios'}">
            <g:if test="${params.action == 'creditos'}">
                <br/>
                <br/>
            </g:if>
        </g:if>
        <g:if test="${params.controller == 'relatorios'}">
            <g:if test="${params.action == 'clientes'}">
                <br/>
                <br/>
            </g:if>
        </g:if>
        <g:if test="${params.controller == 'entidade'}">
            <g:if test="${params.action == 'contrato'}">
                <br/>
                <br/>
            </g:if>
        </g:if>
    %{--<h5 class="title"><span class="badge"><g:layoutTitle default="${meta(name: 'app.name')}"/></span></h5>--}%
    </div>
</header>
