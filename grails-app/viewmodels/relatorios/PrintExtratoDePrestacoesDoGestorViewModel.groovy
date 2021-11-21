package relatorios

import mz.maleyanga.RelatoriosService
import mz.maleyanga.SessionStorageService
import mz.maleyanga.pagamento.Pagamento
import mz.maleyanga.security.Utilizador
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Init
import org.zkoss.zul.ListModelList
@Service
class PrintExtratoDePrestacoesDoGestorViewModel {

    private  dataInicial
    private  dataFinal
    BigDecimal totalValorDaPrestacao
    BigDecimal totalPago
    BigDecimal totalDivida
    BigDecimal totalMoras
    Utilizador selectedGestor
    boolean excluirPagos
    boolean excluirMoras
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
    RelatoriosService relatoriosService
    SessionStorageService sessionStorageService
    private ListModelList<Pagamento> pagamentos
    @Init init() {
        if(sessionStorageService.selectedGestor!=null){
            selectedGestor = sessionStorageService.selectedGestor as Utilizador
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
            if(p.credito.cliente.utilizador?.username==selectedGestor?.username){
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
        totalValorDaPrestacao = 0.0
        for(Pagamento p in pagamentos){
            totalMoras += p.valorDeJurosDeDemora
            totalPago += p.valorPago
            totalDivida += p.totalEmDivida
            totalValorDaPrestacao +=p.valorDaPrestacao
        }
    }

}
