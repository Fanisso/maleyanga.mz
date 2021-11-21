package relatorios

import mz.maleyanga.SessionStorageService
import mz.maleyanga.credito.Credito
import mz.maleyanga.pagamento.Pagamento
import mz.maleyanga.security.Utilizador
import org.zkoss.bind.annotation.Init
import org.zkoss.zul.ListModelList

class PrintExtratoDosCreditosDoGestorViewModel {
    private Utilizador selectedGestor
    SessionStorageService sessionStorageService
    private ListModelList<Credito> creditos
    private  dataInicial
    private  dataFinal
    BigDecimal totalValorCreditado
    BigDecimal totalJuros
    BigDecimal totalPago
    BigDecimal totalDivida
    BigDecimal totalDividaSemMoras

    BigDecimal getTotalDividaSemMoras() {
        return totalDividaSemMoras
    }

    Utilizador getSelectedGestor() {
        return selectedGestor
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
        if(sessionStorageService.selectedGestor!=null){
            selectedGestor = sessionStorageService.selectedGestor as Utilizador
        }

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
            creditos = new ListModelList<Credito>()
        }
        creditos.clear()
        def cre = Credito.findAllByDateConcecaoBetweenAndInvalido(dataInicial,dataFinal,false)
        for (Credito c in cre){
            if(c?.cliente?.utilizador?.username==selectedGestor.username){
                creditos.add(c)
            }
        }
        return creditos
    }
    def calcular(){
        totalValorCreditado = 0.0
        totalJuros = 0.0
        totalDivida = 0.0
        totalPago = 0.0
        totalDividaSemMoras = 0.0

        for(Credito p in getCreditos()){
            totalValorCreditado += p.valorCreditado
            totalJuros += p.valorDeJuros
            totalDivida+=p.valorEmDivida
            totalPago+=p.totalPago
            totalDividaSemMoras+=p.totalDaDividaSemMoras
        }
    }

    BigDecimal getTotalJuros() {
        return totalJuros
    }

}
