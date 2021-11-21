package mz.maleyanga

import grails.transaction.Transactional
import mz.maleyanga.pagamento.Remissao
import mz.maleyanga.transferencia.Transferencia

import java.sql.SQLDataException

/**
 * RemissaoService
 * A service class encapsulates the core business logic of a Grails application
 */
@Transactional
class RemissaoService {

    def save(Remissao remissao) {
        try {
            remissao.save()
            return true
        } catch (SQLDataException e) {
            System.println(e.toString())
            return false
        }
    }

    def merge(Remissao remissao) {
        try {
            remissao.save()
            return true
        } catch (SQLDataException e) {
            System.println(e.toString())
            return false
        }
    }
}
