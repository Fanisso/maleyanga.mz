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
    BigDecimal totalDeTotalMoras

    BigDecimal getTotalDeTotalMoras() {
        return totalDeTotalMoras
    }

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
        calcular()
    }

    ListModelList<Credito> getCreditos() {

        if(creditos==null){
            creditos = new ListModelList<Credito>(Credito.findAllByDateConcecaoBetweenAndInvalido(dataInicial,dataFinal,false))
        }
        return creditos
    }
    def calcular(){

        totalValorCreditado = 0.0
        totalJuros = 0.0
        totalDivida = 0.0
        totalPago = 0.0
        totalDividaSemMoras = 0.0
        totalMoras = 0.0
        totalDeTotalMoras = 0.0
        def creditoss = Credito.findAllByDateConcecaoBetweenAndInvalido(dataInicial,dataFinal,false)
        
        for(Credito c in creditoss){
            totalValorCreditado += c?.valorCreditado
            totalJuros += c?.valorDeJuros
            totalPago+=c?.totalPago
            totalDivida+=c?.valorEmDivida

            totalDividaSemMoras+=c?.totalDaDividaSemMoras
            totalDeTotalMoras+=c?.totalMoras


        }
    }

    BigDecimal getTotalJuros() {
        return totalJuros
    }
}
