<div class="">
    <ul class="nav nav-tabs " data-role="listview" data-split-icon="gear" data-filter="true">
        <li class="${params.controller == "cliente" ? 'active' : ''}">
            <g:link controller="cliente" action="index" target="${blank}"><glyph:icon
                    iconName="clientes"/>Clientes</g:link>
        </li>

        <li class="${params.controller == "credito" ? 'active' : ''}">
            <g:link controller="credito" action="credito" target="${blank}"><glyph:icon
                    iconName="credito"/>Créditos</g:link>
        </li>
        <li class="${params.controller == "pagamento" ? 'active' : ''}">
            <g:link controller="pagamento" action="pagamentos" target="${blank}"><glyph:icon
                    iconName="caixa"/>Caixa</g:link>
        </li>
        <li class="${params.controller == "diario" ? 'active' : ''}">
            <g:link controller="diario" action="diario" target="${blank}"><glyph:icon
                    iconName="diarios"/>Diário</g:link>
        </li>

        <li class="${params.controller == "pedidoDeCredito" ? 'active' : ''}">
            <g:link controller="pedidoDeCredito" action="index" target="${blank}"><glyph:icon
                    iconName="pedido"/>Pedidos de Créditos</g:link>
        </li>
        <li class="${params.controller == "conta" ? 'active' : ''}">
            <g:link controller="conta" action="contas" target="${blank}"><glyph:icon iconName="contas"/>Contas</g:link>
        </li>
        <li class="${params.controller == "relatorios" ? 'active' : ''}">
            <g:link controller="relatorios" action="index" target="${blank}"><glyph:icon
                    iconName="relatorios_32"/>Relatórios</g:link>
        </li>
        <g:render template="/_menu/user"/>
    </ul>
</div>
