package mz.maleyanga

import grails.transaction.Transactional
import mz.maleyanga.Taxa.Taxa
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.credito.Credito
import mz.maleyanga.pagamento.Pagamento
import mz.maleyanga.pedidoDeCredito.PedidoDeCredito
import java.sql.SQLDataException

/**
 * CreditoService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class CreditoService {
    Credito credito
    PedidoDeCredito pedidoDeCredito
    List<Pagamento> pagamentos

    Credito getCredito(Credito credito1) {
        return Credito.findById(credito1.id)
    }

    def calcularTaxaDeSeguros(Credito creditoInstance) {

        Taxa taxa = new Taxa()
        taxa.nome = "Taxa se seguros"
        taxa.valor = creditoInstance.valorCreditado * 0.04
        creditoInstance.addToTaxas(taxa)
    }

    def getEmPagamento() {
        return Credito.findAllByEstado('EmProgresso')
    }

    def getMoras() {
        return Credito.findAllByEmMoraAndEmDividaAndInvalido(true, true, false)
    }

    def getPendentes() {
        return Credito.findAllByEstado('Pendente')
    }

    def getFechados() {
        return Credito.findAllByEstadoAndInvalido('Fechado', false)
    }

    def getInvalidos() {
        return Credito.findAllByInvalido(true)
    }

    def getAbertos() {
        return Credito.findAllByEstadoAndInvalido('Aberto', false)
    }

    def findAllByCliente(Cliente cliente) {
        return Credito.findAllByClienteAndInvalido(cliente, false).sort { it.id }
    }

    def findAllByClienteAndEmDivida(Cliente cliente) {
        return Credito.findAllByClienteAndEmDividaAndInvalido(cliente, true, false).sort { it.id }
    }

    def save(Credito credito1) {
        try {
            credito1.save()
            return true
        } catch (SQLDataException e) {
            System.println(e.toString())
            return false
        }
    }

    def merge(Credito credito1) {
        try {
            credito1.merge(flush: true)
            return true
        } catch (SQLDataException e) {
            System.println(e.toString())
            return false
        }
    }

}
