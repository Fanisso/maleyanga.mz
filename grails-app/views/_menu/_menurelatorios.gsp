<ul class="nav nav-pills" data-role="listview" data-split-icon="gear" data-filter="true">
    %{--<li class="${ params.action == "create" ? 'active' : '' }">  <g:link controller="pedido" action="create"> <g:img dir="images" file="file_add.png"  uri="" />Novo Pedido</g:link></li>--}%
    %{--<li class="${params.action == "receitas" ? 'active' : ''}"><g:link controller="relatorios"
                                                                       action="receitas"><glyph:icon
                iconName="revenue"/>Recebimentos</g:link></li--}%>
    <li class="${params.action == "clientes" ? 'active' : ''}"><g:link controller="relatorios"
                                                                       action="clientes"><glyph:icon
                iconName="clientes"/>Gestores/Clientes</g:link></li>
    <li class="${params.action == "logs" ? 'active' : ''}"><g:link controller="relatorios" action="logs"><glyph:icon
            iconName="record"/>Action Logging</g:link></li>
    <li class="${params.action == "prestacoes" ? 'active' : ''}"><g:link controller="relatorios"
                                                                         action="prestacoes"><glyph:icon
                iconName="prestacoes_report"/>Prestações</g:link></li>
    <li class="${params.action == "creditos" ? 'active' : ''}"><g:link controller="relatorios"
                                                                       action="creditos"><glyph:icon
                iconName="credito"/>Creditos</g:link></li>
    <li class="${params.action == "pedidosDeCredito" ? 'active' : ''}"><g:link controller="relatorios"
                                                                               action="pedidosDeCredito"><glyph:icon
                iconName="pedido"/>Pedidos de crédito</g:link></li>
    <li class="${params.action == "diarios" ? 'active' : ''}"><g:link controller="relatorios"
                                                                      action="diarios"><glyph:icon
                iconName="diarios"/>Diários</g:link></li>
    %{--
    <li class="${ params.action == "extratoPrestacoesEmDivida" ? 'active' : '' }">  <g:link controller="relatorios" action="extratoPrestacoesEmDivida"><glyph:icon iconName="glyphicons_015_print"/>Extrato De Pagamentos por receber</g:link></li>
    --}%

    %{--<li class="${ params.action == "listAll" ? 'active' : '' }">  <g:link  controller="pedidoDeCredito" action="listAll"> <glyph:icon iconName="glyphicons_114_list"/>Todos Pedidos</g:link></li>--}%
    %{-- <g:link onclick="${'Pedidos'}" controller="pedido" action="listPendentes"> <g:img dir="images" file="all.png"  /><span class="alert-info">Pendentes</span></g:link>--}%

</ul>