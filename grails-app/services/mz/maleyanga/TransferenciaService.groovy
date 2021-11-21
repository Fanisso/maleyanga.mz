package mz.maleyanga

import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional

import mz.maleyanga.conta.Conta
import mz.maleyanga.conta.Lancamento
import mz.maleyanga.security.Utilizador
import mz.maleyanga.transacao.Transacao
import mz.maleyanga.transferencia.Transferencia

import java.sql.SQLDataException


/**
 * TransferenciaService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class TransferenciaService {
    def SpringSecurityService

    boolean salvarTransferencias(List<Transferencia> transferencias) {

        try {
            def utilizador = Utilizador.findById(springSecurityService.principal?.id)
            for (Transferencia t in transferencias) {
                t.utilizador = utilizador
                Transacao credito = new Transacao()
                Transacao debito = new Transacao()
                credito.valor = t.valor
                debito.valor = t.valor
                t.save()
                credito.credito = true
                debito.credito = false
                credito.save flush: true
                debito.save flush: true

                def credora = Conta.findById(t.origem.id)
                def devedora = Conta.findById(t.destino.id)
                if (credora.transacoes == null) {
                    credora.transacoes = new LinkedHashSet<Transacao>()
                }
                if (devedora.transacoes == null) {
                    devedora.transacoes = new LinkedHashSet<Transacao>()
                }
                credora.transacoes.add(credito)
                devedora.transacoes.add(debito)
                credito.merge(flush: true)
                devedora.merge(flush: true)
            }
            return true
        } catch (Exception e) {
            System.println(e.toString())
            return false
        }

    }

    boolean salvarTransferencia(Transferencia t) {

        try {
            BigDecimal valor = t.valor
            System.println(valor)
            Transacao tCredito = new Transacao()
            Transacao tDebito = new Transacao()
            tCredito.setValor(valor)
            tDebito.setValor(valor)
            System.println("tCredito" + tCredito.valor)
            System.println("tDebito" + tDebito.valor)
            tCredito.credito = true
            tDebito.credito = false
            tCredito.save()
            tCredito.save()
            t.save()
            def credora = Conta.findById(t.origem.id)
            def devedora = Conta.findById(t.destino.id)
            if (credora.transacoes == null) {
                credora.transacoes = new LinkedHashSet<Transacao>()
            }
            if (devedora.transacoes == null) {
                devedora.transacoes = new LinkedHashSet<Transacao>()
            }
            credora.transacoes.add(tCredito)
            devedora.transacoes.add(tDebito)
            credora.merge(flush: true)
            devedora.merge(flush: true)
            return true
        } catch (Exception e) {
            System.println(e.toString())
            return false
        }

    }

    def save(Transferencia transferencia) {
        try {
            transferencia.save(flush: true)
            return true
        } catch (SQLDataException e) {
            System.println(e.toString())
            return false
        }
    }

    def merge(Transferencia transferencia) {
        try {
            transferencia.merge(flush: true)
            return true
        } catch (SQLDataException e) {
            System.println(e.toString())
            return false
        }
    }
}
