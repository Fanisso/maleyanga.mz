package mz.maleyanga

import grails.transaction.Transactional
import mz.maleyanga.saidas.Saida

import java.sql.SQLDataException

/**
 * SaidaService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class SaidaService {

    def serviceMethod() {

    }

    def save(Saida saida) {
        try {
            saida.save(flush: true)
            return true
        } catch (SQLDataException e) {
            System.println(e.toString())
            return false
        }
    }

    def merge(Saida saida) {
        try {
            saida.merge(flush: true)
            return true
        } catch (SQLDataException e) {
            System.println(e.toString())
            return false
        }
    }

    def delete(Saida saida) {
        try {
            saida.delete()
            return true
        } catch (SQLDataException e) {
            System.println(e.toString())
            return false
        }
    }
}
