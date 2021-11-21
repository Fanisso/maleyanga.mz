package relatorios

import mz.maleyanga.SessionStorageService
import mz.maleyanga.credito.Credito
import mz.maleyanga.pagamento.Pagamento
import org.springframework.stereotype.Service
import org.zkoss.bind.annotation.Init
import org.zkoss.zul.ListModelList
@Service
class PrintExtratoDosCreditosViewModel {
    SessionStorageService sessionStorageService
    private ListModelList<Credito> creditos
    private  dataInicial
    private  dataFinal
    BigDecimal totalValorCreditado
    BigDecimal totalJuros
    BigDecimal totalPago
    BigDecimal totalDivida
    BigDecimal totalDividaSemMoras
    BigDecimal totalMoras



    BigDecimal getTotalValorDaPrestacao() {
        return totalValorDaPrestacao
    }

    BigDecimal getTotalPago() {
        return totalPago
    }

    BigDecimal getTotalDividaSemMoras() {
        return totalDividaSemMoras
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

        if(sessionStorageService.dataInicial!=null){
            dataInicial = sessionStorageService.getDataInicial()
        }
        if(sessionStorageService.dataFinal!=null){
            dataFinal = sessionStorageService.getDataFinal()
        }

    }

    ListModelList<Pagamento> getCreditos() {

        if(creditos==null){
            creditos = new ListModelList<Credito>(Credito.findAllByDateConcecaoBetweenAndInvalido(dataInicial,dataFinal,false))
        }
        calcular()
        return creditos
    }
    def calcular(){
        totalValorCreditado = 0.0
        totalJuros = 0.0
        totalDivida = 0.0
        totalPago = 0.0
        totalDividaSemMoras = 0.0
        totalMoras = 0.0

        for(Credito p in creditos){
            p.getTotalMoras()
            totalValorCreditado += p.valorCreditado
            totalJuros += p.valorDeJuros
            totalPago+=p.totalPago
            totalDivida+=p.valorEmDivida
            totalDividaSemMoras+=p.totalDaDividaSemMoras

        }
    }

    BigDecimal getTotalJuros() {
        return totalJuros
    }
}
