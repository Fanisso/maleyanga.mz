package relatorios

import mz.maleyanga.ContaService
import mz.maleyanga.SessionStorageService
import mz.maleyanga.UtilizadorService
import mz.maleyanga.conta.Conta
import mz.maleyanga.security.Utilizador
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.zul.ListModelList

class DiariosViewModel {

    SessionStorageService sessionStorageService
    UtilizadorService utilizadorService
    private List<Utilizador> caixas
    private  Utilizador selectedCaixa
    ContaService contaService
    Date dataInicial
    Date dataFinal
    boolean exluirPagos = true
    boolean exluirMoras = true



    Date getDataInicial() {
        return dataInicial
    }

    @NotifyChange(["dataFinal","dataInicial"])
    void setDataInicial(Date dataInicial) {
        Calendar c = Calendar.getInstance()
        c.setTime(dataInicial)
        c.add(Calendar.MONTH, 1)
        c.add(Calendar.DAY_OF_MONTH,-1)
        dataFinal = c.getTime()
        this.dataInicial = dataInicial
        sessionStorageService.dataInicial = dataInicial
        sessionStorageService.dataFinal = dataFinal

    }

    boolean getExluirMoras() {
        sessionStorageService.setExluirMoras(exluirMoras)
        return exluirMoras
    }

    @NotifyChange(["exluirPagos"])
    @Command
    void excluir() {
        this.exluirPagos =!exluirPagos
        sessionStorageService.setExluirPagos(exluirPagos)
    }


    Date getDataFinal() {
        return dataFinal
    }

    void setDataFinal(Date dataFinal) {
        if(dataFinal<dataInicial){
            Calendar c = Calendar.getInstance()
            c.setTime(dataInicial)
            c.add(Calendar.DAY_OF_MONTH, 1)
            dataFinal = c.getTime()
            this.dataFinal = dataFinal

        }else {
            this.dataFinal = dataFinal
        }

        sessionStorageService.dataFinal= dataFinal
    }

    Utilizador getSelectedCaixa() {
        return selectedCaixa
    }

    void setSelectedCaixa(Utilizador selectedCaixa) {
        this.selectedCaixa = selectedCaixa
        sessionStorageService.selectedCaixa = selectedCaixa
    }

    ListModelList<Utilizador> getCaixas() {
        if (caixas==null){
            caixas = new ListModelList<Utilizador>()
        }
        caixas.clear()
        def contas = contaService.findAllContaCaixa()
        for(Conta c in contas){
           caixas.add(utilizadorService.findById(c.utilizador.id))
        }
        return caixas
    }



}
