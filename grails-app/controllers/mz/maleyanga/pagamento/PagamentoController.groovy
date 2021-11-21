package mz.maleyanga.pagamento

import grails.transaction.Transactional
import mz.maleyanga.BasicController
import mz.maleyanga.conta.Conta
import mz.maleyanga.credito.Credito
import mz.maleyanga.security.Utilizador
import org.mirzsoft.grails.actionlogging.annotation.ActionLogging
import org.mirzsoft.grails.actionlogging.annotation.SpringUserIdentification
import org.springframework.security.access.annotation.Secured

import java.awt.List
import java.nio.ByteBuffer
import java.nio.charset.Charset

import static org.springframework.http.HttpStatus.*

/**
 * PagamentoController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
@Transactional(readOnly = false)
@ActionLogging
@SpringUserIdentification
class PagamentoController extends BasicController {
    def scaffold = Pagamento
    def pagamentoService

    static allowedMethods = [save: ["POST","PUT"], update: ["PUT","POST"], delete: "DELETE"]


    def pagamentos() {

    }



}
