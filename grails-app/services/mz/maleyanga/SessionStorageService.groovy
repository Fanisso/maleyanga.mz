package mz.maleyanga

import mz.maleyanga.cliente.Cliente
import mz.maleyanga.conta.Conta
import mz.maleyanga.credito.Credito
import mz.maleyanga.diario.Diario
import mz.maleyanga.pagamento.Pagamento
import mz.maleyanga.pagamento.Parcela
import mz.maleyanga.pedidoDeCredito.PedidoDeCredito
import mz.maleyanga.saidas.Saida
import mz.maleyanga.security.Utilizador

/**
 * SessionStorageService
 * A service class encapsulates the core business logic of a Grails application
 */
import javax.servlet.http.HttpSession
import org.springframework.web.context.request.RequestContextHolder

class SessionStorageService {
    static transactional = false
    static scope = "singleton"


    private HttpSession getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }

    def setCreditos(List<Credito> creditos) { getSession().creditos = creditos }

    def setEstado(String estado) { getSession().estado = estado }

    def getCreditos() { getSession().creditos }

    def setDiarioId(Diario diarioId) { getSession().diarioId = diarioId }

    def getDiarioId() { getSession().diarioId }


    def setDiario(Diario diario) {
        getSession().diario = diario
    }

    def getDiario() {
        getSession().diario
    }

    def setBlank(String blank) {
        getSession().blank = blank
    }

    def getBlank() {
        getSession().blank
    }


    def setEntrada(Parcela entrada) {
        getSession().entrada = entrada
    }

    def getEntrada() {
        getSession().entrada
    }

    def setDia(Date dia) { getSession().dia = dia }

    def getDia() { getSession().dia }


    def setSaidaInstance(Saida saidaInstance) { getSession().saidaInstance }

    def getSaidaInstance() { getSession().saidaInstance }

    def setCredito(Credito credito) {
        getSession().credito = credito
    }

    def getCredito() { getSession().credito }

    def setConta(Conta conta) { getSession().conta = conta }


    def getConta() { getSession().conta }

    def setCliente(Cliente cliente) { getSession().cliente = cliente }

    def getCliente() { getSession().cliente }

    def setDataInicial(Date dataInicial) { getSession().dataInicial = dataInicial }

    def getDataInicial() { getSession().dataInicial }

    def setDataFinal(Date dataFinal) { getSession().dataFinal = dataFinal }

    def getDataFinal() { getSession().dataFinal }

    def setPagamentoInstance(Pagamento pagamentoInstance) { getSession().pagamentoInstance = pagamentoInstance }

    def getPagamentoInstance() { getSession().pagamentoInstance }


    def setParcela(Parcela parcela) { getSession().parcela = parcela }

    def getParcela() { getSession().parcela }

    def setParcelaId(def parcelaId) { getSession().parcelaId = parcelaId }

    def getParcelaId() { getSession().parcelaId }

    def setUtilizador(String utilizador) { getSession().utilizador = utilizador }

    def getUtilizador() { getSession().utilizador }

    def setPedidoDeCredito(PedidoDeCredito pedidoDeCredito) { getSession().pedidoDeCredito = pedidoDeCredito }

    def getPedidoDeCredito() { getSession().pedidoDeCredito }

    def setSelectedGestor(Utilizador selectedGestor) { getSession().selectedGestor = selectedGestor }

    def getSelectedGestor() { getSession().selectedGestor }

    def setSelectedCaixa(Utilizador selectedCaixa) { getSession().selectedCaixa = selectedCaixa }

    def getSelectedCaixa() { getSession().selectedCaixa }

    def setExluirPagos(boolean excluirPagos) { getSession().excluirPagos = excluirPagos }

    def getExcluirPagos() { getSession().excluirPagos }

    def setExluirMoras(boolean excluirMoras) { getSession().excluirMoras = excluirMoras }

    def getExcluirMoras() { getSession().excluirMoras }
}
