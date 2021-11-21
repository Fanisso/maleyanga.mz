package mz.maleyanga.pagamento

import grails.transaction.Transactional
import mz.maleyanga.BasicController
import mz.maleyanga.security.Utilizador
import org.springframework.security.access.annotation.Secured

import static org.springframework.http.HttpStatus.*

/**
 * RemissaoController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
@Transactional(readOnly = true)
class RemissaoController extends BasicController {
    def springSecurityService
    static allowedMethods = [save: ["POST", "PUT"], update: ["PUT", "POST"], delete: "DELETE"]




}
