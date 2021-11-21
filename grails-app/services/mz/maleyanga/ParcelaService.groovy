package mz.maleyanga

import grails.transaction.Transactional
import mz.maleyanga.cliente.Cliente
import mz.maleyanga.conta.Conta
import mz.maleyanga.credito.Credito
import mz.maleyanga.pagamento.Pagamento
import mz.maleyanga.pagamento.Parcela
import mz.maleyanga.saidas.Saida
import mz.maleyanga.transacao.Transacao

import java.sql.SQLDataException

/**
 * ParcelaService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class ParcelaService {
    Parcela parcelaInstance = new Parcela()
    Parcela entrada = new Parcela()
    Date dia
    Saida saidaInstance = new Saida()
    Credito creditoInstance = new Credito()

    def save(Parcela parcela) {
        try {
            parcela.save(flush: true)
            return true
        } catch (SQLDataException e) {
            System.println(e.toString())
            return false
        }

    }

    def merge(Parcela parcela) {
        try {
            parcela.merge(flush: true)
            return true
        } catch (SQLDataException e) {
            System.println(e.toString())
            return false
        }

    }

    def serviceMethod() {

    }

    boolean creditarValor(BigDecimal valor, Cliente cliente, String descricao) {
        String designacaoDaConta = 'conta_cliente_' + cliente.id
        def conta = Conta.findByDesignacaoDaConta(designacaoDaConta)
        System.println('creditarValor, conta=' + conta)
        if (conta != null) {
            Transacao transacao = new Transacao(valor: valor, descricao: descricao, credito: true).save(flush: true)
            if (conta?.transacoes == null) {
                conta.transacoes = new ArrayList<Transacao>()
            }
            conta.transacoes.add(transacao)
            conta.save(flush: true)
            return true
        }
        return false

    }

    boolean salvarParcela(Parcela parcela) {
        try {
            parcela.save()
            def parcelaDb = Parcela.findById(parcela.id)
            if (parcelaDb != null) {
                System.println("Parela gravada na base de dados!")
                return true
            } else {
                System.println("Parela não gravada na base de dados!")
                return false

            }
        } catch (Exception e) {
            System.println(e.toString())
            return false
        }

    }

    def delete(Parcela parcela) {
        try {
            parcela.delete(flush: true)
            return true
        } catch (SQLDataException e) {
            System.println(e.toString())
            return false
        }
    }

}
