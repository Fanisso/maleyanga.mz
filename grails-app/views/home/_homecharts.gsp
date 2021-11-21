
<div id="myPieChart"/>
<%@ page import="mz.maleyanga.credito.Credito" %>
<%@ page import="mz.maleyanga.pagamento.Pagamento" %>
<%@ page import="mz.maleyanga.pedidoDeCredito.PedidoDeCredito" %>
<%

    def labels = ['Abertos','Pendentes','Fechados']
    def labels1 = ['Prestações Pagas','Prestações em dívida']

    def colors = ['00ff00','FF0000','0000ff']
    def colors6 = ['00ff00','FF0000','0000ff','ff5733','d68910']
    def colors3 = ['800000','808080']
    def creditos = Credito.all
    def pagamentos = Pagamento.all
    def pedidos = PedidoDeCredito.all
    def abertos = creditos.count {it.estado.equals('Aberto')}
    def pendentes = creditos.count {it.estado.equals('Pendente')}
    def fechados = creditos.count {it.estado.equals('Fechado')}
    def values = [abertos,pendentes,fechados]
    def pagos = pagamentos.count {it.pago}
    def npagos = pagamentos.count {!it.pago}
    def values3 = [pagos , npagos]
    def pedidosAbertos  = pedidos.count {it.estado.equals('Aberto')}
    def pedidosPendentes = pedidos.count {it.estado.equals('Pendente')}
    def pedidosAprovados = pedidos.count {it.estado.equals('Aprovado')}
    def pedidosReprovados = pedidos.count {it.estado.equals('Reprovado')}
    def values6 = [pedidosAbertos,pedidosPendentes,pedidosAprovados,pedidosAprovados,pedidosReprovados]

%>



<g:pieChart title='Situação geral dos créditos' colors="${colors}"
            labels="${labels}" fill="${'bg,s,efefef'}" dataType='simple' data='${values}' />


<g:pieChart title='Situação geral de pagamento das prestações' colors="${colors3}"
               labels="${labels1}"  fill="${'bg,s,efefef'}" dataType='extended' data='${values3}'  />










