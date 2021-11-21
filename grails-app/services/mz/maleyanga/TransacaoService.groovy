package mz.maleyanga

import grails.transaction.Transactional
import mz.maleyanga.transacao.Transacao

/**
 * TransacaoService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class TransacaoService {

    def save(Transacao transacao) {
        transacao.save(flush: true)
    }

    def merge(Transacao transacao) {
        transacao.merge(flush: true)
    }
}
