package mz.maleyanga

import grails.transaction.Transactional
import mz.maleyanga.entidade.Entidade

/**
 * EntidadeService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class EntidadeService {
    Entidade entidade

    def getEntidade() {
        entidade = Entidade.findAll().first()
        return entidade
    }
}
