package mz.maleyanga

import mz.maleyanga.security.Utilizador

/**
 * RelatoriosService
 * A service class encapsulates the core business logic of a Grails application
 */

class RelatoriosService {
    boolean abertos
    boolean emPagamento
    boolean pendentes
    boolean fechados
    boolean invalidos
    boolean vencidos
    Date ano


    Date dataInicial
    Date dataFinal
    Utilizador selectedGestor
    Utilizador selectedCaixa
    boolean excluirPagos

    boolean getExcluirPagos() {
        return excluirPagos
    }

    void setExcluirPagos(boolean excluirPagos) {
        this.excluirPagos = excluirPagos
    }

    Utilizador getSelectedGestor() {
        return selectedGestor
    }

    void setSelectedGestor(Utilizador selectedGestor) {
        this.selectedGestor = selectedGestor
    }

    Date getDataInicial() {
        return dataInicial
    }

    void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial
    }

    Date getDataFinal() {
        return dataFinal
    }

    void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal
    }
}
