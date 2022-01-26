package credito

import mz.maleyanga.CurrencyWriter
import mz.maleyanga.PenhoraService
import mz.maleyanga.SessionStorageService
import mz.maleyanga.credito.Credito
import mz.maleyanga.entidade.Entidade
import mz.maleyanga.pedidoDeCredito.PedidoDeCredito
import mz.maleyanga.pedidoDeCredito.Penhora
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Init
import org.zkoss.zul.ListModelList


@Service
class PrintContratoDeCreditoViewModel {
    SessionStorageService sessionStorageService
    private  Credito credito
    String valorCreditadoPorExtenso
    String prestacao
    String representante
    String numeroDeRegisto
    private  PedidoDeCredito pedidoDeCredito
    BigDecimal valorDaPrestacao
    BigDecimal valorAPagar
    String valorAPagarExt
    String valorDaPrestacaoExt
    PenhoraService penhoraService

    CurrencyWriter currencyWriter = CurrencyWriter.getInstance()

    String getRepresentante() {
        if(entidade.genero=="femenino"){
            representante=", neste acto representada pela Senhora "
        }else {
            representante  = ", neste acto representado pela Senhor "
        }
        return representante
    }

    String getNumeroDeRegisto() {
        if(entidade.numeroDeRegisto.empty){
            return ""
        }else
        return ""
    }

    String getPrestacao() {
        if(credito.numeroDePrestacoes==1){
            prestacao = " prestação"
        }else {
            prestacao = " prestações"
        }
        return prestacao
    }

    String getValorAPagarExt() {
        return currencyWriter.write(getValorAPagar())
    }

    BigDecimal getValorAPagar() {
        return getValorDaPrestacao()*credito.numeroDePrestacoes
    }
    ListModelList<Penhora> penhoras

    String getValorDaPrestacaoExt() {
        return currencyWriter.write(getValorDaPrestacao())
    }

    BigDecimal getValorDaPrestacao() {
        return credito.pagamentos.first().valorDaPrestacao*(-1)
    }

    PedidoDeCredito getPedidoDeCredito() {
        pedidoDeCredito = PedidoDeCredito?.findByCredito(credito)
        if(pedidoDeCredito==null){
            pedidoDeCredito = PedidoDeCredito.findByCliente(credito.cliente)
        }
        return pedidoDeCredito
    }

    String getValorCreditadoPorExtenso() {
        return currencyWriter.write(credito.valorCreditado)
    }
    private  Entidade entidade = Entidade.all.first()

    Entidade getEntidade() {
        return entidade
    }

    ListModelList<Penhora> getPenhoras() {
        if(penhoras==null){
            penhoras = penhoraService.getPenhoras(credito.cliente)
        }
        return penhoras
    }

    Credito getCredito() {
        return credito
    }

    @Init init() {
        credito = Credito.findById(sessionStorageService?.credito?.id)
    }



}
