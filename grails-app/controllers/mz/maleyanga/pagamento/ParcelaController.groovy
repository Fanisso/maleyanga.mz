package mz.maleyanga.pagamento

import grails.transaction.Transactional
import mz.maleyanga.BasicController
import mz.maleyanga.PagamentoService
import mz.maleyanga.ParcelaService
import mz.maleyanga.documento.Anexo
import mz.maleyanga.security.Utilizador
import org.mirzsoft.grails.actionlogging.annotation.ActionLogging
import org.mirzsoft.grails.actionlogging.annotation.SpringUserIdentification
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.*

/**
 * ParcelaController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
@Transactional(readOnly = false)
@ActionLogging
@SpringUserIdentification
class ParcelaController extends BasicController {
    PagamentoService pagamentoService
    ParcelaService parcelaService
    def sessionStorageService
    def springSecurityService
    def contaService

    static allowedMethods = [save: ["POST", "PUT"], update: ["PUT", "POST"], delete: "DELETE"]

    @Secured(['ROLE_ADMIN', 'PARCELA_INDEX'])
    def printParcela() {
        [parcela: sessionStorageService.parcela.numeroDoRecibo]
    }

    @Secured(['ROLE_ADMIN', 'PARCELA_INDEX'])
    def recibos() {

    }

    @Secured(['ROLE_ADMIN', 'PARCELA_INDEX'])
    def printEntrada() {
        [entrada: sessionStorageService.entrada.numeroDoRecibo]
    }

    @Secured(['ROLE_ADMIN', 'PARCELA_INDEX'])
    def printPrestacoesDoDia() {

    }

    def printRecibo() {

    }

    def printParcel() {

    }

    def printEntrad() {

    }


}
