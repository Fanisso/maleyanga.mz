package mz.maleyanga

import grails.transaction.Transactional
import mz.maleyanga.contrato.Contrato

import java.sql.SQLException

/**
 * ContratoService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class ContratoService {

    def saveContrato(Contrato contrato) {
        try {
            contrato.save(flush: true)
        } catch (SQLException e) {
            System.println(e.toString())
        }
    }
}
