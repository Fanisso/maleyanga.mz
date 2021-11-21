package relatorios

import mz.maleyanga.RelatoriosService
import mz.maleyanga.SessionStorageService
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.pagamento.Pagamento
import mz.maleyanga.security.Utilizador
import org.zkoss.bind.annotation.Init

import org.zkoss.zul.ListModelList

class PrintExtratoDePrestacoesDoClienteViewModel {
    private  dataInicial
    private  dataFinal
    BigDecimal totalValorDaPrestacao
    BigDecimal totalPago
    BigDecimal totalParcial
    BigDecimal totalDivida
    BigDecimal totalMoras
    Cliente selectedCliente
    boolean excluirPagos
    boolean excluirMoras
    SessionStorageService sessionStorageService
    private ListModelList<Pagamento> pagamentos

    BigDecimal getTotalParcial() {
        return totalParcial
    }

    Utilizador getSelectedGestor() {
        return selectedGestor
    }

    void setSelectedGestor(Utilizador selectedGestor) {
        this.selectedGestor = selectedGestor
    }

    BigDecimal getTotalValorDaPrestacao() {
        return totalValorDaPrestacao
    }

    BigDecimal getTotalPago() {
        return totalPago
    }

    BigDecimal getTotalDivida() {
        return totalDivida
    }

    BigDecimal getTotalMoras() {
        return totalMoras
    }

    def getDataInicial() {

        return dataInicial
    }

    def getDataFinal() {

        return dataFinal
    }


    @Init init() {
        if(sessionStorageService.cliente!=null){
            selectedCliente = sessionStorageService.cliente as Cliente
        }
        if(sessionStorageService.dataInicial!=null){
            dataInicial = sessionStorageService.getDataInicial()
        }
        if(sessionStorageService.dataFinal!=null){
            dataFinal = sessionStorageService.getDataFinal()
        }
        if(sessionStorageService.excluirPagos!=null){
            excluirPagos = sessionStorageService.getExcluirPagos()
        }
        if(sessionStorageService.excluirMoras!=null){
            excluirMoras = sessionStorageService.getExcluirMoras()
        }

    }

    ListModelList<Pagamento> getPagamentos() {
        def pagamentoss
        if(excluirPagos){
            pagamentoss = new ListModelList<Pagamento>(Pagamento.findAllByDataPrevistoDePagamentoBetweenAndPagoAndValorDaPrestacaoLessThan(dataInicial, dataFinal,false,0.0))
        }else{
            pagamentoss = new ListModelList<Pagamento>(Pagamento.findAllByDataPrevistoDePagamentoBetweenAndValorDaPrestacaoLessThan(dataInicial, dataFinal,0.0))

        }
        if(pagamentos==null){
            pagamentos = new ListModelList<Pagamento>()
        }
        pagamentos.clear()
        for (Pagamento p in pagamentoss){
            if(p.credito.cliente.nome==selectedCliente.nome){
                if(excluirMoras){
                    if(p.valorPago<p.valorDaPrestacao*(-1)){
                        pagamentos.add(p)
                    }
                }else {
                    pagamentos.add(p)
                }

            }
        }
        calcular()
        return pagamentos
    }
    def calcular(){
        totalMoras = 0.0
        totalPago = 0.0
        totalDivida = 0.0
        totalParcial = 0.0
        totalValorDaPrestacao = 0.0
        for(Pagamento p in pagamentos){
            totalMoras += p.valorDeJurosDeDemora
            totalPago += p.valorPago
            totalParcial += p.totalPago
            totalDivida += p.totalEmDivida
            totalValorDaPrestacao +=p.valorDaPrestacao
        }
    }

}
