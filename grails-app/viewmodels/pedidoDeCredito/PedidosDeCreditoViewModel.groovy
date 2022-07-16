package pedidoDeCredito

import mz.maleyanga.SessionStorageService
import mz.maleyanga.pedidoDeCredito.ListaDeDesembolso
import mz.maleyanga.pedidoDeCredito.PedidoDeCredito
import org.zkoss.bind.annotation.Init
import org.zkoss.zul.ListModelList


class PedidosDeCreditoViewModel {
    private ListaDeDesembolso listaDeDesembolso
  SessionStorageService sessionStorageService
    ListModelList<PedidoDeCredito> pedidos
    private  BigDecimal totalCreditado

    ListaDeDesembolso getListaDeDesembolso() {
        return listaDeDesembolso
    }

    BigDecimal getTotalCreditado() {
        totalCreditado = 0
        for (PedidoDeCredito pdc in pedidos){
            totalCreditado+=pdc.valorDeCredito
        }
        return totalCreditado
    }

    @Init init() {
        listaDeDesembolso = sessionStorageService.getListaDeDesembolo()
        getTotalCreditado()
    }

    ListModelList<PedidoDeCredito> getPedidos() {
        if(pedidos==null){
            pedidos = new ListModelList<>(PedidoDeCredito.findAllByListaDeDesembolso(listaDeDesembolso))
        }
        return pedidos
    }
}
