package entidade

import mz.maleyanga.ClienteService
import mz.maleyanga.CreditoService
import mz.maleyanga.EntidadeService
import mz.maleyanga.PedidoDeCreditoService
import mz.maleyanga.PenhoraService
import mz.maleyanga.SessionStorageService
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.credito.Credito
import mz.maleyanga.entidade.Entidade
import mz.maleyanga.pedidoDeCredito.PedidoDeCredito
import mz.maleyanga.pedidoDeCredito.Penhora
import org.zkoss.bind.annotation.Init
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zul.ListModelList


class ContratoViewModel {
    EntidadeService entidadeService
    ClienteService clienteService
    CreditoService creditoService
    @Wire  gd_show
     private Entidade entidade
    SessionStorageService sessionStorageService
    PedidoDeCreditoService pedidoDeCreditoService
    PenhoraService penhoraService

    Credito credito
    Cliente cliente
    ListModelList<Penhora> penhoras

    ListModelList<Penhora> getPenhoras() {
        penhoras = penhoraService.getPenhoras(getCliente())
        return penhoras
    }


    Credito getCredito() {
        return  creditoService.credito
    }



    @Init init() {
        System.println("dasdasdas")
        entidade = Entidade.first()
        System.println(entidade)
        def cred = sessionStorageService.getCredito() as Credito
        credito = creditoService.getCredito(cred)
    }


}
